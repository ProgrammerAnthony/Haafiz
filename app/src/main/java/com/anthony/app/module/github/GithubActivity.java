package com.anthony.app.module.github;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.anthony.app.R;
import com.anthony.app.dagger.DaggerActivity;
import com.anthony.app.dagger.DataRepository;
import com.anthony.app.dagger.component.ActivityComponent;
import com.anthony.library.data.bean.NewsItem;
import com.anthony.library.data.net.HttpSubscriber;
import com.anthony.library.widgets.webview.WebViewCommentActivity;
import com.anthony.rvhelper.adapter.CommonAdapter;
import com.anthony.rvhelper.base.ViewHolder;
import com.anthony.rvhelper.divider.RecycleViewDivider;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import rx.Subscription;

/**
 * Created by Anthony on 2016/10/13.
 * Class Note:
 * load github user list from Github API
 */

public class GithubActivity extends DaggerActivity {
    @BindView(R.id.recycleView)
    RecyclerView recycleView;
    @Inject
    DataRepository mDataManager;
    private UserItemAdapter mUserItemAdapter;

    @Override
    protected int getContentViewID() {
        return R.layout.prj_rev_list;
    }

    @Override
    protected void initViewsAndEvents(Bundle savedInstanceState) {
        //recyclerView
        mUserItemAdapter = new UserItemAdapter(mContext);
        recycleView.addItemDecoration(new RecycleViewDivider(mContext, LinearLayoutManager.VERTICAL));
        recycleView.setLayoutManager(new LinearLayoutManager(mContext));
        recycleView.setAdapter(mUserItemAdapter);

//load data ,what we get is String
//        mSubscription = mDataManager.loadUserFollowingString("CameloeAnthony")
//                .subscribe(new HttpSubscriber<String>() {
//                    @Override
//                    public void onNext(String s) {
//                        Timber.i("response String is "+s);
//                    }
//                });

// load data,what we get is List<GithubUser>
        Subscription subscription = mDataManager.loadUserFollowingList("CameloeAnthony")
                .subscribe(new HttpSubscriber<List<GithubUser>>() {
                    @Override
                    public void onNext(List<GithubUser> users) {
                        mUserItemAdapter.addDataAll(users);
                        mUserItemAdapter.notifyDataSetChanged();
                    }
                });

        mSubscriptions.add(subscription);
    }

    @Override
    protected void injectDagger(ActivityComponent activityComponent) {
        activityComponent.inject(this);
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

    public class UserItemAdapter extends CommonAdapter<GithubUser> {
        public UserItemAdapter(Context context) {
            super(context, R.layout.prj_icon_with_title);
        }


        @Override
        protected void convert(ViewHolder holder, final GithubUser user, int position) {
            holder.setImageUrl(R.id.item_header_icon, user.getAvatarUrl());
            holder.setText(R.id.item_tv_title, user.getLogin());

            //load webview when click
            holder.getConvertView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    NewsItem newsItem = new NewsItem();
                    newsItem.setUrl(user.getHtmlUrl());
                    loadWebViewActivity(mContext, newsItem);
                }
            });

        }
    }
}
