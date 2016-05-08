package edu.com.mvpcommon.main;

import android.support.v4.app.Fragment;

import java.util.ArrayList;


import edu.com.mvplibrary.presenter.BasePresenter;
import edu.com.mvplibrary.ui.BaseView;


/**
 * Created by Anthony on 2016/5/3.
 * Class Note:
 * 1 define your Views & Presenters in Contract Classes;
 * 2 define listener used for View & Model indirectly interaction;
 * 3 {@link DrawerMainContract}-------------------------------------------Manager role of MVP
 * {@link DrawerMainPresenter}------Presenter
 * &{@link DrawerMainActivity}-------------View
 * &{@link DrawerData}---------------------------------Model
 */
public interface DrawerMainContract {

    interface Presenter extends BasePresenter{

        void getDrawerData();

        void onDrawerIconClicked();

        void getSelectFragment(int position);
    }

    interface View extends BaseView{
        void onDrawerListGet(ArrayList<DrawerData.DrawerItem> list);

        void setDrawerIcon(String url);

        void onSelectFragmentGet(Fragment fragment);


    }


    interface onGetDrawerListListener {
        void onSuccess();

        void onError();
    }


}
