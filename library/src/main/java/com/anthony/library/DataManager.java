package com.anthony.library;

import android.content.Context;
import android.content.Intent;

import com.anthony.library.data.EventPosterHelper;
import com.anthony.library.data.HttpHelper;
import com.anthony.library.data.PreferencesHelper;
import com.anthony.library.data.database.DatabaseHelper;
import com.anthony.library.data.download.DownloadEvent;
import com.anthony.library.data.download.DownloadFinishEvent;
import com.anthony.library.data.download.DownloadService;
import com.anthony.library.data.event.UploadEvent;
import com.anthony.library.data.event.UploadFinishEvent;
import com.anthony.library.data.net.RemoteApi;
import com.anthony.library.data.upload.UploadParam;
import com.anthony.library.data.upload.UploadService;
import com.anthony.library.utils.FileUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

import okhttp3.ResponseBody;
import rx.Observable;
import rx.exceptions.Exceptions;
import rx.functions.Action0;
import rx.functions.Func1;

/**
 * Created by Anthony on 2016/6/12.
 * Class Note:
 * data entrance of all kinds of data
 * using {@link HttpHelper},{@link PreferencesHelper},
 * {@link EventPosterHelper} ，
 * {@link DatabaseHelper}
 * and {@link RxBus} to access data
 * <p>
 * 所有数据的入口类
 */
public class DataManager {


    HttpHelper mHttpHelper;


    PreferencesHelper mPreferencesHelper;


    EventPosterHelper mEventPoster; //otto


    DatabaseHelper mDatabaseHelper;

    protected Context mContext;


    public DataManager(Context context) {
        this.mContext = context;
        mHttpHelper = new HttpHelper(mContext);
        mPreferencesHelper = new PreferencesHelper(mContext);
        mDatabaseHelper = new DatabaseHelper(mContext);
    }

    public DatabaseHelper getDatabaseHelper() {
        return mDatabaseHelper;
    }

    public PreferencesHelper getPreferencesHelper() {
        return mPreferencesHelper;
    }

    public HttpHelper getHttpHelper() {
        return mHttpHelper;
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
     * 想要获知上传进度事件，请订阅{@link UploadEvent}
     * 想要获知上传完成事件，请订阅{@link UploadFinishEvent}
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