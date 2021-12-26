package com.haafiz.common.queue.mpmc;


/**
 * @author Anthony
 * @create 2021/12/26
 * @desc Linux Intel CacheLine Size 64
 */
public class Contended {

    public static final int CACHE_LINE = Integer.getInteger("Intel.CacheLineSize", 64); // bytes

}
