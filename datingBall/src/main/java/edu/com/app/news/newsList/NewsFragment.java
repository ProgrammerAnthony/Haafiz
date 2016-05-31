package edu.com.app.news.newsList;

import android.graphics.Color;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.astuetz.PagerSlidingTabStrip;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;
import edu.com.app.R;

import edu.com.base.model.bean.Channel;

import edu.com.base.ui.fragment.AbsBaseFragment;
import edu.com.base.ui.fragment.AbsTitleFragment;
import edu.com.base.util.AppUtils;
import edu.com.app.main.MainActivity;

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

    protected static String TAG = "NewsFragment";
    private static int INIT_INDEX = 0;

    private View mTopBar;


    private NewsPresenter mPresenter;
    private TabViewPagerAdapter mViewPagerAdapter;


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



}
