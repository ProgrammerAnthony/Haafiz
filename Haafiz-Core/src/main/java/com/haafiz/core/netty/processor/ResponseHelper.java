package com.haafiz.core.netty.processor;

import com.haafiz.common.exception.ResponseCode;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.http.*;

/**
 * @author Anthony
 * @create 2021/12/26
 * @desc
 */
public class ResponseHelper {
    public static FullHttpResponse getHttpResponse(ResponseCode internalError) {
        String errorContent = "internal error";
        DefaultFullHttpResponse httpResponse = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1,
                HttpResponseStatus.INTERNAL_SERVER_ERROR,
                Unpooled.wrappedBuffer(errorContent.getBytes()));

        httpResponse.headers().set(HttpHeaderNames.CONTENT_TYPE, HttpHeaderValues.APPLICATION_JSON + ";charset=utf-8");
        httpResponse.headers().set(HttpHeaderNames.CONTENT_LENGTH, errorContent.length());
        return httpResponse;
    }
}
