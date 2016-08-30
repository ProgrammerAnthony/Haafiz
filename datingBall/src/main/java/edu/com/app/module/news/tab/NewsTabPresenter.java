package edu.com.app.module.news.tab;

import android.content.Context;

import java.util.List;

import javax.inject.Inject;

import edu.com.app.MyApplication;
import edu.com.app.data.DataManager;
import edu.com.app.data.bean.Channel;
import edu.com.app.data.bean.Constants;
import edu.com.app.data.retrofit.HttpSubscriber;
import edu.com.app.injection.scope.ActivityContext;
import edu.com.app.module.news.NewsContract;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import timber.log.Timber;

/**
 * Created by Anthony on 2016/7/27.
 * Class Note:
 * todo implement function here
 * MVP Presenter for {@link NewsTabFragment}
 */
public class NewsTabPresenter implements NewsContract.TabPresenter {
    private NewsContract.TabView mView;
    private Context mContext;
    private Subscription mSubscription;

    @Inject
    DataManager mDataManager;

    private MyApplication mApplication;

    @Inject
    public NewsTabPresenter(@ActivityContext Context context, MyApplication application) {
        mContext = context;
        this.mApplication = application;
    }

    /**
     * --operation in contract class--
     **/
    @Override
    public void loadDataFromDb() {

        mSubscription = mDataManager.queryChannelList()
                .subscribe(new Action1<List<Channel>>() {
                    @Override
                    public void call(List<Channel> channels) {
                        Timber.i("NewsTabPresenter,channel size is " + channels.size());
                        if (channels == null || channels.size() == 0)
                            mView.showEmptyView();
                        else {
//                            assignChannels(channels);
                            mView.showTabView(channels);
                        }

                    }
                });
    }

    @Override
    public void loadDataOnlineThenSave() {
        mSubscription = mDataManager.loadChannelList(Constants.FIRST_MENU_URL)
                .doOnNext(mDataManager.saveChannelListToDb)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new HttpSubscriber<List<Channel>>() {
                    @Override
                    public void onNext(List<Channel> channels) {
                        Timber.i("NewsTabPresenter,channel size is " + channels.size());
//                        mApplication.channels = channels;//load to global instance
                        if (channels == null || channels.size() == 0)
                            mView.showEmptyView();
                        else {
//                            assignChannels(channels);
                            mView.showTabView(channels);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        mView.showEmptyView();
                    }
                });
    }

    @Override
    public void updateSubscribeInfo(List<Channel> myChannels, List<Channel> otherChannels) {
        List<Channel> channels1 = myChannels;
        //step1 update current channels,then update db
        for (Channel channel : channels1) {
            channel = Channel.create(channel.title(), channel.type(), channel.url(), channel.isFix(), 1);
            mDataManager.updateChannelInDb(channel);
        }
//        mMyChannels = channels1;
        mDataManager.mMyChannels = channels1;

        //step2 update other channels,then update db
        List<Channel> channels2 = otherChannels;

        for (Channel channel : channels2) {
            channel = Channel.create(channel.title(), channel.type(), channel.url(), channel.isFix(), 0);
            mDataManager.updateChannelInDb(channel);
        }
//        mOtherChannels = channels2;
        mDataManager.mOtherChannels = channels2;
    }


    @Override
    public void attachView(NewsContract.TabView view) {
        this.mView = view;
    }

    @Override
    public void detachView() {
        mView = null;
        if (mSubscription != null && !mSubscription.isUnsubscribed()) {
            mSubscription.unsubscribe();
        }
    }


}
