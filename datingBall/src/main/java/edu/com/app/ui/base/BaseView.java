package edu.com.app.ui.base;

/**
 * Created by Anthony on 2016/5/3.
 * Class Note:
 * MVP中所有 View的接口
 */
public interface BaseView {

    void showMessage(String msg);

    void close();

    void showProgress(String msg);

    void showProgress(String msg, int progress);

    void hideProgress();

    void showErrorMessage(String msg,String content);
}
