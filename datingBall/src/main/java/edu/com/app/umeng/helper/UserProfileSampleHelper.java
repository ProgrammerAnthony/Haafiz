package edu.com.app.umeng.helper;

import android.content.Intent;
import android.widget.Toast;

import com.alibaba.mobileim.YWIMKit;
import com.alibaba.mobileim.channel.cloud.contact.YWProfileInfo;
import com.alibaba.mobileim.channel.event.IWxCallback;
import com.alibaba.mobileim.channel.util.YWLog;
import com.alibaba.mobileim.contact.IYWContact;
import com.alibaba.mobileim.contact.IYWContactHeadClickCallback;
import com.alibaba.mobileim.contact.IYWContactService;
import com.alibaba.mobileim.contact.IYWCrossContactProfileCallback;
import com.alibaba.mobileim.lib.model.contact.Contact;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.com.base.AbsApplication;

/**
 * 用户自定义昵称和头像
 *
 * @author zhaoxu
 */
public class UserProfileSampleHelper {

    private static final String TAG = UserProfileSampleHelper.class.getSimpleName();

    private static boolean enableUseLocalUserProfile = true;

    //初始化，建议放在登录之前
    public static void initProfileCallback() {
        if (!enableUseLocalUserProfile){
            //目前SDK会自动获取导入到OpenIM的帐户昵称和头像，如果用户设置了回调，则SDK不会从服务器获取昵称和头像
            return;
        }
        YWIMKit imKit = LoginSampleHelper.getInstance().getIMKit();
        if(imKit == null) {
            return;
        }
        final IYWContactService contactManager = imKit.getContactService();

        //头像点击的回调（开发者可以按需设置）
        contactManager.setContactHeadClickCallback(new IYWContactHeadClickCallback() {
            @Override
            public Intent onShowProfileActivity(String userId, String appKey) {
                Toast.makeText(AbsApplication.app(), String.format("你点击了 %s 的头像哦~", userId), Toast.LENGTH_SHORT).show();
                return null;
            }
        });


        //设置用户信息回调，如果开发者已经把用户信息导入了IM服务器，则不需要再调用该方法，IMSDK会自动到IM服务器获取用户信息
        contactManager.setCrossContactProfileCallback(new IYWCrossContactProfileCallback() {

            /**
             * 设置头像点击事件, 该方法已废弃，后续请使用{@link IYWContactService#setContactHeadClickCallback(IYWContactHeadClickCallback)}
             * @param userId 需要打开页面的用户
             * @param appKey 需要返回个人信息的用户所属站点
             * @return
             * @deprecated
             */
            @Override
            public Intent onShowProfileActivity(String userId, String appKey) {
                return null;
            }

            @Override
            public void updateContactInfo(Contact contact) {

            }
            //此方法会在SDK需要显示头像和昵称的时候，调用。同一个用户会被多次调用的情况。
            //比如显示会话列表，显示聊天窗口时同一个用户都会被调用到。
            @Override
            public IYWContact onFetchContactInfo(final String userId, final String appKey) {
                //[需特殊处理的账号(如客服账号)] 或者 [昵称等Profile信息未导入云旺服务器]时。此为示例代码
                if(isNeedSpecialHandleAccount(userId, appKey)){
                    //首先从内存中获取用户信息
                    // todo 由于每次更新UI都会调用该方法，所以必现创建一个内存缓存，先从内存中拿用户信息，内存中没有才访问数据库或者网络，如果不创建内存缓存，而是每次都访问数据库或者网络，会导致死循环！！
                    final IYWContact userInfo = mUserInfo.get(userId);
                    //若内存中有用户信息，则直接返回该用户信息
                    if (userInfo != null){
                        YWLog.d(TAG + "@profile", "onFetchContactInfo Hit! " + userInfo.toString());
                        return userInfo;
                    } else {
                        //若内存中没有用户信息则从服务器获取用户信息
                        //先创建一个临时的IYWContact对象保存到mUserInfo，这是为了避免服务器请求成功之前SDK又一次调用了onFetchContactInfo()方法，从而导致再次触发服务器请求
                        IYWContact temp = new UserInfo(userId, null,userId,appKey);
                        mUserInfo.put(userId, temp);
                        List<String> uids = new ArrayList<String>();
                        uids.add(userId);
                        //从服务端获取用户信息，这里的示例的代码是从IM服务端获取用户信息的，在实际使用时开发者可以根据需求 从自己的服务器或者从本地数据库获取用户信息
                        YWLog.d(TAG + "@profile", "onFetchContactInfo Not-Hit! fetchUserProfile " + userId);

                        contactManager.fetchUserProfile(uids, appKey, new IWxCallback() {
                            @Override
                            public void onSuccess(Object... result) {
                                if (result != null && result.length > 0){
                                    List<YWProfileInfo> infos = (List<YWProfileInfo>) result[0];
                                    if (infos.size() > 0){
                                        YWProfileInfo profileInfo = infos.get(0);
                                        IYWContact contact = new UserInfo(profileInfo.nick, profileInfo.icon,userId,appKey);
                                        //todo 更新内存中的用户信息，这里必须要更新内存中的数据，以便IMSDK刷新UI时可以直接从内存中拿到数据
                                        mUserInfo.remove(userId);   //移除临时的IYWContact对象
                                        mUserInfo.put(profileInfo.userId, contact);  //保存从服务器获取到的数据
                                        //todo 通知IMSDK更新UI
                                        YWLog.d(TAG + "@profile", "fetchUserProfile notifyContactProfileUpdate! " + contact.toString());

                                        contactManager.notifyContactProfileUpdate(contact.getUserId(), appKey);
                                    }
                                }
                            }

                            @Override
                            public void onError(int code, String info) {
                                //移除临时的IYWContact对象，从而保证SDK再次调用onFetchContactInfo()时可用再次触发服务器请求
                                mUserInfo.remove(userId);
                            }

                            @Override
                            public void onProgress(int progress) {

                            }
                        });
                        return temp;
                    }
                }
                //todo 当返回null时，SDK内部才会从云旺服务器获取对应的profile,因此这里必须返回null
                return null;
            }
        });
    }

    /**
     *  todo 1. 账号的昵称和头像已经导入云旺服务器的情况，一般情况直接返回false即可，除非需"特殊处理"的账号 2.未导入云旺服务器的情况，返回true即可（建议导入）
     * @param userid
     * @param appkey
     * @return
     */
    private static boolean isNeedSpecialHandleAccount(String userid, String appkey){
//        if(!TextUtils.isEmpty(userid)&&userid.startsWith("云")){
//            return true;
//        }
//        return  false;

        return true;
    }

    // 这个只是个示例，开发者需要自己管理用户昵称和头像
    private static Map<String, IYWContact> mUserInfo = new HashMap<String, IYWContact>();

    private static class UserInfo implements IYWContact {

        private String mUserNick;    // 用户昵称
        private String mAvatarPath;    // 用户头像URL
        private int mLocalResId;//主要用于本地资源
        private String mUserId;    // 用户id
        private String mAppKey;    // 用户appKey

        public UserInfo(String nickName, String avatarPath) {
            this.mUserNick = nickName;
            this.mAvatarPath = avatarPath;
        }
        public UserInfo(String nickName, String avatarPath,String userId,String appKey) {
            this.mUserNick = nickName;
            this.mAvatarPath = avatarPath;
            this.mUserId = userId;
            this.mAppKey = appKey;
        }

        public UserInfo(String nickName, int resId) {
            this.mUserNick = nickName;
            this.mLocalResId = resId;
        }

        @Override
        public String getAppKey() {
            return mAppKey;
        }

        @Override
        public String getAvatarPath() {
            if (mLocalResId != 0) {
                return mLocalResId + "";
            } else {
                return mAvatarPath;
            }
        }

        @Override
        public String getShowName() {
            return mUserNick;
        }

        @Override
        public String getUserId() {
            return mUserId;
        }

        @Override
        public String toString() {
            return "UserInfo{" +
                    "mUserNick='" + mUserNick + '\'' +
                    ", mAvatarPath='" + mAvatarPath + '\'' +
                    ", mUserId='" + mUserId + '\'' +
                    ", mAppKey='" + mAppKey + '\'' +
                    ", mLocalResId=" + mLocalResId +
                    '}';
        }
    }

}
