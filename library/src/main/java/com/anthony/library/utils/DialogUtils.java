package com.anthony.library.utils;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;




/**
 * 对话框工具类, 提供常用对话框显示, 使用support.v7包内的AlertDialog样式
 */
public class DialogUtils {

    public static Dialog createProgressDialog(Context context) {
        return createProgressDialog(context, true);
    }

    public static Dialog createProgressDialog(Context context, boolean needCancle) {
        ProgressDialog dialog = new ProgressDialog(context);
        dialog.setMessage("Loading ...");
        dialog.setCancelable(needCancle);
        dialog.setCanceledOnTouchOutside(false);
        return dialog;
    }

    public static Dialog showCommonDialog(Context context, String message,
                                          DialogInterface.OnClickListener listener) {
        return new AlertDialog.Builder(context)
                .setMessage(message)
                .setPositiveButton("确定", listener)
                .setNegativeButton("取消", null)
                .show();
    }

    public static Dialog showConfirmDialog(Context context, String message,
                                           DialogInterface.OnClickListener listener) {
        return new AlertDialog.Builder(context)
                .setMessage(message)
                .setPositiveButton("确定", listener)
                .show();
    }

}
