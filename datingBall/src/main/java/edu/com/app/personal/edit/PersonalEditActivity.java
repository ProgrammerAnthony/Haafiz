package edu.com.app.personal.edit;

import edu.com.app.R;
import edu.com.base.ui.activity.AbsSwipeBackActivity;

/**
 * Created by Anthony on 2016/5/10.
 * Class Note:
 */
public class PersonalEditActivity extends AbsSwipeBackActivity {



    @Override
    protected void initViewsAndEvents() {

    }

    @Override
    protected int getContentViewID() {
        return R.layout.activity_personal_edit;
    }



    @Override
    protected void initToolBar() {

    }
    @Override
    protected void injectDagger() {
//        mActivityComponent.inject(this);
    }
}
