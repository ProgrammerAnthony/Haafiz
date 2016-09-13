package com.anthony.app.common.data.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;


public class NormalJsonInfo implements Serializable {

    public PageInfo page_info;
    @SerializedName(value = "topic_datas")
    public List<NewsItem> topic_datas;
    @SerializedName(value = "datas")
    public List<NewsItem> datas;

    public class PageInfo {
        public String page_count;
        public String page_index;
    }
}
