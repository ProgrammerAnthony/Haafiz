package com.anthony.library.data.download;


import com.anthony.library.Constants;

import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Streaming;
import retrofit2.http.Url;
import rx.Observable;

/**
 * Created by Anthony on 2016/6/12.
 * Class Note:
 * download API
 */
public interface DownloadApi {
    String end_point = Constants.Remote_BASE_END_POINT;

    @Streaming
    @GET
    Observable<ResponseBody> downloadFile(@Url String url);
}
