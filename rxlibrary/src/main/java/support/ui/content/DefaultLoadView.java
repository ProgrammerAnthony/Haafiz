package support.ui.content;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import starter.kit.rx.R;

public class DefaultLoadView extends FrameLayout {

  public DefaultLoadView(Context context) {
    super(context);
    initialize(context);
  }

  private void initialize(Context context) {
    View view = LayoutInflater.from(context).inflate(R.layout.support_ui_content_load, this, false);
    addView(view);
  }
}
