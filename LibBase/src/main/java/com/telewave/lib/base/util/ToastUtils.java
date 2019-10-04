package com.telewave.lib.base.util;

import android.content.Context;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

import com.telewave.lib.base.AppProxy;

/**
 * @Author: rick_tan
 * @Date: 19-7-20
 * @Version: v1.0
 * @Des Toast操作工具类
 */
public class ToastUtils {

    private static Context sCtx;
    private static Toast sToast;

    public static final void toastLongMessage(final String message) {
        BackgroundTasks.getInstance().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (sToast != null) {
                    sToast.cancel();
                    sToast = null;
                }
                sToast = Toast.makeText(AppProxy.getContext(), message,
                        Toast.LENGTH_LONG);
                sToast.show();
            }
        });
    }

    public static final void toastShortMessage(final String message) {
        BackgroundTasks.getInstance().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (sToast != null) {
                    sToast.cancel();
                    sToast = null;
                }
                sToast = Toast.makeText(AppProxy.getContext(), message,
                        Toast.LENGTH_SHORT);
                sToast.show();
            }
        });
    }

    public static void toastMsg(String msg) {
        if (TextUtils.isEmpty(msg)) {
            return;
        }
        cancel();
        checkContext();
        sToast = Toast.makeText(sCtx, msg, Toast.LENGTH_SHORT);
        sToast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        sToast.show();
    }

    public static void toastLongMsg(String msg) {
        if (TextUtils.isEmpty(msg)) {
            return;
        }
        cancel();
        checkContext();
        sToast = Toast.makeText(sCtx, msg, Toast.LENGTH_LONG);
        sToast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        sToast.show();
    }

    public static void toastMsg(int msgId) {
        checkContext();
        toastMsg(sCtx.getResources().getString(msgId));
    }

    private static void checkContext() {
        if (sCtx == null) {
            sCtx = AppProxy.getInstance().getApplication();
        }
    }

    /**
     * 自定义toast样式
     */
    public static void toastMsgWithStyle(View toastVieww) {
        if (sToast != null) {
            sToast.cancel();
        }
        checkContext();
        sToast = new Toast(sCtx);
        sToast.setDuration(Toast.LENGTH_SHORT);
        sToast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        sToast.setView(toastVieww);
        sToast.show();
    }

    /**
     * 取消toast
     */
    public static void cancel() {
        if (sToast != null) {
            sToast.cancel();
            sToast = null;
        }
    }
}
