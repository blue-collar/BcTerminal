package com.telewave.lib.base.util;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.util.Log;

import com.telewave.lib.base.AppProxy;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 *
 */

public class AppUtils {
    public static int INSTALL_BAIDU = 1;
    public static int INSTALL_WPS = 2;

    /**
     * 检查手机上是否安装了某APP
     *
     * @param context
     * @return
     */
    public static boolean isAvilible(Context context, String packageName) {
        //获取packagemanager
        final PackageManager packageManager = context.getPackageManager();
        //获取所有已安装程序的包信息
        List<PackageInfo> packageInfos = packageManager.getInstalledPackages(0);
        //用于存储所有已安装程序的包名
        List<String> packageNames = new ArrayList<String>();
        //从pinfo中将包名字逐一取出，压入pName list中
        if (packageInfos != null) {
            for (int i = 0; i < packageInfos.size(); i++) {
                String packName = packageInfos.get(i).packageName;
                packageNames.add(packName);
            }
        }
        //判断packageNames中是否有目标程序的包名，有TRUE，没有FALSE
        return packageNames.contains(packageName);
    }

    public static void InstallApk(Context context, String path, String appName, int tag) {
        ToastUtils.toastShortMessage("正在安装...");
        File f = new File(path + appName);
        if (f.exists()) {
            if (tag == INSTALL_BAIDU) {
                setUrl(path + appName);
                install(context);
                Intent intent = new Intent();
                intent.setAction("updaterBroadcastReceiver");
                context.sendBroadcast(intent);
            } else if (tag == INSTALL_WPS) {
                setUrl(path + appName);
                install(context);
            }
        } else {
            ToastUtils.toastShortMessage("文件不存在");
        }
    }

    // 下载完成后打开安装apk界面
    public static void installApk(File file, Context context) {
        Intent openFile = getFileIntent(file, context);
        context.startActivity(openFile);
    }

    public static Intent getFileIntent(File apkFile, Context context) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        //判断是否是AndroidN以及更高的版本
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            Uri contentUri = FileProvider.getUriForFile(context, AppProxy.getInstance().getAPPLICATION_ID() + ".uikit.fileprovider", apkFile);
            intent.setDataAndType(contentUri, "application/vnd.android.package-archive");
        } else {
            intent.setDataAndType(Uri.fromFile(apkFile), "application/vnd.android.package-archive");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        return intent;
    }

    private static String mUrl;
    private static Context mContext;

    /**
     * 外部传进来的url以便定位需要安装的APK
     *
     * @param url
     */
    public static void setUrl(String url) {
        mUrl = url;
    }

    /**
     * 安装
     *
     * @param context 接收外部传进来的context
     */
    private static void install(Context context) {
        mContext = context;
        Intent intent = new Intent(Intent.ACTION_VIEW);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            Uri contentUri = FileProvider.getUriForFile(context, AppProxy.getInstance().getAPPLICATION_ID() + ".fileprovider", new File(mUrl));
            intent.setDataAndType(contentUri, "application/vnd.android.package-archive");
        } else {
            // 核心是下面几句代码
            intent.setDataAndType(Uri.fromFile(new File(mUrl)),
                    "application/vnd.android.package-archive");
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        mContext.startActivity(intent);
    }

    public static boolean isAppOnForeground(Context context) {
        // Returns a list of application processes that are running on the
        // device
        ActivityManager activityManager = (ActivityManager) context.getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
        String packageName = context.getApplicationContext().getPackageName();

        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager
                .getRunningAppProcesses();
        if (appProcesses == null) {
            return false;
        }
        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            // The name of the process that this object is associated with.
            if (appProcess.processName.equals(packageName)
                    && appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断当前应用是否在最栈顶
     *
     * @param context     上下文对象
     * @param packageName 要判断应用的包名
     * @return boolean
     */
    public static boolean isAppAliveOnTop(Context context, String packageName) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> processInfos = activityManager.getRunningAppProcesses();
        for (int i = 0; i < processInfos.size(); i++) {
            if (processInfos.get(i).processName.equals(packageName)) {
                if (processInfos.get(i).importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND
                        || processInfos.get(i).importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_VISIBLE) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 判断某个Activity 界面是否在前台
     *
     * @param context
     * @param className 某个界面名称
     * @return
     */
    public static boolean isForeground(Context context, String className) {
        if (context == null || TextUtils.isEmpty(className)) {
            return false;
        }

        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> list = am.getRunningTasks(1);
        if (list != null && list.size() > 0) {
            ComponentName cpn = list.get(0).topActivity;
            if (className.equals(cpn.getClassName())) {
                return true;
            }
        }

        return false;

    }

    /**
     * 版本号比较
     *
     * @param version1
     * @param version2
     * @return结果说明：0代表相等，1代表version1大于version2，-1代表version1小于version2
     */
    public static int compareVersion(String version1, String version2) {
        if (version1.equals(version2)) {
            return 0;
        }
        String[] version1Array = version1.split("\\.");
        String[] version2Array = version2.split("\\.");
        Log.e("MainActivity", "version1Array==" + version1Array.length);
        Log.e("MainActivity", "version2Array==" + version2Array.length);
        int index = 0;
        // 获取最小长度值
        int minLen = Math.min(version1Array.length, version2Array.length);
        int diff = 0;
        // 循环判断每位的大小
        Log.e("MainActivity", "verTag2=2222=" + version1Array[index]);
        while (index < minLen
                && (diff = Integer.parseInt(version1Array[index])
                - Integer.parseInt(version2Array[index])) == 0) {
            index++;
        }
        if (diff == 0) {
            // 如果位数不一致，比较多余位数
            for (int i = index; i < version1Array.length; i++) {
                if (Integer.parseInt(version1Array[i]) > 0) {
                    return 1;
                }
            }

            for (int i = index; i < version2Array.length; i++) {
                if (Integer.parseInt(version2Array[i]) > 0) {
                    return -1;
                }
            }
            return 0;
        } else {
            return diff > 0 ? 1 : -1;
        }
    }

}
