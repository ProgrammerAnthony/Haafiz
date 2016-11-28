package com.anthony.app.common.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.anthony.app.R;
import com.anthony.app.common.data.DataManager;
import com.anthony.app.common.data.EventPosterHelper;
import com.anthony.app.common.injection.component.ActivityComponent;
import com.anthony.app.common.injection.component.DaggerActivityComponent;
import com.anthony.app.common.injection.module.ActivityModule;
import com.anthony.app.common.utils.ToastUtils;
import com.anthony.statuslayout.StatusLayout;
import com.malinskiy.materialicons.IconDrawable;
import com.malinskiy.materialicons.Iconify;

import java.util.List;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import rx.subscriptions.CompositeSubscription;
import timber.log.Timber;


/**
 * Created by Anthony on 2016/4/24.
 * Class Note:
 * 1 all activities implement from this class
 * <p>
 * todo add Umeng analysis
 */
public abstract class AbsBaseActivity extends AppCompatActivity {

    protected static String TAG_LOG = null;// Log tag

    protected Context mContext = null;//context

    private ActivityComponent mActivityComponent;//dagger2 ActivityComponent

    //    protected Subscription mSubscription;
    protected CompositeSubscription mSubscriptions;

    private Unbinder mUnbinder;

    private StatusLayout mStatusLayout;//global status to define progress/error/content/empty

    @Inject
    ToastUtils toastUtils;

    @Inject
    EventPosterHelper eventPosterHelper;


    @Inject
    DataManager mDataManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        init(savedInstanceState);
    }

    private void init(Bundle savedInstanceState) {
        mContext = this;
//inject Dagger2 here
        injectDagger(activityComponent());
//set timber tag
        TAG_LOG = this.getClass().getSimpleName();
        Timber.tag(TAG_LOG);
//save activities stack
        BaseAppManager.getInstance().addActivity(this);
//rxjava subscriptions,use it like  --->       mSubscriptions.add(subscription)
        mSubscriptions = new CompositeSubscription();

//        if (getLayoutId() != 0)
//            setContentView(getLayoutId());
//set content view support di
        if (getStatusLayoutView() != null) {
            setContentView(getStatusLayoutView());
        }
//bind this after setContentView
        mUnbinder = ButterKnife.bind(this);

//register EventBus
        eventPosterHelper.getBus().register(this);
//sample        eventPosterHelper.postEventSafely(xxx);

//init views and events
        initViewsAndEvents(savedInstanceState);
    }


    public ActivityComponent activityComponent() {
        if (mActivityComponent == null) {
            mActivityComponent = DaggerActivityComponent.builder()
                    .activityModule(new ActivityModule(this))
                    .applicationComponent(MyApplication.get(this).getAppComponent())
                    .build();
        }
        return mActivityComponent;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mUnbinder != null) {
            mUnbinder.unbind();
            mUnbinder = null;
        }
        mSubscriptions.clear();
    }

    @Override
    public void finish() {
        super.finish();
        BaseAppManager.getInstance().removeActivity(this);
    }


    /**
     * use {@link StatusLayout} to  global status of progress/error/content/empty
     *
     * @return
     */
    protected View getStatusLayoutView() {
        mStatusLayout = (StatusLayout) LayoutInflater.from(this).inflate(R.layout.abs_activity_show_status, null);
        if (getContentViewID() != 0) {
//            FrameLayout statusFrameLayout = (FrameLayout) findViewById(R.id.status_frame_content);
            View contentView = LayoutInflater.from(this).inflate(getContentViewID(), null);
            ViewGroup.LayoutParams params =
                    new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            mStatusLayout.addView(contentView, params);
        }
        return mStatusLayout;
    }

    /**
     * Dagger2 use in your application module(not used in 'base' module)
     * <p>
     * using dagger2 in base class：https://github.com/google/dagger/issues/73
     */
    protected void injectDagger(ActivityComponent activityComponent) {
        activityComponent.inject(this);
    }

    /**
     * init views and events here
     */
    protected abstract void initViewsAndEvents(Bundle savedInstanceState);

    /**
     * bind layout resource file
     */
    protected abstract int getContentViewID();

    /**
     * show toast
     *
     * @param content toast content
     */
    protected void showToast(String content) {
        toastUtils.showToast(content);
    }

    /**
     * show log
     *
     * @param logInfo
     */
    protected void showLog(String logInfo) {
        Timber.i(logInfo);
    }

    protected void showContent() {
        showContent(null);
    }

    /**
     * Hide all other states and show content
     */
    protected void showContent(List<Integer> skipIds) {
        if (skipIds == null || skipIds.size() == 0) {
            mStatusLayout.showContent();
        } else {
            mStatusLayout.showContent(skipIds);
        }

    }

    /**
     * Hide content and show the progress bar
     */
    protected void showLoading(List<Integer> skipIds) {
        if (skipIds == null || skipIds.size() == 0) {
            mStatusLayout.showLoading();
        } else {
            mStatusLayout.showLoading(skipIds);
        }

    }

    /**
     * Show empty view when there are not data to show
     */
    protected void showEmpty(Drawable emptyDrawable, String emptyTitle, String emptyMessage, List<Integer> skipIds) {
        if (emptyDrawable == null) {
            emptyDrawable = new IconDrawable(this, Iconify.IconValue.zmdi_shopping_basket)
                    .colorRes(android.R.color.white);
        }
        if (TextUtils.isEmpty(emptyTitle)) {
            emptyMessage = "oops";
        }
        if (TextUtils.isEmpty(emptyMessage)) {
            emptyMessage = "nothing to show";
        }
        if (skipIds == null || skipIds.size() == 0) {
            mStatusLayout.showEmpty(emptyDrawable, emptyTitle, emptyMessage);
        } else {
            mStatusLayout.showEmpty(emptyDrawable, emptyTitle, emptyMessage, skipIds);
        }
    }

    /**
     * Show error view with a button when something
     * goes wrong and prompting the user to try again
     */
    protected void showError(Drawable errorDrawable, String errorTitle,
                             String errorMessage, String errorBtnTxt,
                             List<Integer> skipIds, View.OnClickListener errorClickListener) {
        if (errorDrawable == null) {
            errorDrawable = new IconDrawable(this, Iconify.IconValue.zmdi_wifi_off)
                    .colorRes(android.R.color.white);
        }
        if (errorClickListener == null) {
            errorClickListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getApplication(), "Try again button clicked", Toast.LENGTH_LONG).show();
                }
            };
        }
        if (errorTitle == null) {
            errorTitle = "No Connection";
        }
        if (errorMessage == null) {
            errorMessage = "We could not establish a connection with our servers." +
                    " Please try again when you are connected to the internet.";
        }
        if (errorBtnTxt == null) {
            errorBtnTxt = "Try Again";
        }
        if (skipIds == null || skipIds.size() == 0) {
            mStatusLayout.showError(errorDrawable, errorTitle, errorMessage, errorBtnTxt, errorClickListener);
        } else {
            mStatusLayout.showError(errorDrawable, errorTitle, errorMessage, errorBtnTxt, errorClickListener, skipIds);
        }
    }

    public DataManager getDataManager() {
        return mDataManager;
    }

    public void startActivity(Class<? extends Activity> tarActivity, Bundle options) {
        Intent intent = new Intent(this, tarActivity);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            startActivity(intent, options);
        } else {
            startActivity(intent);
        }
    }

    public void startActivity(Class<? extends Activity> tarActivity) {
        Intent intent = new Intent(this, tarActivity);
        startActivity(intent);
    }
}
