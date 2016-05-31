package edu.com.base.model.config;

/**
 * Created by Anthony on 2016/4/28.
 * Class Note:
 */
public class Constants {
    public static final String HTTP_PREFIX = "http://";
    public static final String HTTPS_PREFIX = "https://";

    /** Activity和Fragment类型文件 */
    public static final String BASE_TYPE_FRAGMENT_MAP_PATH = "raw://type_fragment_map";

    public static final String BASE_TYPE_ACTIVITY_MAP_PATH = "raw://type_activity_map";

    /** END */

    /** Broadcast constants */
    public static final String SET_FONT_ACTION = "com.trs.activity.TRSAbsBaseFragmentActivity.SetFontAction";
    public static final String SET_FONT_ACTION_FONT_NAME = "com.trs.activity.TRSAbsBaseFragmentActivity.FontName";
    public static final String SET_DAY_NIGHT_MODE_ACTION = "com.trs.activity.TRSAbsBaseFragmentActivity.SetDayNightModeAction";
    public static final String SET_DAY_NIGHT_MODE_ACTION_THEME_NAME = "com.trs.activity.TRSAbsBaseFragmentActivity.ThemeName";
    public static final String SP_NAME ="SP_NAME";

    /** JSON数据中Key数组 */
    public static final String[] ID_NAMES = {"id"};
    public static final String[] TYPE_NAMES = {"type", "t"};
    public static final String[] IMAGE_URL_NAMES = {"picture", "pic", "image",
            "img", "icon", "ic"};
    public static final String[] URL_NAMES = {"url", "link"};
    public static final String[] TITLE_NAMES = {"title"};
    public static final String[] SUBTITLE_NAMES = {"summary", "sub", "subtitle"};
    public static final String[] EXTRA_NAMES = {"extra", "extras", "ex"};
    public static final String[] CHANNEL_NAMES = {"channels", "channel", "chnls"};
    public static final String[] PAGE_INFO_NAMES = {"page_info"};
    public static final String[] PAGE_COUNT_NAMES = {"page_count"};
    public static final String[] TOP_DATAS_NAMES = {"topic_datas", "topic_data"};
    public static final String[] DATAS_NAMES = {"datas", "data"};
    public static final String[] TIME_NAMES = {"date", "time"};


    /** 朋友圈数据 */
    public static final String[] FRIENDS_NAME = {"friends"};
    public static final String[] FRIEND_ID = {"id"};
    public static final String[] FRIEND_IMG = {"img"};
    public static final String[] FRIEND_NAME = {"name"};
    public static final String[] FRIEND_NICKNAME = {"nickName"};
    public static final String[] FRIEND_SORT_LETTER = {"sortLetter"};

    public static final String APP_KEY = "56de821ce0f55ae9900030d4";
    public static final String UMENG_APPKEY = "5424dc93fd98c58ec20289da";

}
