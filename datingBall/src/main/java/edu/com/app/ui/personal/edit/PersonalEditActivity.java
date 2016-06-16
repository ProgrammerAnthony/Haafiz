package edu.com.app.ui.personal.edit;

import edu.com.app.R;
import edu.com.app.ui.base.AbsSwipeBackActivity;

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
        activityComponent().inject(this);
    }
}
