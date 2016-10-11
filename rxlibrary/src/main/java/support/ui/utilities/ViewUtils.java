/**
 * Created by YuGang Yang on September 23, 2015.
 * Copyright 2007-2015 Laputapp.com. All rights reserved.
 */
package support.ui.utilities;

import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.View;
import android.view.ViewTreeObserver;

import static android.view.View.GONE;
import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

/**
 * Utilities for working with the {@link View} class
 */
public class ViewUtils {

  /**
   * Set visibility of given view to be gone or visible
   * <p>
   * This method has no effect if the view visibility is currently invisible
   *
   * @param view View
   * @param gone boolean
   * @return view View
   */
  public static <V extends View> V setGone(final V view, final boolean gone) {
    if (view != null)
      if (gone) {
        if (GONE != view.getVisibility())
          view.setVisibility(GONE);
      } else {
        if (VISIBLE != view.getVisibility())
          view.setVisibility(VISIBLE);
      }
    return view;
  }

  /**
   * Set visibility of given view to be invisible or visible
   * <p>
   * This method has no effect if the view visibility is currently gone
   *
   * @param view View
   * @param invisible boolean
   * @return view View
   */
  public static <V extends View> V setInvisible(final V view,
                                                final boolean invisible) {
    if (view != null)
      if (invisible) {
        if (INVISIBLE != view.getVisibility())
          view.setVisibility(INVISIBLE);
      } else {
        if (VISIBLE != view.getVisibility())
          view.setVisibility(VISIBLE);
      }
    return view;
  }

  @SuppressWarnings("deprecation")
  public static void setBackground(View view, Drawable drawable) {
    if (BuildCompat.hasJellyBean()) {
      view.setBackground(drawable);
    } else {
      view.setBackgroundDrawable(drawable);
    }
  }

  @SuppressWarnings("deprecation")
  public static void removeOnGlobalLayoutListener(View view,
      ViewTreeObserver.OnGlobalLayoutListener listener) {
    if (Build.VERSION.SDK_INT < 16) {
      view.getViewTreeObserver().removeGlobalOnLayoutListener(listener);
    } else {
      view.getViewTreeObserver().removeOnGlobalLayoutListener(listener);
    }
  }

  private ViewUtils() {

  }
}
