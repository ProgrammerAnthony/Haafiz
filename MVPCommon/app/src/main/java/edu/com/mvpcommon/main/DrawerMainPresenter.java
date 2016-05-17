package edu.com.mvpcommon.main;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.view.View;

import edu.com.mvplibrary.model.Channel;
import edu.com.mvplibrary.ui.widget.ViewDisplay;
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
    public void getSelectView(int position) {

        Channel mChannel = new Channel();
        switch (position) {
            case 0:
                mChannel.setType("1003");//LoginActivity
                break;
            case 1:
                mChannel.setType("2002");//ChattingListFragment
                break;
            case 2:
                mChannel.setType("2003");//FriendsListFragment
                break;
            case 3:
                mChannel.setType("2004");//NearByListFragment
                break;
            case 4:
                mChannel.setType("2005");//NewsFragment
                break;
            case 5:
                mChannel.setType("1005");//SettingsActivity
                break;
        }

        Fragment fragment = ViewDisplay.initialView(mContext, mChannel);
        if (fragment != null) {
            mView.onSelectFragmentGet(fragment, position);
        }


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
