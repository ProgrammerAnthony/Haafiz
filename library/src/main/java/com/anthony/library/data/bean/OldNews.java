package com.anthony.library.data.bean;

import java.util.List;


public class OldNews<E> {
    public PageInfo page_info;
    public List<NewsItem> topic_datas;
    public List<NewsItem> datas;

    public class PageInfo {
        public String page_count;
        public String page_index;
    }
}
