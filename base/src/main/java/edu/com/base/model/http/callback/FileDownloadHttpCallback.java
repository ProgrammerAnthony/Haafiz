package edu.com.base.model.http.callback;

import java.io.File;

/**
 * Created by Anthony on 2016/3/2.
 * Class Note:
 * file download http callback for file
 */
public abstract class FileDownloadHttpCallback extends BaseHttpCallback<File> {
    public abstract void onProgress(long byteCount, long contentLength, boolean done);
}
