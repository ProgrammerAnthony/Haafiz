package edu.com.mvpCommon.main;

import android.content.Context;
import android.support.v4.app.Fragment;

import edu.com.base.model.bean.Channel;
import edu.com.base.ui.BaseView;
import edu.com.base.ui.widget.ViewDisplay;
import edu.com.base.util.ToastUtils;

/**
 * Created by Anthony on 2016/5/3.
 * Class Note: Presenter in MVP
 * see {@link MainContract}--------Manager role of MVP
 * &{@link MainPresenter}------Presenter
 * &{@link MainActivity}-------------View
 * &{@link DrawerData}-----------Model
 */
public class MainPresenter implements MainContract.Presenter, MainContract.onGetDrawerListListener {

    private MainContract.View mView;
    private Context mContext;
    private DrawerData mData;


    public MainPresenter(Context mContext) {
        this.mContext = mContext;

//        this.mView = mView;
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
                mChannel.setType("1006");//LoginActivity
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
//        mView.showNetError(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });
        mView.showMessage("error");
    }


    @Override
    public void attachView(BaseView view) {
        mView = (MainContract.View) view;
    }

    @Override
    public void detachView() {

    }
}
