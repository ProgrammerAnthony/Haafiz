package com.anthony.app.common;

import android.os.Environment;

/**
 * Created by Anthony on 2016/4/28.
 * Class Note:
 * constants class in the project
 */
public class Constants {
    public static final String HTTP_PREFIX = "http://";
    public static final String HTTPS_PREFIX = "https://";

    /** Activity和Fragment类型文件 */
    public static final String BASE_TYPE_FRAGMENT_MAP_PATH = "raw://type_fragment_map";

    public static final String BASE_TYPE_ACTIVITY_MAP_PATH = "raw://type_activity_map";

    public static final String Remote_BASE_END_POINT = "http://www.baidu.com";//url for testing
    public static final String Remote_BASE_END_POINT_GITHUB = "https://api.github.com/";//url for testing
    public static final String LOCAL_FILE_BASE_END_POINT = "raw://";

    public static final String CURRENT_USER = "current_user";



    public static final int CURRENT_FONT_SIZE_SMALL = 1;
    public static final int CURRENT_FONT_SIZE_MEDIUM = 2;
    public static final int CURRENT_FONT_SIZE_LARGE = 3;




    public static final String TRS_FONT_MAP_PATH = "trs_font_map";

    //Baidu api
    public static final String REMOTE_BASE_END_POINT_WEATHER = "http://apis.baidu.com/";

    public static String DOWNLOAD_STORE_FOLDER = Environment.getExternalStorageDirectory().getAbsolutePath()
            + "/Download/";

    public static final String WEIXIN_KEY = "3d88f5fb6e5a1fbf2921dbb643f25894";
}
