package edu.com.app.base.widget.recyclerview;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;



import java.util.concurrent.atomic.AtomicBoolean;

import edu.com.app.R;

import static android.support.v7.widget.RecyclerView.Adapter;
import static android.support.v7.widget.RecyclerView.ViewHolder;



/**
 * Create By Anthony on 2016/1/15
 * Class Note:
 * 实现双向加载更多的recyclerView
 */
public class EndlessRecyclerViewAdapter extends RecyclerViewAdapterWrapper {
    public static final int TYPE_PENDING_AFTER = 999; //后向加载
    public static final int TYPE_PENDING_BEFORE = 888; //前向加载
    private final Context context;
    private final int pendingViewResId;
    private AtomicBoolean keepOnAppendingAfter; //后向是否触发endless
    private AtomicBoolean dataPendingAfter; //后向加载中
    private AtomicBoolean keepOnAppendingBefore; //前向是否触发endless
    private AtomicBoolean dataPendingBefore; //前向加载中
    private RequestToLoadMoreListener requestToLoadMoreListener;

    public EndlessRecyclerViewAdapter(Context context,  Adapter wrapped, RequestToLoadMoreListener requestToLoadMoreListener, @LayoutRes int pendingViewResId, boolean keepOnAppendingAfter, boolean keepOnAppendingBefore) {
        super(wrapped);
        this.context = context;
        this.requestToLoadMoreListener = requestToLoadMoreListener;
        this.pendingViewResId = pendingViewResId;
        this.keepOnAppendingAfter = new AtomicBoolean(keepOnAppendingAfter);
        this.keepOnAppendingBefore = new AtomicBoolean(keepOnAppendingBefore);
        dataPendingAfter = new AtomicBoolean(false);
        dataPendingBefore = new AtomicBoolean(false);
    }

    public EndlessRecyclerViewAdapter(Context context, Adapter wrapped, RequestToLoadMoreListener requestToLoadMoreListener) {
        this(context, wrapped, requestToLoadMoreListener, R.layout.item_loading, true, true);
    }

    private void stopAppending() {
        setKeepOnAppendingAfter(false);
    }

    /**
     * Let the adapter know that data is load and ready to view.
     *
     * @param keepOnAppending whether the adapter should request to load more when scrolling to the bottom.
     */
    public void onDataReadyAfter(boolean keepOnAppending) {
        dataPendingAfter.set(false);
        setKeepOnAppendingAfter(keepOnAppending);
    }

    public void onDataReadyBefore(boolean keepOnAppending) {
        dataPendingBefore.set(false);
        setKeepOnAppendingBefore(keepOnAppending);
    }

    private void setKeepOnAppendingAfter(boolean newValue) {
        keepOnAppendingAfter.set(newValue);
        getWrappedAdapter().notifyDataSetChanged();
    }

    private void setKeepOnAppendingBefore(boolean newValue) {
        keepOnAppendingBefore.set(newValue);
        getWrappedAdapter().notifyDataSetChanged();
    }

    /**
     *
     */
    public void restartAppending() {
        dataPendingAfter.set(false);
        setKeepOnAppendingAfter(true);
        dataPendingBefore.set(false);
        setKeepOnAppendingBefore(true);
    }

    private View getPendingView(ViewGroup viewGroup) {
        return LayoutInflater.from(context).inflate(pendingViewResId, viewGroup, false);
    }

    @Override
    public int getItemCount() {
        return super.getItemCount() + (keepOnAppendingAfter.get() ? 1 : 0) + (keepOnAppendingBefore.get() ? 1 : 0);
    }

    @Override
    public int getItemViewType(int position) {
        //后向取最后
        if (keepOnAppendingAfter.get() && position == getItemCount() - 1) {
            return TYPE_PENDING_AFTER;
        }
        //前向取0
        if (keepOnAppendingBefore.get() && position == 0) {
            return TYPE_PENDING_BEFORE;
        }
        return super.getItemViewType(getWrappedAdapterPosition(position));
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(getWrappedAdapterPosition(position));
    }

    /**
     * 得到被装饰的适配器真实的位置
     *
     * @param position
     * @return
     */
    private int getWrappedAdapterPosition(int position) {
        return keepOnAppendingBefore.get() ? position - 1 : position;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_PENDING_AFTER || viewType == TYPE_PENDING_BEFORE) {
            return new PendingViewHolder(getPendingView(parent));
        }
        return super.onCreateViewHolder(parent, viewType);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Log.i("songhang", "position : " + position);
        if (getItemViewType(position) == TYPE_PENDING_AFTER) {
            if (!dataPendingAfter.get()) {
                dataPendingAfter.set(true);
                requestToLoadMoreListener.onAfterLoadMoreRequested();
            }
        } else if (getItemViewType(position) == TYPE_PENDING_BEFORE) {
            if (!dataPendingBefore.get()) {
                dataPendingBefore.set(true);
                requestToLoadMoreListener.onBeforeLoadMoreRequested();
            }
        } else {
            super.onBindViewHolder(holder, getWrappedAdapterPosition(position));
        }
    }

    public interface RequestToLoadMoreListener {
        /**
         * The adapter requests to load more data.
         * load after
         */
        void onAfterLoadMoreRequested();

        /**
         * load before
         */
        void onBeforeLoadMoreRequested();
    }

    static class PendingViewHolder extends ViewHolder {

        public PendingViewHolder(View itemView) {
            super(itemView);
        }
    }
}
