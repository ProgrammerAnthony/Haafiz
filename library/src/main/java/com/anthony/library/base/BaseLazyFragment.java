package com.anthony.library.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Anthony on 2016/11/2.
 * Class Note:
 * implements this class to implements Lazy loading for fragment
 * <p>
 * must load data in method {@link #loadLazyData()}
 */

public abstract class BaseLazyFragment extends AbsBaseFragment {
    private boolean isPrepared;//flag whether view is okay
    private boolean isFirstLoad = true;//flag whether firstly load
    private boolean isVisible;//flag whether view is visible

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        isFirstLoad = true;
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        isPrepared = true;
        lazyLoad(); //load data before initViews and ButterKnife bind
        super.onViewCreated(view, savedInstanceState);
    }

    /**
     * when used with {@link android.support.v4.view.ViewPager} invoke this method
     *
     * @param isVisibleToUser
     */
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (getUserVisibleHint()) {
            isVisible = true;
            onVisible();
        } else {
            isVisible = false;
            onInvisible();
        }
    }


    /**
     * 1 used when using {@link android.support.v4.app.FragmentTransaction}to show or hide fragment
     * 2 to invoke this event ,when initialize fragment call show after hide
     * 3 Called when the hidden state (as returned by {@link #isHidden()} of
     * the fragment has changed.  Fragments start out not hidden; this will
     * be called whenever the fragment changes state from that.
     *
     * @param hidden True if the fragment is now hidden, false if it is not
     *               visible.
     */
    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            isVisible = true;
            onVisible();
        } else {
            isVisible = false;
            onInvisible();
        }
    }

    protected void onVisible() {
        lazyLoad();
    }

    protected void onInvisible() {
    }


    /**
     * to implements lazy laodd , need to set isPrepared to true in {@link #onViewCreated(View, Bundle)}
     * isPrepared = true;
     */
    protected void lazyLoad() {
        if (!isPrepared || !isVisible || !isFirstLoad) {
            return;
        }
        isFirstLoad = false;
        loadLazyData();
    }


    protected abstract void loadLazyData();

    @Override
    protected void loadData() {
        //use  loadLazyData() instead
    }

}
