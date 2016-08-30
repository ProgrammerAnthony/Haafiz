package edu.com.app.module.news.list;

import android.content.Context;

import javax.inject.Inject;

import edu.com.app.MyApplication;
import edu.com.app.data.DataManager;
import edu.com.app.injection.scope.ActivityContext;
import edu.com.app.module.news.NewsContract;
import edu.com.app.util.ToastUtils;
import rx.Subscription;

/**
 * Created by Anthony on 2016/7/22.
 * Class Note:
 * MVP Presenter for {@link NewsListFragment}
 *  todo implement function here
 */
public class NewsListPresenter implements NewsContract.ListPresenter{
    private NewsContract.ListsView mView;
    private Context mContext;
    private Subscription mSubscription;

    @Inject
    ToastUtils mToastUtil;

    @Inject
    DataManager mDataManager;


    private MyApplication mApplication;

    @Inject
    public NewsListPresenter(@ActivityContext Context context, MyApplication application) {
        mContext = context;
        this.mApplication = application;
    }
    @Override
    public void attachView(NewsContract.ListsView view) {
        this.mView =view;
    }

    @Override
    public void detachView() {
        mView = null;
        if (mSubscription != null && !mSubscription.isUnsubscribed()) {
            mSubscription.unsubscribe();
        }
    }


    @Override
    public void refreshData() {

    }

    @Override
    public void loadMore(int pageIndex) {

    }

    @Override
    public void preLoadFromDb() {

    }


}
