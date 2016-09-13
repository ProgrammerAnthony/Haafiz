package com.anthony.app.common.data.event;


import com.anthony.app.common.data.upload.UploadTask;

/**
 * Created by Anthony on 2016/7/8.
 * Class Note:
 *
 */
public class UploadEventInternal {
    public long byteCount;
    public UploadTask task;

    public UploadEventInternal(long byteCount, UploadTask task) {
        this.byteCount = byteCount;
        this.task = task;
    }
}
