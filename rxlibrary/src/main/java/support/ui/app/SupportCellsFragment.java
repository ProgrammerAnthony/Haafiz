package support.ui.app;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
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
public abstract class SupportCellsFragment extends Fragment
    implements EasyViewHolder.OnItemClickListener {

  private RecyclerView mRecyclerView;
  private EasyRecyclerAdapter mAdapter;

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                           @Nullable Bundle savedInstanceState) {
    setupRecyclerView();
    return mRecyclerView;
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    mRecyclerView = null;
  }

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setupAdapter();
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    mAdapter = null;
  }

  private void setupAdapter() {
    mAdapter = new EasyRecyclerAdapter(getContext());
    mAdapter.viewHolderFactory(new CellsViewHolderFactory(getContext()));
    mAdapter.setOnClickListener(this);
  }

  private void setupRecyclerView() {
    mRecyclerView = new RecyclerView(getContext());
    ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
        ViewGroup.LayoutParams.MATCH_PARENT);
    mRecyclerView.setLayoutParams(params);
    mRecyclerView.setHasFixedSize(true);
    mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    mRecyclerView.setAdapter(mAdapter);
  }

  @Override
  public void onItemClick(int position, View view) {
    onItemClick((CellModel) mAdapter.get(position));
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

  public void addItem(CellModel cellModel) {
    mAdapter.add(cellModel);
  }

  public void addAll(ArrayList<CellModel> items) {
    mAdapter.addAll(items);
  }

  protected abstract void onItemClick(CellModel cellModel);
}
