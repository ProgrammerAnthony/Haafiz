package com.haafiz.core.netty;

import com.haafiz.common.lifecycle.AbstractLifecycleComponent;
import com.haafiz.core.HaafizConfig;
import com.haafiz.core.util.AsyncHttpHelper;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.EventLoopGroup;
import lombok.extern.slf4j.Slf4j;
import org.asynchttpclient.AsyncHttpClient;
import org.asynchttpclient.DefaultAsyncHttpClient;
import org.asynchttpclient.DefaultAsyncHttpClientConfig;

import java.io.IOException;

/**
 * @author Anthony
 * @create 2021/12/16
 * @desc for downstream connection http client
 */
@Slf4j
public class NettyHttpClient extends AbstractLifecycleComponent {

    private final HaafizConfig haafizConfig;
    private final EventLoopGroup eventLoopGroupWork;
    private AsyncHttpClient asyncHttpClient;
    private DefaultAsyncHttpClientConfig.Builder clientBuilder;


    public NettyHttpClient(HaafizConfig haafizConfig, EventLoopGroup eventLoopGroupWork) {
        this.haafizConfig = haafizConfig;
        this.eventLoopGroupWork = eventLoopGroupWork;
        this.clientBuilder = new DefaultAsyncHttpClientConfig.Builder().
                setFollowRedirect(false)
                .setEventLoopGroup(eventLoopGroupWork)
                .setConnectTimeout(haafizConfig.getHttpConnectTimeout())
                .setRequestTimeout(haafizConfig.getHttpRequestTimeout())
                .setMaxRequestRetry(haafizConfig.getHttpMaxRequestRetry())
                .setAllocator(PooledByteBufAllocator.DEFAULT)
                .setCompressionEnforced(true)
                .setMaxConnections(haafizConfig.getHttpMaxConnections())
                .setMaxConnectionsPerHost(haafizConfig.getHttpConnectionsPerHost())
                .setPooledConnectionIdleTimeout(haafizConfig.getHttpPooledConnectionIdleTimeout());
    }

    @Override
    protected void doStart() {
        this.asyncHttpClient = new DefaultAsyncHttpClient(clientBuilder.build());
        AsyncHttpHelper.getInstance().initialized(asyncHttpClient);
    }

    @Override
    protected void doStop() {
        if (asyncHttpClient != null) {
            try {
                asyncHttpClient.close();
            } catch (IOException e) {
                e.printStackTrace();
                log.error("NettyHttpClient shutdown error");
            }
        }
    }

    @Override
    protected void doClose() throws IOException {

    }
}
