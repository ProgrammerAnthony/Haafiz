package com.anthony.app.module.zhihu;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;

import com.anthony.app.R;
import com.anthony.app.dagger.DaggerActivity;
import com.anthony.app.dagger.component.ActivityComponent;
import com.anthony.library.data.net.HttpSubscriber;
import com.anthony.pullrefreshview.PullToRefreshView;
import com.anthony.rvhelper.adapter.CommonAdapter;
import com.anthony.rvhelper.base.ViewHolder;
import com.anthony.rvhelper.divider.RecycleViewDivider;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import rx.functions.Action0;

/**
 * Created by Anthony on 2016/12/2.
 * Class Note:
 */

public class ZhihuDailyListActivity extends DaggerActivity {
    @BindView(R.id.recycleView)
    RecyclerView recycleView;
    @BindView(R.id.ptr)
    PullToRefreshView ptr;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    private ZhihuDailyAdapter mAdapter;

    @Override
    protected void initViewsAndEvents(Bundle savedInstanceState) {
        //show loading but not include toolbar
        List<Integer> skipIds = new ArrayList<>();
        skipIds.add(com.anthony.library.R.id.toolbar);
        showLoading(skipIds);

        setToolBar(toolbar, "Zhihu Daily");

        ptr.setListener(new PullToRefreshView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                showToast("refresh not support");
            }

            @Override
            public void onLoadMore() {
                showToast("load more not support");
            }
        });

        mAdapter = new ZhihuDailyAdapter(mContext);
        recycleView.addItemDecoration(new RecycleViewDivider(mContext, LinearLayoutManager.VERTICAL));
        recycleView.setLayoutManager(new LinearLayoutManager(mContext));
        recycleView.setAdapter(mAdapter);

        getZhihuDailyData();
    }

    private void getZhihuDailyData() {
        getDataRepository()
                .getDailyData()
                .doOnTerminate(new Action0() {
                    @Override
                    public void call() {
                        ptr.onFinishLoading();
                    }
                })
                .subscribe(new HttpSubscriber<ZhihuDailyListBean>() {
                    @Override
                    public void onNext(ZhihuDailyListBean zhihuDailyListBean) {
                        onDataReceived(zhihuDailyListBean);
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        showToast("load error");
                    }
                });
    }


    /**
     * todo load topic images
     * @param zhihuDailyListBean
     */
    private void onDataReceived(ZhihuDailyListBean zhihuDailyListBean) {

        mAdapter.addDataAll(zhihuDailyListBean.getStories());
        mAdapter.notifyDataSetChanged();

        showContent();
    }

    @Override
    protected int getContentViewID() {
        return R.layout.prj_pull_list;
    }

    protected void injectDagger(ActivityComponent activityComponent) {
        activityComponent.inject(this);
    }

    public class ZhihuDailyAdapter extends CommonAdapter<ZhihuDailyListBean.StoriesBean> {
        public ZhihuDailyAdapter(Context context) {
            super(context, R.layout.prj_item_daily);
        }


        @Override
        protected void convert(ViewHolder holder, final ZhihuDailyListBean.StoriesBean item, int position) {
            holder.setText(R.id.tv_daily_item_title, item.getTitle());
            Glide.with(mContext).load(item.getImages().get(0)).crossFade().placeholder(R.mipmap.prj_default_pic_big).into((ImageView) holder.getView(R.id.iv_daily_item_image));
            holder.setOnClickListener(R.id.ll_click, v -> {
                ZhihuDailyDetailActivity.start(mContext, v.findViewById(R.id.iv_daily_item_image), mAdapter.getDatas().get(position).getId());
            });
        }
    }


}
