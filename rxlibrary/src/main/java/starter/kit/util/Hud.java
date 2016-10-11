package starter.kit.util;

import android.content.Context;

public class Hud {

  private static Hud SINGLETON = null;

  private HudInterface mHudInterface;

  private Hud(HudInterface hudInterface) {
    mHudInterface = hudInterface;
  }

  public static Hud initialize(HudInterface hudInterface) {
    SINGLETON = new Hud(hudInterface);
    return SINGLETON;
  }

  public static Hud getInstance() {
    if (SINGLETON == null) {
      SINGLETON = new Hud(new AbstractHud() {});
    }
    return SINGLETON;
  }

  public void showMessage(Context context, String msg) {
    if (mHudInterface != null) {
      mHudInterface.showMessage(context, msg);
    }
  }

  public void dismissHud() {
    if (mHudInterface != null) {
      mHudInterface.dismissHud();
    }
  }

  public void showHud(Context context) {
    showHud(context, null);
  }

  public void showHud(Context context, String message) {
    showHud(context, message, false, null);
  }

  public void showHud(Context context, String msg, HudCallback callback) {
    if (mHudInterface != null) {
      mHudInterface.showHud(context, msg, true, callback);
    }
  }

  public void showHud(Context context, String msg, boolean cancelable, HudCallback callback) {
    if (mHudInterface != null) {
      mHudInterface.showHud(context, msg, cancelable, callback);
    }
  }

  public interface HudInterface {
    void showMessage(Context context, String msg);
    void showHud(Context context, String msg, boolean cancelable, HudCallback callback);
    void dismissHud();
  }

  public static abstract class AbstractHud implements HudInterface{
    @Override
    public void showMessage(Context context, String msg) {

    }

    @Override
    public void showHud(Context context, String msg, boolean cancelable, HudCallback callback) {

    }

    @Override
    public void dismissHud() {

    }
  }

  public interface HudCallback {
    void onDismissed();
  }
}
