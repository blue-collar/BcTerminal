package com.telewave.battlecommand.receiver;

import android.app.KeyguardManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.telewave.battlecommand.activity.LockScreenMessageActivity;
import com.telewave.battlecommand.mqtt.MqttMessageDto.CallPoliceMessage;
import com.telewave.battlecommand.mqtt.MqttMessageDto.MessageType;
import com.telewave.battlecommand.mqtt.MqttMessageDto.NewDisasterInfo;


/**
 * 监听锁屏消息的广播接收器
 */

public class LockScreenMsgReceiver extends BroadcastReceiver {
    private static final String TAG = "LockScreenMsgReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(TAG, "onReceive:收到了锁屏消息 ");
        String action = intent.getAction();
        if (action.equals("com.telewave.ministation.LockScreenMsgReceiver")) {
            //管理锁屏的一个服务
            KeyguardManager km = (KeyguardManager) context.getSystemService(Context.KEYGUARD_SERVICE);
            String text = km.inKeyguardRestrictedInputMode() ? "锁屏了" : "屏幕亮着的";
            Log.i(TAG, "text: " + text);
            String msgType = intent.getStringExtra("msgType");
            if (msgType.equals(MessageType.NEW_DISASTER_INFO)) {
                NewDisasterInfo.MsgContentBean msgContentBean = (NewDisasterInfo.MsgContentBean)
                        intent.getSerializableExtra("msgContentBean");
                if (km.inKeyguardRestrictedInputMode()) {
                    Log.i(TAG, "onReceive:锁屏了 ");
                    //判断是否锁屏
                    Intent alarmIntent = new Intent(context, LockScreenMessageActivity.class);
                    alarmIntent.putExtra("msgContentBean", msgContentBean);
                    alarmIntent.putExtra("msgType", MessageType.NEW_DISASTER_INFO);
                    //在广播中启动Activity的context可能不是Activity对象，所以需要添加NEW_TASK的标志，否则启动时可能会报错。
                    alarmIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    //启动显示锁屏消息的activity
                    context.startActivity(alarmIntent);
                }
            } else if (msgType.equals(MessageType.RECEIVE_CALL_POLICE_MESSAGE)) {
                CallPoliceMessage callPoliceMessage = (CallPoliceMessage)
                        intent.getSerializableExtra("msgContentBean");
                if (km.inKeyguardRestrictedInputMode()) {
                    Log.i(TAG, "onReceive:锁屏了 ");
                    //判断是否锁屏
                    Intent alarmIntent = new Intent(context, LockScreenMessageActivity.class);
                    alarmIntent.putExtra("msgContentBean", callPoliceMessage);
                    alarmIntent.putExtra("msgType", MessageType.RECEIVE_CALL_POLICE_MESSAGE);
                    //在广播中启动Activity的context可能不是Activity对象，所以需要添加NEW_TASK的标志，否则启动时可能会报错。
                    alarmIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    //启动显示锁屏消息的activity
                    context.startActivity(alarmIntent);
                }
            }
        }
    }
}