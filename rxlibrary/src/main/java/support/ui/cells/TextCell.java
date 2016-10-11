package support.ui.cells;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.anthony.rxlibrary.R;

import support.ui.utilities.AndroidUtilities;
import support.ui.utilities.LayoutHelper;

/**
 * Created by YuGang Yang on 04 07, 2016.
 * Copyright 2015-2016 qiji.tech. All rights reserved.
 */
public class TextCell extends FrameLayout {

  private TextView textView;
  private TextView valueTextView;
  private ImageView imageView;
  private ImageView valueImageView;

  private static Paint paint;
  private boolean needDivider;

  public TextCell(Context context) {
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
    textView.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
    addView(textView, LayoutHelper.createFrame(LayoutHelper.MATCH_PARENT, LayoutHelper.MATCH_PARENT,
        Gravity.LEFT | Gravity.TOP, 71, 0, 16, 0));

    valueTextView = new TextView(context);
    valueTextView.setTextColor(CellUtils.getValueColor(context));
    valueTextView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16);
    valueTextView.setLines(1);
    valueTextView.setMaxLines(1);
    valueTextView.setSingleLine(true);
    valueTextView.setGravity(Gravity.RIGHT | Gravity.CENTER_VERTICAL);
    addView(valueTextView, LayoutHelper.createFrame(LayoutHelper.WRAP_CONTENT, LayoutHelper.MATCH_PARENT,
        Gravity.RIGHT | Gravity.TOP, 0, 0,32, 0));

    imageView = new ImageView(context);
    imageView.setScaleType(ImageView.ScaleType.CENTER);
    addView(imageView, LayoutHelper.createFrame(LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT,
        Gravity.LEFT | Gravity.TOP, 16, 5, 0, 0));

    valueImageView = new ImageView(context);
    valueImageView.setScaleType(ImageView.ScaleType.CENTER);
    addView(valueImageView, LayoutHelper.createFrame(LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT,
        Gravity.RIGHT | Gravity.CENTER_VERTICAL, 0, 0, 16, 0));
  }

  @Override
  protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    super.onMeasure(MeasureSpec.makeMeasureSpec(MeasureSpec.getSize(widthMeasureSpec), MeasureSpec.EXACTLY),
        MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(48) + (needDivider ? 1 : 0), MeasureSpec.EXACTLY));
  }

  public void setTextColor(int color) {
    textView.setTextColor(color);
  }

  public void bindView(CellModel cellModel) {
    if (cellModel.backgroundResource != 0) {
      setBackgroundResource(cellModel.backgroundResource);
    } else {
      setBackgroundResource(R.drawable.list_selector_white);
    }
    if (!TextUtils.isEmpty(cellModel.text)) {
      textView.setText(cellModel.text);
      textView.setTextColor(CellUtils.getTextColor(getContext()));
    } else if (!TextUtils.isEmpty(cellModel.hint)) {
      textView.setText(cellModel.hint);
      textView.setTextColor(CellUtils.getHintColor(getContext()));
    }
    if (cellModel.textColor != 0) {
      textView.setTextColor(cellModel.textColor);
    }

    boolean paddingSet = false;
    if (cellModel.drawable != null) {
      imageView.setImageDrawable(cellModel.drawable);
      imageView.setVisibility(VISIBLE);
      imageView.setPadding(0, AndroidUtilities.dp(7), 0, 0);
      paddingSet = true;
    } else {
      imageView.setVisibility(INVISIBLE);
    }
    if (!TextUtils.isEmpty(cellModel.value)) {
      valueTextView.setText(cellModel.value);
      valueTextView.setVisibility(VISIBLE);
    } else {
      valueTextView.setVisibility(INVISIBLE);
    }
    if (cellModel.valueDrawable != null) {
      valueImageView.setImageDrawable(cellModel.valueDrawable);
      valueImageView.setVisibility(VISIBLE);
      if (!paddingSet) {
        imageView.setPadding(0, AndroidUtilities.dp(7), 0, 0);
      }
    } else if (cellModel.showArrow) {
      valueImageView.setImageResource(R.drawable.ic_arrow_right);
      valueImageView.setVisibility(VISIBLE);
      if (!paddingSet) {
        imageView.setPadding(0, AndroidUtilities.dp(7), 0, 0);
      }
    } else {
      valueImageView.setVisibility(INVISIBLE);
    }

    needDivider = cellModel.needDivider;
    setWillNotDraw(!cellModel.needDivider);
    requestLayout();
  }

  @Override
  protected void onDraw(Canvas canvas) {
    if (needDivider) {
      canvas.drawLine(AndroidUtilities.dp(72), getHeight() - 1, getWidth() - getPaddingRight(), getHeight() - 1, paint);
    }
  }

}
