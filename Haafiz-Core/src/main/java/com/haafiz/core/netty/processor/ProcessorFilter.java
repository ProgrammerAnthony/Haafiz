package com.haafiz.core.netty.processor;

/**
 * @author Anthony
 * @create 2022/1/9
 * @desc
 */
public interface ProcessorFilter<T> {

    boolean doCheck(T t) throws Throwable;

    void transformEntry(T t, Object... args) throws Throwable;

    void entry(T t, Object... args) throws Throwable;

    void fireNext(T t, Object... args) throws Throwable;

    default void init() throws Exception {

    }

    default void destroy() throws Exception {

    }

    default void refresh() throws Exception {

    }

}
