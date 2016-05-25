package edu.com.mvplibrary.util;

import android.content.Context;
import android.content.SharedPreferences;
/**
 * Created by Anthony on 2016/5/23.
 * Class Note:
 * SharedPreference Manager
 */
public class PreferenceManager {
    private static final String SETTING = "setting";
    private static final String ID = "ID";
    private static final String FIRST_TIME = "firsttime";
    private static final String ISLOGIN = "islogin";
    private static final boolean FIRST_TIME_DEFAULT = true;


    private static SharedPreferences mSharedPreferences;

    public static PreferenceManager getInstance(){
        if(mInstance==null){
            throw new IllegalArgumentException("Initialize PreferenceManager First");
        }
        return mInstance;
    }

    public PreferenceManager(Context context) {
        mSharedPreferences = context.getSharedPreferences(SETTING, Context.MODE_PRIVATE);
    }
    public static void init(Context context){
        if (mInstance == null) {
            synchronized (PreferenceManager.class) {
                if (mInstance == null) {
                    mInstance = new PreferenceManager(context);
                }
            }
        }
    }
    private static volatile PreferenceManager mInstance = null;



    public void saveCurrentUserId(String id) {
        mSharedPreferences.edit().putString(ID, id).apply();
    }

    public String getCurrentUserId() {
        return mSharedPreferences.getString(ID, null);

    }

    public boolean isLogined() {
        return mSharedPreferences.getBoolean(ISLOGIN, false);
    }

    public void setIslogin(boolean islogin) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putBoolean(ISLOGIN, islogin);
        editor.apply();

    }

    public boolean isFirstTime() {
        return mSharedPreferences.getBoolean(FIRST_TIME, FIRST_TIME_DEFAULT);
    }

    public void saveFirsttime(boolean isFirst) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putBoolean(FIRST_TIME, isFirst);
        editor.apply();

    }

    public void Clear() {
        mSharedPreferences.edit().clear().apply();
    }

}
