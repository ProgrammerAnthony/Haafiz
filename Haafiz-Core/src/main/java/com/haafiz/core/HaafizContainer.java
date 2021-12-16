package com.haafiz.core;

import com.haafiz.common.lifecycle.AbstractLifecycleComponent;
import com.haafiz.core.config.HaafizConfig;
import com.haafiz.core.netty.NettyHttpClient;
import com.haafiz.core.netty.NettyHttpServer;
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

    public HaafizContainer(HaafizConfig haafizConfig) {
        nettyHttpClient = new NettyHttpClient(haafizConfig);
        nettyHttpServer = new NettyHttpServer(haafizConfig);
    }


    @Override
    protected void doStart() {

    }

    @Override
    protected void doStop() {

    }

    @Override
    protected void doClose() throws IOException {

    }
}
