package com.haafiz.core.netty.filter;

import com.haafiz.core.context.Context;

import java.util.List;

/**
 * @author Anthony
 * @create 2022/1/9
 * @desc
 */
public interface ProcessorFilterFactory {

    void buildFilterChain(ProcessorFilterType filterType, List<ProcessorFilter<Context>> filters) throws Exception;



    void doFilterChain(Context ctx) throws Exception;



    void doErrorFilterChain(Context ctx) throws Exception;


    <T> T getFilter(Class<T> t) throws Exception;


    <T> T getFilter(String filterId) throws Exception;
}
