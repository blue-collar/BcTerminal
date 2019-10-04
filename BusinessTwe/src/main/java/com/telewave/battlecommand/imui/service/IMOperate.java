package com.telewave.battlecommand.imui.service;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.huawei.android.hms.agent.HMSAgent;
import com.huawei.android.hms.agent.common.handler.ConnectHandler;
import com.huawei.android.hms.agent.push.handler.EnableReceiveNotifyMsgHandler;
import com.huawei.android.hms.agent.push.handler.GetTokenHandler;
import com.meizu.cloud.pushsdk.PushManager;
import com.meizu.cloud.pushsdk.util.MzSystemUtils;
import com.telewave.battlecommand.activity.LoginActivity;
import com.telewave.battlecommand.imui.thirdpush.ThirdPushTokenMgr;
import com.telewave.lib.widget.util.IMActivityUtil;
import com.telewave.battlecommand.imui.util.IMInformOperate;
import com.telewave.battlecommand.service.FloatingCallService;
import com.telewave.battlecommand.trtc.TRTCCallMessageManager;
import com.telewave.battlecommand.trtc.TRTCCallReceiveActivity;
import com.telewave.battlecommand.trtc.bean.TRTCCallMessage;
import com.telewave.lib.base.AppProxy;
import com.telewave.lib.base.ConstData;
import com.telewave.lib.base.Constents;
import com.telewave.lib.base.util.DateUtils;
import com.telewave.lib.base.util.SharePreferenceUtils;
import com.telewave.lib.base.util.ToastUtils;
import com.tencent.imsdk.TIMBackgroundParam;
import com.tencent.imsdk.TIMCallBack;
import com.tencent.imsdk.TIMConversation;
import com.tencent.imsdk.TIMCustomElem;
import com.tencent.imsdk.TIMElem;
import com.tencent.imsdk.TIMElemType;
import com.tencent.imsdk.TIMFriendAllowType;
import com.tencent.imsdk.TIMFriendshipManager;
import com.tencent.imsdk.TIMGroupTipsElem;
import com.tencent.imsdk.TIMLogLevel;
import com.tencent.imsdk.TIMManager;
import com.tencent.imsdk.TIMMessage;
import com.tencent.imsdk.TIMSdkConfig;
import com.tencent.imsdk.TIMUserProfile;
import com.tencent.imsdk.TIMValueCallBack;
import com.tencent.imsdk.session.SessionWrapper;
import com.tencent.imsdk.utils.IMFunc;
import com.tencent.qcloud.tim.uikit.TUIKit;
import com.tencent.qcloud.tim.uikit.base.IMEventListener;
import com.tencent.qcloud.tim.uikit.base.IUIKitCallBack;
import com.tencent.qcloud.tim.uikit.bean.IMUserExtObj;
import com.tencent.qcloud.tim.uikit.config.CustomFaceConfig;
import com.tencent.qcloud.tim.uikit.config.GeneralConfig;
import com.tencent.qcloud.tim.uikit.config.TUIKitConfigs;
import com.tencent.qcloud.tim.uikit.utils.IMJsonUtil;
import com.tencent.qcloud.tim.uikit.utils.TUIKitLog;
import com.vivo.push.PushClient;
import com.xiaomi.mipush.sdk.MiPushClient;

import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.List;

/**
 * IM
 *
 * @author PF-NAN
 * @date 2019-07-23
 */
public class IMOperate {
    private static IMOperate mInstance;
    private WeakReference<Activity> mWeakActivity = null;

    private IMOperate() {
    }

    public static IMOperate getInstance() {
        if (null == mInstance) {
            synchronized (IMOperate.class) {
                if (null == mInstance) {
                    mInstance = new IMOperate();
                }
            }
        }
        return mInstance;
    }

    /**
     * 初始化
     */
    public void initIM() {
        if (SessionWrapper.isMainProcess(AppProxy.getContext())) {
            TUIKitConfigs configs = TUIKit.getConfigs();
            TIMSdkConfig timSdkConfig = new TIMSdkConfig(Constants.SDKAPPID);
            timSdkConfig.setLogLevel(TIMLogLevel.ERROR);
            timSdkConfig.enableLogPrint(true);
            configs.setSdkConfig(timSdkConfig);
            configs.setCustomFaceConfig(new CustomFaceConfig());
            configs.setGeneralConfig(new GeneralConfig());
            TUIKit.init(AppProxy.getContext(), Constants.SDKAPPID, configs);
            if (IMFunc.isBrandXiaoMi()) {
                // 小米离线推送
                TUIKitLog.e("=====小米离线推送==--=-=-=-=-=-=-=-=-=-=-=-=-=-=");
                MiPushClient.registerPush(AppProxy.getContext(), Constants.XM_PUSH_APPID, Constants.XM_PUSH_APPKEY);
            }
            if (IMFunc.isBrandHuawei()) {
                // 华为离线推送
                TUIKitLog.e("=====华为离线推送==--=-=-=-=-=-=-=-=-=-=-=-=-=-=");
                HMSAgent.init(AppProxy.getApplication());
            }
            if (MzSystemUtils.isBrandMeizu(AppProxy.getContext())) {
//                 魅族离线推送
                PushManager.register(AppProxy.getContext(), Constants.MZ_PUSH_APPID, Constants.MZ_PUSH_APPKEY);
            }
            if (IMFunc.isBrandVivo()) {
                // vivo离线推送
                PushClient.getInstance(AppProxy.getContext()).initialize();
            }


            AppProxy.getApplication().registerActivityLifecycleCallbacks(new Application.ActivityLifecycleCallbacks() {
                private int foregroundActivities = 0;
                private boolean isChangingConfiguration;

                @Override
                public void onActivityCreated(Activity activity, Bundle bundle) {

                }

                @Override
                public void onActivityStarted(final Activity activity) {
                    foregroundActivities++;
                    if (foregroundActivities == 1 && !isChangingConfiguration) {
                        // 应用切到前台
                        TIMManager.getInstance().doForeground(new TIMCallBack() {
                            @Override
                            public void onError(int code, String desc) {
                                TUIKitLog.e("doForeground err = " + code + ", desc = " + desc);
                            }

                            @Override
                            public void onSuccess() {
                                TUIKitLog.e("doForeground success");
//                                login(activity);
                            }
                        });
                    }
                    isChangingConfiguration = false;
                }

                @Override
                public void onActivityResumed(Activity activity) {

                }

                @Override
                public void onActivityPaused(Activity activity) {

                }

                @Override
                public void onActivityStopped(Activity activity) {
                    foregroundActivities--;
                    if (foregroundActivities == 0) {
                        // 应用切到后台
//                        int unReadCount = 0;
//                        List<TIMConversation> conversationList = TIMManager.getInstance().getConversationList();
//                        for (TIMConversation timConversation : conversationList) {
//                            unReadCount += timConversation.getUnreadMessageNum();
//                        }
                        TIMBackgroundParam param = new TIMBackgroundParam();
                        param.setC2cUnread(0);
                        param.setGroupUnread(0);
                        TIMManager.getInstance().doBackground(param, new TIMCallBack() {
                            @Override
                            public void onError(int code, String desc) {
                                TUIKitLog.e("doBackground err = " + code + ", desc = " + desc);
                            }

                            @Override
                            public void onSuccess() {
                                TUIKitLog.e("doBackground success");
                            }
                        });
                    }
                    isChangingConfiguration = activity.isChangingConfigurations();
                }

                @Override
                public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {

                }

                @Override
                public void onActivityDestroyed(Activity activity) {

                }
            });
        }
    }

    /**
     * 更新用户配置文件
     */
    public void updateUserProfile() {
        ConstData.username = SharePreferenceUtils.getDataSharedPreferences(AppProxy.getContext(), "username");
        if (!TextUtils.isEmpty(ConstData.username)) {
            TIMFriendshipManager.getInstance().getSelfProfile(new TIMValueCallBack<TIMUserProfile>() {
                @Override
                public void onError(int code, String desc) {
                    modifySelfProfile();

                }

                @Override
                public void onSuccess(TIMUserProfile timUserProfile) {
                    TUIKitLog.e("USER", timUserProfile.toString());

                    modifySelfProfile();
                }
            });
        }

    }

    private void modifySelfProfile() {
        // 登录后，设置pushtoken
        ThirdPushTokenMgr.getInstance().setIsLogin(true);
        ThirdPushTokenMgr.getInstance().setPushTokenToTIM();
        HashMap<String, Object> hashMap = new HashMap<>();
        // 昵称
        hashMap.put(TIMUserProfile.TIM_PROFILE_TYPE_KEY_NICK, ConstData.username);
        /*允许任何人添加为好友*/
        hashMap.put(TIMUserProfile.TIM_PROFILE_TYPE_KEY_ALLOWTYPE, TIMFriendAllowType.TIM_FRIEND_ALLOW_ANY);
        // 个性签名
        hashMap.put(TIMUserProfile.TIM_PROFILE_TYPE_KEY_SELFSIGNATURE, "");

        String organid = SharePreferenceUtils.getDataSharedPreferences(AppProxy.getContext(), "organid");
        String organName = SharePreferenceUtils.getDataSharedPreferences(AppProxy.getContext(), "organName");

        // 设置角色信息
        hashMap.put(TIMUserProfile.TIM_PROFILE_TYPE_KEY_CUSTOM_PREFIX + "ExtData", IMJsonUtil.obj2Json(new IMUserExtObj(organid, organName)));
        // 地区
        hashMap.put(TIMUserProfile.TIM_PROFILE_TYPE_KEY_LOCATION, "sz"); // TODO 不加SDK会有个崩溃
        TIMFriendshipManager.getInstance().modifySelfProfile(hashMap, new TIMCallBack() {
            @Override
            public void onError(int i, String s) {

            }

            @Override
            public void onSuccess() {
            }
        });

    }

    /**
     * 登录
     *
     * @param activity
     */
    public void login(final Activity activity) {
        TUIKitLog.e("-------login----" + activity);
        if (null != activity) {
            mWeakActivity = new WeakReference<>(activity);
        }
        ConstData.userid = SharePreferenceUtils.getDataSharedPreferences(AppProxy.getContext(), "userid");
        ConstData.userSig = SharePreferenceUtils.getDataSharedPreferences(AppProxy.getContext(), "userSig");
        TUIKit.login(ConstData.userid, ConstData.userSig, new IUIKitCallBack() {
            @Override
            public void onSuccess(Object data) {
                TUIKitLog.e(ConstData.userid + "======onSuccess-------IM---" + ConstData.userSig);
                updateUserProfile();
            }

            @Override
            public void onError(String module, int errCode, String errMsg) {
                if (errCode == 6208 || errCode == 70001) {
                    ToastUtils.toastLongMessage("您的帐号已在其它终端登录");
                    onForceOffline(mWeakActivity.get());
                }
            }
        });
    }

    /**
     * 登录
     *
     * @param callBack
     */
    public void login(IUIKitCallBack callBack) {
        ConstData.userid = SharePreferenceUtils.getDataSharedPreferences(AppProxy.getContext(), "userid");
        ConstData.userSig = SharePreferenceUtils.getDataSharedPreferences(AppProxy.getContext(), "userSig");
        TUIKit.login(ConstData.userid, ConstData.userSig, callBack);
    }

    /**
     * 退出IM
     *
     * @param loginOutInter
     */
    public void loginOut(final IMLoginOutInter loginOutInter) {
        TIMManager.getInstance().logout(new TIMCallBack() {
            @Override
            public void onError(int code, String desc) {
                TUIKit.unInit();
                if (null != loginOutInter) {
                    loginOutInter.success();
                }
//                editHuaWeiPushState(false);
            }

            @Override
            public void onSuccess() {
                TUIKit.unInit();
                if (null != loginOutInter) {
                    loginOutInter.success();
                }
//                editHuaWeiPushState(false);
            }
        });
    }


    public interface IMLoginOutInter {
        void success();
    }

    /**
     * 设置监听
     */
    public void setIMEventListener(final Activity activity) {
        mWeakActivity = new WeakReference<>(activity);
        TUIKit.setIMEventListener(new IMEventListener() {
            @Override
            public void onForceOffline() {
                super.onForceOffline();
                IMOperate.getInstance().onForceOffline(mWeakActivity.get());
            }

            @Override
            public void onUserSigExpired() {
                super.onUserSigExpired();
            }

            @Override
            public void onConnected() {
                super.onConnected();
            }

            @Override
            public void onDisconnected(int code, String desc) {
                super.onDisconnected(code, desc);
            }

            @Override
            public void onWifiNeedAuth(String name) {
                super.onWifiNeedAuth(name);
            }

            @Override
            public void onRefreshConversation(List<TIMConversation> conversations) {
                super.onRefreshConversation(conversations);
            }

            @Override
            public void onNewMessages(List<TIMMessage> msgs) {
                super.onNewMessages(msgs);
                if (!msgs.isEmpty()) {
                    for (TIMMessage timMessage : msgs) {
                        if (TIMElemType.ProfileTips != timMessage.getElement(0).getType()) {
                            IMInformOperate.getInstance().createIMInform(timMessage);
                        }
                    }
                    TIMMessage lastTimMessage = msgs.get(msgs.size() - 1);
                    Log.e("NNN", "onNewMessages: " + lastTimMessage);
                    if (lastTimMessage != null) {
                        TIMElem timElem = lastTimMessage.getElement(0);
                        if (timElem.getType() == TIMElemType.Custom) {
                            TIMCustomElem customElem = (TIMCustomElem) timElem;
                            String data = new String(customElem.getData());
                            try {
                                JSONObject jsonObject = new JSONObject(data);
                                String msgType = jsonObject.getString("msgType");
                                if (!TextUtils.isEmpty(msgType)) {
                                    if (Constents.AUDIO_CALL_MESSAGE_DESC.equals(msgType) ||
                                            Constents.VIDEO_CALL_MESSAGE_DESC.equals(msgType)) {
                                        Gson gson = new Gson();
                                        String msgContent = jsonObject.getString("msgContent");
                                        TRTCCallMessage trtcCallMessage = gson.fromJson(msgContent, TRTCCallMessage.class);
                                        //没有在通话中
                                        if (!ConstData.isEnterTRTCCALL) {
                                            long nowTime = System.currentTimeMillis() / 1000;
                                            if (nowTime - lastTimMessage.timestamp() <= 5) {
                                                IMOperate.getInstance().onReceiveTRTCCallMessage(activity, trtcCallMessage);
                                            }
                                        }
                                        //已经进入通话中
                                        else {
                                            String trtcCallType = trtcCallMessage.getTrtcCallType();
                                            int roomId = trtcCallMessage.getTrtcCallRoomId();
                                            if (ConstData.isEnterTRTCCALL && !TextUtils.isEmpty(ConstData.currentRoomId)) {
                                                int currentRoom = Integer.parseInt(ConstData.currentRoomId);
                                                if (currentRoom != roomId) {
                                                    Intent intent = new Intent(activity, FloatingCallService.class);
                                                    intent.putExtra("TRTCCallMessage", trtcCallMessage);
                                                    activity.startService(intent);
                                                } else {
                                                    lastTimMessage.remove();
                                                }
                                            } else {
                                                Intent intent = new Intent(activity, FloatingCallService.class);
                                                intent.putExtra("TRTCCallMessage", trtcCallMessage);
                                                activity.startService(intent);
                                            }
                                        }
                                        TIMConversation conversation = lastTimMessage.getConversation();
                                        //将此会话的该条消息标记为已读
                                        //将此会话的 timMessage 代表的消息及这个消息之前的所有消息标记为已读
                                        conversation.setReadMessage(lastTimMessage, new TIMCallBack() {
                                            @Override
                                            public void onError(int code, String desc) {
                                                Log.d("NNN", "send message failed. code: " + code + " errmsg: " + desc);
                                            }

                                            @Override
                                            public void onSuccess() {
                                                Log.e("NNN", "SendMsg ok");
                                            }
                                        });

                                    } else if (Constents.AUDIO_CALL_MESSAGE_DECLINE_DESC.equals(msgType)) {
                                        TRTCCallMessageManager.getInstance().sendTRTCAudioCallMessageCancel();
                                        lastTimMessage.remove();
                                    } else if (Constents.VIDEO_CALL_MESSAGE_DECLINE_DESC.equals(msgType)) {
                                        TRTCCallMessageManager.getInstance().sendTRTCVideoCallMessageCancel();
                                        lastTimMessage.remove();
                                    }
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }

            @Override
            public void onGroupTipsEvent(TIMGroupTipsElem elem) {
                super.onGroupTipsEvent(elem);
            }
        });
    }

    /**
     * 接收音视频消息
     *
     * @param activity
     */
    private void onReceiveTRTCCallMessage(Activity activity, TRTCCallMessage trtcCallMessage) {
        Intent intent = new Intent(activity, TRTCCallReceiveActivity.class);
        intent.putExtra("TRTCCallMessage", trtcCallMessage);
        activity.startActivity(intent);
    }

    /**
     * 退出处理
     */
    public void onForceOffline(Activity activity) {
        mWeakActivity = new WeakReference<>(activity);
        ToastUtils.toastLongMessage("您的帐号已在其它终端登录");
        DateUtils.clear(mWeakActivity.get());
        IMActivityUtil.finishAllActivity();
        Intent intent = new Intent(mWeakActivity.get(), LoginActivity.class);
        mWeakActivity.get().startActivity(intent);
        mWeakActivity.get().finish();
    }

    /**
     * 初始化并连接华为推送
     *
     * @param activity
     */
    public void initHuaWeiPush(Activity activity) {
        if (IMFunc.isBrandHuawei()) {
            HMSAgent.connect(activity, new ConnectHandler() {
                @Override
                public void onConnect(int rst) {
                    TUIKitLog.e("HUAWEIPushReceiver=====rst = " + rst);
                    HMSAgent.Push.getToken(new GetTokenHandler() {
                        @Override
                        public void onResult(int rst) {
                            TUIKitLog.e("HUAWEIPushReceiver=====getToken = " + rst);
                        }
                    });
                }
            });
        }
    }

    /**
     * 关闭接收华为推送
     *
     * @param isReceiveNotifyMsg
     */
    private void editHuaWeiPushState(boolean isReceiveNotifyMsg) {
        if (IMFunc.isBrandHuawei()) {
            HMSAgent.Push.enableReceiveNotifyMsg(isReceiveNotifyMsg, new EnableReceiveNotifyMsgHandler() {
                @Override
                public void onResult(int rst) {

                }
            });
        }
    }

}
