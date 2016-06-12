package edu.com.app.splash;

import android.content.Context;

import dagger.Component;
import edu.com.app.injection.component.ApplicationComponent;
import edu.com.app.injection.scope.ActivityScoped;
import edu.com.app.injection.scope.ContextLife;

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
