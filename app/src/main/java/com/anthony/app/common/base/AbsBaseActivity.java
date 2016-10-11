package com.anthony.app.common.base;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.anthony.app.common.data.EventPosterHelper;
import com.anthony.app.common.injection.component.ActivityComponent;
import com.anthony.app.common.injection.component.DaggerActivityComponent;
import com.anthony.app.common.injection.module.ActivityModule;
import com.anthony.app.common.utils.ToastUtils;
import com.anthony.app.common.widgets.dialog.DialogManager;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.pedant.SweetAlert.SweetAlertDialog;
import rx.Subscription;
import timber.log.Timber;


/**
 * Created by Anthony on 2016/4/24.
 * Class Note:
 * 1 all activities implement from this class
 * 2 implemented method from {@link BaseView}
 * todo add Umeng analysis
 */
public abstract class AbsBaseActivity extends AppCompatActivity implements BaseView {

    protected static String TAG_LOG = null;// Log tag

    protected Context mContext = null;//context
    private ActivityComponent mActivityComponent;
    protected Subscription mSubscription;
    private Unbinder mUnbinder;

    @Inject
    ToastUtils toastUtils;

    @Inject
    EventPosterHelper eventPosterHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        mContext = this;

//set timber tag
        TAG_LOG = this.getClass().getSimpleName();
        Timber.tag(TAG_LOG);
//save activities stack
        BaseAppManager.getInstance().addActivity(this);
//set content view
        if (getContentViewID() != 0)
            setContentView(getContentViewID());
//bind this after setContentView
        mUnbinder = ButterKnife.bind(this);
//inject Dagger2 here
        injectDagger(activityComponent());


//register EventBus
        eventPosterHelper.getBus().register(this);
//sample        eventPosterHelper.postEventSafely(xxx);

//init views and events
        initViewsAndEvents();


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
//        ButterKnife.unbind(this);
        if (mUnbinder != null) {
            mUnbinder.unbind();
            mUnbinder = null;
        }
        if (mSubscription != null && !mSubscription.isUnsubscribed()) {
            mSubscription.unsubscribe();
        }
    }

    @Override
    public void finish() {
        super.finish();
        BaseAppManager.getInstance().removeActivity(this);
    }


    /**
     * bind layout resource file
     */
    protected abstract int getContentViewID();

    /**
     * Dagger2 use in your application module(not used in 'base' module)
     */
    protected void injectDagger(ActivityComponent activityComponent) {
        activityComponent.inject(this);
    }

    /**
     * init views and events here
     */
    protected abstract void initViewsAndEvents();

    /**
     * -----------------------using in MVP implements methods in BaseView------------
     **/
    @Override
    public void showMessage(String msg) {
        toastUtils.showToast(msg);
    }

    @Override
    public void close() {
        finish();
    }

    @Override
    public void showProgress(String message) {
        DialogManager.showProgressDialog(mContext, message);
    }

    @Override
    public void showProgress(String message, int progress) {
        DialogManager.showProgressDialog(mContext, message, progress);
    }

    @Override
    public void hideProgress() {
        DialogManager.hideProgressDialog();
    }

    @Override
    public void showErrorMessage(String msg, String content) {
        DialogManager.showErrorDialog(mContext, msg, content, new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                sweetAlertDialog.dismissWithAnimation();
            }
        });
    }
}

