package edu.com.app.splash;

import android.content.Intent;

import edu.com.base.ui.activity.AbsBaseActivity;
import edu.com.app.R;
import edu.com.app.main.MainActivity;

/**
 * Created by Anthony on 2016/5/31.
 * Class Note:
 */
public class SplashActivity extends AbsBaseActivity implements SplashContract.View {
    private SplashContract.Presenter mPresenter;


    @Override
    public void toMainActivity() {
        Intent intent =new Intent(this, MainActivity.class);
        startActivity(intent);
    }


    @Override
    protected void initViewsAndEvents() {
        mPresenter =new SplashPresenterImpl(mContext);
        mPresenter.attachView(this);
        mPresenter.initData();
    }

    @Override
    protected int getContentViewID() {
        return R.layout.activity_splash;
    }

    @Override
    protected void initDagger() {

    }

    @Override
    protected void initToolBar() {

    }

}
