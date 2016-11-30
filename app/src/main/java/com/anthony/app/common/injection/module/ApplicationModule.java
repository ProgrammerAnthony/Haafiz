package com.anthony.app.common.injection.module;


import android.content.Context;

import com.anthony.app.common.MyApplication;
import com.anthony.app.common.data.database.dao.ChannelDao;
import com.anthony.app.common.data.database.dao.NewsItemDao;
import com.anthony.app.common.data.database.dao.OfflineResourceDao;
import com.anthony.app.common.injection.scope.ApplicationContext;
import com.anthony.app.common.widgets.CircleProgressBar;
import com.anthony.app.common.widgets.ViewDisplay;
import com.anthony.imageloader.ImageLoaderUtil;
import com.squareup.otto.Bus;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

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
     * if children with different scope also want to get this instantce
     * exposing in the {@link com.anthony.app.common.injection.component.ApplicationComponent} is needed
     *
     * @return
     */
    @Provides
    @Singleton
    Bus provideEventBus() {
        return new Bus();
    }


    @Provides
    @Singleton
    ViewDisplay provideViewDisplay(@ApplicationContext Context context) {
        return new ViewDisplay(context);
    }

    @Provides
    @Singleton
    CircleProgressBar providesCircleProgressBar(@ApplicationContext Context context) {
        return new CircleProgressBar(context);
    }

    @Provides
    @Singleton
    ChannelDao providesChannelDao() {
        return new ChannelDao(mApplication);
    }


    @Provides
    @Singleton
    NewsItemDao provideNewsItemDao() {
        return new NewsItemDao(mApplication);
    }

    @Provides
    @Singleton
    OfflineResourceDao provideOfflineResourceDao() {
        return new OfflineResourceDao(mApplication);
    }

    @Provides
    @Singleton
    ImageLoaderUtil imageLoaderUtil() {
        return new ImageLoaderUtil();
    }


}
