package com.anthony.library.data.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Anthony on 2016/9/14.
 * Class Note:
 */
public class NormalJsonInfo<T> implements Serializable {
    @SerializedName(value = "page_info")
    public PageInfo page_info;
    @SerializedName(value = "topic_datas")
    public List<T> topic_datas;
    @SerializedName(value = "datas")
    public List<T> datas;

    public class PageInfo {
        public String page_count;
        public String page_index;
    }

/*    {
        "code": 0,
            "message": "请求数据成功",
            "response": {
        "page_info": {
            "page_index": 0,
            "page_count": 1
        },
        "topic_datas": [],
        "datas": []
    }
    }*/
}
