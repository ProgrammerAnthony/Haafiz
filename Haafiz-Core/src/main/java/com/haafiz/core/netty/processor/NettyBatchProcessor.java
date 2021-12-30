package com.haafiz.core.netty.processor;

import com.haafiz.common.exception.ResponseCode;
import com.haafiz.common.queue.flusher.ParallelFlusher;
import com.haafiz.core.HaafizConfig;
import com.haafiz.core.context.HttpRequestWrapper;
import com.lmax.disruptor.dsl.ProducerType;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.*;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Anthony
 * @create 2021/12/20
 * @desc
 */
@Slf4j
public class NettyBatchProcessor implements NettyProcessor {

    private final String THREAD_NAME_PREFIX = "haafizFlusher-";
    private ParallelFlusher<HttpRequestWrapper> parallelFlusher;
    private HaafizConfig haafizConfig;
    private NettyCoreProcessor nettyCoreProcessor;

    public NettyBatchProcessor(HaafizConfig haafizConfig, NettyCoreProcessor nettyCoreProcessor) {
        this.haafizConfig = haafizConfig;
        this.nettyCoreProcessor = nettyCoreProcessor;
        ParallelFlusher.Builder<HttpRequestWrapper> builder = new ParallelFlusher.Builder<HttpRequestWrapper>()
                .setBufferSize(haafizConfig.getBufferSize())
                .setThreads(haafizConfig.getProcessThread())
                .setProducerType(ProducerType.MULTI)
                .setNamePrefix(THREAD_NAME_PREFIX)
                .setWaitStrategy(haafizConfig.getATrueWaitStrategy());

        BatchEventProcessorListener batchEventProcessorListener = new BatchEventProcessorListener();
        builder.setEventListener(batchEventProcessorListener);
        this.parallelFlusher = builder.build();
    }

    @Override
    public void process(HttpRequestWrapper httpRequestWrapper) {
        System.err.println("NettyBatchProcessor add!");
        this.parallelFlusher.add(httpRequestWrapper);
    }

    @Override
    public void start() {
        this.nettyCoreProcessor.start();
        this.parallelFlusher.start();
    }

    @Override
    public void shutdown() {
        this.nettyCoreProcessor.shutdown();
        this.parallelFlusher.shutdown();
    }

    private class BatchEventProcessorListener implements ParallelFlusher.DisruptorEventListener<HttpRequestWrapper> {
        @Override
        public void onEvent(HttpRequestWrapper event) throws Exception {
            nettyCoreProcessor.process(event);
        }

        @Override
        public void onException(Throwable ex, long sequence, HttpRequestWrapper event) {
            HttpRequest request = event.getFullHttpRequest();
            ChannelHandlerContext ctx = event.getCtx();
            try {
                log.error("#BatchEventProcessorListener# onException request process failed, request: {}. errorMessage: {}",
                        request, ex.getMessage(), ex);

                FullHttpResponse fullHttpResponse = ResponseHelper.getHttpResponse(ResponseCode.INTERNAL_ERROR);
                if (!HttpUtil.isKeepAlive(request)) {
                    ctx.writeAndFlush(fullHttpResponse).addListener(ChannelFutureListener.CLOSE);
                } else {
                    fullHttpResponse.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE);
                    ctx.writeAndFlush(fullHttpResponse);
                }

            } catch (Exception e) {
                //	ignore
                log.error("#BatchEventProcessorListener# onException request write back failed, request: {}. errorMessage: {}",
                        request, e.getMessage(), e);
            }

        }
    }

    public HaafizConfig getHaafizConfig() {
        return haafizConfig;
    }
}
