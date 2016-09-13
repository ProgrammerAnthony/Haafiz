package com.anthony.app.common.data.event;


import com.anthony.app.common.data.download.DownloadTask;

public class DownloadEvent {
    public String url;
    public long total;
    public long progress;
    public DownloadTask task;

    public DownloadEvent(String url, long total, long progress, DownloadTask task) {
        this.url = url;
        this.total = total;
        this.progress = progress;
        this.task = task;
    }
}
