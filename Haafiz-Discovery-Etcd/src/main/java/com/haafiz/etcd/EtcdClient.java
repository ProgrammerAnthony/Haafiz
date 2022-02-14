package com.haafiz.etcd;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import io.etcd.jetcd.KeyValue;
import io.etcd.jetcd.kv.PutResponse;
import io.etcd.jetcd.lease.LeaseKeepAliveResponse;
import io.etcd.jetcd.lease.LeaseRevokeResponse;
import io.etcd.jetcd.lease.LeaseTimeToLiveResponse;
import io.etcd.jetcd.lock.LockResponse;
import io.etcd.jetcd.lock.UnlockResponse;
import io.etcd.jetcd.support.CloseableClient;
import io.grpc.stub.StreamObserver;

/**
 * @author Anthony
 * @create 2022/2/14
 * @desc
 **/
public interface EtcdClient {

    String CHARSET = "utf-8";

    void putKey(String key, String value) throws Exception;

    CompletableFuture<PutResponse> putKeyCallFuture(String key, String value) throws Exception;

    KeyValue getKey(final String key) throws Exception;

    void deleteKey(String key);

    List<KeyValue> getKeyWithPrefix(String prefix);

    void deleteKeyWithPrefix(String prefix);

    long putKeyWithExpireTime(String key, String value, long expireTime);

    long putKeyWithLeaseId(String key, String value, long leaseId) throws Exception;

    long generatorLeaseId(long expireTime) throws Exception;

    CloseableClient keepAliveSingleLease(long leaseId, StreamObserver<LeaseKeepAliveResponse> observer);

    LeaseKeepAliveResponse keepAliveOnce(long leaseId) throws InterruptedException, ExecutionException;

    LeaseKeepAliveResponse keepAliveOnce(long leaseId, long timeout) throws InterruptedException, ExecutionException, TimeoutException;

    LeaseTimeToLiveResponse timeToLiveLease(long leaseId) throws InterruptedException, ExecutionException;

    LeaseRevokeResponse revokeLease(long leaseId) throws InterruptedException, ExecutionException;

    long getHeartBeatLeaseId() throws InterruptedException;

    LockResponse lock(String lockName) throws Exception;

    LockResponse lock(String lockName, long expireTime) throws Exception;

    LockResponse lockByLeaseId(String lockName, long leaseId) throws Exception;

    UnlockResponse unlock(String lockName) throws Exception;

    void addWatcherListener(final String watcherKey, final boolean usePrefix, WatcherListener watcherListener);

    void removeWatcherListener(final String watcherKey);

    void addHeartBeatLeaseTimeoutNotifyListener(HeartBeatLeaseTimeoutListener heartBeatLeaseTimeoutListener);

    void close();

}
