package edu.com.app.base;

import android.os.Bundle;
import android.view.View;

import com.anthony.ultimateswipetool.SwipeHelper;
import com.anthony.ultimateswipetool.activity.SwipeBackActivityBase;
import com.anthony.ultimateswipetool.activity.SwipeBackLayout;


//import edu.com.app.widget.swipebackActivity.SwipeBackActivityBase;
//import edu.com.app.widget.swipebackActivity.SwipeBackActivityHelper;
//import edu.com.app.widget.swipebackActivity.SwipeBackLayout;

/**
 * Created by Anthony on 2016/4/28.
 * Class Note:
 * 1 same operation like {@link AbsBaseActivity },
 * but support swipe back with gesture
 */
public abstract class AbsSwipeBackActivity extends AbsBaseActivity implements SwipeBackActivityBase {
    private SwipeHelper mHelper;

    public AbsSwipeBackActivity() {
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mHelper = new SwipeHelper(this);
        this.mHelper.onActivityCreate();
    }

    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        this.mHelper.onPostCreate();
    }

    public View findViewById(int id) {
        View v = super.findViewById(id);
        return v == null && this.mHelper != null?this.mHelper.findViewById(id):v;
    }

    public SwipeBackLayout getSwipeBackLayout() {
        return this.mHelper.getSwipeBackLayout();
    }

    public void setSwipeBackEnable(boolean enable) {
        this.getSwipeBackLayout().setEnableGesture(enable);
    }

    public void scrollToFinishActivity() {
        SwipeHelper.convertActivityToTranslucent(this);
        this.getSwipeBackLayout().scrollToFinishActivity();
    }

    public void setScrollDirection(int edgeFlags) {
        this.getSwipeBackLayout().setEdgeTrackingEnabled(edgeFlags);
    }
}
