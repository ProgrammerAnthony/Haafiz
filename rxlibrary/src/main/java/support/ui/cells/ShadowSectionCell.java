package support.ui.cells;

import android.content.Context;
import android.view.View;

import com.anthony.rxlibrary.R;

import support.ui.utilities.AndroidUtilities;

/**
 * Created by YuGang Yang on 04 07, 2016.
 * Copyright 2015-2016 qiji.tech. All rights reserved.
 */
public class ShadowSectionCell extends View {

  private int size = 12;

  public ShadowSectionCell(Context context) {
    super(context);
    setBackgroundResource(R.drawable.greydivider);
  }

  public void setSize(int value) {
    size = value;
  }

  @Override
  protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    super.onMeasure(MeasureSpec.makeMeasureSpec(MeasureSpec.getSize(widthMeasureSpec), MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(
        AndroidUtilities.dp(size), MeasureSpec.EXACTLY));
  }
}
