package com.anthony.app.dagger.component;

import android.content.Context;

import com.anthony.app.dagger.DaggerApplication;
import com.anthony.app.dagger.DataRepository;
import com.anthony.app.dagger.module.ApplicationModule;
import com.anthony.app.dagger.scope.ApplicationContext;
import com.anthony.app.dagger.scope.PerActivity;
import com.anthony.imageloader.ImageLoaderUtil;
import com.anthony.library.data.dao.ChannelDao;
import com.anthony.library.data.dao.NewsItemDao;
import com.anthony.library.data.dao.OfflineResourceDao;
import com.anthony.library.utils.ToastUtils;
import com.anthony.library.widgets.CircleProgressBar;
import com.anthony.library.widgets.ViewDisplay;
import com.squareup.otto.Bus;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by Anthony on 2016/6/13.
 * Class Note:
 */
@Singleton
@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {

    void inject(DaggerApplication application);

    /**
     * instance expose to children,
     * because children are in different Scope {@link PerActivity}
     * ,so if this is field children wanna load , we must expose here
     */
    @ApplicationContext
    Context context();


    DaggerApplication application();

    //third part lib Otto used here
    Bus eventBus();

    ViewDisplay viewDisplay();

    CircleProgressBar circleProgressBar();

    ChannelDao channelDao();

    NewsItemDao newsItemDao();

    OfflineResourceDao offlineResourceDao();

    ImageLoaderUtil imageLoaderUtil();

    ToastUtils toastUtils();

    DataRepository dataRepository();
}
