package edu.com.mvplibrary.util;

import android.content.Context;
import android.widget.Toast;

/**
 * ToastUtils
 * 
 *
 */
public class ToastUtils {

    private static Context mContext;
    private static ToastUtils mInstance;
    private Toast mToast;
    private ToastUtils(Context context){
        mContext=context;
    }
    public static ToastUtils getInstance(){
        return mInstance;
    }
    public static void initialize(Context context){
        mInstance=new ToastUtils(context);
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
