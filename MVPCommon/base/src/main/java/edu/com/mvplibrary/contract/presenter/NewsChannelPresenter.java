package edu.com.mvplibrary.contract.presenter;

import android.content.Context;

import edu.com.mvplibrary.contract.NewsChannelContract;
import edu.com.mvplibrary.model.NewsChannelData;

/**
 * Created by Anthony on 2016/5/3.
 * Class Note: Presenter in MVP
 *see {@link NewsChannelContract}-----Manager role of MVP
 * &{@link NewsChannelPresenter}------Presenter
 * &{@link edu.com.mvplibrary.ui.fragment.NewsChannelFragment}-------------View
 * &{@link NewsChannelData}--------Model
 */
public class NewsChannelPresenter implements NewsChannelContract.Presenter,NewsChannelContract.onGetChannelListListener{
    private NewsChannelContract.View mView;
    private Context mContext;
    private NewsChannelData mData;

    public NewsChannelPresenter(NewsChannelContract.View mView,Context mContext){
        this.mView =mView;
        this.mContext=mContext;

        mData=new NewsChannelData(mContext,this);//init news channel data
    }

    @Override
    public void loadChannel() {

    }

    @Override
    public void loadDetailFragment() {

    }

    @Override
    public void onSuccess() {

    }

    @Override
    public void onError() {

    }
}
