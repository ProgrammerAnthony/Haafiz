package com.haafiz.core.netty;

import com.haafiz.core.netty.processor.AbstractLinkedProcessorFilter;

/**
 * @author Anthony
 * @create 2022/1/9
 * @desc
 */
public abstract class ProcessorFilterChain<T> extends AbstractLinkedProcessorFilter<T> {
    public abstract void addFirst(AbstractLinkedProcessorFilter<T> filter);

    public abstract void addLast(AbstractLinkedProcessorFilter<T> filter);
}
