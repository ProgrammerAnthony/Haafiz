package edu.com.mvpcommon.main;

import android.support.v4.app.Fragment;

import java.util.ArrayList;

import edu.com.mvplibrary.ui.BaseView;

/**
 * Created by Anthony on 2016/5/3.
 * Class Note:
 * 1 define your Views & Presenters in Contract Classes;
 * 2 define listener used for View & Model indirectly interaction;
 * 3 {@link DrawerMainContract}-------------------------------------------Manager role of MVP
 * {@link DrawerMainPresenter}------Presenter
 * &{@link DrawerMainActivity}-------------View
 * &{@link DrawerItemsData}------------------------------------------------Model
 */
public interface DrawerMainContract {

    interface Presenter {

        void getDrawerList();

        void onDrawerIconClicked();

        void getSelectFragment(int position);
    }

    interface View {
        void onDrawerListGet(ArrayList<DrawerItemsData.DrawerItem> list);

        void setDrawerIcon(int resId);

        void onSelectFragmentGet(Fragment fragment);


    }


    interface onGetDrawerListListener {
        void onSuccess();

        void onError();
    }


}
