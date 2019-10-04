package com.telewave.lib.log.impl;

import android.content.Context;
import android.content.IntentFilter;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import com.telewave.lib.log.TweLog;
import com.telewave.lib.log.TweLogBaseConfig;
import com.telewave.lib.log.TweLogReceiver;
import com.telewave.lib.log.upload.TweLogUploadImpl;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.util.Calendar;

/**
 * @Author: rick_tan
 * @Date: 19-7-20
 * @Version: v1.0
 * @Des TweLogImpl
 * @Modify On 2019-xx-xx by *** for reason ...
 */
public class TweLogImpl {

    private TweLogBaseConfig mConfig = null;

    private HandlerThread mLogThread = null;
    private TweLogHandler mLogHandler = null;

//    private TweTraceParams mTraceParams = null;
//    private boolean mTraceInit = false;

    private TweLogReceiver mLogReceiver;

    private WeakReference<Context> mContextRef = null;

    private static TweLogImpl me = null;

    private static StringBuilder mStrBuilder = new StringBuilder(1024);

    private boolean mDebugable = false;

    private boolean mForceLog = false;

    private int mPid = -1;

    private TweLogImpl() {
        mPid = android.os.Process.myPid();

        try {
            Class<TweLogBaseConfig> clz = (Class<TweLogBaseConfig>) Class.forName("com.telewave.config.TweLogConfig");
            mConfig = clz.newInstance();
        } catch (Exception e) {
            throw new RuntimeException("Missing class TweLogConfig(must implement the abstract class TweLogBaseConfig) " +
                    "in package which is 'twe.citizencard.config'");
        }

        try {
            Class<?> clz = Class.forName(mConfig.getPackageName() + ".BuildConfig");
            Field f = clz.getField("DEBUG");
            mDebugable = f.getBoolean(null);
        } catch (Exception e) {
            mDebugable = false;
        }

        if (!mDebugable) {
            mDebugable = mConfig.isDebug();
        }

        mForceLog = TweLogUtils.getLogCfgSwitch(mConfig.getPackageName());

        if (mLogHandler == null) {
            initLogThread();
            if (mLogThread.getLooper() != null) {
                mLogHandler = new TweLogHandler(mLogThread.getLooper(), mConfig);
                mLogHandler.sendEmptyMessage(TweLogHandler.LOG_MSG_FILE_CLEAN);  // 清理过期日志
            }
        }
    }

    public static TweLogImpl getInstance() {
        if (me == null) {
            me = new TweLogImpl();
        }
        return me;
    }

    public boolean isTweLogOpen() {
        if (!mForceLog && !mDebugable) {
            return false;
        }

        return mConfig.getLogMode() != TweLogBaseConfig.LOG_NONE;
    }

    public void log(char level, String tag, String msg, Throwable tr) {
        if (!mForceLog && !mDebugable) {
            return;
        }

        if (mConfig.getLogMode() == TweLogBaseConfig.LOG_NONE) {
            return;
        }

        if (tag == null) {
            return;
        }

        if (msg != null) {
            msg += '\n';
        } else {
            msg = "[null]\n";
        }

        String throwableStacks = null;
        // 是否需要输出到IDE控制台上
        if (mConfig.getLogMode() == TweLogBaseConfig.LOG_CONSOLE ||
                mConfig.getLogMode() == TweLogBaseConfig.LOG_BOTH) {
            if (tr != null) {
                throwableStacks = TweLog.getStackTraceString(tr);
            }
            switch (level) {
                case 'i':
                    Log.i(tag, tr == null ? msg : msg + throwableStacks);
                    break;

                case 'v':
                    Log.v(tag, tr == null ? msg : msg + throwableStacks);
                    break;

                case 'd':
                    Log.d(tag, tr == null ? msg : msg + throwableStacks);
                    break;

                case 'w':
                    Log.w(tag, tr == null ? msg : throwableStacks);
                    break;

                case 'e':
                    Log.e(tag, tr == null ? msg : msg + throwableStacks);
                    break;

                case 't':
                    Log.wtf(tag, msg, tr);
                    break;

                default:
                    Log.d(tag, msg);
            }
        }

        // 是否需要在file中输出日志
        if (mForceLog || mConfig.getLogMode() == TweLogBaseConfig.LOG_FILE ||
                mConfig.getLogMode() == TweLogBaseConfig.LOG_BOTH) {
            if (throwableStacks == null && tr != null) {
                throwableStacks = TweLog.getStackTraceString(tr);
            }

            switch (level) {
                case 'i':
                    sendHandlerMsg("Info", tag, tr == null ? msg : msg + throwableStacks, false, mPid);
                    break;

                case 'v':
                    sendHandlerMsg("Verbose", tag, tr == null ? msg : msg + throwableStacks, false, mPid);
                    break;

                case 'd':
                    sendHandlerMsg("Debug", tag, tr == null ? msg : msg + throwableStacks, false, mPid);
                    break;

                case 'w':
                    if (msg == null) {
                        sendHandlerMsg("Warn", tag, throwableStacks, false, mPid);
                    } else {
                        sendHandlerMsg("Warn", tag, tr == null ? msg : msg + throwableStacks, false, mPid);
                    }
                    break;

                case 'e':
                    if (msg == null) {
                        sendHandlerMsg("Error", tag, throwableStacks, false, mPid);
                    } else {
                        sendHandlerMsg("Error", tag, tr == null ? msg : msg + throwableStacks, false, mPid);
                    }
                    break;

                case 't':
                    TerribleFailure what = new TerribleFailure(msg, tr);
                    sendHandlerMsg("Assert", tag, TweLog.getStackTraceString(what), false, mPid);
                    break;

                default:
                    sendHandlerMsg("Debug", tag, tr == null ? msg : msg + throwableStacks, false, mPid);
            }
        }
    }

    public void trace(int module, String tag, String msg, Throwable tr) {
        if (!mForceLog && !mDebugable) {
            return;
        }

        if (msg != null) {
            msg += '\n';
        }

        if (tag != null) {
            if (mForceLog || mConfig.getLogMode() == TweLogBaseConfig.LOG_FILE ||
                    mConfig.getLogMode() == TweLogBaseConfig.LOG_BOTH) {
                sendHandlerMsg("Trace", tag, tr == null ? msg : msg + TweLog.getStackTraceString(tr), false, mPid);
            }
        }
    }


    public void crash(String tag, Throwable throwable) {
        if (throwable == null || mConfig.getLogMode() == TweLogBaseConfig.LOG_NONE) {
            return;
        }

        File dir = TweLogUtils.getCrashFileDirectory(mConfig.getPackageName());
        File crashFile;
        if (dir != null) {
            Calendar cal = Calendar.getInstance();
            crashFile = TweLogUtils.createNewFile(dir.getAbsolutePath(), "crash_" + formatDate(cal) + ".log");
            if (crashFile != null) {
                FileWriter writer = null;
                try {
                    writer = new FileWriter(crashFile, true);
                    writer.write(formatLogMessage("Crash", tag, TweLog.getStackTraceString(throwable), mPid));
                    writer.write("\r\n");
                    writer.flush();
                } catch (Exception ex) {
                    System.out.println(ex.getMessage());
                } finally {
                    if (writer != null) {
                        try {
                            writer.close();
                        } catch (IOException e) {
                            System.out.println(e.getMessage());
                        }
                    }
                }
            }
        }
    }

    public void crash(String tag, String msg) {
        if (tag == null || msg == null || mConfig.getLogMode() == TweLogBaseConfig.LOG_NONE) {
            return;
        }

        File dir = TweLogUtils.getCrashFileDirectory(mConfig.getPackageName());
        File crashFile;
        if (dir != null) {
            Calendar cal = Calendar.getInstance();
            crashFile = TweLogUtils.createNewFile(dir.getAbsolutePath(), "crash_" + formatDate(cal) + ".log");
            if (crashFile != null) {
                FileWriter writer = null;
                try {
                    writer = new FileWriter(crashFile, true);
                    writer.write(formatLogMessage("Crash", tag, msg, mPid));
                    writer.write("\r\n");
                    writer.flush();
                } catch (Exception ex) {
                    System.out.println(ex.getMessage());
                } finally {
                    if (writer != null) {
                        try {
                            writer.close();
                        } catch (IOException e) {
                            System.out.println(e.getMessage());
                        }
                    }
                }
            }
        }
    }

    public void setForceLog(boolean isForce) {
        mForceLog = isForce;
    }

    protected void sendHandlerMsg(String level, String tag, String msg, boolean isTrace, int pid) {
        if (mLogHandler != null) {
            Message message;
            if (isTrace) {
                message = mLogHandler.obtainMessage(TweLogHandler.LOG_MSG_TRACE_WRITE, formatLogMessage(level, tag, msg, pid));
            } else {
                message = mLogHandler.obtainMessage(TweLogHandler.LOG_MSG_LOG_WRITE, formatLogMessage(level, tag, msg, pid));
            }
            mLogHandler.sendMessage(message);
        }
    }

    public synchronized void registerLogReceiver(Context context) {
        if (context != null) {
            setContext(context);
        } else {
            return;
        }

        if (mLogReceiver == null) {
            mLogReceiver = new TweLogReceiver();
            IntentFilter filter = new IntentFilter();
            filter.addAction(TweLogReceiver.ACTION_FORCE_LOG);
            filter.addAction(TweLogReceiver.ACTION_TRACE_LOG);
            // 添加接收上报日志所需的相关信息数据
            String pkgName = getPkgName();
            if (pkgName == null || "".equals(pkgName)) {
                pkgName = context.getPackageName();
            }
            filter.addAction(pkgName + TweLogUploadImpl.ACTION_REPORT_LOG_INFO);
//            TweLog.w("=====", "registerReceiver: " + pkgName+TweLogLogUploadImpl.ACTION_REPORT_LOG_INFO);
            //aidanzhang, 临时解决方法，system server中拿到的context, context.getApplicationContext()为null
            if (context.getApplicationContext() != null)
                context.getApplicationContext().registerReceiver(mLogReceiver, filter);
            else
                context.registerReceiver(mLogReceiver, filter);
        }
    }


//    public void trace(int module, String tag, String msg, Throwable tr) {
//        if (!mForceLog && !mDebugable) {
//            return;
//        }
//
//        if (!mTraceInit) {
//            mTraceInit = true;
//            readTraceLogParams();
//        }
//
//        if (mTraceParams == null) {
//            return;
//        }
//
//        if (tag != null) {
//            if (mForceLog || mConfig.getLogMode() == TweLogBaseConfig.LOG_FILE ||
//                    mConfig.getLogMode() == TweLogBaseConfig.LOG_BOTH) {
//                sendHandlerMsg("Trace", tag, tr == null ? msg : msg + '\n' + TweLog.getStackTraceString(tr), false);
//            }
//
//            if (mTraceParams.traceModules.size() > 0) {
//                if (mTraceParams.traceExpires < System.currentTimeMillis()) {
//                    closeTraceLog();
//                } else {
//                    if (mTraceParams.traceModules.contains(module) || mTraceParams.traceModules.contains(TweLogBaseConfig.TRACE_MODULE_ALL)) {
//                        String moduleStr = mConfig.getTraceModules().get(module);
//                        sendHandlerMsg(moduleStr == null ? "UNKNOWN" : moduleStr, tag, msg, true);
//                    }
//                }
//            }
//        }
//    }

//    /**
//     * 打开TraceLog日志
//     *
//     * @param expireTime 多少毫秒后Trace失效
//     */
//    public void openTraceLog(ArrayList<Integer> modules, long expireTime) {
//        if (modules != null && modules.size() > 0 && expireTime > 0) {
//            if (mTraceParams == null) {
//                mTraceParams = new TweTraceParams();
//            } else {
//                mTraceParams.traceModules.clear();
//            }
//
//            mTraceParams.traceModules.addAll(modules);
//            mTraceParams.traceExpires = System.currentTimeMillis() + expireTime;
//        }
//    }
//
//    public void closeTraceLog() {
//        if (mLogHandler == null || mTraceParams == null) {
//            return;
//        }
//
//        Message msg = Message.obtain(mLogHandler, TweLogHandler.LOG_MSG_TRACE_CLOSE);
//        if (mConfig.getLogMode() ==  TweLogBaseConfig.LOG_NONE) {
//            msg.obj = true;
//            mLogHandler.sendMessageAtFrontOfQueue(msg);
//            mLogThread = null;
//            mLogHandler = null;
//            mCalendar = null;
//        } else{
//            mLogHandler.sendMessageAtFrontOfQueue(msg);
//        }
//
//        /* 清空Trace Params */
//        mTraceParams = null;
//    }
//
//    public void registerTraceLogReceiver(Context context) {
//        if (context != null) {
//             setContext(context);
//        } else {
//            return;
//        }
//
//        if (mTraceLogReceiver == null) {
//            String action = context.getPackageName() + ".ACTION_TRACELOG";
//            mTraceLogReceiver = new TweLogReceiver();
//            context.getApplicationContext().registerReceiver(mTraceLogReceiver,
//                    new IntentFilter(action));
//        }
//    }
//
//    public void unregisterTraceLogReceiver(Context context) {
//        if (context != null) {
//            setContext(context);
//        } else {
//            return;
//        }
//
//        if (mTraceLogReceiver != null) {
//            context.getApplicationContext().unregisterReceiver(mTraceLogReceiver);
//            mTraceLogReceiver = null;
//        }
//    }
//
//    public ArrayList<File> prepareUploadTraceLog() {
//        if (mLogHandler != null) {
//            return mLogHandler.prepareUploadTraceLog();
//        } else {
//            return null;
//        }
//    }
//
//    /**
//     * 发广播的方式打开TraceLog日志
//     *
//     * @param expireTime 多少毫秒后Trace失效
//     */
//    public void notifyTraceLogOpened(Context context, ArrayList<Integer> modules, long expireTime) {
//        if (context != null) {
//            setContext(context);
//        } else {
//            return;
//        }
//
//        writeTraceLogParams(modules, 1);
//        Intent intent = new Intent(context.getPackageName() + ".ACTION_TRACELOG");
//        intent.putIntegerArrayListExtra("TRACE_MODULES", modules);
//        if (expireTime > 0) {
//            intent.putExtra("TRACE_EXPIRES", expireTime);
//        }
//        intent.putExtra("TRACE_FLAG", true);
//        context.sendBroadcast(intent);
//    }
//
//    public void notifyTraceLogClosed(Context context) {
//        if (context != null) {
//            setContext(context);
//        } else {
//            return;
//        }
//
//        writeTraceLogParams(null, 0);
//        Intent intent = new Intent(context.getPackageName() + ".ACTION_TRACELOG");
//        intent.putExtra("TRACE_FLAG", false);
//        context.sendBroadcast(intent);
//    }
//
//
//    private void readTraceLogParams() {
//        File ini = new File(TweLogUtils.getLogFileDirectory(mConfig.getPackageName(), true), "trace.ini");
//        if (ini != null && ini.exists()) {
//            BufferedReader br = null;
//            String modules = null;
//            String expires = null;
//            try {
//                br = new BufferedReader(new FileReader(ini));
//                String line;
//                while((line = br.readLine()) != null) {
//                    if (line.startsWith("TraceModules=")) {
//                        modules = line.substring("TraceModules=".length());
//                    } else if (line.startsWith("TraceExpires=")){
//                        expires = line.substring("TraceExpires=".length());
//                    }
//                }
//            } catch (Exception ex) {
//                System.out.println(ex.getMessage());
//            } finally {
//                if (br != null) {
//                    try {
//                        br.close();
//                    } catch (IOException e) {
//                        System.out.println(e.getMessage());
//                    }
//                }
//            }
//
//            if (!TextUtils.isEmpty(modules) && !TextUtils.isEmpty(expires)) {
//                long time = Long.parseLong(expires);
//                if (time > System.currentTimeMillis()) {
//                    String[] array = modules.split(";");
//                    if (array != null && array.length > 0) {
//                        ArrayList<Integer> list = new ArrayList<Integer>();
//                        for (String s : array) {
//                            list.add(Integer.parseInt(s));
//                        }
//
//                        if (mTraceParams == null) {
//                            mTraceParams = new TweTraceParams();
//                        } else {
//                            mTraceParams.traceModules.clear();
//                        }
//                        mTraceParams.traceModules.addAll(list);
//                        mTraceParams.traceExpires = time;
//                    } else {
//                        ini.delete();
//                    }
//                } else {
//                    ini.delete();
//                }
//            } else {
//                ini.delete();
//            }
//        }
//    }
//
//    private void writeTraceLogParams(ArrayList<Integer> modules, int timeOutHours) {
//        if (modules != null && modules.size() > 0) {
//            File dir = TweLogUtils.getLogFileDirectory(mConfig.getPackageName(), true);
//            if (dir == null) {
//               return;
//            }
//
//            // 创建ini文件
//            File ini = new File(dir, "trace.ini");
//            if (!ini.exists()) {
//                try {
//                    if (!ini.createNewFile()) {
//                        return;
//                    }
//                } catch (IOException ex) {
//                    return;
//                }
//            }
//
//            // 解析参数
//            StringBuilder builder = new StringBuilder();
//            int size = modules.size();
//            for (int i = 0; i < size; i++) {
//                if (i != 0) {
//                    builder.append(";");
//                }
//                builder.append(modules.get(i));
//            }
//            long expires = System.currentTimeMillis() + timeOutHours * 60 * 60 * 1000;
//            // 写入参数
//            BufferedWriter bw = null;
//            try {
//                bw = new BufferedWriter(new FileWriter(ini));
//                bw.write("TraceModules=" + builder.toString() + "\r\n");
//                bw.write("TraceExpires=" + String.valueOf(expires) + "\r\n");
//                bw.flush();
//            } catch (Exception ex) {
//                ini.delete();
//            } finally {
//                if (bw != null) {
//                    try {
//                        bw.close();
//                    } catch (IOException e) {
//                        System.out.println(e.getMessage());
//                    }
//                }
//            }
//        } else {
//            File ini = new File(TweLogUtils.getLogFileDirectory(mConfig.getPackageName(), true), "trace.ini");
//            if (ini.exists()) {
//                ini.delete();
//            }
//        }
//    }

    private void setContext(Context context) {
        if (mContextRef == null || mContextRef.get() == null) {
            if (context.getApplicationContext() != null) {
                mContextRef = new WeakReference<Context>(context.getApplicationContext());
            } else {
                mContextRef = new WeakReference<Context>(context);
            }
        }
    }

    /**
     * 获取context
     *
     * @return
     */
    public Context getContext() {
        if (mContextRef != null) {
            return mContextRef.get();
        }
        return null;
    }


    public boolean getDebugMode() {
        return mDebugable;
    }

    private synchronized static String formatLogMessage(String level, String tag, String content, int pid) {

        // %s/thread-%d/%s/%s: %s
        String time = formatTime();
        mStrBuilder.setLength(0);
        mStrBuilder.append(time);
        try {
            mStrBuilder.append("/thread-").append(Thread.currentThread().getId()).append("/");
            mStrBuilder.append(level).append("/").append(tag)
                    .append("(")
                    .append(pid)
                    .append(")").append(": ").append(content.toString());
        } catch (ArrayIndexOutOfBoundsException e) {
            //do nothing
        }
        return mStrBuilder.toString();
    }

    private static String formatTime() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());

        try {
            mStrBuilder.setLength(0);
            int num = calendar.get(Calendar.MONTH) + 1;
            if (num < 10) {
                mStrBuilder.append("0");
            }
            mStrBuilder.append(num);

            mStrBuilder.append("-");
            num = calendar.get(Calendar.DAY_OF_MONTH);
            if (num < 10) {
                mStrBuilder.append("0");
            }
            mStrBuilder.append(num);

            mStrBuilder.append(" ");
            num = calendar.get(Calendar.HOUR_OF_DAY);
            if (num < 10) {
                mStrBuilder.append("0");
            }
            mStrBuilder.append(num);

            mStrBuilder.append(":");
            num = calendar.get(Calendar.MINUTE);
            if (num < 10) {
                mStrBuilder.append("0");
            }
            mStrBuilder.append(num);

            mStrBuilder.append(":");
            num = calendar.get(Calendar.SECOND);
            if (num < 10) {
                mStrBuilder.append("0");
            }
            mStrBuilder.append(num);

            mStrBuilder.append(":");
            num = calendar.get(Calendar.MILLISECOND);
            if (num < 100) {
                mStrBuilder.append("0");
                if (num < 10) {
                    mStrBuilder.append("0");
                }
            }
            mStrBuilder.append(num);
            return mStrBuilder.toString();
        } finally {
            mStrBuilder.setLength(0);
        }
    }

    private synchronized static String formatDate(Calendar calendar) {
        if (calendar == null) {
            calendar = Calendar.getInstance();
        }

        try {
            mStrBuilder.setLength(0);
            int num = calendar.get(Calendar.YEAR);
            mStrBuilder.append(num);

            num = calendar.get(Calendar.MONTH) + 1;
            if (num < 10) {
                mStrBuilder.append("0");
            }
            mStrBuilder.append(num);

            mStrBuilder.append("-");
            num = calendar.get(Calendar.DAY_OF_MONTH);
            if (num < 10) {
                mStrBuilder.append("0");
            }
            mStrBuilder.append(num);
            return mStrBuilder.toString();
        } finally {
            mStrBuilder.setLength(0);
        }
    }

//    private static class TweTraceParams {
//        public Set<Integer> traceModules = null;
//        public long traceExpires = 0;
//        public TweTraceParams() {
//            traceModules = new HashSet<Integer>();
//        }
//    }

    private static class TerribleFailure extends Exception {
        TerribleFailure(String msg, Throwable cause) {
            super(msg, cause);
        }
    }

    private void initLogThread() {
        if (mLogThread == null) {
            mLogThread = new HandlerThread("TweLogThread");
            mLogThread.start();
        }
    }

    public Looper getLogThreadLooper() {

        initLogThread();
        return mLogThread.getLooper();
    }

    public File getLogStoragePath() {
        return TweLogUtils.getLogFileDirectory(mConfig.getPackageName(), false);
    }

    public String getLogStoragePathStr() {
        File logFileDir = TweLogUtils.getLogFileDirectory(mConfig.getPackageName(), false);
        if (logFileDir == null) {
            return null;
        }
        return logFileDir.getAbsolutePath();
    }

    public String getPkgName() {
        if (mConfig != null) {
            return mConfig.getPackageName();
        }
        return null;
    }

    public boolean isTweLoggable(String tag, int logLevel) {
        return Log.isLoggable(tag, logLevel);
    }
}
