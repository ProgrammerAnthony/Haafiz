package support.ui.content;

import android.support.annotation.Nullable;

public final class ContentPresenterLifecycleDelegate {

  @Nullable
  private ContentPresenterFactory presenterFactory;
  @Nullable
  private ContentPresenter presenter;

  public ContentPresenterLifecycleDelegate(@Nullable ContentPresenterFactory presenterFactory) {
    this.presenterFactory = presenterFactory;
  }

  public ContentPresenter getPresenter() {
    if (presenterFactory != null) {
      if (presenter == null) {
        presenter = presenterFactory.createContentPresenter();
      }
    }
    return presenter;
  }
}
