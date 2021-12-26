package com.haafiz.common.queue.mpmc;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

/**
 * @author Anthony
 * @create 2021/12/26
 * @desc Return true once a condition is satisfied
 */
interface Condition {

    long PARK_TIMEOUT = 50L;

    int MAX_PROG_YIELD = 2000;


    /**
     * return true if the queue condition is satisfied
     * @return
     */
    boolean test();

    /**
     * wake me when the condition is satisfied, or timeout
     * @param timeout
     * @throws InterruptedException
     */
    void awaitNanos(final long timeout) throws InterruptedException;

    /**
     * <B>方法名称：</B>await<BR>
     * <B>概要说明：</B>wake if signal is called, or wait indefinitely<BR>
     * @author JiFeng
     * @since 2021年12月7日 上午11:20:32
     * @throws InterruptedException
     */
    /**
     * wake if signal is called, or wait indefinitely
     * @throws InterruptedException
     */
    void await() throws InterruptedException;


    /**
     * tell threads waiting on condition to wake up
     */
    void signal();

    /**
     * progressively transition from spin to yield over time
     * @param n
     * @return
     */
    static int progressiveYield(final int n) {
        if(n > 500) {
            if(n<1000) {
                // "randomly" yield 1:8
                if((n & 0x7) == 0) {
                    LockSupport.parkNanos(PARK_TIMEOUT);
                } else {
                    onSpinWait();
                }
            } else if(n<MAX_PROG_YIELD) {
                // "randomly" yield 1:4
                if((n & 0x3) == 0) {
                    Thread.yield();
                } else {
                    onSpinWait();
                }
            } else {
                Thread.yield();
                return n;
            }
        } else {
            onSpinWait();
        }
        return n+1;
    }

    static void onSpinWait() {
        // Java 9 hint for spin waiting PAUSE instruction
        //http://openjdk.java.net/jeps/285
        // Thread.onSpinWait();
    }

    /**
     * Wait for timeout on condition
     * @param timeout
     * @param unit
     * @param condition
     * @return
     * @throws InterruptedException
     */
    static boolean waitStatus(final long timeout, final TimeUnit unit, final Condition condition) throws InterruptedException {
        // until condition is signaled
        final long timeoutNanos = TimeUnit.NANOSECONDS.convert(timeout, unit);
        final long expireTime = System.nanoTime() + timeoutNanos;
        // the queue is empty or full wait for something to change
        while (condition.test()) {
            final long now = System.nanoTime();
            if (now > expireTime) {
                return false;
            }
            condition.awaitNanos(expireTime - now - PARK_TIMEOUT);
        }
        return true;
    }

}