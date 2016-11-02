package com.anthony.app.common.data.database;

import com.anthony.app.common.base.MyApplication;
import com.anthony.app.common.data.bean.NewsItem;
import com.j256.ormlite.stmt.QueryBuilder;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by Anthony on 2016/11/1.
 * Class Note:
 * @deprecated  use {@link com.anthony.app.common.data.database.dao.NewsItemDao}
 */

public class NewsItemDaoOld {
    private MyApplication myApplication;


    public NewsItemDaoOld(MyApplication myApplication) {
        this.myApplication = myApplication;

    }

    public void add(NewsItem t) {
        try {
            myApplication.dbHelper.getNewsItemDao().create(t);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void delete(NewsItem t) {
        try {
            myApplication.dbHelper.getNewsItemDao().delete(t);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void update(NewsItem t) {
        try {
            myApplication.dbHelper.getNewsItemDao().update(t);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<NewsItem> all() {
        try {
            return myApplication.dbHelper.getNewsItemDao().queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<NewsItem> queryByColumn(String columnName, Object columnValue) {
        try {
            QueryBuilder builder =  myApplication.dbHelper.getNewsItemDao().queryBuilder();
            builder.where().eq(columnName, columnValue);
            return builder.query();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}