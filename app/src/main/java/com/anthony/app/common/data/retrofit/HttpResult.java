package com.anthony.app.common.data.retrofit;

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
    public String message;
    @SerializedName(value = "data", alternate = {"datas","response"})
    public T response;
}
