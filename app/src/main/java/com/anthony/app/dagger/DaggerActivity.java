package com.anthony.app.dagger;

import android.os.Bundle;

import com.anthony.app.dagger.component.ActivityComponent;
import com.anthony.app.dagger.component.DaggerActivityComponent;
import com.anthony.app.dagger.module.ActivityModule;
import com.anthony.library.base.AbsBaseActivity;
import com.anthony.library.utils.ToastUtils;

import javax.inject.Inject;

/**
 * Created by Anthony on 2016/12/1.
 * Class Note:
 * Dagger2 support
 */

public abstract class DaggerActivity extends AbsBaseActivity {


    private ActivityComponent mActivityComponent;//dagger2 ActivityComponent
    @Inject
    DataRepository dataRepository;
    @Inject
    ToastUtils toastUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        injectDagger(activityComponent());
        super.onCreate(savedInstanceState);

    }

    /**
     * Dagger2 use in your application module(not used in 'base' module)
     * <p>
     * using dagger2 in base class：https://github.com/google/dagger/issues/73
     */
    protected void injectDagger(ActivityComponent activityComponent) {
        activityComponent.inject(this);
    }

    public ActivityComponent activityComponent() {
        if (mActivityComponent == null) {
            mActivityComponent = DaggerActivityComponent.builder()
                    .activityModule(new ActivityModule(this))
                    .applicationComponent(DaggerApplication.get(this).getAppComponent())
                    .build();
        }
        return mActivityComponent;
    }

    public DataRepository getDataRepository() {
        return dataRepository;
    }

    public void showToast(String txt) {
        toastUtils.showToast(txt);
    }
}
