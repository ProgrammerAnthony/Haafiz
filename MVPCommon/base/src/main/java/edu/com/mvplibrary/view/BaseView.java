package edu.com.mvplibrary.view;

import android.view.View;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;

import edu.com.mvplibrary.presenter.BasePresenter;

/**
 * Created by Anthony on 2016/4/24.
 * Class Note:
 */
public interface BaseView{

    void showLoading();

    void hideLoading();

    void showError();

    void showEmpty();

    void showNetError();

}
//    void showLoading(String msg);
//
//    void hideLoading();
//
//    void showError(String msg, View.OnClickListener onClickListener);
//
//    void showEmpty(String msg, View.OnClickListener onClickListener);
//
//    void showEmpty(String msg, View.OnClickListener onClickListener,int imageId);
//
//    void showNetError(View.OnClickListener onClickListener);