package com.anthony.library.data.net;


import com.anthony.library.utils.LogUtil;

import rx.functions.Func1;


/**
 * Created by Anthony on 2016/7/8.
 * unify operation resultCode from Http response,
 * return {@link HttpResult#response} to {@link rx.Subscriber}
 *
 * @param <T> what the  {@link rx.Subscriber} really needs ，the {@link HttpResult#response}
 *            <p>
 *            用来统一处理Http的resultCode,并将HttpResult的Data部分剥离出来返回给subscriber
 * @param <T> Subscriber真正需要的数据类型，也就是Data部分的数据类型
 */
public class HttpResultFunc<T> implements Func1<HttpResult<T>, T> {
    @Override
    public T call(HttpResult<T> httpResult) {
        if (httpResult.code != 200) {
            LogUtil.i("response code is not 200,response error");
            throw new RuntimeException(httpResult.message);
        }
        return httpResult.response;
    }
}