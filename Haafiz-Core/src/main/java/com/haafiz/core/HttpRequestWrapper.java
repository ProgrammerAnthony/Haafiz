package com.haafiz.core;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;
import lombok.Data;

/**
 * @author Anthony
 * @create 2021/12/16
 * @desc http request wrapper
 */
@Data
public class HttpRequestWrapper {

	private FullHttpRequest fullHttpRequest;
	
	private ChannelHandlerContext ctx;
	
}
