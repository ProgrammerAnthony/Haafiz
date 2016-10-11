package com.anthony.app.module.weather;

import com.anthony.app.R;
import com.anthony.app.common.base.AbsBaseActivity;
import com.anthony.app.common.injection.component.ActivityComponent;

/**
 * Created by Anthony on 2016/10/11.
 * Class Note:
 */

public class WeatherActivity extends AbsBaseActivity {

    @Override
    protected int getContentViewID() {
        return R.layout.prj_weather_activity;
    }

    @Override
    protected void initViewsAndEvents() {

    }

    @Override
    protected void injectDagger(ActivityComponent activityComponent) {
        activityComponent.inject(this);
    }
}
