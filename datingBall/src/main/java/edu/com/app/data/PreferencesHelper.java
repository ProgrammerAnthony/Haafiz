package edu.com.app.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.inject.Inject;

import edu.com.app.data.bean.Friends;
import edu.com.app.injection.scope.ApplicationContext;

/**
 * Created by Anthony on 2016/5/23.
 * Class Note:
 * SharedPreference Helper class
 */
public class PreferencesHelper {
    private static final String SETTING = "setting";
    private static final String ID = "USER_ID";
    private static final String FIRST_TIME = "firstTime";
    private static final String ISLOGIN = "isLogin";
    private static final boolean FIRST_TIME_DEFAULT = true;
    private static final String PREF_KEY_SIGNED_IN_RIBOT = "PREF_KEY_SIGNED_IN_RIBOT";
    private final SharedPreferences mPref;

    private final Gson mGson;

    @Inject
    public PreferencesHelper(@ApplicationContext Context context) {
        mPref = context.getSharedPreferences(SETTING, Context.MODE_PRIVATE);
        mGson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSz")
                .create();
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

    public void saveFirsttime(boolean isFirst) {
        SharedPreferences.Editor editor = mPref.edit();
        editor.putBoolean(FIRST_TIME, isFirst);
        editor.apply();
    }

    public void Clear() {
        mPref.edit().clear().apply();
    }


    public void putSignedInRibot(Friends friends) {
        mPref.edit().putString(PREF_KEY_SIGNED_IN_RIBOT, mGson.toJson(friends)).apply();
    }

    @Nullable
    public Friends getSignedInRibot() {
        String ribotJson = mPref.getString(PREF_KEY_SIGNED_IN_RIBOT, null);
        if (ribotJson == null) return null;
        return mGson.fromJson(ribotJson, Friends.class);
    }
}
