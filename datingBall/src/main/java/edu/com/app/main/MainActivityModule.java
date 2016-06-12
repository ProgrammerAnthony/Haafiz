package edu.com.app.main;

import android.app.Activity;
import android.content.Context;

import dagger.Module;
import dagger.Provides;
import edu.com.app.injection.scope.ActivityScoped;
import edu.com.app.injection.scope.ContextLife;
import edu.com.base.ui.widget.ChoosePicDialog;


@Module
public class MainActivityModule {

    private Activity mActivity;
    private MainContract.Presenter mPresenter;
    private ChoosePicDialog mPicDialog;

    public MainActivityModule(Activity activity,MainContract.Presenter presenter,ChoosePicDialog picDialog) {
        mActivity = activity;
        mPresenter = presenter;
        mPicDialog = picDialog;
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
    @ActivityScoped
    public MainContract.Presenter provideMainContractPresenter(){
        return mPresenter;
    }

    @Provides
    @ActivityScoped
    public ChoosePicDialog provideChoosePicDialog(){
        return mPicDialog;
    }

}
