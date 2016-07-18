package edu.com.app.ui.friends.list;

import android.app.Activity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import edu.com.app.R;
//import edu.com.app.data.SyncService;
import edu.com.app.data.bean.Friends;
import edu.com.app.base.AbsBaseFragment;
import edu.com.app.ui.main.MainActivity;

/**
 * Created by Anthony on 2016/5/10.
 * Class Note:好友列表界面,利用android-bi
 */
public class FriendsListFragment extends AbsBaseFragment implements FriendsListContract.View{
    private static final String EXTRA_TRIGGER_SYNC_FLAG =
            "edu.com.app.module.friends.list.FriendsListFragment.EXTRA_TRIGGER_SYNC_FLAG";
    @Inject
    FriendsListPresenter mPresenter;

    @Inject
    FriendsAdapter mFriendsAdapter;

    @Bind(R.id.recycler_view)
    RecyclerView mRecyclerView;

    @Inject
    Activity activity;

    @Override
    protected void initDagger() {
        ((MainActivity) getActivity()).activityComponent().inject(this);
    }

    @Override
    protected void initViewsAndEvents(View rootView) {

        if (activity.getIntent().getBooleanExtra(EXTRA_TRIGGER_SYNC_FLAG, true)) {
//            activity.startService(SyncService.getStartIntent(activity));
        }

        mRecyclerView.setAdapter(mFriendsAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));

        mPresenter.attachView(this);
        mPresenter.loadFriends();


    }

    @Override
    protected int getContentViewID() {
        return R.layout.fragment_friends;
    }

    /***** MVP View methods implementation *****/
    @Override
    public void showFriends(List<Friends> contacts) {
        mFriendsAdapter.setFriends(contacts);
        mFriendsAdapter.notifyDataSetChanged();
    }

}
