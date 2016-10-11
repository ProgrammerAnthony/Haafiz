package support.ui.utilities;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.SparseIntArray;

/**
 * Created by YuGang Yang on 04 08, 2016.
 * Copyright 2015-2016 qiji.tech. All rights reserved.
 */
public final class ThemeCompat {

  private ThemeCompat() {
  }

  private static final int[] TEMP_ARRAY = new int[1];
  private static final SparseIntArray sAttrArray = new SparseIntArray();

  public static int getThemeAttrColor(Context context, int attr) {
    return getThemeAttrColor(context, attr, 0);
  }

  public static int getThemeAttrColor(Context context, int attr, int defaultColor) {
    int attrColor = sAttrArray.get(attr);
    if (attrColor != 0) {
      return attrColor;
    }
    TEMP_ARRAY[0] = attr;
    TypedArray a = context.obtainStyledAttributes(null, TEMP_ARRAY);
    try {
      int color = a.getColor(0, defaultColor);
      sAttrArray.put(attr, color);
      return color;
    } finally {
      a.recycle();
    }
  }

  public static float getThemeAttrDimension(Context context, int attr) {
    TEMP_ARRAY[0] = attr;
    TypedArray a = context.obtainStyledAttributes(null, TEMP_ARRAY);
    try {
      return a.getDimension(0, 0);
    } finally {
      a.recycle();
    }
  }
}
