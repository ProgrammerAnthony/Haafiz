package com.anthony.library.widgets.webview;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.webkit.JavascriptInterface;

import com.anthony.library.data.bean.Image;
import com.anthony.library.widgets.imagebrowse.ImageBrowserActivity;

import java.util.ArrayList;
import java.util.List;


public class JSObject {
    private Context mContext;
    private Handler mHandler;
    private List<String> mImgList;
    private String mTitle = "";

    public JSObject(Context context) {
        this.mContext = context;
        this.mHandler = new Handler();
    }

    @JavascriptInterface
    public void addTitle(String title) {
        mTitle = title;
    }

    @JavascriptInterface
    public void addImage(String src) {
        if (mImgList == null)
            mImgList = new ArrayList<>();

        mImgList.add(src);
    }

    @JavascriptInterface
    public void openImageInWeb(final String initSrc) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(mContext, ImageBrowserActivity.class);
                intent.putExtra(ImageBrowserActivity.IMAGE_BROWSER_INIT_SRC, initSrc);
                intent.putExtra(ImageBrowserActivity.IMAGE_BROWSER_TITLE, mTitle);
                ArrayList<Image> list = new ArrayList<>();
                for (String src:mImgList) {
                    Image img = new Image();
                    img.title = mTitle;
                    img.url = src;
                    list.add(img);
                }
                intent.putParcelableArrayListExtra(ImageBrowserActivity.IMAGE_BROWSER_LIST, list);
                mContext.startActivity(intent);
            }
        });
    }
}
