package edu.com.base;

import android.app.Application;
import android.content.Context;
import android.os.Environment;
import android.support.multidex.MultiDex;
import android.util.Log;


import com.avos.avoscloud.AVOSCloud;


import java.io.File;




import edu.com.base.ui.widget.ViewDisplay;
import edu.com.base.util.LocalFileUncaughtExceptionHandler;
import edu.com.base.util.PreferenceManager;
import edu.com.base.util.ToastUtils;


/**
 * Create By Anthony on 2016/1/15
 * 当前类注释：Application的父类，本项目中的Application将继承本类。
 * 当前功能：单例模式，异常捕获，由子类实现的获取url
 */
public  class AbsApplication extends Application {
//    private ApplicationComponent mAppComponent;
//    private static AbsApplication sInstance;

    //    private Menu mFirstLevelMenu;  //一级菜单，全局保存

    //支持MultiDex，解决65536问题


//    public static AbsApplication app() {
//
//        return sInstance;
//    }


    @Override
    public void onCreate() {
        super.onCreate();
//        sInstance = this;

//        initComponent();

        ViewDisplay.init(this);
        ToastUtils.init(this);
        PreferenceManager.init(this);
//        LogUtil.init();
//        LeakCanary.install(this);  //使用LeakCanary

        initLeanCloud();

//        initEaseUI();
        //异常捕获
        Thread.setDefaultUncaughtExceptionHandler(new LocalFileUncaughtExceptionHandler(this,
                Thread.getDefaultUncaughtExceptionHandler()));
    }

    private void initComponent() {
//        mAppComponent = DaggerApplicationComponent.builder().
//                applicationModule(new ApplicationModule(this)).build();
    }

//    public ApplicationComponent getAppComponent(){
//        return mAppComponent;
//    }

    //初始化LeanCloud
    private void initLeanCloud() {
        // 初始化参数依次为 this, AppId, AppKey
        AVOSCloud.initialize(this, "k9XVHXc7UHdiv6UOielOPyYc-gzGzoHsz", "5WHJ0HesNrOnveAYcoM5sLL2");
    }

    //初始化EaseUI（环信支持库）
    private void initEaseUI() {
//        EaseUIHelper.getInstance(this).init();
    }

//    @Override
//    public File getCacheDir() {
//        Log.i("getCacheDir", "cache sdcard state: " + Environment.getExternalStorageState());
//        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
//            File cacheDir = getExternalCacheDir();
//            if (cacheDir != null && (cacheDir.exists() || cacheDir.mkdirs())) {
//                Log.i("getCacheDir", "cache dir: " + cacheDir.getAbsolutePath());
//                return cacheDir;
//            }
//        }
//
//        File cacheDir = super.getCacheDir();
//        Log.i("getCacheDir", "cache dir: " + cacheDir.getAbsolutePath());
//
//        return cacheDir;
//    }


//    public Menu getFirstLevelMenu() {
//        return mFirstLevelMenu;
//    }
//
//    public void setFirstLevelMenu(Menu mFirstLevelMenu) {
//        this.mFirstLevelMenu = mFirstLevelMenu;
//    }

}