package support.ui.app;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import support.ui.adapters.EasyRecyclerAdapter;
import support.ui.adapters.EasyViewHolder;
import support.ui.cells.CellModel;
import support.ui.cells.CellsViewHolderFactory;

/**
 * Created by YuGang Yang on 04 09, 2016.
 * Copyright 2015-2016 qiji.tech. All rights reserved.
 */
public abstract class SupportCellsActivity extends AppCompatActivity
    implements EasyViewHolder.OnItemClickListener {

  private RecyclerView mRecyclerView;
  private EasyRecyclerAdapter mAdapter;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setupAdapter();
    setupRecyclerView();
    setContentView(mRecyclerView);
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    mRecyclerView = null;
    mAdapter = null;
  }

  private void setupAdapter() {
    mAdapter = new EasyRecyclerAdapter(this);
    mAdapter.viewHolderFactory(new CellsViewHolderFactory(this));
    mAdapter.setOnClickListener(this);
  }

  private void setupRecyclerView() {
    mRecyclerView = new RecyclerView(this);
    ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
        ViewGroup.LayoutParams.MATCH_PARENT);
    mRecyclerView.setLayoutParams(params);
    mRecyclerView.setHasFixedSize(true);
    mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    mRecyclerView.setAdapter(mAdapter);
  }

  @Override
  public void onItemClick(int position, View view) {
    onItemClick(mAdapter.get(position));
  }

  public RecyclerView getRecyclerView() {
    return mRecyclerView;
  }

  public EasyRecyclerAdapter getAdapter() {
    return mAdapter;
  }

  public void clearAll() {
    mAdapter.clear();
  }

  public void addItem(Object object) {
    mAdapter.add(object);
  }

  public void appendAll(ArrayList<CellModel> items) {
    mAdapter.appendAll(items);
  }

  public void addAll(ArrayList<CellModel> items) {
    mAdapter.addAll(items);
  }

  protected abstract void onItemClick(Object object);
}
