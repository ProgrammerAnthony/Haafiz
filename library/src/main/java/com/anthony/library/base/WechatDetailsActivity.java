package com.anthony.library.base;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.anthony.library.R;
import com.anthony.library.utils.AppUtils;
import com.anthony.library.utils.SettingUtil;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Anthony on 2016/12/2.
 * Class Note:
 * webview detail activity,
 */
public class WechatDetailsActivity extends AbsBaseActivity {
    //    @BindView(R.id.toolbar)
    Toolbar toolBar;
    //    @BindView(R.id.wv_tech_content)
    WebView wvTechContent;


    @Override
    protected void initViewsAndEvents(Bundle savedInstanceState) {
        toolBar = (Toolbar) findViewById(R.id.toolbar);
        wvTechContent = (WebView) findViewById(R.id.wv_tech_content);

        //show loading but not include toolbar
        List<Integer> skipIds = new ArrayList<>();
        skipIds.add(R.id.toolbar);
        showLoading(skipIds);

        Intent intent = getIntent();
        setToolBar(toolBar, intent.getExtras().getString("title"));

        WebSettings settings = wvTechContent.getSettings();
        if (SettingUtil.getNoImageState(this)) {
            settings.setBlockNetworkImage(true);
        }
        if (SettingUtil.getNoImageState(this)) {
            settings.setAppCacheEnabled(true);
            settings.setDomStorageEnabled(true);
            settings.setDatabaseEnabled(true);
            if (AppUtils.isNetworkConnected(mContext)) {
                settings.setCacheMode(WebSettings.LOAD_DEFAULT);
            } else {
                settings.setCacheMode(WebSettings.LOAD_CACHE_ONLY);
            }
        }
        wvTechContent.setBackgroundColor(0x00000000);
        settings.setJavaScriptEnabled(true);
        settings.setLoadWithOverviewMode(true);
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        settings.setSupportZoom(true);
        wvTechContent.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageFinished(WebView webView, String s) {
                super.onPageFinished(webView, s);
                showContent();
            }
        });
        //可以操作progress
        wvTechContent.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                if (newProgress == 100) {

                } else {

                }

            }

            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                setTitle(title);
            }
        });
        wvTechContent.loadUrl(intent.getExtras().getString("url"));
    }

    public static void start(Context context, String title, String url) {
        Intent intent = new Intent();
        intent.setClass(context, WechatDetailsActivity.class);
        intent.putExtra("title", title);
        intent.putExtra("url", url);
        context.startActivity(intent);
    }


    @Override
    protected int getContentViewID() {
        return R.layout.lib_activity_webview_details;
    }
}
