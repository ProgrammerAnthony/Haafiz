package com.haafiz.client.registry;

/**
 * @author Anthony
 * @create 2022/1/15
 * @desc listener
 */
public interface Notify {

    void put(String key, String value) throws Exception;

    void delete(String key) throws Exception;

}
