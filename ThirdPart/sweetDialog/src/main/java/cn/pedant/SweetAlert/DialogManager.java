package cn.pedant.SweetAlert;

import android.content.Context;


/**
 * Create By Anthony on 2016/1/15
 * Class Note:
 * static dialog factory class to show dialog，use method like Dialog.xxx(),
 * SweetAlertDialog in Third-Part used
 * 利用静态工厂类构建dialog，直接使用Dialog.xxx()的方法进行访问创建dialog
 * 使用了Third-Part中的SweetAlertDialog
 */
public class DialogManager {

    private static SweetAlertDialog mDialog;

    public static void showWarningDialog(Context context, String title, String content, SweetAlertDialog.OnSweetClickListener listener) {
        if (mDialog != null) {
            mDialog = null;
        }
        mDialog = new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE)
                .setTitleText(title)
                .setContentText(content)
                .setConfirmText("确定")
                .setCancelText("取消")
                .setConfirmClickListener(listener);
        mDialog.show();
    }

    public static void showErrorDialog(Context context, String title, String content, SweetAlertDialog.OnSweetClickListener listener) {
        if (mDialog != null) {
            mDialog = null;
        }
        mDialog = new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
                .setConfirmText("确定")
                .setTitleText(title)
                .setContentText(content)
                .setConfirmClickListener(listener);
        mDialog.show();
    }

    public static void showProgressDialog(Context context, String message) {
        if (mDialog != null) {
            mDialog = null;
        }
        mDialog = new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE);
        mDialog.getProgressHelper().setBarColor(context.getResources().getColor(R.color.red_btn_bg_pressed_color));
        mDialog.setTitleText(message);
        mDialog.setCancelable(true);
        mDialog.show();
    }


    public static void showProgressDialog(Context context, String message, int progress) {
        if (mDialog != null) {
            mDialog = null;
        }
        mDialog = new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE);
        mDialog.getProgressHelper().setBarColor(context.getResources().getColor(R.color.red_btn_bg_pressed_color));
        mDialog.setTitleText(message);
        mDialog.setCancelable(true);
        mDialog.getProgressHelper().setProgress(progress);
        mDialog.show();
    }

    public static void hideProgressDialog() {
        if (mDialog != null && mDialog.isShowing()) {
            mDialog.dismiss();
        }
    }


}
