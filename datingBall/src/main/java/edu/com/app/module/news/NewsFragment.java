package edu.com.app.module.news;

import android.view.View;
import android.widget.ListView;

import com.squareup.sqlbrite.BriteDatabase;

import javax.inject.Inject;

import butterknife.Bind;
import edu.com.app.R;
import edu.com.app.base.AbsBaseFragment;
import edu.com.app.module.main.MainActivity;
import rx.android.schedulers.AndroidSchedulers;
import timber.log.Timber;

/**
 * Created by Anthony on 2016/7/22.
 * Class Note:
 * todo implements with MVP
 */
public class NewsFragment  extends AbsBaseFragment{

    @Bind(R.id.lists)
    ListView listView;

    private MenuAdapter adapter;

    @Inject
    BriteDatabase db;

    @Override
    protected void initDagger() {
        ((MainActivity)( getActivity())).activityComponent().inject(this);
    }

    @Override
    protected void initViewsAndEvents(View rootView) {
        adapter = new MenuAdapter(mContext);
        //        listView.setEmptyView(xxx);
        listView.setAdapter(adapter);

        mSubscription =db.createQuery(MenuItem.TABLES,MenuItem.QUERY_LIST)
                .mapToList(MenuItem.MAPPER)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(adapter);

        Timber.i("QUERY_LIST menu SQL:"+ MenuItem.QUERY_LIST);

    }

    @Override
    protected int getContentViewID() {
        return R.layout.fragment_lists;
    }
}
