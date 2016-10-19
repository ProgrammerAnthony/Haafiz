package com.anthony.app.module.segment;

import android.widget.FrameLayout;

import com.anthony.app.R;
import com.anthony.app.common.base.AbsBaseActivity;
import com.anthony.app.common.injection.component.ActivityComponent;
import com.anthony.segmentcontrol.SegmentControl;

import butterknife.BindView;

/**
 * Created by Anthony on 2016/10/14.
 * Class Note:
 */

public class SegmentControlActivity extends AbsBaseActivity {
    @BindView(R.id.segment_control)
    SegmentControl segmentControl;
    @BindView(R.id.child_fragment_content)
    FrameLayout childFragmentContent;



    @Override
    protected int getContentViewID() {
        return R.layout.prj_segment_control;
    }

    @Override
    protected void initViewsAndEvents() {
        segmentControl.setOnSegmentControlClickListener(new SegmentControl.OnSegmentControlClickListener() {
            @Override
            public void onSegmentControlClick(int index) {
                showToast("click "+ index);
            }
        });
    }

    @Override
    protected void injectDagger(ActivityComponent activityComponent) {
        activityComponent.inject(this);
    }

}
