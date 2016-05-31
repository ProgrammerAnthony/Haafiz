package edu.com.base.ui.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.umeng.analytics.MobclickAgent;

import butterknife.ButterKnife;

import cn.pedant.SweetAlert.SweetAlertDialog;

import edu.com.base.util.AppUtils;
import edu.com.base.util.BaseAppManager;
import edu.com.base.util.ToastUtils;
import edu.com.mvplibrary.R;

/**
 * Created by Anthony on 2016/4/24.
 * Class Note:
 * 所有的activity继承于这个类
 */
public abstract class AbsBaseActivity extends AppCompatActivity {
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
        BaseAppManager.getInstance().addActivity(this);


        if (getContentViewID() != 0)
            setContentView(getContentViewID());

        ButterKnife.bind(this);
        initDagger();
        initViewsAndEvents();
    }


    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
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
}

/**
 * loading view controller
 * <p>
 * is applyStatusBarTranslucency
 * <p>
 * get loading view
 * <p>
 * toggle show loading
 * <p>
 * toggle show empty
 * <p>
 * toggle show empty
 * <p>
 * toggle show error
 * <p>
 * toggle show network error
 * <p>
 * use SystemBarTintManager to change status bar
 *
 * @param tintDrawable
 * <p>
 * set status bar translucency
 * call this before use {@link SystemBarTintManager}
 * @param on
 * <p>
 * is applyStatusBarTranslucency
 * <p>
 * get loading view
 * <p>
 * toggle show loading
 * <p>
 * toggle show empty
 * <p>
 * toggle show empty
 * <p>
 * toggle show error
 * <p>
 * toggle show network error
 * <p>
 * use SystemBarTintManager to change status bar
 * @param tintDrawable
 * <p>
 * set status bar translucency
 * call this before use {@link SystemBarTintManager}
 * @param on
 * <p>
 * change to day/night mode if needed
 * <p>
 * network connected
 * <p>
 * network disconnected
 * <p>
 * get bundle extras
 * <p>
 * network status
 * <p>
 * is applyStatusBarTranslucency
 * <p>
 * get loading view
 * <p>
 * toggle show loading
 * <p>
 * toggle show empty
 * <p>
 * toggle show empty
 * <p>
 * toggle show error
 * <p>
 * toggle show network error
 * <p>
 * use SystemBarTintManager to change status bar
 * @param tintDrawable
 * <p>
 * set status bar translucency
 * call this before use {@link SystemBarTintManager}
 * @param on
 * <p>
 * change to day/night mode if needed
 * <p>
 * network connected
 * <p>
 * network disconnected
 * <p>
 * get bundle extras
 * <p>
 * network status
 */
//    private VaryViewHelperController mVaryViewHelperController = null;
/**
 * is applyStatusBarTranslucency
 */
//    protected abstract boolean isApplyStatusBarTranslucency();
/**
 * get loading view
 */
//    protected abstract View getLoadingTargetView();
//    private NightModeHelper mNightModeHelper;//day night mode change
/**
 * toggle show loading
 */
//    protected void toggleShowLoading(boolean toggle, String msg) {
//        if (null == mVaryViewHelperController) {
//            throw new IllegalArgumentException("You must return a right target view for loading");
//        }
//
//        if (toggle) {
//            mVaryViewHelperController.showLoading(msg);
//        } else {
//            mVaryViewHelperController.restore();
//        }
//    }

/**
 * toggle show empty
 */
//    protected void toggleShowEmpty(boolean toggle, String msg, View.OnClickListener onClickListener) {
//        if (null == mVaryViewHelperController) {
//            throw new IllegalArgumentException("You must return a right target view for loading");
//        }
//
//        if (toggle) {
//            mVaryViewHelperController.showEmpty(msg, onClickListener);
//        } else {
//            mVaryViewHelperController.restore();
//        }
//    }

/**
 * toggle show empty
 */
//    protected void toggleShowEmpty(boolean toggle, String msg, View.OnClickListener onClickListener, int img) {
//        if (null == mVaryViewHelperController) {
//            throw new IllegalArgumentException("You must return a right target view for loading");
//        }
//
//        if (toggle) {
//            mVaryViewHelperController.showEmpty(msg, onClickListener, img);
//        } else {
//            mVaryViewHelperController.restore();
//        }
//    }

/**
 * toggle show error
 */
//    protected void toggleShowError(boolean toggle, String msg, View.OnClickListener onClickListener) {
//        if (null == mVaryViewHelperController) {
//            throw new IllegalArgumentException("You must return a right target view for loading");
//        }
//
//        if (toggle) {
//            mVaryViewHelperController.showError(msg, onClickListener);
//        } else {
//            mVaryViewHelperController.restore();
//        }
//    }

/**
 * toggle show network error
 */
//    protected void toggleNetworkError(boolean toggle, View.OnClickListener onClickListener) {
//        if (null == mVaryViewHelperController) {
//            throw new IllegalArgumentException("You must return a right target view for loading");
//        }
//
//        if (toggle) {
//            mVaryViewHelperController.showNetworkError(onClickListener);
//        } else {
//            mVaryViewHelperController.restore();
//        }
//    }

/**
 * use SystemBarTintManager to change status bar
 *
 * @param tintDrawable
 */
//    protected void setSystemBarTintDrawable(Drawable tintDrawable) {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            SystemBarTintManager mTintManager = new SystemBarTintManager(this);
//            if (tintDrawable != null) {
//                mTintManager.setStatusBarTintEnabled(true);
//                mTintManager.setTintDrawable(tintDrawable);
//            } else {
//                mTintManager.setStatusBarTintEnabled(false);
//                mTintManager.setTintDrawable(null);
//            }
//        }
//    }

/**
 * set status bar translucency
 * call this before use {@link SystemBarTintManager}
 *
 * @param on
 */
//    protected void setTranslucentStatus(boolean on) {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            Window win = getWindow();
//            WindowManager.LayoutParams winParams = win.getAttributes();
//            final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
//            if (on) {
//                winParams.flags |= bits;
//            } else {
//                winParams.flags &= ~bits;
//            }
//            win.setAttributes(winParams);
//        }
//    }
//
//mNetChangeObserver = new NetChangeObserver() {
//@Override
//public void onNetConnected(NetUtils.NetType type) {
//        super.onNetConnected(type);
//        onNetworkConnected(type);
//        }
//
//@Override
//public void onNetDisConnect() {
//        super.onNetDisConnect();
//        onNetworkDisConnected();
//        }
//        };
//
//        NetStateReceiver.registerObserver(mNetChangeObserver);
/**
 * change to day/night mode if needed
 */
//    protected void changeDayNightMode() {
//        mNightModeHelper.toggle();
//    }
/**
 * network connected
 */
//    protected void onNetworkConnected(NetUtils.NetType type) {
//        // TODO: 2016/5/11 net work connect ,do something
//    }

/**
 * network disconnected
 */
//    protected void onNetworkDisConnected() {
//        // TODO: 2016/5/11 net work disconnect ,do something
//    }
/**
 * get bundle extras
 */
//    protected void getBundleExtras(Bundle extras) {
////        mWebTitle = extras.getString(BUNDLE_KEY_TITLE);
//    }
/**
 * network status
 */
//    protected NetChangeObserver mNetChangeObserver = null;
//        mNightModeHelper = new NightModeHelper(this);