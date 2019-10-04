package com.telewave.battlecommand;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.util.Log;

import com.telewave.battlecommand.service.MyMqttService;

public class MyLifecycleHandler implements Application.ActivityLifecycleCallbacks {
    private static int resumed;
    private static int paused;
    private static int started;
    private static int stopped;

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
    }

    @Override
    public void onActivityStarted(Activity activity) {
        ++started;
    }

    @Override
    public void onActivityResumed(Activity activity) {
        ++resumed;
        if (!MyMqttService.isConnected()) {
            //开启ActiveMQ服务
            MyMqttService.startService(activity);
            Log.e("onActivityResumed", "初始化服务: ");
        } else {
            Log.e("onActivityResumed", "服务连接正常");
        }
    }

    @Override
    public void onActivityPaused(Activity activity) {
        ++paused;
        Log.w("test", "application is in foreground: " + (resumed > paused));
    }

    @Override
    public void onActivityStopped(Activity activity) {
        ++stopped;
        Log.w("test", "application is visible: " + (started > stopped));
    }

    @Override
    public void onActivityDestroyed(Activity activity) {
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
    }

    public static boolean isApplicationVisible() {
        return started > stopped;
    }

    /**
     * 应用在前台
     *
     * @return
     */
    public static boolean isApplicationInForeground() {
        // 当所有 Activity 的状态中处于 resumed 的大于 paused 状态的，即可认为有Activity处于前台状态中
        return resumed > paused;
    }

}
