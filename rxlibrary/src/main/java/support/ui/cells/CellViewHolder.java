package support.ui.cells;

import android.view.View;

import com.anthony.rxlibrary.R;

import support.ui.adapters.EasyViewHolder;

/**
 * Created by YuGang Yang on 04 07, 2016.
 * Copyright 2015-2016 qiji.tech. All rights reserved.
 */
public class CellViewHolder extends EasyViewHolder<CellModel> implements View.OnClickListener {

  public CellViewHolder(View itemView) {
    super(itemView);
    setBackground();
  }

  @Override
  public void bindTo(int position, CellModel value) {
    if (value.enabled) {
      bindListeners();
    } else {
      unbindListeners();
    }
  }

  private void setBackground() {
    if (itemView.getBackground() == null) {
      itemView.setBackgroundResource(R.drawable.list_selector_white);
    }
  }
}
