package com.telewave.lib.base;

import android.widget.RelativeLayout;

/**
 * @author liwh
 * @date 2019/6/4
 */
public class Constents {

    /**
     * 腾讯实时音视频
     * 私钥和appKey
     */
    public static String PRIVATE_KEY = "-----BEGIN PRIVATE KEY-----\n" +
            "MIGHAgEAMBMGByqGSM49AgEGCCqGSM49AwEHBG0wawIBAQQgbGjv/61qFhroqNWt\n" +
            "LLL/0m+imSVmXoqbvNNtOgzGMJehRANCAAQj5UI71JNjSSBuf5on2gQWciPHfBVj\n" +
            "ZCAPI7IVAxyGrlW/5uLgXLl/LgbDrKMo8Dbw9apr1WPbnyqp4GEY5+GL\n" +
            "-----END PRIVATE KEY-----\n";
    public static long APPKEY = 1400246223;

//    public static String PRIVATE_KEY = "-----BEGIN PRIVATE KEY-----\n" +
//            "MIGHAgEAMBMGByqGSM49AgEGCCqGSM49AwEHBG0wawIBAQQgp+AYz4jBwx+yyXig\n" +
//            "QxoA0ci0/4hEc6s112gS0Z0dzcahRANCAAT9KzgWH4esbmB/F17hTtAW/6IVHZtH\n" +
//            "W4xyHzSU9KA6OAySEO00vDbuS7pfm63+jZhjkgSqmJKkTwx8kOxbRXkh\n" +
//            "-----END PRIVATE KEY-----\n";
//    public static long APPKEY = 1400239551;

    /**
     * 1对1语音通话
     */
    public final static String ONE_TO_ONE_AUDIO_CALL = "1";
    /**
     * 1对多语音通话
     */
    public final static String ONE_TO_MULTIPE_AUDIO_CALL = "2";
    /**
     * 1对1视频通话
     */
    public final static String ONE_TO_ONE_VIDEO_CALL = "3";

    /**
     * 1对多视频通话
     */
    public final static String ONE_TO_MULTIPE_VIDEO_CALL = "4";

    /**
     * 实时语音通话消息描述内容
     */
    public final static String AUDIO_CALL_MESSAGE_DESC = "AUDIO_CALL_MESSAGE_DESC";
    /**
     * 实时视频通话消息描述内容
     */
    public final static String VIDEO_CALL_MESSAGE_DESC = "VIDEO_CALL_MESSAGE_DESC";

    /**
     * 实时语音通话消息拒接
     */
    public final static String AUDIO_CALL_MESSAGE_DECLINE_DESC = "AUDIO_CALL_MESSAGE_DECLINE_DESC";
    /**
     * 实时视频通话消息拒接
     */
    public final static String VIDEO_CALL_MESSAGE_DECLINE_DESC = "VIDEO_CALL_MESSAGE_DECLINE_DESC";

    /**
     * 悬浮窗与TRTCVideoActivity共享的视频View
     */
    public static RelativeLayout mVideoViewLayout;

    /**
     * 悬浮窗是否开启
     */
    public static boolean isShowFloatWindow = false;

    /**
     * 语音通话开始计时时间（悬浮窗要显示时间在这里记录开始值）
     */
    public static long audioCallStartTime;


}
