package com.anthony.app.common.data.net;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Anthony on 2016/7/8.
 * Class Note:
 * wrapper for return result，include {@link #code}， {@link #message}
 * and {@link #response}
 *
 * 对请求结果返回对象的封装，包含{@link #code}， {@link #message}
 * 和{@link #response}
 */
public class HttpResult<T> {
    public int code;
    @SerializedName(value = "message", alternate = {"msg"})
    public String message;
    @SerializedName(value = "data", alternate = {"datas","response","newslist"})
    public T response;
}
