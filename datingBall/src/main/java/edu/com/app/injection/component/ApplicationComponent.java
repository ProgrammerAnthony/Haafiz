package edu.com.app.injection.component;

import android.app.Application;
import android.content.Context;

import com.squareup.otto.Bus;

import javax.inject.Singleton;

import dagger.Component;
import edu.com.app.MyApplication;
import edu.com.app.data.DataManager;
import edu.com.app.data.EventPosterHelper;
import edu.com.app.data.rx.LeanCloudHelper;
import edu.com.app.data.rx.RxBus;
import edu.com.app.injection.module.ApplicationModule;
import edu.com.app.injection.scope.ApplicationContext;

/**
 * Created by Anthony on 2016/6/13.
 * Class Note:
 */
@Singleton
@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {

    void inject(MyApplication application);
//    void inject(SyncService syncService);

    @ApplicationContext
    Context context();

    Application application();

//    RxRealm realm();

    LeanCloudHelper rxLeanCLoud();

    RxBus rxBus();

//    FriendsService ribotService();

//    PreferencesHelper preferencesHelper();

//    DatabaseHelper databaseHelper();

    DataManager dataManager();

    Bus eventBus();

    EventPosterHelper eventPosterHelper();
}
