package com.telewave.lib.base.util;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.StatFs;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.text.DecimalFormat;


/**
 * 文件处理类
 *
 * @author liwh
 * @date 2018/6/22
 */
public class FileUtils {

    /**
     * 是否有外存卡
     *
     * @return
     */
    public static boolean isExistExternalStore() {
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 创建文件
     *
     * @param path
     * @return
     */
    public static boolean createFilepath(String path) {
        if (!isExistExternalStore())
            return false;
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
        if (file.exists()) {
            return true;
        } else
            return false;
    }

    /**
     * 创建文件夹及文件
     *
     * @param filePath
     * @param fileName
     * @throws IOException
     */
    public static void createFile(String filePath, String fileName) throws IOException {
        File file = new File(filePath);
        if (!file.exists()) {
            try {
                //按照指定的路径创建文件夹
                file.mkdirs();
            } catch (Exception e) {
                // TODO: handle exception
            }
        }
        File dir = new File(filePath, fileName);
        if (!dir.exists()) {
            try {
                //在指定的文件夹中创建文件
                dir.createNewFile();
            } catch (Exception e) {
            }
        }

    }

    /*
     * Java文件操作 获取文件扩展名
     * */
    public static String getExtensionName(String filename) {
        if ((filename != null) && (filename.length() > 0)) {
            int dot = filename.lastIndexOf('.');
            if ((dot > -1) && (dot < (filename.length() - 1))) {
                return filename.substring(dot + 1);
            }
        }
        return filename.toLowerCase();
    }

    /**
     * 获取外部存储可用容量
     *
     * @return
     */
    public static long getAvailaleSize() {

        File path = Environment.getExternalStorageDirectory(); //取得sdcard文件路径
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSize();
        long availableBlocks = stat.getAvailableBlocks();
        return (availableBlocks * blockSize) / 1024 / 1024;//  MIB单位
    }

    /**
     * 获取文件大小
     *
     * @param f
     * @return
     * @throws Exception
     */
    public static long getFileSize(File f) throws Exception {
        long s = 0;
        if (f.exists()) {
            FileInputStream fis = null;
            fis = new FileInputStream(f);
            s = fis.available();
            fis.close();
        } else {
            f.createNewFile();
            Log.e("debug", "文件夹不存在");
        }
        return s;
    }

    /**
     * 递归 文件夹
     */
    public static long getFileSizes(File f) {
        long size = 0;
        File flist[] = f.listFiles();
        for (int i = 0; i < flist.length; i++) {
            if (flist[i].isDirectory()) {
                size = size + getFileSizes(flist[i]);
            } else {
                size = size + flist[i].length();
            }
        }
        return size;
    }

    /**
     * 转换文件大小 转为kb
     */
    public static String FormentFileSize(long fileS) {
        DecimalFormat df = new DecimalFormat("#");
        return df.format((double) fileS / 1024);
    }

    /**
     * 用UTF8读取一个文件.
     */
    public static String getFileUTF8(String path) {
        String result = "";
        InputStream fin = null;
        try {
            fin = new FileInputStream(path);
            int length = fin.available();
            byte[] buffer = new byte[length];
            fin.read(buffer);
            fin.close();
            result = new String(buffer, "UTF-8");
        } catch (Exception e) {
        }
        return result;
    }

    /**
     * 用UTF8保存一个文件.
     */
    public static void saveFileUTF8(String path, String content, Boolean append) throws IOException {
        FileOutputStream fos = new FileOutputStream(path, append);
        Writer out = new OutputStreamWriter(fos, "UTF-8");
        out.write(content);
        out.flush();
        out.close();
        fos.flush();
        fos.close();
    }

    /**
     * 复制文件. 文件 到 文件
     */
    public static void copyFile(File from, File to) throws IOException {
        if (!from.exists()) {
            throw new IOException("The source file not exist: " + from.getAbsolutePath());
        }
        FileInputStream fis = new FileInputStream(from);
        try {
            copyFile(fis, to);
        } finally {
            fis.close();
        }
    }

    public static long copyFile(InputStream from, File to) throws IOException {
        long totalBytes = 0;
        FileOutputStream fos = new FileOutputStream(to, false);
        try {
            byte[] data = new byte[1024];
            int len;
            while ((len = from.read(data)) > -1) {
                fos.write(data, 0, len);
                totalBytes += len;
            }
            fos.flush();
        } finally {
            fos.close();
        }
        return totalBytes;
    }

    /**
     * 文件复制. path  to  path
     */
    public static boolean copy(String srcFile, String destFile) {
        try {
            FileInputStream in = new FileInputStream(srcFile);
            FileOutputStream out = new FileOutputStream(destFile);
            byte[] bytes = new byte[1024];
            int c;
            while ((c = in.read(bytes)) != -1) {
                out.write(bytes, 0, c);
            }
            in.close();
            out.flush();
            out.close();
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    /**
     * 重命名文件.
     */
    public static boolean renameFile(String resFilePath, String newFilePath) {
        File resFile = new File(resFilePath);
        File newFile = new File(newFilePath);
        return resFile.renameTo(newFile);
    }

    /**
     * 删除指定文件夹下所有文件, 保留文件夹.
     */
    public static boolean delAllFile(String path) {
        boolean flag = false;
        File file = new File(path);
        if (!file.exists()) {
            return flag;
        }
        if (file.isFile()) {
            file.delete();
            return true;
        }
        File[] files = file.listFiles();
        for (int i = 0; i < files.length; i++) {
            File exeFile = files[i];
            if (exeFile.isDirectory()) {
                delAllFile(exeFile.getAbsolutePath());
            } else {
                exeFile.delete();
            }
        }
        return flag;
    }

    /**
     * 删除已存储的文件
     */
    public static void deleteFile(String filePath) {
        try {
            // 找到文件所在的路径并删除该文件
            File file = new File(filePath);
            file.delete();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void deleteFile(File file) {
        // 判断文件是否存在
        if (file.exists()) {
            // 判断是否是文件
            if (file.isFile()) {
                // delete()方法 你应该知道 是删除的意思;
                file.delete();
            }
            // 否则如果它是一个目录
            else if (file.isDirectory()) {
                // 声明目录下所有的文件 files[];
                File files[] = file.listFiles();
                // 遍历目录下所有的文件
                for (int i = 0; i < files.length; i++) {
                    // 把每个文件 用这个方法进行迭代
                    deleteFile(files[i]);
                }
            }
            file.delete();
        } else {
            ToastUtils.toastShortMessage("文件不存在");
        }
    }


    /**
     * 得到SD卡根目录.
     */
    public static File getRootPath() {
        File path = null;
        if (FileUtils.isExistExternalStore()) {
            path = Environment.getExternalStorageDirectory(); // 取得sdcard文件路径
        } else {
            path = Environment.getDataDirectory();
        }
        return path;
    }

    /**
     * 获取文件名字
     *
     * @param filePath
     * @return
     */
    public static String getFileName(String filePath) {
        File file = new File(filePath);
        return file.getName();
    }

    public static void setRead(String path) {
        File device = new File(path);
        if (!device.canRead() || !device.canWrite()) {
            try {
                Process su;
                su = Runtime.getRuntime().exec("su");
                String cmd = "chmod 100 " + device.getAbsolutePath();
                su.getOutputStream().write(cmd.getBytes());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 将图片保存到本地相册路径
     *
     * @param context
     * @param oldPath
     */
    public static void copyFile(final Context context, String oldPath) {
        //系统相册目录
        String galleryPath = Environment.getExternalStorageDirectory()
                + File.separator + Environment.DIRECTORY_DCIM
                + File.separator + "Camera" + File.separator;
        String fileName = "MicroStation_" + oldPath.substring(oldPath.lastIndexOf("/") + 1);
        try {
            int bytesum = 0;
            int byteread = 0;
            File oldfile = new File(oldPath);
            if (oldfile.exists()) { //文件存在时
                InputStream inStream = new FileInputStream(oldPath); //读入原文件
                FileOutputStream fs = new FileOutputStream(galleryPath + fileName);
                byte[] buffer = new byte[1444];
                int length;
                while ((byteread = inStream.read(buffer)) != -1) {
                    //字节数 文件大小
                    bytesum += byteread;
                    System.out.println(bytesum);
                    fs.write(buffer, 0, byteread);
                }
                inStream.close();
            }
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    ToastUtils.toastShortMessage("已保存至本地相册");
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

        Intent intentBroadcast = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File file = new File(galleryPath + fileName);
        intentBroadcast.setData(Uri.fromFile(file));
        context.sendBroadcast(intentBroadcast);

    }
}
