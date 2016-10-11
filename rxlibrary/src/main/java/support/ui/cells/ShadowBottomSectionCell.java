package support.ui.cells;

import android.content.Context;
import android.view.View;

import com.anthony.rxlibrary.R;

import support.ui.utilities.AndroidUtilities;

/**
 * Created by YuGang Yang on 04 07, 2016.
 * Copyright 2015-2016 qiji.tech. All rights reserved.
 */
public class ShadowBottomSectionCell extends View {

  public ShadowBottomSectionCell(Context context) {
    super(context);
    setBackgroundResource(R.drawable.greydivider_bottom);
  }

  @Override
  protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    super.onMeasure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(6), MeasureSpec.EXACTLY));
  }
}
