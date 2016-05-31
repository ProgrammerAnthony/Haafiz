package edu.com.mvpCommon.news.newsList;

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
import edu.com.mvpCommon.R;
import edu.com.mvpCommon.main.MainActivity;

import edu.com.base.model.bean.Channel;

import edu.com.base.ui.fragment.AbsTitleFragment;
import edu.com.base.util.AppUtils;

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
public class NewsFragment extends AbsTitleFragment implements NewsContract.View {

//    @Bind(R.id.fragment_tab_content)
//    LinearLayout mFragmentContent;
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
        if (mContext instanceof MainActivity) {
            MainActivity.openDrawer();
        }
    }

    protected static String TAG = "NewsFragment";
    private static int INIT_INDEX = 0;

    private View mTopBar;


    private NewsPresenter mPresenter;
    private TabViewPagerAdapter mViewPagerAdapter;


    @Override
    protected void initViewsAndEvents(View rootView) {
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
    protected int getCenterViewID() {
        return R.layout.fragment_tab;
    }

    @Override
    protected int getTopBarViewID() {
        return R.layout.title_bar_common;
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


    @Override
    public void showMessage(String msg) {
        showMessageDialog(msg);
    }

    @Override
    public void close() {
//        finish();
    }

    @Override
    public void showProgress(String message) {
        showProgressDialog(message);
    }

    @Override
    public void showProgress(String message, int progress) {
        showProgressDialog(message, progress);
    }

    @Override
    public void hideProgress() {
        hideProgressDialog();
    }

    @Override
    public void showErrorMessage(String msg, String content) {
        showErrorDialog(msg, content, new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                sweetAlertDialog.dismissWithAnimation();
            }
        });
    }


}
