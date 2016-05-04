package edu.com.mvpcommon.news.newsChannel;

import java.util.ArrayList;

import edu.com.mvplibrary.ui.BaseView;
import edu.com.mvplibrary.model.Channel;

/**
 * Created by Anthony on 2016/5/3.
 * Class Note:
 * 1 define your Views & Presenters in Contract Classes;
 * 2 define listener used for View & Model indirectly interaction;
 * 3 {@link NewsChannelContract}-------------------------------------------Manager role of MVP
 * {@link NewsChannelPresenter}---------Presenter
 * &{@link NewsChannelFragment}---------------------------View
 * &{@link  NewsChannelData}--------------------Model
 *
 */
public interface NewsChannelContract {

    interface Presenter {
        void getChannels();

    }

    interface View  {
        void onChannelsGet(ArrayList<Channel> channels);
    }

    interface onGetChannelListListener{
        void onSuccess();
        void onError();
    }
}
