package com.anthony.app.common.data;

import android.content.Context;
import android.content.Intent;

import com.anthony.app.common.base.Constants;
import com.anthony.app.common.data.bean.Channel;
import com.anthony.app.common.data.bean.Menu;
import com.anthony.app.common.data.bean.NewsItem;
import com.anthony.app.common.data.bean.NormalJsonInfo;
import com.anthony.app.common.data.download.DownloadService;
import com.anthony.app.common.data.retrofit.HttpResult;
import com.anthony.app.common.data.retrofit.HttpResultFunc;
import com.anthony.app.common.data.retrofit.ItemJsonDeserializer;
import com.anthony.app.common.data.retrofit.RemoteApi;
import com.anthony.app.common.data.upload.UploadParam;
import com.anthony.app.common.data.upload.UploadService;
import com.anthony.app.common.injection.scope.ApplicationContext;
import com.anthony.app.common.utils.FileUtil;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import okhttp3.ResponseBody;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.exceptions.Exceptions;
import rx.functions.Action0;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by Anthony on 2016/6/12.
 * Class Note:
 * data entrance of all kinds of data
 */


public class DataManager {

    private final Gson gson;
    @Inject
    HttpHelper httpHelper;

    @Inject
    PreferencesHelper mPreferencesHelper;

    @Inject
    EventPosterHelper mEventPoster; //otto

    @Inject
    RxBus mRxBus;

    private Context mContext;

    @Inject
    public DataManager(@ApplicationContext Context context) {
        this.mContext = context;
        gson = new GsonBuilder()
                .registerTypeAdapter(NewsItem.class, new ItemJsonDeserializer<NewsItem>())
                .create();
    }

    public PreferencesHelper getPreferencesHelper() {
        return mPreferencesHelper;
    }

    public RxBus getRxBus() {
        return mRxBus;
    }

    private Action0 postEventAction(final Object event) {
        return new Action0() {
            @Override
            public void call() {
                mEventPoster.postEventSafely(event);
            }
        };
    }

    /**
     * load String local or online
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
            return httpHelper.getService(RemoteApi.class)
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
        return httpHelper.getService(RemoteApi.class)
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
     * 请求本地或远程一级栏目数据函数
     * 注意:Gson数据反序列化为TRSMenu类型
     */
    public Observable<Menu> loadMenu(String url) {
        return loadString(url).flatMap(new Func1<String, Observable<Menu>>() {
            @Override
            public Observable<Menu> call(String s) {
                return Observable.just(gson.fromJson(s, Menu.class));
            }
        });
    }

    /**
     * 请求本地或远程频道数据函数
     * 注意:Gson数据反序列化为HttpResult<List<TRSChannel>>类型
     */
    public Observable<HttpResult<List<Channel>>> loadChannel(String url) {
        return loadString(url).flatMap(new Func1<String, Observable<HttpResult<List<Channel>>>>() {
            @Override
            public Observable<HttpResult<List<Channel>>> call(String s) {
                HttpResult<List<Channel>> obj = gson.fromJson(s,
                        new TypeToken<HttpResult<List<Channel>>>() {
                        }.getType());
                return Observable.just(obj);
            }
        });
    }

    /**
     * 通过GET方式下载远程文件，支持大文件下载
     * 想要获知下载进度事件，请订阅com.trs.library.rx.bus.event.DownloadEvent
     * 想要获知下载完成事件，请订阅com.trs.library.rx.bus.event.DownloadFinishEvent
     */
    public void downloadFile(Context ctx, final String url) {
        Intent startIntent = new Intent(ctx, DownloadService.class);
        startIntent.putExtra(DownloadService.DOWNLOAD_URL, url);
        ctx.startService(startIntent);
    }

    /**
     * 通过POST方式上传文件，支持多文件上传
     * 想要获知上传进度事件，请订阅com.trs.library.rx.bus.event.UploadEvent
     * 想要获知上传完成事件，请订阅com.trs.library.rx.bus.event.UploadFinishEvent
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





    /**
     * load news data in { com.app.gzgov.module.tab1.GZTab1Fragment}
     *
     * @param url
     * @return
     */
    public Observable<NormalJsonInfo> loadNormalNewsData(String url) {
        return loadString(url)
                .flatMap(new Func1<String, Observable<HttpResult<NormalJsonInfo>>>() {
                    @Override
                    public Observable<HttpResult<NormalJsonInfo>> call(String s) {
                        HttpResult<NormalJsonInfo> obj = gson.fromJson(s,
                                new TypeToken<HttpResult<NormalJsonInfo>>() {
                                }.getType());
                        return Observable.just(obj);
                    }
                }).map(new HttpResultFunc<NormalJsonInfo>())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }


}