package com.haafiz.core.netty.processor;

import com.haafiz.core.HttpRequestWrapper;

/**
 * @author Anthony
 * @create 2021/12/20
 * @desc
 */
public interface NettyProcessor {
    void process(HttpRequestWrapper httpRequestWrapper) throws Exception ;

    void start();

    void shutdown();
}
