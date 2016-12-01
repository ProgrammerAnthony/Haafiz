package com.anthony.library.widgets;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;

import com.anthony.library.data.bean.NewsItem;
import com.anthony.library.widgets.banner.RecommendController;
import com.anthony.library.widgets.webview.WebViewCommentActivity;
import com.anthony.rvhelper.wrapper.HeaderAndFooterWrapper;

import java.util.List;


public class TopicWrapper extends HeaderAndFooterWrapper {
    private RecommendController mController;
    private Context mContext;

    public TopicWrapper(Context ctx, RecyclerView.Adapter adapter) {
        super(adapter);
        mContext = ctx;
        mController = new RecommendController(ctx);
        mController.setOnClickListener(new RecommendController.OnItemClickListener() {
            @Override
            public void onItemClick(NewsItem topic) {
                Intent intent = new Intent(mContext, WebViewCommentActivity.class);
                intent.putExtra(WebViewCommentActivity.WEB_VIEW_ITEM, topic);
                mContext.startActivity(intent);
            }
        });
        addHeaderView(mController.getView());
    }

    public void addTopicView() {
        addHeaderView(mController.getView());
    }

    public void deleteTopicView() {
        deleteHeaderView(mController.getView());
    }

    public void addTopic(List list) {
        if (mController != null)
            mController.setTopicList(list);
    }


    public void clearTopic() {
        if (mController != null)
            mController.clearTopicList();
    }
}
