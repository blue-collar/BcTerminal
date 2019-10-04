package com.tencent.qcloud.tim.uikit.utils;

import android.media.MediaRecorder;
import android.util.Log;

import com.telewave.lib.base.ConstData;

import java.io.File;

/**
 * 录音
 * Created by wc on 2016/10/29.
 */
public class AudioRecordTool {
    private static final String TAG = "AudioRecordTool";
    private MediaRecorder mediaRecorder;
    private String dir;
    private String currentFilePath;

    private static AudioRecordTool audioInstance;

    public boolean isPrepared = false;

    private AudioRecordTool(String dir) {
        this.dir = dir;
    }

    synchronized public static AudioRecordTool getInstance(String dir) {
        if (audioInstance == null) {
            if (audioInstance == null) {
                audioInstance = new AudioRecordTool(dir);
            }
        }
        return audioInstance;
    }

    public void prepareAudio() {
        try {
            isPrepared = false;
            File fileDir = new File(dir);
            if (!fileDir.exists()) {
                fileDir.mkdirs();
            }
            Log.e(TAG, "fileDir=" + fileDir.getAbsolutePath());
            String fileName = generateFileName();
            File file = new File(fileDir, fileName);
            currentFilePath = file.getAbsolutePath();
            mediaRecorder = new MediaRecorder();
            // 设置输出文件
            mediaRecorder.setOutputFile(file.getAbsolutePath());
            // 设置音频源
            mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            // 设置音频格式
            mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.AMR_NB);
            // 设置音频编码
            mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

            mediaRecorder.prepare();
            mediaRecorder.start();
            // 准备结束
            isPrepared = true;
            //
            if (audioRecordPreparedListener != null) {
                audioRecordPreparedListener.AudioRecordPrepared();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 随机生成文件名称
     *
     * @return
     */
    private String generateFileName() {
        return ConstData.CHAT_FILE_PRX + System.currentTimeMillis() + ".amr";
    }

    public int getVoiceLevel(int maxLevel) {
        if (isPrepared) {
            try {
                // 振幅范围mediaRecorder.getMaxAmplitude():1-32767
                return maxLevel * mediaRecorder.getMaxAmplitude() / 32768 + 1;
            } catch (Exception e) {
            }
        }
        return 1;
    }

    public void release() {
        //有一些网友反应在5.0以上在调用stop的时候会报错，翻阅了一下谷歌文档发现上面确实写的有可能会报错的情况，捕获异常清理一下就行了，感谢大家反馈！
        try {
            mediaRecorder.setOnErrorListener(null);
            mediaRecorder.setOnInfoListener(null);
            mediaRecorder.setPreviewDisplay(null);
            mediaRecorder.stop();
            mediaRecorder.release();
            mediaRecorder = null;
        } catch (RuntimeException e) {
            mediaRecorder.release();
            mediaRecorder = null;
        }
    }

    public void cancel() {
        release();
        if (currentFilePath != null) {
            File file = new File(currentFilePath);
            file.delete();
            currentFilePath = null;
        }
    }

    public String getCurrentPath() {
        return currentFilePath;
    }

    public interface AudioRecordPreparedListener {
        void AudioRecordPrepared();
    }

    public AudioRecordPreparedListener audioRecordPreparedListener;

    public void setOnAudioStateChangeListener(AudioRecordPreparedListener listener) {
        audioRecordPreparedListener = listener;
    }
}
