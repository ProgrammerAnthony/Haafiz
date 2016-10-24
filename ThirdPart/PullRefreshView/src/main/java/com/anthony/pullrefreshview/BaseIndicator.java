package com.anthony.pullrefreshview;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Anthony on 2016/7/18.
 * 为Footer 和Header 定义不同的状态
 */
public abstract class BaseIndicator {
    public abstract View createView(LayoutInflater inflater, ViewGroup parent);
    public abstract void onAction();
    public abstract void onUnaction();
    public abstract void onRestore();
    public abstract void onLoading();
}
