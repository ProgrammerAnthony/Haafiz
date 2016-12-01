package com.anthony.library.utils;

import android.content.Context;
import android.widget.Toast;


/**
 * ToastUtils
 * 
 *
 */
public class ToastUtils {
    private Context mContext;
    private static ToastUtils mInstance;
    private Toast mToast;

//    public static ToastUtils getInstance() {
//        return mInstance;
//    }
//
//    public static void initialize(Context ctx) {
//        mInstance = new ToastUtils(ctx);
//    }

    public ToastUtils(Context ctx) {
        mContext = ctx;
    }

    public void showToast(String text) {
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