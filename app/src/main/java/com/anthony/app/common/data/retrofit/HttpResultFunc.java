package com.anthony.app.common.data.retrofit;

import rx.functions.Func1;

/**
 * Created by Anthony on 2016/7/8.
 * Class Note:
 * Rx {@link Func1}wrapper for result ，return {@link HttpResult#response}
 */
public class HttpResultFunc<T> implements Func1<HttpResult<T>, T> {
    @Override
    public T call(HttpResult<T> httpResult) {
        if (httpResult.code != 0) {
            throw new RuntimeException(httpResult.message);
        }
        return httpResult.response;
    }
}