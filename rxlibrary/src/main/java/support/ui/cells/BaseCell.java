package support.ui.cells;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.HapticFeedbackConstants;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;

/**
 * Created by YuGang Yang on 04 07, 2016.
 * Copyright 2015-2016 honc.tech. All rights reserved.
 */
public class BaseCell extends View {

  private final class CheckForTap implements Runnable {
    @Override
    public void run() {
      if (pendingCheckForLongPress == null) {
        pendingCheckForLongPress = new CheckForLongPress();
      }
      pendingCheckForLongPress.currentPressCount = ++pressCount;
      postDelayed(pendingCheckForLongPress, ViewConfiguration.getLongPressTimeout() - ViewConfiguration.getTapTimeout());
    }
  }

  class CheckForLongPress implements Runnable {
    public int currentPressCount;

    @Override
    public void run() {
      if (checkingForLongPress && getParent() != null && currentPressCount == pressCount) {
        checkingForLongPress = false;
        performHapticFeedback(HapticFeedbackConstants.LONG_PRESS);
        onLongPress();
        MotionEvent event = MotionEvent.obtain(0, 0, MotionEvent.ACTION_CANCEL, 0, 0, 0);
        onTouchEvent(event);
        event.recycle();
      }
    }
  }

  private boolean checkingForLongPress = false;
  private CheckForLongPress pendingCheckForLongPress = null;
  private int pressCount = 0;
  private CheckForTap pendingCheckForTap = null;

  public BaseCell(Context context) {
    super(context);
  }

  protected void setDrawableBounds(Drawable drawable, int x, int y) {
    setDrawableBounds(drawable, x, y, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
  }

  protected void setDrawableBounds(Drawable drawable, int x, int y, int w, int h) {
    if (drawable != null) {
      drawable.setBounds(x, y, x + w, y + h);
    }
  }

  protected void startCheckLongPress() {
    if (checkingForLongPress) {
      return;
    }
    checkingForLongPress = true;
    if (pendingCheckForTap == null) {
      pendingCheckForTap = new CheckForTap();
    }
    postDelayed(pendingCheckForTap, ViewConfiguration.getTapTimeout());
  }

  protected void cancelCheckLongPress() {
    checkingForLongPress = false;
    if (pendingCheckForLongPress != null) {
      removeCallbacks(pendingCheckForLongPress);
    }
    if (pendingCheckForTap != null) {
      removeCallbacks(pendingCheckForTap);
    }
  }

  @Override
  public boolean hasOverlappingRendering() {
    return false;
  }

  protected void onLongPress() {

  }
}
