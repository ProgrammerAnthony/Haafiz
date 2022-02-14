package com.haafiz.discovery.api;

/**
 * @author Anthony
 * @create 2022/1/15
 * @desc
 */
public interface RegistryService extends Registry{

    void addWatcherListeners(String superPath, Notify notify);

    /**
     * initialize registry service
     * @param registryAddress
     */
    void initialized(String registryAddress);

}
