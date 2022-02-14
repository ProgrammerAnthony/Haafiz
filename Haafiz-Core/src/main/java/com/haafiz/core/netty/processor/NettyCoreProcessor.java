package com.haafiz.core.netty.processor;

import com.haafiz.core.RequestHelper;
import com.haafiz.core.context.HaafizContext;
import com.haafiz.core.context.HttpRequestWrapper;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;

/**
 * @author Anthony
 * @create 2021/12/20
 * @desc
 */
public class NettyCoreProcessor implements NettyProcessor{
    @Override
    public void process(HttpRequestWrapper event) {
        FullHttpRequest request = event.getFullHttpRequest();
        ChannelHandlerContext ctx = event.getCtx();
        try {
            //	1. parse FullHttpRequest, convert to inner object：Context
            HaafizContext haafizContext = RequestHelper.doContext(request, ctx);

            //	2. execute filter with：FilterChain

        } catch (Throwable t) {
        }
    }

    @Override
    public void start() {

    }

    @Override
    public void shutdown() {

    }
}
