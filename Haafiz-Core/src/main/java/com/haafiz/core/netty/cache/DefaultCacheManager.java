package com.haafiz.core.netty.cache;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Anthony
 * @create 2022/1/9
 * @desc
 */
public class DefaultCacheManager {
    private DefaultCacheManager() {
    }

    public static final String FILTER_CONFIG_CACHE_ID = "filterConfigCache";
    private final ConcurrentHashMap<String, Cache<String, ?>> cacheMap = new ConcurrentHashMap<>();


    private static class SingletonHolder {
        private static DefaultCacheManager INSTANCE = new DefaultCacheManager();
    }

    public static DefaultCacheManager getInstance() {
        return SingletonHolder.INSTANCE;
    }

    @SuppressWarnings("unchecked")
    public <V> Cache<String, V> create(String cacheId) {
        Cache<String, V> build = Caffeine.newBuilder().build();
        cacheMap.put(cacheId, build);
        return (Cache<String, V>) cacheMap.get(cacheId);
    }

    @SuppressWarnings("unchecked")
    public <V> void remove(String cacheId,String key){
        Cache<String, V> cache = (Cache<String, V>) cacheMap.get(cacheId);
        if(cache!=null){
            cache.invalidate(key);
        }
    }

    public <V> void remove(String cacheId) {
        @SuppressWarnings("unchecked")
        Cache<String, V> cache = (Cache<String, V>) cacheMap.get(cacheId);
        if(cache != null) {
            cache.invalidateAll();
        }
    }

    public void cleanAll() {
        cacheMap.values().forEach(cache -> cache.invalidateAll());
    }
}
