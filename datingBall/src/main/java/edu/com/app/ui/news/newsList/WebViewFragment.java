package edu.com.app.ui.news.newsList;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.http.SslError;
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

import java.io.File;

import javax.inject.Inject;

import butterknife.Bind;
import edu.com.app.R;
import edu.com.app.ui.news.detail.WebViewActivity;
import edu.com.app.base.AbsTitleFragment;
import edu.com.app.util.ToastUtils;

/**
 * Created by Anthony on 2016/4/25.
 * Class Note:
 * webview fragment with js
 */
public class WebViewFragment extends AbsTitleFragment {

    public static int TEXT_SIZE_SMALL = 100;
    public static int TEXT_SIZE_MEDIUM = 125;
    public static int TEXT_SIZE_BIG = 150;
    public static final String mTitle = "WEB_VIEW_TITLE";
    @Bind(R.id.web_view)
    WebView mWebView;

    @Inject
    ToastUtils toastUtils;
//    @Override
//    protected View getLoadingTargetView() {
//        return mWebView;
//    }

    @Override
    protected int getCenterViewID() {
        return R.layout.fragment_web_view;
    }

    /**
     * no top bar view for  this fragment
     */
    @Override
    protected int getTopBarViewID() {
        return 0;
    }

    @Override
    protected void initDagger() {

    }

    @Override
    protected void initViewsAndEvents(View rootView) {
        mWebView.setVisibility(View.INVISIBLE);
//        toggleShowLoading(true, "loading");
//        DialogManager.showProgressDialog(mContext,"loading");
        setWebViewOption(mWebView);
        if (getFragmentUrl() != null) {
            mWebView.loadUrl(getFragmentUrl());
        }
    }

    private void setWebViewOption(WebView webview) {
        //设置编码
        webview.getSettings().setDefaultTextEncodingName("UTF-8");

        //设置缓存
        webview.getSettings().setAppCacheEnabled(true);
        File cacheFile = mContext.getCacheDir();
        if (cacheFile != null) {
            webview.getSettings().setAppCachePath(cacheFile.getAbsolutePath());
        }
        /**
         * 设置缓存加载模式
         * LOAD_DEFAULT(默认值)：如果缓存可用且没有过期就使用，否则从网络加载
         * LOAD_NO_CACHE：从网络加载
         * LOAD_CACHE_ELSE_NETWORK：缓存可用就加载即使已过期，否则从网络加载
         * LOAD_CACHE_ONLY：不使用网络，只加载缓存即使缓存不可用也不去网络加载
         */
        webview.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);

        //设置是否支持运行JavaScript，仅在需要时打开
        webview.getSettings().setJavaScriptEnabled(true);

        //设置WebView视图大小与HTML中viewport Tag的关系
        webview.getSettings().setUseWideViewPort(true);
        webview.getSettings().setLoadWithOverviewMode(true);

        //设置字体大小
        webview.getSettings().setTextZoom(TEXT_SIZE_SMALL);

        //设置支持缩放
        webview.getSettings().setBuiltInZoomControls(true);
        webview.getSettings().setSupportZoom(true);

        //设置WebViewClient
        webview.setWebViewClient(new MyWebViewClient());
        webview.setWebChromeClient(new MyWebChromeClient());
    }

    private class MyWebViewClient extends WebViewClient {
        /**
         * 当前webview将要加载新的url的时候调用。
         * <p/>
         * 如果没有提供WebViewClient，将由Activity Manager选择合适的方式处理url
         * 如果提供了WebViewClient，返回true代表应用处理url，返回false当前webview处理url
         * <p/>
         * 重写此方法表明点击webview里面新的链接是由当前webview处理（false），还是自定义处理（true）
         */
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
//            view.loadUrl(url);
            Intent intent = new Intent(mContext, WebViewActivity.class);
            intent.putExtra(WebViewActivity.WEB_VIEW_URL, url);
            startActivity(intent);
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
//            injectJS();

//            DialogManager.hideProgressDialog();
            if (view.getVisibility() == View.INVISIBLE) {
                view.setVisibility(View.VISIBLE);

            }

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
            toastUtils.showToast(message);
            result.confirm();
            return true;
        }

        @Override
        public void onReceivedTitle(WebView view, String title) {
            super.onReceivedTitle(view, title);
        }

        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            if (newProgress > 25) {
                injectJS(view);
            }

            super.onProgressChanged(view, newProgress);
        }
    }


    private void injectJS(WebView webview) {
        webview.loadUrl("javascript:(function() " +
                "{ " +
                "document.getElementsByClassName('m-top-bar')[0].style.display='none'; " +
                "document.getElementsByClassName('m-footer')[0].style.display='none';" +
                "document.getElementsByClassName('m-page')[0].style.display='none';" +
                "})()");
    }
}
