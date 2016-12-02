package com.anthony.app.module.zhihu;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.Toolbar;
import android.transition.Transition;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.anthony.app.R;
import com.anthony.app.dagger.DaggerActivity;
import com.anthony.library.data.net.HttpSubscriber;
import com.anthony.library.utils.HtmlUtil;
import com.anthony.library.utils.NetworkUtil;
import com.anthony.library.utils.SettingUtil;
import com.bumptech.glide.Glide;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;

import butterknife.BindView;

/**
 * Created by Anthony on 2016/12/2.
 * Class Note:
 *
 */

public class ZhihuDailyDetailActivity extends DaggerActivity {
    public static final String ARG_DAILY_ID = "arg_daily_id";
    private static final String TRANSLATE_VIEW = "translate_view";
    @BindView(R.id.detail_bar_image)
    ImageView detailBarImage;
    @BindView(R.id.detail_bar_copyright)
    TextView detailBarCopyright;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.collapsing_toolbar)
    CollapsingToolbarLayout collapsingToolbar;
    @BindView(R.id.app_bar)
    AppBarLayout appBar;
    @BindView(R.id.wv_detail_content)
    WebView wvDetailContent;
    @BindView(R.id.nsv_scroller)
    NestedScrollView nsvScroller;
    @BindView(R.id.fab)
    FloatingActionButton fab;
    boolean isTransitionEnd = false;
    boolean isImageShow = false;
    String imgUrl;


    @Override
    protected void initViewsAndEvents(Bundle savedInstanceState) {
        setToolBar(toolbar, "");
        WebSettings settings = wvDetailContent.getSettings();
        if (SettingUtil.getNoImageState(this)) {
            settings.setBlockNetworkImage(true);
        }
        if (SettingUtil.getNoImageState(this)) {
            settings.setAppCacheEnabled(true);
            settings.setDomStorageEnabled(true);
            settings.setDatabaseEnabled(true);
            if (NetworkUtil.isNetworkConnected(mContext)) {
                settings.setCacheMode(WebSettings.LOAD_DEFAULT);
            } else {
                settings.setCacheMode(WebSettings.LOAD_CACHE_ONLY);
            }
        }
        settings.setJavaScriptEnabled(true);
        settings.setLoadWithOverviewMode(true);
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        settings.setSupportZoom(true);

        wvDetailContent.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
        nsvScroller.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {

            }
        });
        loadDailyData();

        (getWindow().getSharedElementEnterTransition()).addListener(new Transition.TransitionListener() {
            @Override
            public void onTransitionStart(Transition transition) {
            }

            @Override
            public void onTransitionEnd(Transition transition) {
                /**
                 * 测试发现部分手机(如红米note2)上加载图片会变形,没有达到centerCrop效果
                 * 查阅资料发现Glide配合SharedElementTransition是有坑的,需要在Transition动画结束后再加载图片
                 * https://github.com/TWiStErRob/glide-support/blob/master/src/glide3/java/com/bumptech/glide/supportapp/github/_847_shared_transition/DetailFragment.java
                 */
                isTransitionEnd = true;
                if (imgUrl != null) {
                    isImageShow = true;
                    Glide.with(mContext).load(imgUrl).crossFade().into(detailBarImage);
                }
            }

            @Override
            public void onTransitionCancel(Transition transition) {
            }

            @Override
            public void onTransitionPause(Transition transition) {
            }

            @Override
            public void onTransitionResume(Transition transition) {
            }
        });
    }

    private void loadDailyData() {
        getDataRepository()
                .getZhihuDetail(getIntent().getIntExtra(ARG_DAILY_ID, -1))
                .subscribe(new HttpSubscriber<ZhihuDailyDetailBean>() {
                    @Override
                    public void onNext(ZhihuDailyDetailBean zhihuDetailBean) {
                        onDataReceived(zhihuDetailBean);
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        showToast("加载失败");
                    }
                });
    }

    private void onDataReceived(ZhihuDailyDetailBean info) {
        imgUrl = info.getImage();
        if (!isImageShow && isTransitionEnd) {
            Glide.with(mContext).load(info.getImage()).crossFade().into(detailBarImage);
        }
        collapsingToolbar.setTitle(info.getTitle());
        detailBarCopyright.setText(info.getImage_source());
        String htmlData = HtmlUtil.createHtmlData(info.getBody(), info.getCss(), info.getJs());
        wvDetailContent.loadData(htmlData, HtmlUtil.MIME_TYPE, HtmlUtil.ENCODING);
//        showContent();
    }

    @Override
    protected int getContentViewID() {
        return R.layout.prj_daily_details;
    }

    public static void start(Context context, View view, int id) {
        Intent starter = new Intent(context, ZhihuDailyDetailActivity.class);
        starter.putExtra(ARG_DAILY_ID, id);
        ActivityCompat.startActivity((Activity) context, starter, ActivityOptionsCompat.makeSceneTransitionAnimation((Activity) context, view, TRANSLATE_VIEW).toBundle());
    }
}
