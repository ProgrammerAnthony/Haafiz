package support.ui.cells;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;

import support.ui.utilities.AndroidUtilities;

/**
 * Created by YuGang Yang on 04 07, 2016.
 * Copyright 2015-2016 qiji.tech. All rights reserved.
 */
public class DividerCell extends BaseCell {

  private static Paint paint;

  public DividerCell(Context context) {
    super(context);
    if (paint == null) {
      paint = new Paint();
      paint.setColor(CellUtils.getDividerColor(context));
      paint.setStrokeWidth(1);
    }
  }

  @Override
  protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec), AndroidUtilities.dp(16) + 1);
  }

  @Override
  protected void onDraw(Canvas canvas) {
    canvas.drawLine(getPaddingLeft(), AndroidUtilities.dp(8), getWidth() - getPaddingRight(), AndroidUtilities.dp(8), paint);
  }
}
