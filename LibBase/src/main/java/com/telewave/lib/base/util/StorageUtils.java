package com.telewave.lib.base.util;

import android.content.Context;
import android.os.Environment;

import java.io.File;
import java.io.IOException;

/**
 * Created by yintangwen952 on 2018/9/2.
 */

public class StorageUtils {

    /* Checks if external storage is available for read and write */
    public static boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state);
    }

    /* Checks if external storage is available to at least read */
    public static boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state)
                || Environment.MEDIA_MOUNTED_READ_ONLY.equals(state);
    }

    public static void disableMediaScan(File deviceIdFileDir) {
        File nomediaFile = new File(deviceIdFileDir, ".nomedia");
        if (!nomediaFile.exists())
            try {
                nomediaFile.createNewFile();
            } catch (IOException e) {
            }
    }

    /**
     * 获取App外存根目录
     * <p>
     * <p>1. 系统维护的目录，app卸载时此目录中文件一并删除
     * <p>2. 4.4后读写app目录不用声明权限
     *
     * @param context
     * @return 外存不存在则返回null
     */
    public static String getAppRoot(Context context) {
        String root = null;
        File file = context.getExternalFilesDir(null);
        if (file != null) {
            root = file.getAbsolutePath();
        }
        return root;
    }

    /**
     * 获取App外存指定目录
     * <p>
     * <p>1. 系统维护的目录，app卸载时此目录中文件一并删除
     * <p>2. 4.4后读写app目录不用声明权限
     *
     * @param context
     * @param specDir 指定目录名称
     * @return 外存不存在则返回null
     */
    public static String getAppDir(Context context, String specDir) {
        String dir = null;
        File file = context.getExternalFilesDir(specDir);
        if (file != null) {
            dir = file.getAbsolutePath();
        } else {
            dir = new File(context.getCacheDir(), specDir).getAbsolutePath();
        }

        return dir;
    }

    /**
     * 获取App外部缓存目录
     *
     * @param context
     * @param specDir
     * @return 无外存则返回null
     */
    public static String getAppCacheDir(Context context, String specDir) {
        String dir = null;
        File file = context.getExternalCacheDir();
        if (file != null) {
            file = new File(file.getAbsolutePath(), specDir);
            file.mkdirs();
            dir = file.getAbsolutePath();
        }
        return dir;
    }

}

