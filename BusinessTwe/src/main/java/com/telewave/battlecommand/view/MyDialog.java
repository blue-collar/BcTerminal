package com.telewave.battlecommand.view;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.telewave.lib.base.ConstData;

/**
 * dialog
 */

public class MyDialog extends Dialog {

    private static int default_width = ConstData.screenWidth * 4 / 6; //默认宽度
    private static int default_height = WindowManager.LayoutParams.WRAP_CONTENT;//默认高度

    public MyDialog(Context context, View layout, int style) {
        this(context, default_width, default_height, layout, style);
    }

    public MyDialog(Context context, int width, int height, View layout, int style) {
        super(context, style);
        setContentView(layout);
        Window window = getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params.gravity = Gravity.CENTER;
        params.width = ConstData.screenWidth * 4 / 6;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(params);
    }

}
