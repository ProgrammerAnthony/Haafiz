package support.ui.utilities;

import android.content.Context;
import android.widget.Toast;

import support.ui.app.SupportApp;

public final class ToastUtils {

  private ToastUtils() {

  }

  public static void toast(int textResId) {
    toast(SupportApp.getInstance().getString(textResId));
  }

  public static void toast(String text) {
    toast(SupportApp.appContext(), text);
  }

  public static void toast(Context context, int textResId) {
    toast(context, context.getString(textResId));
  }

  public static void toast(Context context, String text) {
    Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
  }
}
