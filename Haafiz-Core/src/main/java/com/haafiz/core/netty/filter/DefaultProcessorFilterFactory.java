package com.haafiz.core.netty.filter;

import com.haafiz.common.util.ServiceLoader;
import com.haafiz.core.context.Context;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

/**
 * @author Anthony
 * @create 2022/1/9
 * @desc
 */
@Slf4j
public class DefaultProcessorFilterFactory extends AbstractProcessorFilterFactory {
    private static class SingletonHolder {
        private static DefaultProcessorFilterFactory INSTANCE = new DefaultProcessorFilterFactory();
    }

    public DefaultProcessorFilterFactory getInstance() {
        return SingletonHolder.INSTANCE;
    }

    private DefaultProcessorFilterFactory(){
        Map<String , List<ProcessorFilter<Context>>> filterMap = new LinkedHashMap<String, List<ProcessorFilter<Context>>>();
        ServiceLoader<ProcessorFilter> serviceLoader = ServiceLoader.load(ProcessorFilter.class);


        for(ProcessorFilter<Context> filter : serviceLoader) {
            Filter annotation = filter.getClass().getAnnotation(Filter.class);
            if(annotation != null) {
                String filterType = annotation.value().getCode();
                List<ProcessorFilter<Context>> filterList = filterMap.get(filterType);
                if(filterList == null) {
                    filterList = new ArrayList<ProcessorFilter<Context>>();
                }
                filterList.add(filter);
                filterMap.put(filterType, filterList);
            }
        }

        for(ProcessorFilterType filterType : ProcessorFilterType.values()) {
            List<ProcessorFilter<Context>> filterList = filterMap.get(filterType.getCode());
            if(filterList == null || filterList.isEmpty()) {
                continue;
            }

            Collections.sort(filterList, new Comparator<ProcessorFilter<Context>>() {
                @Override
                public int compare(ProcessorFilter<Context> o1, ProcessorFilter<Context> o2) {
                    return o1.getClass().getAnnotation(Filter.class).order() -
                            o2.getClass().getAnnotation(Filter.class).order();
                }
            });

            try {
                super.buildFilterChain(filterType, filterList);
            } catch (Exception e) {
                //	ignore
                log.error("#DefaultProcessorFilterFactory.buildFilterChain# gateway filter load error# ERROR MESSAGE:{}!",e.getMessage(), e);
            }
        }

    }

    /**
     * default filter chain: pre + route + post
     * @param ctx
     * @throws Exception
     */
    @Override
    public void doFilterChain(Context ctx) throws Exception {
        try {
            defaultProcessorFilterChain.entry(ctx);
        } catch (Throwable e) {
            log.error("#DefaultProcessorFilterFactory.doFilterChain# ERROR MESSAGE: {}", e.getMessage(), e);
            ctx.setThrowable(e);
            doErrorFilterChain(ctx);
        }
    }

    /**
     * error filter chain :error+post
     * @param ctx
     * @throws Exception
     */
    @Override
    public void doErrorFilterChain(Context ctx) throws Exception {
        try {
            errorProcessorFilterChain.entry(ctx);
        } catch (Throwable e) {
            log.error("#DefaultProcessorFilterFactory.doErrorFilterChain# ERROR MESSAGE: {}" , e.getMessage(), e);
        }
    }
}
