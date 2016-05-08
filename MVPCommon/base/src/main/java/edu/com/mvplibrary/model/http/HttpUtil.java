package edu.com.mvplibrary.model.http;

import edu.com.mvplibrary.model.http.callback.FileDownloadHttpCallback;
import edu.com.mvplibrary.model.http.callback.FileUploadHttpCallback;
import edu.com.mvplibrary.model.http.callback.StringHttpCallback;
import edu.com.mvplibrary.model.http.provider.BaseHttpProvider;
import edu.com.mvplibrary.model.http.request.HttpRequest;

/**
 * Created by Anthony on 2016/5/6.
 * Class Note:
 */
public class HttpUtil {
    private static HttpUtil mInstance;
    private BaseHttpProvider mProvider;
    public static final int GET = 0;
    public static final int POST = 1;

    public static HttpUtil getInstance() {
        if (mInstance == null) {
            synchronized (HttpUtil.class) {
                if (mInstance == null) {
                    mInstance = new HttpUtil();
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

}
