package com.anthony.app.module.splash;

import android.Manifest;
import android.os.Bundle;

import com.anthony.app.R;
import com.anthony.app.common.base.AbsBaseActivity;
import com.anthony.app.common.utils.RxUtils;
import com.anthony.app.module.MainListActivity;
import com.tbruyelle.rxpermissions.RxPermissions;

import java.util.concurrent.TimeUnit;

import rx.Observable;

/**
 * Created by Anthony on 2016/11/28.
 * Class Note:
 * 闪屏页面 splash view
 */

public class SplashActivity extends AbsBaseActivity{
    @Override
    protected void initViewsAndEvents(Bundle savedInstanceState) {

        Observable.timer(2000, TimeUnit.MILLISECONDS)
                .compose(RxPermissions.getInstance(this).ensureEach(Manifest.permission.READ_PHONE_STATE, Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE))
                .compose(RxUtils.defaultSchedulers())
                .subscribe(permission -> {
                    if (permission.granted) {
                        startActivity(MainListActivity.class);
                        finish();
                    }
                });
    }

    @Override
    protected int getContentViewID() {
        return R.layout.prj_activity_flash;
    }
}
