package com.anthony.app.dagger;

import android.os.Bundle;

import com.anthony.app.dagger.component.ActivityComponent;
import com.anthony.library.base.AbsBaseFragment;


/**
 * Created by Anthony on 2016/12/1.
 * Class Note:
 *  Dagger2 support
 */

public abstract class DaggerFragment extends AbsBaseFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        //initialize Dagger2 to support DI
        initDagger2(((DaggerActivity) getActivity()).activityComponent());
        super.onCreate(savedInstanceState);


    }

    /**
     * override this method to use Dagger2 which support for Dependency Injection
     * <p>
     * using dagger2 in base class：https://github.com/google/dagger/issues/73
     */
    protected void initDagger2(ActivityComponent activityComponent) {
        activityComponent.inject(this);
    }
}
