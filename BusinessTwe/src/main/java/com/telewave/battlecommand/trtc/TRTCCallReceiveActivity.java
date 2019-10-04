package com.telewave.battlecommand.trtc;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.telewave.battlecommand.trtc.bean.TRTCCallMessage;
import com.telewave.business.twe.R;
import com.telewave.lib.base.ConstData;
import com.telewave.lib.base.Constents;
import com.telewave.lib.base.SampleUser;
import com.telewave.lib.base.util.JsonUtils;
import com.telewave.lib.base.util.RingUtils;
import com.telewave.lib.widget.util.ActivityUtil;
import com.tencent.imsdk.TIMConversation;
import com.tencent.imsdk.TIMConversationType;
import com.tencent.imsdk.TIMCustomElem;
import com.tencent.imsdk.TIMManager;
import com.tencent.imsdk.TIMMessage;
import com.tencent.imsdk.TIMValueCallBack;
import com.tls.tls_sigature.tls_sigature;

import java.util.ArrayList;
import java.util.List;


/**
 * @author liwh
 * @date 2019/6/13
 */
public class TRTCCallReceiveActivity extends AppCompatActivity implements TRTCCallMessageManager.TRTCAudioCallMessageCancelListener,
        TRTCCallMessageManager.TRTCVideoCallMessageCancelListener {
    private static final String TAG = TRTCCallReceiveActivity.class.getSimpleName();
    private final static String TRTCAudioCall_TAG = TRTCAudioCallActivity.class.getSimpleName();
    private final static String TRTCVideoCall_TAG = TRTCVideoCallActivity.class.getSimpleName();
    private int mRingRawId;
    private TextView trtcCallNameText;
    private int mRoomId;
    private String trtcCallType;
    //通话发起人 id
    private String trtcCallFrom;
    //通话发起人 名称
    private String trtcCallName;
    private TRTCCallMessage trtcCallMessage;
    private List<SampleUser> trtcCallReceiveList = new ArrayList<>();

    private CountDownTimer countDownTimer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = getWindow();
        WindowManager.LayoutParams attrs = window.getAttributes();
        attrs.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;
        window.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        window.setAttributes(attrs);
        setContentView(R.layout.activity_trtc_call_receive);

        TRTCCallMessageManager.getInstance().setTRTCAudioCallMessageListener(this);
        TRTCCallMessageManager.getInstance().setTRTCVideoCallMessageListener(this);
        trtcCallNameText = (TextView) findViewById(R.id.call_incoming_name);
        mRingRawId = R.raw.video_phone_receive;

        trtcCallMessage = (TRTCCallMessage) getIntent().getSerializableExtra("TRTCCallMessage");
        trtcCallName = trtcCallMessage.getTrtcCallName();
        trtcCallType = trtcCallMessage.getTrtcCallType();
        trtcCallFrom = trtcCallMessage.getTrtcCallFrom();
        mRoomId = trtcCallMessage.getTrtcCallRoomId();
        trtcCallReceiveList = trtcCallMessage.getTrtcCallReceiveList();
        if (Constents.ONE_TO_ONE_AUDIO_CALL.equals(trtcCallType)) {
            trtcCallNameText.setText(trtcCallName + "邀请你加入语音通话");
        } else if (Constents.ONE_TO_MULTIPE_AUDIO_CALL.equals(trtcCallType)) {
            trtcCallNameText.setText(trtcCallName + "邀请你加入语音群聊");
        } else if (Constents.ONE_TO_ONE_VIDEO_CALL.equals(trtcCallType)) {
            trtcCallNameText.setText(trtcCallName + "邀请你加入视频通话");
        } else if (Constents.ONE_TO_MULTIPE_VIDEO_CALL.equals(trtcCallType)) {
            trtcCallNameText.setText(trtcCallName + "邀请你加入视频群聊");
        }
        ring();
        /** 倒计时30秒，一次1秒 */
        countDownTimer = new CountDownTimer(30 * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onFinish() {
                //倒计时全部结束执行操作
                if (!TRTCCallReceiveActivity.this.isFinishing()) {
                    ringStop();
                    finish();
                }
            }
        };
        countDownTimer.start();
    }

    public void onAnswer(View v) {
        ringStop();
        Intent intent = null;
        if (trtcCallType.equals(Constents.ONE_TO_ONE_AUDIO_CALL)
                || trtcCallType.equals(Constents.ONE_TO_MULTIPE_AUDIO_CALL)) {
            ActivityUtil.destoryAllActivity();
            intent = new Intent(TRTCCallReceiveActivity.this, TRTCAudioCallActivity.class);
        } else if (trtcCallType.equals(Constents.ONE_TO_ONE_VIDEO_CALL)
                || trtcCallType.equals(Constents.ONE_TO_MULTIPE_VIDEO_CALL)) {
            ActivityUtil.destoryAllActivity();
            intent = new Intent(TRTCCallReceiveActivity.this, TRTCVideoCallActivity.class);
        }

        intent.putExtra("roomId", mRoomId);
        intent.putExtra("trtcCallFrom", trtcCallFrom);
        intent.putExtra("trtcCallType", trtcCallType);
        tls_sigature.GenTLSSignatureResult result = tls_sigature.genSig(Constents.APPKEY, ConstData.userid, Constents.PRIVATE_KEY);
        if (result != null && !TextUtils.isEmpty(result.urlSig)) {
            intent.putExtra("sdkAppId", Constents.APPKEY);
            intent.putExtra("userSig", result.urlSig);
            startActivity(intent);
        }
        ConstData.receiveUserSet.addAll(trtcCallReceiveList);
        ConstData.isEnterTRTCCALL = true;
        finish();
    }

    public void onDecline(View v) {
        ringStop();
        if (trtcCallType.equals(Constents.ONE_TO_ONE_AUDIO_CALL)
                || trtcCallType.equals(Constents.ONE_TO_ONE_VIDEO_CALL)) {
            sendDeclineMsg();
        }
        finish();
        if (ConstData.isEnterTRTCCALL) {
            //在这里实现点击重新回到Activity
            Intent reIntent = null;
            if (ConstData.currentTrtcCallType.equals(Constents.ONE_TO_ONE_AUDIO_CALL)
                    || ConstData.currentTrtcCallType.equals(Constents.ONE_TO_MULTIPE_AUDIO_CALL)) {
                reIntent = new Intent(TRTCCallReceiveActivity.this, TRTCAudioCallActivity.class);
            } else if (ConstData.currentTrtcCallType.equals(Constents.ONE_TO_ONE_VIDEO_CALL)
                    || ConstData.currentTrtcCallType.equals(Constents.ONE_TO_MULTIPE_VIDEO_CALL)) {
                reIntent = new Intent(TRTCCallReceiveActivity.this, TRTCVideoCallActivity.class);
            }
            startActivity(reIntent);
        }
    }

    private void sendDeclineMsg() {
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
        //发送在线消息
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

    @Override
    public void onTRTCAudioCallMessageCancel() {
        if (!TRTCCallReceiveActivity.this.isFinishing()) {
            ringStop();
            finish();
        }
    }

    @Override
    public void onTRTCVideoCallMessageCancel() {
        if (!TRTCCallReceiveActivity.this.isFinishing()) {
            ringStop();
            finish();
        }
    }


    private void vibrate() {
        Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(123L);
    }

    private void ring() {
        if (mRingRawId > 0) {
            if (!RingUtils.startRing(getBaseContext(), mRingRawId)) {
                Log.e(TAG, "Ring File Raw Id is wrong");
            }
        }
    }

    private void ringStop() {
        RingUtils.stop();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }
}
