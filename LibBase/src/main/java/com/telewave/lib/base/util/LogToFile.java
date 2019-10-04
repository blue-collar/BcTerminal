package com.telewave.lib.base.util;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class LogToFile {
    private static String logPath = null;//log日志存放路径

    private static SimpleDateFormat dateFormatFileName = new SimpleDateFormat("yyyy-MM-dd", Locale.US);//日期格式;
    private static SimpleDateFormat dateFormatLogTime = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss", Locale.US);//日期格式;

    private static Date date = null;
    //因为log日志是使用日期命名的，使用静态成员变量主要是为了在整个程序运行期间只存在一个.log文件中;

    private static boolean isDebug = true;
    private static boolean isLogDebug = true;

    /**
     * 初始化，须在使用之前设置，最好在Application创建时调用
     *
     * @param context context
     */
    public static void init(Context context) {
        logPath = getFilePath(context) + "/ULogs";//获得文件储存路径,在后面加"/Logs"建立子文件夹
    }

    public static void isOpenDebug(boolean isOpen) {
        isDebug = isOpen;
    }

    /**
     * 获得文件存储路径
     *
     * @return 文件存储路径
     */
    private static String getFilePath(Context context) {
        String dir = null;
        String state = Environment.getExternalStorageState();
        boolean emulated = true;
        emulated = Environment.isExternalStorageEmulated();
        if (!"mounted".equals(state) || !emulated && Environment.isExternalStorageRemovable()) {
            dir = context.getFilesDir().getAbsolutePath();
        } else {
            dir = Environment.getExternalStorageDirectory().getAbsolutePath();
        }
        PackageManager pm = context.getPackageManager();
        String appName = context.getApplicationInfo().loadLabel(pm).toString();
        Log.e("getFilePath", "getFilePath: " + dir + "/" + appName);
        return dir + "/" + appName;
    }

    private static final char VERBOSE = 'v';

    private static final char DEBUG = 'd';

    private static final char INFO = 'i';

    private static final char WARN = 'w';

    private static final char ERROR = 'e';

    public static void v(String tag, String msg) {
        if (isLogDebug) {
            Log.v(tag, msg);
        }
        writeToFile(VERBOSE, tag, msg);
    }

    public static void d(String tag, String msg) {
        if (isLogDebug) {
            Log.d(tag, msg);
        }
        writeToFile(DEBUG, tag, msg);
    }

    public static void i(String tag, String msg) {
        if (isLogDebug) {
            Log.i(tag, msg);
        }
        writeToFile(INFO, tag, msg);
    }

    public static void w(String tag, String msg) {
        if (isLogDebug) {
            Log.w(tag, msg);
        }
        writeToFile(WARN, tag, msg);
    }

    public static void e(String tag, String msg) {
        if (isLogDebug) {
            Log.e(tag, msg);
        }
        writeToFile(ERROR, tag, msg);
    }

    /**
     * 将log信息写入文件中
     *
     * @param type 类型
     * @param tag  类名
     * @param msg  消息内容
     */
    private static void writeToFile(char type, String tag, String msg) {

        if (null == logPath) {
            Log.e("LogToFile", "logPath == null ，未初始化LogToFile");
            return;
        }
        if (!isDebug) {
            return;
        }
        date = new Date(System.currentTimeMillis());
        String fileName = logPath + "/log_" + dateFormatFileName.format(date) + ".txt";//log日志名，使用时间命名，保证不重复
        String log = dateFormatLogTime.format(date) + " " + tag + "-" + type + ":" + msg + "\n";//log日志内容，可以自行定制

        //如果父路径不存在
        File file = new File(logPath);
        if (!file.exists()) {
            if (!file.mkdirs()) {
                return;
            }
        }

        FileOutputStream fos = null;//FileOutputStream会自动调用底层的close()方法，不用关闭
        BufferedWriter bw = null;
        try {

            fos = new FileOutputStream(fileName, true);//这里的第二个参数代表追加还是覆盖，true为追加，flase为覆盖
            bw = new BufferedWriter(new OutputStreamWriter(fos));
            bw.write(log);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (bw != null) {
                    bw.close();//关闭缓冲流
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

}