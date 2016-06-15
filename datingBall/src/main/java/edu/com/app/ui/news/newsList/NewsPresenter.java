package edu.com.app.ui.news.newsList;

import android.content.Context;

import javax.inject.Inject;

import edu.com.app.data.bean.NewsData;
import edu.com.app.injection.scope.ActivityContext;

/**
 * Created by Anthony on 2016/5/3.
 * Class Note: Presenter in MVP
 * see {@link NewsContract}-----------Manager role of MVP
 * &{@link NewsPresenter}------------Presenter
 * &{@link NewsFragment}-------------View
 * &{@link NewsData}-----------------Model
 */
public class NewsPresenter implements NewsContract.Presenter, NewsContract.onGetChannelListListener {
    private NewsContract.View mView;
    private Context mContext;
    private NewsData mData;

    @Inject
    public NewsPresenter(@ActivityContext Context mContext) {
        this.mContext = mContext;
//        this.mView = mView;
//        mView.setPresenter(this);//!!! bind presenter for View

// TODO: 2016/6/14  modify data
        mData = new NewsData(mContext, this);//!!!bind data listener to Model
    }

    @Override
    public void getData(String url) {
        // TODO: 2016/5/6  传递给model层处理数据
        try {
            mData.parseUrl(url);
        } catch (Exception e) {
            this.onError();
            e.printStackTrace();
        }

    }


    @Override
    public void onSuccess() {
        mView.hideProgress();
        mView.onDataReceived(mData.getChannels());

    }

    @Override
    public void onError() {
        mView.showMessage("error");
    }


    @Override
    public void attachView(NewsContract.View view) {
        mView = view;
    }

    @Override
    public void detachView() {
        mView = null;
    }
}
