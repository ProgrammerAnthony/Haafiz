package edu.com.app.di.component;

import android.content.Context;

import javax.inject.Singleton;

import dagger.Component;
import edu.com.app.di.module.ApplicationModule;
import edu.com.app.di.scope.ContextLife;
import edu.com.base.model.rx.RxBus;
import edu.com.base.model.rx.RxLeanCloud;
import edu.com.base.model.rx.RxRealm;
import edu.com.base.util.PreferenceManager;


@Singleton
@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {
    @ContextLife("Application")
    Context getContext();

    PreferenceManager getPreferenceManager();

    RxRealm getRealm();

    RxLeanCloud getRxLeanCLoud();

    RxBus getRxBus();

}
