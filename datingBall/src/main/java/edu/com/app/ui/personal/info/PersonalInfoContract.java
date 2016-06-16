package edu.com.app.ui.personal.info;

import android.net.Uri;
import android.support.v4.app.Fragment;
import android.widget.ImageView;
import android.widget.TextView;

import edu.com.app.ui.base.BasePresenter;
import edu.com.app.ui.base.BaseView;

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
