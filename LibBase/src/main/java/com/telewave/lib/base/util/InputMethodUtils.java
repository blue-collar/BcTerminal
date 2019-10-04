package com.telewave.lib.base.util;

import android.app.Activity;
import android.content.Context;
import android.os.IBinder;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import static android.content.Context.INPUT_METHOD_SERVICE;
import static android.view.inputmethod.InputMethodManager.HIDE_NOT_ALWAYS;
import static android.view.inputmethod.InputMethodManager.RESULT_UNCHANGED_SHOWN;

/**
 * @Author: rick_tan
 * @Date: 19-7-23
 * @Version: v1.0
 * @Des 输入法工具类（包括软键盘）
 */
public class InputMethodUtils {

    /**
     * 隐藏软键盘
     */
    public static void hideSoftInput(EditText et) {
        InputMethodManager inputMethodManager = (InputMethodManager) et.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(et.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    /**
     * 隐藏输入法
     */
    public static void hideInputMethod(Activity activity) {
        View focus = activity.getCurrentFocus();
        if (focus != null) {
            InputMethodManager imeManager = (InputMethodManager) activity.getSystemService(INPUT_METHOD_SERVICE);
            if (imeManager != null) {
                imeManager.hideSoftInputFromWindow(activity.getWindow().getDecorView().getWindowToken(), 0);
            }
        }
    }

    /**
     * 显示输入法
     */
    public static void showInputMethod(Activity activity) {
        View focus = activity.getCurrentFocus();
        if (focus != null) {
            InputMethodManager imeManager = (InputMethodManager) activity.getSystemService(INPUT_METHOD_SERVICE);
            if (imeManager != null) {
                imeManager.showSoftInput(focus, InputMethodManager.SHOW_IMPLICIT);
            }
        }
    }

    private static InputMethodManager getInputManager(Context paramContext) {
        return (InputMethodManager) paramContext.getSystemService(Context.INPUT_METHOD_SERVICE);
    }

    public static void hideKeyboard(Context paramContext, IBinder iBinder) {
        getInputManager(paramContext).hideSoftInputFromWindow(iBinder, HIDE_NOT_ALWAYS);
    }

    public static void hideKeyboard(View view) {
        hideKeyboard(view.getContext(), view.getWindowToken());
    }

    public static void showKeyboard(View view) {
        getInputManager(view.getContext()).showSoftInput(view, 0);
    }

    public static void hideInputMethod(View view) {
        InputMethodManager inputMethodManager = getInputManager(view.getContext());
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), RESULT_UNCHANGED_SHOWN);
    }
}
