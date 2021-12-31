package com.haafiz.core.context;

import com.google.common.collect.Lists;
import com.haafiz.common.constants.BasicConst;
import com.haafiz.common.util.TimeUtil;
import com.jayway.jsonpath.JsonPath;
import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http.cookie.Cookie;
import io.netty.handler.codec.http.cookie.ServerCookieDecoder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.asynchttpclient.Request;
import org.asynchttpclient.RequestBuilder;

import java.nio.charset.Charset;
import java.util.*;

/**
 * @author Anthony
 * @create 2021/12/30
 * @desc
 */
@Slf4j
public class HaafizRequest implements HaafizRequestMutable {


    /**
     * required in header, which is "serviceId:version"
     */
    @Getter
    private final String uniqueId;

    /**
     * 	begin time of enter Haafiz flow
     */
    @Getter
    private final long beginTime;

    @Getter
    private final Charset charset;

    /**
     * 	clietn ip address：flow control, white list
     */
    @Getter
    private final String clientIp;

    /**
     * 	request address：ip:port
     */
    @Getter
    private final String host;

    /**
     * 	request path：/xxx/xx/xxx
     */
    @Getter
    private final String path;

    /**
     * uri: /xxx/xx/xxx?attr1=value1&attr2=value2
     */
    @Getter
    private final String uri;

    /**
     * 	request method：get/post/put...
     */
    @Getter
    private final HttpMethod method;


    @Getter
    private final String contentType;

    /**
     * 	http header
     */
    @Getter
    private final HttpHeaders headers;

    /**
     * query param decoder from netty
     */
    @Getter
    private final QueryStringDecoder queryDecoder;

    /**
     *	FullHttpRequest from netty
     */
    @Getter
    private final FullHttpRequest fullHttpRequest;


    private String body;


    private Map<String, Cookie> cookieMap;


    private Map<String, List<String>> postParameters;

    /***************** RapidRequestMutable:mutable object 	**********************/

    /**
     * 	scheme：default http://
     */
    private String modifyScheme;
    private String modifyHost;
    private String modifyPath;

    /**
     * 	http request builder for down-stream request
     */
    private final RequestBuilder requestBuilder;


    public HaafizRequest(String uniqueId, Charset charset, String clientIp, String host,
                        String uri, HttpMethod method, String contentType, HttpHeaders headers, FullHttpRequest fullHttpRequest) {
        this.uniqueId = uniqueId;
        this.beginTime = TimeUtil.currentTimeMillis();
        this.charset = charset;
        this.clientIp = clientIp;
        this.host = host;
        this.method = method;
        this.contentType = contentType;
        this.headers = headers;
        this.uri = uri;
        this.queryDecoder = new QueryStringDecoder(uri, charset);
        this.path = queryDecoder.path();
        this.fullHttpRequest = fullHttpRequest;

        this.modifyHost = host;
        this.modifyPath = path;
        this.modifyScheme = BasicConst.HTTP_PREFIX_SEPARATOR;
        this.requestBuilder = new RequestBuilder();
        this.requestBuilder.setMethod(getMethod().name());
        this.requestBuilder.setHeaders(getHeaders());
        this.requestBuilder.setQueryParams(queryDecoder.parameters());
        ByteBuf contentBuffer = fullHttpRequest.content();
        if(Objects.nonNull(contentBuffer)) {
            this.requestBuilder.setBody(contentBuffer.nioBuffer());
        }
    }


    public String getBody() {
        if(StringUtils.isEmpty(body)) {
            body = fullHttpRequest.content().toString(charset);
        }
        return body;
    }


    public Cookie getCookie(String name) {
        if(cookieMap == null) {
            cookieMap = new HashMap<String, Cookie>();
            String cookieStr = getHeaders().get(HttpHeaderNames.COOKIE);
            Set<Cookie> cookies = ServerCookieDecoder.STRICT.decode(cookieStr);
            for(Cookie cookie : cookies) {
                cookieMap.put(name, cookie);
            }
        }
        return cookieMap.get(name);
    }


    public List<String> getQueryParametersMultiple(String name){
        return queryDecoder.parameters().get(name);
    }

    public List<String> getPostParametersMultiple(String name){
        String body = getBody();
        if(isFormPost()) {
            if(postParameters == null) {
                QueryStringDecoder paramDecoder = new QueryStringDecoder(body, false);
                postParameters = paramDecoder.parameters();
            }

            if(postParameters == null || postParameters.isEmpty()) {
                return null;
            } else {
                return postParameters.get(name);
            }

        } else if (isJsonPost()) {
            try {
                return Lists.newArrayList(JsonPath.read(body, name).toString());
            } catch (Exception e) {
                //	ignore
                log.error("#RapidRequest# getPostParametersMultiple JsonPath parse fail，jsonPath: {}, body: {}", name, body, e);
            }
        }
        return null;
    }


    @Override
    public Request build() {
        requestBuilder.setUrl(getFinalUrl());
        return requestBuilder.build();
    }

    @Override
    public String getFinalUrl() {
        return modifyScheme + modifyHost + modifyPath;
    }


    @Override
    public void setModifyHost(String modifyHost) {
        this.modifyHost = modifyHost;
    }

    @Override
    public String getModifyHost() {
        return modifyHost;
    }

    @Override
    public void setModifyPath(String modifyPath) {
        this.modifyPath = modifyPath;
    }

    @Override
    public String getModifyPath() {
        return modifyPath;
    }

    @Override
    public void addHeader(CharSequence name, String value) {
        requestBuilder.addHeader(name, value);
    }

    @Override
    public void setHeader(CharSequence name, String value) {
        requestBuilder.setHeader(name, value);
    }

    @Override
    public void addQueryParam(String name, String value) {
        requestBuilder.addQueryParam(name, value);
    }

    @Override
    public void addOrReplaceCookie(org.asynchttpclient.cookie.Cookie cookie) {
        requestBuilder.addOrReplaceCookie(cookie);
    }

    @Override
    public void addFormParam(String name, String value) {
        if(isFormPost()) {
            requestBuilder.addFormParam(name, value);
        }
    }

    @Override
    public void setRequestTimeout(int requestTimeout) {
        requestBuilder.setRequestTimeout(requestTimeout);
    }

    public boolean isFormPost() {
        return HttpMethod.POST.equals(method) &&
                (contentType.startsWith(HttpHeaderValues.FORM_DATA.toString()) ||
                        contentType.startsWith(HttpHeaderValues.APPLICATION_X_WWW_FORM_URLENCODED.toString()));
    }

    public boolean isJsonPost() {
        return HttpMethod.POST.equals(method) &&
                contentType.startsWith(HttpHeaderValues.APPLICATION_JSON.toString());
    }

}
