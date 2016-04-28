package edu.com.mvplibrary.presenter;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;

import edu.com.mvplibrary.model.BaseModel;
import edu.com.mvplibrary.view.BaseView;

/**
 * Created by Anthony on 2016/4/24.
 * Class Note:
 * 1 Every presenter in the app must either implement this interface or extend BasePresenter
 * indicating the View type that wants to be attached with.
 */
public abstract class BasePresenter<V extends BaseView> {
    protected Reference<V> mViewRef;

    public BasePresenter() {
    }

    /**
     * init one view for one presenter
     */
    public BasePresenter(V view) {
        attachView(view);
    }

    public void attachView(V view) {
        mViewRef = new WeakReference<>(view);
    }


    protected V getView() {
        return mViewRef.get();
    }

    public boolean isViewAttached() {
        return mViewRef != null && mViewRef.get() != null;
    }

    public void detachView() {
        if (mViewRef != null) {
            mViewRef.clear();
            mViewRef = null;
        }

    }


    public void onDestroy() {
        if (mViewRef != null) {
            mViewRef.clear();
            mViewRef = null;
        }
    }

}
