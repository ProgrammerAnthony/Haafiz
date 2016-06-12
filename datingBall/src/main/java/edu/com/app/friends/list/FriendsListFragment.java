package edu.com.app.friends.list;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.Bind;
import butterknife.ButterKnife;
import edu.com.app.R;
import edu.com.base.ui.fragment.AbsBaseFragment;

/**
 * Created by Anthony on 2016/5/10.
 * Class Note:好友列表界面 集成中
 */
public class FriendsListFragment extends AbsBaseFragment {


    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;

    @Override
    protected void initViewsAndEvents(View rootView) {

    }

    @Override
    protected int getContentViewID() {
        return R.layout.fragment_friends;
    }



}
