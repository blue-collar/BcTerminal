package com.telewave.battlecommand.imui.thirdpush;

import android.content.Context;

import com.telewave.lib.router.BaseJumper;
import com.telewave.lib.router.RouterPath;
import com.xiaomi.mipush.sdk.ErrorCode;
import com.xiaomi.mipush.sdk.MiPushClient;
import com.xiaomi.mipush.sdk.MiPushCommandMessage;
import com.xiaomi.mipush.sdk.MiPushMessage;
import com.xiaomi.mipush.sdk.PushMessageReceiver;

import java.util.List;

/**
 * 处理小米离线推送
 */
public class XiaomiMsgReceiver extends PushMessageReceiver {

    private static final String TAG = XiaomiMsgReceiver.class.getSimpleName();
    private String mRegId;

    @Override
    public void onReceivePassThroughMessage(Context context, MiPushMessage miPushMessage) {
        /*onReceivePassThroughMessage is called*/
    }

    @Override
    public void onNotificationMessageClicked(Context context, MiPushMessage miPushMessage) {
        /*点击通知栏处理*/
        // context.startActivity(new Intent(context, SplashActivity.class));
        BaseJumper.jump(RouterPath.SPLASH_ACTIVITY_PATH);
    }

    @Override
    public void onNotificationMessageArrived(Context context, MiPushMessage miPushMessage) {
        /*通知栏收到通知了*/
    }

    @Override
    public void onReceiveRegisterResult(Context context, MiPushCommandMessage miPushCommandMessage) {
        /*onReceiveRegisterResult is called.*/
        String command = miPushCommandMessage.getCommand();
        List<String> arguments = miPushCommandMessage.getCommandArguments();
        String cmdArg1 = ((arguments != null && arguments.size() > 0) ? arguments.get(0) : null);
        if (MiPushClient.COMMAND_REGISTER.equals(command)) {
            if (miPushCommandMessage.getResultCode() == ErrorCode.SUCCESS) {
                mRegId = cmdArg1;
            }
        }
        ThirdPushTokenMgr.getInstance().setThirdPushToken(mRegId);
        ThirdPushTokenMgr.getInstance().setPushTokenToTIM();
    }

    @Override
    public void onCommandResult(Context context, MiPushCommandMessage miPushCommandMessage) {
        super.onCommandResult(context, miPushCommandMessage);
    }
}
