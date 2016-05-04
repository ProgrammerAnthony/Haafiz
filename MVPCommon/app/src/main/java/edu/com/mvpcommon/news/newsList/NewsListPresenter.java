package edu.com.mvpcommon.news.newsList;

import android.content.Context;

/**
 * Created by Anthony on 2016/5/3.
 * Class Note:
 */
public class NewsListPresenter implements NewsListContract.Presenter, NewsListContract.onGetChannelListListener {
    private Context mContext;
    private NewsListData mData;
    private NewsListContract.View mView;

    public NewsListPresenter(Context mContext, NewsListContract.View mView) {
        this.mView = mView;
        this.mContext = mContext;

        mData =new NewsListData();

    }


    @Override
    public void getNewsList() {

    }

    @Override
    public void onSuccess() {

    }

    @Override
    public void onError() {

    }
}
