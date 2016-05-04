package edu.com.mvplibrary.ui;

import android.view.View;

/**
 * Created by Anthony on 2016/5/3.
 * Class Note:
 */
public interface BaseView {
    void showLoading(String msg);

    void hideLoading();

    void showError(String msg, View.OnClickListener onClickListener);

    void showEmpty(String msg, View.OnClickListener onClickListener);

    void showEmpty(String msg, View.OnClickListener onClickListener,int imageId);

    void showNetError(View.OnClickListener onClickListener);

}
