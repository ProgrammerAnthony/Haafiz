package com.haafiz.core.netty;

import com.haafiz.common.lifecycle.AbstractLifecycleComponent;
import com.haafiz.common.util.RemotingHelper;
import com.haafiz.common.util.RemotingUtil;
import com.haafiz.core.HaafizConfig;
import com.haafiz.core.netty.processor.NettyProcessor;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.*;
import io.netty.channel.epoll.Epoll;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.HttpServerExpectContinueHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.concurrent.DefaultThreadFactory;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;

/**
 * @author Anthony
 * @create 2021/12/16
 * @desc
 */
@Slf4j
public class NettyHttpServer extends AbstractLifecycleComponent {

    private final HaafizConfig haafizConfig;

    private int port = 8888;

    private ServerBootstrap serverBootstrap;

    private EventLoopGroup eventLoopGroupBoss;

    private EventLoopGroup eventLoopGroupWork;

    private NettyProcessor nettyProcessor;


    public NettyHttpServer(HaafizConfig haafizConfig, NettyProcessor nettyProcessor) {
        this.haafizConfig = haafizConfig;
        this.nettyProcessor = nettyProcessor;
        if (haafizConfig.getPort() > 0 && haafizConfig.getPort() < 65535) {
            this.port = haafizConfig.getPort();
        }
        initServer();

    }

    private void initServer() {
      this.serverBootstrap=new ServerBootstrap();
        if(useEPoll()) {
            this.eventLoopGroupBoss = new EpollEventLoopGroup(haafizConfig.getEventLoopGroupBossNum(),
                    new DefaultThreadFactory("NettyBossEPoll"));
            this.eventLoopGroupWork = new EpollEventLoopGroup(haafizConfig.getEventLoopGroupWorkNum(),
                    new DefaultThreadFactory("NettyWorkEPoll"));
        } else {
            this.eventLoopGroupBoss = new NioEventLoopGroup(haafizConfig.getEventLoopGroupBossNum(),
                    new DefaultThreadFactory("NettyBossNio"));
            this.eventLoopGroupWork = new NioEventLoopGroup(haafizConfig.getEventLoopGroupWorkNum(),
                    new DefaultThreadFactory("NettyWorkNio"));
        }
    }

    public boolean useEPoll() {
        return haafizConfig.isUseEPoll() && RemotingUtil.isLinuxPlatform() && Epoll.isAvailable();
    }


    @Override
    protected void doStart() {
        ServerBootstrap handler = this.serverBootstrap
                .group(eventLoopGroupBoss, eventLoopGroupWork)
                .channel(useEPoll() ? EpollServerSocketChannel.class : NioServerSocketChannel.class)
                .option(ChannelOption.SO_BACKLOG, 1024)			//	sync + accept = backlog
                .option(ChannelOption.SO_REUSEADDR, true)   	//	tcp端口重绑定
                .option(ChannelOption.SO_KEEPALIVE, false)  	//  如果在两小时内没有数据通信的时候，TCP会自动发送一个活动探测数据报文
                .childOption(ChannelOption.TCP_NODELAY, true)   //	该参数的左右就是禁用Nagle算法，使用小数据传输时合并
                .childOption(ChannelOption.SO_SNDBUF, 65535)	//	设置发送数据缓冲区大小
                .childOption(ChannelOption.SO_RCVBUF, 65535)	//	设置接收数据缓冲区大小
                .localAddress(new InetSocketAddress(this.port))
                .childHandler(new ChannelInitializer<Channel>() {

                    @Override
                    protected void initChannel(Channel ch) throws Exception {
                        ch.pipeline().addLast(
                                new HttpServerCodec(),
                                new HttpObjectAggregator(haafizConfig.getMaxContentLength()),
                                new HttpServerExpectContinueHandler(),
                                new NettyServerConnectManagerHandler(),
                                new NettyHttpServerHandler(nettyProcessor)
                        );
                    }
                });

        if(haafizConfig.isNettyAllocator()) {
            handler.childOption(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT);
        }

        try {
            this.serverBootstrap.bind().sync();
            log.info("< ============= Rapid Server StartUp On Port: " + this.port + "================ >");
        } catch (Exception e) {
            throw new RuntimeException("this.serverBootstrap.bind().sync() fail!", e);
        }
    }

    @Override
    protected void doStop() {
        if(eventLoopGroupBoss != null) {
            eventLoopGroupBoss.shutdownGracefully();
        }
        if(eventLoopGroupWork != null) {
            eventLoopGroupWork.shutdownGracefully();
        }
    }

    @Override
    protected void doClose() throws IOException {

    }

    public EventLoopGroup getEventLoopGroupWork() {
        return eventLoopGroupWork;
    }

    static class NettyServerConnectManagerHandler extends ChannelDuplexHandler {

        @Override
        public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
            final String remoteAddr = RemotingHelper.parseChannelRemoteAddr(ctx.channel());
            log.debug("NETTY SERVER PIPLINE: channelRegistered {}", remoteAddr);
            super.channelRegistered(ctx);
        }

        @Override
        public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
            final String remoteAddr = RemotingHelper.parseChannelRemoteAddr(ctx.channel());
            log.debug("NETTY SERVER PIPLINE: channelUnregistered {}", remoteAddr);
            super.channelUnregistered(ctx);
        }

        @Override
        public void channelActive(ChannelHandlerContext ctx) throws Exception {
            final String remoteAddr = RemotingHelper.parseChannelRemoteAddr(ctx.channel());
            log.debug("NETTY SERVER PIPLINE: channelActive {}", remoteAddr);
            super.channelActive(ctx);
        }

        @Override
        public void channelInactive(ChannelHandlerContext ctx) throws Exception {
            final String remoteAddr = RemotingHelper.parseChannelRemoteAddr(ctx.channel());
            log.debug("NETTY SERVER PIPLINE: channelInactive {}", remoteAddr);
            super.channelInactive(ctx);
        }

        @Override
        public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
            if(evt instanceof IdleStateEvent) {
                IdleStateEvent event = (IdleStateEvent)evt;
                if(event.state().equals(IdleState.ALL_IDLE)) {
                    final String remoteAddr = RemotingHelper.parseChannelRemoteAddr(ctx.channel());
                    log.warn("NETTY SERVER PIPLINE: userEventTriggered: IDLE {}", remoteAddr);
                    ctx.channel().close();
                }
            }
            ctx.fireUserEventTriggered(evt);
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
                throws Exception {
            final String remoteAddr = RemotingHelper.parseChannelRemoteAddr(ctx.channel());
            log.warn("NETTY SERVER PIPLINE: remoteAddr： {}, exceptionCaught {}", remoteAddr, cause);
            ctx.channel().close();
        }

    }
}
