package com.anthony.app.common.data.event;


import com.anthony.app.common.data.bean.NewsItem;


public class VideoListClickEvent {
    public int position;
    public NewsItem item;

    public VideoListClickEvent(int position, NewsItem item) {
        this.position = position;
        this.item = item;
    }
}
