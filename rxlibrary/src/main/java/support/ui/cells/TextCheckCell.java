package support.ui.cells;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.v7.widget.SwitchCompat;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import support.ui.utilities.AndroidUtilities;
import support.ui.utilities.LayoutHelper;
import support.ui.utilities.LocaleController;
import support.ui.widget.FrameLayoutFixed;

/**
 * Created by YuGang Yang on 04 07, 2016.
 * Copyright 2015-2016 qiji.tech. All rights reserved.
 */
public class TextCheckCell extends FrameLayoutFixed {

  private TextView textView;
  private TextView valueTextView;
  private SwitchCompat checkBox;
  private static Paint paint;
  private boolean needDivider;

  public TextCheckCell(Context context) {
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
    addView(textView, LayoutHelper.createFrame(LayoutHelper.MATCH_PARENT, LayoutHelper.MATCH_PARENT, (LocaleController.isRTL ? Gravity.RIGHT : Gravity.LEFT) | Gravity.TOP, 17, 0, 17, 0));

    valueTextView = new TextView(context);
    valueTextView.setTextColor(CellUtils.getValueColor(context));
    valueTextView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 13);
    valueTextView.setGravity(LocaleController.isRTL ? Gravity.RIGHT : Gravity.LEFT);
    valueTextView.setLines(1);
    valueTextView.setMaxLines(1);
    valueTextView.setSingleLine(true);
    valueTextView.setPadding(0, 0, 0, 0);
    addView(valueTextView, LayoutHelper.createFrame(LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT, (LocaleController.isRTL ? Gravity.RIGHT : Gravity.LEFT) | Gravity.TOP, 17, 35, 17, 0));

    checkBox = new SwitchCompat(context);
    checkBox.setDuplicateParentStateEnabled(false);
    checkBox.setFocusable(false);
    checkBox.setFocusableInTouchMode(false);
    checkBox.setClickable(false);
    checkBox.setBackgroundResource(0);
    addView(checkBox, LayoutHelper.createFrame(LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT, (LocaleController.isRTL ? Gravity.LEFT : Gravity.RIGHT) | Gravity.CENTER_VERTICAL, 14, 0, 14, 0));
  }

  @Override
  protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    super.onMeasure(View.MeasureSpec.makeMeasureSpec(View.MeasureSpec.getSize(widthMeasureSpec), View.MeasureSpec.EXACTLY),
        View.MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(valueTextView.getVisibility() == VISIBLE ? 64 : 48) + (needDivider ? 1 : 0), View.MeasureSpec.EXACTLY));
  }

  public void bindView(String text, String value, boolean checked, boolean divider) {
    textView.setText(text);
    checkBox.requestLayout();
    checkBox.setChecked(checked);
    needDivider = divider;

    if (!TextUtils.isEmpty(value)) {
      valueTextView.setText(value);
      valueTextView.setVisibility(VISIBLE);
    } else {
      valueTextView.setVisibility(GONE);
    }

    FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) textView.getLayoutParams();
    layoutParams.height = FrameLayout.LayoutParams.WRAP_CONTENT;
    layoutParams.topMargin = AndroidUtilities.dp(10);
    textView.setLayoutParams(layoutParams);
    setWillNotDraw(!divider);
  }

  public void setChecked(boolean checked) {
    checkBox.setChecked(checked);
  }

  @Override
  protected void onDraw(Canvas canvas) {
    if (needDivider) {
      canvas.drawLine(getPaddingLeft(), getHeight() - 1, getWidth() - getPaddingRight(), getHeight() - 1, paint);
    }
  }
}
