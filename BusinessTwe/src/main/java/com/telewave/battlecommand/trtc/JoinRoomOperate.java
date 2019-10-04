package com.telewave.battlecommand.trtc;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.telewave.battlecommand.trtc.bean.TRTCCallMessage;
import com.telewave.lib.base.ConstData;
import com.telewave.lib.base.Constents;
import com.telewave.lib.base.SampleUser;
import com.telewave.lib.base.util.JsonUtils;
import com.telewave.lib.base.util.SharePreferenceUtils;
import com.tencent.imsdk.TIMConversation;
import com.tencent.imsdk.TIMMessage;
import com.tencent.imsdk.TIMValueCallBack;
import com.tencent.qcloud.tim.uikit.modules.chat.ChatLayout;
import com.tencent.qcloud.tim.uikit.modules.message.MessageInfo;
import com.tencent.qcloud.tim.uikit.modules.message.MessageInfoUtil;
import com.tls.tls_sigature.tls_sigature;

import java.io.Serializable;
import java.util.List;

/**
 * @author liwh
 * @date 2019/6/20
 */
public class JoinRoomOperate {
    private Context mContext;
    private int roomId;

    public JoinRoomOperate(Context context) {
        this.mContext = context;
        long nowTime = System.currentTimeMillis() / 1000;
        this.roomId = new Long(nowTime).intValue();
    }


    public JoinRoomOperate(Context context, int roomId) {
        this.mContext = context;
        this.roomId = roomId;
    }

    public void onTRTCCallJoinRoom(ChatLayout mChatLayout, String trtcCallType, String typeDesc,
                                   List<SampleUser> sampleUserList) {
        String userId = SharePreferenceUtils.getDataSharedPreferences(mContext, "userid");
        String userName = SharePreferenceUtils.getDataSharedPreferences(mContext, "username");
        Intent intent = null;
        if (trtcCallType.equals(Constents.ONE_TO_ONE_AUDIO_CALL)
                || trtcCallType.equals(Constents.ONE_TO_MULTIPE_AUDIO_CALL)) {
            intent = new Intent(mContext, TRTCAudioCallActivity.class);
        } else if (trtcCallType.equals(Constents.ONE_TO_ONE_VIDEO_CALL)
                || trtcCallType.equals(Constents.ONE_TO_MULTIPE_VIDEO_CALL)) {
            intent = new Intent(mContext, TRTCVideoCallActivity.class);
        }
        intent.putExtra("roomId", roomId);
        intent.putExtra("trtcCallFrom", userId);
        intent.putExtra("trtcCallType", trtcCallType);
        //此处传入未加入自己的 接收人列表
        intent.putExtra("receiveUserList", (Serializable) sampleUserList);
        tls_sigature.GenTLSSignatureResult result = tls_sigature.genSig(Constents.APPKEY, userId, Constents.PRIVATE_KEY);
        if (result != null && !TextUtils.isEmpty(result.urlSig)) {
            intent.putExtra("sdkAppId", Constents.APPKEY);
            intent.putExtra("userSig", result.urlSig);
            mContext.startActivity(intent);
        }
        TRTCCallMessage trtcCallMessage = new TRTCCallMessage(trtcCallType);
        trtcCallMessage.setTrtcCallFrom(userId);
        trtcCallMessage.setTrtcCallName(userName);
        trtcCallMessage.setTrtcCallRoomId(roomId);
        trtcCallMessage.setTrtcCallType(trtcCallType);
        //单人语音、视频通话时将自己加入列表中
        //群不需要，因为群插查了包括自己的所有成员
        if (trtcCallType.equals(Constents.ONE_TO_ONE_AUDIO_CALL)
                || trtcCallType.equals(Constents.ONE_TO_ONE_VIDEO_CALL)) {
            //将自己也加入进去
            SampleUser selfSampleUser = new SampleUser(userId, userName);
            sampleUserList.add(selfSampleUser);
        }

        trtcCallMessage.setTrtcCallReceiveList(sampleUserList);
        Gson gson = new Gson();
        String msgStr = JsonUtils.toJson(typeDesc, gson.toJson(trtcCallMessage));
        MessageInfo info = MessageInfoUtil.buildCustomTRTCMessage(typeDesc, msgStr);
        mChatLayout.sendMessage(info, false);

        //发送语音、视频通话方 保存数据
        ConstData.receiveUserSet.addAll(sampleUserList);
        ConstData.isEnterTRTCCALL = true;
    }

    public void onTRTCCallJoinRoom(TIMConversation conversation, String trtcCallType, String typeDesc,
                                   List<SampleUser> sampleUserList) {
        String userId = SharePreferenceUtils.getDataSharedPreferences(mContext, "userid");
        String userName = SharePreferenceUtils.getDataSharedPreferences(mContext, "username");
        Intent intent = null;
        if (trtcCallType.equals(Constents.ONE_TO_ONE_AUDIO_CALL)
                || trtcCallType.equals(Constents.ONE_TO_MULTIPE_AUDIO_CALL)) {
            intent = new Intent(mContext, TRTCAudioCallActivity.class);
        } else if (trtcCallType.equals(Constents.ONE_TO_ONE_VIDEO_CALL)
                || trtcCallType.equals(Constents.ONE_TO_MULTIPE_VIDEO_CALL)) {
            intent = new Intent(mContext, TRTCVideoCallActivity.class);
        }
        intent.putExtra("roomId", roomId);
        intent.putExtra("trtcCallFrom", userId);
        intent.putExtra("trtcCallType", trtcCallType);
        //此处传入未加入自己的 接收人列表
        intent.putExtra("receiveUserList", (Serializable) sampleUserList);
        tls_sigature.GenTLSSignatureResult result = tls_sigature.genSig(Constents.APPKEY, userId, Constents.PRIVATE_KEY);
        if (result != null && !TextUtils.isEmpty(result.urlSig)) {
            intent.putExtra("sdkAppId", Constents.APPKEY);
            intent.putExtra("userSig", result.urlSig);
            mContext.startActivity(intent);
        }
        TRTCCallMessage trtcCallMessage = new TRTCCallMessage(trtcCallType);
        trtcCallMessage.setTrtcCallFrom(userId);
        trtcCallMessage.setTrtcCallName(userName);
        trtcCallMessage.setTrtcCallRoomId(roomId);
        trtcCallMessage.setTrtcCallType(trtcCallType);
        //单人语音、视频通话时将自己加入列表中
        //群不需要，因为群插查了包括自己的所有成员
        if (trtcCallType.equals(Constents.ONE_TO_ONE_AUDIO_CALL)
                || trtcCallType.equals(Constents.ONE_TO_ONE_VIDEO_CALL)) {
            //将自己也加入进去
            SampleUser selfSampleUser = new SampleUser(userId, userName);
            sampleUserList.add(selfSampleUser);
        }

        trtcCallMessage.setTrtcCallReceiveList(sampleUserList);
        Gson gson = new Gson();
        String msgStr = JsonUtils.toJson(typeDesc, gson.toJson(trtcCallMessage));
        MessageInfo info = MessageInfoUtil.buildCustomTRTCMessage(typeDesc, msgStr);
        //发送消息
        conversation.sendOnlineMessage(info.getTIMMessage(), new TIMValueCallBack<TIMMessage>() {
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
        //发送语音、视频通话方 保存数据
        ConstData.receiveUserSet.addAll(sampleUserList);
        ConstData.isEnterTRTCCALL = true;
    }

}
