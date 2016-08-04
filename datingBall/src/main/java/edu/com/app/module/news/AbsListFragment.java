package edu.com.app.module.news;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.OnClick;
import edu.com.app.R;
import edu.com.app.base.AbsBaseFragment;
import edu.com.app.data.DataManager;
import edu.com.app.data.bean.NewsItem;
import edu.com.app.data.retrofit.HttpSubscriber;
import edu.com.app.module.main.MainActivity;
import edu.com.app.widget.pullrefresh.PullToRefreshView;
import edu.com.app.widget.recyclerview.adapter.MultiItemTypeAdapter;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.schedulers.Schedulers;

/**
 * Created by Anthony on 2016/8/3.
 * Class Note:
 * a wrapper Fragment with {@link RecyclerView} list
 * support pull to refresh and pull up to load more with {@link PullToRefreshView}
 */
public abstract class AbsListFragment extends AbsBaseFragment {
    private static long AUTO_REFRESH_THRESHOLD = 60 * 1000;
    protected int mCurrentPageIndex = getInitPageIndex();
    protected int mPageCount = 0;
    protected int mPageSize = 15;
    protected MultiItemTypeAdapter mRecyclerAdapter;


    @Bind(R.id.recycleView)
    RecyclerView mRecyclerView;
    @Bind(R.id.ptr)
    PullToRefreshView mPtr;
    @Bind(R.id.layout_reload)
    RelativeLayout mLayoutReload;

    @Inject
    DataManager mDataManager;

//    private List<E> mTopicList=new ArrayList<>();
//    private List<E> mNewsList=new ArrayList<>();

    @Override
    protected void initViewsAndEvents(View rootView) {
        init();
    }

    @Override
    protected void initDagger() {
        ( (MainActivity)(getActivity())).activityComponent().inject(this);
    }

    /**
     * initialize {@link #mPtr}, {@link #mRecyclerView}and{@link #mLayoutReload}
     */
    private void init() {
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
                    Toast.makeText(mContext, "no more", Toast.LENGTH_SHORT).show();
                    mPtr.onFinishLoading();
                }
            }
        });


        mRecyclerView.setLayoutManager(getLayoutManager());
        if (getItemDecoration() != null) {
            mRecyclerView.addItemDecoration(getItemDecoration());
        }
        mRecyclerAdapter =getAdapter();
        mRecyclerAdapter.setOnItemClickListener(getItemListener());

        preLoad();
    }

    protected void preLoad() {

    }

    protected abstract MultiItemTypeAdapter getAdapter();

    protected abstract RecyclerView.ItemDecoration getItemDecoration();

    protected abstract RecyclerView.LayoutManager getLayoutManager();

    protected abstract MultiItemTypeAdapter.OnItemClickListener getItemListener();


//    if (index == 0) {
//        return getUrl();
//    } else {
//        String prefix = getUrl().substring(0, getUrl().lastIndexOf("."));
//        return prefix + "_" + String.valueOf(index) + ".json";
//    }
    protected abstract String getRequestUrl(int index);

    protected void refreshMoreData(int pageIndex) {
        loadData(pageIndex);
    }

    /**
     * on refresh
     */
    protected void refreshData() {
        loadData(getInitPageIndex());
    }

    protected void loadData(final int pageIndex) {
        final String url = getRequestUrl(pageIndex);//define load strategy

        mSubscription =mDataManager.loadData(url ,NewsItem.class)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnTerminate(new Action0() {
                    @Override
                    public void call() {

                    }
                })
                .subscribe(new HttpSubscriber<NewsItem>() {
                    @Override
                    public void onNext(NewsItem newsItem) {

                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                    }
                });
    }

    protected int getInitPageIndex() {
        return 0;
    }

    @Override
    protected int getContentViewID() {
        return R.layout.prj_fragment_recy_list;
    }

    @OnClick({ R.id.layout_reload})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layout_reload:
                mLayoutReload.setVisibility(View.INVISIBLE);
                mPtr.onAutoRefresh();
                break;
        }
    }
}
