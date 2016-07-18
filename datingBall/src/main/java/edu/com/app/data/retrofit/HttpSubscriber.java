package edu.com.app.data.retrofit;

import android.util.Log;

import rx.Subscriber;

/**
 * Created by Anthony on 2016/7/8.
 * Class Note:
 * {@link Subscriber} for {@link retrofit2.Retrofit}
 */
public abstract class HttpSubscriber<T> extends Subscriber<T> {
    @Override
    public void onCompleted() {
        Log.v("HttpHelper", "Subscriber On Completed");
    }

    @Override
    public void onError(Throwable e) {
        Log.e("HttpHelper", "Subscriber On Error: " + e.toString());
    }
}
