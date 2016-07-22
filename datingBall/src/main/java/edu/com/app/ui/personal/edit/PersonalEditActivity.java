package edu.com.app.ui.personal.edit;

import edu.com.app.R;
import edu.com.app.base.AbsSwipeBackActivity;
import edu.com.app.injection.component.ActivityComponent;

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
    protected void injectDagger(ActivityComponent activityComponent) {
        activityComponent.inject(this);
    }
}
