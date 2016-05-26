package edu.com.mvpcommon.main;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.widget.ImageView;
import android.widget.TextView;

import edu.com.mvplibrary.ui.BaseView;

/**
 * Created by Anthony on 2016/5/26.
 * Class Note:
 */
public class NewMainPresenter implements NewMainContract.Presenter {
    private NewMainContract.View mView;
    private Context mContext;


    public NewMainPresenter(Context mContext) {
        this.mContext = mContext;
    }

    // TODO: 2016/5/26  fragment 替换操作

    @Override
    public void replaceFragment(Fragment to, String tag, boolean isExpanded) {

    }


    // TODO: 2016/5/26
    @Override
    public void fabOnclick() {

    }

    /**
     * 初始化
     * @param avatar 头像
     * @param nick 昵称
     * @param background 头像背景
     */
    @Override
    public void initData(ImageView avatar, TextView nick, ImageView background) {

    }

    /**
     * 根据是否登陆进行跳转
     * @param isLogin true 表示已经登录，跳转到PersonalInfoActivity
     *                false 表示未登陆，跳转到LoginActivity
     */
    @Override
    public void processHeadIconClicked(boolean isLogin) {
        if (isLogin)
            mView.toPersonalActivity();
        else
            mView.toLoginActivity();
    }

    @Override
    public void Share() {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT, "约个球");
        intent.putExtra(Intent.EXTRA_TEXT, "和我一起来约个球玩耍吧,下载地址：XXX");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(Intent.createChooser(intent, "把约个球分享给朋友吧"));
    }

    @Override
    public void Logout() {
//        EMClient.getInstance().logout(false, new EMCallBack() {
//            @Override
//            public void onSuccess() {
//                AVUser.logOut();
//                mPreferenceManager.Clear();
//                mActivity.startActivity(new Intent(mActivity, SplashActivity.class));
//                mActivity.finish();
//            }
//
//            @Override
//            public void onError(int i, String s) {
//
//            }
//
//            @Override
//            public void onProgress(int i, String s) {
//
//            }
//        });
    }

    @Override
    public void attachView(BaseView view) {
        mView = (NewMainContract.View) view;
    }

    @Override
    public void detachView() {

    }
}
