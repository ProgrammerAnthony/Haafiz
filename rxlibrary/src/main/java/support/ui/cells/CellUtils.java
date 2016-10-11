package support.ui.cells;

import android.content.Context;

import com.anthony.rxlibrary.R;

import support.ui.utilities.ThemeCompat;

/**
 * Created by YuGang Yang on 04 08, 2016.
 * Copyright 2015-2016 qiji.tech. All rights reserved.
 */
public final class CellUtils {
  private CellUtils() {

  }

  public static int getHintColor(Context context) {
    return ThemeCompat.getThemeAttrColor(context, R.attr.cellTextHintColor, 0xff8a8a8a);
  }

  public static int getTextColor(Context context) {
    return ThemeCompat.getThemeAttrColor(context, R.attr.cellTextColor, 0xff333333);
  }

  public static int getHeaderColor(Context context) {
    return ThemeCompat.getThemeAttrColor(context, R.attr.cellHeaderColor, 0xff159588);
  }

  public static int getDetailColor(Context context) {
    return ThemeCompat.getThemeAttrColor(context, R.attr.cellDetailColor, 0xff8a8a8a);
  }

  public static int getValueColor(Context context) {

    return ThemeCompat.getThemeAttrColor(context, R.attr.cellValueColor, 0xff8a8a8a);
  }

  public static int getInfoColor(Context context) {
    return ThemeCompat.getThemeAttrColor(context, R.attr.cellInfoColor, 0xff808080);
  }

  public static int getLinkTextColor(Context context) {
    return ThemeCompat.getThemeAttrColor(context, R.attr.cellLinkTextColor, 0xff316f9f);
  }

  public static int getDividerColor(Context context) {
    return ThemeCompat.getThemeAttrColor(context, R.attr.cellDividerColor, 0xffd9d9d9);
  }
}
