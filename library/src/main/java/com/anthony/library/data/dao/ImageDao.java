package com.anthony.library.data.dao;


import com.anthony.library.data.bean.Image;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;

public class ImageDao extends BaseDao<Image> {

    public ImageDao(OrmLiteSqliteOpenHelper sqliteOpenHelper) {
        super(sqliteOpenHelper);
    }
}
