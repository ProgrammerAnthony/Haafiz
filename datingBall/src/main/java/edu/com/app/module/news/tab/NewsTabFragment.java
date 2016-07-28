package edu.com.app.module.news.tab;


import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
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
import edu.com.app.data.bean.Channel;
import edu.com.app.data.bus.RxBus;
import edu.com.app.module.main.MainActivity;
import edu.com.app.module.news.NewsContract;
import edu.com.app.widget.CustomViewPager;
import edu.com.app.widget.ViewDisplay;

/**
 * Created by Anthony on 2016/7/27.
 * Class Note:
 * todo implement function here
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

    private TabViewPagerAdapter mViewPagerAdapter;

    private static int INIT_INDEX = 0;


    @Override
    protected void initDagger() {
        ((MainActivity) (getActivity())).activityComponent().inject(this);
    }

    @Override
    protected void initViewsAndEvents(View rootView) {


        mPresenter.attachView(this, mSubscription);
        mPresenter.loadData();
    }

    private void initTabViewPager() {
        mViewPagerAdapter =getPagerAdapter();
        mViewPager.setAdapter(mViewPagerAdapter);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

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

    private TabViewPagerAdapter getPagerAdapter() {
        List<Channel> channels =new ArrayList<>();
        return new TabViewPagerAdapter(mContext,channels,getChildFragmentManager(),mViewDisplay);
    }

    protected int getInitIndex() {
        return INIT_INDEX;
    }
    @Override
    protected int getContentViewID() {
        return R.layout.fragment_tab;
    }


    @Override
    public void showSubscribeView() {

    }

    @Override
    public void showEmptyView() {

    }

    @Override
    public void showTabView(List<Channel> channels) {
        initTabViewPager();

        mViewPagerAdapter.clear();
        mViewPagerAdapter.addAll(channels);
        mViewPagerAdapter.notifyDataSetChanged();
//        mTabStrip.notifyDataSetChanged();
        mTabStrip.setShouldExpand(true);
    }




    @OnClick({R.id.ic_subscribe, R.id.layout_reload})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ic_subscribe:
                showPopupWindow();
                break;
            case R.id.layout_reload:
                mPresenter.loadData();
                break;
        }
    }

    private void showPopupWindow() {

    }
}
