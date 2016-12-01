package com.anthony.library.data.event;


import com.anthony.library.data.upload.UploadTask;

/**
 * Created by Anthony on 2016/7/8.
 * Class Note:
 *  upload event
 */
public class UploadEvent {
    public long total;
    public long progress;
    public UploadTask task;

    public UploadEvent(long total, long progress, UploadTask task) {
        this.total = total;
        this.progress = progress;
        this.task = task;
    }
}
