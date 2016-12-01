package com.anthony.app.module.wechatlist;


import com.anthony.library.data.net.HttpResult;

import java.util.List;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

public interface WechatApi {
    String end_point = "http://api.tianapi.com/";

    /**
     * 微信精选列表
     */
    @GET("wxnew")
    Observable<HttpResult<List<WXItemBean>>> getWXHot(@Query("key") String key, @Query("num") int num, @Query("page") int page);


}
