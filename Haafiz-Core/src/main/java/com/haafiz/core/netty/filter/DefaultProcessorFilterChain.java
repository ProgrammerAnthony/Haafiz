package com.haafiz.core.netty.filter;

import com.haafiz.core.context.Context;

/**
 * @author Anthony
 * @create 2022/1/9
 * @desc
 */
public  class DefaultProcessorFilterChain extends ProcessorFilterChain<Context> {

    private final String id;

    public DefaultProcessorFilterChain(String id) {
        this.id = id;
    }

    /**
     * dummy head
     */
    AbstractLinkedProcessorFilter<Context> first = new AbstractLinkedProcessorFilter<Context>() {

        @Override
        public void entry(Context ctx, Object... args) throws Throwable {
            super.fireNext(ctx, args);
        }

        @Override
        public boolean check(Context ctx) throws Throwable {
            return true;
        }
    };

    /**
     * end node
     */
    AbstractLinkedProcessorFilter<Context> end = first;

    @Override
    public void addFirst(AbstractLinkedProcessorFilter<Context> filter) {
        filter.setNext(first.getNext());
        first.setNext(filter);
        if (end == first) {
            end = filter;
        }
    }

    @Override
    public void addLast(AbstractLinkedProcessorFilter<Context> filter) {
        end.setNext(filter);
        end = filter;
    }

    @Override
    public void setNext(AbstractLinkedProcessorFilter<Context> filter) {
        addLast(filter);
    }

    @Override
    public AbstractLinkedProcessorFilter<Context> getNext() {
        return first.getNext();
    }


    @Override
    public boolean check(Context ctx) throws Throwable {
        return true;
    }

    @Override
    public void entry(Context ctx, Object... args) throws Throwable {
        first.transformEntry(ctx, args);
    }

    public String getId() {
        return id;
    }

}
