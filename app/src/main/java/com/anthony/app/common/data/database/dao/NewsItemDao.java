package com.anthony.app.common.data.database.dao;


import com.anthony.app.common.base.MyApplication;
import com.anthony.app.common.data.bean.NewsItem;

public class NewsItemDao extends BaseDao<NewsItem> {

    public NewsItemDao(MyApplication mApplication) {
        super(mApplication);
    }

}
