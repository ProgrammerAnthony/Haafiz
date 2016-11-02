package com.anthony.app.common.base;

import android.app.Application;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.support.multidex.MultiDex;
import android.util.Log;

import com.anthony.app.BuildConfig;
import com.anthony.app.common.data.DataManager;
import com.anthony.app.common.data.bean.Channel;
import com.anthony.app.common.data.bean.Menu;
import com.anthony.app.common.data.database.DatabaseHelper;
import com.anthony.app.common.injection.component.ApplicationComponent;
import com.anthony.app.common.injection.component.DaggerApplicationComponent;
import com.anthony.app.common.injection.module.ApplicationModule;
import com.anthony.app.common.utils.FakeCrashLibrary;
import com.avos.avoscloud.AVOSCloud;

import java.io.File;
import java.util.List;

import javax.inject.Inject;

import timber.log.Timber;

/**
 * Created by Anthony on 2016/6/3.
 * Class Note:
 * Base Application for Application
 * use in AndroidManifest.xml
 */
public class MyApplication extends Application {

    @Inject
    DataManager mDataManager;

    private ApplicationComponent mAppComponent;
    public DatabaseHelper dbHelper;
    //global instance ,channel list data
    public List<Channel> channels;
    private Menu mFirstLevelMenu;

    @Override
    public void onCreate() {

        super.onCreate();

//Dagger2 inject
        getAppComponent().inject(this);
//get DatabaseHelper instance
        dbHelper = mDataManager.getDatabaseHelper();
//        mEventBus.register(this);
        initAVOS();

//        initEaseUI(); // init EaseUI(for IM,Instant Messaging)

//        Thread.setDefaultUncaughtExceptionHandler(new LocalFileUncaughtExceptionHandler(this,
//                Thread.getDefaultUncaughtExceptionHandler()));   //exception handler

        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        } else {
            //sorry,Fabric currently not available ,   Fabric.with(this, new Crashlytics());   Timber.plant(new CrashlyticsTree());
            Timber.plant(new CrashReportingTree());
        }


//        initDb();
    }


    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);//multi dex support
    }
//    private void initDb() {
//        //初始化数据库
//        if (TextUtils.isEmpty(SpUtil.getString(this, Constants.CURRENT_USER, ""))) {
//            SpUtil.putString(this, Constants.CURRENT_USER, "fish_man_default");
//        }
//        String current_user = SpUtil.getString(this, Constants.CURRENT_USER, "");
//        dbHelper = new DatabaseHelper(this, current_user + ".db");
//    }


    //init LeanCloud (Online storage)
    private void initAVOS() {
        try {
            ApplicationInfo appInfo = getPackageManager()
                    .getApplicationInfo(getPackageName(), PackageManager.GET_META_DATA);
            String id = appInfo.metaData.getString("AVOS_APP_ID");
            String key = appInfo.metaData.getString("AVOS_APP_KEY");
            // init LeanCloud AppId and AppKey
            AVOSCloud.initialize(this, id, key);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    public Menu getFirstLevelMenu() {
        return mFirstLevelMenu;
    }

    public void setFirstLevelMenu(Menu mFirstLevelMenu) {
        this.mFirstLevelMenu = mFirstLevelMenu;
    }

    public ApplicationComponent getAppComponent() {
        if (mAppComponent == null) {
            mAppComponent = DaggerApplicationComponent.builder().
                    applicationModule(new ApplicationModule(this)).build();
        }
        return mAppComponent;
    }


    public static MyApplication get(Context context) {
        return (MyApplication) context.getApplicationContext();
    }

    public void setAppComponent(ApplicationComponent appComponent) {
        mAppComponent = appComponent;
    }


    /**
     * get cache dir
     *
     * @return cache directory
     */
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


    /**
     * A tree which logs important information for crash reporting.fake one
     */
    public class CrashReportingTree extends Timber.Tree {
        @Override
        protected void log(int priority, String tag, String message, Throwable t) {
            if (priority == Log.VERBOSE || priority == Log.DEBUG) {
                return;
            }

            FakeCrashLibrary.log(priority, tag, message);

            if (t != null) {
                if (priority == Log.ERROR) {
                    FakeCrashLibrary.logError(t);
                } else if (priority == Log.WARN) {
                    FakeCrashLibrary.logWarning(t);
                }
            }
        }
    }

    /**
     * A logging implementation which reports 'info', 'warning', and 'error' logs to Crashlytics.
     */
/*    public class CrashlyticsTree extends Timber.Tree {

        @Override
        protected void log(int priority, String tag, String message, Throwable t) {
            if (priority == Log.VERBOSE || priority == Log.DEBUG) {
                return;
            }

            Crashlytics.log(priority, tag, message);

            if (t != null) {
                if (priority == Log.ERROR) {
                    Crashlytics.logException(t);
                }
            }

        }
    }*/

}
