package com.haafiz.core.netty;


import com.haafiz.core.context.HttpRequestWrapper;
import com.haafiz.core.netty.processor.NettyProcessor;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.util.ReferenceCountUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Anthony
 * @create 2021/12/20
 * @desc
 */
@Slf4j
public class NettyHttpServerHandler extends ChannelInboundHandlerAdapter {
    private NettyProcessor nettyProcessor;

    public NettyHttpServerHandler(NettyProcessor nettyProcessor) {
        this.nettyProcessor = nettyProcessor;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if(msg instanceof HttpRequest) {
            FullHttpRequest request = (FullHttpRequest)msg;
            HttpRequestWrapper httpRequestWrapper = new HttpRequestWrapper();
            httpRequestWrapper.setFullHttpRequest(request);
            httpRequestWrapper.setCtx(ctx);

            //	processor
            nettyProcessor.process(httpRequestWrapper);

        } else {
            //	never come to this way, ignore
            log.error("#NettyHttpServerHandler.channelRead# message type is not httpRequest: {}", msg);
            boolean release = ReferenceCountUtil.release(msg);
            if(!release) {
                log.error("#NettyHttpServerHandler.channelRead# release fail release resource fail");
            }
        }
    }
}
