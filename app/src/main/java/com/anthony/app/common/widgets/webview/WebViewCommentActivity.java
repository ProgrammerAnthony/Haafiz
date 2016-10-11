package com.anthony.app.common.widgets.webview;

import android.graphics.Bitmap;
import android.net.http.SslError;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
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

import com.anthony.app.R;
import com.anthony.app.common.base.AbsBaseActivity;
import com.anthony.app.common.base.Constants;
import com.anthony.app.common.base.MyApplication;
import com.anthony.app.common.data.bean.NewsItem;
import com.anthony.app.common.data.bean.OfflineResource;
import com.anthony.app.common.data.database.dao.OfflineResourceDao;
import com.anthony.app.common.injection.component.ActivityComponent;
import com.anthony.app.common.utils.AppUtils;
import com.anthony.app.common.utils.FileUtil;
import com.anthony.app.common.utils.SettingUtil;
import com.anthony.app.common.utils.ToastUtils;
import com.anthony.app.common.widgets.CommentView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Anthony on 2016/9/5.
 * Class Note:
 * a webview Activity with comment layout below
 */
public class WebViewCommentActivity extends AbsBaseActivity {

    @BindView(R.id.txt_title)
    TextView txt_title;
    @BindView(R.id.layout_comments)
    CommentView mCommentView;
    @BindView(R.id.web_view)
    WebView mWebView;
    @BindView(R.id.progress)
    ProgressBar progress;
    @BindView(R.id.prj_layout_reload)
    RelativeLayout mReloadLayout;
    @Inject
    MyApplication mApplication;

    @Inject
    ToastUtils toastUtils;

    public static String WEB_VIEW_ITEM = "WebViewItem";
    public static int TEXT_SIZE_SMALL = 75;
    public static int TEXT_SIZE_MEDIUM = 100;
    public static int TEXT_SIZE_BIG = 125;
    private NewsItem mItem;
    private OfflineResourceDao dao;

    @Override
    protected int getContentViewID() {
        return R.layout.prj_activity_web_view;
    }

    @Override
    protected void injectDagger(ActivityComponent activityComponent) {
        activityComponent.inject(this);
    }

    @Override
    protected void initViewsAndEvents() {
        dao = new OfflineResourceDao(mApplication);
        mItem = (NewsItem) getIntent().getSerializableExtra(WEB_VIEW_ITEM);

//        StatusBarCompat.setStatusBarColor(this, getResources().getColor(R.color.app_primary), 0);

        mReloadLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        mCommentView.setUrl(mItem.getUrl());
        mCommentView.setTitle(mItem.getTitle());
        mCommentView.setDescription(mItem.getSummary());
        if (mItem.getImgs() != null && mItem.getImgs().size() > 0) {
            mCommentView.setImageUrl(mItem.getImgs().get(0));
        }

        setWebViewOption();

        mWebView.loadUrl(mItem.getUrl());

    }

    private void setWebViewOption() {
        //设置编码
        mWebView.getSettings().setDefaultTextEncodingName("UTF-8");

        //设置缓存
        mWebView.getSettings().setDomStorageEnabled(true); //开启DOM storage API 功能
        mWebView.getSettings().setDatabaseEnabled(true); //开启database storage API 功能
        mWebView.getSettings().setAppCacheEnabled(true);
        File cacheFile = this.getApplicationContext().getCacheDir();
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
        int type = AppUtils.getNetWorkType(this);
        switch (type) {
            case AppUtils.NETWORKTYPE_3G:
            case AppUtils.NETWORKTYPE_WIFI:
                mWebView.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
                break;
            default:
                mWebView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
                break;
        }

        //设置是否支持运行JavaScript，仅在需要时打开
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.addJavascriptInterface(new JSObject(this), "JSObject");

        //设置WebView视图大小与HTML中viewport Tag的关系
        mWebView.getSettings().setUseWideViewPort(true);
        mWebView.getSettings().setLoadWithOverviewMode(true);

        //设置字体大小
        mWebView.getSettings().setTextZoom(getFontSize(SettingUtil.getCurrentFontSize(this)));

        //设置支持缩放
        mWebView.getSettings().setBuiltInZoomControls(true);
        mWebView.getSettings().setSupportZoom(true);

        //设置WebViewClient
        mWebView.setWebViewClient(new MyWebViewClient());
        mWebView.setWebChromeClient(new MyWebChromeClient());
    }



    @OnClick(R.id.prj_layout_reload)
    public void onClick() {
        mWebView.reload();
        mReloadLayout.setVisibility(View.INVISIBLE);
    }


    private class MyWebViewClient extends WebViewClient {
        @Override
        public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
            String suffix = getSuffix(url);
            if (TextUtils.isEmpty(suffix)) {
                return null;
            } else {
                String type = getMimeType(suffix);
                OfflineResource res = dao.queryResourceByUrl(url);
                if (res == null || TextUtils.isEmpty(type)) {
                    return null;
                }

                InputStream in = null;
                try {
                    in = new FileInputStream(res.res_path);
                    return new WebResourceResponse(type, "UTF-8", in);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    Log.v("Offline", "Offline Resource Cache File Not Found!");
                    return null;
                }
            }
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            //重写此方法表明点击网页里面的链接还是在当前的WebView里跳转，不跳到浏览器那边
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onLoadResource(WebView view, String url) {
            super.onLoadResource(view, url);
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);

            try {
                InputStream is = FileUtil.getStream(WebViewCommentActivity.this, "raw://inithtml");
                String js = FileUtil.readStreamString(is, "UTF-8");
                mWebView.loadUrl("javascript:" + js);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
//            mReloadLayout.setVisibility(View.VISIBLE);
            super.onReceivedError(view, request, error);
        }

        @Override
        public void onReceivedHttpError(WebView view, WebResourceRequest request, WebResourceResponse errorResponse) {
//            mReloadLayout.setVisibility(View.VISIBLE);
            super.onReceivedHttpError(view, request, errorResponse);
        }

        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
//            mReloadLayout.setVisibility(View.VISIBLE);
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
            txt_title.setText(title);
        }

        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            if (newProgress < 100) {
                progress.setVisibility(View.VISIBLE);
                progress.setProgress(newProgress);
            } else {
                progress.setProgress(newProgress);
                progress.setVisibility(View.GONE);
            }
            super.onProgressChanged(view, newProgress);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && mWebView.canGoBack()) {
            mWebView.goBack(); // goBack()表示返回WebView的上一页面
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void onBtnBackClick(View v) {
        if (mWebView.canGoBack()) {
            mWebView.goBack();
        } else {
            finish();
        }
    }

    private int getFontSize(int size) {
        switch (size) {
            case Constants.CURRENT_FONT_SIZE_SMALL:
                return TEXT_SIZE_SMALL;
            case Constants.CURRENT_FONT_SIZE_MEDIUM:
                return TEXT_SIZE_MEDIUM;
            case Constants.CURRENT_FONT_SIZE_LARGE:
                return TEXT_SIZE_BIG;
        }

        return TEXT_SIZE_SMALL;
    }

    private String getSuffix(String url) {
        return url.substring(url.lastIndexOf(".") + 1);
    }

    private String getMimeType(String suffix) {
        if (suffix.equals("png"))
            return "image/png";
        else if (suffix.equals("jpeg") || suffix.equals("jpg") || suffix.equals("jpe"))
            return "image/jpeg";
        else if (suffix.equals("gif"))
            return "image/gif";
        else if (suffix.equals("css"))
            return "text/css";
        else if (suffix.equals("js"))
            return "text/javascript";
        else if (suffix.equals("html") || suffix.equals("htm") || suffix.equals("shtml"))
            return "text/html";
        else
            return "";
    }

}
