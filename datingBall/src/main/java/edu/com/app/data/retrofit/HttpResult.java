package edu.com.app.data.retrofit;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Anthony on 2016/7/8.
 * Class Note:
 * wrapper for return result，include {@link #code}， {@link #message}
 * and {@link #data}
 */
public class HttpResult<T> {

    public int code;

    public String message;

    @SerializedName(value = "data", alternate = {"datas","channels"})
    public T data;
}
