package com.telewave.battlecommand.imui.thirdpush;

import android.text.TextUtils;
import android.util.Log;

import com.telewave.battlecommand.imui.service.Constants;
import com.tencent.imsdk.TIMCallBack;
import com.tencent.imsdk.TIMManager;
import com.tencent.imsdk.TIMOfflinePushToken;
import com.tencent.imsdk.utils.IMFunc;

/**
 * 用来保存厂商注册离线推送token的管理类示例，当登陆im后，通过 setOfflinePushToken 上报证书 ID 及设备 token 给im后台。开发者可以根据自己的需求灵活实现
 */

public class ThirdPushTokenMgr {

    private static final String TAG = ThirdPushTokenMgr.class.getSimpleName();
    private String mThirdPushToken;
    private boolean mIsTokenSet = false;
    private boolean mIsLogin = false;

    public static ThirdPushTokenMgr getInstance() {
        return ThirdPushTokenHolder.instance;
    }

    private static class ThirdPushTokenHolder {
        private static final ThirdPushTokenMgr instance = new ThirdPushTokenMgr();
    }

    public void setIsLogin(boolean isLogin) {
        mIsLogin = isLogin;
    }

    public String getThirdPushToken() {
        return mThirdPushToken;
    }

    public void setThirdPushToken(String mThirdPushToken) {
        this.mThirdPushToken = mThirdPushToken;
    }

    public void setPushTokenToTIM() {
        if (mIsTokenSet) {
            Log.e(TAG, "setPushTokenToTIM mIsTokenSet true, ignore");
            return;
        }
        String token = ThirdPushTokenMgr.getInstance().getThirdPushToken();
        if (TextUtils.isEmpty(token)) {
            Log.e(TAG, "setPushTokenToTIM third token is empty");
            mIsTokenSet = false;
            return;
        }
        if (!mIsLogin) {
            Log.e(TAG, "setPushTokenToTIM not login, ignore");
            return;
        }
        TIMOfflinePushToken param = null;
        if (IMFunc.isBrandXiaoMi()) {
            param = new TIMOfflinePushToken(Constants.XM_PUSH_BUZID, token);
        } else if (IMFunc.isBrandHuawei()) {
            param = new TIMOfflinePushToken(Constants.HW_PUSH_BUZID, token);
        } else if (IMFunc.isBrandMeizu()) {
            param = new TIMOfflinePushToken(Constants.MZ_PUSH_BUZID, token);
        } else if (IMFunc.isBrandOppo()) {

        } else if (IMFunc.isBrandVivo()) {
            param = new TIMOfflinePushToken(Constants.VIVO_PUSH_BUZID, token);
        } else {
            return;
        }
        TIMManager.getInstance().setOfflinePushToken(param, new TIMCallBack() {
            @Override
            public void onError(int code, String desc) {
                Log.e(TAG, "setOfflinePushToken err code = " + code);
            }

            @Override
            public void onSuccess() {
                Log.e(TAG, "setOfflinePushToken success");
                mIsTokenSet = true;
            }
        });
    }
}
