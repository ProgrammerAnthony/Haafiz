package com.haafiz.discovery;

import com.haafiz.common.util.Pair;
import com.haafiz.discovery.api.Notify;
import com.haafiz.discovery.api.Registry;
import com.haafiz.discovery.api.RegistryService;
import com.haafiz.etcd.EtcdChangedEvent;
import com.haafiz.etcd.EtcdClient;
import com.haafiz.etcd.EtcdClientImpl;
import com.haafiz.etcd.HeartBeatLeaseTimeoutListener;
import com.haafiz.etcd.WatcherListener;
import io.etcd.jetcd.KeyValue;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Anthony
 * @create 2022/2/14
 * @desc
 **/
@Slf4j
public class RegistryServiceEtcdImpl implements RegistryService {

    private EtcdClient etcdClient;

    private Map<String, String> cachedMap = new HashMap<String, String>();


    @Override
    public void initialized(String registryAddress) {
        etcdClient = new EtcdClientImpl(registryAddress,
                true,
                "",
                null,
                null,
                null);

        etcdClient.addHeartBeatLeaseTimeoutNotifyListener(new HeartBeatLeaseTimeoutListener() {
            @Override
            public void timeoutNotify() {
                cachedMap.forEach((key, value) ->{
                    try {
                        registerEphemeralNode(key, value);
                    } catch (Exception e) {
                        //	ignore
                        log.error("#RegistryServiceEtcdImpl.initialized# HeartBeatLeaseTimeoutListener: timeoutNotify is error", e);
                    }
                });
            }
        });
    }

    @Override
    public void registerPathIfNotExist(String path, String value, boolean isPersistent) throws Exception {
        if(!isExistKey(path)) {
            if(isPersistent) {
                registerPersistentNode(path, value);
            } else {
                registerEphemeralNode(path, value);
            }
        }
    }

    @Override
    public long registerEphemeralNode(String key, String value) throws Exception {
        long leaseId = this.etcdClient.getHeartBeatLeaseId();
        cachedMap.put(key, value);
        return this.etcdClient.putKeyWithLeaseId(key, value, leaseId);
    }

    @Override
    public void registerPersistentNode(String key, String value) throws Exception {
        this.etcdClient.putKey(key, value);
    }

    @Override
    public List<Pair<String, String>> getListByPrefixKey(String prefix) throws Exception {
        List<KeyValue> list = this.etcdClient.getKeyWithPrefix(prefix);
        List<Pair<String, String>> result = new ArrayList<Pair<String, String>>();
        for(KeyValue kv: list) {
            result.add(new Pair<String, String>(kv.getKey().toString(Charset.defaultCharset()),
                    kv.getValue().toString(Charset.defaultCharset())));
        }
        return result;
    }

    @Override
    public Pair<String, String> getByKey(String key) throws Exception {
        KeyValue kv = etcdClient.getKey(key);
        return new Pair<String, String>(kv.getKey().toString(Charset.defaultCharset()),
                kv.getValue().toString(Charset.defaultCharset()));
    }

    @Override
    public boolean isExistKey(String key) throws Exception {
        KeyValue kv = etcdClient.getKey(key);
        if(kv == null) {
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void deleteByKey(String key) {
        etcdClient.deleteKey(key);
    }

    @Override
    public void close() {
        if(etcdClient != null) {
            etcdClient.close();
        }
    }

    @Override
    public void addWatcherListeners(String superPath, Notify notify) {
        etcdClient.addWatcherListener(superPath + Registry.SERVICE_PREFIX, true, new InnerWatcherListener(notify));
        etcdClient.addWatcherListener(superPath + Registry.INSTANCE_PREFIX, true, new InnerWatcherListener(notify));
        etcdClient.addWatcherListener(superPath + Registry.RULE_PREFIX, true, new InnerWatcherListener(notify));
        //	gateway service changed：
        etcdClient.addWatcherListener(superPath + Registry.GATEWAY_PREFIX, true, new InnerWatcherListener(notify));
    }

    static class InnerWatcherListener implements WatcherListener {

        private final Notify notify;

        public InnerWatcherListener(Notify notify) {
            this.notify = notify;
        }

        @Override
        public void watcherKeyChanged(EtcdClient etcdClient, EtcdChangedEvent event) throws Exception {
            EtcdChangedEvent.Type type = event.getType();
            KeyValue curtKeyValue = event.getCurtkeyValue();
            switch (type) {
                case PUT:
                    notify.put(curtKeyValue.getKey().toString(Charset.defaultCharset()),
                            curtKeyValue.getValue().toString(Charset.defaultCharset()));
                    break;
                case DELETE:
                    notify.delete(curtKeyValue.getKey().toString(Charset.defaultCharset()));
                    break;
                default:
                    break;
            }
        }

    }



}
