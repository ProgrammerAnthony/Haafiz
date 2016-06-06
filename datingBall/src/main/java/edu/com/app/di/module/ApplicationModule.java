package edu.com.app.di.module;

import android.app.Application;
import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import edu.com.app.MyApplication;
import edu.com.app.di.scope.ContextLife;
import edu.com.base.AbsApplication;
import edu.com.base.model.rx.RxBus;
import edu.com.base.model.rx.RxLeanCloud;
import edu.com.base.model.rx.RxRealm;
import edu.com.base.util.PreferenceManager;


@Module
public class ApplicationModule {
    private MyApplication mApplication;
    private PreferenceManager mPreferenceManager;
    private RxRealm mRxRealm;
    private RxLeanCloud mRxLeanCloud;
    private RxBus mRxBus;


    public ApplicationModule(MyApplication application) {
        mApplication = application;
        mPreferenceManager = new PreferenceManager(mApplication.getApplicationContext());
        mRxRealm = new RxRealm(mApplication.getApplicationContext());
        mRxLeanCloud = new RxLeanCloud(mApplication.getApplicationContext());
        mRxBus = new RxBus();
    }

    @Provides
    @Singleton
    @ContextLife("Application")
    public Context provideContext() {
        return mApplication.getApplicationContext();
    }


    @Provides
    @Singleton
    public PreferenceManager providePreferenceManager() {
        return mPreferenceManager;
    }

/*
    @Provides
    @Singleton
    public PreferenceManager providePreferenceManager(Application application) {
        return new PreferenceManager(application.getApplicationContext());
    }
*/

    @Provides
    @Singleton
    public RxRealm provideRealm() {
        return mRxRealm;

    }

    @Provides
    @Singleton
    public RxLeanCloud provideRxLeanCloud() {
        return mRxLeanCloud;
    }

    @Provides
    @Singleton
    public RxBus provideRxBus() {
        return mRxBus;
    }


}
