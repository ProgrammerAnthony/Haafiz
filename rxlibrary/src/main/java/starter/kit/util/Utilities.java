package starter.kit.util;

import android.support.v7.widget.RecyclerView;

public final class Utilities {

  public static boolean isAdapterEmpty(RecyclerView.Adapter adapter) {
    return isNotNull(adapter) && adapter.getItemCount() <= 0;
  }

  public static boolean isNotNull(Object object) {
    return !isNull(object);
  }

  public static boolean isNull(Object object) {
    return object == null;
  }
}
