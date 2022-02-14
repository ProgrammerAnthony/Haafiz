package com.haafiz.etcd;

/**
 * @author Anthony
 * @create 2022/2/14
 * @desc
 **/
public interface WatcherListener {

    void watcherKeyChanged(EtcdClient etcdClient, EtcdChangedEvent event) throws Exception;

}
