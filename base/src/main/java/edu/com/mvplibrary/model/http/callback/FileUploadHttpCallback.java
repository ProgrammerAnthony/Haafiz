package edu.com.mvplibrary.model.http.callback;

/**
 * Created by Anthony on 2016/3/2.
 * Class Note:
 * file upload http callback for file
 */
public abstract class FileUploadHttpCallback extends BaseHttpCallback<String> {
    public abstract void onProgress(long byteCount, long contentLength, boolean done);
}
