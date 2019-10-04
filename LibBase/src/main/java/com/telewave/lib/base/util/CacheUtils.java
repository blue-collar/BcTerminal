package com.telewave.lib.base.util;

import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;

import java.io.File;
import java.math.BigDecimal;

/**
 * @Author: rick_tan
 * @Date: 19-7-23
 * @Version: v1.0
 * @Des 手机缓存工具类
 */
public class CacheUtils {
    /**
     * 获取缓存大小
     *
     * @throws Exception
     */
    public static String getTotalCacheSize(Context context) throws Exception {
        long cacheSize = getFolderSize(context.getCacheDir());
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            cacheSize += getFolderSize(context.getExternalCacheDir());
        }
        return getFormatSizeOnlyWithM(cacheSize);
    }

    /**
     * 获取除白名单目录下的缓存大小
     *
     * @throws Exception
     */
    public static String getTotalCacheSizeExceptWhiteList(Context context, String fileName) throws Exception {
        long cacheSize = getFolderSizeExceptWhiteList(context.getCacheDir(), fileName);
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            cacheSize += getFolderSizeExceptWhiteList(context.getExternalCacheDir(), fileName);
        }
        return getFormatSizeOnlyWithM(cacheSize);
    }

    public static void clearAllCache(Context context) {
        deleteDir(context.getCacheDir(), "");
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            deleteDir(context.getExternalCacheDir(), "");
        }
    }

    /**
     * 清除cache目录下处白名单目录下的所有文件
     *
     * @param context  上下文
     * @param fileName 白名单目录名字
     */
    public static void clearAllCacheExceptWhiteList(Context context, String fileName) {
        deleteDir(context.getCacheDir(), fileName);
        if (Environment.getExternalStorageState().equals("mounted")) {
            deleteDir(context.getExternalCacheDir(), fileName);
        }
    }

    private static boolean deleteDir(File dir, String fileName) {
        if (dir != null && dir.isDirectory()) {
//            String[] children = dir.list();
            File[] files = dir.listFiles();
            for (int i = 0; i < files.length; i++) {
                File child = files[i];
                if (child.isDirectory() && !TextUtils.isEmpty(fileName)
                        && child.getName().equals(fileName)) {//过滤白名单
                    continue;
                }
                boolean success = deleteDir(new File(dir, child.getName()), fileName);
                if (!success) {
                    return false;
                }
            }
        }
        return dir.delete();
    }


    // 获取文件
    //Context.getExternalFilesDir() --> SDCard/Android/data/你的应用的包名/files/ 目录，一般放一些长时间保存的数据
    //Context.getExternalCacheDir() --> SDCard/Android/data/你的应用包名/cache/目录，一般存放临时缓存数据
    public static long getFolderSize(File file) {
        return getFolderSizeExceptWhiteList(file, "");
    }

    private static long getFolderSizeExceptWhiteList(File file, String fileName) {
        long size = 0;
        try {
            File[] fileList = file.listFiles();
            for (int i = 0; i < fileList.length; i++) {
                // 如果下面还有文件
                if (fileList[i].isDirectory()) {
                    if (!TextUtils.isEmpty(fileName) && fileList[i].getName().equals(fileName)) {
                        continue;
                    }
                    size = size + getFolderSize(fileList[i]);
                } else {
                    size = size + fileList[i].length();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return size;
    }


    /**
     * 格式化单位
     */
    public static String getFormatSize(double size) {
        double kiloByte = size / 1024;
        if (kiloByte < 1) {
            return "0KB";
        }

        double megaByte = kiloByte / 1024;
        if (megaByte < 1) {
            BigDecimal result1 = new BigDecimal(Double.toString(kiloByte));
            return result1.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "KB";
        }

        double gigaByte = megaByte / 1024;
        if (gigaByte < 1) {
            BigDecimal result2 = new BigDecimal(Double.toString(megaByte));
            return result2.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "MB";
        }

        double teraBytes = gigaByte / 1024;
        if (teraBytes < 1) {
            BigDecimal result3 = new BigDecimal(Double.toString(gigaByte));
            return result3.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "GB";
        }
        BigDecimal result4 = new BigDecimal(teraBytes);
        return result4.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "TB";
    }

    /**
     * 转换为M 最小0.1M
     */
    public static String getFormatSizeWithM(double size) {
        double kiloByte = size / 1024;
        if (kiloByte < 1) {
//            return size + "Byte";
            return "0K";
        }

        double megaByte = kiloByte / 1024;
        if (megaByte < 1) {
            BigDecimal result1 = new BigDecimal(Double.toString(kiloByte));
            return result1.setScale(2, BigDecimal.ROUND_HALF_UP)
                    .toPlainString() + "KB";
        }

        double gigaByte = megaByte / 1024;
        if (gigaByte < 1) {
            BigDecimal result2 = new BigDecimal(Double.toString(megaByte));
            return result2.setScale(2, BigDecimal.ROUND_HALF_UP)
                    .toPlainString() + "MB";
        }

        double teraBytes = gigaByte / 1024;
        if (teraBytes < 1) {
            BigDecimal result3 = new BigDecimal(Double.toString(gigaByte));
            return result3.setScale(2, BigDecimal.ROUND_HALF_UP)
                    .toPlainString() + "GB";
        }
        BigDecimal result4 = new BigDecimal(teraBytes);
        return result4.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString()
                + "TB";
    }


    /**
     * 转换为M 最小0.1M
     */
    public static String getFormatSizeOnlyWithM(double size) {
        double kiloByte = size / 1024;

        double megaByte = kiloByte / 1024;

        double gigaByte = megaByte / 1024;
        if (gigaByte < 1) {
            if (megaByte < 0.01) return "0.00M";
            BigDecimal result2 = new BigDecimal(Double.toString(megaByte));
            return result2.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "M";
        }

        double teraBytes = gigaByte / 1024;
        if (teraBytes < 1) {
            BigDecimal result3 = new BigDecimal(Double.toString(gigaByte));
            return result3.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "G";
        }
        BigDecimal result4 = new BigDecimal(teraBytes);
        return result4.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "T";
    }
}

