package edu.com.mvpcommon;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.SaveCallback;

import edu.com.mvplibrary.model.bean.Channel;
import edu.com.mvplibrary.ui.activity.AbsSplashActivity;
import edu.com.mvplibrary.ui.widget.ViewDisplay;
import edu.com.mvplibrary.util.LogUtil;

/**
 * Created by Anthony on 2016/5/10.
 * Class Note:
 */
public class SplashActivity extends AbsSplashActivity{


    @Override
    protected String getFirstUrl() {
        //// TODO: 2016/5/17  url define here to get data
        return "file://xxx";
    }

    @Override
    protected int getContentViewID() {
        return R.layout.activity_splash;
    }

    @Override
    protected void initDagger() {

    }

    @Override
    protected void showMain() {
        Channel channel = new Channel();
        channel.setType("1002");
        ViewDisplay.initialView(mContext, channel);
    }

}
