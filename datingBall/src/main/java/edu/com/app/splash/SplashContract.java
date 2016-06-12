package edu.com.app.splash;

import edu.com.base.ui.BasePresenter;
import edu.com.base.ui.BaseView;

/**
 * Created by Anthony on 2016/5/31.
 * Class Note:
 */
public interface SplashContract {
    interface Presenter extends BasePresenter<View> {
        void initData();//初始化数据
    }

    interface View extends BaseView {
        void toMainActivity(); //跳转到MainActivity
    }
}
