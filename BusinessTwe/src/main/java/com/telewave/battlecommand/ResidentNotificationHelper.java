package com.telewave.battlecommand;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.annotation.DrawableRes;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import android.widget.RemoteViews;

import com.telewave.battlecommand.activity.MainActivity;
import com.telewave.battlecommand.mqtt.MqttMessageDto.CallPoliceMessage;
import com.telewave.battlecommand.mqtt.MqttMessageDto.MessageType;
import com.telewave.battlecommand.mqtt.MqttMessageDto.NewDisasterInfo;
import com.telewave.battlecommand.mqtt.MqttMessageDto.NotifyMemberMessage;
import com.telewave.battlecommand.mqtt.MqttMessageDto.NotifyWeiZhanMessage;
import com.telewave.battlecommand.mqtt.MqttMessageDto.ReceiveRollCallMessage;
import com.telewave.business.twe.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


/**
 * 常驻通知帮助类
 */
public class ResidentNotificationHelper {
    public static final int NOTICE_ID_TYPE_0 = R.string.app_name;


    @TargetApi(16)
    public static void sendNewDisasterNotice(Context context, NewDisasterInfo.MsgContentBean info) {
        NotificationCompat.Builder builder;
        int notification_id = 0;
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        /***
         * 在这里用自定的view来显示Notification
         */
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.view_notification_type_1);
        remoteViews.setTextViewText(R.id.title_tv, info.getZq().getZhdd());
        if (!TextUtils.isEmpty(info.getZq().getXqzdjgmc())) {
            remoteViews.setTextViewText(R.id.content_tv1, "主管中队：" + info.getZq().getXqzdjgmc());
        } else {
            remoteViews.setTextViewText(R.id.content_tv1, "主管中队：" + "未知");
        }

        remoteViews.setTextViewText(R.id.content_tv2, "灾害等级：" + info.getZq().getZhdjdm() + "级");
        remoteViews.setTextViewText(R.id.time_tv, getTime());

        //适配安卓8.0的消息渠道
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String channelID = "NewDisaster";
            NotificationChannel channel = new NotificationChannel(channelID, "新灾情消息", NotificationManager.IMPORTANCE_LOW);
            notificationManager.createNotificationChannel(channel);
            builder = new NotificationCompat.Builder(context, channelID);
        } else {
            builder = new NotificationCompat.Builder(context);
        }

        builder.setSmallIcon(R.mipmap.ic_launcher);
        // 设置声音/震动等
        builder.setDefaults(Notification.DEFAULT_VIBRATE);
        // 设置通知的点击行为：自动取消/跳转等
        builder.setAutoCancel(true);
        builder.setContent(remoteViews);

        Intent intent = new Intent(context, MainActivity.class);
        //在这里传递参数
        intent.putExtra("type", 1);
        intent.putExtra("msgContentBean", info);
        intent.putExtra("msgType", MessageType.NEW_DISASTER_INFO);
        builder.setContentIntent(PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT));

        notificationManager.notify(notification_id, builder.build());

    }

    @TargetApi(16)
    public static void sendNewCallPoliceNotice(Context context, CallPoliceMessage info) {
        NotificationCompat.Builder builder;
        int notification_id = 0;
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        /***
         * 在这里用自定的view来显示Notification
         */
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.view_notification_type_1);
        remoteViews.setTextViewText(R.id.title_tv, info.getZq().getZhdd());
        if (!TextUtils.isEmpty(info.getZq().getXqzdjgmc())) {
            remoteViews.setTextViewText(R.id.content_tv1, "主管中队：" + info.getZq().getXqzdjgmc());
        } else {
            remoteViews.setTextViewText(R.id.content_tv1, "主管中队：" + "未知");
        }

        remoteViews.setTextViewText(R.id.content_tv2, "灾害等级：" + info.getZq().getZhdjdm() + "级");
        remoteViews.setTextViewText(R.id.time_tv, getTime());

        //适配安卓8.0的消息渠道
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String channelID = "NewDisaster";
            NotificationChannel channel = new NotificationChannel(channelID, "新灾情消息", NotificationManager.IMPORTANCE_LOW);
            notificationManager.createNotificationChannel(channel);
            builder = new NotificationCompat.Builder(context, channelID);
        } else {
            builder = new NotificationCompat.Builder(context);
        }

        builder.setSmallIcon(R.mipmap.ic_launcher);
        // 设置声音/震动等
        builder.setDefaults(Notification.DEFAULT_VIBRATE);
        // 设置通知的点击行为：自动取消/跳转等
        builder.setAutoCancel(true);
        builder.setContent(remoteViews);

        Intent intent = new Intent(context, MainActivity.class);
        //在这里传递参数
        intent.putExtra("type", 1);
        intent.putExtra("msgContentBean", info);
        intent.putExtra("msgType", MessageType.RECEIVE_CALL_POLICE_MESSAGE);
        builder.setContentIntent(PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT));

        notificationManager.notify(notification_id, builder.build());

    }

    @TargetApi(16)
    public static void sendNotifyNotice(Context context, Object object) {
        NotificationCompat.Builder builder;
        int notification_id = 0;
        int intentFlag = 0;
        String title;
        String content;
        String organname;
        String noticeid;
        if (object instanceof NotifyMemberMessage) {
            intentFlag = 2;
            title = ((NotifyMemberMessage) object).getTitle();
            content = ((NotifyMemberMessage) object).getContent();
            organname = ((NotifyMemberMessage) object).getOrganname();
            noticeid = ((NotifyMemberMessage) object).getNoticeid();
        } else {
            intentFlag = 3;
            title = ((NotifyWeiZhanMessage) object).getTitle();
            content = ((NotifyWeiZhanMessage) object).getContent();
            organname = ((NotifyWeiZhanMessage) object).getOrganname();
            noticeid = ((NotifyWeiZhanMessage) object).getNoticeid();
        }

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        /***
         * 在这里用自定的view来显示Notification
         */
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.view_notification_type_1);
        remoteViews.setTextViewText(R.id.title_tv, title);
        remoteViews.setTextViewText(R.id.content_tv1, content);
        remoteViews.setTextViewText(R.id.content_tv2, "来源：" + organname);
        remoteViews.setTextViewText(R.id.time_tv, getTime());

        //适配安卓8.0的消息渠道
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String channelID = "NewNotify";
            NotificationChannel channel = new NotificationChannel(channelID, "新通知", NotificationManager.IMPORTANCE_LOW);
            notificationManager.createNotificationChannel(channel);
            builder = new NotificationCompat.Builder(context, channelID);
        } else {
            builder = new NotificationCompat.Builder(context);
        }

        builder.setSmallIcon(R.mipmap.ic_launcher);
        // 设置声音/震动等
        builder.setDefaults(Notification.DEFAULT_VIBRATE);
        // 设置通知的点击行为：自动取消/跳转等
        builder.setAutoCancel(true);
        builder.setContent(remoteViews);

        Intent intent = new Intent(context, MainActivity.class);
        //在这里传递参数
        intent.putExtra("type", intentFlag);
        intent.putExtra("id", noticeid);
        builder.setContentIntent(PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT));

        notificationManager.notify(notification_id, builder.build());

    }

    @TargetApi(16)
    public static void sendRollCallNotice(Context context, ReceiveRollCallMessage info) {
        NotificationCompat.Builder builder;
        int notification_id = 0;
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        /***
         * 在这里用自定的view来显示Notification
         */
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.view_notification_type_1);
        remoteViews.setTextViewText(R.id.title_tv, "点名通知提醒");
        remoteViews.setTextViewText(R.id.content_tv1, "点名开始，请及时应答");
        remoteViews.setTextViewText(R.id.content_tv2, "截止时间：" + info.getCutofftime());
        remoteViews.setTextViewText(R.id.time_tv, getTime());

        //适配安卓8.0的消息渠道
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String channelID = "RollCallNotify";
            NotificationChannel channel = new NotificationChannel(channelID, "点名通知", NotificationManager.IMPORTANCE_LOW);
            notificationManager.createNotificationChannel(channel);
            builder = new NotificationCompat.Builder(context, channelID);
        } else {
            builder = new NotificationCompat.Builder(context);
        }

        builder.setSmallIcon(R.mipmap.ic_launcher);
        // 设置声音/震动等
        builder.setDefaults(Notification.DEFAULT_VIBRATE);
        // 设置通知的点击行为：自动取消/跳转等
        builder.setAutoCancel(true);
        builder.setContent(remoteViews);

        Intent intent = new Intent(context, MainActivity.class);
        //在这里传递参数
        intent.putExtra("type", 4);
        intent.putExtra("ReceiveRollCallMessage", info);
        builder.setContentIntent(PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT));

        notificationManager.notify(notification_id, builder.build());

    }

    public static void sendDefaultNotice(Context context, String title, String content, @DrawableRes int res) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        builder.setOngoing(true);
        builder.setPriority(NotificationCompat.PRIORITY_MAX);

        Notification notification = builder
                .setContentTitle("Campus")
                .setContentText("It's a default notification")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(BitmapFactory.decodeResource(
                        context.getResources(), R.mipmap.ic_launcher))
                .build();


        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(NOTICE_ID_TYPE_0, notification);
    }


    private static String getTime() {
        SimpleDateFormat format = new SimpleDateFormat("HH:mm", Locale.SIMPLIFIED_CHINESE);
        return format.format(new Date());
    }


    public static void clearNotification(Context context, int noticeId) {
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.cancel(noticeId);
    }

}