package com.anthony.app.module;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.anthony.app.R;
import com.anthony.app.common.base.AbsBaseActivity;
import com.anthony.app.common.data.bean.Channel;
import com.anthony.app.common.injection.component.ActivityComponent;
import com.anthony.app.common.widgets.ViewDisplay;
import com.anthony.app.common.widgets.recyclerview.adapter.CommonAdapter;
import com.anthony.app.common.widgets.recyclerview.base.ViewHolder;
import com.anthony.app.common.widgets.recyclerview.divider.RecycleViewDivider;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

/**
 * Created by Anthony on 2016/10/10.
 * Class Note:
 * Main list ,show all of the operation in this page
 * 首页列表，显示所有的操作
 */

public class MainListActivity extends AbsBaseActivity {
    List<Channel> strs = new ArrayList<>();
    @BindView(R.id.recycleView)
    RecyclerView recycleView;
    @Inject
    ViewDisplay mViewDisplay;

    private JustTitleAdapter mJustTitleAdapter;

    @Override
    protected int getContentViewID() {
        return R.layout.prj_rev_list;
    }

    @Override
    protected void initViewsAndEvents() {


        mJustTitleAdapter = new JustTitleAdapter(mContext);
        recycleView.addItemDecoration(new RecycleViewDivider(mContext, LinearLayoutManager.VERTICAL));
        recycleView.setLayoutManager(new LinearLayoutManager(mContext));
        recycleView.setAdapter(mJustTitleAdapter);

        initData();
    }

    private void initData() {
        strs.add(new Channel("tab 底部标签页面", "1001"));
        strs.add(new Channel("splash view 闪屏页", "1002"));
        strs.add(new Channel("city list 城市列表", "1003"));//com.anthony.citypicker.CityPickerActivity
        strs.add(new Channel("banner 广告栏", "1004"));
        strs.add(new Channel("weather API 天气接口接入", "1005"));
        strs.add(new Channel("load github API 通用retrofit 加载github数据", "1006"));
        strs.add(new Channel("SegmentControl 分段控件", "1007"));
        strs.add(new Channel("视频播放列表", "1008"));
        strs.add(new Channel("todo 新闻列表", ""));
        strs.add(new Channel("todo Emoji Input Layout emoji输入框", ""));
        strs.add(new Channel("todo Image Compress 图片压缩", ""));
//        strs.add(new Channel("todo网页加载", ""));
//        strs.add(new Channel("todo推送", ""));
//        strs.add(new Channel("todo图片加载", ""));
        strs.add(new Channel("todo图文添加以及上传", ""));
        strs.add(new Channel("todo栏目订阅排序", ""));
        strs.add(new Channel("todo LeadCloud登录注册", ""));
        strs.add(new Channel("todo ShareSDK分享", ""));
        strs.add(new Channel("todo 普通新闻列表", ""));
        strs.add(new Channel("todo 即时通讯", ""));
        strs.add(new Channel("todo 直播", ""));


        mJustTitleAdapter.addDataAll(strs);
        mJustTitleAdapter.notifyDataSetChanged();
    }

    @Override
    protected void injectDagger(ActivityComponent activityComponent) {
        activityComponent.inject(this);
    }


    public class JustTitleAdapter extends CommonAdapter<Channel> {
        public JustTitleAdapter(Context context) {
            super(context, R.layout.prj_just_title);
        }


        @Override
        protected void convert(ViewHolder holder, final Channel channel, int position) {
            holder.setText(R.id.tv_item_title, channel.getTitle());
            holder.getConvertView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (channel.getUrl() != null && !channel.getUrl().isEmpty()) {
                        mViewDisplay.showActivity(mContext, channel.getUrl());//打开activity
                    } else {
                        showToast("暂未提供实现");
                    }

                }
            });
        }
    }
}
