package edu.com.app.main;

import android.app.Activity;
import android.content.Context;

import dagger.Module;
import dagger.Provides;
import edu.com.app.di.scope.ActivityScoped;
import edu.com.app.di.scope.ContextLife;


@Module
public class MainActivityModule {

    private Activity mActivity;
    private MainContract.Presenter mPresenter;

    public MainActivityModule(Activity activity,MainContract.Presenter presenter) {
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
    public MainContract.Presenter provideMainContractPresenter(){
        return mPresenter;
    }

}
