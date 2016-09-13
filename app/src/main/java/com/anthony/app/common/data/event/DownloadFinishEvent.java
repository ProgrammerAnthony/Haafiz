package com.anthony.app.common.data.event;


import com.anthony.app.common.data.download.DownloadTask;

public class DownloadFinishEvent {
    public boolean isSuccess;
    public DownloadTask task;

    public DownloadFinishEvent(DownloadTask task, boolean isSuccess) {
        this.task = task;
        this.isSuccess = isSuccess;
    }
}
