package com.anthony.app.common.widgets;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.anthony.app.R;
import com.anthony.app.common.base.MyApplication;
import com.anthony.app.common.data.bean.NewsItem;
import com.anthony.app.common.data.database.dao.NewsItemDao;

import java.util.HashMap;
import java.util.List;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.tencent.qzone.QZone;
import cn.sharesdk.wechat.favorite.WechatFavorite;


public class CommentView extends RelativeLayout {
    private Context mContext;
    private RelativeLayout mLayoutEditComment;
    private ImageView mImgStar;
    private boolean isStar = false;
    private RelativeLayout mLayoutIcShare;
    private RelativeLayout mLayoutIcStar;
    private String mTitle = "";
    private String mUrl = "";
    private String mImageUrl = "";
    private String mDescription = "";
    private NewsItemDao dao;
    private NewsItem mItem;


    public CommentView(Context context) {
        super(context);
        mContext = context;
        initView();
    }

    public CommentView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CommentView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        initView();
    }

    private void initView() {
        dao = new NewsItemDao(MyApplication.get(mContext));

        LayoutInflater.from(mContext).inflate(R.layout.lib_layout_comment, this);
        mLayoutEditComment = (RelativeLayout) findViewById(R.id.layout_edit_comment);
        mLayoutEditComment.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                showCommentEditBox();
            }
        });

        mImgStar = (ImageView) findViewById(R.id.ic_star);
        mLayoutIcStar = (RelativeLayout) findViewById(R.id.layout_ic_star);
        mLayoutIcStar.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isStar) {
                    mImgStar.setImageResource(R.mipmap.prj_ic_star);
                    mItem.setStar(false);
                } else {
                    mImgStar.setImageResource(R.mipmap.prj_ic_star_solid);
                    mItem.setStar(true);
                }
                dao.update(mItem);
                isStar = !isStar;
            }
        });

        mLayoutIcShare = (RelativeLayout) findViewById(R.id.layout_ic_share);
        mLayoutIcShare.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                showShare();
            }
        });
    }

    public void setTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public void setUrl(String mUrl) {
        this.mUrl = mUrl;
        List<NewsItem> list = dao.queryByColumn("url", mUrl);
        if (list != null && list.size() > 0) {
            mItem = list.get(0);
            if(mItem.isStar()) {
                mImgStar.setImageResource(R.mipmap.prj_ic_star_solid);
            } else {
                mImgStar.setImageResource(R.mipmap.prj_ic_star);
            }
        }
    }

    public void setImageUrl(String mImageUrl) {
        this.mImageUrl = mImageUrl;
    }

    public void setDescription(String mDescription) {
        this.mDescription = mDescription;
    }

    private void showCommentEditBox() {
        final Dialog dialog = new Dialog(mContext, R.style.dialog_comment);
        View contentView = LayoutInflater.from(mContext).inflate(R.layout.lib_dialog_comment, null);
        dialog.setContentView(contentView);
        Window dialogWindow = dialog.getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width = WindowManager.LayoutParams.FILL_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialogWindow.setGravity(Gravity.BOTTOM);
        dialogWindow.setAttributes(lp);

        final EditText mEditTextComment = (EditText) contentView.findViewById(R.id.edit_comment);
        TextView submit = (TextView) contentView.findViewById(R.id.btn_commit);
        submit.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                String content = mEditTextComment.getText().toString();
                if (TextUtils.isEmpty(content)) {
//                    ToastUtil.getInstance().showToast("评论内容不能为空");
                    Toast.makeText(mContext,"评论内容不能为空",Toast.LENGTH_SHORT).show();
                    return;
                }

                //TODO: 这里提交HTTP请求上传评论
            }
        });
        contentView.findViewById(R.id.btn_close).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(mEditTextComment.getWindowToken(), 0);
            }
        });
        dialog.show();
    }

    private void showShare() {
        OnekeyShare oks = new OnekeyShare();
        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
        oks.setTitle(mTitle);
        // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
        oks.setTitleUrl(mUrl);
        // text是分享文本，所有平台都需要这个字段
        oks.setText(mDescription);
        // imageUrl是图片的网络路径，新浪微博、人人网、QQ空间、微信的两个平台、Linked-In支持此字段
        oks.setImageUrl(mImageUrl);
        // url仅在微信（包括好友和朋友圈）中使用
        oks.setUrl(mUrl);
        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
        oks.setComment("我正在使用" + getResources().getString(R.string.app_name) + "，快来试试吧");
        // site是分享此内容的网站名称，仅在QQ空间使用
        oks.setSite(getResources().getString(R.string.app_name));
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        oks.setSiteUrl(mUrl);
        // latitude是维度数据，仅在新浪微博、腾讯微博和Foursquare使用
//        oks.setLatitude(23.122619f);
        // longitude是经度数据，仅在新浪微博、腾讯微博和Foursquare使用
//        oks.setLongitude(113.372338f);
        // 是否直接分享（true则直接分享）
        oks.setSilent(false);
        // 指定分享平台，和silent一起使用可以直接分享到指定的平台
//        oks.setPlatform(platform);
        // 去除注释可通过OneKeyShareCallback来捕获快捷分享的处理结果
        oks.setCallback(new PlatformActionListener() {
            @Override
            public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
//                ToastUtil.getInstance().showToast("分享成功");
                Toast.makeText(mContext,"分享成功",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(Platform platform, int i, Throwable throwable) {
//                ToastUtil.getInstance().showToast("分享失败");
                Toast.makeText(mContext,"分享失败",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancel(Platform platform, int i) {
//                ToastUtil.getInstance().showToast("取消分享");
                Toast.makeText(mContext,"取消分享",Toast.LENGTH_SHORT).show();
            }
        });
        //通过OneKeyShareCallback来修改不同平台分享的内容
//        oks.setShareContentCustomizeCallback(new ShareContentCustomizeDemo());
        oks.addHiddenPlatform(QZone.NAME);
        oks.addHiddenPlatform(WechatFavorite.NAME);
        oks.show(mContext);
    }

//    private void shareTest() {
//        OnekeyShare oks = new OnekeyShare();
//        // 分享时Notification的图标和文字
////        oks.setNotification(R.drawable.ic_launcher, getContext().getString(R.string.app_name));
//        // address是接收人地址，仅在信息和邮件使用
////        oks.setAddress(“12345678901″);
//        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
//        oks.setTitle("TRS移动模板");
//        // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
//        oks.setTitleUrl("http://www.trs.com.cn");
//        // text是分享文本，所有平台都需要这个字段
//        oks.setText("我正在使用TRS移动模板，快来试试吧");
//        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
////        oks.setImagePath("http://www.trs.com.cn/images/trslogo.png");
//        // imageUrl是图片的网络路径，新浪微博、人人网、QQ空间、
//        // 微信的两个平台、Linked-In支持此字段
//        oks.setImageUrl("http://www.trs.com.cn/images/trslogo.png");
//        // url仅在微信（包括好友和朋友圈）中使用
//        oks.setUrl("http://www.trs.com.cn");
//        // appPath是待分享应用程序的本地路劲，仅在微信中使用
////        oks.setAppPath(MainActivity.TEST_IMAGE);
//        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
//        oks.setComment("我正在使用TRS移动模板，快来试试吧");
//        // site是分享此内容的网站名称，仅在QQ空间使用
//        oks.setSite("TRS");
//        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
//        oks.setSiteUrl("http://www.trs.com.cn");
//        // venueName是分享社区名称，仅在Foursquare使用
////        oks.setVenueName(“Southeast in China”);
//        // venueDescription是分享社区描述，仅在Foursquare使用
////        oks.setVenueDescription(“This is a beautiful place !”);
//        // latitude是维度数据，仅在新浪微博、腾讯微博和Foursquare使用
////        oks.setLatitude(23.122619f);
//        // longitude是经度数据，仅在新浪微博、腾讯微博和Foursquare使用
////        oks.setLongitude(113.372338f);
//        // 是否直接分享（true则直接分享）
//        oks.setSilent(false);
//        // 指定分享平台，和slient一起使用可以直接分享到指定的平台
////        oks.setPlatform(platform);
//        // 去除注释可通过OneKeyShareCallback来捕获快捷分享的处理结果
//        oks.setCallback(new PlatformActionListener() {
//            @Override
//            public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
//                Log.v("X", "hhh");
//            }
//
//            @Override
//            public void onError(Platform platform, int i, Throwable throwable) {
//                Log.v("X", "hhh");
//            }
//
//            @Override
//            public void onCancel(Platform platform, int i) {
//                Log.v("X", "hhh");
//            }
//        });
//        //通过OneKeyShareCallback来修改不同平台分享的内容
////        oks.setShareContentCustomizeCallback(new ShareContentCustomizeDemo());
//        oks.addHiddenPlatform(QZone.NAME);
//        oks.addHiddenPlatform(WechatFavorite.NAME);
//        oks.show(this);
//    }
}
