package com.haafiz.core.netty.processor;

import com.haafiz.common.exception.ResponseCode;
import com.haafiz.common.queue.mpmc.MpmcBlockingQueue;
import com.haafiz.core.HaafizConfig;
import com.haafiz.core.context.HttpRequestWrapper;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.*;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author Anthony
 * @create 2021/12/20
 * @desc
 */
@Slf4j
public class NettyMpmcProcessor  implements NettyProcessor {

    private HaafizConfig haafizConfig;

    private NettyCoreProcessor nettyCoreProcessor;

    private MpmcBlockingQueue<HttpRequestWrapper> mpmcBlockingQueue;

    private boolean usedExecutorPool;

    private ExecutorService executorService;

    private volatile boolean isRunning = false;

    private Thread consumerProcessorThread;

    public NettyMpmcProcessor(HaafizConfig haafizConfig, NettyCoreProcessor nettyCoreProcessor, boolean usedExecutorPool) {
        this.haafizConfig = haafizConfig;
        this.nettyCoreProcessor = nettyCoreProcessor;
        this.mpmcBlockingQueue = new MpmcBlockingQueue<>(haafizConfig.getBufferSize());
        this.usedExecutorPool = usedExecutorPool;
    }

    @Override
    public void process(HttpRequestWrapper httpRequestWrapper) throws Exception {
        System.err.println("NettyMpmcProcessor put!");
        this.mpmcBlockingQueue.put(httpRequestWrapper);
    }

    @Override
    public void start() {
        this.isRunning = true;
        this.nettyCoreProcessor.start();
        if(usedExecutorPool) {
            this.executorService = Executors.newFixedThreadPool(haafizConfig.getProcessThread());
            for(int i = 0; i < haafizConfig.getProcessThread(); i ++) {
                this.executorService.submit(new ConsumerProcessor());
            }
        } else {
            this.consumerProcessorThread = new Thread(new ConsumerProcessor());
            this.consumerProcessorThread.start();
        }
    }

    @Override
    public void shutdown() {
        this.isRunning = false;
        this.nettyCoreProcessor.shutdown();
        if(usedExecutorPool) {
            this.executorService.shutdown();
        }
    }


    /**
     * @author Anthony
     * @create 2021/12/20
     * @desc consumer core impl
     */
    public class ConsumerProcessor implements Runnable {

        @Override
        public void run() {
            while(isRunning) {
                HttpRequestWrapper event = null;
                try {
                    event = mpmcBlockingQueue.take();
                    nettyCoreProcessor.process(event);
                } catch (Throwable t) {
                    if(event != null) {
                        HttpRequest request = event.getFullHttpRequest();
                        ChannelHandlerContext ctx = event.getCtx();
                        try {
                            log.error("#ConsumerProcessor# onException request process fail, request: {}. errorMessage: {}",
                                    request, t.getMessage(), t);

                            FullHttpResponse fullHttpResponse = ResponseHelper.getHttpResponse(ResponseCode.INTERNAL_ERROR);
                            if(!HttpUtil.isKeepAlive(request)) {
                                ctx.writeAndFlush(fullHttpResponse).addListener(ChannelFutureListener.CLOSE);
                            } else {
                                fullHttpResponse.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE);
                                ctx.writeAndFlush(fullHttpResponse);
                            }

                        } catch (Exception e) {
                            //	ignore
                            log.error("#ConsumerProcessor# onException request write back error, request: {}. errorMessage: {}",
                                    request, e.getMessage(), e);
                        }
                    } else {
                        log.error("#ConsumerProcessor# onException event is Empty errorMessage: {}",  t.getMessage(), t);
                    }
                }
            }
        }
    }

}
