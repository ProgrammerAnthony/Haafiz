package com.anthony.app.common;

import android.content.Context;
import android.content.Intent;

import com.anthony.app.common.data.EventPosterHelper;
import com.anthony.app.common.data.HttpHelper;
import com.anthony.app.common.data.PreferencesHelper;
import com.anthony.app.common.data.RxBus;
import com.anthony.app.common.data.bean.NewsItem;
import com.anthony.app.common.data.bean.NormalJsonInfo;
import com.anthony.app.common.data.bean.WeatherData;
import com.anthony.app.common.data.database.DatabaseHelper;
import com.anthony.app.common.data.download.DownloadEvent;
import com.anthony.app.common.data.download.DownloadFinishEvent;
import com.anthony.app.common.data.download.DownloadService;
import com.anthony.app.common.data.net.HttpResult;
import com.anthony.app.common.data.net.HttpResultFunc;
import com.anthony.app.common.data.net.RemoteApi;
import com.anthony.app.common.data.upload.UploadParam;
import com.anthony.app.common.data.upload.UploadService;
import com.anthony.app.common.injection.scope.ApplicationContext;
import com.anthony.app.common.utils.FileUtil;
import com.anthony.app.common.utils.RxUtils;
import com.anthony.app.common.utils.SpUtil;
import com.anthony.app.module.github.GithubApi;
import com.anthony.app.module.github.GithubUser;
import com.anthony.app.module.weather.WeatherApi;
import com.anthony.app.module.wechatlist.WXItemBean;
import com.anthony.app.module.wechatlist.WechatApi;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import okhttp3.ResponseBody;
import rx.Observable;
import rx.exceptions.Exceptions;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func1;
import timber.log.Timber;

/**
 * Created by Anthony on 2016/6/12.
 * Class Note:
 * data entrance of all kinds of data
 * using {@link HttpHelper},{@link PreferencesHelper},
 * {@link EventPosterHelper} ，
 * {@link com.anthony.app.common.data.database.DatabaseHelper}
 * and {@link RxBus} to access data
 * <p>
 * 所有数据的入口类
 */
public class DataManager {

    @Inject
    HttpHelper mHttpHelper;

    @Inject
    PreferencesHelper mPreferencesHelper;

    @Inject
    EventPosterHelper mEventPoster; //otto

    @Inject
    DatabaseHelper mDatabaseHelper;
//    @Inject
//    RxBus mRxBus;

    private Context mContext;

    @Inject
    public DataManager(@ApplicationContext Context context) {
        this.mContext = context;

    }

    public DatabaseHelper getDatabaseHelper() {
        return mDatabaseHelper;
    }

    public PreferencesHelper getPreferencesHelper() {
        return mPreferencesHelper;
    }


//    public RxBus getRxBus() {
//        return mRxBus;
//    }

    private Action0 postEventAction(final Object event) {
        return new Action0() {
            @Override
            public void call() {
                mEventPoster.postEventSafely(event);
            }
        };
    }

    /**
     * load weather data from Baidu API
     *
     * @param location
     * @return
     */
    public Observable<WeatherData> loadWeatherData(String location) {
        Map<String, String> params = new HashMap<>();
        params.put("location", location);
        params.put("language", "zh-Hans");
        params.put("unit", "c");
        params.put("start", "0");
        params.put("days", "3");
        return mHttpHelper.getApi(WeatherApi.class)
                .loadWeatherData(params)
                .compose(RxUtils.defaultSchedulers());
    }

    /**
     * load  following list of github users
     *
     * @return Observable<String>
     */
    public Observable<String> loadUserFollowingString(String userName) {
        return mHttpHelper.getApi(GithubApi.class)
                .loadUserFollowingString(userName)
                .flatMap(new Func1<ResponseBody, Observable<String>>() {
                    @Override
                    public Observable<String> call(ResponseBody responseBody) {
                        try {
                            String result = responseBody.string();
                            return Observable.just(result);
                        } catch (IOException e) {
                            e.printStackTrace();
                            throw new RuntimeException("IOException when convert Response Body to String");
                        }
                    }
                })
                .compose(RxUtils.defaultSchedulers());
    }

    /**
     * load  following list of github users
     *
     * @return Observable<List<GithubUser>>
     */
    public Observable<List<GithubUser>> loadUserFollowingList(String userName) {
        return mHttpHelper.getApi(GithubApi.class)
                .loadUserFollowingList(userName)
                .compose(RxUtils.defaultSchedulers());
    }


    /**
     * load String data ,support data from local and  online
     */
    public Observable<String> loadString(String url) {
        if (url.startsWith(Constants.LOCAL_FILE_BASE_END_POINT)) {
            try {
                String s = FileUtil.getString(mContext, url);
                return Observable.just(s);
            } catch (IOException e) {
                e.printStackTrace();
                throw Exceptions.propagate(e);
            }
        } else {
            String path = url.substring(Constants.Remote_BASE_END_POINT.length());
            return mHttpHelper.getApi(RemoteApi.class)
                    .loadString(path)
                    .flatMap(new Func1<ResponseBody, Observable<String>>() {
                        @Override
                        public Observable<String> call(ResponseBody responseBody) {
                            try {
                                String result = responseBody.string();
                                return Observable.just(result);
                            } catch (IOException e) {
                                e.printStackTrace();
                                throw new RuntimeException("IOException when convert Response Body to String");
                            }
                        }
                    });
        }
    }


    /**
     * post string to server
     */
    public Observable<String> postString(String url, Map<String, String> paramMap) {
        return mHttpHelper.getApi(RemoteApi.class)
                .postString(url, paramMap)
                .flatMap(new Func1<ResponseBody, Observable<String>>() {
                    @Override
                    public Observable<String> call(ResponseBody responseBody) {
                        try {
                            String result = responseBody.string();
                            return Observable.just(result);
                        } catch (IOException e) {
                            e.printStackTrace();
                            throw new RuntimeException("IOException when convert Response Body to String");
                        }
                    }
                });
    }

    /**
     * 加载第一个tab 的url数据，返回新闻列表数据
     *
     * @param url 需要加载的url
     * @return NormalJsonInfo<NewsItem>
     */

    public Observable<NormalJsonInfo<NewsItem>> loadNewsJsonInfo(final String url) {
        final Gson gson = new GsonBuilder().create();
        return loadString(url)
                .doOnNext(new Action1<String>() {
                    @Override
                    public void call(String result) {
                        SpUtil.putString(mContext, url, result);
                        Timber.i("currently data string news ---> " + SpUtil.getString(mContext, url));
                    }
                })
                .flatMap(new Func1<String, Observable<HttpResult<NormalJsonInfo<NewsItem>>>>() {
                    @Override
                    public Observable<HttpResult<NormalJsonInfo<NewsItem>>> call(String s) {
                        HttpResult<NormalJsonInfo<NewsItem>> obj = gson.fromJson(s,
                                new TypeToken<HttpResult<NormalJsonInfo<NewsItem>>>() {
                                }.getType());
                        return Observable.just(obj);
                    }
                }).map(new HttpResultFunc<>())
                .compose(RxUtils.defaultSchedulers());
    }

    public Observable<List<WXItemBean>> getWechatData(int num, int page) {
        return mHttpHelper.getApi(WechatApi.class)
                .getWXHot(Constants.WEIXIN_KEY, num, page)
//                .map(new HttpResultFunc<>())  和下方语句一样效果
                .compose(RxUtils.handleResult())
                .compose(RxUtils.defaultSchedulers());
    }

    /**
     * 通过GET方式下载远程文件，支持大文件下载
     * 想要获知下载进度事件，请订阅{@link DownloadEvent}
     * 想要获知下载完成事件，请订阅{@link DownloadFinishEvent}
     */
    public void downloadFile(Context ctx, final String url) {
        Intent startIntent = new Intent(ctx, DownloadService.class);
        startIntent.putExtra(DownloadService.DOWNLOAD_URL, url);
        ctx.startService(startIntent);
    }

    /**
     * 通过POST方式上传文件，支持多文件上传
     * 想要获知上传进度事件，请订阅{@link com.anthony.app.common.data.event.UploadEvent}
     * 想要获知上传完成事件，请订阅{@link com.anthony.app.common.data.event.UploadFinishEvent}
     */
    public void uploadFile(Context ctx, String url, ArrayList<UploadParam> fileList, ArrayList<UploadParam> paramList) {
        Intent startIntent = new Intent(ctx, UploadService.class);
        startIntent.putExtra(UploadService.UPLOAD_URL, url);
        startIntent.putParcelableArrayListExtra(UploadService.UPLOAD_FILES, fileList);
        if (paramList != null) {
            startIntent.putParcelableArrayListExtra(UploadService.UPLOAD_PARAMS, paramList);
        }
        ctx.startService(startIntent);
    }

    public void uploadFile(Context ctx, String url, ArrayList<UploadParam> fileList) {
        uploadFile(ctx, url, fileList, null);
    }


}