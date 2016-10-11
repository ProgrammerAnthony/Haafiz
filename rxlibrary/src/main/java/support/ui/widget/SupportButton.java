package support.ui.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.RippleDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.os.Build;
import android.support.v4.graphics.ColorUtils;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.widget.AppCompatButton;
import android.util.AttributeSet;

import com.anthony.rxlibrary.R;

import java.util.Arrays;

import support.ui.utilities.BuildCompat;
import support.ui.utilities.ThemeCompat;
import support.ui.utilities.ViewUtils;

/**
 * Created by YuGang Yang on 04 13, 2016.
 * Copyright 2015-2016 qiji.tech. All rights reserved.
 */
public class SupportButton extends AppCompatButton {

  private int mRippleColor;
  private int mPressedColor;
  private int mDisabledColor;
  private int mNormalColor;
  private float mCornerRadius;

  public SupportButton(Context context) {
    this(context, null);
  }

  public SupportButton(Context context, AttributeSet attrs) {
    this(context, attrs, R.attr.buttonStyle);
  }

  public SupportButton(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    initialize(context);
    parseAttrs(context, attrs);
    if (BuildCompat.hasLollipop()) {
      ViewUtils.setBackground(this, createRippleDrawable());
    } else {
      Drawable drawable = createRadiusBackground();
      drawable = DrawableCompat.wrap(drawable);
      DrawableCompat.setTintList(drawable, createButtonColorStateList());
      ViewUtils.setBackground(this, drawable);
    }
  }

  private void parseAttrs(Context context, AttributeSet attrs) {
    TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SupportButton);
    if (a == null) return;
    try {
      mRippleColor = a.getColor(R.styleable.SupportButton_supportButtonRippleColor, mRippleColor);
      mNormalColor = a.getColor(R.styleable.SupportButton_supportButtonNormalColor, mNormalColor);
      mPressedColor = ColorUtils.setAlphaComponent(mNormalColor, 200);
      mPressedColor = a.getColor(R.styleable.SupportButton_supportButtonPressedColor, mPressedColor);
      mDisabledColor = ColorUtils.setAlphaComponent(mNormalColor, 150);
      mDisabledColor = a.getColor(R.styleable.SupportButton_supportButtonDisabledColor, mDisabledColor);
      mCornerRadius = a.getDimension(R.styleable.SupportButton_supportButtonCornerRadius, 0);
    } finally {
      a.recycle();
    }
  }

  protected ColorStateList createButtonColorStateList() {
    int[][] states = new int[][] {
        new int[] { -android.R.attr.state_enabled }, // disabled
        new int[] { android.R.attr.state_pressed },  // pressed
        new int[] { android.R.attr.state_focused }, // focused
        new int[] { android.R.attr.state_enabled }, // enabled
    };
    int[] colors = new int[] {
        mDisabledColor,
        mPressedColor,
        mPressedColor,
        mNormalColor,
    };
    return new ColorStateList(states, colors);
  }

  @TargetApi(Build.VERSION_CODES.LOLLIPOP) private Drawable createRippleDrawable() {
    ColorStateList color = ColorStateList.valueOf(mRippleColor);
    return new RippleDrawable(color, createRippleContentDrawable(), createRippleMaskDrawable());
  }

  @TargetApi(Build.VERSION_CODES.LOLLIPOP) protected ShapeDrawable createRippleContentDrawable() {
    ShapeDrawable shapeDrawable = createRadiusBackground();
    shapeDrawable.setTintList(createRippleColorStateList());
    return shapeDrawable;
  }

  protected ColorStateList createRippleColorStateList() {
    int[][] states = new int[][] {
        new int[] { -android.R.attr.state_enabled }, // disabled
        new int[] { android.R.attr.state_enabled }, // enabled
    };
    int[] colors = new int[] {
        mDisabledColor,
        mNormalColor,
    };
    return new ColorStateList(states, colors);
  }

  @TargetApi(Build.VERSION_CODES.LOLLIPOP) protected ShapeDrawable createRippleMaskDrawable() {
    ShapeDrawable shapeDrawable = createRadiusBackground();
    shapeDrawable.setTintList(createRippleColorStateList());
    return shapeDrawable;
  }

  protected ShapeDrawable createRadiusBackground() {
    float[] outerRadius = new float[8];
    Arrays.fill(outerRadius, mCornerRadius);
    return new ShapeDrawable(new RoundRectShape(outerRadius, null, null));
  }

  private void initialize(Context context) {
    mNormalColor = ThemeCompat.getThemeAttrColor(context, R.attr.colorPrimary);
    mRippleColor = ThemeCompat.getThemeAttrColor(context, R.attr.colorAccent);
  }
}
