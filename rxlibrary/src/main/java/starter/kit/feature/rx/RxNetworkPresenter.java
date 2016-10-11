package starter.kit.feature.rx;

import android.os.Bundle;

import com.trello.rxlifecycle.FragmentEvent;
import com.trello.rxlifecycle.RxLifecycle;

import rx.Observable;
import rx.functions.Action2;
import rx.functions.Func0;
import rx.subjects.BehaviorSubject;
import starter.kit.feature.NetworkContract;
import starter.kit.retrofit.RetrofitException;
import starter.kit.util.HudInterface;
import starter.kit.util.RxUtils;

import static rx.android.schedulers.AndroidSchedulers.mainThread;
import static rx.schedulers.Schedulers.io;

public abstract class RxNetworkPresenter<T, ViewType extends NetworkContract.View> extends
    RxStarterPresenter<ViewType> implements
    HudInterface {

  private static final int RESTARTABLE_ID = 2000;

  @Override
  protected void onCreate(Bundle savedState) {
    super.onCreate(savedState);

    restartableFirst(restartableId(), new Func0<Observable<T>>() {
      @Override
      public Observable<T> call() {
        BehaviorSubject<FragmentEvent> lifecycle = BehaviorSubject.create();
        return request().subscribeOn(io())
            .compose(RxUtils.hudTransformer(RxNetworkPresenter.this))
            .compose(RxLifecycle.bindFragment(lifecycle))
            .observeOn(mainThread());
      }
    }, new Action2<ViewType, T>() {
      @Override
      public void call(ViewType viewType, T item) {
        viewType.onSuccess(item);
      }
    }, new Action2<ViewType, Throwable>() {
      @Override
      public void call(ViewType viewType, Throwable throwable) {
        RetrofitException error = (RetrofitException) throwable;
        viewType.onError(error);
      }
    });
  }

  public int restartableId() {
    return RESTARTABLE_ID;
  }

  public abstract Observable<T> request();

  public void start() {
    start(restartableId());
  }

  public void stop() {
    stop(restartableId());
  }
}
