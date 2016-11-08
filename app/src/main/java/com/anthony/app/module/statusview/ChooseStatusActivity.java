package com.anthony.app.module.statusview;

import android.content.Intent;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import com.anthony.app.R;
import com.anthony.app.common.base.AbsBaseActivity;
import com.anthony.app.common.injection.component.ActivityComponent;

import butterknife.BindView;
import butterknife.OnClick;

public class ChooseStatusActivity extends AbsBaseActivity {
    @BindView(R.id.activityToolbar)
    Toolbar activityToolbar;
    @BindView(R.id.loadingButton)
    Button loadingButton;
    @BindView(R.id.emptyButton)
    Button emptyButton;
    @BindView(R.id.errorButton)
    Button errorButton;
    @BindView(R.id.contentButton)
    Button contentButton;

    @Override
    protected int getContentViewID() {
        return R.layout.activity_choose_status_view;
    }

    @Override
    protected void initViewsAndEvents() {
        setToolbar();
    }


    @Override
    protected void injectDagger(ActivityComponent activityComponent) {
        activityComponent.inject(this);
    }

    private void setToolbar() {
        setSupportActionBar(activityToolbar);

        if (getSupportActionBar() != null) {
            setTitle("Status Layout");
        }
    }


    @OnClick({R.id.loadingButton, R.id.emptyButton, R.id.errorButton, R.id.contentButton})
    public void onClick(View view) {
        Intent intent = new Intent(getApplicationContext(), ShowStatusActivity.class);
        switch (view.getId()) {
            case R.id.loadingButton:
                intent.putExtra("STATE", "LOADING");
                break;
            case R.id.emptyButton:
                intent.putExtra("STATE", "EMPTY");
                break;
            case R.id.errorButton:
                intent.putExtra("STATE", "ERROR");
                break;
            case R.id.contentButton:
                intent.putExtra("STATE", "CONTENT");
                break;
        }
        startActivity(intent);
    }

}
