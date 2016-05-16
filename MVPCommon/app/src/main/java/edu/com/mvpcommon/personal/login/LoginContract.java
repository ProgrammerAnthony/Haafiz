package edu.com.mvpcommon.personal.login;

import android.support.v4.app.Fragment;

import java.util.ArrayList;

import edu.com.mvpcommon.main.DrawerData;
import edu.com.mvplibrary.presenter.BasePresenter;
import edu.com.mvplibrary.ui.BaseView;

/**
 * Created by Anthony on 2016/5/16.
 * Class Note:
 */
public interface LoginContract {
    interface Presenter extends BasePresenter {
        void login();

    }

    interface View extends BaseView {
        void onLoginStateGet();

    }


    interface onLoginListener {
        void onSuccess();

        void onError();
    }

}
