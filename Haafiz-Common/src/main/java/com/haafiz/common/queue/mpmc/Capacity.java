package com.haafiz.common.queue.mpmc;


/**
 * @author Anthony
 * @create 2021/12/26
 * @desc verification for power of 2
 */
final class Capacity {

    public static final int MAX_POWER2 = (1 << 30);

    public static int getCapacity(int capacity) {
        int c = 1;
        if (capacity >= MAX_POWER2) {
            c = MAX_POWER2;
        } else {
            while (c < capacity) c <<= 1;
        }

        if (isPowerOf2(c)) {
            return c;
        } else {
            throw new RuntimeException("Capacity is not a power of 2.");
        }
    }

    private static final boolean isPowerOf2(final int p) {
        // thanks mcheng for the suggestion
        return (p & (p - 1)) == 0;
    }

}