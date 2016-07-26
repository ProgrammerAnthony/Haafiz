package edu.com.app.injection.module;

import android.content.Context;

import com.squareup.otto.Bus;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import edu.com.app.MyApplication;
import edu.com.app.injection.scope.ApplicationContext;

/**
 * Created by Anthony on 2016/6/13.
 * Class Note:
 * Application Module，
 */
@Module
public class ApplicationModule {
    protected final MyApplication mApplication;

    public ApplicationModule(MyApplication application) {
        mApplication = application;
    }

    @Provides
    @Singleton
    MyApplication provideApplication() {
        return mApplication;
    }

    @Provides
    @ApplicationContext
    @Singleton
    Context provideContext() {
        return mApplication;
    }


    /**
     * third part lib must provided in module
     * @return
     */
    @Provides
    @Singleton
    Bus provideEventBus() {
        return new Bus();
    }





}
