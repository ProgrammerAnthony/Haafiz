package com.anthony.app.common.utils;

import android.content.Context;
import android.widget.Toast;

import com.anthony.app.common.injection.scope.ApplicationContext;

import javax.inject.Inject;


/**
 * ToastUtils
 * 
 *
 */
public class ToastUtils {

    private static Context mContext;
    private static ToastUtils mInstance;
    private Toast mToast;

    @Inject
    public ToastUtils(@ApplicationContext Context context){
        mContext=context;
    }


    public  void showToast(String text) {
        if(mToast == null) {
            mToast = Toast.makeText(mContext, text, Toast.LENGTH_LONG);
        } else {
            mToast.setText(text);
            mToast.setDuration(Toast.LENGTH_LONG);
        }
        mToast.show();
    }

    public void cancelToast() {
        if (mToast != null) {
            mToast.cancel();
        }
    }
}
