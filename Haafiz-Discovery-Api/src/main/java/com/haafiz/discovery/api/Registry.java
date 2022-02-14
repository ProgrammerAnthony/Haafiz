package com.haafiz.discovery.api;

import com.haafiz.common.util.Pair;

import java.util.List;

/**
 * @author Anthony
 * @create 2022/1/15
 * @desc
 */
public interface Registry {

    String SERVICE_PREFIX = "/services";

    String INSTANCE_PREFIX = "/instance";

    String RULE_PREFIX = "/rule";

    String GATEWAY_PREFIX = "/gateway";

    String PATH = "/";

    void registerPathIfNotExist(String path, String key, boolean isPersistent) throws Exception;

    long registerEphemeralNode(String key, String value) throws Exception;

    void registerPersistentNode(String key, String value) throws Exception;

    List<Pair<String, String>> getListByPrefixKey(String prefix) throws Exception;

    Pair<String, String> getByKey(String key) throws Exception;

    boolean isExistKey(String key) throws Exception;

    void deleteByKey(String key);

    void close();

}
