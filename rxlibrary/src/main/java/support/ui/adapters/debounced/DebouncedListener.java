package support.ui.adapters.debounced;

import android.view.View;

public interface DebouncedListener {
  boolean onDebouncedClick(View v, int position);
}
