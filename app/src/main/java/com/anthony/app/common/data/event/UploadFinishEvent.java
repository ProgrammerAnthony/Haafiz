package com.anthony.app.common.data.event;


import com.anthony.app.common.data.upload.UploadTask;

/**
 * Created by Anthony on 2016/7/8.
 * Class Note:
 *
 *
 */
public class UploadFinishEvent {
    public boolean isSuccess;
    public UploadTask task;

    public UploadFinishEvent(UploadTask task, boolean isSuccess) {
        this.task = task;
        this.isSuccess = isSuccess;
    }
}
