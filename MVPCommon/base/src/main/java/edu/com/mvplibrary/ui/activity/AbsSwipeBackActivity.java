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

import edu.com.mvplibrary.ui.widget.loading.VaryViewHelperController;
import edu.com.mvplibrary.ui.widget.netstatus.NetChangeObserver;
import edu.com.mvplibrary.ui.widget.netstatus.NetStateReceiver;
import edu.com.mvplibrary.ui.widget.netstatus.NetUtils;
import edu.com.mvplibrary.util.AppUtils;
import edu.com.mvplibrary.util.BaseAppManager;
import edu.com.mvplibrary.util.NightModeHelper;
import edu.com.mvplibrary.ui.widget.swipeback.SwipeBackActivityBase;
import edu.com.mvplibrary.ui.widget.swipeback.SwipeBackActivityHelper;
import edu.com.mvplibrary.ui.widget.swipeback.SwipeBackLayout;
import edu.com.mvplibrary.ui.widget.swipeback.Utils;

/**
 * Created by Anthony on 2016/4/28.
 * Class Note:
 * 1 same operation like {@link AbsBaseActivity },
 * but support swipe back with gesture
 */
public abstract class AbsSwipeBackActivity extends AbsBaseActivity implements SwipeBackActivityBase {
    /**
     * swipeBack helper to using swipeBack
     */
    private SwipeBackActivityHelper mHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mHelper = new SwipeBackActivityHelper(this);
        mHelper.onActivityCreate();
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

}
