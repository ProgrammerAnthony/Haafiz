package com.haafiz.core.netty.filter;

import com.haafiz.core.context.Context;
import lombok.extern.slf4j.Slf4j;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Anthony
 * @create 2022/1/9
 * @desc
 */
@Slf4j
public abstract class AbstractProcessorFilterFactory implements ProcessorFilterFactory {
    /*
     *	pre + route + post
     */
    public DefaultProcessorFilterChain defaultProcessorFilterChain = new DefaultProcessorFilterChain("defaultProcessorFilterChain");

    /*
     * 	error + post
     */
    public DefaultProcessorFilterChain errorProcessorFilterChain = new DefaultProcessorFilterChain("errorProcessorFilterChain");

    public Map<String /* processorFilterType */, Map<String, ProcessorFilter<Context>>> processorFilterTypeMap = new LinkedHashMap<>();

    public Map<String /* filterId */, ProcessorFilter<Context>> processorFilterIdMap = new LinkedHashMap<>();

    @Override
    public void buildFilterChain(ProcessorFilterType filterType, List<ProcessorFilter<Context>> filters) throws Exception {
        switch (filterType) {
            case PRE:
            case ROUTE:
                addFilterChain(defaultProcessorFilterChain, filters);
                break;
            case POST:
                addFilterChain(defaultProcessorFilterChain, filters);
                addFilterChain(errorProcessorFilterChain, filters);
                break;
            case ERROR:
                addFilterChain(errorProcessorFilterChain, filters);
                break;
            default:
                throw new RuntimeException("ProcessorFilterType is not supported !");

        }
    }

    private void addFilterChain(DefaultProcessorFilterChain processorFilterChain, List<ProcessorFilter<Context>> filters) throws Exception {
        for (ProcessorFilter<Context> processorFilter : filters) {
            processorFilter.init();
            doBuilder(processorFilterChain, processorFilter);
        }
    }


    private void doBuilder(DefaultProcessorFilterChain processorFilterChain, ProcessorFilter<Context> processorFilter) {
        log.info("filterChain: {}, the scanner filter is : {}", processorFilterChain.getId(), processorFilter.getClass().getName());
        Filter annotation=processorFilter.getClass().getAnnotation(Filter.class);
        if(annotation!=null){
            processorFilterChain.addLast((AbstractLinkedProcessorFilter<Context>)processorFilter);
            String filterId = annotation.id();
            if(filterId == null || filterId.length() < 1) {
                filterId = processorFilter.getClass().getName();
            }
            String code = annotation.value().getCode();
            Map<String, ProcessorFilter<Context>> filterMap = processorFilterTypeMap.get(code);
            if(filterMap == null) {
                filterMap = new LinkedHashMap<String, ProcessorFilter<Context>>();
            }
            filterMap.put(filterId, processorFilter);

            //	type
            processorFilterTypeMap.put(code, filterMap);
            //	id
            processorFilterIdMap.put(filterId, processorFilter);
        }
    }

    @Override
    public <T> T getFilter(Class<T> t) throws Exception {
        Filter annotation = t.getAnnotation(Filter.class);
        if (annotation != null) {
            String filterId = annotation.id();
            if (filterId == null || filterId.length() <= 1) {
                filterId = annotation.name();
            }
            return this.getFilter(filterId);
        }
        return null;
    }

    @Override
    public <T> T getFilter(String filterId) throws Exception {
        ProcessorFilter<Context> filter = null;
        if (!processorFilterIdMap.isEmpty()) {
            filter = processorFilterIdMap.get(filterId);
        }
        return (T) filter;
    }
}
