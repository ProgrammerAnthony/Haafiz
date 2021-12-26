package com.haafiz.common.queue.mpmc;


/**
 * @author Anthony
 * @create 2021/12/26
 * @desc blocking spinning abstract class
 */
public abstract class ConditionAbstractSpinning implements Condition {


    /**
     * on spinning waiting breaking on test and expires > timeNow
     * @param timeout
     * @throws InterruptedException
     */
    @Override
    public void awaitNanos(final long timeout) throws InterruptedException {
        long timeNow = System.nanoTime();
        final long expires = timeNow+timeout;

        final Thread t = Thread.currentThread();

        while(test() && expires > timeNow && !t.isInterrupted()) {
            timeNow = System.nanoTime();
            Condition.onSpinWait();
        }

        if(t.isInterrupted()) {
            throw new InterruptedException();
        }
    }


    /**
     * on spinning waiting breaking on test
     * @throws InterruptedException
     */
    @Override
    public void await() throws InterruptedException {
        final Thread t = Thread.currentThread();

        while(test() && !t.isInterrupted()) {
            Condition.onSpinWait();
        }

        if(t.isInterrupted()) {
            throw new InterruptedException();
        }
    }

    @Override
    public void signal() {

    }
}
