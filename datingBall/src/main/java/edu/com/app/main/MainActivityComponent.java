package edu.com.app.main;

import android.content.Context;

import dagger.Component;
import edu.com.app.injection.component.ApplicationComponent;
import edu.com.app.injection.scope.ActivityScoped;
import edu.com.app.injection.scope.ContextLife;


@ActivityScoped
@Component(dependencies = ApplicationComponent.class, modules = MainActivityModule.class)
public interface MainActivityComponent {
    @ContextLife("Activity")
    Context getActivityContext();

    void inject(MainActivity activity);

}
