package edu.com.mvpcommon.news.newsList;

import android.graphics.Color;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.astuetz.PagerSlidingTabStrip;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.OnClick;
import edu.com.mvpcommon.main.DrawerMainActivity;
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
 * {@link NewsContract}----------Manager role of MVP
 * {@link NewsPresenter}---------Presenter
 * &{@link NewsFragment}---------View
 * &{@link  NewsData}------------Model
 */
public class NewsFragment extends AbsBaseFragment implements NewsContract.View {

    @Bind(R.id.fragment_tab_content)
    LinearLayout mFragmentContent;
    @Bind(R.id.view_pager)
    ViewPager mViewPager;
    @Bind(R.id.tab_strip)
    PagerSlidingTabStrip mTabStrip;
    @Bind(R.id.title_txt_center)
    TextView mTitle;
    @Bind(R.id.title_image_left)
    ImageView mIcon;
    @Bind(R.id.title_txt_right)
    TextView mTitleRight;

    @OnClick(R.id.title_image_left)
    public void openDrawer(){
        if (mContext instanceof DrawerMainActivity) {
            DrawerMainActivity.openDrawer();
        }
    }

    protected static String TAG = "NewsFragment";
    private static int INIT_INDEX = 0;

    private View mTopBar;


    private NewsPresenter mPresenter;
    private TabViewPagerAdapter mViewPagerAdapter;


    @Override
    protected void initViewsAndEvents(View rootView) {
//        super.initViewsAndEvents(rootView);

        mTitle.setText("新闻资讯");
        mTitleRight.setVisibility(View.GONE);
        mIcon.setImageResource(R.drawable.icon_head);


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
    public void onResume() {
        super.onResume();
        mPresenter.start();
    }

    protected void loadData() {
        if (mPresenter == null)
            mPresenter = new NewsPresenter(this, mContext);
        mPresenter.getData("raw://news_channels");

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
//    public void setPresenter(NewsContract.Presenter presenter) {
//        mPresenter = (NewsPresenter) presenter;
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
