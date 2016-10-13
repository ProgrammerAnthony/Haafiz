package com.anthony.app.common.data.retrofit;


import com.anthony.app.common.base.Constants;
import com.anthony.app.common.data.bean.WeatherData;

import java.util.Map;

import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * Created by Anthony on 2016/9/22.
 * Class Note:
 * Weather API
 * entity class {@link com.anthony.app.common.data.bean.WeatherData}
 * data from {@see "http://apistore.baidu.com/apiworks/servicedetail/2573.html?qq-pf-to=pcqq.c2c"}
 *
 * 天气api接口
 * 实体类{@link com.anthony.app.common.data.bean.WeatherData}
 * 天气数据来自 http://apistore.baidu.com/apiworks/servicedetail/2573.html?qq-pf-to=pcqq.c2c
 */

public interface WeatherApi {
    String end_point = Constants.REMOTE_BASE_END_POINT_WEATHER;

    //example , remember to add a apikey to your header
// "http://apis.baidu.com/thinkpage/weather_api/suggestion?location=beijing&language=zh-Hans&unit=c&start=0&days=3";

    @Headers("apikey: 87f4cacc3ffe1f1025ebf1ea415ff112")
    @GET("/thinkpage/weather_api/suggestion")
    Observable<WeatherData> loadWeatherData(@QueryMap Map<String,String> params);
}
