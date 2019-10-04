package com.telewave.battlecommand.imui.util;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.inputmethod.InputMethodManager;

import com.telewave.lib.base.AppProxy;

import java.util.List;

/**
 * App系统,运行,相关工具
 *
 * @author PF-NAN
 * @date 2018/3/9
 */

public class IMAppUtil {

    /**
     * 程序是否在前台运行
     *
     * @return
     */
    public static boolean isAppOnForeground() {
        ActivityManager activityManager = (ActivityManager) AppProxy.getContext().getSystemService(Context.ACTIVITY_SERVICE);
        String packageName = AppProxy.getApplication().getPackageName();
        if (null != activityManager) {
            List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
            if (null != appProcesses) {
                for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
                    if (TextUtils.equals(appProcess.processName, packageName)
                            && ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND == appProcess.importance) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * 判断程序是否正在后台运行
     *
     * @return
     */
    public static boolean isAppOnBackground() {
        ActivityManager activityManager = (ActivityManager) AppProxy.getApplication().getSystemService(Context.ACTIVITY_SERVICE);
        String packageName = AppProxy.getApplication().getPackageName();
        if (activityManager != null) {
            List<ActivityManager.RunningTaskInfo> processList = activityManager.getRunningTasks(100);
            for (ActivityManager.RunningTaskInfo info : processList) {
                if (info.topActivity.getPackageName().startsWith(packageName)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 检测APK是否存在
     *
     * @return
     */
    public static boolean checkPackage() {
        boolean flag = false;
        try {
            PackageManager packageManager = AppProxy.getApplication().getPackageManager();
            String packageName = AppProxy.getApplication().getPackageName();
            PackageInfo packageInfo = packageManager.getPackageInfo(packageName, 0);
            if (null != packageInfo) {
                flag = true;
            }
        } catch (PackageManager.NameNotFoundException e) {
            flag = false;
            e.printStackTrace();
        }
        return flag;
    }

    /**
     * 打开App
     */
    public static void openApp() {
        try {
            String packageName = AppProxy.getApplication().getPackageName();
            PackageManager packageManager = AppProxy.getApplication().getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(packageName, 0);
            Intent resolveIntent = new Intent(Intent.ACTION_MAIN, null);
            resolveIntent.addCategory(Intent.CATEGORY_LAUNCHER);
            resolveIntent.setPackage(packageInfo.packageName);
            List<ResolveInfo> apps = packageManager.queryIntentActivities(resolveIntent, 0);

            ResolveInfo ri = apps.iterator().next();
            if (ri != null) {
                String className = ri.activityInfo.name;

                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_LAUNCHER);

                ComponentName cn = new ComponentName(packageName, className);

                intent.setComponent(cn);
                AppProxy.getApplication().startActivity(intent);
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }


    /**
     * 获取版本号
     */
    public static int checkVersionCode() {
        PackageManager packageManager = AppProxy.getApplication().getPackageManager();
        try {
            PackageInfo packInfo = packageManager.getPackageInfo(AppProxy.getApplication().getPackageName(), 0);
            return packInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 获取版本信息
     */
    public static String checkVersionInfo() {
        PackageManager packageManager = AppProxy.getApplication().getPackageManager();
        try {
            PackageInfo packInfo = packageManager.getPackageInfo(AppProxy.getApplication().getPackageName(), 0);
            return packInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 获取安装包中logo
     *
     * @param apkPath
     * @return
     */
    public static Drawable getApkIcon(String apkPath) {
        PackageManager pm = AppProxy.getApplication().getPackageManager();
        PackageInfo info = pm.getPackageArchiveInfo(apkPath, PackageManager.GET_ACTIVITIES);
        if (null != info) {
            ApplicationInfo applicationInfo = info.applicationInfo;
            applicationInfo.sourceDir = apkPath;
            applicationInfo.publicSourceDir = apkPath;
            try {
                return pm.getApplicationIcon(applicationInfo);
            } catch (OutOfMemoryError e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 获取APP包名
     *
     * @return
     */
    public static String getAppName() {
        int pid = android.os.Process.myPid();
        String processName = null;
        ActivityManager am = (ActivityManager) AppProxy.getApplication().getSystemService(Context.ACTIVITY_SERVICE);
        assert am != null;
        List l = am.getRunningAppProcesses();
        for (Object aL : l) {
            ActivityManager.RunningAppProcessInfo info = (ActivityManager.RunningAppProcessInfo) (aL);
            try {
                if (info.pid == pid) {
                    processName = info.processName;
                    return processName;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    //隐藏键盘
    public static boolean hintKeyBoard(Activity activity) {
        //拿到InputMethodManager
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        //如果window上view获取焦点 && view不为空
        if (imm.isActive() && activity.getCurrentFocus() != null) {
            //拿到view的token 不为空
            if (activity.getCurrentFocus().getWindowToken() != null) {
                //表示软键盘窗口总是隐藏，除非开始时以SHOW_FORCED显示。
                imm.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                return true;
            }
        }
        return false;
    }

}