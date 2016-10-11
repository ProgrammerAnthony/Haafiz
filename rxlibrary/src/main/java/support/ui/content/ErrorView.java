package support.ui.content;

import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;
import android.view.View;

public interface ErrorView {
  ErrorView buildErrorImageView(@DrawableRes int drawableRes);

  ErrorView buildErrorTitle(@StringRes int stringRes);

  ErrorView buildErrorTitle(String title);

  ErrorView buildErrorSubtitle(@StringRes int stringRes);

  ErrorView buildErrorSubtitle(String subtitle);

  ErrorView shouldDisplayErrorSubtitle(boolean display);

  ErrorView shouldDisplayErrorTitle(boolean display);

  ErrorView shouldDisplayErrorImageView(boolean display);

  void setOnErrorViewClickListener(OnErrorViewClickListener listener);

  interface OnErrorViewClickListener {
    void onErrorViewClick(View view);
  }
}
