package edu.com.app.splash;

import android.content.Context;

import dagger.Component;
import dagger.Module;
import edu.com.app.di.component.ApplicationComponent;
import edu.com.app.di.scope.ActivityScoped;
import edu.com.app.di.scope.ContextLife;
import edu.com.app.main.MainActivity;
import edu.com.app.main.MainActivityModule;

/**
 * Created by Anthony on 2016/6/6.
 * Class Note:
 */
@ActivityScoped
@Component(dependencies = ApplicationComponent.class, modules = SplashActivityModule.class)
public interface SplashActivityComponent {
    @ContextLife("Activity")
    Context getActivityContext();

    void inject(SplashActivity activity);
}
