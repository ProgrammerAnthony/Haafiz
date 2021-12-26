package com.haafiz.common.queue.flusher;

import com.google.common.base.Preconditions;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.lmax.disruptor.*;
import com.lmax.disruptor.dsl.ProducerType;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author Anthony
 * @create 2021/12/26
 * @desc use {@link com.lmax.disruptor.dsl.Disruptor} to build Parallel multi producers
 * and consumers tools
 */
public class ParallelFlusher<E> implements Flusher<E> {

    private RingBuffer<Holder> ringBuffer;

    private DisruptorEventListener<E> eventListener;

    private WorkerPool<Holder> workerPool;

    private ExecutorService executorService;

    private EventTranslatorOneArg<Holder, E> eventTranslator;

    private ParallelFlusher(Builder<E> builder) {

        this.executorService = Executors.newFixedThreadPool(builder.threads,
                new ThreadFactoryBuilder().setNameFormat("ParallelFlusher-" + builder.namePrefix + "-pool-%d").build());

        this.eventListener = builder.listener;
        this.eventTranslator = new HolderEventTranslator();

        //	create RingBuffer
        RingBuffer<Holder> ringBuffer = RingBuffer.create(builder.producerType,
                new HolderEventFactory(),
                builder.bufferSize,
                builder.waitStrategy);


        SequenceBarrier sequenceBarrier = ringBuffer.newBarrier();

        //	create multi consumers handler: HolderWorkHandler
        @SuppressWarnings("unchecked")
        WorkHandler<Holder>[] workHandlers = new WorkHandler[builder.threads];
        for (int i = 0; i < workHandlers.length; i++) {
            workHandlers[i] = new HolderWorkHandler();
        }

        //WorkerPool contains a pool of WorkProcessors that will consume sequences
        WorkerPool<Holder> workerPool = new WorkerPool<>(ringBuffer,
                sequenceBarrier,
                new HolderExceptionHandler(),
                workHandlers);

        //	设置多个消费者的sequence序号 用于单独统计消费进度, 并且设置到ringbuffer中
        ringBuffer.addGatingSequences(workerPool.getWorkerSequences());

        this.workerPool = workerPool;

    }

    @Override
    public void start() {
        this.ringBuffer = workerPool.start(executorService);
    }

    @Override
    public boolean isShutdown() {
        return ringBuffer == null;
    }


    @Override
    public void shutdown() {
        RingBuffer<Holder> temp = ringBuffer;
        ringBuffer = null;
        if (temp == null) {
            return;
        }
        if (workerPool != null) {
            workerPool.drainAndHalt();
        }
        if (executorService != null) {
            executorService.shutdown();
        }
    }

    private static <E> void process(DisruptorEventListener<E> listener, Throwable e, E event) {
        listener.onException(e, -1, event);
    }

    private static <E> void process(DisruptorEventListener<E> listener, Throwable e, @SuppressWarnings("unchecked") E... events) {
        for (E event : events) {
            process(listener, e, event);
        }
    }

    @Override
    public void add(E event) {
        final RingBuffer<Holder> temp = ringBuffer;
        if (temp == null) {
            process(this.eventListener, new IllegalStateException("ParallelFlusher is closed"), event);
            return;
        }
        try {
            ringBuffer.publishEvent(this.eventTranslator, event);
        } catch (NullPointerException e) {
            process(this.eventListener, new IllegalStateException("ParallelFlusher is closed"), event);
        }
    }

    @Override
    public void add(@SuppressWarnings("unchecked") E... events) {
        final RingBuffer<Holder> temp = ringBuffer;
        if (temp == null) {
            process(this.eventListener, new IllegalStateException("ParallelFlusher is closed"), events);
            return;
        }
        try {
            ringBuffer.publishEvents(this.eventTranslator, events);
        } catch (NullPointerException e) {
            process(this.eventListener, new IllegalStateException("ParallelFlusher is closed"), events);
        }
    }

    @Override
    public boolean tryAdd(E event) {
        final RingBuffer<Holder> temp = ringBuffer;
        if (temp == null) {
            return false;
        }
        try {
            return ringBuffer.tryPublishEvent(this.eventTranslator, event);
        } catch (NullPointerException e) {
            return false;
        }
    }

    @Override
    public boolean tryAdd(@SuppressWarnings("unchecked") E... events) {
        final RingBuffer<Holder> temp = ringBuffer;
        if (temp == null) {
            return false;
        }
        try {
            return ringBuffer.tryPublishEvents(this.eventTranslator, events);
        } catch (NullPointerException e) {
            return false;
        }
    }


    public interface DisruptorEventListener<E> {
        void onEvent(E event) throws Exception;

        void onException(Throwable ex, long sequence, E event);
    }

    public static class Builder<E> {

        private ProducerType producerType = ProducerType.MULTI;

        private int bufferSize = 16 * 1024;

        private int threads = 1;

        private String namePrefix = "";

        private WaitStrategy waitStrategy = new BlockingWaitStrategy();

        private DisruptorEventListener<E> listener;

        public Builder<E> setProducerType(ProducerType producerType) {
            Preconditions.checkNotNull(producerType);
            this.producerType = producerType;
            return this;
        }

        public Builder<E> setThreads(int threads) {
            Preconditions.checkArgument(threads > 0);
            this.threads = threads;
            return this;
        }

        public Builder<E> setBufferSize(int bufferSize) {
            Preconditions.checkArgument(Integer.bitCount(bufferSize) == 1);
            this.bufferSize = bufferSize;
            return this;
        }

        public Builder<E> setNamePrefix(String namePrefix) {
            Preconditions.checkNotNull(namePrefix);
            this.namePrefix = namePrefix;
            return this;
        }

        public Builder<E> setWaitStrategy(WaitStrategy waitStrategy) {
            Preconditions.checkNotNull(waitStrategy);
            this.waitStrategy = waitStrategy;
            return this;
        }

        public Builder<E> setEventListener(DisruptorEventListener<E> listener) {
            Preconditions.checkNotNull(listener);
            this.listener = listener;
            return this;
        }

        public ParallelFlusher<E> build() {
            return new ParallelFlusher<>(this);
        }
    }

    private class Holder {

        private E event;

        public void setValue(E event) {
            this.event = event;
        }

        public String toString() {
            return "Holder event=" + event;
        }

    }

    private class HolderEventFactory implements EventFactory<Holder> {
        @Override
        public ParallelFlusher<E>.Holder newInstance() {
            return new Holder();
        }
    }

    private class HolderWorkHandler implements WorkHandler<Holder> {

        @Override
        public void onEvent(ParallelFlusher<E>.Holder event) throws Exception {
            eventListener.onEvent(event.event);
            event.setValue(null);
        }

    }

    private class HolderExceptionHandler implements ExceptionHandler<Holder> {

        @Override
        public void handleEventException(Throwable ex, long sequence, ParallelFlusher<E>.Holder event) {
            Holder holder = (Holder) event;
            try {
                eventListener.onException(ex, sequence, holder.event);
            } catch (Exception e) {
                //	ignore..
            } finally {
                holder.setValue(null);
            }
        }

        @Override
        public void handleOnStartException(Throwable ex) {
            throw new UnsupportedOperationException(ex);
        }

        @Override
        public void handleOnShutdownException(Throwable ex) {
            throw new UnsupportedOperationException(ex);
        }

    }

    private class HolderEventTranslator implements EventTranslatorOneArg<Holder, E> {

        @Override
        public void translateTo(ParallelFlusher<E>.Holder holder, long sequence, E event) {
            holder.setValue(event);
        }

    }

}
