package com.anthony.app.common.injection.component;

import android.content.Context;

import com.anthony.app.common.base.MyApplication;
import com.anthony.app.common.data.database.dao.ChannelDao;
import com.anthony.app.common.data.database.dao.NewsItemDao;
import com.anthony.app.common.injection.module.ApplicationModule;
import com.anthony.app.common.injection.scope.ApplicationContext;
import com.anthony.app.common.widgets.CircleProgressBar;
import com.anthony.app.common.widgets.ViewDisplay;
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

    void inject(MyApplication application);

    /**
     * instance expose to children,
     * because children are in different Scope {@link com.anthony.app.common.injection.scope.PerActivity}
     * ,so if this is field children wanna load , we must expose here
     */
    @ApplicationContext
    Context context();


    MyApplication application();

    //third part lib Otto used here
    Bus eventBus();

    ViewDisplay viewDisplay();

    CircleProgressBar circleProgressBar();

    ChannelDao channelDao();

    NewsItemDao newsItemDao();
}
