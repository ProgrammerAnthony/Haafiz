package support.ui.cells;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import support.ui.utilities.AndroidUtilities;
import support.ui.utilities.LayoutHelper;
import support.ui.utilities.LocaleController;

/**
 * Created by YuGang Yang on 04 07, 2016.
 * Copyright 2015-2016 qiji.tech. All rights reserved.
 */
public class TextSettingsCell extends FrameLayout {

  private TextView textView;
  private TextView valueTextView;
  private ImageView valueImageView;
  private static Paint paint;
  private boolean needDivider;

  public TextSettingsCell(Context context) {
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
    textView.setEllipsize(TextUtils.TruncateAt.END);
    textView.setGravity((LocaleController.isRTL ? Gravity.RIGHT : Gravity.LEFT) | Gravity.CENTER_VERTICAL);
    addView(textView, LayoutHelper.createFrame(LayoutHelper.MATCH_PARENT, LayoutHelper.MATCH_PARENT, (LocaleController.isRTL ? Gravity.RIGHT : Gravity.LEFT) | Gravity.TOP, 17, 0, 17, 0));

    valueTextView = new TextView(context);
    valueTextView.setTextColor(CellUtils.getValueColor(context));
    valueTextView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16);
    valueTextView.setLines(1);
    valueTextView.setMaxLines(1);
    valueTextView.setSingleLine(true);
    valueTextView.setEllipsize(TextUtils.TruncateAt.END);
    valueTextView.setGravity((LocaleController.isRTL ? Gravity.LEFT : Gravity.RIGHT) | Gravity.CENTER_VERTICAL);
    addView(valueTextView, LayoutHelper.createFrame(LayoutHelper.WRAP_CONTENT, LayoutHelper.MATCH_PARENT, (LocaleController.isRTL ? Gravity.LEFT : Gravity.RIGHT) | Gravity.TOP, 17, 0, 17, 0));

    valueImageView = new ImageView(context);
    valueImageView.setScaleType(ImageView.ScaleType.CENTER);
    valueImageView.setVisibility(INVISIBLE);
    addView(valueImageView, LayoutHelper.createFrame(LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT, (LocaleController.isRTL ? Gravity.LEFT : Gravity.RIGHT) | Gravity.CENTER_VERTICAL, 17, 0, 17, 0));
  }

  @Override
  protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec), AndroidUtilities.dp(48) + (needDivider ? 1 : 0));

    int availableWidth = getMeasuredWidth() - getPaddingLeft() - getPaddingRight() - AndroidUtilities.dp(34);
    int width = availableWidth / 2;
    if (valueImageView.getVisibility() == VISIBLE) {
      valueImageView.measure(MeasureSpec.makeMeasureSpec(width, MeasureSpec.AT_MOST), MeasureSpec.makeMeasureSpec(getMeasuredHeight(), MeasureSpec.EXACTLY));
    }
    if (valueTextView.getVisibility() == VISIBLE) {
      valueTextView.measure(MeasureSpec.makeMeasureSpec(width, MeasureSpec.AT_MOST), MeasureSpec.makeMeasureSpec(getMeasuredHeight(), MeasureSpec.EXACTLY));
      width = availableWidth - valueTextView.getMeasuredWidth() - AndroidUtilities.dp(8);
    } else {
      width = availableWidth;
    }
    textView.measure(MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(getMeasuredHeight(), MeasureSpec.EXACTLY));
  }

  public void setTextColor(int color) {
    textView.setTextColor(color);
  }

  public void setTextValueColor(int color) {
    valueTextView.setTextColor(color);
  }

  public void bindView(String text, Drawable drawable, String value, boolean divider) {
    textView.setText(text);
    if (drawable != null) {
      valueImageView.setVisibility(VISIBLE);
      valueImageView.setImageDrawable(drawable);
    } else {
      valueImageView.setVisibility(INVISIBLE);
    }
    if (!TextUtils.isEmpty(value)) {
      valueTextView.setText(value);
      valueTextView.setVisibility(VISIBLE);
    } else {
      valueTextView.setVisibility(INVISIBLE);
    }
    needDivider = divider;
    setWillNotDraw(!divider);
    requestLayout();
  }

  @Override
  protected void onDraw(Canvas canvas) {
    if (needDivider) {
      canvas.drawLine(getPaddingLeft(), getHeight() - 1, getWidth() - getPaddingRight(), getHeight() - 1, paint);
    }
  }
}
