package edu.com.mvpCommon.splash;

import android.content.Intent;

import cn.pedant.SweetAlert.SweetAlertDialog;
import edu.com.base.ui.activity.AbsBaseActivity;
import edu.com.base.util.ToastUtils;
import edu.com.mvpCommon.R;
import edu.com.mvpCommon.main.MainActivity;
import edu.com.mvpCommon.personal.login.NewLoginActivity;

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
