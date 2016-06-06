package edu.com.app.splash;

import android.app.Activity;
import android.content.Context;

import dagger.Module;
import dagger.Provides;
import edu.com.app.di.scope.ActivityScoped;
import edu.com.app.di.scope.ContextLife;
import edu.com.app.main.MainContract;

/**
 * Created by Anthony on 2016/6/6.
 * Class Note:
 */

@Module
public class SplashActivityModule {
    private Activity mActivity;
    private SplashContract.Presenter mPresenter;

    public SplashActivityModule(Activity activity,SplashContract.Presenter presenter) {
        mActivity = activity;
        mPresenter = presenter;
    }

    @Provides
    @ActivityScoped
    @ContextLife("Activity")
    public Context provideContext() {
        return mActivity;
    }

    @Provides
    @ActivityScoped
    public Activity provideActivity() {
        return mActivity;
    }


    @Provides
    public SplashContract.Presenter provideSplashContractPresenter(){
        return mPresenter;
    }

}
