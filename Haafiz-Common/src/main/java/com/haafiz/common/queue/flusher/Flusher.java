package com.haafiz.common.queue.flusher;

public interface Flusher<E> {

    void add(E event);

    void add(@SuppressWarnings("unchecked") E... event);

    boolean tryAdd(E event);


    boolean tryAdd(@SuppressWarnings("unchecked") E... event);

    boolean isShutdown();

    void start();

    void shutdown();

}
