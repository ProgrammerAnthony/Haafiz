package edu.com.mvpcommon.main;

import android.content.Context;

import edu.com.mvpcommon.news.newsChannel.NewsChannelFragment;
import edu.com.mvplibrary.R;
import edu.com.mvpcommon.TestFragment;

/**
 * Created by Anthony on 2016/5/3.
 * Class Note: Presenter in MVP
 * see {@link DrawerMainContract}--------Manager role of MVP
 * &{@link DrawerMainPresenter}------Presenter
 * &{@link DrawerMainActivity}-------------View
 * &{@link DrawerItemsData}-----------Model
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
            mDrawerMainView.onSelectFragmentGet(new NewsChannelFragment());
        else mDrawerMainView.onSelectFragmentGet(new TestFragment());
    }


    @Override
    public void onSuccess() {
        mDrawerMainView.onDrawerListGet(mDrawerItemsData.mDrawerItems);
        mDrawerMainView.setDrawerIcon(R.drawable.icon_head);
    }

    @Override
    public void onError() {
        // show error view
    }
}
