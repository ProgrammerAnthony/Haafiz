package edu.com.app.ui.splash;

import edu.com.app.ui.base.BasePresenter;
import edu.com.app.ui.base.BaseView;

/**
 * Created by Anthony on 2016/5/31.
 * Class Note:
 * contract class for splash view & presenter
 */
public interface SplashContract {
    interface Presenter extends BasePresenter<View> {
        void initData();//初始化数据
    }

    interface View extends BaseView {
        void toMainActivity(); //跳转到MainActivity
    }
}
