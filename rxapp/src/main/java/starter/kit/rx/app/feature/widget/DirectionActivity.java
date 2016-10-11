package starter.kit.rx.app.feature.widget;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import butterknife.BindView;
import butterknife.OnClick;
import java.util.concurrent.TimeUnit;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import starter.kit.rx.app.R;
import starter.kit.feature.rx.RxStarterActivity;
import starter.kit.rx.app.feature.feed.FeedsViewHolder;
import starter.kit.rx.app.model.entity.Feed;
import starter.kit.util.ProgressInterface;
import starter.kit.util.RxUtils;
import support.ui.adapters.EasyRecyclerAdapter;
import support.ui.widget.SwipeRefreshLayout;

import static rx.android.schedulers.AndroidSchedulers.mainThread;

/**
 * Created by YuGang Yang on 07 19, 2016.
 * Copyright 2015-2016 qiji.tech. All rights reserved.
 */
public class DirectionActivity extends RxStarterActivity
    implements SwipeRefreshLayout.OnRefreshListener, ProgressInterface {

  @BindView(R.id.swipeRefreshLayout) SwipeRefreshLayout swipeRefreshLayout;
  @BindView(R.id.recyclerView) RecyclerView recyclerView;

  private EasyRecyclerAdapter mAdapter;

  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_direction);

    swipeRefreshLayout.setOnRefreshListener(this);
    swipeRefreshLayout.setColorSchemeColors(Color.BLUE, Color.GREEN, Color.RED, Color.YELLOW);

    mAdapter = new EasyRecyclerAdapter(this);
    mAdapter.bind(Feed.class, FeedsViewHolder.class);
    recyclerView.setAdapter(mAdapter);
    recyclerView.setLayoutManager(new LinearLayoutManager(this));
  }

  @OnClick({
      R.id.buttonBoth, R.id.buttonTop, R.id.buttonBottom, R.id.buttonRefresh,
  }) public void onClick(View view) {
    switch (view.getId()) {
      case R.id.buttonBoth:
        swipeRefreshLayout.setDirection(SwipeRefreshLayout.Direction.BOTH);
        break;
      case R.id.buttonTop:
        swipeRefreshLayout.setDirection(SwipeRefreshLayout.Direction.TOP);
        break;
      case R.id.buttonBottom:
        swipeRefreshLayout.setDirection(SwipeRefreshLayout.Direction.BOTTOM);
        break;
      case R.id.buttonRefresh:
        doRefresh();
        break;
    }
  }

  @Override public void showProgress() {
    Observable.empty()
        .observeOn(mainThread())
        .doOnTerminate(() -> swipeRefreshLayout.setRefreshing(true))
        .subscribe();
  }

  @Override public void hideProgress() {
    rx.Observable.empty()
        .observeOn(mainThread())
        .doOnTerminate(() -> swipeRefreshLayout.setRefreshing(false))
        .subscribe();
  }

  @Override public void onRefresh(SwipeRefreshLayout.Direction direction) {
    doRefresh();
  }

  private void doRefresh() {
    rx.Observable.empty()
        .subscribeOn(Schedulers.io())
        .delay(5, TimeUnit.SECONDS)
        .compose(RxUtils.progressTransformer(this))
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe();
  }
}
