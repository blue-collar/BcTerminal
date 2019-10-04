package com.telewave.battlecommand.view;

import android.content.Context;
import android.os.Handler;
import android.support.v7.widget.AppCompatButton;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.PopupWindow;

import com.telewave.business.twe.R;
import com.telewave.lib.base.ConstData;
import com.telewave.lib.base.util.FileUtils;
import com.telewave.lib.base.util.ToastUtils;
import com.tencent.qcloud.tim.uikit.utils.AudioRecordTool;
import com.tencent.qcloud.tim.uikit.utils.AudioStopUtil;


/**
 * Created by wc on 2016/10/29.
 */
public class RollCallVoiceRecordButton extends AppCompatButton
        implements AudioRecordTool.AudioRecordPreparedListener {

    private static final String TAG = "RollCallVoiceRecordButton";

    private static final int STATE_NORMAL = 1;
    private static final int STATE_RECORDING = 2;
    private static final int STATE_WANT_CANCEL = 3;

    private static final int DISTANCE_CANCEL_Y = 50;

    private int currentState = STATE_NORMAL;
    private boolean isRecording = false;

    private AudioRecordTool audioRecordTool;

    private float mTime;
    // 是否触发LongClick
    private boolean isReady = false;

    /**
     * pop view
     */
    //录音动画
    private ChatVoiceRecordingAnimView voice_rcd_hint_anim;
    //正在录音view
    private View voice_rcd_hint_rcding;
    //正在录音
    private View voice_rcd_hint_anim_area;
    //取消录音view
    private View voice_rcd_hint_cancel_area;
    //录音太短view
    private View voice_rcd_hint_tooshort;

    //屏幕宽高
    private int WindowWidth, WindowHeight;

    private int width, height;
    /**
     * 录音提示pop
     */
    private PopupWindow popupWindow;

    public RollCallVoiceRecordButton(Context context) {
        this(context, null);
    }

    public RollCallVoiceRecordButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        setText(R.string.chatfooter_presstorcd);
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        WindowHeight = wm.getDefaultDisplay().getHeight();
        WindowWidth = wm.getDefaultDisplay().getWidth();
        width = this.getMeasuredHeight();
        height = this.getMeasuredHeight();
        audioRecordTool = AudioRecordTool.getInstance(ConstData.ROLL_CALL_VOICE_DIR);
        audioRecordTool.setOnAudioStateChangeListener(this);
        setOnLongClickListener(new OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {
                isReady = true;
                audioRecordTool.prepareAudio();
                return false;
            }
        });
    }


    private Runnable getVolumeRunnable = new Runnable() {

        @Override
        public void run() {

            while (isRecording) {
                try {
                    Thread.sleep(100);
                    mTime += 0.1f;
                    mHanlder.sendEmptyMessage(MSG_VOLUME_CHAMGED);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        }

    };

    private static final int MSG_AUDIO_PREPARED = 0x110;
    private static final int MSG_VOLUME_CHAMGED = 0x111;
    private static final int MSG_DIALOG_DISMISS = 0x112;

    private Handler mHanlder = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case MSG_AUDIO_PREPARED:
                    showVoiceRecordWindow();
                    isRecording = true;
                    // 音量
                    new Thread(getVolumeRunnable).start();

                    break;
                case MSG_VOLUME_CHAMGED:

                    break;
                case MSG_DIALOG_DISMISS:
                    dismissPopuWindow();

                    break;

                default:
                    break;
            }
        }

        ;
    };

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if (FileUtils.getAvailaleSize() < 10) {
            ToastUtils.toastShortMessage(getContext().getResources().getString(R.string.media_no_memory));
            return false;
        }

        if (!FileUtils.isExistExternalStore()) {
            ToastUtils.toastShortMessage(getContext().getResources().getString(R.string.media_ejected));
            return false;
        }

        int action = event.getAction();
        int x = (int) event.getX();
        int y = (int) event.getY();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                // 开始录音时将正在播放的音乐暂停
                AudioStopUtil.muteAudioFocus(getContext(), true);
                changeState(STATE_RECORDING);
                break;
            case MotionEvent.ACTION_MOVE:
                // 已经开始录音
                if (isRecording) {
//                    AudioStopMusic.muteAudioFocus(getContext(), true);
//                    if (mTime >= 60) {
//                        dismissPopuWindow();
//                        audioRecordTool.release();
//                        // 录制完成后将音乐恢复
//                        AudioStopMusic.muteAudioFocus(getContext(), false);
//                        // callbackToActivity
//                        if (audioRecordFinishListener != null) {
//                            audioRecordFinishListener.onAudioRecordFinishFinish((int) mTime, audioRecordTool
//                                    .getCurrentPath());
//                        }
//                        resetState();
//                    }
                    // 根据X，Y的坐标判断是否想要取消
                    if (IsCancelRecord(x, y)) {
                        //取消
                        changeState(STATE_WANT_CANCEL);
                    } else {
                        changeState(STATE_RECORDING);
                    }
                }

                break;

            case MotionEvent.ACTION_UP:
                setText(R.string.chatfooter_presstorcd);
                // 没有触发longClick
                if (!isReady) {
                    resetState();
                    AudioStopUtil.muteAudioFocus(getContext(), false);
                    return super.onTouchEvent(event);
                }
                // prepare未完成就up,录音时间过短
                if (!isRecording || mTime < 0.6f) {
                    ShowPopTooShort();
                    audioRecordTool.cancel();
                    mHanlder.sendEmptyMessageDelayed(MSG_DIALOG_DISMISS, 1300);
                    AudioStopUtil.muteAudioFocus(getContext(), false);
                } else if (currentState == STATE_RECORDING) { // 正常录制结束
                    dismissPopuWindow();
                    audioRecordTool.release();
                    // 录制完成后将音乐恢复
                    AudioStopUtil.muteAudioFocus(getContext(), false);
                    if (audioRecordFinishListener != null) {
                        if ((int) mTime != 0) {
                            audioRecordFinishListener.onAudioRecordFinish((int) mTime, audioRecordTool
                                    .getCurrentPath());
                        }

                    }

                } else if (currentState == STATE_WANT_CANCEL) {
                    AudioStopUtil.muteAudioFocus(getContext(), false);
                    dismissPopuWindow();
                    audioRecordTool.cancel();
                }
                resetState();
                dismissPopuWindow();
                break;

            default:
                break;
        }
        return super.onTouchEvent(event);
    }

    private void changeState(int state) {

        if (currentState != state) {
            currentState = state;
            switch (state) {
                case STATE_NORMAL:
                    //setBackgroundResource(R.drawable.voice_normal);
                    setText(R.string.chatfooter_presstorcd);
                    break;
                case STATE_RECORDING:
                    //setBackgroundResource(R.drawable.voice_press);
                    setText(R.string.chatfooter_release_send);
                    ShowPopAnim();
                    break;
                case STATE_WANT_CANCEL:
                    //setBackgroundResource(R.drawable.voice_normal);
                    ShowPopCancel();
                    setText(R.string.chatfooter_cancel_rcd_release);
                    break;

                default:
                    break;
            }
        }
    }

    /**
     * 恢复标志位
     */
    private void resetState() {
        changeState(STATE_NORMAL);
        isRecording = false;
        isReady = false;
        mTime = 0;
    }

    private boolean IsCancelRecord(int x, int y) {
        if (x < 0 || x > getWidth()) {
            return true;
        }
        // 零点在左下角？
        if (y < -DISTANCE_CANCEL_Y || y > getHeight() + DISTANCE_CANCEL_Y) {
            return true;
        }
        return false;
    }


    public final void showVoiceRecordWindow() {
        if (popupWindow == null) {
            popupWindow = new PopupWindow(View.inflate(getContext(), R.layout.voice_rcd_hint_window, null), WindowWidth * 1 / 3, WindowHeight * 1 / 5);

            voice_rcd_hint_rcding = popupWindow.getContentView()
                    .findViewById(R.id.voice_rcd_hint_rcding);

            voice_rcd_hint_anim_area = popupWindow.getContentView()
                    .findViewById(R.id.voice_rcd_hint_anim_area);
            voice_rcd_hint_anim = ((ChatVoiceRecordingAnimView) popupWindow.getContentView()
                    .findViewById(R.id.voice_rcd_hint_anim));

            voice_rcd_hint_cancel_area = popupWindow.getContentView()
                    .findViewById(R.id.voice_rcd_hint_cancel_area);

            voice_rcd_hint_tooshort = popupWindow.getContentView()
                    .findViewById(R.id.voice_rcd_hint_tooshort);
            voice_rcd_hint_rcding.setVisibility(View.VISIBLE);
            voice_rcd_hint_anim_area.setVisibility(View.VISIBLE);
            voice_rcd_hint_anim.setVisibility(View.VISIBLE);
            voice_rcd_hint_cancel_area.setVisibility(View.GONE);
            voice_rcd_hint_tooshort.setVisibility(View.GONE);
            popupWindow.showAtLocation(this, Gravity.CENTER, 0, 0);
        }
    }

    public int getMetricsDensity(Context context, float height) {
        DisplayMetrics localDisplayMetrics = new DisplayMetrics();
        ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay()
                .getMetrics(localDisplayMetrics);
        return Math.round(height * localDisplayMetrics.densityDpi / 160.0F);
    }

    /**
     *
     */
    private final void dismissPopuWindow() {
        if (popupWindow != null) {
            popupWindow.dismiss();
            voice_rcd_hint_rcding.setVisibility(View.GONE);
            voice_rcd_hint_tooshort.setVisibility(View.GONE);
            voice_rcd_hint_cancel_area.setVisibility(View.GONE);
            voice_rcd_hint_anim_area.setVisibility(View.GONE);
        }
        popupWindow = null;
    }

    private final boolean IsPopViewTrue() {
        if (voice_rcd_hint_rcding == null || voice_rcd_hint_tooshort == null || voice_rcd_hint_cancel_area == null || voice_rcd_hint_anim_area == null || voice_rcd_hint_anim == null) {
            return false;
        }
        return true;
    }

    /**
     * 录音太短
     */
    private final void ShowPopTooShort() {
        if (!IsPopViewTrue()) {
            return;
        }
        voice_rcd_hint_rcding.setVisibility(View.GONE);
        voice_rcd_hint_tooshort.setVisibility(View.VISIBLE);
        voice_rcd_hint_cancel_area.setVisibility(View.GONE);
        voice_rcd_hint_anim_area.setVisibility(View.GONE);
    }

    /**
     * 录音取消
     */
    private final void ShowPopCancel() {
        if (!IsPopViewTrue()) {
            return;
        }
        voice_rcd_hint_rcding.setVisibility(View.VISIBLE);
        voice_rcd_hint_tooshort.setVisibility(View.GONE);
        voice_rcd_hint_cancel_area.setVisibility(View.VISIBLE);
        voice_rcd_hint_anim_area.setVisibility(View.GONE);
    }

    /**
     * 录音正常
     */
    private final void ShowPopAnim() {
        if (!IsPopViewTrue()) {
            return;
        }
        voice_rcd_hint_rcding.setVisibility(View.VISIBLE);
        voice_rcd_hint_tooshort.setVisibility(View.GONE);
        voice_rcd_hint_cancel_area.setVisibility(View.GONE);
        voice_rcd_hint_anim_area.setVisibility(View.VISIBLE);
        voice_rcd_hint_anim.setVisibility(View.VISIBLE);
    }

    /**
     * 开始准备录音的回调
     */
    @Override
    public void AudioRecordPrepared() {
        mHanlder.sendEmptyMessage(MSG_AUDIO_PREPARED);
    }

    /**
     * 录音完成后的回调
     */
    public interface AudioRecordFinishListener {
        void onAudioRecordFinish(int second, String filePath);
    }

    private AudioRecordFinishListener audioRecordFinishListener;

    public void setAudioRecordFinishListener(AudioRecordFinishListener listener) {
        audioRecordFinishListener = listener;
    }
}
