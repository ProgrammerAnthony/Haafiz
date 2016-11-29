package com.anthony.app.common.base;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.anthony.app.R;
import com.anthony.app.common.data.DataManager;
import com.anthony.app.common.injection.component.ActivityComponent;
import com.anthony.app.common.utils.ToastUtils;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import rx.subscriptions.CompositeSubscription;
import timber.log.Timber;

/**
 * Created by Anthony on 2016/2/25.
 * Class Note:
 * <p/>
 * Base Fragment for all the Fragment defined in the project
 * 1 extended from {@link AbsBaseFragment} to do
 * some base operation.
 * 2 do operation in {@link #initViews(View, Bundle)}
 */
public abstract class AbsBaseFragment extends Fragment {
    /**
     * Log tag
     */
    protected static String TAG_LOG = null;
    /**
     * url and title passed into fragment
     */
    public static String EXTRA_URL = "url";
    private String mUrl;
    public static String EXTRA_TITLE = "url";
    private String mTitle;
    /**
     * activity context of fragment
     */
    protected Context mContext;
    protected Activity mActivity;
    //    protected Subscription mSubscription;
    protected CompositeSubscription mSubscriptions;

    private Unbinder mUnBinder;


    @Inject
    ToastUtils mToastUtils;

    @Inject
    DataManager mDataManager;


    @Override
    public void onAttach(Context context) {
        //set a context from current activity
        mActivity = (Activity) context;
        mContext = context;
        super.onAttach(context);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//set Timber as log util
        TAG_LOG = this.getClass().getSimpleName();
        Timber.tag(TAG_LOG);

//url and title
        if (getArguments() != null) {
            mUrl = getArguments().getString(EXTRA_URL);
            mTitle = getArguments().getString(EXTRA_TITLE);
        }
//initialize Dagger2 to support DI
        initDagger2(((AbsBaseActivity) getActivity()).activityComponent());
//rxjava subscriptions,use it like  --->       mSubscriptions.add(subscription)
        mSubscriptions = new CompositeSubscription();

        loadData();

    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if(getLayoutView()!=null){
            return getLayoutView();
        }
        if (getLayoutId() != 0) {
            return inflater.inflate(getLayoutId(), null);
        } else {
            return super.onCreateView(inflater, container, savedInstanceState);
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        //设置状态栏透明
//        setStatusBarColor();
        super.onViewCreated(view, savedInstanceState);
        //bind The View returned by {@link #onCreateView(LayoutInflater, ViewGroup, Bundle)}.
        mUnBinder = ButterKnife.bind(this, view);
        //init views events here so we can use ButterKnife
        initViews(view,savedInstanceState);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mSubscriptions.clear();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
//ButterKnife unbind
        if (mUnBinder != null) {
            mUnBinder.unbind();
            mUnBinder = null;
        }
    }


    public String getFragmentUrl() {
        return mUrl;
    }

    public void setFragmentUrl(String url) {
        this.mUrl = url;
    }

    /**
     * Every fragment has to inflate a layout in the onCreateView method.
     * We have added this method to avoid duplicate all the inflate code in every fragment.
     * You only have to return the layout to inflate in this method when extends AbsBaseFragment.
     */
    protected abstract int getLayoutId();

    public View getLayoutView() {
        return null;
    }

    /**
     * override this method to use Dagger2 which support for Dependency Injection
     * <p>
     * using dagger2 in base class：https://github.com/google/dagger/issues/73
     */
    protected void initDagger2(ActivityComponent activityComponent) {
        activityComponent.inject(this);
    }

    /**
     * override this method to do operation in the fragment
     */
    protected abstract void initViews(View rootView, Bundle savedInstanceState);

    protected abstract void loadData();//load data in onCreate method

    protected void showToast(String content) {
        mToastUtils.showToast(content);
    }


    /**
     * show log
     * @param logInfo
     */
    protected void showLog(String logInfo) {
        Timber.i(logInfo);
    }

    public DataManager getDataManager() {
        return mDataManager;
    }

//    public void setStatusBarColor() {
//        StatusBarUtil.setTranslucentForImageViewInFragment(getActivity(), null);
//    }

    protected void setToolBar(Toolbar toolbar, String title) {
        toolbar.setTitle(title);
        toolbar.setNavigationIcon(R.mipmap.ic_arrow_back_white_24dp);
//               toolbar.setNavigationOnClickListener(v -> onBackPressedSupport());
    }


}

