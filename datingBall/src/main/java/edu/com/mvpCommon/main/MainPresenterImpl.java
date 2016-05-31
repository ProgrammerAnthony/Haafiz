package edu.com.mvpCommon.main;

import android.app.Activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.Fragment;

import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;

import edu.com.base.model.rx.RxLeanCloud;
import edu.com.base.ui.BaseView;
import edu.com.base.util.ToastUtils;
import edu.com.mvpCommon.R;

/**
 * Created by Anthony on 2016/5/26.
 * Class Note:
 */
public class MainPresenterImpl implements MainContract.Presenter {
    private MainContract.View mView;
    private Context mContext;
    private RxLeanCloud mRxLeanCloud;
    private Activity mActivity;
    String currentFragmentTag;


    public MainPresenterImpl(Context mContext, Activity mActivity, RxLeanCloud mRxLeanCloud) {
        this.mContext = mContext;
        this.mActivity = mActivity;
        this.mRxLeanCloud = mRxLeanCloud;
    }

    @Override
    public void replaceFragment(Fragment to, String tag, boolean isExpanded) {
        FragmentManager fragmentManager = ((FragmentActivity)mActivity).getSupportFragmentManager();

        Fragment currentfragment = fragmentManager.findFragmentByTag(currentFragmentTag);

        if (currentfragment == null || !TextUtils.equals(tag, currentFragmentTag)) {
            currentFragmentTag = tag;
            fragmentManager.beginTransaction().replace(R.id.fragment_container, to, currentFragmentTag).commit();
        }
        if (isExpanded) {
            mView.showScrollView();
        } else {
            mView.hideScrollView();
        }
    }


    // TODO: 2016/5/26
    @Override
    public void fabOnclick() {
        ToastUtils.getInstance().showToast("fab on clicked");
/*        if (isHavedLover()) {

            mMainView.toAddSouvenirActivity();
        } else {
            mMainView.toProfileActivity();
            mMainView.showToast("请先设置另一半才能发日记哦");
        }*/

    }

    /**
     * todo 初始化头像，昵称和背景
     *
     * @param avatar     头像
     * @param nick       昵称
     * @param background CollapsingLayout的背景
     */
    @Override
    public void initData(ImageView avatar, TextView nick, ImageView background) {
//        User user = User.getCurrentUser(User.class);
//
//        String avatarurl = user.getString(UserDao.AVATARURL);
//        if (avatarurl != null) {
//            Glide.with(mContext).load(user.getString(UserDao.AVATARURL)).bitmapTransform(new CropCircleTransformation(mContext)).diskCacheStrategy(DiskCacheStrategy.ALL).into(avatar);
//        }
//        if (user.getBackground() != null) {
//            Glide.with(mContext).load(user.getBackground()).crossFade().diskCacheStrategy(DiskCacheStrategy.ALL).fitCenter().into(iv_album);
//        }
//        if (user.getBackground() == null && user.getLoverBackGround() != null) {
//            Glide.with(mContext).load(user.getLoverBackGround()).crossFade().diskCacheStrategy(DiskCacheStrategy.ALL).fitCenter().into(iv_album);
//        }
//        nick.setText(user.getString(UserDao.NICK));
    }

    /**
     * 根据是否登陆进行跳转
     *
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

    // TODO: 2016/5/27  登出处理
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
    public void UploadPicture(Uri uri) {
/*        mMainView.showProgress("上传中...");
        try {
            AVFile file = AVFile.withFile(mPreferenceManager.getCurrentUserId(), new File(new URI(uri.toString())));
            mRxLeanCloud.UploadPicture(file)
                    .flatMap(new Func1<String, Observable<AVUser>>() {
                        @Override
                        public Observable<AVUser> call(String s) {
                            User user = User.getCurrentUser(User.class);
                            user.setBackground(s);
                            return mRxLeanCloud.SaveUserByLeanCloud(user);
                        }
                    }).observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<AVUser>() {
                        @Override
                        public void onCompleted() {
                            mMainView.hideProgress();
                        }

                        @Override
                        public void onError(Throwable e) {
                            mMainView.hideProgress();
                            com.orhanobut.logger.Logger.e(e.getMessage());
                        }

                        @Override
                        public void onNext(AVUser user) {
                            mMainView.showToast("保存成功");

                        }
                    });
        } catch (FileNotFoundException | URISyntaxException e) {
            e.printStackTrace();
        }*/
    }

    @Override
    public void attachView(BaseView view) {
        mView = (MainContract.View) view;
    }

    @Override
    public void detachView() {

    }
}
