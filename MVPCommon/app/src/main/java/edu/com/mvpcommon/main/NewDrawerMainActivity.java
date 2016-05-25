package edu.com.mvpcommon.main;

import android.support.v4.widget.DrawerLayout;

import edu.com.mvplibrary.ui.activity.AbsBaseActivity;

/**
 * Created by Anthony on 2016/5/3.
 * Class Note:
 * 1 use{@link DrawerLayout} to
 * acts as a top-level container for window content that allows for
 * interactive "drawer" views to be pulled out from the edge of the window.
 * 2 View in MVP
 * see {@link MainContract}------Manager role of MVP
 * {@link MainPresenter}---------Presenter
 * &{@link MainActivity}---------View
 * &{@link DrawerData}------------Model
 */
public class NewDrawerMainActivity extends AbsBaseActivity {
    @Override
    protected void initViewsAndEvents() {

    }

    @Override
    protected int getContentViewID() {
        return 0;
    }

    @Override
    protected void initDagger() {

    }
}
