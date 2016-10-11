package starter.kit.feature.rx;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import nucleus.presenter.Presenter;
import nucleus.view.NucleusAppCompatActivity;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

public class RxStarterActivity<P extends Presenter> extends NucleusAppCompatActivity<P> {

  private Unbinder mUnbinder;

  private final CompositeSubscription subscriptions = new CompositeSubscription();

  public void add(Subscription subscription) {
    subscriptions.add(subscription);
  }

  public void remove(Subscription subscription) {
    subscriptions.remove(subscription);
  }

  @Override
  public void setContentView(int layoutResID) {
    super.setContentView(layoutResID);
    mUnbinder = ButterKnife.bind(this);
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    if (mUnbinder != null) {
      mUnbinder.unbind();
      mUnbinder = null;
    }
    subscriptions.unsubscribe();
  }
}
