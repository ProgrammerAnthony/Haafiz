package com.anthony.library.data.net;




import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Url;
import rx.Observable;

/**
 * Created by Anthony on 2016/7/8.
 * Class Note:
 * normal interface class of api to load String or post String ,replace {@link #end_point}
 * with your own base url
 *
 * 常用API接口类用于加载和post 字符串操作，请在 {@link #end_point}中替换基地址
 */
public interface RemoteApi {
    String end_point = "";//not used

    @GET("{url}")
    Observable<ResponseBody> loadString(@Path(value = "url", encoded = true) String url);

    @POST
    @FormUrlEncoded
    Observable<ResponseBody> postString(@Url String url, @FieldMap Map<String, String> params);
}