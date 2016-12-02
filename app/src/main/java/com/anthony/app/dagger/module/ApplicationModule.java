package com.anthony.app.dagger.module;


import android.content.Context;

import com.anthony.app.dagger.DaggerApplication;
import com.anthony.app.dagger.DataRepository;
import com.anthony.app.dagger.component.ApplicationComponent;
import com.anthony.app.dagger.scope.ApplicationContext;
import com.anthony.imageloader.ImageLoaderUtil;
import com.anthony.library.data.dao.ChannelDao;
import com.anthony.library.data.dao.NewsItemDao;
import com.anthony.library.data.dao.OfflineResourceDao;
import com.anthony.library.utils.ToastUtils;
import com.anthony.library.widgets.CircleProgressBar;
import com.anthony.library.widgets.ViewDisplay;
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
    protected final DaggerApplication mApplication;

    public ApplicationModule(DaggerApplication application) {
        mApplication = application;
    }


    @Provides
    @Singleton
    DaggerApplication provideApplication() {
        return mApplication;
    }

    @Provides
    @ApplicationContext
    @Singleton
    Context provideContext() {
        return mApplication;
    }

    @Provides
    @Singleton
    DataRepository provideDataRepository(@ApplicationContext Context context) {
        return new DataRepository(context);
    }

    /**
     * third part lib must provided in module
     * if children with different scope also want to get this instantce
     * exposing in the {@link ApplicationComponent} is needed
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
        return new ChannelDao(mApplication.getDataRepository().getDatabaseHelper());
    }


    @Provides
    @Singleton
    NewsItemDao provideNewsItemDao() {
        return new NewsItemDao(mApplication.getDataRepository().getDatabaseHelper());
    }

    @Provides
    @Singleton
    OfflineResourceDao provideOfflineResourceDao() {
        return new OfflineResourceDao(mApplication.getDataRepository().getDatabaseHelper());
    }

    @Provides
    @Singleton
    ImageLoaderUtil imageLoaderUtil() {
        return new ImageLoaderUtil();
    }

    @Provides
    @Singleton
    ToastUtils provideToastUtils() {
        return new ToastUtils(mApplication.getApplicationContext());
    }


}
