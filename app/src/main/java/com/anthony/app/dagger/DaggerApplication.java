package com.anthony.app.dagger;


import android.content.Context;

import com.anthony.app.dagger.component.ApplicationComponent;
import com.anthony.app.dagger.component.DaggerApplicationComponent;
import com.anthony.app.dagger.module.ApplicationModule;
import com.anthony.library.MyApplication;

/**
 * Created by Anthony on 2016/12/1.
 * Class Note:
 * Dagger2 support
 */

public class DaggerApplication extends MyApplication {
    private ApplicationComponent mAppComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        getAppComponent().inject(this);
    }

    public ApplicationComponent getAppComponent() {
        if (mAppComponent == null) {
            mAppComponent = DaggerApplicationComponent.builder().
                    applicationModule(new ApplicationModule(this)).build();
        }
        return mAppComponent;
    }


    public static DaggerApplication get(Context context) {
        return (DaggerApplication) context.getApplicationContext();
    }

}
