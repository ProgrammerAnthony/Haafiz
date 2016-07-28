package edu.com.app.module.news;

import java.util.List;

import edu.com.app.base.BasePresenter;
import edu.com.app.base.BaseView;
import edu.com.app.data.bean.Channel;

/**
 * Created by Anthony on 2016/7/27.
 * Class Note:
 * contract class define Presenter and View operation in MVP
 * {@link NewsContract} define operation of P
 * and V both in {@link edu.com.app.module.news.tab.NewsTabFragment}
 * and {@link edu.com.app.module.news.list.NewsListFragment}
 */
public interface NewsContract {

/**----tab fragment-------**/
    interface TabPresenter extends BasePresenter<TabView>{
        void loadData();
    }

    interface TabView extends BaseView{
        void showSubscribeView();
        void showEmptyView();
        void showTabView(List<Channel> channels);
    }

/**----list fragment-------**/
    interface ListPresenter extends BasePresenter<ListsView>{

        void refreshData();
        void loadMore(int pageIndex);
        void preLoadFromDb();
    }

    interface ListsView extends BaseView{
        void toDetailActivity();
    }
}
