package edu.com.app.module.personal.info;

import edu.com.app.base.BasePresenter;
import edu.com.app.base.BaseView;

/**
 * Created by Anthony on 2016/6/16.
 * Class Note:todo
 */
public class PersonalInfoContract {
    interface Presenter extends BasePresenter<View> {
        void processAlphaTopBar();
        void loadData();
        void edit();

    }

    interface View extends BaseView {
        void updateInfo();
        void toEditInfoActivity();
        void toMainActivity();

    }

}
