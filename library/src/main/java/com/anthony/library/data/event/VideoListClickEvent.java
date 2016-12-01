package com.anthony.library.data.event;


import com.anthony.library.data.bean.NewsItem;

public class VideoListClickEvent {
    public int position;
    public NewsItem item;

    public VideoListClickEvent(int position, NewsItem item) {
        this.position = position;
        this.item = item;
    }
}
