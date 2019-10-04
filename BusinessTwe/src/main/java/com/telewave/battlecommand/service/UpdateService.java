package com.telewave.battlecommand.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.RemoteViews;

import com.telewave.business.twe.R;
import com.telewave.lib.base.ConstData;
import com.telewave.lib.base.util.AppUtils;
import com.yanzhenjie.nohttp.Headers;
import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.download.DownloadListener;
import com.yanzhenjie.nohttp.download.DownloadRequest;

import java.io.File;
import java.lang.ref.WeakReference;
import java.text.DecimalFormat;

/**
 * 自动更新升级服务
 *
 * @author Kevin
 */
public class UpdateService extends Service {
    private static String down_url;
    // 下载完成
    private static final int DOWN_OK = 1;
    private static final int DOWN_ERROR = 0;

    private String newVersion;

    private NotificationManager mNotificationManager;
    private NotificationCompat.Builder builder;

    private RemoteViews contentView;
    private int notification_id = 0;
    private String apkFilePath;

    private Handler handler = new Handler(new WeakReference<Handler.Callback>(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case DOWN_OK:
                    apkFilePath = (String) msg.obj;
                    Log.e("progress", "apkFilePath:" + apkFilePath);
                    AppUtils.installApk(new File(apkFilePath), getApplicationContext());
                    mNotificationManager.cancel(notification_id);
                    stopSelf();
                    break;
                case DOWN_ERROR:
//                    builder.setContentTitle(app_name);
//                    builder.setContentText("下载失败");
//                    builder.setContentIntent(pendingIntent);
//                    notificationManager.notify(notification_id, builder.build());
                    break;
                default:
                    break;
            }
            return false;
        }
    }).get());

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            try {
                newVersion = intent.getStringExtra("version");
                down_url = intent.getStringExtra("down_url");
                // 创建通知
                createNotification();
                // 开始下载
                downloadUpdateFile(down_url);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return super.onStartCommand(intent, flags, startId);
    }


    /***
     * 创建通知栏
     */
    public void createNotification() {
        mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        /***
         * 在这里用自定的view来显示Notification
         */
        contentView = new RemoteViews(getPackageName(), R.layout.notification_item);
        contentView.setTextViewText(R.id.notificationTitle, getString(R.string.app_name));
        contentView.setTextViewText(R.id.notificationProgressText, "新版本下载中....0%");
        contentView.setProgressBar(R.id.notificationProgress, 100, 0, false);

        //适配安卓8.0的消息渠道
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String channelID = "update";
            NotificationChannel channel = new NotificationChannel(channelID, "版本更新", NotificationManager.IMPORTANCE_LOW);
            mNotificationManager.createNotificationChannel(channel);
            builder = new NotificationCompat.Builder(this, channelID);
        } else {
            builder = new NotificationCompat.Builder(getApplicationContext());
        }

        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setDefaults(Notification.FLAG_ONLY_ALERT_ONCE);
        builder.setAutoCancel(true);
        // 这个参数是通知提示闪出来的值.
        builder.setTicker("下载中...");
        builder.setContent(contentView);
        mNotificationManager.notify(notification_id, builder.build());
    }


    /***
     * 下载文件
     */
    public void downloadUpdateFile(String down_url) {
        Log.e("progress", "down_url:" + down_url);
        Log.e("progress", "app_name:" + getString(R.string.app_name) + newVersion);
        DownloadRequest downreqs = NoHttp.createDownloadRequest(down_url, ConstData.UPDATE_SYSTEM_DIR, getString(R.string.app_name) + newVersion + ".apk", true, true);
        NoHttp.getDownloadQueueInstance().add(0, downreqs, new DownloadListener() {
            @Override
            public void onDownloadError(int what, Exception exception) {
                Message message = handler.obtainMessage();
                message.what = DOWN_ERROR;
                handler.sendMessage(message);
            }

            @Override
            public void onStart(int what, boolean isResume, long rangeSize, Headers responseHeaders, long allCount) {
                int progress = 0;
                if (allCount != 0) {
                    progress = (int) (rangeSize * 100 / allCount);
                }
                Log.e("progress", "rangeSize:" + rangeSize);
                Log.e("progress", "allCount:" + allCount);
                updateProgress(progress, 0);
            }

            @Override
            public void onProgress(int what, int progress, long fileCount, long speed) {
                Log.e("progress", "progress:" + progress);
                Log.e("progress", "speed:" + speed);
                updateProgress(progress, speed);
            }

            @Override
            public void onFinish(int what, String filePath) {
                // 下载成功
                Message message = handler.obtainMessage();
                message.what = DOWN_OK;
                message.obj = filePath;
                handler.sendMessage(message);
            }

            @Override
            public void onCancel(int what) {

            }

        });
    }

    private void updateProgress(int progress, final long speed) {
        double newSpeed = speed / 1024D;
        DecimalFormat decimalFormat = new DecimalFormat("###0.00");
//        contentView.setTextViewText(R.id.notificationProgressText, "已下载" + progress + "%，下载速度" + decimalFormat.format(newSpeed) + "kb/s");
        contentView.setTextViewText(R.id.notificationProgressText, "已下载" + progress + "%");
        contentView.setProgressBar(R.id.notificationProgress, 100, progress, false);
        mNotificationManager.notify(notification_id, builder.build());
    }


}