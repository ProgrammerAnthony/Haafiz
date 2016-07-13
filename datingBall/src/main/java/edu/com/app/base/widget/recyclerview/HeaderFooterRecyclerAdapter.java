package edu.com.app.base.widget.recyclerview;

import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.view.ViewGroup;

/**
 * Create By Anthony on 2016/1/15
 * Class Note:
 * 实现header和footer简单添加的RecyclerView Adapter
 * {@see http://www.tuicool.com/articles/qMnAfen}
 */
public class HeaderFooterRecyclerAdapter extends RecyclerViewAdapterWrapper {
    public static final int TYPE_HEADER = 111;
    public static final int TYPE_FOOTER = 112;

    private RecyclerView.LayoutManager layoutManager;

    private View headerView, footerView;

    public HeaderFooterRecyclerAdapter(@NonNull RecyclerView.Adapter targetAdapter) {
        super(targetAdapter);
    }

    public void setHeaderView(View view) {
        headerView = view;
        getWrappedAdapter().notifyDataSetChanged();
    }

    public void removeHeaderView() {
        headerView = null;
        getWrappedAdapter().notifyDataSetChanged();
    }

    public void setFooterView(View view) {
        footerView = view;
        getWrappedAdapter().notifyDataSetChanged();
    }

    public void removeFooterView() {
        footerView = null;
        getWrappedAdapter().notifyDataSetChanged();
    }

    private void setGridHeaderFooter(RecyclerView.LayoutManager layoutManager) {
        if (layoutManager instanceof GridLayoutManager) {
            final GridLayoutManager gridLayoutManager = (GridLayoutManager) layoutManager;
            gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    boolean isShowHeader = (position == 0 && hasHeader());
                    boolean isShowFooter = (position == getItemCount() - 1 && hasFooter());
                    if (isShowFooter || isShowHeader) {
                        return gridLayoutManager.getSpanCount();
                    }
                    return 1;
                }
            });
        }
    }

    private boolean hasHeader() {
        return headerView != null;
    }

    private boolean hasFooter() {
        return footerView != null;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        layoutManager = recyclerView.getLayoutManager();
        setGridHeaderFooter(layoutManager);
    }

    @Override
    public int getItemCount() {
        return super.getItemCount() + (hasHeader() ? 1 : 0) + (hasFooter() ? 1 : 0);
    }

    @Override
    public int getItemViewType(int position) {
        if (hasHeader() && position == 0) {
            return TYPE_HEADER;
        }

        if (hasFooter() && position == getItemCount() - 1) {
            return TYPE_FOOTER;
        }
        return super.getItemViewType(hasHeader() ? position - 1 : position);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = null;
        if (viewType == TYPE_HEADER) {
            itemView = headerView;
        } else if (viewType == TYPE_FOOTER) {
            itemView = footerView;
        }
        if (itemView != null) {
            //set StaggeredGridLayoutManager header & footer view
            if (layoutManager instanceof StaggeredGridLayoutManager) {
                ViewGroup.LayoutParams targetParams = itemView.getLayoutParams();
                StaggeredGridLayoutManager.LayoutParams StaggerLayoutParams;
                if (targetParams !=  null) {
                    StaggerLayoutParams = new StaggeredGridLayoutManager.LayoutParams(targetParams.width, targetParams.height);
                } else {
                    StaggerLayoutParams = new StaggeredGridLayoutManager.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                }
                StaggerLayoutParams.setFullSpan(true);
                itemView.setLayoutParams(StaggerLayoutParams);
            }
            return new RecyclerView.ViewHolder(itemView) {
            };
        }
        return super.onCreateViewHolder(parent, viewType);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == TYPE_HEADER || getItemViewType(position) == TYPE_FOOTER) {
            //if you need get header & footer state , do here
            return;
        }
        super.onBindViewHolder(holder, hasHeader() ? position - 1 : position);
    }
}
