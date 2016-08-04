package edu.com.app.module.news.tab;


import android.graphics.drawable.BitmapDrawable;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gxz.PagerSlidingTabStrip;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.OnClick;
import edu.com.app.R;
import edu.com.app.adapter.TabViewPagerAdapter;
import edu.com.app.base.AbsBaseFragment;
import edu.com.app.data.DataManager;
import edu.com.app.data.bean.Channel;
import edu.com.app.data.bus.RxBus;
import edu.com.app.module.main.MainActivity;
import edu.com.app.module.news.NewsContract;
import edu.com.app.util.ToastUtils;
import edu.com.app.widget.CustomViewPager;
import edu.com.app.widget.ViewDisplay;
import edu.com.app.widget.channel.ChannelAdapter;
import edu.com.app.widget.channel.ItemDragHelperCallback;
import timber.log.Timber;

/**
 * Created by Anthony on 2016/7/27.
 * Class Note:
 * news tab Fragment
 */
public class NewsTabFragment extends AbsBaseFragment implements NewsContract.TabView {
    @Inject
    NewsTabPresenter mPresenter;

    @Inject
    RxBus mRxBus;

    @Inject
    ViewDisplay mViewDisplay;


    @Bind(R.id.txt_news_title)
    TextView mNewsTitle;
    @Bind(R.id.tab_strip)
    PagerSlidingTabStrip mTabStrip;
    @Bind(R.id.ic_subscribe)
    ImageView mIvSubscribe;
    @Bind(R.id.view_pager)
    CustomViewPager mViewPager;
    @Bind(R.id.layout_reload)
    RelativeLayout mLayoutReload;
    @Bind(R.id.top_bar_view)
    View mTopBar;

    @Inject
    DataManager mDataManager;

    @Inject
    ToastUtils toastUtils;

    private PopupWindow mSubscribeWindow;

    private TabViewPagerAdapter mViewPagerAdapter;

    private static int INIT_INDEX = 0;
    private RecyclerView mSubscribeRecyclerView;
    //    public List<Channel> mMyChannels = new ArrayList<>();//my channels
//    public List<Channel> mOtherChannels = new ArrayList<>();//other channels
    //    public List<Channel> mDbChannels;//channels same with those in db
    private ChannelAdapter mSubscribeAdapter;


    @Override
    protected int getContentViewID() {
        return R.layout.fragment_tab;
    }

    @Override
    protected void initDagger() {
        ((MainActivity) (getActivity())).activityComponent().inject(this);
    }

    @Override
    protected void initViewsAndEvents(View rootView) {
        initTabViewPager();
        //subscription is null
        mPresenter.attachView(NewsTabFragment.this, mSubscription);
        mPresenter.loadDataFromDb();//we already load data in splash view ,so use db data.

    }


    private void initTabViewPager() {
        List<Channel> channels = new ArrayList<>();
        mViewPagerAdapter = new TabViewPagerAdapter(mContext, channels, getChildFragmentManager(), mViewDisplay);
        mViewPager.setAdapter(mViewPagerAdapter);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                //get current Channel
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        mTabStrip.setSmoothScrollWhenClickTab(true);// 这是当点击tab时内容区域Viewpager是否是左右滑动,默认是true
        mTabStrip.setFadeEnabled(true);// 是否支持动画渐变(颜色渐变和文字大小渐变)
//        mTabStrip.setUnderlineHeight(1);// 设置Tab底部线的高度,传入的是dp
//        mTabStrip.setUnderlineColor(getResources().getColor(R.color.color_1A000000));// 设置Tab底部线的颜色
//        mTabStrip.setDividerColor(getResources().getColor(R.color.color_80cbc4));// 设置Tab的分割线的颜色
//        mTabStrip.setDividerPaddingTopBottom(12);// 设置分割线的上下的间距,传入的是dp
//        mTabStrip.setIndicatorHeight(4);// 设置Tab 指示器Indicator的高度,传入的是dp
//        mTabStrip.setIndicatorColor(getResources().getColor(R.color.color_45c01a));// 设置Tab Indicator的颜色
//        mTabStrip.setTextSize(16);// 设置Tab标题文字的大小,传入的是sp
//        mTabStrip.setSelectedTextColor(getResources().getColor(R.color.color_45c01a));// 设置选中Tab文字的颜色
//        mTabStrip.setTextColor(getResources().getColor(R.color.color_C231C7));// 设置正常Tab文字的颜色
//        mTabStrip.setTabPaddingLeftRight(24);// 设置Tab文字的左右间距,传入的是dp
//        mTabStrip.setTabBackground(R.drawable.background_tab);// 设置点击每个Tab时的背景色
//        mTabStrip.setZoomMax(0.3F);// 设置最大缩放,是正常状态的0.3倍
//        mTabStrip.setShouldExpand(true);// 设置Tab是自动填充满屏幕的
        mTabStrip.setViewPager(mViewPager);
    }


    protected int getInitIndex() {
        return INIT_INDEX;
    }


    @OnClick({R.id.ic_subscribe, R.id.layout_reload})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ic_subscribe:
                showPopupWindow();
                break;
            case R.id.layout_reload:
                mPresenter.loadDataOnlineThenSave();
                break;
        }
    }


    private void showPopupWindow() {
        if (mSubscribeWindow == null) {
            View popupView = LayoutInflater.from(getActivity()).inflate(R.layout.prj_pop_window_subscribe, null);
            mSubscribeWindow = new PopupWindow(popupView, ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT, true);
            mSubscribeWindow.setBackgroundDrawable(new BitmapDrawable());
            mSubscribeWindow.setOutsideTouchable(true);
            mSubscribeWindow.setFocusable(true);
            mSubscribeWindow.setAnimationStyle(R.style.subscribe_pop_window_anim_style);
            mSubscribeRecyclerView = (RecyclerView) popupView.findViewById(R.id.subscribe_recycler_view);
            initSubscribeRecyclerView();


            mSubscribeWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    updateSubscribeInfo();
//                    sortList();
                    notifyDataSetChanged();
                    mSubscribeWindow = null;
                    mViewPager.setCurrentItem(0, true);
                }
            });
        } else {
            if (mSubscribeWindow.isShowing()) {
                mSubscribeWindow.dismiss();
                return;
            }
        }
        mSubscribeWindow.showAsDropDown(mTopBar);


    }

    /**
     * on subscribe view closing,
     * save current channels in {@link DataManager#mMyChannels}and {@link DataManager#mOtherChannels}
     * and update db at the same time
     */
    private void updateSubscribeInfo() {
        mPresenter.updateSubscribeInfo(mSubscribeAdapter.getMyChannelItems(), mSubscribeAdapter.getOtherChannelItems());
    }


    /**
     * init SubScribe RecyclerView
     * todo sort list
     */
    private void initSubscribeRecyclerView() {
        GridLayoutManager manager = new GridLayoutManager(getActivity(), 4);
        mSubscribeRecyclerView.setLayoutManager(manager);

        ItemDragHelperCallback callback = new ItemDragHelperCallback();
        final ItemTouchHelper helper = new ItemTouchHelper(callback);
        helper.attachToRecyclerView(mSubscribeRecyclerView);


        mSubscribeAdapter = new ChannelAdapter(getActivity(), helper, mDataManager.mMyChannels, mDataManager.mOtherChannels);
        manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                int viewType = mSubscribeAdapter.getItemViewType(position);
                return viewType == ChannelAdapter.TYPE_MY || viewType == ChannelAdapter.TYPE_OTHER ? 1 : 4;
            }
        });
        mSubscribeRecyclerView.setAdapter(mSubscribeAdapter);

        mSubscribeAdapter.setOnMyChannelItemClickListener(new ChannelAdapter.OnMyChannelItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                mSubscribeWindow.dismiss();
                mViewPager.setCurrentItem(position, true);
            }
        });
        mSubscribeAdapter.setOnBackClickListener(new ChannelAdapter.OnBackClickListener() {
            @Override
            public void onBackClick() {
                mSubscribeWindow.dismiss();
                updateSubscribeInfo();
//                sortList();
                notifyDataSetChanged();
                mViewPager.setCurrentItem(0, true);
            }
        });
    }

    /**
     * --operation in contract class--
     **/

    @Override
    public void showEmptyView() {
        mLayoutReload.setVisibility(View.VISIBLE);
    }


    /**
     * get channels data here
     *
     * @param channels
     */
    @Override
    public void showTabView(List<Channel> channels) {
        Timber.i("NewsTabFragment,channels we get here " + channels.size());
        mLayoutReload.setVisibility(View.INVISIBLE);
        //channels show on the tab()
        assignChannels(channels);
//        List<Channel> showChannels = mPresenter.assignChannels(channels);
//        Timber.i("NewsTabFragment,channels we show here " + showChannels.size());

        notifyDataSetChanged();

//        mViewPager.setCurrentItem(getInitIndex(), true);

    }

    /**
     * update channels data on tab strip
     */
    private void notifyDataSetChanged() {
        mViewPagerAdapter.clear();
        mViewPagerAdapter.addAll(mDataManager.mMyChannels);
        mViewPagerAdapter.notifyDataSetChanged();
        mTabStrip.setShouldExpand(true);
    }


    public void assignChannels(List<Channel> channels) {
        mDataManager.mMyChannels.clear();
        mDataManager.mOtherChannels.clear();
//        mMyChannels.clear();
//        mOtherChannels.clear();
        for (Channel channel : channels) {
            if (channel.isFix() == 1 || channel.isSubscribe() == 1) {
//                mMyChannels.add(channel);
                mDataManager.mMyChannels.add(channel);
//                Timber.i("NewsTabFragment,add my channel ,channel title is" + channel.title());
            } else {
                mDataManager.mOtherChannels.add(channel);
//                mOtherChannels.add(channel);
//                Timber.i("NewsTabFragment,add other channel ,channel title is" + channel.title());
            }
        }

//        return mDataManager.mMyChannels;
    }
}
