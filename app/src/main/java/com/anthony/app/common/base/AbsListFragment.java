package com.anthony.app.common.base;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.anthony.app.R;
import com.anthony.app.common.data.DataManager;
import com.anthony.app.common.data.bean.Channel;
import com.anthony.app.common.data.bean.NewsItem;
import com.anthony.app.common.data.bean.NormalJsonInfo;
import com.anthony.app.common.data.retrofit.HttpSubscriber;
import com.anthony.app.common.injection.component.ActivityComponent;
import com.anthony.app.common.utils.SpUtil;
import com.anthony.app.common.utils.TimeUtils;
import com.anthony.pullrefreshview.PullToRefreshView;
import com.anthony.rvhelper.adapter.MultiItemTypeAdapter;
import com.anthony.app.common.widgets.TopicWrapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

import javax.inject.Inject;

import rx.functions.Action0;

/**
 * Created by Anthony on 2016/9/9.
 * Class Note:
 * abstract class support pullToRefresh  + RecyclerView
 *
 * todo preload !!!!
 */
public abstract class AbsListFragment extends AbsBaseFragment {

    public static String EXTRA_CHANNEL = "channel";
    //    public static String EXTRA_URL = "url";
    private static long AUTO_REFRESH_THRESHOLD = 60 * 1000;
    protected Channel mChannel;

    protected int mCurrentPageIndex = getInitPageIndex();
    protected int mPageCount = 0;

    private RelativeLayout mReloadLayout;
    protected RecyclerView mRecyclerView;
    protected PullToRefreshView mPtr;
    protected MultiItemTypeAdapter mRecyclerAdapter;
    private TopicWrapper mTopicWrapper;
    @Inject
    DataManager mDataManager;
    protected Gson mGson;
    private String newUrl = "";


    @Override
    protected int getContentViewID() {
        return R.layout.prj_fragment_pull_list;
    }

    @Override
    protected void initDagger2(ActivityComponent activityComponent) {
        activityComponent.inject(this);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mGson = new GsonBuilder().create();
        if (getArguments() != null) {
            mChannel = (Channel) getArguments().getSerializable(EXTRA_CHANNEL);
        }
    }


    @Override
    protected void initViews(View rootView) {
        mPtr = (PullToRefreshView) rootView.findViewById(R.id.ptr);
        mPtr.setListener(new PullToRefreshView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshData();
            }

            @Override
            public void onLoadMore() {
                if (mCurrentPageIndex < mPageCount + (getInitPageIndex() - 1)) {
                    refreshMoreData(mCurrentPageIndex + 1);
                } else {
                    showToast("no more");
                    mPtr.onFinishLoading();
                }

            }
        });

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recycleView);
        mRecyclerView.setLayoutManager(getLayoutManager());
        if (getItemDecoration() != null) {
            mRecyclerView.addItemDecoration(getItemDecoration());
        }
        mRecyclerAdapter = getAdapter();
        mRecyclerAdapter.setOnItemClickListener(getItemListener());
        mTopicWrapper = new TopicWrapper(getActivity(), mRecyclerAdapter);
        mRecyclerView.setAdapter(mTopicWrapper);

        //Init reload layout
        mReloadLayout = (RelativeLayout) rootView.findViewById(R.id.prj_layout_reload);
        mReloadLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mReloadLayout.setVisibility(View.INVISIBLE);
                mPtr.onAutoRefresh();
            }
        });

        //Add cache data to list when first enter
//        preLoad();
    }


    @Override
    public void onResume() {
        super.onResume();
        if (isNeedRefresh()) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    mPtr.onAutoRefresh();
                }
            }, 1000);
        }
    }

    //pre load data in database
//    private void preLoad() {
//        String result = SpUtil.getString(getActivity(), getFragmentUrl());
//        if (TextUtils.isEmpty(result)) {
//            return;
//        }
//        mSubscription =
//        //获取Topic数据
//        List topic = parseTopic(jsonInfo);
//        mTopicWrapper.clearTopic();
//        if (topic != null && topic.size() > 0) {
//            mTopicWrapper.addTopic(topic);
//            mTopicWrapper.addTopicView();
//        } else {
//            mTopicWrapper.deleteTopicView();
//        }
//
//        //获取新闻数据
//        List data = parseData(jsonInfo);
//        if (data != null && data.size() > 0) {
//            mRecyclerAdapter.clearData();
//            mRecyclerAdapter.addDataAll(data);
//        }
//        mRecyclerView.getAdapter().notifyDataSetChanged();
//    }


    protected void refreshData() {
        loadData(getInitPageIndex());
    }

    protected void refreshMoreData(int index) {

        loadData(index);
    }

    protected void loadData(final int requestPageIndex) {
        newUrl = getRequestUrl(requestPageIndex);
        mSubscription = mDataManager.loadNewsJsonInfo(newUrl)
                .doOnTerminate(new Action0() {
                    @Override
                    public void call() {
                        mPtr.onFinishLoading();
                    }
                })
                .subscribe(new HttpSubscriber<NormalJsonInfo<NewsItem>>() {
                    @Override
                    public void onNext(NormalJsonInfo<NewsItem> jsonInfo) {
                        SpUtil.putLong(getActivity(), newUrl + "_LRT", TimeUtils.getNowTime());
                        if (requestPageIndex == getInitPageIndex()) {
                            if (jsonInfo.page_info != null) {
                                mPageCount = parsePageCount(jsonInfo);
                            }
                            mCurrentPageIndex = getInitPageIndex();
                        }

                        onDataReceived(requestPageIndex, parseTopic(jsonInfo), parseData(jsonInfo));
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        onErrorDataReceived();
                    }
                });
    }

    private void onDataReceived(int requestIndex, List topic, List data) {
        boolean isRefresh = (requestIndex == getInitPageIndex());
        if (isRefresh) {
            mTopicWrapper.clearTopic();
            mRecyclerAdapter.clearData();

            if (topic != null && topic.size() > 0) {
//                mTopicWrapper.addGZTopic(topic);
                mTopicWrapper.addTopic(topic);
                mTopicWrapper.addTopicView();
            } else {
                mTopicWrapper.deleteTopicView();
            }
        } else {
            mCurrentPageIndex += 1;
        }

        mRecyclerAdapter.addDataAll(data);
        mRecyclerView.getAdapter().notifyDataSetChanged();

        restoreTopic(topic);
        restoreData(data);
    }

    public void toggleAutoRefresh() {
        mPtr.onAutoRefresh();
    }

    protected void onErrorDataReceived() {
        if (mRecyclerAdapter.getDatas().size() == 0)
            mReloadLayout.setVisibility(View.VISIBLE);
        Toast.makeText(mContext, "加载失败", Toast.LENGTH_SHORT).show();
    }

    private boolean isNeedRefresh() {
        long lastRefreshTime = SpUtil.getLong(getActivity(), newUrl + "_LRT", 0L);
        long now = TimeUtils.getNowTime();
        return (now - lastRefreshTime) > AUTO_REFRESH_THRESHOLD;
    }

    protected int getInitPageIndex() {
        return 0;
    }

    protected abstract RecyclerView.LayoutManager getLayoutManager();

    protected abstract RecyclerView.ItemDecoration getItemDecoration();

    protected abstract MultiItemTypeAdapter getAdapter();

    protected abstract MultiItemTypeAdapter.OnItemClickListener getItemListener();

    protected abstract String getRequestUrl(int index);

    protected abstract List parseData(NormalJsonInfo jsonInfo);

    protected abstract List parseTopic(NormalJsonInfo jsonInfo);

    protected abstract int parsePageCount(NormalJsonInfo jsonInfo);

    protected abstract void restoreData(List data);

    protected abstract void restoreTopic(List data);

    @Override
    protected void loadData() {
        //empty implements
    }

}