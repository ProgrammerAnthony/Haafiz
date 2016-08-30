package edu.com.app.module.news.list;

import android.view.View;
import android.widget.ListView;

import javax.inject.Inject;

import butterknife.Bind;
import edu.com.app.MyApplication;
import edu.com.app.R;
import edu.com.app.base.AbsBaseFragment;
import edu.com.app.data.DataManager;
import edu.com.app.module.main.MainActivity;
import edu.com.app.module.news.NewsContract;

/**
 * Created by Anthony on 2016/7/22.
 * Class Note:
 * todo implement function here
 */
public class NewsListFragment extends AbsBaseFragment implements NewsContract.ListsView{
    private static long AUTO_REFRESH_THRESHOLD = 60 * 1000;


    @Bind(R.id.lists)
    ListView mListView;

    @Inject
    MyApplication mApplication;

    @Inject
    DataManager dataManager;

    @Inject
    NewsListPresenter mPresenter;

    @Override
    protected void initDagger() {
        ((MainActivity) (getActivity())).activityComponent().inject(this);
    }

    @Override
    protected void initViewsAndEvents(View rootView) {

        mPresenter.attachView(this);
        mPresenter.refreshData();
    }

    @Override
    protected int getContentViewID() {
        return R.layout.fragment_lists;
    }

    @Override
    public void toDetailActivity() {

    }
}
