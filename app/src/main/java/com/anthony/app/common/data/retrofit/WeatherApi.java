package com.anthony.app.common.data.retrofit;


import com.anthony.app.common.base.Constants;

import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by Anthony on 2016/9/22.
 * Class Note:
 * 天气api接口
 * 实体类{@link com.anthony.app.common.data.bean.WeatherData}
 * 天气数据来自 http://apistore.baidu.com/apiworks/servicedetail/2573.html?qq-pf-to=pcqq.c2c
 */

public interface WeatherApi {
    String end_point = Constants.REMOTE_BASE_END_POINT_WEATHER;

    //天气示例 ，需要添加apiKey到header
// "http://apis.baidu.com/thinkpage/weather_api/suggestion?location=beijing&language=zh-Hans&unit=c&start=0&days=3";
    @Headers("apikey: 87f4cacc3ffe1f1025ebf1ea415ff112")
    @GET("/thinkpage/weather_api/suggestion")
    Observable<ResponseBody> loadWeatherData(@Query("location") String location,//位置,拼音
                                             @Query(value = "language", encoded = true) String language,//语言，默认使用zh-Hans
                                             @Query("unit") String unit,//单位，默认为c
                                             @Query("start") String start,//起始时间
                                             @Query("days") String days);//天数
}
