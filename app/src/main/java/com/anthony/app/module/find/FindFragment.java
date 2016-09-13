package com.anthony.app.module.find;

import android.view.View;

import com.anthony.app.R;
import com.anthony.app.common.base.AbsBaseFragment;
import com.anthony.app.common.injection.component.ActivityComponent;

/**
 * Created by Anthony on 2016/9/12.
 * Class Note:
 * 发现 fragment
 */
public class FindFragment extends AbsBaseFragment {

    @Override
    protected int getContentViewID() {
        return R.layout.dy_fragment_find;
    }

    @Override
    protected void initDagger2(ActivityComponent activityComponent) {

    }

    @Override
    protected void initViewsAndEvents(View rootView) {

    }
}
