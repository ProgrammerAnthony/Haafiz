package edu.com.mvpcommon.main;

import android.content.Context;
import android.view.View;

import edu.com.mvpcommon.news.newsList.NewsFragment;
import edu.com.mvpcommon.TestFragment;
import edu.com.mvplibrary.util.ToastUtils;

/**
 * Created by Anthony on 2016/5/3.
 * Class Note: Presenter in MVP
 * see {@link DrawerMainContract}--------Manager role of MVP
 * &{@link DrawerMainPresenter}------Presenter
 * &{@link DrawerMainActivity}-------------View
 * &{@link DrawerData}-----------Model
 */
public class DrawerMainPresenter implements DrawerMainContract.Presenter, DrawerMainContract.onGetDrawerListListener {

    private DrawerMainContract.View mView;
    private Context mContext;
    private DrawerData mData;


    public DrawerMainPresenter(DrawerMainContract.View mView, Context mContext) {
        this.mContext = mContext;

        this.mView = mView;
//        mView.setPresenter(this);//bind presenter for View

        mData = new DrawerData(mContext, this);//bind data listener for Model

    }

    @Override
    public void onDrawerIconClicked() {
        //已经登录，跳到个人详情页
        ToastUtils.getInstance().showToast("icon clicked");
        //没有登录 ，则跳到登录页面。。。
    }

    @Override
    public void getDrawerData() {
        mData.initItemsData();
    }

    @Override
    public void getSelectFragment(int position) {
        if (position == 3)
            mView.onSelectFragmentGet(new NewsFragment());
        else mView.onSelectFragmentGet(new TestFragment());
    }


    @Override
    public void onSuccess() {
        mView.onDrawerListGet(mData.mDrawerItems);
//        mView.setDrawerIcon(R.drawable.icon_head);
        mView.setDrawerIcon(mData.headIconUrl);
    }

    @Override
    public void onError() {
        // show error view
        mView.showNetError(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public void start() {

    }
}
