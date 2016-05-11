package edu.com.mvpcommon;

import android.view.View;

import edu.com.mvplibrary.ui.activity.AbsSwipeBackActivity;

/**
 * Created by Anthony on 2016/5/10.
 * Class Note:
 */
public class SplashActivity extends AbsSwipeBackActivity{


    @Override
    protected View getLoadingTargetView() {
        return null;
    }

    @Override
    protected void initViewsAndEvents() {

    }

    @Override
    protected int getContentViewID() {
        return R.layout.activity_splash;
    }

    @Override
    protected boolean isApplyStatusBarTranslucency() {
        return false;
    }


}
