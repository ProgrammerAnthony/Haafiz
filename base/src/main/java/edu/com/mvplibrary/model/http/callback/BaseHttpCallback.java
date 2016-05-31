package edu.com.mvplibrary.model.http.callback;

/**
 * Created by Anthony on 2016/3/2.
 * Class Note:
 * abstract class for base http callback
 */
public abstract class BaseHttpCallback<T> {
    public void onStart(){}

    public void onEnd() {}

    public abstract void onResponse(T response);
    public abstract void onError(String error);

}
