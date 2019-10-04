package com.telewave.battlecommand.imui.thirdpush;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.telewave.lib.router.BaseJumper;
import com.telewave.lib.router.RouterPath;
import com.vivo.push.model.UPSNotificationMessage;
import com.vivo.push.sdk.OpenClientPushMessageReceiver;


public class VIVOPushMessageReceiverImpl extends OpenClientPushMessageReceiver {

    private static final String TAG = VIVOPushMessageReceiverImpl.class.getSimpleName();

    @Override
    public void onNotificationMessageClicked(Context context, UPSNotificationMessage upsNotificationMessage) {
        /*点击通知栏处理*/
        //context.startActivity(new Intent(context, SplashActivity.class));
        BaseJumper.jump(RouterPath.SPLASH_ACTIVITY_PATH);
    }

    @Override
    public void onReceiveRegId(Context context, String regId) {
        // vivo regId有变化会走这个回调。根据官网文档，获取regId需要在开启推送的回调里面调用PushClient.getInstance(getApplicationContext()).getRegId();参考LoginActivity
        Log.e(TAG, "onReceiveRegId = " + regId);
    }
}
