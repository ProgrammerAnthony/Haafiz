package edu.com.app.data;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import edu.com.app.data.bean.Friends;
import edu.com.app.data.local.DatabaseHelper;
import edu.com.app.data.local.PreferencesHelper;
import edu.com.app.data.remote.FriendsService;
import rx.Observable;
import rx.functions.Action0;
import rx.functions.Func1;

/**
 * Created by Anthony on 2016/6/12.
 * Class Note:
 * Data entrance!!!
 */
@Singleton
public class DataManager {

    @Inject
    FriendsService friendsService;

    private final DatabaseHelper mDatabaseHelper;
    private final PreferencesHelper mPreferencesHelper;
    private final EventPosterHelper mEventPoster;

    @Inject
    public DataManager(PreferencesHelper preferencesHelper,
                       DatabaseHelper databaseHelper, EventPosterHelper eventPosterHelper) {
        mPreferencesHelper = preferencesHelper;
        mDatabaseHelper = databaseHelper;
        mEventPoster = eventPosterHelper;
    }

    public PreferencesHelper getPreferencesHelper() {
        return mPreferencesHelper;
    }

    public Observable<Friends> syncFriends() {
        return friendsService.getFriends()
                .concatMap(new Func1<List<Friends>, Observable<Friends>>() {
                    @Override
                    public Observable<Friends> call(List<Friends> friends) {
                        return mDatabaseHelper.setFriends(friends);
                    }
                });
    }

    public Observable<List<Friends>> getFriends() {
//        if()
        return mDatabaseHelper.getFriends().distinct();
    }


    /// Helper method to post events from doOnCompleted.
    private Action0 postEventAction(final Object event) {
        return new Action0() {
            @Override
            public void call() {
                mEventPoster.postEventSafely(event);
            }
        };
    }

}
