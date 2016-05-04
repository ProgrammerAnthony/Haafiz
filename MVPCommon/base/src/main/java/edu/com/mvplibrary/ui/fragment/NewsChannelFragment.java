package edu.com.mvplibrary.ui.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.astuetz.PagerSlidingTabStrip;

import java.util.ArrayList;

import edu.com.mvplibrary.R;
import edu.com.mvplibrary.contract.DrawerMainContract;
import edu.com.mvplibrary.contract.NewsChannelContract;
import edu.com.mvplibrary.contract.presenter.NewsChannelPresenter;
import edu.com.mvplibrary.model.Channel;
import edu.com.mvplibrary.model.NewsChannelData;
import edu.com.mvplibrary.util.AppUtils;
import edu.com.mvplibrary.util.BaseUtil;

/**
 * Created by Anthony on 2016/5/3.
 * Class Note:
 * 1 use {@link com.astuetz.PagerSlidingTabStrip} and
 * {@link ViewPager} to create tab fragment
 * TabViewPagerAdapter to return a FragmentStatePagerAdapter{@link FragmentStatePagerAdapter}
 * 2 View in MVP
 * {@link NewsChannelContract}-------------------------------------------Manager role of MVP
 * {@link NewsChannelPresenter}---------Presenter
 * &{@link NewsChannelFragment}-----------------------------------------------------View
 * &{@link  NewsChannelData}--------------------Model
 */
public class NewsChannelFragment extends AbsBaseFragment implements NewsChannelContract.View, View.OnClickListener {
    protected static String TAG = "com.trs.fragment.TRSAbsTabFragment";
    private static int INIT_INDEX = 0;
    private PagerSlidingTabStrip mTabStrip;
    private ViewPager mViewPager;
//    private TabViewPagerAdapter mViewPagerAdapter;

    //    private Menu mTabMenu;
    private View mTopBar;

    private RelativeLayout mReloadLayout;

    private NewsChannelPresenter mPresenter;

    @Override
    protected int getContentViewID() {
        return R.layout.abs_fragment_tab;
    }

    @Override
    protected void initViewsAndEvents(View rootView) {
        super.initViewsAndEvents(rootView);

        mPresenter = new NewsChannelPresenter(this, mContext);
        mPresenter.loadChannel();

        if (getTopBarViewID() != 0) {
            LinearLayout ll = (LinearLayout) rootView.findViewById(R.id.fragment_tab_content);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, 0, 3);
            mTopBar = LayoutInflater.from(getActivity()).inflate(getTopBarViewID(), null);
            ll.addView(mTopBar, 0, params);
        }

        mTabStrip = (PagerSlidingTabStrip) rootView.findViewById(R.id.tab_strip);
        mTabStrip.setTextColor(Color.rgb(170, 170, 170),
                getResources().getColor(R.color.colorBlueDark));
        mTabStrip.setTextSize(AppUtils.sp2px(getActivity(), 14));
        mViewPager = (ViewPager) rootView.findViewById(R.id.view_pager);

//        mViewPagerAdapter = new TabViewPagerAdapter(getActivity(),
//                getChildFragmentManager(),
//                new ArrayList<Channel>());
//        mViewPager.setAdapter(mViewPagerAdapter);
        mTabStrip.setViewPager(mViewPager);

        mReloadLayout = (RelativeLayout) rootView.findViewById(R.id.layout_reload);
        mReloadLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPresenter.loadChannel();
            }
        });
    }

    protected int getTopBarViewID() {
        return R.layout.title_bar_common;
    }

    @Override
    public void showChannel(Channel channel) {

    }

    @Override
    public void showDetailFragment(Fragment fragment) {

    }

    @Override
    public void showErrorView() {

    }

    @Override
    public void showProgress() {

    }

    @Override
    public void showEmpty() {

    }

    @Override
    public void onClick(View v) {

    }


}
