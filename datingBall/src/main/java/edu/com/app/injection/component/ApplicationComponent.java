package edu.com.app.injection.component;

import android.content.Context;

import com.squareup.otto.Bus;

import javax.inject.Singleton;

import dagger.Component;
import edu.com.app.data.DataManager;
import edu.com.app.data.SyncService;
import edu.com.base.model.local.DatabaseHelper;
import edu.com.base.model.local.PreferencesHelper;
import edu.com.base.model.remote.RibotsService;
import edu.com.app.injection.module.ApplicationModule;
import edu.com.app.injection.scope.ContextLife;
import edu.com.base.model.rx.RxBus;
import edu.com.base.model.rx.RxLeanCloud;
import edu.com.base.model.rx.RxRealm;


@Singleton
@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {



    @ContextLife("Application")
    Context getContext();

    RxRealm getRealm();

    RxLeanCloud getRxLeanCLoud();

    RxBus getRxBus();

    void inject(SyncService syncService);

    RibotsService getRibotsService();

    PreferencesHelper getPreferencesHelper();

    DatabaseHelper getDatabaseHelper();

    DataManager getDataManager();

    Bus getEventBus();

}
