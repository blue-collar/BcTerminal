package com.telewave.battlecommand.imui.util;


import android.app.Activity;
import android.app.Dialog;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;


public class IMDialogUtil {
    /**
     * Dialog显示工具
     *
     * @param view
     * @param style
     * @param anim
     * @param gravity
     * @param isTouchCancel
     * @return
     */
    public static Dialog getDialog(View view, int style, int anim, int gravity, boolean isTouchCancel) {
        Dialog dialog = new Dialog(view.getContext(), style);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(view, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        Window window = dialog.getWindow();
        if (null != window) {
            window.setWindowAnimations(anim);
            WindowManager.LayoutParams layoutParams = window.getAttributes();
            layoutParams.x = 0;
            layoutParams.gravity = gravity;
            dialog.onWindowAttributesChanged(layoutParams);
            if (gravity == Gravity.BOTTOM) {
                layoutParams.width = LayoutParams.MATCH_PARENT;
            }
        }
        dialog.setCanceledOnTouchOutside(isTouchCancel);
        return dialog;
    }

    /**
     * 点击非dialog区域，dialog不消失
     *
     * @param activity
     * @param view
     * @param style
     * @param anim
     * @param centerY  是否从中间弹出
     * @return dialog
     */
    public static Dialog getDialog(Activity activity, View view, int style, int anim, boolean centerY) {
        Dialog dialog = new Dialog(activity, style);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(view, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        Window window = dialog.getWindow();
        window.setWindowAnimations(anim);
        WindowManager.LayoutParams wl = window.getAttributes();
        wl.x = 0;
        if (centerY) {
            wl.gravity = Gravity.CENTER;
        } else {
            wl.y = activity.getWindowManager().getDefaultDisplay().getHeight();
        }
        wl.width = LayoutParams.MATCH_PARENT;
        wl.height = LayoutParams.WRAP_CONTENT;

        dialog.onWindowAttributesChanged(wl);
        dialog.setCanceledOnTouchOutside(false);
        return dialog;
    }

    /**
     * 点击非dialog区域，dialog消失
     *
     * @param activity
     * @param view
     * @param style
     * @param anim
     * @param centerY  是否从中间弹出
     * @return dialog
     */
    public static Dialog getDialogOutside(Activity activity, View view, int style, int anim, boolean centerY) {
        Dialog dialog = new Dialog(activity, style);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(view, new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        Window window = dialog.getWindow();
        window.setWindowAnimations(anim);
        WindowManager.LayoutParams wl = window.getAttributes();
        wl.x = 0;
        if (centerY) {
            wl.gravity = Gravity.CENTER;
        } else {
            wl.y = activity.getWindowManager().getDefaultDisplay().getHeight();
        }
        wl.width = LayoutParams.WRAP_CONTENT;
        wl.height = LayoutParams.WRAP_CONTENT;

        dialog.onWindowAttributesChanged(wl);
        dialog.setCanceledOnTouchOutside(true);
        return dialog;
    }


    /**
     * 点击非dialog区域，dialog消失
     *
     * @param activity
     * @param view
     * @param style
     * @param anim
     * @param centerY  是否从中间弹出
     * @return dialog
     */
    public static Dialog getDialogOutSide(Activity activity, View view, int style, int anim, boolean centerY) {
        Dialog dialog = new Dialog(activity, style);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(view, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        Window window = dialog.getWindow();
        if (anim != 0) {
            Animation animation = AnimationUtils.loadAnimation(activity, anim);
            view.setAnimation(animation);
        }
        WindowManager.LayoutParams wl = window.getAttributes();
        wl.x = 0;
        if (centerY) {
            wl.gravity = Gravity.CENTER;
        } else {
            wl.y = activity.getWindowManager().getDefaultDisplay().getHeight();
        }
        dialog.onWindowAttributesChanged(wl);
        dialog.setCanceledOnTouchOutside(true);
        return dialog;
    }

}
