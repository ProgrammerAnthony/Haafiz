package edu.com.mvpcommon.news.newsList;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Bundle;
import android.view.View;
import android.webkit.JsResult;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;


import java.io.File;

import edu.com.mvpcommon.R;
import edu.com.mvplibrary.ui.fragment.AbsBaseFragment;
import edu.com.mvplibrary.ui.widget.CircleImageView;
import edu.com.mvplibrary.util.ToastUtils;

/**
 * Created by Anthony on 2016/4/25.
 * Class Note:
 */
public class WebViewFragment extends AbsBaseFragment {
    public static String WEB_VIEW_URL = "WebViewUrl";
    public static int TEXT_SIZE_SMALL = 100;
    public static int TEXT_SIZE_MEDIUM = 125;
    public static int TEXT_SIZE_BIG = 150;
    private WebView mWebView;
    private String mUrl;
    private RelativeLayout mReloadLayout;
    private TextView txt_title;
    private ProgressBar progress;
    private TextView title_txt_center;
    private TextView title_txt_right;
    private CircleImageView title_image_left;
    public static String WEB_VIEW_TITLE="WebViewTitle";
    private String mTitle;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mTitle = getArguments().getString(WEB_VIEW_TITLE);
        }
    }

    @Override
    protected void initViewsAndEvents(View rootView) {
        mWebView = (WebView) rootView.findViewById(R.id.web_view);
        mWebView.setVisibility(View.INVISIBLE);
//        mUrl = getIntent().getStringExtra(WEB_VIEW_URL);

//        mReloadLayout = (RelativeLayout)  rootView.findViewById(R.id.layout_reload);
//        mReloadLayout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mWebView.reload();
//                mReloadLayout.setVisibility(View.INVISIBLE);
//            }
//        });

//        title_txt_center = (TextView)  rootView.findViewById(R.id.title_txt_center);
//        title_image_left=(CircleImageView)rootView.findViewById(R.id.title_image_left);
//        title_txt_right=(TextView)rootView.findViewById(R.id.title_txt_right);
//        title_txt_right.setVisibility(View.GONE);
//        title_image_left.setImageResource(R.drawable.icon_head);

//        progress = (ProgressBar)  rootView.findViewById(R.id.progress);

        setWebViewOption();

        if (getFragmentUrl() != null) {
            mWebView.loadUrl(getFragmentUrl());
        } else {
//            mWebView.loadUrl("http://m.hupu.com/soccer/england/news");
        }


    }

    @Override
    protected int getContentViewId() {
        return R.layout.fragment_web_view;
    }
    private void setWebViewOption() {
        //设置编码
        mWebView.getSettings().setDefaultTextEncodingName("UTF-8");

        //设置缓存
        mWebView.getSettings().setAppCacheEnabled(true);
        File cacheFile = mContext.getCacheDir();
        if (cacheFile != null) {
            mWebView.getSettings().setAppCachePath(cacheFile.getAbsolutePath());
        }
        /**
         * 设置缓存加载模式
         * LOAD_DEFAULT(默认值)：如果缓存可用且没有过期就使用，否则从网络加载
         * LOAD_NO_CACHE：从网络加载
         * LOAD_CACHE_ELSE_NETWORK：缓存可用就加载即使已过期，否则从网络加载
         * LOAD_CACHE_ONLY：不使用网络，只加载缓存即使缓存不可用也不去网络加载
         */
        mWebView.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);

        //设置是否支持运行JavaScript，仅在需要时打开
        mWebView.getSettings().setJavaScriptEnabled(true);

        //设置WebView视图大小与HTML中viewport Tag的关系
        mWebView.getSettings().setUseWideViewPort(true);
        mWebView.getSettings().setLoadWithOverviewMode(true);

        //设置字体大小
        mWebView.getSettings().setTextZoom(TEXT_SIZE_SMALL);

        //设置支持缩放
        mWebView.getSettings().setBuiltInZoomControls(true);
        mWebView.getSettings().setSupportZoom(true);

        //设置WebViewClient
        mWebView.setWebViewClient(new MyWebViewClient());
        mWebView.setWebChromeClient(new MyWebChromeClient());
    }

    private class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            //重写此方法表明点击webview里面新的链接是由当前webview处理（false），还是自定义处理（true）
//            Intent intent=new Intent(mContext, WebViewActivity.class);
//            intent.putExtra(WebViewActivity.WEB_VIEW_URL,url);
//            intent.putExtra(WebViewActivity.WEB_VIEW_TITLE,mTitle);
//            startActivity(intent);
            ToastUtils.getInstance().showToast("url clicked");
            return true;
        }

        @Override
        public void onLoadResource(WebView view, String url) {
            super.onLoadResource(view, url);
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
//            injectJS();
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
        }

        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
            super.onReceivedError(view, request, error);
        }

        @Override
        public void onReceivedHttpError(WebView view, WebResourceRequest request, WebResourceResponse errorResponse) {
            super.onReceivedHttpError(view, request, errorResponse);
        }

        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            super.onReceivedSslError(view, handler, error);
        }
    }


    private class MyWebChromeClient extends WebChromeClient {
        @Override
        public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
            ToastUtils.getInstance().showToast(message);
            result.confirm();
            return true;
        }

        @Override
        public void onReceivedTitle(WebView view, String title) {
            super.onReceivedTitle(view, title);
        }

        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            if(newProgress>25){
                injectJS();
            }
            if(newProgress>95&&mWebView.getVisibility()==View.INVISIBLE){
                mWebView.setVisibility(View.VISIBLE);
            }
            super.onProgressChanged(view, newProgress);
        }
    }



    private void injectJS(){
        mWebView.loadUrl("javascript:(function() " +
                "{ " +
                "document.getElementsByClassName('m-top-bar')[0].style.display='none'; " +
                "document.getElementsByClassName('m-footer')[0].style.display='none';" +
                "document.getElementsByClassName('m-page')[0].style.display='none';" +
                "})()");
//        mWebView.loadUrl("javascript:(function() { document.getElementsByClassName('m-top-bar')[0].style.display='none'; })()");
    }

}
