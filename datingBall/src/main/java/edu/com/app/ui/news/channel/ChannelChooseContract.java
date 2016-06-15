package edu.com.app.ui.news.channel;

import android.net.Uri;
import android.support.v4.app.Fragment;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import edu.com.app.data.bean.ChannelEntity;
import edu.com.app.ui.base.BasePresenter;
import edu.com.app.ui.base.BaseView;

/**
 * Created by Anthony on 2016/6/14.
 * Class Note:
 */
public interface ChannelChooseContract {
    interface Presenter extends BasePresenter<View> {
        //初始化页面数据
        void initChannelData();

        //保存数据,传递当前页面数据
        void saveData(ArrayList<ChannelEntity> myChannels);
    }

    interface View extends BaseView {
        // 展示页面数据
        void showChannelChooseData(List<ChannelEntity> myChannels, List<ChannelEntity> otherChannels);

        //页面跳转
        void toMainActivity();
    }

}
