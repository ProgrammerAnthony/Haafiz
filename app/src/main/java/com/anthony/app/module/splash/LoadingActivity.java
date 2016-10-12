package com.anthony.app.module.splash;

import android.os.AsyncTask;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.anthony.app.R;
import com.anthony.app.common.base.AbsBaseActivity;
import com.anthony.app.common.base.MyApplication;
import com.anthony.app.common.data.DataManager;
import com.anthony.app.common.injection.component.ActivityComponent;
import com.anthony.app.common.utils.ToastUtils;
import com.anthony.app.common.widgets.CircleProgressBar;
import com.anthony.app.common.widgets.ViewDisplay;
import com.viewpagerindicator.CirclePageIndicator;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Anthony on 2016/9/5.
 * Class Note:
 * loading introduction view when firstly open this project
 * or loading splash view when open this project but not the first time
 * todo add Ads
 * 第一次打开app时加载引导页
 * 后面打开app时加载闪屏页
 * todo 加入广告页
 */
public class LoadingActivity extends AbsBaseActivity {

    @BindView(R.id.view_pager)
    ViewPager mViewPager;
    @BindView(R.id.pager_indicator)
    CirclePageIndicator mPagerIndicator;
    @BindView(R.id.btn_enter_home)
    Button mBtnEnterHome;
    @BindView(R.id.layout_intro)
    RelativeLayout mLayoutIntro;
    @BindView(R.id.pb_splash)
    CircleProgressBar mProgressBar;
    @BindView(R.id.layout_splash)
    RelativeLayout mLayoutSplash;

    @Inject
    ToastUtils mToastUtil;
    @Inject
    ViewDisplay mViewDisplay;
    @Inject
    DataManager mDataManager;
    @Inject
    MyApplication mApplication;
    @Inject
    ToastUtils toastUtils;

    int[] loadingPics = new int[]{R.mipmap.loading_bg1, R.mipmap.loading_bg2, R.mipmap.loading_bg3};

    private ArrayList<View> mPageView;
    private AsyncTask<String, String, String> showMainTask;
    private static final short SPLASH_SHOW_SECONDS = 2;
    private long mShowMainTime;

    @Override
    protected int getContentViewID() {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        return R.layout.prj_activity_loading;
    }

    @Override
    protected void injectDagger(ActivityComponent activityComponent) {
        activityComponent.inject(this);
    }

    @Override
    protected void initViewsAndEvents() {
        loadData();
        if (whetherFirstTimeEnterApp())
            initIntroView();
        else
            initSplashView();

    }


    /**
     * @return true ,first time entering . false, not first time
     */
    public boolean whetherFirstTimeEnterApp() {
        if (mDataManager.getPreferencesHelper().isFirstTime()) {
            mDataManager.getPreferencesHelper().saveFirstTime(false);
            return true;
        } else {
            return false;
        }
    }

    /**
     * pre load data in loading view
     * todo
     */
    private void loadData() {

    }


    private void initIntroView() {
        mLayoutIntro.setVisibility(View.VISIBLE);
        mLayoutSplash.setVisibility(View.INVISIBLE);
        mPageView = new ArrayList<>();
        for (int i = 0; i < loadingPics.length; i++) {
            //初始化三张图片
            ImageView imageView = new ImageView(mContext);
            imageView.setImageResource(loadingPics[i]);
            mPageView.add(imageView);
        }

        mViewPager.setAdapter(new LoadingIvAdapter());
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == mPageView.size() - 1) {
                    mBtnEnterHome.setVisibility(View.VISIBLE);
                } else {
                    mBtnEnterHome.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        mPagerIndicator.setViewPager(mViewPager);
        mBtnEnterHome.setVisibility(View.INVISIBLE);
    }

    @OnClick({R.id.btn_enter_home, R.id.pb_splash})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_enter_home:
                mViewDisplay.showActivity(this, "1000");
                this.finish();
                break;
            case R.id.pb_splash:
                mProgressBar.stop();
                if (!showMainTask.isCancelled()) {
                    showMainTask.cancel(true);
                }
                showMain();
                finish();
                break;

        }

    }


    public class LoadingIvAdapter extends PagerAdapter {
        @Override
        public int getCount() {
            return mPageView.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(mPageView.get(position));
            return mPageView.get(position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(mPageView.get(position));
        }
    }

    private void initSplashView() {
        mLayoutIntro.setVisibility(View.INVISIBLE);
        mLayoutSplash.setVisibility(View.VISIBLE);
        mShowMainTime = System.currentTimeMillis() + SPLASH_SHOW_SECONDS * 1000;
        showMainTask = new AsyncTask<String, String, String>() {
            @Override
            protected String doInBackground(String[] params) {
                if (System.currentTimeMillis() < mShowMainTime) {
                    try {
                        long sleepTime = mShowMainTime - System.currentTimeMillis();
                        if (sleepTime > 0) {
                            Thread.sleep(mShowMainTime - System.currentTimeMillis());
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                return null;
            }

            @Override
            protected void onPostExecute(String o) {
                showMain();
                finish();
            }
        };

        showMainTask.execute();
    }

    protected void showMain() {
        mViewDisplay.showActivity(this, "1000");
    }
}
