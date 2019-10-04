package com.telewave.lib.base;

import android.app.Application;
import android.content.Context;
import android.content.Intent;

import com.telewave.lib.log.TweLog;

/**
 * @Author: rick_tan
 * @Date: 19-7-20
 * @Version: v1.0
 * @Des 应用程序在底层LibBase处的代理，方便上层调用应用程序的基础数据：1、application 2、isDbug 3、Context
 * 另外，在这里会对LibBase的基础库功能进行初始化
 * @modify On 2018-08-28 by author for reason ...
 */
public class AppProxy {
    private static final String TAG = AppProxy.class.getSimpleName();

    // 表示应用是被杀死后在启动的
    public final static int APP_STATUS_KILLED = 0;
    // 表示应用时正常的启动流程
    public final static int APP_STATUS_NORMAL = 1;
    // 记录App的启动状态
    public static int APP_STATUS = APP_STATUS_KILLED;

    // 注意这里的值,和主工程的渠道配置是对应的
    private static Application sApplication;

    private String VERSION_NAME;
    private String APPLICATION_ID;
    private boolean sIsDebug = false;

    public interface AbnormalStartCallback {
        public void rbnormalStartReInitApp();
    }

    public static AppProxy getInstance() {
        return SingletonHolder.instance;
    }

    /**
     * 静态内部类,只有在装载该内部类时才会去创建单例对象
     */
    private static class SingletonHolder {
        private static final AppProxy instance = new AppProxy();
    }

    public AppProxy init(Application application) {
        return init(application, false);
    }

    public AppProxy init(Application application, boolean productModel) {
        if (null == application) {
            throw new IllegalArgumentException("Illega application Exception, please check~ !");
        }

        AppProxy.sApplication = application;

        // 非productModel初始化 Stetho
        if (!productModel) {
            //^ 如果是生产环境，……
        }

        TweLog.registerLogReceiver(application);

        // init LeakCanary
        // ^ Write code here

        TweLog.d(TAG, "debug? => " + sIsDebug);

        return this;
    }

    AbnormalStartCallback mAbnormalCallback = null;

    public AppProxy setAbnormalStartCallback(AbnormalStartCallback callback) {
        this.mAbnormalCallback = callback;
        return this;
    }

    public AbnormalStartCallback getAbnormalStartCallback() {
        if (null == this.mAbnormalCallback) {
            throw new IllegalAccessError("Illega mAbnormalCallback, call setAbnormalStartCallback first~ !");
        }

        return this.mAbnormalCallback;
    }

    public AppProxy setVERSION_NAME(String versionName) {
        this.VERSION_NAME = versionName;
        return this;
    }

    public String getVERSION_NAME() {
        return VERSION_NAME;
    }

    public AppProxy setAPPLICATION_ID(String versionName) {
        this.APPLICATION_ID = versionName;
        return this;
    }

    public String getAPPLICATION_ID() {
        return APPLICATION_ID;
    }

    public AppProxy setIsDebug(boolean isDebug) {
        sIsDebug = isDebug;
        return this;
    }

    public static Application getApplication() {
        if (null == sApplication) {
            throw new IllegalAccessError("Please initialize the AppProxy first.");
        }

        return sApplication;
    }

    public boolean isDebug() {
        return this.sIsDebug;
    }

    public static Context getContext() {
        if (null == sApplication) {
            throw new IllegalAccessError("Please initialize the AppProxy first.");
        }

        return sApplication.getApplicationContext();
    }

    /**
     * 重新初始化应用界面，清空当前Activity棧，并启动欢迎页面
     */
    public static void reinitAndStartActivity(Class<?> splashActivity) {
        Intent intent = new Intent(getContext(), splashActivity);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        getContext().startActivity(intent);
        android.os.Process.killProcess(android.os.Process.myPid());
    }
}
