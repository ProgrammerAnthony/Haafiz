package edu.com.app.injection.module;

import android.app.Activity;
import android.content.Context;

import dagger.Module;
import dagger.Provides;
import edu.com.app.injection.scope.ActivityContext;

/**
 * Created by Anthony on 2016/6/13.
 * Class Note:
 * include {@link DbModule},define Database object in it
 */

@Module
        (includes = DbModule.class)
public class ActivityModule {
    private Activity mActivity;

    public ActivityModule(Activity activity) {
        mActivity = activity;
    }

    @Provides
    Activity provideActivity() {
        return mActivity;
    }

    @Provides
    @ActivityContext
    Context providesContext() {
        return mActivity;
    }
}
