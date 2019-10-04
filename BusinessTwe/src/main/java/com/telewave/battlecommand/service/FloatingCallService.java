package com.telewave.battlecommand.service;

import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.telewave.battlecommand.trtc.TRTCCallReceiveActivity;
import com.telewave.battlecommand.trtc.bean.TRTCCallMessage;
import com.telewave.business.twe.R;
import com.telewave.lib.base.Constents;
import com.telewave.lib.base.util.JsonUtils;
import com.telewave.lib.base.util.RingUtils;
import com.tencent.imsdk.TIMConversation;
import com.tencent.imsdk.TIMConversationType;
import com.tencent.imsdk.TIMCustomElem;
import com.tencent.imsdk.TIMManager;
import com.tencent.imsdk.TIMMessage;
import com.tencent.imsdk.TIMValueCallBack;

/**
 * 悬浮来电框
 * Created by liwh on 2019/8/05.
 */
public class FloatingCallService extends Service {
    public static boolean isStarted = false;
    private int mRingRawId = R.raw.video_phone_receive;
    private WindowManager windowManager;
    private WindowManager.LayoutParams layoutParams;
    private CountDownTimer countDownTimer;
    private boolean isRemoveView = false;

    @Override
    public void onCreate() {
        super.onCreate();
        isStarted = true;
        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        layoutParams = new WindowManager.LayoutParams();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            layoutParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else {
            layoutParams.type = WindowManager.LayoutParams.TYPE_PHONE;
        }
        layoutParams.format = PixelFormat.RGBA_8888;
        layoutParams.gravity = Gravity.LEFT | Gravity.TOP;
        layoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        TRTCCallMessage trtcCallMessage = (TRTCCallMessage) intent.getSerializableExtra("TRTCCallMessage");
        showFloatingWindow(trtcCallMessage);
        return super.onStartCommand(intent, flags, startId);
    }

    private void ring() {
        if (mRingRawId > 0) {
            if (!RingUtils.startRing(getBaseContext(), mRingRawId)) {
                Log.e("FloatingCallService", "Ring File Raw Id is wrong");
            }
        }
    }

    private void ringStop() {
        RingUtils.stop();
    }

    private void showFloatingWindow(final TRTCCallMessage trtcCallMessage) {
        isRemoveView = false;
        if (Settings.canDrawOverlays(this)) {
            final View showView = LayoutInflater.from(getApplication()).inflate(R.layout.float_call_layout, null);
            TextView textView = showView.findViewById(R.id.float_call_show_title);
            Button floatCallAnswerBtn = showView.findViewById(R.id.float_call_answer);
            Button floatCallEndBtn = showView.findViewById(R.id.float_call_end);
            final String trtcCallType = trtcCallMessage.getTrtcCallType();
            String trtcCallName = trtcCallMessage.getTrtcCallName();
            final String trtcCallFrom = trtcCallMessage.getTrtcCallFrom();
            if (Constents.ONE_TO_ONE_AUDIO_CALL.equals(trtcCallType)) {
                textView.setText(trtcCallName + "邀请你加入语音通话");
            } else if (Constents.ONE_TO_MULTIPE_AUDIO_CALL.equals(trtcCallType)) {
                textView.setText(trtcCallName + "邀请你加入语音群聊");
            } else if (Constents.ONE_TO_ONE_VIDEO_CALL.equals(trtcCallType)) {
                textView.setText(trtcCallName + "邀请你加入视频通话");
            } else if (Constents.ONE_TO_MULTIPE_VIDEO_CALL.equals(trtcCallType)) {
                textView.setText(trtcCallName + "邀请你加入视频群聊");
            }
            windowManager.addView(showView, layoutParams);
            floatCallAnswerBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ringStop();
                    if (!isRemoveView) {
                        windowManager.removeView(showView);
                    }
                    isRemoveView = true;
                    Intent intent = new Intent(getApplicationContext(), TRTCCallReceiveActivity.class);
                    intent.putExtra("TRTCCallMessage", trtcCallMessage);
                    // 服务是没有任务栈的，在服务中开启activity，要指定这个activity运行的任务栈
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    getApplicationContext().startActivity(intent);
                }
            });
            floatCallEndBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ringStop();
                    if (!isRemoveView) {
                        windowManager.removeView(showView);
                    }
                    isRemoveView = true;
                    sendDeclineMsg(trtcCallType, trtcCallFrom);
                }
            });
            ring();
            /** 倒计时10秒，一次1秒 */
            countDownTimer = new CountDownTimer(10 * 1000, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    // TODO Auto-generated method stub
                    if (isRemoveView) {
                        countDownTimer.cancel();
                    }
                }

                @Override
                public void onFinish() {
                    //倒计时全部结束执行操作
                    ringStop();
                    if (!isRemoveView) {
                        windowManager.removeView(showView);
                    }
                }
            };
            countDownTimer.start();
        }
    }

    private void sendDeclineMsg(String trtcCallType, String trtcCallFrom) {
        TIMMessage timMessage = new TIMMessage();
        TIMCustomElem ele = new TIMCustomElem();
        /**
         * 拒接语音、视频通话消息
         * msgContent不放内容
         */
        String msgStr = null;
        if (trtcCallType.equals(Constents.ONE_TO_ONE_AUDIO_CALL)
                || trtcCallType.equals(Constents.ONE_TO_MULTIPE_AUDIO_CALL)) {
            msgStr = JsonUtils.toJson(Constents.AUDIO_CALL_MESSAGE_DECLINE_DESC, null);
        } else if (trtcCallType.equals(Constents.ONE_TO_ONE_VIDEO_CALL)
                || trtcCallType.equals(Constents.ONE_TO_MULTIPE_VIDEO_CALL)) {
            msgStr = JsonUtils.toJson(Constents.VIDEO_CALL_MESSAGE_DECLINE_DESC, null);
        }
        ele.setData(msgStr.getBytes());
        timMessage.addElement(ele);

        TIMConversation conversation = TIMManager.getInstance().getConversation(
                TIMConversationType.C2C, trtcCallFrom);
        //发送消息
        conversation.sendOnlineMessage(timMessage, new TIMValueCallBack<TIMMessage>() {
            @Override
            public void onError(int code, String desc) {//发送消息失败
                //错误码 code 和错误描述 desc，可用于定位请求失败原因
                //错误码 code 含义请参见错误码表
                Log.d("NNN", "send message failed. code: " + code + " errmsg: " + desc);
            }

            @Override
            public void onSuccess(TIMMessage msg) {//发送消息成功
                Log.e("NNN", "SendMsg ok");
            }
        });
    }

}