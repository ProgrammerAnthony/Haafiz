package com.anthony.app.module.statusview;

import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.anthony.app.R;
import com.anthony.app.common.base.AbsBaseActivity;
import com.anthony.app.common.injection.component.ActivityComponent;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class ShowStatusActivity extends AbsBaseActivity {
    @BindView(R.id.activityToolbar)
    Toolbar activityToolbar;

    @Override
    protected int getContentViewID() {
        return R.layout.activity_show_status;
    }

    @Override
    protected void initViewsAndEvents() {
        setToolbar();

        List<Integer> skipIds = new ArrayList<>();
        skipIds.add(R.id.activityToolbar);
        skipIds.add(R.id.tv_title_login);

        String state = getIntent().getStringExtra("STATE");
        switch (state) {
            case "CONTENT":
                showContent(null);
                setTitle("Content");
                break;
            case "LOADING":
                showLoading(skipIds);
                setTitle("Loading");
                break;
            case "EMPTY":
                showEmpty(null, "Empty Shopping Cart",
                        "Please add things in the cart to continue.", skipIds);
                setTitle("Empty");
                break;
            case "ERROR":
                showError(null, "ERROR", "nothing but error", "Try Again", skipIds, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        showToast("this is where you restart your load of data");
                    }
                });
                setTitle("Error");
                break;
        }

    }

    @Override
    protected void injectDagger(ActivityComponent activityComponent) {
        activityComponent.inject(this);
    }


    private void setToolbar() {
        setSupportActionBar(activityToolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().openOptionsMenu();
            setTitle("Status Layout");
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
