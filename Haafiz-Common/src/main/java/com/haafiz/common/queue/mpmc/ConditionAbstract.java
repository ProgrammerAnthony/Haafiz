package com.haafiz.common.queue.mpmc;

import java.util.concurrent.locks.ReentrantLock;


/**
 * @author Anthony
 * @create 2021/12/26
 * @desc use java sync to signal
 */
abstract class ConditionAbstract implements Condition {

    private final ReentrantLock queueLock = new ReentrantLock();

    private final java.util.concurrent.locks.Condition condition = queueLock.newCondition();


    /**
     * wake me when the condition is satisfied, or timeout
     * @param timeout
     * @throws InterruptedException
     */
    @Override
    public void awaitNanos(final long timeout) throws InterruptedException {
        long remaining = timeout;
        queueLock.lock();
        try {
        	//	if current queue is full
            while(test() && remaining > 0) {
                remaining = condition.awaitNanos(remaining);
            }
        }
        finally {
            queueLock.unlock();
        }
    }


    /**
     * wake if signal is called, or wait indefinitely
     * @throws InterruptedException
     */
    @Override
    public void await() throws InterruptedException {
        queueLock.lock();
        try {
            while(test()) {
                condition.await();
            }
        }
        finally {
            queueLock.unlock();
        }
    }


    /**
     * tell threads waiting on condition to wake up
     */
    @Override
    public void signal() {
        queueLock.lock();
        try {
            condition.signalAll();
        }
        finally {
            queueLock.unlock();
        }

    }

}