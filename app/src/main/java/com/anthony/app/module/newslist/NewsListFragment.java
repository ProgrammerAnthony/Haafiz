package com.anthony.app.module.newslist;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.anthony.app.R;
import com.anthony.app.dagger.component.ActivityComponent;
import com.anthony.library.data.bean.NewsItem;
import com.anthony.library.data.bean.NormalJsonInfo;
import com.anthony.library.data.database.dao.NewsItemDao;
import com.anthony.app.module.webview.WebViewCommentActivity;
import com.anthony.rvhelper.adapter.MultiItemTypeAdapter;
import com.anthony.rvhelper.divider.RecycleViewDivider;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by Anthony on 2016/9/9.
 * Class Note:
 * normal news list fragment
 */
public class NewsListFragment extends AbsListFragment {

    @Inject
    NewsItemDao newsItemDao;


    @Override
    protected RecyclerView.LayoutManager getLayoutManager() {
        return new LinearLayoutManager(getActivity());
    }

    @Override
    protected RecyclerView.ItemDecoration getItemDecoration() {
        return new RecycleViewDivider(mContext, LinearLayoutManager.VERTICAL, 4, R.color.gz_background_gray);
    }

    @Override
    protected MultiItemTypeAdapter getAdapter() {
        return new NewsMultiAdapter(mContext);
    }

    @Override
    protected MultiItemTypeAdapter.OnItemClickListener getItemListener() {
        return new MultiItemTypeAdapter.OnItemClickListener<NewsItem>() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder,
                                    NewsItem item, int position) {
                if (item.getUrl().endsWith(".json")) {  //if suffix is json end ,load list data ,else load webview
                    // todo
                } else {
                   loadWebViewActivity(mContext, item);
                }


            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder,
                                           NewsItem o, int position) {
                return false;
            }
        };
    }

    @Override
    protected String getRequestUrl(int index) {
        if (index == 0) {
            return getFragmentUrl();
        } else {
            String prefix = getFragmentUrl().substring(0, getFragmentUrl().lastIndexOf("."));
            return prefix + "_" + String.valueOf(index) + ".json";
        }
    }

    /**
     * 加载网页，每个网页都需要NewsItem对象
     *
     * @param context
     * @param newsItem
     */
    public static void loadWebViewActivity(Context context, NewsItem newsItem) {
        Intent intent = new Intent(context, WebViewCommentActivity.class);
        intent.putExtra(WebViewCommentActivity.WEB_VIEW_ITEM, newsItem);
        context.startActivity(intent);
    }

    @Override
    protected void initDagger2(ActivityComponent activityComponent) {
        activityComponent.inject(this);
    }

    @Override
    protected List parseData(NormalJsonInfo jsonInfo) {
        return jsonInfo.datas;
    }

    @Override
    protected List parseTopic(NormalJsonInfo jsonInfo) {
        return jsonInfo.topic_datas;
    }

    @Override
    protected int parsePageCount(NormalJsonInfo jsonInfo) {
        return Integer.parseInt(jsonInfo.page_info.page_count);
    }


    @Override
    protected void restoreData(List data) {
        restore(data, false);
    }

    @Override
    protected void restoreTopic(List data) {
        restore(data, true);
    }


    private void restore(List data, boolean isTopic) {
        if (data == null || data.size() == 0) {
            return;
        }
        List<NewsItem> list = data;
        for (NewsItem e : list) {
//            e.setChannel(mChannel);
            e.setTopic(isTopic);
        }

        for (NewsItem e : list) {
            if (isExistInDb(e)) {
                newsItemDao.update(e);
            } else {
                newsItemDao.add(e);
            }
        }
    }

    private boolean isExistInDb(NewsItem e) {
        List<NewsItem> list = newsItemDao.queryByColumn("id", e.getId());
        return list != null && list.size() > 0;
    }

//    @Override
//    public String getFragmentUrl() {
//        return Constants.MAIN_PAGE_URL;
//    }

}
