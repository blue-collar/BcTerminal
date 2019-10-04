package com.telewave.battlecommand.imui.thirdpush;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.meizu.cloud.pushsdk.MzPushMessageReceiver;
import com.meizu.cloud.pushsdk.handler.MzPushMessage;
import com.meizu.cloud.pushsdk.notification.PushNotificationBuilder;
import com.meizu.cloud.pushsdk.platform.message.PushSwitchStatus;
import com.meizu.cloud.pushsdk.platform.message.RegisterStatus;
import com.meizu.cloud.pushsdk.platform.message.SubAliasStatus;
import com.meizu.cloud.pushsdk.platform.message.SubTagsStatus;
import com.meizu.cloud.pushsdk.platform.message.UnRegisterStatus;
import com.telewave.lib.router.BaseJumper;
import com.telewave.lib.router.RouterPath;

/**
 * 处理魅族离线推送
 */
public class MEIZUPushReceiver extends MzPushMessageReceiver {

    private static final String TAG = MEIZUPushReceiver.class.getSimpleName();

    @Override
    public void onMessage(Context context, String s) {
        Log.e(TAG, "onMessage method1 msg = " + s);
    }

    @Override
    public void onMessage(Context context, String message, String platformExtra) {
        Log.e(TAG, "onMessage method2 msg = " + message + ", platformExtra = " + platformExtra);
    }

    @Override
    public void onMessage(Context context, Intent intent) {
        String content = intent.getExtras().toString();
        Log.e(TAG, "flyme3 onMessage = " + content);
    }

    @Override
    public void onUpdateNotificationBuilder(PushNotificationBuilder pushNotificationBuilder) {
        super.onUpdateNotificationBuilder(pushNotificationBuilder);
    }

    @Override
    public void onNotificationClicked(Context context, MzPushMessage mzPushMessage) {
        /*点击通知栏处理*/
        //context.startActivity(new Intent(context, SplashActivity.class));
        BaseJumper.jump(RouterPath.SPLASH_ACTIVITY_PATH);
    }

    @Override
    public void onNotificationArrived(Context context, MzPushMessage mzPushMessage) {
        super.onNotificationArrived(context, mzPushMessage);
        /*通知栏收到通知了*/
    }

    @Override
    public void onNotificationDeleted(Context context, MzPushMessage mzPushMessage) {
        super.onNotificationDeleted(context, mzPushMessage);
    }

    @Override
    public void onNotifyMessageArrived(Context context, String s) {
        super.onNotifyMessageArrived(context, s);
    }

    @Override
    public void onPushStatus(Context context, PushSwitchStatus pushSwitchStatus) {

    }

    @Override
    public void onRegisterStatus(Context context, RegisterStatus registerStatus) {
        Log.e(TAG, "onRegisterStatus token = " + registerStatus.getPushId());
        ThirdPushTokenMgr.getInstance().setThirdPushToken(registerStatus.getPushId());
        ThirdPushTokenMgr.getInstance().setPushTokenToTIM();

    }

    @Override
    public void onUnRegisterStatus(Context context, UnRegisterStatus unRegisterStatus) {

    }

    @Override
    public void onSubTagsStatus(Context context, SubTagsStatus subTagsStatus) {

    }

    @Override
    public void onSubAliasStatus(Context context, SubAliasStatus subAliasStatus) {

    }

    @Override
    public void onRegister(Context context, String s) {
    }

    @Override
    public void onUnRegister(Context context, boolean b) {

    }
}
