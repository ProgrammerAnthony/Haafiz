package com.anthony.app.module.videolist;


import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.anthony.app.R;
import com.anthony.app.dagger.DaggerActivity;
import com.anthony.app.dagger.component.ActivityComponent;
import com.anthony.library.base.AbsBaseFragment;
import com.anthony.library.data.RxBus;
import com.anthony.library.data.event.VideoOrientationChangeEvent;

import butterknife.BindView;
import butterknife.OnClick;
import rx.functions.Action1;

/**
 * Created by Anthony on 2016/10/19.
 * Class Note:
 * video list activity
 * currently load data from local "raw://video_list_data"
 */

public class VideoListActivity extends DaggerActivity {
    @BindView(R.id.iv_title_left)
    ImageView ivTitleLeft;
    @BindView(R.id.tv_title_center)
    TextView tvTitleCenter;
    @BindView(R.id.layout_content)
    FrameLayout layoutContent;
    @BindView(R.id.titleLayout)
    RelativeLayout titleLayout;

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
        mUrl = "raw://video_list_data";//load local data for testing
        tvTitleCenter.setText("Video List");


        Fragment fragment = new NewsVideoFragment();
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


        //监听屏幕方向变化，显示或隐藏底部Tab导航栏，方便全屏播放
        RxBus.getDefault().toObserverable(VideoOrientationChangeEvent.class)
                .subscribe(new Action1<VideoOrientationChangeEvent>() {
                    @Override
                    public void call(VideoOrientationChangeEvent event) {
                        if (event.newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
                            titleLayout.setVisibility(View.VISIBLE);
                        }

                        if (event.newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                            titleLayout.setVisibility(View.GONE);
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        throwable.printStackTrace();
                    }
                });
    }

    @Override
    protected void injectDagger(ActivityComponent activityComponent) {
        activityComponent.inject(this);
    }


    @OnClick(R.id.iv_title_left)
    public void onClick() {
        this.finish();
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        RxBus.getDefault().post(new VideoOrientationChangeEvent(newConfig));
    }

    @Override
    public void onBackPressed() {

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            return;
        }
        super.onBackPressed();
    }

}
