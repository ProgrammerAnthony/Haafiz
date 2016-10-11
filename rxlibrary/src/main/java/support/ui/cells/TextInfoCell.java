package support.ui.cells;

import android.content.Context;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.FrameLayout;
import android.widget.TextView;

import support.ui.utilities.AndroidUtilities;
import support.ui.utilities.LayoutHelper;

/**
 * Created by YuGang Yang on 04 07, 2016.
 * Copyright 2015-2016 qiji.tech. All rights reserved.
 */
public class TextInfoCell extends FrameLayout {

  private TextView textView;

  public TextInfoCell(Context context) {
    super(context);

    textView = new TextView(context);
    textView.setTextColor(CellUtils.getTextColor(context));
    textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 13);
    textView.setGravity(Gravity.CENTER);
    textView.setPadding(0, AndroidUtilities.dp(19), 0, AndroidUtilities.dp(19));
    addView(textView, LayoutHelper.createFrame(LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT, Gravity.CENTER, 17, 0, 17, 0));
  }

  @Override
  protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    super.onMeasure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
  }

  public void setText(String text) {
    textView.setText(text);
  }
}
