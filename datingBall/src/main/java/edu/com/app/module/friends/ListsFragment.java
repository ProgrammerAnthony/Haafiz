package edu.com.app.module.friends;

import android.view.View;
import android.widget.ListView;

import com.squareup.sqlbrite.BriteDatabase;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.OnItemClick;
import edu.com.app.R;
import edu.com.app.base.AbsBaseFragment;
import edu.com.app.module.main.MainActivity;
import rx.android.schedulers.AndroidSchedulers;
import timber.log.Timber;

/**
 * Created by Anthony on 2016/7/21.
 * Class Note:
 * test for SqlBrite data
 * todo implements with IM or by yourself
 */
public class ListsFragment extends AbsBaseFragment {
    @Inject
    BriteDatabase db;
    @Bind(R.id.lists)
    ListView listView;

    private ListsAdapter adapter;


    @Override
    protected void initDagger() {
        ((MainActivity) getActivity()).activityComponent().inject(this);
    }

    @Override
    protected void initViewsAndEvents(View rootView) {

        adapter = new ListsAdapter(mContext);
//        listView.setEmptyView(xxx);
        listView.setAdapter(adapter);

        mSubscription = db.createQuery(ListsItem.TABLES, ListsItem.QUERY_LIST)
                .mapToList(ListsItem.MAPPER)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(adapter);

        Timber.i("QUERY_LIST SQL:"+ ListsItem.QUERY_LIST);

    }

    @OnItemClick(R.id.lists)
    void onItemClick(long listId) {

    }

    @Override
    protected int getContentViewID() {
        return R.layout.fragment_lists;
    }
}
