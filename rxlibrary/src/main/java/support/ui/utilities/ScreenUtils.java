package support.ui.utilities;

import android.app.Activity;
import android.content.Context;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.view.Display;

import java.lang.reflect.Method;

import support.ui.app.SupportApp;

/**
 * Created by YuGang Yang on 04 13, 2016.
 * Copyright 2015-2016 qiji.tech. All rights reserved.
 */
public final class ScreenUtils {

  private static DisplayMetrics metrics;

  public static DisplayMetrics getDisplayMetrics() {
    if( metrics == null ) {
      metrics = SupportApp.appResources().getDisplayMetrics();
    }

    return metrics;
  }

  public static float getDensity() {
    if( metrics == null ) {
      getDisplayMetrics();
    }
    return metrics.density;
  }

  public static float getScaledDensity() {
    if( metrics == null ) {
      getDisplayMetrics();
    }
    return metrics.scaledDensity;
  }

  public static int getScreenWidth() {
    if( metrics == null ) {
      getDisplayMetrics();
    }
    return metrics.widthPixels;
  }

  public static int getScreenHeight() {
    if( metrics == null ) {
      getDisplayMetrics();
    }
    return metrics.heightPixels;
  }

  public static int px2dp(float pxValue) {
    if( metrics == null ) {
      getDisplayMetrics();
    }
    return (int) (pxValue / metrics.density + 0.5f);
  }

  public static int dp2px(float dipValue) {
    if( metrics == null ) {
      getDisplayMetrics();
    }
    return (int) (dipValue * metrics.density + 0.5f);
  }

  public static int dp2px(float dpValue, Context context) {
    final float scale = context.getResources().getDisplayMetrics().density;
    return (int) (dpValue * scale + 0.5f);
  }

  public static int px2sp(float pxValue) {
    if( metrics == null ) {
      getDisplayMetrics();
    }
    return (int) (pxValue / metrics.scaledDensity + 0.5f);
  }

  public static int sp2px(float spValue) {
    if( metrics == null ) {
      getDisplayMetrics();
    }
    return (int) (spValue * metrics.scaledDensity + 0.5f);
  }

  public static float getTextLength(float textSize, String text) {
    Paint paint = new Paint();
    paint.setTextSize(textSize);
    return paint.measureText(text);
  }

  /**
   * 获取实际屏幕高度
   * 如 1920 * 1080
   * @param activity Activity
   * @return int[]
   */
  public static int[] getRealMetrics(Activity activity) {
    int[] dpi = new int[2];
    Display display = activity.getWindowManager().getDefaultDisplay();
    DisplayMetrics dm = new DisplayMetrics();
    @SuppressWarnings("rawtypes") Class c;
    try {
      c = Class.forName("android.view.Display");
      @SuppressWarnings("unchecked") Method method =
          c.getMethod("getRealMetrics", DisplayMetrics.class);
      method.invoke(display, dm);
      dpi[0] = dm.widthPixels;
      dpi[1] = dm.heightPixels;
    } catch (Exception e) {
      e.printStackTrace();
    }
    return dpi;
  }

  /**
   *
   * @param activity Activity
   * @return int
   */
  public static int getStatusHeight(Activity activity) {
    Rect rect = new Rect();
    activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
    return rect.top;
  }
}
