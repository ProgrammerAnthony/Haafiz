package com.anthony.app.common.widgets.imagebrowse;

import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.anthony.app.R;
import com.anthony.app.common.base.AbsBaseActivity;
import com.anthony.app.common.data.bean.Image;
import com.anthony.app.common.injection.component.ActivityComponent;
import com.anthony.app.common.widgets.CommentView;
import com.anthony.app.common.widgets.imageloader.ImageLoader;
import com.anthony.app.common.widgets.imageloader.ImageLoaderUtil;
import com.bm.library.PhotoView;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;


public class ImageBrowserActivity extends AbsBaseActivity {
    public static String IMAGE_BROWSER_LIST = "ImageBrowserList";
    public static String IMAGE_BROWSER_TITLE = "ImageBrowserTitle";
    public static String IMAGE_BROWSER_INIT_SRC = "ImageBrowserInitSrc";
    public static String IMAGE_BROWSER_URL = "ImageBrowserUrl";
    @BindView(R.id.view_pager)
    ViewPager mViewPager;
    @BindView(R.id.tv_page_index)
    TextView mPageIndex;
    @BindView(R.id.layout_img_top_bar)
    RelativeLayout mTopBar;
    @BindView(R.id.layout_comment)
    CommentView mCommentView;
    @BindView(R.id.tv_img_title)
    TextView mImgTitle;
    @BindView(R.id.tv_img_description)
    TextView mImgDescription;
    @BindView(R.id.layout_description)
    LinearLayout mLayoutDescription;
    @BindView(R.id.tv_page_index_light_off)
    TextView mPageIndexLightOff;
    @BindView(R.id.iv_download)
    ImageView mIvDownload;
    @BindView(R.id.layout_light_off)
    RelativeLayout mLayoutLightOff;

    private List<Image> mList;
    private String mTitle;
    private boolean isLightOff = false;
    private int initIndex = 0;
    private int mCurrentIndex;
    private String mUrl;

    @Inject
    ImageLoaderUtil imageLoaderUtil;

    @Override
    protected int getContentViewID() {
        return R.layout.prj_activity_image_browser;
    }

    @Override
    protected void injectDagger(ActivityComponent activityComponent) {
        activityComponent.inject(this);
    }

    @Override
    protected void initViewsAndEvents() {
//        StatusBarCompat.setStatusBarColor(this, getResources().getColor(R.color.common_tv_dark), 0);
        Intent intent = getIntent();
        mList = intent.getParcelableArrayListExtra(IMAGE_BROWSER_LIST);
        mTitle = intent.getStringExtra(IMAGE_BROWSER_TITLE);
        mUrl = intent.getStringExtra(IMAGE_BROWSER_URL);
        String initSrc = intent.getStringExtra(IMAGE_BROWSER_INIT_SRC);
        if (!TextUtils.isEmpty(initSrc)) {
            for (int i = 0; i < mList.size(); i++) {
                Image img = mList.get(i);
                if (initSrc.equals(img.url)) {
                    initIndex = i;
                    mCurrentIndex = i;
                    break;
                }
            }
        }

        initView();
    }

    private void initView() {
        setAllText(initIndex);

        mCommentView.setUrl(mUrl);
        mCommentView.setTitle(mTitle);
//        mCommentView.mDescription = mItem.getSummary();
//        if (mItem.getImg() != null && mItem.getImg().size() > 0) {
//            mCommentView.mImageUrl = mItem.getImg().get(0).url;
//        }

//        mViewPager = (ViewPager) findViewById(R.id.view_pager);
        mViewPager.setPageMargin((int) (getResources().getDisplayMetrics().density * 15));
        mViewPager.setAdapter(new ImageBrowserAdapter());
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                setAllText(position);
                mCurrentIndex = position;

                mCommentView.setDescription(mList.get(mCurrentIndex).des);
                mCommentView.setImageUrl(mList.get(mCurrentIndex).url);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        mViewPager.setCurrentItem(initIndex, false);
    }


    private class ImageBrowserAdapter extends PagerAdapter {
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            PhotoView view = new PhotoView(ImageBrowserActivity.this);
            view.enable();
            view.setScaleType(ImageView.ScaleType.FIT_CENTER);

            ImageLoader.Builder builder = new ImageLoader.Builder();
            ImageLoader img = builder.url(mList.get(position).url)
                    .imgView(view).strategy(ImageLoaderUtil.LOAD_STRATEGY_ONLY_WIFI).build();
            imageLoaderUtil.loadImage(ImageBrowserActivity.this, img);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    trigger(!isLightOff);
                    isLightOff = !isLightOff;
                }
            });

//            Glide.with(TRSImageBrowserActivity.this)
//                    .load(mList.get(position).url)
//                    .crossFade()
//                    .into(view);
            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public int getCount() {
            return mList.size();
        }
    }

    private void setAllText(int position) {
        String title = mList.get(position).title;
        title = title.trim();
        if (!TextUtils.isEmpty(title)) {
            mImgTitle.setText(title);
        } else {
            mImgTitle.setText(mTitle);
        }
        mImgDescription.setText(mList.get(position).des);
        mPageIndex.setText((position + 1) + "/" + mList.size());
        mPageIndexLightOff.setText((position + 1) + "/" + mList.size());
    }

    private void trigger(boolean off) {
        if (off) {
            mLayoutDescription.setVisibility(View.INVISIBLE);
            AlphaAnimation alphaAnimation1 = new AlphaAnimation(1f, 0f);
            alphaAnimation1.setDuration(600);
            mLayoutDescription.startAnimation(alphaAnimation1);

            mLayoutLightOff.setVisibility(View.VISIBLE);
            AlphaAnimation alphaAnimation2 = new AlphaAnimation(0f, 1f);
            alphaAnimation2.setDuration(600);
            mLayoutLightOff.startAnimation(alphaAnimation2);

            mTopBar.setVisibility(View.INVISIBLE);
            TranslateAnimation translateAnimation1 = new TranslateAnimation(
                    Animation.RELATIVE_TO_SELF, 0f,
                    Animation.RELATIVE_TO_SELF, 0f,
                    Animation.RELATIVE_TO_SELF, 0f,
                    Animation.RELATIVE_TO_SELF, -1f);
            translateAnimation1.setDuration(200);
            translateAnimation1.setInterpolator(new AccelerateDecelerateInterpolator());
            mTopBar.startAnimation(translateAnimation1);

            mCommentView.setVisibility(View.INVISIBLE);
            TranslateAnimation translateAnimation2 = new TranslateAnimation(
                    Animation.RELATIVE_TO_SELF, 0f,
                    Animation.RELATIVE_TO_SELF, 0f,
                    Animation.RELATIVE_TO_SELF, 0f,
                    Animation.RELATIVE_TO_SELF, 1f);
            translateAnimation2.setDuration(200);
            translateAnimation2.setInterpolator(new AccelerateDecelerateInterpolator());
            mCommentView.startAnimation(translateAnimation2);
        } else {
            mLayoutDescription.setVisibility(View.VISIBLE);
            AlphaAnimation alphaAnimation1 = new AlphaAnimation(0f, 1f);
            alphaAnimation1.setDuration(600);
            mLayoutDescription.startAnimation(alphaAnimation1);

            mLayoutLightOff.setVisibility(View.INVISIBLE);
            AlphaAnimation alphaAnimation2 = new AlphaAnimation(1f, 0f);
            alphaAnimation2.setDuration(600);
            mLayoutLightOff.startAnimation(alphaAnimation2);

            mTopBar.setVisibility(View.VISIBLE);
            TranslateAnimation translateAnimation1 = new TranslateAnimation(
                    Animation.RELATIVE_TO_SELF, 0f,
                    Animation.RELATIVE_TO_SELF, 0f,
                    Animation.RELATIVE_TO_SELF, -1f,
                    Animation.RELATIVE_TO_SELF, 0f);
            translateAnimation1.setDuration(200);
            translateAnimation1.setInterpolator(new AccelerateDecelerateInterpolator());
            mTopBar.startAnimation(translateAnimation1);

            mCommentView.setVisibility(View.VISIBLE);
            TranslateAnimation translateAnimation2 = new TranslateAnimation(
                    Animation.RELATIVE_TO_SELF, 0f,
                    Animation.RELATIVE_TO_SELF, 0f,
                    Animation.RELATIVE_TO_SELF, 1f,
                    Animation.RELATIVE_TO_SELF, 0f);
            translateAnimation2.setDuration(200);
            translateAnimation2.setInterpolator(new AccelerateDecelerateInterpolator());
            mCommentView.startAnimation(translateAnimation2);
        }
    }

}
