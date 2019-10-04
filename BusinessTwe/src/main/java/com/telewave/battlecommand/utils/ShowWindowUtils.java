package com.telewave.battlecommand.utils;

import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.telewave.battlecommand.view.MyDialog;
import com.telewave.business.twe.R;


/**
 * 安装、更新弹出框
 */

public class ShowWindowUtils {
    public static void showInstallWindow(Context context) {
        if (dialog == null || !dialog.isShowing()) {
            showPupWindow(context, INSTALL_TAG, "新版本安装", "系统下载已完成，是否安装?", "是", "");
        }

    }

    public static void showExitSystemWindow(Context context) {
        if (dialog == null || !dialog.isShowing()) {
            showPupWindow(context, EXIT_SYSTEM, "退出系统", "确认退出指挥终端", "确认", "取消");
        }
    }

    public static void showLoginExit(Context context, int flag) {
        if (dialog == null || !dialog.isShowing()) {
            if (flag == LOGIN_ERROR) {
                showPupWindow(context, LOGIN_EXIT, "", "您已连续输入10次错误认证信息，系统已经解绑该终端，请联系系统管理员，卸载并重新安装软件，绑定终端。", "确认", "取消");
            } else if (flag == NO_DEVICEID) {
                showPupWindow(context, LOGIN_EXIT, "", "未获取到设备ID，请检查设备或重启。", "确认", "取消");
            }
        }

    }

    public static void showUpdateWindow(Context context, String updataDetail) {
        if (dialog == null || !dialog.isShowing()) {
            showPupWindow(context, UPDATE_TAG, "新版本升级", updataDetail, "更新", "取消");
        }
    }


    public static Dialog dialog;
    private static int UPDATE_TAG = 1;
    private static int INSTALL_TAG = 2;
    private static int LOGIN_EXIT = 3;
    public static int LOGIN_ERROR = 6;
    public static int NO_DEVICEID = 7;
    public static int EXIT_SYSTEM = 8;


    private static void showPupWindow(final Context context, final int tag, String title, final String hint, final String hintBtn, String rightBtnHint) {
        // 自定义布局
        View view = LayoutInflater.from(context).inflate(R.layout.internet_popview, null);
        dialog = new MyDialog(context, view, R.style.dialog);
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
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                if (tag == EXIT_SYSTEM) {

                    System.exit(0);
                }
                dialog.dismiss();

            }
        });
//        leftButton.setOnClickListener(new View.OnClickListener() {
//            @RequiresApi(api = Build.VERSION_CODES.M)
//            @Override
//            public void onClick(View v) {
//                if (tag == UPDATE_TAG) {
//                    ConstData.isUpdateDownload = true;
//                    ListenerManager.getInstance().SendUpdataCallBackListener(true);
//                    getUpdataApk(context, ConstData.newVersionCode);
//                } else if (tag == INSTALL_TAG) {
//                    ConstData.isUpdateSystem = false;
//                    ConstData.isDBContactOk = false;
//                    ConstData.isContactDataOk = false;
//                    ConstData.isOrganDataOk = false;
//                    ConstData.isSelectOrganDataOk = false;
//                    ConstData.isDBTreatOk = false;
//                    ConstData.isDBChemicalOk = false;
//                    FileCacheUtils.putDataSharedPreferences(context, "isupdate", ConstData.isUpdateSystem);
//                    AppUtils.InstallApk(context, ConstData.UPDATE_SYSTEM_DIR, ConstData.newVersionCode + ".apk", AppUtils.INSTALL_BAIDU);
//                } else if (tag == INSTALL_WPS) {
//                    AppUtils.InstallApk(context, ConstData.wpsDir, "wps.apk", AppUtils.INSTALL_WPS);
//                } else if (tag == DOWNLOAD_BAIDUMAP) {
//                    Intent intent = new Intent();
//                    intent.setAction("android.intent.action.VIEW");
//                    Uri content_url = Uri.parse("http://shouji.baidu.com/software/10303070.html");
//                    intent.setData(content_url);
//                    context.startActivity(intent);
//                } else if (tag == DOWNLOAD_WPS) {
//                    ConstData.isDownloadWps = true;
//                    getWpsApk(context);
//                } else if (tag == LOGIN_EXIT) {
//                    MyApplication.getInstance().exit();
//                } else if (tag == EXIT_SYSTEM) {
//                    MyApplication.getInstance().exit();
//                }
//                dialog.dismiss();
//
//            }
//        });
        if (tag != LOGIN_EXIT) {
            Button rightBtn = (Button) view.findViewById(R.id.right_btn);
            rightBtn.setVisibility(View.VISIBLE);
            rightBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
        }
        WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
        // 显示在中间
        params.gravity = Gravity.CENTER;
        // 设置属性
        dialog.getWindow().setAttributes(params);
        // 把自定义view加上去
        dialog.setContentView(view);
        dialog.show();
    }


    public static void dismisWindow() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
            dialog = null;
        } else if (dialog != null) {
            dialog = null;
        }
    }
}
