package com.haafiz.core.context;

import org.asynchttpclient.Request;
/**
 * @author Anthony
 * @create 2021/12/30
 * @desc mutable request object
 */
public interface HaafizRequestMutable {


    void setModifyHost(String host);


    String getModifyHost();


    void setModifyPath(String path);


    String getModifyPath();


    void addHeader(CharSequence name, String value);


    void setHeader(CharSequence name, String value);


    void addQueryParam(String name, String value);



    void addOrReplaceCookie(org.asynchttpclient.cookie.Cookie cookie);


    void addFormParam(String name, String value);


    void setRequestTimeout(int requestTimeout);


    Request build();

    String getFinalUrl();

}
