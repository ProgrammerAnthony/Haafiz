package support.ui.content;

import android.content.Context;
import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.ButterKnife;
import starter.kit.rx.R;
import support.ui.utilities.ViewUtils;

public class DefaultErrorView extends FrameLayout implements ErrorView, View.OnClickListener {

  private ImageView imageView;
  private TextView titleTextView;
  private TextView subtitleTextView;

  private OnErrorViewClickListener listener;

  public DefaultErrorView(Context context) {
    super(context);
    initialize(context);
  }

  private void initialize(Context context) {
    View view = LayoutInflater.from(context).inflate(R.layout.support_ui_view_error, this, false);
    addView(view);
  }

  @Override
  protected void onAttachedToWindow() {
    super.onAttachedToWindow();
    setOnClickListener(this);
  }

  @Override
  protected void onDetachedFromWindow() {
    super.onDetachedFromWindow();
    setOnClickListener(null);
    listener = null;
  }

  @Override
  public DefaultErrorView buildErrorImageView(@DrawableRes int drawableRes) {
    if (imageView() != null) {
      imageView().setImageResource(drawableRes);
    }
    return this;
  }

  @Override
  public DefaultErrorView buildErrorTitle(@StringRes int stringRes) {
    return buildErrorTitle(getContext().getString(stringRes));
  }

  @Override
  public DefaultErrorView buildErrorTitle(String title) {
    if (titleTextView() != null) {
      titleTextView().setText(title);
    }
    return this;
  }

  @Override
  public DefaultErrorView buildErrorSubtitle(@StringRes int stringRes) {
    return buildErrorSubtitle(getContext().getString(stringRes));
  }

  @Override
  public DefaultErrorView buildErrorSubtitle(String subtitle) {
    if (subtitleTextView() != null) {
      subtitleTextView().setText(subtitle);
    }
    return this;
  }

  @Override
  public ErrorView shouldDisplayErrorSubtitle(boolean display) {
    ViewUtils.setGone(subtitleTextView(), !display);
    return this;
  }

  @Override
  public ErrorView shouldDisplayErrorTitle(boolean display) {
    ViewUtils.setGone(titleTextView(), !display);
    return this;
  }

  @Override
  public ErrorView shouldDisplayErrorImageView(boolean display) {
    ViewUtils.setGone(imageView(), !display);
    return this;
  }

  public TextView titleTextView() {
    if (titleTextView == null) {
      titleTextView = ButterKnife.findById(this, R.id.support_ui_error_title);
    }
    return titleTextView;
  }

  public TextView subtitleTextView() {
    if (subtitleTextView == null) {
      subtitleTextView = ButterKnife.findById(this, R.id.support_ui_error_subtitle);
    }
    return subtitleTextView;
  }

  public ImageView imageView() {
    if (imageView == null) {
      imageView = ButterKnife.findById(this, R.id.support_ui_error_image_view);
    }
    return imageView;
  }

  @Override
  public void onClick(View v) {
    if (listener != null) {
      listener.onErrorViewClick(v);
    }
  }

  @Override
  public void setOnErrorViewClickListener(OnErrorViewClickListener listener) {
    this.listener = listener;
  }
}
