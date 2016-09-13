package com.anthony.app.common.base;

/**
 * Created by Anthony on 2016/5/3.
 * Class Note:
 * interface for MVP View in all of the project
 */
public interface BaseView {

    void showMessage(String msg);

    void close();

    void showProgress(String msg);

    void showProgress(String msg, int progress);

    void hideProgress();

    void showErrorMessage(String msg, String content);
}
