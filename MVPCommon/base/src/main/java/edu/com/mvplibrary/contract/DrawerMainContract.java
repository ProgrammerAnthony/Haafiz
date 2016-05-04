package edu.com.mvplibrary.contract;

import android.support.v4.app.Fragment;

import java.util.ArrayList;

import edu.com.mvplibrary.contract.view.BaseView;
import edu.com.mvplibrary.model.DrawerItemsData;

/**
 * Created by Anthony on 2016/5/3.
 * Class Note:
 * 1 define your Views & Presenters in Contract Classes;
 * 2 define listener used for View & Model indirectly interaction;
 * 3 {@link DrawerMainContract}-------------------------------------------Manager role of MVP
 * {@link edu.com.mvplibrary.contract.presenter.DrawerMainPresenter}------Presenter
 * &{@link edu.com.mvplibrary.ui.activity.DrawerMainActivity}-------------View
 * &{@link DrawerItemsData}------------------------------------------------Model
 */
public interface DrawerMainContract {

    interface Presenter {

        void getDrawerList();

        void onDrawerIconClicked();

        void getSelectFragment(int position);
    }

    interface View extends BaseView {
        void showDrawerList(ArrayList<DrawerItemsData.DrawerItem> list);

        void setDrawerIcon(int resId);

        void showSelectedFragment(Fragment fragment);


    }


    interface onGetDrawerListListener {
        void onSuccess();

        void onError();
    }


}
