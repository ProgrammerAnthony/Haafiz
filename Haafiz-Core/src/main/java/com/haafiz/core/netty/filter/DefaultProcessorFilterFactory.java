package com.haafiz.core.netty.filter;

import com.haafiz.core.context.Context;

import java.util.List;

/**
 * @author Anthony
 * @create 2022/1/9
 * @desc
 */
public class DefaultProcessorFilterFactory extends AbstractProcessorFilterFactory {
    @Override
    public void buildFilterChain(ProcessorFilterType filterType, List<ProcessorFilter<Context>> filters) throws Exception {

    }

    @Override
    public void doFilterChain(Context ctx) throws Exception {

    }

    @Override
    public void doErrorFilterChain(Context ctx) throws Exception {

    }

    @Override
    public <T> T getFilter(Class<T> t) throws Exception {
        return null;
    }

    @Override
    public <T> T getFilter(String filterId) throws Exception {
        return null;
    }
}
