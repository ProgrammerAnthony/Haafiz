package edu.com.mvpcommon.news.newsList;

import java.util.ArrayList;

import edu.com.mvplibrary.model.Channel;
import edu.com.mvplibrary.model.News;
import edu.com.mvplibrary.presenter.BasePresenter;
import edu.com.mvplibrary.ui.BaseView;

/**
 * Created by Anthony on 2016/5/3.
 * Class Note:
 * 1 define your Views & Presenters in Contract Classes;
 * 2 define listener used for View & Model indirectly interaction;
 * 3 {@link NewsListContract}-------------------------------------------Manager role of MVP
 * {@link NewsListPresenter}---------Presenter
 * &{@link NewsChannelFragment}---------------------------View
 * &{@link  NewsListData}--------------------Model
 *
 */
public interface NewsListContract {

    interface Presenter extends BasePresenter{
        void getData(String url);

    }

    interface View  extends BaseView{
        void onDataReceived(ArrayList<Channel> channels);

        void loadMore(ArrayList<News> news);

        void refresh(ArrayList<News> news);
    }

    interface onGetChannelListListener{
        void onSuccess();
        void onError();
    }
}
