package support.ui.adapters.debounced;

import android.view.View;

import support.ui.adapters.EasyViewHolder;

public abstract class DebouncedOnLongClickListener
    implements DebouncedListener, EasyViewHolder.OnItemLongClickListener {
  private final DebouncedClickHandler debouncedClickHandler;
  protected DebouncedOnLongClickListener() {
    this.debouncedClickHandler = new DebouncedClickHandler(this);
  }
  @Override
  public boolean onLongItemClicked(int position, View view) {
    return debouncedClickHandler.invokeDebouncedClick(position, view);
  }
}
