package support.ui.cells;

import android.content.Context;
import android.widget.FrameLayout;

/**
 * Created by YuGang Yang on 04 07, 2016.
 * Copyright 2015-2016 qiji.tech. All rights reserved.
 */
public class EmptyCell extends FrameLayout {

  int cellHeight;

  public EmptyCell(Context context) {
    this(context, 8);
  }

  public EmptyCell(Context context, int height) {
    super(context);
    cellHeight = height;
  }

  public void setHeight(int height) {
    cellHeight = height;
    requestLayout();
  }

  @Override
  protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    super.onMeasure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(cellHeight, MeasureSpec.EXACTLY));
  }
}
