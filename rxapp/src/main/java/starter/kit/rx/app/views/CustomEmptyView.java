package starter.kit.rx.app.views;

import android.content.Context;
import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import starter.kit.rx.app.R;
import support.ui.content.EmptyView;

public class CustomEmptyView extends FrameLayout implements EmptyView, View.OnClickListener {

  private OnEmptyViewClickListener listener;

  public CustomEmptyView(Context context) {
    super(context);
    initialize(context);
  }

  private void initialize(Context context) {
    View view = LayoutInflater.from(context).inflate(R.layout.view_custom_empty, this, false);
    addView(view);
  }

  @Override public EmptyView buildEmptyImageView(@DrawableRes int drawableRes) {
    return null;
  }

  @Override public EmptyView buildEmptyTitle(@StringRes int stringRes) {
    return null;
  }

  @Override public EmptyView buildEmptyTitle(String title) {
    return null;
  }

  @Override public EmptyView buildEmptySubtitle(@StringRes int stringRes) {
    return null;
  }

  @Override public EmptyView buildEmptySubtitle(String subtitle) {
    return null;
  }

  @Override public EmptyView shouldDisplayEmptySubtitle(boolean display) {
    return null;
  }

  @Override public EmptyView shouldDisplayEmptyTitle(boolean display) {
    return null;
  }

  @Override public EmptyView shouldDisplayEmptyImageView(boolean display) {
    return null;
  }

  @Override protected void onAttachedToWindow() {
    super.onAttachedToWindow();
    setOnClickListener(this);
  }

  @Override protected void onDetachedFromWindow() {
    super.onDetachedFromWindow();
    setOnClickListener(null);
    listener = null;
  }

  @Override public void onClick(View v) {
    if (listener != null) {
      listener.onEmptyViewClick(v);
    }
  }

  @Override public void setOnEmptyViewClickListener(OnEmptyViewClickListener listener) {
    this.listener = listener;
  }

}
