package com.haafiz.core.netty.filter;

/**
 * @author Anthony
 * @create 2022/1/10
 * @desc build the filter chain
 */
public interface FilterChainBuilder<T> {
    T build();
}

