package edu.com.base.ui.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;


import butterknife.ButterKnife;

import cn.pedant.SweetAlert.SweetAlertDialog;

import edu.com.base.ui.BaseView;
import edu.com.base.ui.widget.BaseAppManager;
import edu.com.base.ui.widget.DialogFactory;
import edu.com.base.util.LogUtil;

/**
 * Created by Anthony on 2016/4/24.
 * Class Note:
 * 1 所有的activity继承于这个类，
 * 2 实现BaseView中的方法（处理进度条，显示对话框）
 */
public abstract class AbsBaseActivity extends AppCompatActivity implements BaseView {
    protected static String TAG_LOG = null;// Log tag

    protected Context mContext = null;//context

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        mContext = this;

        TAG_LOG = this.getClass().getSimpleName();
        LogUtil.init(TAG_LOG);

        BaseAppManager.getInstance().addActivity(this);
//        StatusBarUtil.setColor(this, getResources().getColor(R.color.colorPrimary));

        if (getContentViewID() != 0)
            setContentView(getContentViewID());

        ButterKnife.bind(this);

        injectDagger();
        initToolBar();
        initViewsAndEvents();
    }


    @Override
    protected void onPause() {
        super.onPause();

    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    @Override
    public void finish() {
        super.finish();
        BaseAppManager.getInstance().removeActivity(this);
    }

    /**
     * init views and events here
     */
    protected abstract void initViewsAndEvents();

    /**
     * bind layout resource file
     */
    protected abstract int getContentViewID();

    /**
     * Dagger2 use in your application module(not used in 'base' module)
     */
    protected abstract void injectDagger();

    /**
     * todo init tool bar
     */
    protected abstract void initToolBar();

    /**
     * show Message in screen
     */

    /**
     * implements methods in BaseView
     */
    @Override
    public void showMessage(String msg) {
        DialogFactory.showMessageDialog(mContext,msg);
    }

    @Override
    public void close() {
        finish();
    }

    @Override
    public void showProgress(String message) {
        DialogFactory.showProgressDialog(mContext,message);
    }

    @Override
    public void showProgress(String message, int progress) {
        DialogFactory.showProgressDialog(mContext,message, progress);
    }

    @Override
    public void hideProgress() {
        DialogFactory.hideProgressDialog();
    }

    @Override
    public void showErrorMessage(String msg, String content) {
        DialogFactory.showErrorDialog(mContext,msg, content, new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                sweetAlertDialog.dismissWithAnimation();
            }
        });
    }
}

