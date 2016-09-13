package com.anthony.app.common.data.database.dao;


import com.anthony.app.common.base.MyApplication;
import com.anthony.app.common.data.bean.Image;

public class ImageDao extends BaseDao<Image> {

    public ImageDao(MyApplication mApplication) {
        super(mApplication);
    }
}
