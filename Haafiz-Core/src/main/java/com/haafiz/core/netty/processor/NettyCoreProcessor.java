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
            //	1. 解析FullHttpRequest, 把他转换为我们自己想要的内部对象：Context
            HaafizContext rapidContext = RequestHelper.doContext(request, ctx);

            //	2. 执行整个的过滤器逻辑：FilterChain

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
