package com.anthony.app.common.data.net;

import rx.Subscriber;
import timber.log.Timber;

/**
 * Created by Anthony on 2016/7/8.
 * Class Note:
 * {@link Subscriber} for {@link retrofit2.Retrofit},
 * add implementation to {@link #onCompleted()}and {@link #onError(Throwable)}
 * <p>
 * 继承自{@link Subscriber},添加对{@link #onCompleted()}和 {@link #onError(Throwable)}的实现。
 */
public abstract class HttpSubscriber<T> extends Subscriber<T> {
    @Override
    public void onCompleted() {
        Timber.i("HttpHelper Subscriber On Completed");
    }

    @Override
    public void onError(Throwable e) {
        Timber.e("HttpHelper Subscriber On Error: " + e.toString());
    }
}
