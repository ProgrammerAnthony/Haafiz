package com.anthony.app.module.main;

import android.content.Intent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.anthony.app.R;
import com.anthony.app.common.base.AbsBaseFragment;
import com.anthony.app.common.injection.component.ActivityComponent;
import com.anthony.citypicker.CityPickerActivity;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by Anthony on 2016/9/12.
 * Class Note:
 * 主页 fragment
 */
public class HomeFragment extends AbsBaseFragment {

    @Bind(R.id.tv_location)
    TextView tvLocation;
    @Bind(R.id.layout_location)
    RelativeLayout layoutLocation;
    @Bind(R.id.iv_title_right)
    ImageView ivTitleRight;
    @Bind(R.id.layout_search)
    RelativeLayout layoutSearch;
    @Bind(R.id.tab_content)
    FrameLayout tabContent;

    @Override
    protected int getContentViewID() {
        return R.layout.dy_fragment_home;
    }

    @Override
    protected void initDagger2(ActivityComponent activityComponent) {

    }

    @Override
    protected void initViewsAndEvents(View rootView) {

    }


    @OnClick({R.id.layout_location, R.id.layout_search})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layout_location:
                Intent intent = new Intent(mContext, CityPickerActivity.class);
                mContext.startActivity(intent);
                break;
            case R.id.layout_search:
                break;
        }
    }
}
