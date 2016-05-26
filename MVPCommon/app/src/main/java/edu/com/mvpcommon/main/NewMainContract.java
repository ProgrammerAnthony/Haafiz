package edu.com.mvpcommon.main;

import android.net.Uri;
import android.support.v4.app.Fragment;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import edu.com.mvplibrary.presenter.BasePresenter;
import edu.com.mvplibrary.ui.BaseView;


/**
 * Created by Anthony on 2016/5/3.
 * Class Note:
 * 1 define your Views & Presenters in Contract Classes;
 * 2 define listener used for View & Model indirectly interaction;
 * 3 {@link NewMainContract}-------------------------------------------Manager role of MVP
 * {@link MainPresenter}------Presenter
 * &{@link MainActivity}-------------View
 * &{@link DrawerData}---------------------------------Model
 */
public interface NewMainContract {

    interface Presenter extends BasePresenter {

        void replaceFragment(Fragment to, String tag, boolean isExpanded);//替换fragment

        void fabOnclick();//悬浮按钮点击

        void initData(ImageView avatar, TextView nick, ImageView background);//初始化数据操作

        void processHeadIconClicked(boolean isLogin);//点击头像，跳转到

// todo 在设置模块中进行处理
        void Share();//分享操作

        void Logout();//登出

    }

    interface View extends BaseView{
        void toLoginActivity();//跳转到登陆注册界面

        void toPersonalActivity();//跳转到个人信息界面

        void showScrollView();//顶部的AppBarLayout可伸缩

        void hideScrollView();//顶部的AppBarLayout不可伸缩

        void ConflictAngRestart();//账号在异地登陆，退出操作

        void showPicDialog();//弹出底部（选择照片、照相）对话框
// TODO: 2016/5/26   到发现模块进行处理（FindFragment）
//        void toFriendCirleActivity();
//        void toNearbyActivity();//跳转到附近的人界面
    }





}
