package com.telewave.lib.base.util;

import android.app.ProgressDialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.Window;
import android.widget.Toast;


public class Tips {
    /*
     * 多次触发只显示一次的Toast
     *
     * */
    private static Toast toast;
    private static String oldMessage;
    private static long previousTime;
    public static ProgressDialog proDialog;

    public static void showToast(Context context, String message) {
        if (toast == null) {
            toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
            toast.show();
            oldMessage = message;
        } else {
            if (message.equals(oldMessage)) {
                if (System.currentTimeMillis() - previousTime > Toast.LENGTH_SHORT) {
                    toast.show();
                }
            } else {
                oldMessage = message;
                toast.setText(message);
                toast.show();
            }
        }
        previousTime = System.currentTimeMillis();
    }

    /*
     *  多次触发只显示一次的Toast
     * */
    public static void showToast(Context context, int res) {
        showToast(context, context.getString(res));
    }

    /*
     * 显示进度dialoga
     * */
    public static void showProgress(Context context, String msg) {
        proDialog = new ProgressDialog(context);
        proDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        proDialog.setCanceledOnTouchOutside(false);
        if (proDialog != null && !proDialog.isShowing()) {
            if (!TextUtils.isEmpty(msg)) proDialog.setMessage(msg);
            proDialog.show();
        }
    }

    /*
     * 取消进度dialoga
     * */
    public static void dismissProgress() {
        if (proDialog != null && proDialog.isShowing()) {
            proDialog.dismiss();
        }
    }

}
