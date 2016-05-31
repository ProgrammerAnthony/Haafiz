package edu.com.app.umeng.helper;

import android.os.Environment;

import com.alibaba.tcms.env.YWEnvType;

import java.util.HashMap;
import java.util.Map;

public class Constant {
	public static final boolean AUTO_LOGIN = false;
	public static final boolean TEST_NULL_PARAM = true;
	public static final String LOCAL_STORE_NAME = "OPENIM_INFO";
	
	public final static String TRIBE_ID = "tribe_id";
	
	public static final String ENVTYPE = "envTpye";
	public static final String APPKEYS = "appkeys";
	public static final String SOURCE_APPKEY = "sourceAppkey";
	public static final String SOURCE_ID = "sourceId";
	public static final String SOURCE_PWD = "sourcePwd";
	public static final String TARGET_APPKEY = "targetAppkey";
	public static final String TARGET_ID = "targetId";
	
	public static final String[] ITEMS = {"online","pre", "test", "sandbox"};
	public static final Map<String, YWEnvType> map = new HashMap<String, YWEnvType>();
	
	public final static String APPKEY = "23015524"; // daily：2333，test：3344
	public final static String APPKEY2 = "23015524";//消息target
	public final static String DEFAULT_USER = "testpro1"; 	//登录id
	public final static String DEFAULT_TARGET = "testpro2";	//会话id
	
	public static final String ROOT_PATH = Environment
			.getExternalStorageDirectory().getAbsolutePath();
	public final static String STORE_PATH = Environment
			.getExternalStorageDirectory().getAbsolutePath()
			+ "/openim/images/";

	public static final String[] IMAGE_URLS = {
			"http://pic.ffpic.com/files/tupian/tupian413.jpg",
			"http://pic.ffpic.com/files/2014/1230/1226ffpiccinfrfd5813.jpg",
			"http://pic.ffpic.com/files/2014/1230/1226ffpiccinfrfd4279.jpg",
			"http://pic.ffpic.com/files/2014/1230/1226ffpiccinfrfd4636.jpg",
			"http://pic.ffpic.com/files/2014/1230/1226ffpiccinfrfd5479.jpg" };
	public static final String[] AUDIO_URLS = { "http://2295.com/lsxpf1",
			"http://2295.com/hdxj5d", "http://2295.com/vbfsqa",
			"http://2295.com/y3gv65", "http://2295.com/prsm3p" };
	public static final String[] GEO_INFO= {"36.0401270000,103.8357460000,lanZhou","34.2533190000,108.9789410000,xiAn"};
	public static final long TIMEOUT = 120; // 2分钟
}
