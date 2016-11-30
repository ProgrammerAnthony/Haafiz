package com.anthony.app.common.data.database.dao;


import com.anthony.app.common.MyApplication;
import com.anthony.app.common.data.bean.Channel;
import com.j256.ormlite.stmt.QueryBuilder;

import java.sql.SQLException;
import java.util.List;


public class ChannelDao extends BaseDao<Channel> {


    public ChannelDao(MyApplication mApplication) {
        super(mApplication);
    }

    public List<Channel> getChannelByTitle(String title) {
        try {
            QueryBuilder builder = daoOpe.queryBuilder();
            builder.where().eq("title", title);
            return builder.query();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<Channel> getChannelByParentId(int id){
        try {
            QueryBuilder builder = daoOpe.queryBuilder();
            builder.where().eq("parentChannelId", id);
            return builder.query();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Channel getChannelById(int id) {
        try {
            return daoOpe.queryForId(id);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<Channel> queryVisibleChannel() {
        try {
            QueryBuilder builder = daoOpe.queryBuilder();
            builder.distinct().where().eq("isSubscribe", 1).or().eq("isFix", 1);
            return builder.query();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }


}
