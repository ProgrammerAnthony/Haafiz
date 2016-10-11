package support.ui.cells;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import support.ui.utilities.AndroidUtilities;
import support.ui.utilities.LayoutHelper;
import support.ui.utilities.LocaleController;
import support.ui.widget.FrameLayoutFixed;

/**
 * Created by YuGang Yang on 04 07, 2016.
 * Copyright 2015-2016 qiji.tech. All rights reserved.
 */
public class TextDetailSettingsCell extends FrameLayoutFixed {

  private TextView textView;
  private TextView valueTextView;
  private static Paint paint;
  private boolean needDivider;
  private boolean multiline;

  public TextDetailSettingsCell(Context context) {
    super(context);

    if (paint == null) {
      paint = new Paint();
      paint.setColor(CellUtils.getDividerColor(context));
      paint.setStrokeWidth(1);
    }

    textView = new TextView(context);
    textView.setTextColor(CellUtils.getTextColor(context));
    textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16);
    textView.setLines(1);
    textView.setMaxLines(1);
    textView.setSingleLine(true);
    textView.setGravity((LocaleController.isRTL ? Gravity.RIGHT : Gravity.LEFT) | Gravity.CENTER_VERTICAL);
    addView(textView, LayoutHelper.createFrame(LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT, (LocaleController.isRTL ? Gravity.RIGHT : Gravity.LEFT) | Gravity.TOP, 17, 10, 17, 0));

    valueTextView = new TextView(context);
    valueTextView.setTextColor(CellUtils.getValueColor(context));
    valueTextView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 13);
    valueTextView.setGravity(LocaleController.isRTL ? Gravity.RIGHT : Gravity.LEFT);
    valueTextView.setLines(1);
    valueTextView.setMaxLines(1);
    valueTextView.setSingleLine(true);
    valueTextView.setPadding(0, 0, 0, 0);
    addView(valueTextView, LayoutHelper.createFrame(LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT, (LocaleController.isRTL ? Gravity.RIGHT : Gravity.LEFT) | Gravity.TOP, 17, 35, 17, 0));
  }

  @Override
  protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    if (!multiline) {
      super.onMeasure(View.MeasureSpec.makeMeasureSpec(View.MeasureSpec.getSize(widthMeasureSpec), View.MeasureSpec.EXACTLY),
          View.MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(64) + (needDivider ? 1 : 0), View.MeasureSpec.EXACTLY));
    } else {
      super.onMeasure(View.MeasureSpec.makeMeasureSpec(View.MeasureSpec.getSize(widthMeasureSpec), View.MeasureSpec.EXACTLY),
          View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
    }
  }

  public void setMultilineDetail(boolean value) {
    multiline = value;
    if (value) {
      valueTextView.setLines(0);
      valueTextView.setMaxLines(0);
      valueTextView.setSingleLine(false);
      valueTextView.setPadding(0, 0, 0, AndroidUtilities.dp(12));
    } else {
      valueTextView.setLines(1);
      valueTextView.setMaxLines(1);
      valueTextView.setSingleLine(true);
      valueTextView.setPadding(0, 0, 0, 0);
    }
  }

  public void setTextAndValue(String text, String value, boolean divider) {
    textView.setText(text);
    valueTextView.setText(value);
    needDivider = divider;
    setWillNotDraw(!divider);
  }

  @Override
  protected void onDraw(Canvas canvas) {
    if (needDivider) {
      canvas.drawLine(getPaddingLeft(), getHeight() - 1, getWidth() - getPaddingRight(), getHeight() - 1, paint);
    }
  }
}
