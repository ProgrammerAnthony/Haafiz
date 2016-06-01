package edu.com.base.ui.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;



import butterknife.ButterKnife;

import cn.pedant.SweetAlert.SweetAlertDialog;

import edu.com.base.ui.BaseView;
import edu.com.base.util.AppUtils;
import edu.com.base.util.BaseAppManager;
import edu.com.base.util.LogUtil;
import edu.com.base.util.StatusBarUtil;
import edu.com.base.util.ToastUtils;
import edu.com.mvplibrary.R;

/**
 * Created by Anthony on 2016/4/24.
 * Class Note:
 * 1 所有的activity继承于这个类，
 * 2 实现BaseView中的方法（处理进度条，显示对话框）
 */
public abstract class AbsBaseActivity extends AppCompatActivity implements BaseView{
    protected static String TAG_LOG = null;// Log tag

    protected Context mContext = null;//context

    public SweetAlertDialog mProgressDialog;
    public SweetAlertDialog mWarningDialog;
    public SweetAlertDialog mErrorDialog;

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
        initDagger();
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
     * todo add Dagger init
     */
    protected abstract void initDagger();
    /**
     * todo init tool bar
     */
    protected abstract void initToolBar();
    /**
     * show Message in screen
     */
    protected void showMessageDialog(String msg) {
        if (null != msg && !AppUtils.isEmpty(msg)) {
//            Snackbar.make(this, msg, Snackbar.LENGTH_SHORT).show();
            ToastUtils.getInstance().showToast(msg);
        }
    }

    public void showWarningDialog(String title, String content, SweetAlertDialog.OnSweetClickListener listener) {
        mWarningDialog = new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                .setTitleText(title)
                .setContentText(content)
                .setConfirmText("确定")
                .setCancelText("取消")
                .setConfirmClickListener(listener);

        mWarningDialog.show();
    }

    public void showErrorDialog(String title, String content, SweetAlertDialog.OnSweetClickListener listener) {
        mErrorDialog = new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
                .setConfirmText("确定")
                .setTitleText(title)
                .setContentText(content)
                .setConfirmClickListener(listener);
        mErrorDialog.show();
    }

    public void showProgressDialog(String message) {
        mProgressDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        mProgressDialog.getProgressHelper().setBarColor(getResources().getColor(R.color.colorPrimary));
        mProgressDialog.setTitleText(message);
        mProgressDialog.setCancelable(true);
        mProgressDialog.show();

    }


    public void showProgressDialog(String message, int progress) {
        mProgressDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        mProgressDialog.getProgressHelper().setBarColor(getResources().getColor(R.color.colorPrimary));
        mProgressDialog.setTitleText(message);
        mProgressDialog.setCancelable(true);
        mProgressDialog.getProgressHelper().setProgress(progress);
        mProgressDialog.show();
    }

    public void hideProgressDialog() {
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
        }
    }
    @Override
    public void showMessage(String msg) {
        showMessageDialog(msg);
    }

    @Override
    public void close() {
        finish();
    }

    @Override
    public void showProgress(String message) {
        showProgressDialog(message);
    }

    @Override
    public void showProgress(String message, int progress) {
        showProgressDialog(message, progress);
    }

    @Override
    public void hideProgress() {
        hideProgressDialog();
    }

    @Override
    public void showErrorMessage(String msg, String content) {
        showErrorDialog(msg, content, new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                sweetAlertDialog.dismissWithAnimation();
            }
        });
    }
}

