package edu.com.app.ui.friends.list;

import java.util.List;

import edu.com.app.data.bean.Friends;
import edu.com.app.base.BasePresenter;
import edu.com.app.base.BaseView;

/**
 * Created by Anthony on 2016/6/13.
 * Class Note:
 */
public interface FriendsListContract {
    interface Presenter extends BasePresenter<View> {
        void loadFriends();
    }

    interface View extends BaseView {
        void showFriends(List<Friends> contacts);
    }



}
