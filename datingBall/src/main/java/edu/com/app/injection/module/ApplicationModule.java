package edu.com.app.injection.module;

import android.app.Application;
import android.content.Context;

import com.squareup.otto.Bus;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import edu.com.app.data.remote.FriendsService;
import edu.com.app.injection.scope.ApplicationContext;

/**
 * Created by Anthony on 2016/6/13.
 * Class Note:
 */
@Module
public class ApplicationModule {
    protected final Application mApplication;

    public ApplicationModule(Application application) {
        mApplication = application;
    }

    @Provides
    Application provideApplication() {
        return mApplication;
    }

    @Provides
    @ApplicationContext
    Context provideContext() {
        return mApplication;
    }

    @Provides
    @Singleton
    Bus provideEventBus() {
        return new Bus();
    }

    @Provides
    @Singleton
    FriendsService provideFriendsService() {
        return FriendsService.Creator.newFriendService();
    }



}
