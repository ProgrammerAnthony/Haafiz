package support.ui.utilities;

import android.os.Build;

/**
 * Created by YuGang Yang on 04 13, 2016.
 * Copyright 2015-2016 qiji.tech. All rights reserved.
 */
public class BuildCompat {

  public static boolean hasICS() {
    return Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH;
  }

  public static boolean hasHoneycomb() {
    return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB;
  }

  public static boolean hasHoneycombMR1() {
    return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1;
  }

  public static boolean hasJellyBean() {
    return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN;
  }

  public static boolean hasJellyBeanMR1() {
    return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1;
  }

  public static boolean hasLollipop() {
    return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
  }

  public static boolean hasLollipopMR1() {
    return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1;
  }

  public static boolean hasM() {
    return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
  }
}
