package edu.com.base.model.http.provider;



import android.content.Context;

import java.io.IOException;

import edu.com.base.AbsApplication;
import edu.com.base.model.http.request.HttpRequest;
import edu.com.base.model.http.callback.FileDownloadHttpCallback;
import edu.com.base.model.http.callback.FileUploadHttpCallback;
import edu.com.base.model.http.callback.StringHttpCallback;
import edu.com.base.util.FileUtil;

/**
 * Created by Anthony on 2016/3/1.
 * Class Note:
 * provide base operation of http
 * include load String, upload file,download file and load local String
 * 提供http常用操作，包含加载字符串（本地或者网络），下载文件，上传文件
 */
public abstract class BaseHttpProvider {
    public abstract void loadString(HttpRequest request, StringHttpCallback callback);

    public abstract void uploadFile(HttpRequest request, FileUploadHttpCallback callback);

    public abstract void downloadFile(HttpRequest request, FileDownloadHttpCallback callback);

    protected void loadLocalString(Context context, String path, final StringHttpCallback callback) {

        try {
            String result = FileUtil.getString(context, path);
            callback.onResponse(result);
        } catch (IOException e) {
//            e.printStackTrace();
            callback.onError(e.getMessage());
        }


    }
}
