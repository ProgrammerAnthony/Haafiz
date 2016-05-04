package edu.com.mvplibrary.contract.presenter;

import android.content.Context;

import edu.com.mvplibrary.R;
import edu.com.mvplibrary.contract.DrawerMainContract;
import edu.com.mvplibrary.model.DrawerItemsData;
import edu.com.mvplibrary.ui.fragment.TestFragment;

/**
 * Created by Anthony on 2016/5/3.
 * Class Note: Presenter in MVP
 * see {@link DrawerMainContract}--------------------------------------------Manager role of MVP
 * &{@link edu.com.mvplibrary.contract.presenter.DrawerMainPresenter}------Presenter
 * &{@link edu.com.mvplibrary.ui.activity.DrawerMainActivity}-------------View
 * &{@link DrawerItemsData}------------------------------------------------Model
 */
public class DrawerMainPresenter implements DrawerMainContract.Presenter, DrawerMainContract.onGetDrawerListListener {

    private DrawerMainContract.View mDrawerMainView;
    private Context mContext;
    private DrawerItemsData mDrawerItemsData;


    public DrawerMainPresenter(DrawerMainContract.View mView, Context mContext) {
        this.mContext = mContext;
        this.mDrawerMainView = mView;

        mDrawerItemsData = new DrawerItemsData(mContext, this);//init items Data

    }

    @Override
    public void onDrawerIconClicked() {
        //已经登录，跳到个人详情页

        //没有登录 ，则跳到登录页面。。。
    }

    @Override
    public void getDrawerList() {
        mDrawerItemsData.initItemsData();
    }

    @Override
    public void getSelectFragment(int position) {
        if (position == 3)
            mDrawerMainView.showSelectedFragment(null);
        else mDrawerMainView.showSelectedFragment(new TestFragment());
    }


    @Override
    public void onSuccess() {
        mDrawerMainView.showDrawerList(mDrawerItemsData.mDrawerItems);
        mDrawerMainView.setDrawerIcon(R.drawable.icon_head);
    }

    @Override
    public void onError() {
        // show error view
    }
}
