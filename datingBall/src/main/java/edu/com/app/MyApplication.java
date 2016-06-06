package edu.com.app;

import android.content.Context;
import android.support.multidex.MultiDex;

import edu.com.app.di.component.ApplicationComponent;
import edu.com.app.di.component.DaggerApplicationComponent;
import edu.com.app.di.module.ApplicationModule;
import edu.com.base.AbsApplication;

/**
 * Created by Anthony on 2016/6/3.
 * Class Note:
 */
public class MyApplication extends AbsApplication {
    private ApplicationComponent mAppComponent;
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();

        mAppComponent = DaggerApplicationComponent.builder().
                applicationModule(new ApplicationModule(this)).build();
    }

    public ApplicationComponent getAppComponent() {
        return mAppComponent;
    }

}
