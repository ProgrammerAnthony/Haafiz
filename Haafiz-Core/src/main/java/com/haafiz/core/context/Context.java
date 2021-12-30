package com.haafiz.core.context;

import io.netty.channel.ChannelHandlerContext;

import java.util.function.Consumer;

/**
 * @author Anthony
 * @create 2021/12/30
 * @desc common process for environment context
 */
public interface Context {

    int RUNNING = -1;

    int WRITTEN = 0;

    int COMPLETED = 1;

    int TERMINATED = 2;

    /*************** -- set gateway status -- ********************/


    void runned();


    void writtened();


    void completed();


    void terminated();

    /*************** -- judge gateway status -- ********************/

    boolean isRunning();

    boolean isWrittened();

    boolean isCompleted();

    boolean isTerminated();


    String getProtocol();


    Rule getRule();


    Object getRequest();


    Object getResponse();



    void setResponse(Object response);


    void setThrowable(Throwable throwable);


    Throwable getThrowable();


    <T> T getAttribute(AttributeKey<T> key);


    <T> T putAttribute(AttributeKey<T> key, T value);


    ChannelHandlerContext getNettyCtx();


    boolean isKeepAlive();


    void releaseRequest();


    void completedCallback(Consumer<Context> consumer);

    void invokeCompletedCallback();

}
