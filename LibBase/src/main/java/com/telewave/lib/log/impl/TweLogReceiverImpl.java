package com.telewave.lib.log.impl;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;
import android.text.TextUtils;

import com.telewave.lib.log.TweLog;
import com.telewave.lib.log.TweLogReceiver;
import com.telewave.lib.log.upload.AppRomBaseInfo;
import com.telewave.lib.log.upload.TweLogUploadImpl;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @Author: rick_tan
 * @Date: 19-7-20
 * @Version: v1.0
 * @Des 广播具体实现
 * @Modify On 2019-xx-xx by *** for reason ...
 */
public class TweLogReceiverImpl {
    public static final String PERMISSION_PACKAGE = "com.tencent.qrom.debugtool";

    private static String TAG = "TweLogReceiverImpl";

    private int mPid = -1;

    public TweLogReceiverImpl() {
        mPid = android.os.Process.myPid();
    }

    public void onReceive(Context context, Intent intent) {
        if (intent != null && context != null) {
            String action = intent.getAction();
            if (TextUtils.isEmpty(action)) {
                TweLog.w(TAG, "onReceive-> action: is empty");
                return;
            }

            if (TextUtils.equals(action, TweLogReceiver.ACTION_FORCE_LOG)) {// 仅接收认证app广播  && verifyPermission(context)
                boolean flag = intent.getBooleanExtra(TweLogReceiver.FORCE_LOG_FLAG, false);
                TweLogImpl.getInstance().setForceLog(flag);
            } else {
                TweLog.i(TAG, "onReceive-> action: " + action);
                if (action.contains(TweLogImpl.getInstance().getPkgName())
                        && action.equals(TweLogUploadImpl.getInstance().getReportTicktAction(context))) { // 上报日志相关信息
                    // 可以通过 pid区分是否是本进程触发的请求，用于多进程去重
                    int reportPid = intent.getIntExtra(TweLogContants.PARAM_KEY_REPORT_PID, -1);
                    int resId = intent.getIntExtra(TweLogContants.PARAM_KEY_REPORT_RESID, 0);
//                    // 获取请求额外数据
//                    Bundle bundle =  intent.getBundleExtra(TweLogContants.PARAM_KEY_REPORT_EXTRA_DATA);
                    TweLog.i("TweLogReceiverImpl", "ACTION_REPORT_LOG_INFO, resId = " + resId + ", report pid = " + reportPid);

                    if (mPid != reportPid) {  //进程号不对，取消操作，（ res id暂不校验）
                        TweLog.w(TAG, "ACTION_REPORT_LOG_INFO -> pid is not match, cancel");
                        return;
                    }

                    AppRomBaseInfo appRomBaseInfo = new AppRomBaseInfo();
                    // 获取的ticket相关数据
                    appRomBaseInfo.mTicket = intent.getStringExtra(TweLogContants.PARAM_KEY_APP_TICKET);
                    // 设置上传环境，和获取ticket的环境一致
                    appRomBaseInfo.mEnvFlg = intent.getIntExtra(TweLogContants.PARAM_KEY_ENV_FLG, AppRomBaseInfo.EVN_FLG_RELEASE);
                    TweLog.i(TAG, "ACTION_REPORT_LOG_INFO, resId = " + resId + ",  mEnvFlg = " + appRomBaseInfo.mEnvFlg);
                    // wup 反馈的相关wup 数据
                    appRomBaseInfo.mGuid = intent.getByteArrayExtra("app_guid");
                    appRomBaseInfo.mQua = intent.getStringExtra("app_qua");
                    appRomBaseInfo.mLc = intent.getStringExtra("app_lc");
                    appRomBaseInfo.mPkgName = intent.getStringExtra("app_pkgName");
                    appRomBaseInfo.mRomId = intent.getLongExtra("app_romId", 0);
                    // imei
                    appRomBaseInfo.mImei = TweLogUtils.getImei(context);

                    int rspCode = intent.getIntExtra(TweLogContants.PARAM_KEY_APP_TICKET_RSPCODE, -99);
                    // 通知上报log msg
                    TweLogUploadImpl.getInstance().sendReceiverLogTicketInfoMsg(appRomBaseInfo, resId, rspCode);
                }  // ~ end 日志上报ticket广播
            }
        }
    }

    private boolean verifyPermission(Context context, String strPkgName) {
        boolean result = false;
        PackageManager pm = context.getPackageManager();
        if (pm != null) {
            try {
                String pkgSignature = getMD5(context, strPkgName);

                for (String signMD5 : WHITE_LIST_SIGNATURES_MD5) {
                    if (signMD5.equals(pkgSignature)) {
                        return true;
                    }
                }
            } catch (Exception ex) {

            }
        }
        return result;
    }

    private boolean verifyPermission(Context context) {

        boolean result = false;
        for (String strPkgName : WHITE_LIST_PKG_NAME) {
            result = verifyPermission(context, strPkgName);
            if (result == true)
                break;
        }

        return result;
    }

    // Twe应用的签名MD5列表
    private static final String[] WHITE_LIST_SIGNATURES_MD5 = new String[]{
            "a481b32f0c9932ad72ad01698d49aff5",
            "7fcce5ba187f4152b8c84cf21c4ce5a9",
            "5e793ae6b959cbc13c0e105f5c46e86b",
            "b8835eafb8c43eb00074f77f2861f7d6",
            "8a05a9f70baffea8419fb107cdac3b83",
            "6c309d850511d545addb3f8a348b5884",
            "2eed75e85b154fb0c1013ecd16115c84",
            "be7bdebf080a81acd9875ecde187913d",
            "78e59f87cd0cc35a467eab0034ed98e3",
            "7fcce5ba187f4152b8c84cf21c4ce5a9",
            "5e793ae6b959cbc13c0e105f5c46e86b",
            "a481b32f0c9932ad72ad01698d49aff5",
            "54be22c232e03c9ebe886667e6857c71",
            "e63d0e11ef7837c5a1beb948742b0cb6"
    };

    private static final String[] WHITE_LIST_PKG_NAME = new String[]{
            "com.telewave.battlecommand",
            "com.telewave.ministation",
            "com.telewave.masses",
    };

    private static final String getMD5(final Context context, final String pkgName) {
        String md5 = "";
        try {
            PackageManager manager = context.getPackageManager();
            PackageInfo packageInfo;
            packageInfo = manager.getPackageInfo(pkgName, PackageManager.GET_SIGNATURES);
            Signature[] signs = packageInfo.signatures;
            for (Signature sign : signs) {
                byte[] hexBytes = sign.toByteArray();
                MessageDigest digest = MessageDigest.getInstance("MD5");
                byte[] md5digest = new byte[0];
                if (digest != null) {
                    md5digest = digest.digest(hexBytes);
                    StringBuilder sb = new StringBuilder();
                    for (int i = 0; md5digest != null && i < md5digest.length; ++i) {
                        sb.append((Integer.toHexString((md5digest[i] & 0xFF) | 0x100)).substring(1, 3));
                    }
                    md5 = sb.toString();
                }
            }

            return md5;
        } catch (NoSuchAlgorithmException e) {
            TweLog.w(TAG, "NoSuchAlgorithmException-> " + e.getMessage());
        } catch (NameNotFoundException e) {
            TweLog.w(TAG, "NameNotFoundException -> " + e.getMessage());
        }

        return md5;
    }


//**********************   2014-12-24  先屏蔽trace上报的相关功能 ******************
//
//    public void onReceive(Context context, Intent intent) {
//        if (intent != null) {
//            String action = intent.getAction();
//            if (TextUtils.isEmpty(action)) {
//                return;
//            }
//
//            if (TextUtils.equals(action, context.getPackageName() + ".ACTION_TRACELOG")) {
//                boolean flag = intent.getBooleanExtra("TRACE_FLAG", false);
//                ArrayList<Integer> modules = intent.getIntegerArrayListExtra("TRACE_MODULES");
//                long expires = intent.getLongExtra("TRACE_EXPIRES", 60 * 60 * 1000);
//                if (flag) {
//                    TweLogImpl.getInstance().openTraceLog(modules, expires);
//                } else {
//                    TweLogImpl.getInstance().closeTraceLog();
//                }
//            } else if (TextUtils.equals(action, TweLogReceiver.ACTION_FORCE_LOG)) {
//                boolean flag = intent.getBooleanExtra(TweLogReceiver.FORCE_LOG_FLAG, false);
//                TweLogImpl.getInstance().setForceLog(flag);
//            }
//        }
//    }
//**********************   2014-12-24  先屏蔽trace上报的相关功能 ******************


}
