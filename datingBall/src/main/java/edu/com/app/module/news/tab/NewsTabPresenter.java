package edu.com.app.module.news.tab;

import android.content.Context;

import java.util.List;

import javax.inject.Inject;

import edu.com.app.MyApplication;
import edu.com.app.data.DataManager;
import edu.com.app.data.bean.Channel;
import edu.com.app.injection.scope.ActivityContext;
import edu.com.app.module.news.NewsContract;
import rx.Subscription;
import rx.functions.Action1;

/**
 * Created by Anthony on 2016/7/27.
 * Class Note:
 *  todo implement function here
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

    @Override
    public void loadData() {

        mSubscription =mDataManager.queryChannelList(Channel.TABLE,Channel.QUERY_CHANNEL_LIST,new String[]{})
                .subscribe(new Action1<List<Channel>>() {
                    @Override
                    public void call(List<Channel> channels) {
                        if(channels ==null|| channels.size()==0)
                            mView.showEmptyView();
                        else
                            mView.showTabView(channels);

                    }
                });
    }

    @Override
    public void attachView(NewsContract.TabView view, Subscription subscription) {
        this.mView =view;
        this.mSubscription =subscription;
    }

    @Override
    public void detachView() {
        mView = null;
        if (mSubscription != null && !mSubscription.isUnsubscribed()) {
            mSubscription.unsubscribe();
        }
    }
}
