package com.haafiz.core;

import com.haafiz.common.constants.BasicConst;
import com.haafiz.common.util.NetUtils;
import com.lmax.disruptor.*;
import lombok.Data;

/**
 * @author Anthony
 * @create 2021/12/16
 * @desc
 */
@Data
public class HaafizConfig {
    private int port = 8888;

    //	service unique ID： haafizId  192.168.11.111:8888
    private String haafizId = NetUtils.getLocalIp() + BasicConst.COLON_SEPARATOR + port;

    //	service registry center address
    private String registerAddress = "http://192.168.11.114:2379,http://192.168.11.115:2379,http://192.168.11.116:2379";

    //	namespace used to differentiate environment：dev test prod
    private String nameSpace = "haafiz-dev";


    private int processThread = Runtime.getRuntime().availableProcessors();

    // 	Netty boss threads count
    private int eventLoopGroupBossNum = 1;

    //	Netty work threads count
    private int eventLoopGroupWorkNum = processThread;

    //	whether use epoll
    private boolean useEPoll = true;

    //	whether enable netty memory allocator
    private boolean nettyAllocator = true;

    //	http body max size
    private int maxContentLength = 64 * 1024 * 1024;

    //	dubbo connections
    private int dubboConnections = processThread;

    //	response mode  default single asynchronization ,use CompletableFuture ： whenComplete  or  whenCompleteAsync
    private boolean whenComplete = true;

    //	buffer type is a gateway queue config
    private String bufferType = ""; // HaafizBufferHelper.FLUSHER;

    //	memory queue size
    private int bufferSize = 1024 * 16;

    //blocking or waiting
    private String waitStrategy = "blocking";


    //	Http Async params：

    private int httpConnectTimeout = 30 * 1000;

    private int httpRequestTimeout = 30 * 1000;

    private int httpMaxRequestRetry = 2;

    private int httpMaxConnections = 10000;

    private int httpConnectionsPerHost = 8000;

    private int httpPooledConnectionIdleTimeout = 60 * 1000;


    public WaitStrategy getATrueWaitStrategy() {
        switch (waitStrategy) {
            case "blocking":
                return new BlockingWaitStrategy();
            case "busySpin":
                return new BusySpinWaitStrategy();
            case "yielding":
                return new YieldingWaitStrategy();
            case "sleeping":
                return new SleepingWaitStrategy();
            default:
                return new BlockingWaitStrategy();
        }
    }
}
