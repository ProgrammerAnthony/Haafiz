package com.anthony.app.common.base;

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


    public static final String CURRENT_FONT_PATH = "current_font_path";
    public static final String CURRENT_FONT_SIZE = "current_font_size";
    public static final int CURRENT_FONT_SIZE_SMALL = 1;
    public static final int CURRENT_FONT_SIZE_MEDIUM = 2;
    public static final int CURRENT_FONT_SIZE_LARGE = 3;
    public static final String ONLY_WIFI_LOAD_IMG = "OnlyWifiLoadImg";



    public static final String TRS_FONT_MAP_PATH = "trs_font_map";

    //百度api地址
    public static final String REMOTE_BASE_END_POINT_WEATHER = "http://apis.baidu.com/";

    public static String DOWNLOAD_STORE_FOLDER = Environment.getExternalStorageDirectory().getAbsolutePath()
            + "/Download/";
}
