package com.telewave.battlecommand.utils;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.FileProvider;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.telewave.battlecommand.view.MyDialog;
import com.telewave.business.twe.R;
import com.telewave.lib.base.util.AppUtils;
import com.telewave.lib.base.util.FileUtils;
import com.telewave.lib.base.util.ToastUtils;
import com.tencent.qcloud.tim.uikit.TUIKit;

import java.io.File;

/**
 * 打开WPS文件帮助类
 */

public class OpenWPSUtil {

    public static void openFile(Context context, String filePath) {
        FileUtils.setRead(filePath);
        if (AppUtils.isAvilible(context, "cn.wps.moffice_eng")) {
            Intent intent = new Intent();
            Bundle bundle = new Bundle();
            bundle.putString("OpenMode", "ReadOnly");
            bundle.putBoolean("SendCloseBroad", true);
            bundle.putString("ThirdPackage", "com.android.settings");
            bundle.putBoolean("ClearBuffer", true);
            //这部分都是一些参数 ，wps官网上有解释，大家自己可以去看
            bundle.putBoolean("ClearTrace", true);
            intent.setAction("android.intent.action.VIEW");
            intent.setAction("android.intent.action.EDIT");
            //这两个参数是重点
            intent.setType("application/msword");
            //加入wps的包名和类名\cn.wps.moffice_eng普通版  //包名  类名  不多说
            intent.setClassName("cn.wps.moffice_eng", "cn.wps.moffice.documentmanager.PreStartActivity2");
            File file = new File(filePath);
            if (file == null || !file.exists()) {
                ToastUtils.toastShortMessage("文件不存在");
            }
            Uri uri;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//                uri = FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".fileProvider", file);
                uri = FileProvider.getUriForFile(TUIKit.getAppContext(), TUIKit.getAppContext().getApplicationInfo().packageName + ".uikit.fileprovider", file);

            } else {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                uri = Uri.fromFile(file);
            }
            intent.setData(uri);
            intent.putExtras(bundle);
            try {
                context.startActivity(intent);
            } catch (Exception e) {
                e.printStackTrace();
                ToastUtils.toastShortMessage("文件打开出错");
            }
        } else {
            showDownLoadWPSDiaog(context, "", "检测到未安装WPS，是否去下载安装?", "是", "");
        }

    }

    private static void showDownLoadWPSDiaog(final Context context, String title, final String hint, final String hintBtn, String rightBtnHint) {
        // 自定义布局
        View view = LayoutInflater.from(context).inflate(R.layout.internet_popview, null);
        final Dialog dialog = new MyDialog(context, view, R.style.dialog);
        //设置点击外部区域不消失
        dialog.setCancelable(false);
        TextView hintText = (TextView) view.findViewById(R.id.text_hint);
        hintText.setText(hint);
        if (title != null && !title.equals("")) {
            TextView titleText = (TextView) view.findViewById(R.id.disaster_message_pupview_title);
            titleText.setText(title);
        }
        Button leftButton = (Button) view.findViewById(R.id.internet_setting);
        leftButton.setText(hintBtn);
        if (rightBtnHint != null && !rightBtnHint.equals("")) {
            Button rightButton = (Button) view.findViewById(R.id.right_btn);
            rightButton.setText(rightBtnHint);
        }
        leftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //调用系统浏览器打开下载页面
                Uri uri = Uri.parse("https://a.myapp.com/o/simple.jsp?pkgname=cn.wps.moffice_eng");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                context.startActivity(intent);
                dialog.dismiss();
            }
        });
        WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
        // 显示在中间
        params.gravity = Gravity.CENTER;
        // 设置属性
        dialog.getWindow().setAttributes(params);
        // 把自定义view加上去
        dialog.setContentView(view);
        dialog.show();
    }

}
