package com.haafiz.core.netty.filter;

import com.haafiz.core.context.Context;

/**
 * @author Anthony
 * @create 2022/1/9
 * @desc linked processor
 */
public abstract class AbstractLinkedProcessorFilter<T> implements ProcessorFilter<Context> {
    private AbstractLinkedProcessorFilter<T> next;

    @Override
    public void transformEntry(Context context, Object... args) throws Throwable {
        entry(context, args);
    }


    @Override
    public void fireNext(Context context, Object... args) throws Throwable {
        if (next != null) {
            if (!next.check(context)) {
                next.fireNext(context, args);
            } else {
                next.transformEntry(context, args);
            }
        } else {
            return;
        }
    }

    public void setNext(AbstractLinkedProcessorFilter<T> next) {
        this.next = next;
    }

    public AbstractLinkedProcessorFilter<T> getNext() {
        return next;
    }
}
