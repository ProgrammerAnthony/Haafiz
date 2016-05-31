package edu.com.mvplibrary.presenter;

import android.support.annotation.NonNull;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;

import edu.com.mvplibrary.ui.BaseView;

/**
 * Created by Anthony on 2016/5/4.
 * Class Note:
 * MVP中所有Presenter的接口，完成view的绑定和解除
 *
 */
public interface BasePresenter {
    /**
     * 注入View，使之能够与View相互响应
     *
     * @param view
     */
    void attachView(BaseView view );

    /**
     * 释放资源
     */
    void detachView();
}
/*
    protected Reference<T> mViewRef;

    public void attachView(T view) {
        mViewRef = new WeakReference<T>(view);
    }

    protected T getView() {
        return mViewRef.get();
    }

    public boolean isViewAttahed() {
        return mViewRef != null && mViewRef.get() != null;
    }

    public void detachView() {
        if (mViewRef != null) {
            mViewRef.clear();
            mViewRef = null;
        }
    }*/
