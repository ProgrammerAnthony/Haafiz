package com.anthony.library.data.database.dao;


import com.anthony.library.MyApplication;
import com.anthony.library.data.bean.Image;

public class ImageDao extends BaseDao<Image> {

    public ImageDao(MyApplication mApplication) {
        super(mApplication);
    }
}
