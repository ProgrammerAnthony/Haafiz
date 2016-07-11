package edu.com.app.data.http;

import rx.functions.Func1;

/**
 * Created by Anthony on 2016/7/8.
 * Class Note:
 * 利用Rx操作符{@link Func1}对返回结果的封装，直接返回数据{@link HttpResult#data}
 */
public class HttpResultFunc<T> implements Func1<HttpResult<T>, T> {
    @Override
    public T call(HttpResult<T> httpResult) {
        if (httpResult.code != 0) {
            throw new RuntimeException(httpResult.message);
        }
        return httpResult.data;
    }
}