package com.anthony.library;

import android.app.Application;
import android.content.Context;
import android.os.Environment;
import android.os.Handler;
import android.support.multidex.MultiDex;
import android.util.Log;

import java.io.File;


/**
 * Created by Anthony on 2016/6/3.
 * Class Note:
 * Base Application for Application
 * use in AndroidManifest.xml
 */
public class BaseApplication extends Application {

//    DataManager mDataManager;
    private static Handler mHandler;
    private static Context mContext;



    @Override
    public void onCreate() {

        super.onCreate();

        mHandler = new Handler();

        mContext = getApplicationContext();

//        mDataManager = new DataManager(getApplicationContext());

//Dagger2 inject
//        getAppComponent().inject(this);
//get DatabaseHelper instance
//        dbHelper = mDataManager.getDatabaseHelper();
//        mEventBus.register(this);
//        initAVOS();
//        initEaseUI(); // init EaseUI(for IM,Instant Messaging)
//        Thread.setDefaultUncaughtExceptionHandler(new LocalFileUncaughtExceptionHandler(this,
//                Thread.getDefaultUncaughtExceptionHandler()));   //exception handler
//        if (BuildConfig.DEBUG) {
//            Timber.plant(new Timber.DebugTree());
//        } else {
//            //oops,Fabric currently not available ,   Fabric.with(this, new Crashlytics());   Timber.plant(new CrashlyticsTree());
//            Timber.plant(new CrashReportingTree());
//        }

    }


    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);//multi dex support
    }

    public static Context getContext() {
        return mContext;
    }

    public static Handler getHanlder() {
        return mHandler;
    }



    public static BaseApplication get(Context context) {
        return (BaseApplication) context.getApplicationContext();
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
//    public class CrashReportingTree extends Timber.Tree {
//        @Override
//        protected void log(int priority, String tag, String message, Throwable t) {
//            if (priority == Log.VERBOSE || priority == Log.DEBUG) {
//                return;
//            }
//
//            FakeCrashLibrary.log(priority, tag, message);
//
//            if (t != null) {
//                if (priority == Log.ERROR) {
//                    FakeCrashLibrary.logError(t);
//                } else if (priority == Log.WARN) {
//                    FakeCrashLibrary.logWarning(t);
//                }
//            }
//        }
//    }

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
