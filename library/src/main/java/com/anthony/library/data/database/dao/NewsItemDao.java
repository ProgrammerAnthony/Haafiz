package com.anthony.library.data.database.dao;


import com.anthony.library.MyApplication;
import com.anthony.library.data.bean.NewsItem;
import com.j256.ormlite.stmt.QueryBuilder;

import java.sql.SQLException;
import java.util.List;


/**
 * added to
 */
public class NewsItemDao extends BaseDao<NewsItem> {

    public NewsItemDao(MyApplication mApplication) {
        super(mApplication);
    }

    public List<NewsItem> queryLatest(int channelId, long limit) {
        try {
            QueryBuilder builder = daoOpe.queryBuilder();
            builder.limit(limit)
                    .where()
                    .eq("channel_id", channelId)
                    .and()
                    .eq("isTopic", false);
            return builder.query();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<NewsItem> queryTopic(int channelId) {
        try {
            QueryBuilder builder = daoOpe.queryBuilder();
            builder.where()
                    .eq("channel_id", channelId)
                    .and()
                    .eq("isTopic", true);
            return builder.query();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

}
