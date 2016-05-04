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

import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.umeng.analytics.MobclickAgent;

import butterknife.ButterKnife;

import edu.com.mvplibrary.util.BaseAppManager;
import edu.com.mvplibrary.util.NightModeHelper;
import edu.com.mvplibrary.ui.widget.swipeback.SwipeBackActivityBase;
import edu.com.mvplibrary.ui.widget.swipeback.SwipeBackActivityHelper;
import edu.com.mvplibrary.ui.widget.swipeback.SwipeBackLayout;
import edu.com.mvplibrary.ui.widget.swipeback.Utils;

/**
 * Created by Anthony on 2016/4/28.
 * Class Note:
 * 1 same operation like {@link AbsBaseActivity },but support swipe back with gesture
 */
public abstract class AbsSwipeBackActivity extends AppCompatActivity implements SwipeBackActivityBase {
    protected static String TAG_LOG = null;// Log tag
    /**
     * Screen information
     */
    protected int mScreenWidth = 0;
    protected int mScreenHeight = 0;
    protected float mScreenDensity = 0.0f;

    protected Context mContext = null;//context

    private NightModeHelper mNightModeHelper;//day night mode change
    private Toolbar mTooBar;
    private SwipeBackActivityHelper mHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        initViewsAndEvents();
    }

    /**
     * initialize views and events here
     */
    protected void initViewsAndEvents() {
        mContext = this;
        Bundle extras = getIntent().getExtras();
        if (null != extras) {
            getBundleExtras(extras);
        }
        TAG_LOG = this.getClass().getSimpleName();
        BaseAppManager.getInstance().addActivity(this);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        mScreenDensity = displayMetrics.density;
        mScreenHeight = displayMetrics.heightPixels;
        mScreenWidth = displayMetrics.widthPixels;
        mNightModeHelper = new NightModeHelper(this);

        if (getContentViewLayoutID() != 0) {
            setContentView(getContentViewLayoutID());
            ButterKnife.bind(this);
        } else {
            throw new IllegalArgumentException("You must return a right contentView layout resource Id");
        }


        initToolBar();

        //using swipe back
        mHelper = new SwipeBackActivityHelper(this);
        mHelper.onActivityCreate();
    }

    /**
     * get bundle extras
     *
     * @param extras
     */
    protected void getBundleExtras(Bundle extras) {
//        mWebTitle = extras.getString(BUNDLE_KEY_TITLE);
    }

    /**
     * bind layout resource file
     *
     * @return id of layout resource
     */
    protected abstract int getContentViewLayoutID();


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
        BaseAppManager.getInstance().removeActivity(this);
        ButterKnife.unbind(this);
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

    protected void initToolBar() {
//        if (mTooBar == null) {
//            mTooBar = (Toolbar) findViewById(R.id.toolbar);
//            if (mTooBar != null) {
//                setSupportActionBar(mTooBar);
//            }
//        }
//        final ActionBar ab = getSupportActionBar();
//        if (ab == null)
//            return;
//        ab.setHomeAsUpIndicator(R.drawable.ic_menu);
//        ab.setDisplayHomeAsUpEnabled(true);

    }


    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mHelper.onPostCreate();
    }

    @Override
    public View findViewById(int id) {
        View v = super.findViewById(id);
        if (v == null && mHelper != null)
            return mHelper.findViewById(id);
        return v;
    }

    @Override
    public SwipeBackLayout getSwipeBackLayout() {
        return mHelper.getSwipeBackLayout();
    }

    @Override
    public void setSwipeBackEnable(boolean enable) {
        getSwipeBackLayout().setEnableGesture(enable);
    }

    @Override
    public void scrollToFinishActivity() {
        Utils.convertActivityToTranslucent(this);
        getSwipeBackLayout().scrollToFinishActivity();
    }

//    protected abstract void createPresenter();
}
