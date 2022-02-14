package com.haafiz.etcd;

import io.etcd.jetcd.ByteSequence;
import io.etcd.jetcd.Client;
import io.etcd.jetcd.ClientBuilder;
import io.etcd.jetcd.KV;
import io.etcd.jetcd.KeyValue;
import io.etcd.jetcd.Lease;
import io.etcd.jetcd.Lock;
import io.etcd.jetcd.Watch;
import io.etcd.jetcd.kv.GetResponse;
import io.etcd.jetcd.kv.PutResponse;
import io.etcd.jetcd.lease.LeaseGrantResponse;
import io.etcd.jetcd.lease.LeaseKeepAliveResponse;
import io.etcd.jetcd.lease.LeaseRevokeResponse;
import io.etcd.jetcd.lease.LeaseTimeToLiveResponse;
import io.etcd.jetcd.lock.LockResponse;
import io.etcd.jetcd.lock.UnlockResponse;
import io.etcd.jetcd.options.DeleteOption;
import io.etcd.jetcd.options.GetOption;
import io.etcd.jetcd.options.LeaseOption;
import io.etcd.jetcd.options.PutOption;
import io.etcd.jetcd.options.WatchOption;
import io.etcd.jetcd.support.CloseableClient;
import io.etcd.jetcd.watch.WatchEvent;
import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author Anthony
 * @create 2022/2/14
 * @desc
 **/
@Slf4j
public class EtcdClientImpl implements EtcdClient{
    private static final int LEASE_TIME = 5;

    private Client client;

    private KV kvClient;

    private Lease leaseClient;

    private Lock lockClient;

    private Watch watchClient;

    private long leaseId;

    private HeartBeatLeaseTimeoutListener heartBeatLeaseTimeoutListener;

    private volatile ConcurrentHashMap<String, EtcdWatcher> etcdWatchers = new ConcurrentHashMap<String, EtcdClientImpl.EtcdWatcher>();

    private static final String ETCD_BASE_COUNTDOWN_LATCHER = "__base__v3";

    private AtomicBoolean isCreated = new AtomicBoolean(false);


    public EtcdClientImpl(String address) {
        this(address, false, null, null, null, null);
    }

    public EtcdClientImpl(String address, boolean usedHeartBeatLease) {
        this(address, usedHeartBeatLease, null, null, null, null);
    }

    public EtcdClientImpl(String address, boolean usedHeartBeatLease, String loadBalancerPolicy) {
        this(address, usedHeartBeatLease, loadBalancerPolicy, null, null, null);
    }

    public EtcdClientImpl(String address, boolean usedHeartBeatLease, String loadBalancerPolicy, String authority, String username, String password) {
        if(isCreated.compareAndSet(false, true)) {
            ClientBuilder clientBuilder = Client.builder();
            String[] addressList = address.split(",");
            clientBuilder.endpoints(addressList);
            if(StringUtils.isNotBlank(loadBalancerPolicy)) {
                clientBuilder.loadBalancerPolicy(loadBalancerPolicy);
            } else {
                clientBuilder.loadBalancerPolicy();
            }
            if(StringUtils.isNoneBlank(authority) && StringUtils.isNoneBlank(username) && StringUtils.isNoneBlank(password)) {
                clientBuilder.authority(authority);
                clientBuilder.user(ByteSequence.from(username, Charset.defaultCharset()));
                clientBuilder.password(ByteSequence.from(username, Charset.defaultCharset()));
            }
            this.client = clientBuilder.build();
            this.kvClient = client.getKVClient();
            this.leaseClient = client.getLeaseClient();
            this.watchClient = client.getWatchClient();
            this.lockClient = client.getLockClient();

            try {
                // first as countDownLatch init
                KeyValue kv = getKey(ETCD_BASE_COUNTDOWN_LATCHER);
                if(kv == null) {
                    putKey(ETCD_BASE_COUNTDOWN_LATCHER, ETCD_BASE_COUNTDOWN_LATCHER);
                }
                log.info("#EtcdClientImpl#Constructor Init ok!");
                //	second used HeartBeat lease
                if(usedHeartBeatLease) {
                    this.leaseId = this.generatorLeaseId(LEASE_TIME);

                    this.keepAliveSingleLease(leaseId, new StreamObserver<LeaseKeepAliveResponse>() {

                        @Override
                        public void onNext(LeaseKeepAliveResponse value) {
//							log.info("#EtcdClientImpl.keepAliveSingleLease# onNext, leaseId: {} ttl: {} !", value.getID(), value.getTTL());
                        }

                        @Override
                        public void onError(Throwable t) {
                            log.error("#EtcdClientImpl.keepAliveSingleLease# onError !", t);
                        }

                        @Override
                        public void onCompleted() {
                            log.error("#EtcdClientImpl.keepAliveSingleLease# onCompleted !");
                            heartBeatLeaseTimeoutListener.timeoutNotify();
                        }
                    });

                    log.info("#EtcdClientImpl.HeartBeatLease# heartbeat lease thread is running, leaseId: {} !", leaseId);
                }
            } catch (Exception e) {
                log.error("#EtcdClientImpl#Constructor Init Error, execute close ! ", e);
                this.close();
                log.info("#EtcdClientImpl#Constructor Init Error, execute close ok ! ", e);
            } finally {
                Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                    this.close();
                    log.info("#EtcdClientImpl# EtcdClient Shutdown Hook ok!");
                }));
            }
        }
    }


    @Override
    public long getHeartBeatLeaseId() throws InterruptedException {
        return this.leaseId;
    }

    @Override
    public void putKey(String key, String value) throws Exception {
        CompletableFuture<io.etcd.jetcd.kv.PutResponse> completableFuture = kvClient.put(ByteSequence.from(key, Charset.defaultCharset()), ByteSequence.from(value.getBytes(CHARSET)));
        completableFuture.get();
    }

    @Override
    public CompletableFuture<PutResponse> putKeyCallFuture(String key, String value) throws Exception {
        return kvClient.put(ByteSequence.from(key, Charset.defaultCharset()), ByteSequence.from(value.getBytes(CHARSET)));
    }

    @Override
    public KeyValue getKey(final String key) throws Exception {
        GetResponse getResponse = kvClient.get(ByteSequence.from(key, Charset.defaultCharset())).get();
        if(getResponse == null) {
            return null;
        }
        List<KeyValue> kvs = getResponse.getKvs();
        if (kvs.size() > 0) {
            return kvs.get(0);
        } else {
            return null;
        }
    }

    @Override
    public void deleteKey(String key) {
        kvClient.delete(ByteSequence.from(key, Charset.defaultCharset()));
    }

    @Override
    public List<KeyValue> getKeyWithPrefix(String prefix) {
        List<KeyValue> keyValues = new ArrayList<>();
        GetOption getOption = GetOption.newBuilder().withPrefix(ByteSequence.from(prefix, Charset.defaultCharset())).build();
        try {
            keyValues = kvClient.get(ByteSequence.from(prefix, Charset.defaultCharset()), getOption).get().getKvs();
        } catch (Exception e) {
            throw new RuntimeException("#EtcdClientImpl.getKeyWithPrefix# Error: " + e.getMessage(), e);
        }
        return keyValues;
    }

    @Override
    public void deleteKeyWithPrefix(String prefix) {
        DeleteOption deleteOption = DeleteOption.newBuilder().withPrefix(ByteSequence.from(prefix, Charset.defaultCharset())).build();
        kvClient.delete(ByteSequence.from(prefix, Charset.defaultCharset()), deleteOption);
    }


    private long putKeyWithPrivateLease(String key, String value, long expireTime) {
        CompletableFuture<LeaseGrantResponse> leaseGrantResponse = leaseClient.grant(expireTime);
        PutOption putOption;
        try {
            putOption = PutOption.newBuilder().withLeaseId(leaseGrantResponse.get().getID()).build();
            kvClient.put(ByteSequence.from(key, Charset.defaultCharset()), ByteSequence.from(value, Charset.defaultCharset()), putOption);
            return leaseGrantResponse.get().getID();
        } catch (Exception e) {
            throw new RuntimeException("#EtcdClientImpl.putKeyWithPrivateLease# Error: " + e.getMessage(), e);
        }
    }


    private long putKeyWithPublicLease(String key, String value, long leaseId) throws Exception {
        PutOption putOption = PutOption.newBuilder().withLeaseId(leaseId).build();
        CompletableFuture<PutResponse> putResponse = kvClient.put(ByteSequence.from(key, Charset.defaultCharset()), ByteSequence.from(value, Charset.defaultCharset()), putOption);
        try {
            return putResponse.get().getHeader().getRevision();
        } catch (Exception e) {
            throw new RuntimeException("#EtcdClientImpl.putKeyWithPublicLease# Error: " + e.getMessage(), e);
        }
    }

    @Override
    public long putKeyWithExpireTime(String key, String value, long expireTime) {
        return putKeyWithPrivateLease(key, value, expireTime);
    }

    @Override
    public long putKeyWithLeaseId(String key, String value, long leaseId) throws Exception{
        return putKeyWithPublicLease(key, value, leaseId);
    }

    @Override
    public long generatorLeaseId(long expireTime) throws Exception {
        CompletableFuture<LeaseGrantResponse> leaseGrantResponse = leaseClient.grant(expireTime);
        return leaseGrantResponse.get().getID();
    }

    @Override
    public CloseableClient keepAliveSingleLease(long leaseId, StreamObserver<LeaseKeepAliveResponse> observer) {
        return leaseClient.keepAlive(leaseId, observer);
    }

    @Override
    public LeaseKeepAliveResponse keepAliveOnce(long leaseId) throws InterruptedException, ExecutionException {
        CompletableFuture<LeaseKeepAliveResponse> completableFuture = leaseClient.keepAliveOnce(leaseId);
        if(completableFuture != null) {
            return completableFuture.get();
        } else {
            throw new EtcdResponseNullPointerException();
        }
    }

    @Override
    public LeaseKeepAliveResponse keepAliveOnce(long leaseId, long timeout) throws InterruptedException, ExecutionException, TimeoutException {
        CompletableFuture<LeaseKeepAliveResponse> completableFuture = leaseClient.keepAliveOnce(leaseId);
        if(completableFuture != null) {
            return completableFuture.get(timeout, TimeUnit.MILLISECONDS);
        } else {
            throw new EtcdResponseNullPointerException();
        }
    }

    @Override
    public LeaseTimeToLiveResponse timeToLiveLease(long leaseId) throws InterruptedException, ExecutionException {
        CompletableFuture<LeaseTimeToLiveResponse> completableFuture = leaseClient.timeToLive(leaseId, LeaseOption.newBuilder().withAttachedKeys().build());
        if(completableFuture != null) {
            return completableFuture.get();
        } else {
            throw new EtcdResponseNullPointerException();
        }
    }

    @Override
    public LeaseRevokeResponse revokeLease(long leaseId) throws InterruptedException, ExecutionException {
        CompletableFuture<LeaseRevokeResponse> completableFuture = leaseClient.revoke(leaseId);
        if(completableFuture != null) {
            return completableFuture.get();
        } else {
            throw new EtcdResponseNullPointerException();
        }
    }

    @Override
    public LockResponse lock(String lockName) throws Exception {
        CompletableFuture<LockResponse> completableFuture = lockClient.lock(ByteSequence.from(lockName, Charset.defaultCharset()), 0);
        if(completableFuture != null) {
            return completableFuture.get();
        } else {
            throw new EtcdResponseNullPointerException();
        }
    }

    @Override
    public LockResponse lock(String lockName, long expireTime) throws Exception {
        CompletableFuture<LeaseGrantResponse> leaseGrantResponse = leaseClient.grant(expireTime);
        CompletableFuture<LockResponse> completableFuture = lockClient.lock(ByteSequence.from(lockName, Charset.defaultCharset()), leaseGrantResponse.get().getID());
        if(completableFuture != null) {
            return completableFuture.get();
        } else {
            throw new EtcdResponseNullPointerException();
        }
    }

    @Override
    public UnlockResponse unlock(String lockName) throws Exception {
        CompletableFuture<UnlockResponse> completableFuture = lockClient.unlock(ByteSequence.from(lockName, Charset.defaultCharset()));
        if(completableFuture != null) {
            return completableFuture.get();
        } else {
            throw new EtcdResponseNullPointerException();
        }
    }

    @Override
    public LockResponse lockByLeaseId(String lockName, long leaseId) throws Exception {
        CompletableFuture<LockResponse> completableFuture = lockClient.lock(ByteSequence.from(lockName, Charset.defaultCharset()), leaseId);
        if(completableFuture != null) {
            return completableFuture.get();
        } else {
            throw new EtcdResponseNullPointerException();
        }
    }


    @Override
    public synchronized void addWatcherListener(final String watcherKey, final boolean usePrefix, WatcherListener watcherListener) {
        EtcdWatcher etcdWatcher = etcdWatchers.get(watcherKey);
        if(etcdWatcher == null) {
            etcdWatcher = new EtcdWatcher(EtcdClientImpl.this, watcherKey, usePrefix, watcherListener);
            etcdWatcher.start();
            etcdWatchers.putIfAbsent(watcherKey, etcdWatcher);
            log.info("addWatcherListener watcherKey : {}, usePrefix : {}", watcherKey, usePrefix);
        }
    }

    @Override
    public synchronized void removeWatcherListener(final String watcherKey) {
        EtcdWatcher etcdWatcher = etcdWatchers.get(watcherKey);
        if(etcdWatcher != null) {
            etcdWatcher.stop();
            etcdWatchers.remove(etcdWatcher.getWatcherKey());
            log.info("removeWatcherListener watcherKey : {}", etcdWatcher.getWatcherKey());
        }
    }


    private class EtcdWatcher {

        private EtcdClient etcdClient;

        private final String watcherKey ;

        private final Watch.Watcher watcher;

        private final WatcherListener watcherListener;

        public EtcdWatcher(EtcdClient etcdClient, final String watcherKey, final boolean usePrefix, WatcherListener watcherListener) {
            this.etcdClient = etcdClient;
            this.watcherKey = watcherKey;
            this.watcherListener = watcherListener;

            Watch.Listener listener  = Watch.listener(response -> {
                try {
                    List<WatchEvent> watcherList = response.getEvents();
                    for(WatchEvent watchEvent : watcherList) {
                        KeyValue prevKeyValue = watchEvent.getPrevKV();
                        KeyValue curtkeyValue = watchEvent.getKeyValue();
                        switch (watchEvent.getEventType()) {
                            case PUT:
                                this.watcherListener.watcherKeyChanged(this.etcdClient, new EtcdChangedEvent(
                                        prevKeyValue,
                                        curtkeyValue,
                                        EtcdChangedEvent.Type.PUT));
                                break;
                            case DELETE:
                                this.watcherListener.watcherKeyChanged(this.etcdClient, new EtcdChangedEvent(
                                        prevKeyValue,
                                        curtkeyValue,
                                        EtcdChangedEvent.Type.DELETE));
                                break;
                            case UNRECOGNIZED:
                                // ignore
                                log.warn("#EtcdClientImpl.EtcdWatcher# watched UNRECOGNIZED Warn, Type: {} ", EtcdChangedEvent.Type.UNRECOGNIZED);
//									watcherListener.watcherKeyChanged(etcdClient, new EtcdChangedEvent(
//											keyValue.getKey().toStringUtf8(),
//											keyValue.getValue().toStringUtf8(),
//											EtcdChangedEvent.Type.UNRECOGNIZED));
                                break;
                            default:
                                break;
                        }
                    }
                } catch (InterruptedException e) {
                    log.warn("#EtcdClientImpl.EtcdWatcher# watcher running thread is Warn, catch InterruptedException! ", e);
                    // ignore
                } catch (Throwable e) {
                    log.error("#EtcdClientImpl.EtcdWatcher# watcher running thread is Error, catch Throwable! ", e);
                    // ignore
                }
            });

            if (usePrefix) {
                watcher = watchClient.watch(ByteSequence.from(watcherKey, Charset.defaultCharset()),
                        WatchOption.newBuilder().withPrefix(ByteSequence.from(watcherKey, Charset.defaultCharset())).build(),
                        listener);
            } else {
                watcher = watchClient.watch(ByteSequence.from(watcherKey, Charset.defaultCharset()),
                        listener);
            }
        }

        public void start() {
            //	ignore
        }

        public void stop() {
            this.watcher.close();
        }

        public String getWatcherKey() {
            return watcherKey;
        }
    }

    @Override
    public void close() {
        for(Map.Entry<String, EtcdWatcher> me : etcdWatchers.entrySet()){
            EtcdWatcher etcdWatcher = me.getValue();
            etcdWatcher.stop();
        }
        if(client != null) {
            client.close();
        }
    }

    @Override
    public void addHeartBeatLeaseTimeoutNotifyListener(HeartBeatLeaseTimeoutListener heartBeatLeaseTimeoutListener) {
        this.heartBeatLeaseTimeoutListener = heartBeatLeaseTimeoutListener;
    }

}
