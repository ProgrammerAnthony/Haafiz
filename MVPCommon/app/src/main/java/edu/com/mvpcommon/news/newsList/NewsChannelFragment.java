package edu.com.mvpcommon.news.newsList;

import android.graphics.Color;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.astuetz.PagerSlidingTabStrip;

import java.util.ArrayList;

import butterknife.Bind;
import edu.com.mvplibrary.R;
import edu.com.mvplibrary.model.Channel;
import edu.com.mvplibrary.model.News;
import edu.com.mvplibrary.ui.fragment.AbsBaseFragment;
import edu.com.mvplibrary.util.AppUtils;

/**
 * Created by Anthony on 2016/5/3.
 * Class Note:
 * 1 use {@link PagerSlidingTabStrip} and
 * {@link ViewPager} to create tab fragment
 * TabViewPagerAdapter to return a FragmentStatePagerAdapter{@link FragmentStatePagerAdapter}
 * 2 View in MVP
 * {@link NewsListContract}----------Manager role of MVP
 * {@link NewsListPresenter}---------Presenter
 * &{@link NewsChannelFragment}---------View
 * &{@link  NewsListData}------------Model
 */
public class NewsChannelFragment extends AbsBaseFragment implements NewsListContract.View {

    @Bind(R.id.fragment_tab_content)
    LinearLayout mFragmentContent;

    protected static String TAG = "com.trs.fragment.TRSAbsTabFragment";
    private static int INIT_INDEX = 0;
    private PagerSlidingTabStrip mTabStrip;
    private ViewPager mViewPager;

    private View mTopBar;


    private NewsListPresenter mPresenter;
    private TabViewPagerAdapter mViewPagerAdapter;

    @Override
    protected int getContentViewID() {
        return R.layout.abs_fragment_tab;
    }


    @Override
    protected void initViewsAndEvents(View rootView) {
        super.initViewsAndEvents(rootView);

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

        mViewPagerAdapter = new TabViewPagerAdapter(getActivity(),
                getChildFragmentManager(),
                new ArrayList<Channel>());
        mViewPager.setAdapter(mViewPagerAdapter);
        mTabStrip.setViewPager(mViewPager);


        loadData();

    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.start();
    }

    protected void loadData() {
        if (mPresenter == null)
            mPresenter = new NewsListPresenter(this, mContext);
        mPresenter.getData();

    }

    protected int getTopBarViewID() {
        return R.layout.title_bar_common;
    }

    @Override
    public void onDataReceived(ArrayList<Channel> channels) {
        mViewPagerAdapter.addAll(channels);
        mViewPagerAdapter.notifyDataSetChanged();
        mTabStrip.notifyDataSetChanged();
    }

    @Override
    public void loadMore(ArrayList<News> news) {

    }

    @Override
    public void refresh(ArrayList<News> news) {

    }

    @Override
    protected View getLoadingTargetView() {
        return mFragmentContent;
    }

//    @Override
//    public void setPresenter(NewsListContract.Presenter presenter) {
//        mPresenter = (NewsListPresenter) presenter;
//    }

    @Override
    public void showLoading(String msg) {
        toggleShowLoading(true, msg);
    }

    @Override
    public void hideLoading() {
        toggleShowLoading(false, "");
    }

    @Override
    public void showError(String msg, View.OnClickListener onClickListener) {
        toggleShowError(true, msg, onClickListener);
    }

    @Override
    public void showEmpty(String msg, View.OnClickListener onClickListener) {
        toggleShowEmpty(true, msg, onClickListener);
    }

    @Override
    public void showEmpty(String msg, View.OnClickListener onClickListener, int imageId) {
        toggleShowEmpty(true, msg, onClickListener, imageId);
    }

    @Override
    public void showNetError(View.OnClickListener onClickListener) {
        toggleNetworkError(true, onClickListener);
    }
}
