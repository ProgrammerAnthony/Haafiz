package com.anthony.app.module.newslist;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.anthony.app.R;
import com.anthony.app.dagger.DaggerActivity;
import com.anthony.app.dagger.DaggerApplication;
import com.anthony.app.dagger.component.ActivityComponent;
import com.anthony.library.base.AbsBaseFragment;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Anthony on 2016/10/19.
 * Class Note:
 * news list activity
 * currently load data from local "raw://news_list_data"
 */

public class NewsListActivity extends DaggerActivity {
    @BindView(R.id.iv_title_left)
    ImageView ivTitleLeft;
    @BindView(R.id.tv_title_center)
    TextView tvTitleCenter;
    @BindView(R.id.layout_content)
    FrameLayout layoutContent;
    @Inject
    DaggerApplication mApplication;

    //    @Inject
//    RxBus rxBus;
    public static String URL = "url";
    public static String NAME = "name";


    private String mUrl = "";
    private String mName = "";

    @Override
    protected int getContentViewID() {
        return R.layout.prj_title_content;
    }



    @Override
    protected void initViewsAndEvents(Bundle savedInstanceState) {
        if (getIntent().getExtras() != null) {
            mUrl = getIntent().getExtras().getString(URL);
            mName = getIntent().getExtras().getString(NAME);
        }
        mUrl = "raw://news_list_data";//load local data for testing
        tvTitleCenter.setText("News List");


        Fragment fragment = new NewsListFragment();
        Bundle bundle = fragment.getArguments();
        bundle = bundle == null ? new Bundle() : bundle;
        if (!TextUtils.isEmpty(mUrl))
            bundle.putString(AbsBaseFragment.EXTRA_URL, mUrl);
        if (!TextUtils.isEmpty(mName)) {
//            bundle.putString();
        }
        fragment.setArguments(bundle);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.layout_content, fragment).commit();
    }

    @Override
    protected void injectDagger(ActivityComponent activityComponent) {
        activityComponent.inject(this);
    }


    @OnClick({R.id.iv_title_left, R.id.iv_title_right})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_title_left:
                this.finish();
                break;
            case R.id.iv_title_right:
                mApplication.getDataManager().getDatabaseHelper().exportDb();
//                showToast("导出数据库成功");
                break;
        }
    }


}
