package edu.com.mvplibrary.ui.activity;

import android.view.View;

import edu.com.mvplibrary.ui.widget.netstatus.NetUtils;

/**
 * Created by Anthony on 2016/5/4.
 * Class Note:
 */
public class BaseWebActivity extends AbsSwipeBackActivity{
    @Override
    protected void onNetworkConnected(NetUtils.NetType type) {

    }

    @Override
    protected void onNetworkDisConnected() {

    }

    @Override
    protected View getLoadingTargetView() {
        return null;
    }

    @Override
    protected int getContentViewLayoutID() {
        return 0;
    }
}
