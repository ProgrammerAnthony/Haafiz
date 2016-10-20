package com.anthony.app.common.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.anthony.app.common.injection.component.ActivityComponent;
import com.anthony.app.common.utils.ToastUtils;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import rx.Subscription;
import timber.log.Timber;

/**
 * Created by Anthony on 2016/2/25.
 * Class Note:
 * <p/>
 * Base Fragment for all the Fragment defined in the project
 * 1 extended from {@link AbsBaseFragment} to do
 * some base operation.
 * 2 do operation in initViewAndEvents(){@link #initViewsAndEvents(View rootView)}
 */
public abstract class AbsBaseFragment extends Fragment {
    /**
     * Log tag
     */
    protected static String TAG_LOG = null;
    /**
     * url passed into fragment
     */
    public static String EXTRA_URL = "url";
    private String mUrl;
    /**
     * activity context of fragment
     */
    protected Context mContext;

    protected Subscription mSubscription;

    private Unbinder mUnbinder;

    @Inject
    ToastUtils toastUtils;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//set Timber as log util
        TAG_LOG = this.getClass().getSimpleName();
        Timber.tag(TAG_LOG);
//set a context from current activity
        mContext = getActivity();
//url
        if (getArguments() != null) {
            mUrl = getArguments().getString(EXTRA_URL);
        }
//initialize Dagger2 to support DI
        initDagger2(((AbsBaseActivity) getActivity()).activityComponent());
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (getContentViewID() != 0) {
            return inflater.inflate(getContentViewID(), null);
        } else {
            return super.onCreateView(inflater, container, savedInstanceState);
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mUnbinder = ButterKnife.bind(this, view);
        initViewsAndEvents(view);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mSubscription != null && !mSubscription.isUnsubscribed()) {
            mSubscription.unsubscribe();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
//        ButterKnife.unbind(this);
        if (mUnbinder != null) {
            mUnbinder.unbind();
            mUnbinder = null;
        }
    }


    public String getFragmentUrl() {
        return mUrl;
    }

    public void setFragmentUrl(String url) {
        this.mUrl = url;
    }

    /**
     * Every fragment has to inflate a layout in the onCreateView method. We have added this method
     * to
     * avoid duplicate all the inflate code in every fragment. You only have to return the layout to
     * inflate in this method when extends AbsBaseFragment.
     */
    protected abstract int getContentViewID();

    /**
     * override this method to use Dagger2 which support for Dependency Injection
     */
    protected void initDagger2(ActivityComponent activityComponent) {
        activityComponent.inject(this);
    }

    /**
     * override this method to do operation in the fragment
     */
    protected abstract void initViewsAndEvents(View rootView);


    protected void showToast(String content) {
        toastUtils.showToast(content);
    }


}

