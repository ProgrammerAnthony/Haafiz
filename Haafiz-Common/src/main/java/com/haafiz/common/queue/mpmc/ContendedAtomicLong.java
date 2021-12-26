package com.haafiz.common.queue.mpmc;
import java.util.concurrent.atomic.AtomicLongArray;


/**
 * @author Anthony
 * @create 2021/12/26
 * @desc Avoid false cache line sharing
 */
public class ContendedAtomicLong extends Contended {

	//	一个缓存行需要多少个Long元素的填充：8
    private static final int CACHE_LINE_LONGS = CACHE_LINE / Long.BYTES;

    private final AtomicLongArray contendedArray;

    //	77
    ContendedAtomicLong(final long init)
    {
        contendedArray = new AtomicLongArray(2 * CACHE_LINE_LONGS);
        set(init);
    }

    void set(final long l) {
        contendedArray.set(CACHE_LINE_LONGS, l);
    }

    long get() {
        return contendedArray.get(CACHE_LINE_LONGS);
    }

    public String toString() {
        return Long.toString(get());
    }

    public boolean compareAndSet(final long expect, final long l) {
        return contendedArray.compareAndSet(CACHE_LINE_LONGS, expect, l);
    }
}
