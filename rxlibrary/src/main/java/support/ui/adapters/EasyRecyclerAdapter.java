package support.ui.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import support.ui.adapters.debounced.DebouncedOnClickListener;
import support.ui.adapters.debounced.DebouncedOnLongClickListener;

import static support.ui.adapters.EasyViewHolder.OnItemClickListener;
import static support.ui.adapters.EasyViewHolder.OnItemLongClickListener;

public class EasyRecyclerAdapter extends RecyclerView.Adapter<EasyViewHolder> {

  private List<Object> dataList = new ArrayList<>();
  private BaseEasyViewHolderFactory viewHolderFactory;
  private OnItemClickListener itemClickListener;
  private OnItemLongClickListener longClickListener;

  public EasyRecyclerAdapter(Context context, Class valueClass,
                             Class<? extends EasyViewHolder> easyViewHolderClass) {
    this(context);
    bind(valueClass, easyViewHolderClass);
  }

  public EasyRecyclerAdapter(Context context) {
    this(new BaseEasyViewHolderFactory(context));
  }

  public EasyRecyclerAdapter(BaseEasyViewHolderFactory easyViewHolderFactory, Class valueClass,
      Class<? extends EasyViewHolder> easyViewHolderClass) {
    this(easyViewHolderFactory);
    bind(valueClass, easyViewHolderClass);
  }

  public void viewHolderFactory(BaseEasyViewHolderFactory easyViewHolderFactory) {
    this.viewHolderFactory = easyViewHolderFactory;
  }

  public EasyRecyclerAdapter(BaseEasyViewHolderFactory easyViewHolderFactory) {
    this.viewHolderFactory = easyViewHolderFactory;
  }

  public void bind(Class valueClass, Class<? extends EasyViewHolder> viewHolder) {
    viewHolderFactory.bind(valueClass, viewHolder);
  }

  @Override
  public EasyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    EasyViewHolder easyViewHolder = viewHolderFactory.create(viewType, parent);
    bindListeners(easyViewHolder);
    return easyViewHolder;
  }

  protected void bindListeners(EasyViewHolder easyViewHolder) {
    if (easyViewHolder != null) {
      easyViewHolder.setItemClickListener(itemClickListener);
      easyViewHolder.setLongClickListener(longClickListener);
    }
  }

  @SuppressWarnings("unchecked") @Override
  public void onBindViewHolder(EasyViewHolder holder, int position) {
    holder.bindTo(position, dataList.get(position));
  }

  @Override
  public int getItemViewType(int position) {
    return viewHolderFactory.itemViewType(dataList.get(position));
  }

  @Override
  public int getItemCount() {
    return dataList.size();
  }

  public void add(Object object, int position) {
    dataList.add(position, object);
    notifyItemInserted(position);
  }

  public void add(Object object) {
    dataList.add(object);
    notifyItemInserted(getIndex(object));
  }

  public void addAll(List<?> objects) {
    dataList.clear();
    appendAll(objects);
  }

  public void appendAll(List<?> objects) {
    if (objects == null) {
      throw new IllegalArgumentException("objects can not be null");
    }
    List<?> toAppends = filter(objects);
    final int toAppendSize = toAppends.size();
    if (toAppendSize <= 0) {
      return;
    }
    int prevSize = this.dataList.size();
    List<Object> data = new ArrayList<>(prevSize + toAppendSize);
    data.addAll(dataList);
    data.addAll(toAppends);
    dataList = data;
    notifyItemRangeInserted(prevSize, toAppends.size());
  }

  /**
   * 去掉重复数据
   */
  private List<?> filter(List<?> data) {
    List<Object> returnData = new ArrayList<>();
    List<?> localDataList = this.dataList;
    for (Object o : data) {
      if (!localDataList.contains(o)) {
        returnData.add(o);
      }
    }
    return returnData;
  }

  public boolean update(Object data, int position) {
    Object oldData = dataList.set(position, data);
    if (oldData != null) {
      notifyItemChanged(position);
    }
    return oldData != null;
  }

  public boolean remove(Object data) {
    return dataList.contains(data) && remove(getIndex(data));
  }

  public boolean remove(int position) {
    boolean validIndex = isValidIndex(position);
    if (validIndex) {
      dataList.remove(position);
      notifyItemRemoved(position);
    }
    return validIndex;
  }

  public void clear() {
    dataList.clear();
    notifyDataSetChanged();
  }

  public List<Object> getItems() {
    return dataList;
  }

  public Object get(int position) {
    return dataList.get(position);
  }

  public int getIndex(Object item) {
    return dataList.indexOf(item);
  }

  public boolean isEmpty() {
    return getItemCount() == 0;
  }

  public void setOnClickListener(final OnItemClickListener listener) {
    this.itemClickListener = new DebouncedOnClickListener() {
      @Override
      public boolean onDebouncedClick(View v, int position) {
        if (listener != null) {
          listener.onItemClick(position, v);
        }
        return true;
      }
    };
  }

  public void setOnLongClickListener(final OnItemLongClickListener listener) {
    this.longClickListener = new DebouncedOnLongClickListener() {
      @Override
      public boolean onDebouncedClick(View v, int position) {
        return listener != null && listener.onLongItemClicked(position, v);
      }
    };
  }

  private boolean isValidIndex(int position) {
    return position >= 0 && position < getItemCount();
  }
}
