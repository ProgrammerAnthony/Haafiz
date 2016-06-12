package edu.com.base.model.http;

import android.content.Context;

import edu.com.base.model.http.callback.FileDownloadHttpCallback;
import edu.com.base.model.http.callback.FileUploadHttpCallback;
import edu.com.base.model.http.callback.StringHttpCallback;
import edu.com.base.model.http.provider.BaseHttpProvider;
import edu.com.base.model.http.provider.okhttp.OkHttpProvider;
import edu.com.base.model.http.request.HttpRequest;

/**
 * Created by Anthony on 2016/5/6.
 * Class Note:
 * todo use retrofit please
 * @deprecated
 */
public class HttpUtil {
    private static HttpUtil mInstance;
    private BaseHttpProvider mProvider;
    public static final int GET = 0;
    public static final int POST = 1;

    private HttpUtil(Context context) {
        mProvider = new OkHttpProvider(context);
    }

    public static HttpUtil getInstance(Context context) {
        if (mInstance == null) {
            synchronized (HttpUtil.class) {
                if (mInstance == null) {
                    mInstance = new HttpUtil(context);
                }
            }
        }
        return mInstance;
    }

    /**
     * load String data
     */
    public void loadString(HttpRequest request, StringHttpCallback callback) {
        mProvider.loadString(request, callback);
    }

    /**
     *post file to server
     */
    public void uploadFile(HttpRequest request, FileUploadHttpCallback callback) {
        mProvider.uploadFile(request, callback);
    }
    /**
     * download file from server
     */
    public void downloadFile(HttpRequest request,FileDownloadHttpCallback callback){
        mProvider.downloadFile(request,callback);
    }

/*
example:

 HttpRequest.Builder builder = new HttpRequest.Builder();
    HttpRequest request = builder.url("www.....").build();
    HttpUtil.getInstance().loadString(request, new StringHttpCallback() {
        @Override
        public void onResponse(String response) {
             ....
        }

        @Override
        public void onError(String error) {
            ...
        }
    });
    */
}
