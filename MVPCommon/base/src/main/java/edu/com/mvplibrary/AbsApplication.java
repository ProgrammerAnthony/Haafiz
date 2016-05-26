package edu.com.mvplibrary;

import android.app.Application;
import android.os.Environment;
import android.util.Log;


import com.avos.avoscloud.AVOSCloud;

import java.io.File;

import edu.com.mvplibrary.ui.widget.ViewDisplay;
import edu.com.mvplibrary.util.LocalFileUncaughtExceptionHandler;
import edu.com.mvplibrary.util.LogUtil;
import edu.com.mvplibrary.util.PreferenceManager;
import edu.com.mvplibrary.util.ToastUtils;


/**
 * Create By Anthony on 2016/1/15
 * 当前类注释：Application的父类，本项目中的Application将继承本类。
 * 当前功能：单例模式，异常捕获，由子类实现的获取url
 */
public abstract class AbsApplication extends Application {
    private static AbsApplication sInstance;
//    private Menu mFirstLevelMenu;

    public static AbsApplication app() {

        return sInstance;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
        ViewDisplay.init(this);
        ToastUtils.init(this);
        PreferenceManager.init(this);
        LogUtil.init();

        initLeanCloud();

        Thread.setDefaultUncaughtExceptionHandler(new LocalFileUncaughtExceptionHandler(this,
                Thread.getDefaultUncaughtExceptionHandler()));
    }

    private void initLeanCloud() {

        // 初始化参数依次为 this, AppId, AppKey
        AVOSCloud.initialize(this,"k9XVHXc7UHdiv6UOielOPyYc-gzGzoHsz","5WHJ0HesNrOnveAYcoM5sLL2");

    }


    @Override
    public File getCacheDir() {
        Log.i("getCacheDir", "cache sdcard state: " + Environment.getExternalStorageState());
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            File cacheDir = getExternalCacheDir();
            if (cacheDir != null && (cacheDir.exists() || cacheDir.mkdirs())) {
                Log.i("getCacheDir", "cache dir: " + cacheDir.getAbsolutePath());
                return cacheDir;
            }
        }

        File cacheDir = super.getCacheDir();
        Log.i("getCacheDir", "cache dir: " + cacheDir.getAbsolutePath());

        return cacheDir;
    }


//    public Menu getFirstLevelMenu() {
//        return mFirstLevelMenu;
//    }
//
//    public void setFirstLevelMenu(Menu mFirstLevelMenu) {
//        this.mFirstLevelMenu = mFirstLevelMenu;
//    }

}