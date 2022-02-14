package com.haafiz.core;


import com.haafiz.common.config.DynamicConfigManager;
import com.haafiz.common.config.Rule;
import com.haafiz.common.config.ServiceDefinition;
import com.haafiz.common.config.ServiceInvoker;
import com.haafiz.common.constants.HaafizConst;
import com.haafiz.common.constants.HaafizProtocol;
import com.haafiz.common.exception.HaafizResponseException;
import com.haafiz.common.exception.ResponseCode;
import com.haafiz.common.util.AntPathMatcher;
import com.haafiz.core.context.AttributeKey;
import com.haafiz.core.context.HaafizContext;
import com.haafiz.core.context.HaafizRequest;
import com.haafiz.core.exception.HaafizNotFoundException;
import com.haafiz.core.exception.HaafizPathNoMatchedException;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.*;
import org.apache.commons.lang3.StringUtils;

import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.Map;


/**
 * @author Anthony
 * @create 2021/12/16
 * @desc parse request and build context
 */
public class RequestHelper {
	
	private static final AntPathMatcher ANT_PATH_MATCHER = new AntPathMatcher();

	/**
	 * parse FullHttpRequest , core process
	 * @param request
	 * @param ctx
	 * @return
	 */
	public static HaafizContext doContext(FullHttpRequest request, ChannelHandlerContext ctx) {
		
		//	1. build request
		HaafizRequest haafizRequest = doRequest(request, ctx);
		
		//	2.	get ServiceDefinition by uniqueId
		ServiceDefinition serviceDefinition = getServiceDefinition(haafizRequest);
		
		//	3.	fail-fast strategy for path match
		if(!ANT_PATH_MATCHER.match(serviceDefinition.getPatternPath(), haafizRequest.getPath())) {
			throw new HaafizPathNoMatchedException();
		}
		
		//	4. use request object to load ServiceInvoker and corresponding ruleId
		ServiceInvoker serviceInvoker = getServiceInvoker(haafizRequest, serviceDefinition);
		String ruleId = serviceInvoker.getRuleId();
		Rule rule = DynamicConfigManager.getInstance().getRule(ruleId);
		
		//	5. build context
		HaafizContext haafizContext = new HaafizContext.Builder()
				.setProtocol(serviceDefinition.getProtocol())
				.setHaafizRequest(haafizRequest)
				.setNettyCtx(ctx)
				.setKeepAlive(HttpUtil.isKeepAlive(request))
				.setRule(rule)
				.build();

		putContext(haafizContext, serviceInvoker);
		
		return haafizContext;
	}
	

	/**
	 * build HaafizRequest
	 * @param fullHttpRequest
	 * @param ctx
	 * @return
	 */
	private static HaafizRequest doRequest(FullHttpRequest fullHttpRequest, ChannelHandlerContext ctx) {
		
		HttpHeaders headers = fullHttpRequest.headers();
		//	core property: uniqueId
		String uniqueId = headers.get(HaafizConst.UNIQUE_ID);
		
		if(StringUtils.isBlank(uniqueId)) {
			throw new HaafizResponseException(ResponseCode.REQUEST_PARSE_ERROR_NO_UNIQUEID);
		}
		
		String host = headers.get(HttpHeaderNames.HOST);
		HttpMethod method = fullHttpRequest.method();
		String uri = fullHttpRequest.uri();
		String clientIp = getClientIp(ctx, fullHttpRequest);
		String contentType = HttpUtil.getMimeType(fullHttpRequest) == null ? null : HttpUtil.getMimeType(fullHttpRequest).toString();
		Charset charset = HttpUtil.getCharset(fullHttpRequest, StandardCharsets.UTF_8);

		HaafizRequest haafizRequest = new HaafizRequest(uniqueId,
				charset,
				clientIp,
				host, 
				uri, 
				method,
				contentType,
				headers,
				fullHttpRequest);
		
		return haafizRequest;
	}
	

	private static String getClientIp(ChannelHandlerContext ctx, FullHttpRequest request) {
		String xForwardedValue = request.headers().get(BasicConst.HTTP_FORWARD_SEPARATOR);
		
		String clientIp = null;
		if(StringUtils.isNotEmpty(xForwardedValue)) {
			List<String> values = Arrays.asList(xForwardedValue.split(", "));
			if(values.size() >= 1 && StringUtils.isNotBlank(values.get(0))) {
				clientIp = values.get(0);
			}
		}
		if(clientIp == null) {
			InetSocketAddress inetSocketAddress = (InetSocketAddress)ctx.channel().remoteAddress();
			clientIp = inetSocketAddress.getAddress().getHostAddress();
		}
		return clientIp;
	}


	/**
	 * get request object with request
	 * @param haafizRequest
	 * @return
	 */
	private static ServiceDefinition getServiceDefinition(HaafizRequest haafizRequest) {
		ServiceDefinition serviceDefinition = DynamicConfigManager.getInstance().getServiceDefinition(haafizRequest.getUniqueId());
		if(serviceDefinition == null) {
			throw new HaafizNotFoundException(ResponseCode.SERVICE_DEFINITION_NOT_FOUND);
		}
		return serviceDefinition;
	}


	/**
	 *  get request object with request and ServiceDefinition
	 * @param haafizRequest
	 * @param serviceDefinition
	 * @return
	 */
	private static ServiceInvoker getServiceInvoker(HaafizRequest haafizRequest, ServiceDefinition serviceDefinition) {
		Map<String, ServiceInvoker> invokerMap = serviceDefinition.getInvokerMap();
		ServiceInvoker serviceInvoker = invokerMap.get(haafizRequest.getPath());
		if(serviceInvoker == null) {
			throw new HaafizNotFoundException(ResponseCode.SERVICE_INVOKER_NOT_FOUND);
		}
		return serviceInvoker;
	}


	/**
	 * set required context
	 * @param haafizContext
	 * @param serviceInvoker
	 */
	private static void putContext(HaafizContext haafizContext, ServiceInvoker serviceInvoker) {
		switch (haafizContext.getProtocol()) {
			case HaafizProtocol.HTTP:
				haafizContext.putAttribute(AttributeKey.HTTP_INVOKER, serviceInvoker);
				break;
			case HaafizProtocol.DUBBO:
				haafizContext.putAttribute(AttributeKey.DUBBO_INVOKER, serviceInvoker);
				break;
			default:
				break;
		}
	}

	
}
