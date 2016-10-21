package com.anthony.app.common.data.download;


public class DownloadFinishEvent {
    public boolean isSuccess;
    public DownloadTask task;

    public DownloadFinishEvent(DownloadTask task, boolean isSuccess) {
        this.task = task;
        this.isSuccess = isSuccess;
    }
}
