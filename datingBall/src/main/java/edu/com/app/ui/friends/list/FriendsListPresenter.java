package edu.com.app.ui.friends.list;

import android.content.Context;

import java.util.List;

import javax.inject.Inject;

import edu.com.app.data.DataManager;
import edu.com.app.data.bean.Friends;
import edu.com.app.injection.scope.ActivityContext;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Anthony on 2016/6/13.
 * Class Note:
 */
public class FriendsListPresenter implements FriendsListContract.Presenter {

    private FriendsListContract.View mMvpView;
    private Context mContext;
    private Subscription mSubscription;

    @Inject
    DataManager mDataManager;

    @Inject
    public FriendsListPresenter(@ActivityContext Context context) {
        mContext = context;
    }

    /***** MVP Presenter methods implementation *****/
    @Override
    public void loadFriends() {
//        checkViewAttached();
        mSubscription = mDataManager.getFriends()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<List<Friends>>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
//                        Timber.e(e, "There was an error loading the ribots.");
                        mMvpView.showErrorMessage("error","loading friends error");
                    }

                    @Override
                    public void onNext(List<Friends> friends) {
                        if (friends.isEmpty()) {
//                            mMvpView.showEmpty();
//                            mMvpView.showErrorMessage("empty","nothing to show");
                        } else {
                            mMvpView.showFriends(friends);
                        }
                    }
                });
    }

    @Override
    public void attachView(FriendsListContract.View view) {
        mMvpView = view;
    }

    @Override
    public void detachView() {
        mMvpView = null;
        if (mSubscription != null) mSubscription.unsubscribe();
    }

}
