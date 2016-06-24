package edu.com.app.base;

import android.os.Bundle;
import android.view.View;
import edu.com.app.base.widget.swipeback.SwipeBackActivityBase;
import edu.com.app.base.widget.swipeback.SwipeBackActivityHelper;
import edu.com.app.base.widget.swipeback.SwipeBackLayout;

/**
 * Created by Anthony on 2016/4/28.
 * Class Note:
 * 1 same operation like {@link AbsBaseActivity },
 * but support swipe back with gesture
 */
public abstract class AbsSwipeBackActivity extends AbsBaseActivity implements SwipeBackActivityBase {
    /**
     * swipeBack helper to using swipeBack
     */
    private SwipeBackActivityHelper mHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mHelper = new SwipeBackActivityHelper(this);
        mHelper.onActivityCreate();
    }
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mHelper.onPostCreate();
    }

    @Override
    public View findViewById(int id) {
        View v = super.findViewById(id);
        if (v == null && mHelper != null)
            return mHelper.findViewById(id);
        return v;
    }

    @Override
    public SwipeBackLayout getSwipeBackLayout() {
        return mHelper.getSwipeBackLayout();
    }

    @Override
    public void setSwipeBackEnable(boolean enable) {
        getSwipeBackLayout().setEnableGesture(enable);
    }

    @Override
    public void scrollToFinishActivity() {
//        SwipeBackUtils.convertActivityToTranslucent(this);
        SwipeBackActivityHelper.convertActivityToTranslucent(this);
        getSwipeBackLayout().scrollToFinishActivity();
    }

}
