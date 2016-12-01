package com.anthony.app.dagger.module;


import android.app.Activity;
import android.content.Context;

import com.anthony.app.dagger.scope.ActivityContext;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Anthony on 2016/6/13.
 * Class Note:
 * Activity module class
 */

@Module
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
