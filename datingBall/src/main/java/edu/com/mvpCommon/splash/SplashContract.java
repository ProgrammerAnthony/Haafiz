package edu.com.mvpCommon.splash;

import android.net.Uri;
import android.support.v4.app.Fragment;
import android.widget.ImageView;
import android.widget.TextView;

import edu.com.base.presenter.BasePresenter;
import edu.com.base.ui.BaseView;

/**
 * Created by Anthony on 2016/5/31.
 * Class Note:
 */
public interface SplashContract {
    interface Presenter extends BasePresenter {
        void initData();//初始化数据
    }

    interface View extends BaseView {
        void toMainActivity(); //跳转到MainActivity
    }
}
