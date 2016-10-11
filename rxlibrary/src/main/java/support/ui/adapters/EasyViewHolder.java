package support.ui.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;



public abstract class EasyViewHolder<V> extends RecyclerView.ViewHolder
    implements View.OnLongClickListener, View.OnClickListener {

  private OnItemClickListener itemClickListener;
  private OnItemLongClickListener longClickListener;

  public EasyViewHolder(Context context, ViewGroup parent, int layoutId) {
    this(LayoutInflater.from(context).inflate(layoutId, parent, false));
    bindListeners();
  }

  public EasyViewHolder(View itemView) {
    super(itemView);
    bindListeners();
  }

  void setItemClickListener(OnItemClickListener itemClickListener) {
    this.itemClickListener = itemClickListener;
  }

  void setLongClickListener(OnItemLongClickListener longClickListener) {
    this.longClickListener = longClickListener;
  }

  protected void bindListeners() {
    itemView.setOnClickListener(this);
    itemView.setOnLongClickListener(this);
  }

  protected void unbindListeners() {
    itemView.setOnClickListener(null);
    itemView.setOnLongClickListener(null);
  }

  @Override
  public boolean onLongClick(View v) {
    return longClickListener != null && longClickListener.onLongItemClicked(getAdapterPosition(),
        itemView);
  }

  @Override
  public void onClick(View v) {
    if (itemClickListener == null) return;
    itemClickListener.onItemClick(getAdapterPosition(), v);
  }

  public abstract void bindTo(int position, V value);

  public interface OnItemClickListener {
    void onItemClick(final int position, View view);
  }

  public interface OnItemLongClickListener {
    boolean onLongItemClicked(final int position, View view);
  }
}
