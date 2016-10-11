package starter.kit.rx.app.feature.content;

import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import butterknife.BindView;
import butterknife.OnClick;
import java.util.concurrent.TimeUnit;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import starter.kit.rx.app.R;
import starter.kit.feature.rx.RxStarterActivity;
import starter.kit.rx.app.views.CustomEmptyView;
import starter.kit.util.ProgressInterface;
import starter.kit.util.RxUtils;
import support.ui.content.ContentPresenter;
import support.ui.content.EmptyView;
import support.ui.content.ErrorView;
import support.ui.content.ReflectionContentPresenterFactory;
import support.ui.content.RequiresContent;

import static rx.android.schedulers.AndroidSchedulers.mainThread;

@RequiresContent(emptyView = CustomEmptyView.class) public class ContentActivity extends RxStarterActivity
    implements EmptyView.OnEmptyViewClickListener,
    ErrorView.OnErrorViewClickListener,
    ProgressInterface {

  ReflectionContentPresenterFactory factory =
      ReflectionContentPresenterFactory.fromViewClass(getClass());
  ContentPresenter contentPresenter;

  @BindView(R.id.container) FrameLayout container;
  @BindView(R.id.support_ui_content_view) ImageView contentView;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_content);

    contentPresenter = factory.createContentPresenter();
    contentPresenter.onCreate(this);
    contentPresenter.attachContainer(container);
    contentPresenter.attachContentView(contentView);
    contentPresenter.setOnEmptyViewClickListener(this);
    contentPresenter.setOnErrorViewClickListener(this);
  }

  @Override protected void onDestroy() {
    super.onDestroy();
    contentPresenter.onDestroy();
  }

  @SuppressWarnings("unused")
  @OnClick({
      R.id.btnLoad, R.id.btnEmpty, R.id.btnError, R.id.btnContent,
  }) public void onClick(View view) {
    switch (view.getId()) {
      case R.id.btnLoad:
        onRefresh();
        break;
      case R.id.btnEmpty:
        //contentPresenter.buildEmptyImageView(R.drawable.support_ui_empty)
        //    .buildEmptyTitle(R.string.support_ui_empty_title_placeholder)
        //    .buildEmptySubtitle(R.string.support_ui_empty_subtitle_placeholder)
        //    .displayEmptyView();
        contentPresenter.displayEmptyView();
        break;
      case R.id.btnContent:
        contentPresenter.displayContentView();
        break;
      case R.id.btnError:
        //contentPresenter.buildErrorImageView(R.drawable.support_ui_error_network)
        //    .buildErrorTitle(R.string.support_ui_error_title_placeholder)
        //    .buildErrorSubtitle(R.string.support_ui_error_subtitle_placeholder)
        //    .displayErrorView();
        contentPresenter.displayErrorView();
        break;
    }
  }

  @Override public void onEmptyViewClick(View view) {
    onRefresh();
  }

  @Override public void onErrorViewClick(View view) {
    onRefresh();
  }

  private void onRefresh() {
    rx.Observable.just(1)
        .subscribeOn(Schedulers.io())
        .delay(2, TimeUnit.SECONDS)
        .compose(RxUtils.progressTransformer(this))
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(integer -> {
          contentPresenter.displayContentView();
        });
  }

  @Override public void showProgress() {
    Observable.empty()
        .observeOn(mainThread())
        .doOnTerminate(() -> contentPresenter.displayLoadView())
        .subscribe();
  }

  @Override public void hideProgress() {
  }
}
