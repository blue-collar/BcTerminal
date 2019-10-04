package com.tencent.qcloud.tim.uikit.utils;

import android.content.Context;
import android.media.AudioManager;
import android.util.Log;

/**
 * 停止播放录音
 * Created by wc on 2016/10/29.
 */
public class AudioStopUtil {
    public static boolean muteAudioFocus(Context context, boolean bMute) {
        if (context == null) {
            Log.d("ANDROID_LAB", "context is null.");
            return false;
        }
        boolean bool = false;
        AudioManager am = (AudioManager) context
                .getSystemService(Context.AUDIO_SERVICE);
        if (bMute) {
            int result = am.requestAudioFocus(null, AudioManager.STREAM_MUSIC,
                    AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);
            bool = result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED;
        } else {
            int result = am.abandonAudioFocus(null);
            bool = result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED;
        }
        return bool;
    }
}
