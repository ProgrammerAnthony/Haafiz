package edu.com.app.ui.news.detail;

import android.graphics.Bitmap;
import android.net.http.SslError;
import android.text.TextUtils;
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
import android.widget.TextView;


import java.io.File;

import javax.inject.Inject;

import butterknife.Bind;
import edu.com.app.R;
import edu.com.app.base.AbsSwipeBackActivity;
import edu.com.app.injection.component.ActivityComponent;
import edu.com.app.widget.CircleImageView;
import edu.com.app.widget.dialog.DialogManager;
import edu.com.app.util.ToastUtils;

/**
 * Created by Anthony on 2016/4/26.
 * Class Note:
 */
public class WebViewActivity extends AbsSwipeBackActivity {
    public static String WEB_VIEW_URL = "WebViewUrl";
    public static String WEB_VIEW_TITLE = "WebViewTitle";
    public static int TEXT_SIZE_SMALL = 100;
    public static int TEXT_SIZE_MEDIUM = 125;
    public static int TEXT_SIZE_BIG = 150;
    //    private WebView mWebView;
    private TextView txt_title;
    private ProgressBar progress;
    private TextView title_txt_center;
    private TextView title_txt_right;
    private CircleImageView title_image_left;

    @Bind(R.id.web_view)
    WebView mWebView;

    @Inject
    ToastUtils toastUtils;

    @Override
    protected void initViewsAndEvents() {
//        super.initViewsAndEvents();

/*        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }*/

//        mWebView = (WebView) findViewById(R.id.web_view);

        mWebView.setVisibility(View.INVISIBLE);
//    todo    toggleShowLoading(true, "loading");
        DialogManager.showProgressDialog(mContext,"loading");
        setWebViewOption();
        title_image_left = (CircleImageView) findViewById(R.id.title_image_left);
        title_image_left.setImageResource(R.drawable.ico_back);
        title_image_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mWebView.canGoBack()) {
                    mWebView.goBack();
                } else {
                    scrollToFinishActivity();
                }
            }
        });
        title_txt_right = (TextView) findViewById(R.id.title_txt_right);
        title_txt_right.setVisibility(View.GONE);
        title_txt_center = (TextView) findViewById(R.id.title_txt_center);

        String url = getIntent().getStringExtra(WEB_VIEW_URL);
        String title = getIntent().getStringExtra(WEB_VIEW_TITLE);
        if (TextUtils.isEmpty(title)) {
            title_txt_center.setText("新闻资讯");
        } else {
            title_txt_center.setText(title + "新闻");
        }

        if (url != null) {
            mWebView.loadUrl(url);
        } else {
//            mWebView.loadUrl("http://m.hupu.com/soccer/news/2024852.html");
        }
    }


    @Override
    protected int getContentViewID() {
        return R.layout.activity_web_view;
    }

    @Override
    protected void injectDagger(ActivityComponent activityComponent) {
        activityComponent.inject(this);
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
//            view.loadUrl(url);
//            Intent intent=new Intent(mContext, WebViewActivity.class);
//            startActivity(intent);
            return false;
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

//     todo       toggleShowLoading(false, "");
            DialogManager.hideProgressDialog();
            if (mWebView.getVisibility() == View.INVISIBLE) {
                mWebView.setVisibility(View.VISIBLE);
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
//            title_txt_center.setText(title);
        }

        @Override
        public void onProgressChanged(WebView view, int newProgress) {
//            if (newProgress > 95 && mWebView.getVisibility() == View.INVISIBLE) {
//                mWebView.setVisibility(View.VISIBLE);
//            }
            super.onProgressChanged(view, newProgress);
        }
    }

}
