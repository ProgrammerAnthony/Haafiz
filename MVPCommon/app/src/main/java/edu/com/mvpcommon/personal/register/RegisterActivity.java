package edu.com.mvpcommon.personal.register;

import android.view.View;

import edu.com.mvpcommon.R;
import edu.com.mvplibrary.ui.activity.AbsSwipeBackActivity;

/**
 * Created by Anthony on 2016/5/10.
 * Class Note:
 */
public class RegisterActivity extends AbsSwipeBackActivity {


    @Override
    protected View getLoadingTargetView() {
        return null;
    }

    @Override
    protected void initViewsAndEvents() {

    }

    @Override
    protected int getContentViewID() {
        return R.layout.activity_register;
    }

    @Override
    protected boolean isApplyStatusBarTranslucency() {
        return false;
    }
}
