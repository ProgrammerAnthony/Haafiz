package edu.com.mvpCommon.news.newsList;

import android.content.Context;

import edu.com.base.ui.BaseView;

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

    public NewsPresenter(Context mContext) {
        this.mContext = mContext;
//        this.mView = mView;
//        mView.setPresenter(this);//!!! bind presenter for View

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
//        mView.hideLoading();
        mView.hideProgress();
        mView.onDataReceived(mData.getChannels());

    }

    @Override
    public void onError() {
//        mView.showEmpty("data error", new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//            }
//        });
        mView.showMessage("error");
    }

    @Override
    public void attachView(BaseView view) {
        mView = (NewsContract.View) view;
    }

    @Override
    public void detachView() {

    }
}
