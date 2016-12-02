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
//        initAVOS();
    }

    public ApplicationComponent getAppComponent() {
        if (mAppComponent == null) {
            mAppComponent = DaggerApplicationComponent.builder().
                    applicationModule(new ApplicationModule(this)).build();
        }
        return mAppComponent;
    }


//    private void initAVOS() {
//        try {
//            ApplicationInfo appInfo = getPackageManager()
//                    .getApplicationInfo(getPackageName(), PackageManager.GET_META_DATA);
//            String id = appInfo.metaData.getString("AVOS_APP_ID");
//            String key = appInfo.metaData.getString("AVOS_APP_KEY");
//            // init LeanCloud AppId and AppKey
//            AVOSCloud.initialize(this, id, key);
//        } catch (PackageManager.NameNotFoundException e) {
//            e.printStackTrace();
//        }
//    }

    public static DaggerApplication get(Context context) {
        return (DaggerApplication) context.getApplicationContext();
    }

}
