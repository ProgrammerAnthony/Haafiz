package edu.com.app.main;

import android.content.Context;

import dagger.Component;
import edu.com.app.di.component.ApplicationComponent;
import edu.com.app.di.scope.ActivityScoped;
import edu.com.app.di.scope.ContextLife;


@ActivityScoped
@Component(dependencies = ApplicationComponent.class, modules = MainActivityModule.class)
public interface MainActivityComponent {
    @ContextLife("Activity")
    Context getActivityContext();

    void inject(MainActivity activity);

}
