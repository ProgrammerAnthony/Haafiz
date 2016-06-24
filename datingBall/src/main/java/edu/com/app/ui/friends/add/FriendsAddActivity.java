package edu.com.app.ui.friends.add;

import edu.com.app.R;
import edu.com.app.base.AbsSwipeBackActivity;

/**
 * Created by Anthony on 2016/5/10.
 * Class Note:
 */
public class FriendsAddActivity  extends AbsSwipeBackActivity {




    @Override
    protected void initViewsAndEvents() {

    }

    @Override
    protected int getContentViewID() {
        return R.layout.activity_friends_add;
    }



    @Override
    protected void initToolBar() {

    }
    @Override
    protected void injectDagger() {
//        mActivityComponent.inject(this)
    }
}
