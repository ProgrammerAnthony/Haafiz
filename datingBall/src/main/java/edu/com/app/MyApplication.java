package edu.com.app;

import android.app.Application;
import android.content.Context;
import android.os.Environment;
import android.support.multidex.MultiDex;
import android.util.Log;

import com.avos.avoscloud.AVOSCloud;
import com.squareup.otto.Bus;

import java.io.File;

import javax.inject.Inject;

import edu.com.app.data.DataManager;
import edu.com.app.injection.component.ApplicationComponent;
import edu.com.app.injection.component.DaggerApplicationComponent;
import edu.com.app.injection.module.ApplicationModule;
import edu.com.app.util.FakeCrashLibrary;
import timber.log.Timber;

/**
 * Created by Anthony on 2016/6/3.
 * Class Note:Base Application for Application
 * use in AndroidManifest.xml
 */
public class MyApplication extends Application {
    @Inject
    Bus mEventBus;
    @Inject
    DataManager mDataManager;

    private ApplicationComponent mAppComponent;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);//multi dex support
    }

    @Override
    public void onCreate() {
        super.onCreate();

        getAppComponent().inject(this);
        mEventBus.register(this);
        initLeanCloud();
// init EaseUI(for IM,Instant Messaging)
//        initEaseUI();
 //exception handler
//        Thread.setDefaultUncaughtExceptionHandler(new LocalFileUncaughtExceptionHandler(this,
//                Thread.getDefaultUncaughtExceptionHandler()));

        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        } else {
            //sorry,Fabric currently not available
//            Fabric.with(this, new Crashlytics());
//            Timber.plant(new CrashlyticsTree());
            Timber.plant(new CrashReportingTree());
        }
        Timber.tag("application");
        Timber.i("test timber,application created");
    }

    //init LeanCloud (Online storage)
    private void initLeanCloud() {
        // initial params( this, AppId, AppKey)
        AVOSCloud.initialize(this, "k9XVHXc7UHdiv6UOielOPyYc-gzGzoHsz", "5WHJ0HesNrOnveAYcoM5sLL2");
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



    /** A tree which logs important information for crash reporting.fake one */
    public  class CrashReportingTree extends Timber.Tree {
        @Override protected void log(int priority, String tag, String message, Throwable t) {
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
