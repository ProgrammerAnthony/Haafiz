package edu.com.app.module.splash;

import android.content.Intent;

import javax.inject.Inject;

import edu.com.app.base.AbsBaseActivity;
import edu.com.app.R;
import edu.com.app.injection.component.ActivityComponent;
import edu.com.app.module.main.MainActivity;

/**
 * Created by Anthony on 2016/5/31.
 * Class Note:
 * this class is simple but indicates how to use MVP in your project
 *
 * implements of splash view
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
        mPresenter.attachView(this);
        mPresenter.initData();
    }

    @Override
    protected int getContentViewID() {
        return R.layout.activity_splash;
    }

    @Override
    protected void injectDagger(ActivityComponent activityComponent) {
        activityComponent.inject(this);
    }



}
