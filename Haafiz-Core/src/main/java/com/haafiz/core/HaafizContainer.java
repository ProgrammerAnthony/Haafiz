package com.haafiz.core;

import com.haafiz.common.constants.BufferHelper;
import com.haafiz.common.lifecycle.AbstractLifecycleComponent;
import com.haafiz.core.netty.NettyHttpClient;
import com.haafiz.core.netty.NettyHttpServer;
import com.haafiz.core.netty.processor.NettyBatchProcessor;
import com.haafiz.core.netty.processor.NettyCoreProcessor;
import com.haafiz.core.netty.processor.NettyMpmcProcessor;
import com.haafiz.core.netty.processor.NettyProcessor;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

/**
 * @author Anthony
 * @create 2021/12/16
 * @desc
 */
@Slf4j
public class HaafizContainer extends AbstractLifecycleComponent {
    private NettyHttpServer nettyHttpServer;
    private NettyHttpClient nettyHttpClient;
    private NettyProcessor nettyProcessor;
    private HaafizConfig haafizConfig;

    public HaafizContainer(HaafizConfig haafizConfig) {
        //	1. create core processor
        NettyCoreProcessor nettyCoreProcessor = new NettyCoreProcessor();

        //	2. whether use buffer
        String bufferType = haafizConfig.getBufferType();

        if (BufferHelper.isFlusher(bufferType)) {
            nettyProcessor = new NettyBatchProcessor(haafizConfig, nettyCoreProcessor);
        } else if (BufferHelper.isMpmc(bufferType)) {
            nettyProcessor = new NettyMpmcProcessor(haafizConfig, nettyCoreProcessor, true);
        } else {
            nettyProcessor = nettyCoreProcessor;
        }
        //	3. create NettyHttpServer（for upstream http server）
        nettyHttpServer = new NettyHttpServer(haafizConfig, nettyProcessor);

        //	4. create NettyHttpClient（for downstream connection http client）
        nettyHttpClient = new NettyHttpClient(haafizConfig, nettyHttpServer.getEventLoopGroupWork());


    }


    @Override
    protected void doStart() {
        nettyProcessor.start();
        nettyHttpServer.start();
        nettyHttpClient.start();
        log.info("HaafizContainer started !!!");
    }

    @Override
    protected void doStop() {
        nettyProcessor.shutdown();
        nettyHttpServer.stop();
        nettyHttpClient.stop();
    }

    @Override
    protected void doClose() throws IOException {

    }
}
