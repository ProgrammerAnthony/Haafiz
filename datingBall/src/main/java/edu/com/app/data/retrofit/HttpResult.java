package edu.com.app.data.retrofit;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Anthony on 2016/7/8.
 * Class Note:
 * 返回结果的封装，包含返回码{@link #code}，返回标识{@link #message}
 * 和返回数据{@link #data}
 */
public class HttpResult<T> {
    public int code;
    public String message;
    @SerializedName(value = "data", alternate = {"datas"})
    public T data;
}
