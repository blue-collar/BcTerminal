package com.telewave.battlecommand.imui.util;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;

import com.telewave.lib.base.AppProxy;
import com.tencent.imsdk.TIMConversation;
import com.tencent.imsdk.TIMConversationType;
import com.tencent.imsdk.TIMElem;
import com.tencent.imsdk.TIMElemType;
import com.tencent.imsdk.TIMGroupMemberInfo;
import com.tencent.imsdk.TIMMessage;
import com.tencent.imsdk.TIMTextElem;
import com.tencent.imsdk.TIMUserProfile;
import com.tencent.imsdk.TIMValueCallBack;
import com.tencent.qcloud.tim.uikit.modules.message.MessageInfoUtil;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

/**
 * 通知栏维护
 *
 * @author PF-NAN
 * @date 2018/11/30
 */
public class IMInformOperate {
    private static final int CCP_NOTIFICATOIN_ID_CALLING = 0x1;
    private static NotificationManager mNotificationManager = null;
    private static IMInformOperate mInstance;
    private static String mPackageName;
    /*消息发送者,和其对应的提示ID*/
    private HashMap<String, Integer> mUserAndId = new HashMap<>();
    /*消息发送者,和其对应的消息条数*/
    private HashMap<String, Integer> mUserAndMsgNum = new HashMap<>();

    private IMInformOperate() {
    }

    public static IMInformOperate getInstance() {
        if (null == mInstance || null == mNotificationManager) {
            synchronized (IMInformOperate.class) {
                if (null == mInstance || null == mNotificationManager || TextUtils.isEmpty(mPackageName)) {
                    mInstance = new IMInformOperate();
                    mNotificationManager = (NotificationManager) AppProxy.getApplication().getSystemService(Context.NOTIFICATION_SERVICE);
                    mPackageName = AppProxy.getApplication().getPackageName();
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        String name = AppProxy.getApplication().getApplicationInfo().name;
                        NotificationChannel channel = new NotificationChannel(mPackageName, name, NotificationManager.IMPORTANCE_HIGH);
                        mNotificationManager.createNotificationChannel(channel);
                    }
                }
            }
        }
        return mInstance;
    }

    private String content = "";

    /**
     * 创建状态栏通知
     *
     * @param ecMessage
     */
    public synchronized void createIMInform(@NonNull TIMMessage ecMessage) {
        IMSystemUtil.vibrateAndPlayTone();
        boolean isForeground = IMAppUtil.isAppOnForeground();
        if (!isForeground) {
            TIMElem currentTIMElem = null;
            content = "";
            final TIMConversation conversation = ecMessage.getConversation();
            long elementCount = ecMessage.getElementCount();
            for (int i = 0; i < elementCount; i++) {
                TIMElem element = ecMessage.getElement(i);
                if (element == null) {
                    continue;
                } else {
                    currentTIMElem = element;
                }
            }
            if (null != currentTIMElem) {
                if (currentTIMElem.getType() == TIMElemType.Text) {
                    content = ((TIMTextElem) currentTIMElem).getText();
                } else if (currentTIMElem.getType() == TIMElemType.Sound) {
                    content = "[语音]";

                } else if (currentTIMElem.getType() == TIMElemType.Image) {
                    content = "[图片]";

                } else if (currentTIMElem.getType() == TIMElemType.Location) {
                    content = "[位置]";

                } else if (currentTIMElem.getType() == TIMElemType.File) {
                    content = "[文件]";

                } else if (currentTIMElem.getType() == TIMElemType.Video) {
                    content = "[视频]";
                }
            }
            if (conversation.getType() == TIMConversationType.Group) {
                final TIMGroupMemberInfo senderGroupMemberProfile = ecMessage.getSenderGroupMemberProfile();
                MessageInfoUtil.getUsersProfile(senderGroupMemberProfile.getUser(), new TIMValueCallBack<List<TIMUserProfile>>() {
                    @Override
                    public void onError(int code, String desc) {

                    }

                    @Override
                    public void onSuccess(List<TIMUserProfile> timUserProfiles) {
                        content = String.format(Locale.CHINESE, "%s：%s", timUserProfiles.get(0).getNickName(), content);
                        createInform(content, conversation.getPeer(), conversation.getGroupName());
                    }
                });

            } else {
                MessageInfoUtil.getUsersProfile(conversation.getPeer(), new TIMValueCallBack<List<TIMUserProfile>>() {
                    @Override
                    public void onError(int code, String desc) {

                    }

                    @Override
                    public void onSuccess(List<TIMUserProfile> timUserProfiles) {
                        createInform(content, conversation.getPeer(), timUserProfiles.get(0).getNickName());

                    }
                });
            }
        }
    }


    /**
     * 创建通知栏提醒
     *
     * @param content
     * @param sessionId
     * @param title
     */
    private synchronized void createInform(String content, String sessionId, String title) {
        if (TextUtils.isEmpty(sessionId)) {
            return;
        }
        int notifyId = 10000;
        if (mUserAndId.containsKey(sessionId)) {
            notifyId = mUserAndId.get(sessionId);
        } else {
            Iterator ite = mUserAndId.keySet().iterator();
            while (ite.hasNext()) {
                String key = (String) ite.next();   //   key
                if (notifyId < mUserAndId.get(key)) {
                    notifyId = mUserAndId.get(key);
                }
            }
            notifyId++;
            mUserAndId.put(sessionId, notifyId);
        }
        boolean isHasUserId = mUserAndMsgNum.containsKey(sessionId);
        int msgNum = 1;
        if (isHasUserId) {
            msgNum += mUserAndMsgNum.get(sessionId);
        }
        mUserAndMsgNum.put(sessionId, msgNum);
        sendNotification(notifyId, content, msgNum, title);
    }

    /**
     * 发送通知
     *
     * @param notifyId
     * @param content
     * @param unReadNum
     * @param title
     */
    private void sendNotification(int notifyId, String content, int unReadNum, String title) {
        try {
            Intent intent = new Intent();
            intent.setClassName(AppProxy.getContext(), "com.telewave.battlecommand.activity.MainActivity");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            NotificationCompat.Builder builder = new NotificationCompat.Builder(AppProxy.getContext(), mPackageName)
                    .setSmallIcon(AppProxy.getApplication().getApplicationInfo().icon)
                    .setWhen(System.currentTimeMillis())
                    .setAutoCancel(true);
            PendingIntent pendingIntent = PendingIntent.getActivity(AppProxy.getContext(), notifyId, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            builder.setContentTitle(initMsgContent(title, unReadNum));//标题
            builder.setContentText(content);//显示文本
            builder.setContentIntent(pendingIntent);//需要跳转的
            Notification notification = builder.build();
            mNotificationManager.notify(notifyId, notification);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 初始化标题
     *
     * @param title
     * @param unReadNum
     * @return
     */
    private String initMsgContent(String title, int unReadNum) {
        if (TextUtils.isEmpty(title)) {
            title = (String) AppProxy.getApplication().getPackageManager().getApplicationLabel(AppProxy.getApplication().getApplicationInfo());
        } else if (unReadNum > 1) {
            title = String.format(Locale.CHINA, "%s (%d条新消息)", title, unReadNum);
        }
        return title;
    }

    /**
     * 重置通知栏通知提示
     */
    public void reset() {
        if (mNotificationManager != null) {
            Set<Map.Entry<String, Integer>> entries = mUserAndId.entrySet();
            Iterator<Map.Entry<String, Integer>> iterator = entries.iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, Integer> next = iterator.next();
                Integer notifyId = next.getValue();
                if (notifyId != CCP_NOTIFICATOIN_ID_CALLING) {
                    mNotificationManager.cancel(notifyId);
                }
            }
        }
        mUserAndId.clear();
        mUserAndMsgNum.clear();
    }
}