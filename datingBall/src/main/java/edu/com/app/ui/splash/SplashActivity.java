package edu.com.app.ui.splash;

import android.content.Intent;

import javax.inject.Inject;

import edu.com.app.ui.base.AbsBaseActivity;
import edu.com.app.R;
import edu.com.app.ui.main.MainActivity;

/**
 * Created by Anthony on 2016/5/31.
 * Class Note:
 */
public class SplashActivity extends AbsBaseActivity implements SplashContract.View {
    @Inject
    SplashPresenter mPresenter;


    @Override
    public void toMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }


    @Override
    protected void initViewsAndEvents() {
//        mPresenter =new SplashPresenter(mContext);
        mPresenter.attachView(this);
        mPresenter.initData();
    }

    @Override
    protected int getContentViewID() {
        return R.layout.activity_splash;
    }

    @Override
    protected void injectDagger() {
        activityComponent().inject(this);
    /*    DaggerSplashActivityComponent.builder()
                .splashActivityModule(new SplashActivityModule(this, mPresenter))
                .applicationComponent(((MyApplication) getApplication()).getAppComponent()).build().inject(this);
                */
    }

    @Override
    protected void initToolBar() {

    }

}
