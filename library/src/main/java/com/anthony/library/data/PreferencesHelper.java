package com.anthony.library.data;

import android.content.Context;
import android.content.SharedPreferences;

import com.anthony.library.BaseDataRepository;


/**
 * Created by Anthony on 2016/5/23.
 * Class Note:
 * SharedPreference Helper class,used only by{@link BaseDataRepository} is recommended
 *
 * Preference 的帮助类，推荐只在{@link BaseDataRepository}中使用
 */
public class PreferencesHelper {
    private static final String SETTING = "setting";
    private static final String ID = "USER_ID";
    private static final String FIRST_TIME = "firstTime";
    private static final String ISLOGIN = "isLogin";
    private static final boolean FIRST_TIME_DEFAULT = true;
    private static final String PREF_KEY_SIGNED_IN_RIBOT = "PREF_KEY_SIGNED_IN_RIBOT";
    private final SharedPreferences mPref;


    public PreferencesHelper(Context context) {
        mPref = context.getSharedPreferences(SETTING, Context.MODE_PRIVATE);
    }


    public void saveCurrentUserId(String id) {
        mPref.edit().putString(ID, id).apply();
    }

    public String getCurrentUserId() {
        return mPref.getString(ID, null);

    }

    public boolean isLogined() {
        return mPref.getBoolean(ISLOGIN, false);
    }

    public void setIslogin(boolean islogin) {
        SharedPreferences.Editor editor = mPref.edit();
        editor.putBoolean(ISLOGIN, islogin);
        editor.apply();

    }

    public boolean isFirstTime() {
        return mPref.getBoolean(FIRST_TIME, FIRST_TIME_DEFAULT);
    }

    public void saveFirstTime(boolean isFirst) {
        SharedPreferences.Editor editor = mPref.edit();
        editor.putBoolean(FIRST_TIME, isFirst);
        editor.apply();
    }

    public void Clear() {
        mPref.edit().clear().apply();
    }


//    public void putSignedInRibot(Friends friends) {
//        mPref.edit().putString(PREF_KEY_SIGNED_IN_RIBOT, mGson.toJson(friends)).apply();
//    }

//    @Nullable
//    public Friends getSignedInRibot() {
//        String ribotJson = mPref.getString(PREF_KEY_SIGNED_IN_RIBOT, null);
//        if (ribotJson == null) return null;
//        return mGson.fromJson(ribotJson, Friends.class);
//    }
}
