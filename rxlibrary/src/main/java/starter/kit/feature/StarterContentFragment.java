package starter.kit.feature;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import nucleus.presenter.Presenter;
import support.ui.content.ContentPresenter;
import support.ui.content.EmptyView;
import support.ui.content.ErrorView;
import support.ui.content.ReflectionContentPresenterFactory;
import support.ui.content.RequiresContent;

@RequiresContent public abstract class StarterContentFragment<P extends Presenter>
    extends StarterFragment<P>
    implements EmptyView.OnEmptyViewClickListener,
    ErrorView.OnErrorViewClickListener {

  private ReflectionContentPresenterFactory factory =
      ReflectionContentPresenterFactory.fromViewClass(getClass());
  private ContentPresenter contentPresenter;

  @Override
  public void onCreate(Bundle bundle) {
    super.onCreate(bundle);

    contentPresenter = factory.createContentPresenter();
    contentPresenter.onCreate(getContext());

    contentPresenter.setOnEmptyViewClickListener(this);
    contentPresenter.setOnErrorViewClickListener(this);
  }

  @Override
  public void onResume() {
    super.onResume();
    contentPresenter.attachContainer(provideContainer((ViewGroup) getView()));
    contentPresenter.attachContentView(provideContentView());
  }

  @Override
  public void onPause() {
    super.onPause();
    contentPresenter.onDestroyView();
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    contentPresenter.onDestroy();
    contentPresenter = null;
  }

  public ContentPresenter getContentPresenter() {
    return contentPresenter;
  }

  public ViewGroup provideContainer(ViewGroup view) {
    return view;
  }

  public abstract View provideContentView();
}
