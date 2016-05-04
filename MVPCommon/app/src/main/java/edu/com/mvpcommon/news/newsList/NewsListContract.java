package edu.com.mvpcommon.news.newsList;

import java.util.ArrayList;

import edu.com.mvplibrary.model.Channel;
import edu.com.mvplibrary.model.Topic;
import edu.com.mvplibrary.ui.BaseView;

/**
 * Created by Anthony on 2016/5/3.
 * Class Note:
 */
public interface NewsListContract {
    interface Presenter {

        void getNewsList();
    }

    interface View extends BaseView {
        void loadMore(ArrayList<Channel> news, ArrayList<Topic> topics);

        void refresh(ArrayList<Channel> news, ArrayList<Topic> topics);
    }

    interface onGetChannelListListener {
        void onSuccess();

        void onError();
    }
}
