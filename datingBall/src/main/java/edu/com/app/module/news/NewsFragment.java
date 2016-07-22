package edu.com.app.module.news;

import android.view.View;

import edu.com.app.R;
import edu.com.app.base.AbsBaseFragment;
import edu.com.app.module.main.MainActivity;

/**
 * Created by Anthony on 2016/7/22.
 * Class Note:
 * todo implements with MVP
 */
public class NewsFragment  extends AbsBaseFragment{

    @Override
    protected void initDagger() {
        ((MainActivity)( getActivity())).activityComponent().inject(this);
    }

    @Override
    protected void initViewsAndEvents(View rootView) {

    }

    @Override
    protected int getContentViewID() {
        return R.layout.fragment_lists;
    }
}
