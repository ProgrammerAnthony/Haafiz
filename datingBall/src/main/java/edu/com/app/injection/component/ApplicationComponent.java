package edu.com.app.injection.component;

import android.app.Application;
import android.content.Context;

import com.squareup.otto.Bus;

import javax.inject.Singleton;

import dagger.Component;
import edu.com.app.MyApplication;
import edu.com.app.data.DataManager;
import edu.com.app.data.EventPosterHelper;
import edu.com.app.data.SyncService;
import edu.com.app.data.local.DatabaseHelper;
import edu.com.app.data.local.PreferencesHelper;
import edu.com.app.data.remote.FriendsService;
import edu.com.app.injection.module.ApplicationModule;
import edu.com.app.injection.scope.ApplicationContext;
import edu.com.app.data.rx.RxBus;
import edu.com.app.data.rx.RxLeanCloud;
import edu.com.app.data.rx.RxRealm;

/**
 * Created by Anthony on 2016/6/13.
 * Class Note:
 */
@Singleton
@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {

    void inject(MyApplication application);
    void inject(SyncService syncService);

    @ApplicationContext
    Context context();

    Application application();

    RxRealm realm();

    RxLeanCloud rxLeanCLoud();

    RxBus rxBus();

    FriendsService ribotService();

//    PreferencesHelper preferencesHelper();

//    DatabaseHelper databaseHelper();

    DataManager dataManager();

    Bus eventBus();

    EventPosterHelper eventPosterHelper();
}
