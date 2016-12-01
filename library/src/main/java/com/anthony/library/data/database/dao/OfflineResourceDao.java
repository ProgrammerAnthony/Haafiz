package com.anthony.library.data.database.dao;


import com.anthony.library.MyApplication;
import com.anthony.library.data.bean.OfflineResource;
import com.j256.ormlite.stmt.QueryBuilder;

import java.sql.SQLException;
import java.util.List;


public class OfflineResourceDao extends BaseDao<OfflineResource> {

    public OfflineResourceDao(MyApplication mApplication) {
        super(mApplication);
    }

    public OfflineResource queryResourceByUrl(String url) {
        try {
            QueryBuilder builder = daoOpe.queryBuilder();
            List<OfflineResource> res_list= builder.where().eq("res_url", url).query();
            if (res_list != null && res_list.size() > 0) {
                return res_list.get(0);
            } else {
                return null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}
