package com.anthony.library.data.dao;


import com.anthony.library.data.bean.OfflineResource;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.stmt.QueryBuilder;

import java.sql.SQLException;
import java.util.List;


public class OfflineResourceDao extends BaseDao<OfflineResource> {

    public OfflineResourceDao(OrmLiteSqliteOpenHelper sqliteOpenHelper) {
        super(sqliteOpenHelper);
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
