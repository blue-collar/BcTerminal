package com.telewave.battlecommand.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.telewave.battlecommand.service.MyMqttService;
import com.telewave.lib.base.util.SerivceUtils;

/**
 * Intent.ACTION_TIME_TICK广播接收器
 */

public class TimeTickReceiver extends BroadcastReceiver {
    private static final String TAG = "TimeTickReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Intent.ACTION_TIME_TICK)) {
            Log.i(TAG, "onReceive:ACTION_TIME_TICK");
            // 检查Service状态 不运行就重启
            boolean isServiceRunning = SerivceUtils.isServiceWork(context, "com.telewave.battlecommand.service.MyMqttService");
            Log.i(TAG, "isServiceRunning:" + isServiceRunning);
            if (!isServiceRunning) {
                MyMqttService.startService(context);
            }
        }
    }
}