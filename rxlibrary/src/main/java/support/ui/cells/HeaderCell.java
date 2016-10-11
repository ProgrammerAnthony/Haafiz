package support.ui.cells;

import android.content.Context;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.FrameLayout;
import android.widget.TextView;

import support.ui.utilities.AndroidUtilities;
import support.ui.utilities.LayoutHelper;
import support.ui.utilities.LocaleController;

/**
 * Created by YuGang Yang on 04 07, 2016.
 * Copyright 2015-2016 qiji.tech. All rights reserved.
 */
public class HeaderCell extends FrameLayout {

  private TextView textView;

  public HeaderCell(Context context) {
    super(context);
    textView = new TextView(getContext());
    textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 15);
    textView.setTextColor(CellUtils.getHeaderColor(context));
    textView.setGravity((LocaleController.isRTL ? Gravity.RIGHT : Gravity.LEFT) | Gravity.CENTER_VERTICAL);
    addView(textView, LayoutHelper.createFrame(LayoutHelper.MATCH_PARENT, LayoutHelper.MATCH_PARENT, (LocaleController.isRTL ? Gravity.RIGHT : Gravity.LEFT) | Gravity.TOP, 17, 15, 17, 0));
  }

  @Override
  protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    super.onMeasure(MeasureSpec.makeMeasureSpec(MeasureSpec.getSize(widthMeasureSpec), MeasureSpec.EXACTLY),
        MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(38), MeasureSpec.EXACTLY));
  }

  public void setText(String text) {
    textView.setText(text);
  }
}