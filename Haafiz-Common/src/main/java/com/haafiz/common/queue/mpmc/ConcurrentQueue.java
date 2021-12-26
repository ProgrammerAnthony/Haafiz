package com.haafiz.common.queue.mpmc;


/**
 * @author Anthony
 * @create 2021/12/26
 * @desc A very high performance blocking buffer, based on Disruptor approach to queues
 */
public interface ConcurrentQueue<E> {

    boolean offer(E e);

    E poll();

    E peek();

    int size();

    int capacity();

    boolean isEmpty();

    boolean contains(Object o);

    int remove(E[] e);

    void clear();
    
}