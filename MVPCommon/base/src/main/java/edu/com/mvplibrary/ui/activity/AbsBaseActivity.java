package edu.com.mvplibrary.ui.activity;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.umeng.analytics.MobclickAgent;

import butterknife.ButterKnife;

import edu.com.mvplibrary.ui.BaseView;
import edu.com.mvplibrary.ui.widget.SystemBarTintManager;
import edu.com.mvplibrary.ui.widget.loading.VaryViewHelperController;
import edu.com.mvplibrary.ui.widget.netstatus.NetChangeObserver;
import edu.com.mvplibrary.ui.widget.netstatus.NetStateReceiver;
import edu.com.mvplibrary.ui.widget.netstatus.NetUtils;
import edu.com.mvplibrary.util.AppUtils;
import edu.com.mvplibrary.util.BaseAppManager;
import edu.com.mvplibrary.util.NightModeHelper;
import edu.com.mvplibrary.util.ToastUtils;

/**
 * Created by Anthony on 2016/4/24.
 * Class Note:
 * 1 extended from AppCompatActivity to do
 * some base operation.
 * 2 do operation in initViewAndEvents(){@link #initViewsAndEvents()}
 */
public abstract class AbsBaseActivity extends AppCompatActivity {
    protected static String TAG_LOG = null;// Log tag
    /**
     * Screen information
     */
    protected int mScreenWidth = 0;
    protected int mScreenHeight = 0;
    protected float mScreenDensity = 0.0f;

    protected Context mContext = null;//context

    private NightModeHelper mNightModeHelper;//day night mode change
    /**
     * network status
     */
    protected NetChangeObserver mNetChangeObserver = null;

    /**
     * loading view controller
     */
    private VaryViewHelperController mVaryViewHelperController = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        mContext = this;
        Bundle extras = getIntent().getExtras();
        if (null != extras) {
            getBundleExtras(extras);
        }
        setTranslucentStatus(isApplyStatusBarTranslucency());
        TAG_LOG = this.getClass().getSimpleName();
        BaseAppManager.getInstance().addActivity(this);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        mScreenDensity = displayMetrics.density;
        mScreenHeight = displayMetrics.heightPixels;
        mScreenWidth = displayMetrics.widthPixels;
        mNightModeHelper = new NightModeHelper(this);

        if (getContentViewID() != 0) {
            setContentView(getContentViewID());
            ButterKnife.bind(this);
            if (null != getLoadingTargetView()) {
                mVaryViewHelperController = new VaryViewHelperController(getLoadingTargetView());
            }
        } else {
            throw new IllegalArgumentException("You must return a right contentView layout resource Id");
        }


        mNetChangeObserver = new NetChangeObserver() {
            @Override
            public void onNetConnected(NetUtils.NetType type) {
                super.onNetConnected(type);
                onNetworkConnected(type);
            }

            @Override
            public void onNetDisConnect() {
                super.onNetDisConnect();
                onNetworkDisConnected();
            }
        };

        NetStateReceiver.registerObserver(mNetChangeObserver);

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
        NetStateReceiver.removeRegisterObserver(mNetChangeObserver);
    }

    @Override
    public void finish() {
        super.finish();
        BaseAppManager.getInstance().removeActivity(this);
    }

    /**
     * network connected
     */
    protected void onNetworkConnected(NetUtils.NetType type) {
        // TODO: 2016/5/11 net work connect ,do something
    }

    /**
     * network disconnected
     */
    protected void onNetworkDisConnected() {
        // TODO: 2016/5/11 net work disconnect ,do something
    }

    /**
     * get loading view
     */
    protected abstract View getLoadingTargetView();

    /**
     * get bundle extras
     */
    protected void getBundleExtras(Bundle extras) {
//        mWebTitle = extras.getString(BUNDLE_KEY_TITLE);
    }

    /**
     * initialize views and events here
     */
    protected abstract void initViewsAndEvents();

    /**
     * bind layout resource file
     */
    protected abstract int getContentViewID();
    /**
     * is applyStatusBarTranslucency
     */
    protected abstract boolean isApplyStatusBarTranslucency();

    /**
     * toggle show loading
     */
    protected void toggleShowLoading(boolean toggle, String msg) {
        if (null == mVaryViewHelperController) {
            throw new IllegalArgumentException("You must return a right target view for loading");
        }

        if (toggle) {
            mVaryViewHelperController.showLoading(msg);
        } else {
            mVaryViewHelperController.restore();
        }
    }

    /**
     * toggle show empty
     */
    protected void toggleShowEmpty(boolean toggle, String msg, View.OnClickListener onClickListener) {
        if (null == mVaryViewHelperController) {
            throw new IllegalArgumentException("You must return a right target view for loading");
        }

        if (toggle) {
            mVaryViewHelperController.showEmpty(msg, onClickListener);
        } else {
            mVaryViewHelperController.restore();
        }
    }

    /**
     * toggle show empty
     */
    protected void toggleShowEmpty(boolean toggle, String msg, View.OnClickListener onClickListener, int img) {
        if (null == mVaryViewHelperController) {
            throw new IllegalArgumentException("You must return a right target view for loading");
        }

        if (toggle) {
            mVaryViewHelperController.showEmpty(msg, onClickListener, img);
        } else {
            mVaryViewHelperController.restore();
        }
    }

    /**
     * toggle show error
     */
    protected void toggleShowError(boolean toggle, String msg, View.OnClickListener onClickListener) {
        if (null == mVaryViewHelperController) {
            throw new IllegalArgumentException("You must return a right target view for loading");
        }

        if (toggle) {
            mVaryViewHelperController.showError(msg, onClickListener);
        } else {
            mVaryViewHelperController.restore();
        }
    }

    /**
     * toggle show network error
     */
    protected void toggleNetworkError(boolean toggle, View.OnClickListener onClickListener) {
        if (null == mVaryViewHelperController) {
            throw new IllegalArgumentException("You must return a right target view for loading");
        }

        if (toggle) {
            mVaryViewHelperController.showNetworkError(onClickListener);
        } else {
            mVaryViewHelperController.restore();
        }
    }
    /**
     * use SystemBarTintManager to change status bar
     *
     * @param tintDrawable
     */
    protected void setSystemBarTintDrawable(Drawable tintDrawable) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            SystemBarTintManager mTintManager = new SystemBarTintManager(this);
            if (tintDrawable != null) {
                mTintManager.setStatusBarTintEnabled(true);
                mTintManager.setTintDrawable(tintDrawable);
            } else {
                mTintManager.setStatusBarTintEnabled(false);
                mTintManager.setTintDrawable(null);
            }
        }
    }

    /**
     * set status bar translucency
     * call this before use {@link SystemBarTintManager}
     *
     * @param on
     */
    protected void setTranslucentStatus(boolean on) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window win = getWindow();
            WindowManager.LayoutParams winParams = win.getAttributes();
            final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
            if (on) {
                winParams.flags |= bits;
            } else {
                winParams.flags &= ~bits;
            }
            win.setAttributes(winParams);
        }
    }

    /**
     * change to day/night mode if needed
     */
    protected void changeDayNightMode() {
        mNightModeHelper.toggle();
    }
    /**
     * show toast
     */
    protected void showToast(String msg) {
        //防止遮盖虚拟按键
        if (null != msg && !AppUtils.isEmpty(msg)) {
//            Snackbar.make(getLoadingTargetView(), msg, Snackbar.LENGTH_SHORT).show();
        }
    }
}
