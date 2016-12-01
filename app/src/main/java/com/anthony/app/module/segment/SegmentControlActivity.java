package com.anthony.app.module.segment;

import android.os.Bundle;
import android.widget.FrameLayout;

import com.anthony.app.R;
import com.anthony.app.dagger.DaggerActivity;
import com.anthony.app.dagger.component.ActivityComponent;
import com.anthony.segmentcontrol.SegmentControl;

import butterknife.BindView;

/**
 * Created by Anthony on 2016/10/14.
 * Class Note:
 */

public class SegmentControlActivity extends DaggerActivity {
    @BindView(R.id.segment_control)
    SegmentControl segmentControl;
    @BindView(R.id.child_fragment_content)
    FrameLayout childFragmentContent;



    @Override
    protected int getContentViewID() {
        return R.layout.prj_segment_control;
    }

    @Override
    protected void initViewsAndEvents(Bundle savedInstanceState) {
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
