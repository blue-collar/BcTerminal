package com.telewave.battlecommand.imui.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.location.LocationManager;
import android.media.AudioManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Vibrator;
import android.util.DisplayMetrics;
import android.view.Display;

import com.telewave.lib.base.AppProxy;

import java.lang.ref.WeakReference;
import java.lang.reflect.Method;

/**
 * @author PF-NAN
 * @date 2018/11/7
 */
public class IMSystemUtil {
    private static long mLastNotifiyTime;
    private static AudioManager mAudioManager;
    private static Vibrator mVibrator;
    private static Ringtone mRingtone = null;

    /**
     * 震动和声音
     */
    public static void vibrateAndPlayTone() {
        // 5秒内收到新消息,跳过播放铃声
        if (System.currentTimeMillis() - mLastNotifiyTime < 5000) {
            return;
        }
        try {
            if (null == mAudioManager) {
                mAudioManager = (AudioManager) AppProxy.getContext().getSystemService(Context.AUDIO_SERVICE);
            }
            if (null == mVibrator) {
                mVibrator = (Vibrator) AppProxy.getContext().getSystemService(Context.VIBRATOR_SERVICE);
            }
            mLastNotifiyTime = System.currentTimeMillis();
            if (mAudioManager.getRingerMode() == AudioManager.RINGER_MODE_SILENT) {
                /*当前为静音模式*/
                return;
            }
            //震动
            long[] pattern = new long[]{0, 180, 80, 120};
            mVibrator.vibrate(pattern, -1);
            //铃声 TYPE_NOTIFICATION
            if (mRingtone == null) {
                Uri notificationUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                mRingtone = RingtoneManager.getRingtone(AppProxy.getContext(), notificationUri);
                if (mRingtone == null) {
                    /*没有找到铃声*/
                    return;
                }
            }
            if (!mRingtone.isPlaying()) {
                String vendor = Build.MANUFACTURER;
                mRingtone.play();
                // samsung S3 Bug处理需要添加
                if (vendor != null && vendor.toLowerCase().contains("samsung")) {
                    Thread ctlThread = new Thread() {
                        @Override
                        public void run() {
                            try {
                                if (mRingtone.isPlaying()) {
                                    mRingtone.stop();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    };
                    ctlThread.run();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 判断GPS是否开启，GPS或者AGPS开启一个就认为是开启的
     *
     * @return true 表示开启
     */
    public static boolean isOPenGPS() {
        LocationManager locationManager = (LocationManager)
                AppProxy.getContext().getSystemService(Context.LOCATION_SERVICE);
        assert locationManager != null;
        // 通过WLAN或移动网络(3G/2G)确定的位置（也称作AGPS，辅助GPS定位。主要用于在室内或遮盖物（建筑群或茂密的深林等）密集的地方定位）
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }


    /**
     * 获取系统属性
     *
     * @param key
     * @return
     */
    public static String getAndroidOsSystemProperties(String key) {
        String ret;
        try {
            @SuppressLint("PrivateApi") Method systemProperties_get = Class.forName("android.os.SystemProperties").getMethod("get", String.class);
            if ((ret = (String) systemProperties_get.invoke(null, key)) != null) {
                return ret;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return "";
    }

    /**
     * 获取横向分辨率
     *
     * @param activity
     * @return
     */
    public static int getDisplayWidth(Activity activity) {
        int widthPixels = 0;
        try {
            WeakReference<Activity> weakActivity = new WeakReference<>(activity);
            DisplayMetrics metrics = new DisplayMetrics();
            Display display = weakActivity.get().getWindowManager().getDefaultDisplay();
            display.getMetrics(metrics);
            widthPixels = metrics.widthPixels;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return widthPixels;
    }

    /**
     * 获取竖向分辨率
     *
     * @param activity
     * @return
     */
    public static int getDisplayHeight(Activity activity) {
        int heightPixels = 0;
        try {
            WeakReference<Activity> weakActivity = new WeakReference<>(activity);
            DisplayMetrics metrics = new DisplayMetrics();
            Display display = weakActivity.get().getWindowManager().getDefaultDisplay();
            display.getMetrics(metrics);
            heightPixels = metrics.heightPixels;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return heightPixels;
    }
}
