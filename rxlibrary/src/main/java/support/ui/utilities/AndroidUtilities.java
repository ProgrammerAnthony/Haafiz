package support.ui.utilities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.hardware.display.DisplayManagerCompat;
import android.support.v4.view.ViewCompat;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import java.util.Hashtable;

import support.ui.app.SupportApp;

/**
 * Created by YuGang Yang on 04 07, 2016.
 * Copyright 2015-2016 qiji.tech. All rights reserved.
 */
public final class AndroidUtilities {

  public static float density = 1;
  private static final Hashtable<String, Typeface> typefaceCache = new Hashtable<>();
  public static DisplayMetrics displayMetrics = new DisplayMetrics();
  public static Point displaySize = new Point();

  static {
    density = SupportApp.appResources().getDisplayMetrics().density;
    checkDisplaySize();
  }

  @SuppressWarnings("deprecation")
  public static void checkDisplaySize() {
    try {
      Display[] displays = DisplayManagerCompat.getInstance(SupportApp.appContext()).getDisplays();
      if (displays != null) {
        if (displays.length == 1) {
          Display display = displays[0];
          display.getMetrics(displayMetrics);
          if (android.os.Build.VERSION.SDK_INT < 13) {
            displaySize.set(display.getWidth(), display.getHeight());
          } else {
            display.getSize(displaySize);
          }
          FileLog.e("tmessages", "display size = " + displaySize.x + " " + displaySize.y + " " + displayMetrics.xdpi + "x" + displayMetrics.ydpi);
        }
      }
    } catch (Exception e) {
      FileLog.e("tmessages", e);
    }
  }

  public static int dp(float value) {
    if (value == 0) {
      return 0;
    }
    return (int) Math.ceil(density * value);
  }

  public static float dpf2(float value) {
    if (value == 0) {
      return 0;
    }
    return density * value;
  }

  public static Typeface getTypeface(String assetPath) {
    synchronized (typefaceCache) {
      if (!typefaceCache.containsKey(assetPath)) {
        try {
          Typeface t = Typeface.createFromAsset(SupportApp.appContext().getAssets(), assetPath);
          typefaceCache.put(assetPath, t);
        } catch (Exception e) {
          FileLog.e("Typefaces", "Could not get typeface '" + assetPath + "' because " + e.getMessage());
          return null;
        }
      }
      return typefaceCache.get(assetPath);
    }
  }

  public static void showKeyboard(View view) {
    if (view == null) {
      return;
    }
    InputMethodManager inputManager = (InputMethodManager)view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
    inputManager.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
  }

  public static boolean isKeyboardShowed(View view) {
    if (view == null) {
      return false;
    }
    InputMethodManager inputManager = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
    return inputManager.isActive(view);
  }

  public static void hideKeyboard(View view) {
    if (view == null) {
      return;
    }
    InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
    if (!imm.isActive()) {
      return;
    }
    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
  }

  public static void runOnUIThread(Runnable runnable) {
    runOnUIThread(runnable, 0);
  }

  public static void runOnUIThread(Runnable runnable, long delay) {
    if (delay == 0) {
      SupportApp.appHandler().post(runnable);
    } else {
      SupportApp.appHandler().postDelayed(runnable, delay);
    }
  }

  public static void cancelRunOnUIThread(Runnable runnable) {
    SupportApp.appHandler().removeCallbacks(runnable);
  }

  public static void shakeView(final View view, final float x, final int num) {
    if (num == 6) {
      ViewCompat.setTranslationX(view, 0);
      view.clearAnimation();
      return;
    }
    AnimatorSet animatorSet = new AnimatorSet();
    animatorSet.playTogether(ObjectAnimator.ofFloat(view, "translationX", AndroidUtilities.dp(x)));
    animatorSet.setDuration(50);
    animatorSet.addListener(new AnimatorListenerAdapter() {
      @Override
      public void onAnimationEnd(Animator animation) {
        shakeView(view, num == 5 ? 0 : -x, num + 1);
      }
    });
    animatorSet.start();
  }

  /**
   * 获取AndroidManifest中配置的meta-data
   * @param context Context
   * @param key String
   * @return String
   */
  public static String getMetaData(Context context, String key) {
    Bundle metaData = null;
    String value = null;
    if (context == null || key == null) {
      return null;
    }
    try {
      ApplicationInfo ai = context.getPackageManager().getApplicationInfo(
          context.getPackageName(), PackageManager.GET_META_DATA);
      if (null != ai) {
        metaData = ai.metaData;
      }
      if (null != metaData) {
        value = metaData.getString(key);
      }
    } catch (PackageManager.NameNotFoundException e) {
      // Nothing to do
    }
    return value;
  }

}
