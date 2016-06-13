package edu.com.app.ui.news.newsList;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.astuetz.PagerSlidingTabStrip;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.Bind;
import edu.com.app.R;

import edu.com.app.data.bean.NewsData;
import edu.com.app.data.bean.Channel;

import edu.com.app.ui.base.AbsBaseFragment;
import edu.com.app.ui.main.MainActivity;
import edu.com.app.ui.widget.ViewDisplay;
import edu.com.app.util.AppUtils;

/**
 * Created by Anthony on 2016/5/3.
 * Class Note:
 * 1 use {@link PagerSlidingTabStrip} and
 * {@link ViewPager} to create tab fragment
 * TabViewPagerAdapter to return a FragmentStatePagerAdapter{@link FragmentStatePagerAdapter}
 * 2 View in MVP
 * {@link NewsContract}----------Manager role of MVP
 * {@link NewsPresenter}---------Presenter
 * &{@link NewsFragment}---------View
 * &{@link  NewsData}------------Model
 */
public class NewsFragment extends AbsBaseFragment implements NewsContract.View {

//    @Bind(R.id.fragment_tab_content)
//    LinearLayout mFragmentContent;
    @Bind(R.id.view_pager)
    ViewPager mViewPager;
    @Bind(R.id.tab_strip)
    PagerSlidingTabStrip mTabStrip;


    @Inject
    ViewDisplay viewDisplay;

    protected static String TAG = "NewsFragment";
    private static int INIT_INDEX = 0;

    private View mTopBar;


    private NewsPresenter mPresenter;
    private TabViewPagerAdapter mViewPagerAdapter;


    @Override
    protected void initDagger() {
        ((MainActivity) getActivity()).activityComponent().inject(this);
    }

    @Override
    protected void initViewsAndEvents(View rootView) {


        mTabStrip.setTextColor(Color.rgb(170, 170, 170),
                getResources().getColor(R.color.colorBlueDark));
        mTabStrip.setTextSize(AppUtils.sp2px(getActivity(), 14));


        mViewPagerAdapter = new TabViewPagerAdapter(getActivity(),
                getChildFragmentManager(),
                new ArrayList<Channel>());
        mViewPager.setAdapter(mViewPagerAdapter);
        mTabStrip.setViewPager(mViewPager);


        loadData();

    }

    @Override
    protected int getContentViewID() {
        return R.layout.fragment_tab;
    }

    protected void loadData() {
        if (mPresenter == null)
            mPresenter = new NewsPresenter( mContext);
        mPresenter.attachView(this);
        mPresenter.getData("raw://news_channels");

    }

    @Override
    public void onDataReceived(ArrayList<Channel> channels) {
        mViewPagerAdapter.addAll(channels);
        mViewPagerAdapter.notifyDataSetChanged();
        mTabStrip.notifyDataSetChanged();
    }

    /**
     * Created by Anthony on 2016/2/24.
     * Class Note: adapter extends FragmentStatePagerAdapter for Tab+ViewPager
     */
    public class TabViewPagerAdapter extends FragmentStatePagerAdapter {
        private Context mContext;
        private ArrayList<Channel> mChannels;

        public TabViewPagerAdapter(Context ctx, FragmentManager fm, ArrayList<Channel> channels) {
            super(fm);
            mContext = ctx;
            mChannels = channels;
        }

        @Override
        public Fragment getItem(int position) {
            //show child fragment
            Fragment fragment = viewDisplay.initialView(mContext, mChannels.get(position));
            if (fragment == null) {
                throw new IllegalArgumentException("cannnot get fragment");
            }
            return fragment;
//        return ViewDisplay.createFragment(mContext, channel);
        }

        @Override
        public int getCount() {
            return mChannels.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mChannels.get(position).getTitle();
        }

        public ArrayList<Channel> getChannels() {
            return mChannels;
        }

        public void add(Channel item) {
            mChannels.add(item);
        }

        public void addAll(ArrayList<Channel> dataList) {
            mChannels.addAll(dataList);
        }

        public void clear() {
            mChannels.clear();
        }
    }

}
