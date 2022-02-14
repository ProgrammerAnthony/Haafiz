package com.haafiz.core.context;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.haafiz.common.exception.ResponseCode;
import com.haafiz.common.util.JSONUtil;
import io.netty.handler.codec.http.*;
import lombok.Data;
import org.asynchttpclient.Response;

/**
 * @author Anthony
 * @create 2021/12/30
 * @desc
 */
@Data
public class HaafizResponse {

    //	响应头
    private HttpHeaders responseHeaders = new DefaultHttpHeaders();

    //	额外的响应结果
    private final HttpHeaders extraResponseHeaders = new DefaultHttpHeaders();

    //	返回的响应内容
    private String content;

    //	返回响应状态码
    private HttpResponseStatus httpResponseStatus;

    //	响应对象
    private Response futureResponse;

    private HaafizResponse() {
    }


    public void putHeader(CharSequence key, CharSequence val) {
        responseHeaders.add(key, val);
    }


    public static HaafizResponse buildHaafizResponse(Response futureResponse) {
        HaafizResponse haafizResponse = new HaafizResponse();
        haafizResponse.setFutureResponse(futureResponse);
        haafizResponse.setHttpResponseStatus(HttpResponseStatus.valueOf(futureResponse.getStatusCode()));
        return haafizResponse;
    }


    /**
     * for fail response
     * @param code
     * @param args
     * @return
     */
    public static HaafizResponse buildHaafizResponse(ResponseCode code, Object... args) {
        ObjectNode objectNode = JSONUtil.createObjectNode();
        objectNode.put(JSONUtil.STATUS, code.getStatus().code());
        objectNode.put(JSONUtil.CODE, code.getCode());
        objectNode.put(JSONUtil.MESSAGE, code.getMessage());
        HaafizResponse haafizResponse = new HaafizResponse();
        haafizResponse.setHttpResponseStatus(code.getStatus());
        haafizResponse.putHeader(HttpHeaderNames.CONTENT_TYPE,
                HttpHeaderValues.APPLICATION_JSON + ";charset=utf-8");
        haafizResponse.setContent(JSONUtil.toJSONString(objectNode));
        return haafizResponse;
    }


    /**
     * for success response
     * @param data
     * @return
     */
    public static HaafizResponse buildHaafizResponseObj(Object data) {
        ObjectNode objectNode = JSONUtil.createObjectNode();
        objectNode.put(JSONUtil.STATUS, ResponseCode.SUCCESS.getStatus().code());
        objectNode.put(JSONUtil.CODE, ResponseCode.SUCCESS.getCode());
        objectNode.putPOJO(JSONUtil.DATA, data);
        HaafizResponse haafizResponse = new HaafizResponse();
        haafizResponse.setHttpResponseStatus(ResponseCode.SUCCESS.getStatus());
        haafizResponse.putHeader(HttpHeaderNames.CONTENT_TYPE,
                HttpHeaderValues.APPLICATION_JSON + ";charset=utf-8");
        haafizResponse.setContent(JSONUtil.toJSONString(objectNode));
        return haafizResponse;
    }


}

