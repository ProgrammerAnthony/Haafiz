package edu.com.mvpcommon.setting;

import android.view.View;

import edu.com.mvpcommon.R;
import edu.com.mvplibrary.ui.activity.AbsSwipeBackActivity;
import edu.com.mvplibrary.ui.widget.netstatus.NetUtils;

/**
 * Created by Anthony on 2016/5/10.
 * Class Note:
 */
public class SettingActivity extends AbsSwipeBackActivity{


    @Override
    protected View getLoadingTargetView() {
        return null;
    }

    @Override
    protected int getContentViewID() {
        return R.layout.activity_setting;
    }
}
