package com.telewave.battlecommand.imui.thirdpush;

import android.content.Context;
import android.os.Bundle;

import com.huawei.hms.support.api.push.PushReceiver;
import com.tencent.qcloud.tim.uikit.utils.TUIKitLog;

import java.nio.charset.StandardCharsets;

/**
 * 处理华为离线推送
 */
public class HUAWEIPushReceiver extends PushReceiver {

    @Override
    public boolean onPushMsg(Context context, byte[] msgBytes, Bundle extras) {
        try {
            //CP可以自己解析消息内容，然后做相应的处理
            String content = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                content = new String(msgBytes, StandardCharsets.UTF_8);
            }
            TUIKitLog.e("收到PUSH透传消息,消息内容为HUAWEIPushReceiver : " + content);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public void onToken(Context context, String token, Bundle extras) {
        TUIKitLog.e("onToken =HUAWEIPushReceiver= " + token);
        ThirdPushTokenMgr.getInstance().setThirdPushToken(token);
        ThirdPushTokenMgr.getInstance().setPushTokenToTIM();
    }
}
