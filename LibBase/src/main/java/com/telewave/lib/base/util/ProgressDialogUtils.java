package com.telewave.lib.base.util;

import android.app.ProgressDialog;
import android.content.Context;
import android.view.Window;
import android.widget.TextView;

import com.telewave.lib.base.R;

/**
 * Note: 展示等待对画框的工具类
 * Created by liwh on 2017/10/28,13:25.
 */

public class ProgressDialogUtils {
    private static ProgressDialog mProgressDialog;

    public static void showDialog(Context context, String text) {
        try {
            if (isDialogShowing()) {
                dismissDialog();
            }
            if (mProgressDialog == null) {
                mProgressDialog = new ProgressDialog(context);
                mProgressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                mProgressDialog.setCanceledOnTouchOutside(false);
//            mProgressDialog.setCancelable(false);
            }
            mProgressDialog.show();
            mProgressDialog.setContentView(R.layout.custom_progressdialog_dialog);
            TextView message = (TextView) mProgressDialog.findViewById(R.id.message);
            message.setText(text);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static boolean isDialogShowing() {
        return mProgressDialog != null && mProgressDialog.isShowing();
    }

    public static void dismissDialog() {
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
            mProgressDialog = null;
        }
    }

}
