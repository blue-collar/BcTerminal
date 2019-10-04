package com.telewave.battlecommand.mqtt.MqttFilter;

import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.telewave.battlecommand.mqtt.MqttListener.MqttListenerManager;
import com.telewave.battlecommand.mqtt.MqttMessageDto.CallPoliceMessage;
import com.telewave.battlecommand.mqtt.MqttMessageDto.MessageType;
import com.telewave.battlecommand.mqtt.MqttMessageDto.NewDisasterInfo;
import com.telewave.battlecommand.mqtt.MqttMessageDto.NotifyMemberMessage;
import com.telewave.battlecommand.mqtt.MqttMessageDto.NotifyWeiZhanMessage;
import com.telewave.battlecommand.mqtt.MqttMessageDto.ReceiveRollCallMessage;
import com.telewave.battlecommand.mqtt.MqttMessageDto.ZqSureMessage;
import com.telewave.battlecommand.service.MyMqttService;
import com.telewave.lib.base.ConstData;
import com.telewave.lib.base.util.ParseJsonUtils;

import org.json.JSONObject;


/**
 * 过滤器 过滤后台推送过来的消息
 *
 * @author liwh
 * @date 2018/12/10
 */
public class MqttFilter {
    private static final String TAG = "MqttFilter";

    public static void MsgHandle(String jsonResult) {
        Log.e(TAG, "MsgHandle: " + jsonResult);
        try {
            JSONObject jsonObject = new JSONObject(jsonResult);
            String msgType = jsonObject.getString("MsgType");
            //收到发给指定成员的通知消息
            if (msgType.equals(MessageType.NOTIFY_MEMBER_MESSAGE)) {
                NotifyMemberMessage notifyMemberMessage = (NotifyMemberMessage) ParseJsonUtils.getObjectFromArrayJson(jsonResult, NotifyMemberMessage.class, "MsgContent");
                if (notifyMemberMessage != null && !TextUtils.isEmpty(notifyMemberMessage.getNoticeid())
                        && !TextUtils.isEmpty(notifyMemberMessage.getMember())
                        && (notifyMemberMessage.getMember().equals(ConstData.userid))) {
                    MqttListenerManager.getInstance().sendNotifyMember(notifyMemberMessage);
                }
            }
            //收到发给指定微站的通知消息
            else if (msgType.equals(MessageType.NOTIFY_WEIZHAN_MESSAGE)) {
                NotifyWeiZhanMessage notifyWeiZhanMessage = (NotifyWeiZhanMessage) ParseJsonUtils.getObjectFromArrayJson(jsonResult, NotifyWeiZhanMessage.class, "MsgContent");
                if (notifyWeiZhanMessage != null && !TextUtils.isEmpty(notifyWeiZhanMessage.getNoticeid())
                        && !TextUtils.isEmpty(notifyWeiZhanMessage.getFirestation())
                        && (notifyWeiZhanMessage.getFirestation().equals(ConstData.ORGANID))) {
                    MqttListenerManager.getInstance().sendNotifyWeiZhan(notifyWeiZhanMessage);
                }
            }
            //收到点名消息
            else if (msgType.equals(MessageType.RECEIVE_ROLL_CALL_MESSAGE)) {
                ReceiveRollCallMessage rollCallMessage = (ReceiveRollCallMessage) ParseJsonUtils.getObjectFromArrayJson(jsonResult, ReceiveRollCallMessage.class, "MsgContent");
                if (rollCallMessage != null && !TextUtils.isEmpty(rollCallMessage.getRollcallid())
                        && !TextUtils.isEmpty(rollCallMessage.getFirestation())
                        && (rollCallMessage.getFirestation().equals(ConstData.ORGANID))) {
                    MqttListenerManager.getInstance().sendRollCall(rollCallMessage);
                }
            }
            //收到退出登录用户消息
            else if (msgType.equals(MessageType.RECEIVE_EXITSYSTEM_MESSAGE)) {
                String msgContent = jsonObject.getString("MsgContent");
                JSONObject msgContentJsonObject = new JSONObject(msgContent);
                String imei = msgContentJsonObject.getString("imei");
                if (!TextUtils.isEmpty(imei)) {
                    MqttListenerManager.getInstance().sendExitSystem(imei);
                }
            }
            //接收终端解绑消息
            else if (msgType.equals(MessageType.RECEIVE_UNBIND_AND_EXITSYSTEM_MESSAGE)) {
                String msgContent = jsonObject.getString("MsgContent");
                JSONObject msgContentJsonObject = new JSONObject(msgContent);
                String imei = msgContentJsonObject.getString("imei");
                if (!TextUtils.isEmpty(imei)) {
                    MqttListenerManager.getInstance().sendUnBindSystem(imei);
                }
            }
            //中队收到调派新警情
            else if (msgType.equals(MessageType.NEW_DISASTER_INFO)) {
                Log.e(TAG, "NEW_DISASTER_INFO1: " + msgType);
                Log.e(TAG, "NEW_DISASTER_INFO2: " + jsonResult);
                Gson gson = new Gson();
                NewDisasterInfo newDisasterInfo = gson.fromJson(jsonResult, NewDisasterInfo.class);
                if (newDisasterInfo != null && newDisasterInfo.getMsgContent() != null
                        && newDisasterInfo.getMsgContent().getCdd() != null) {
                    NewDisasterInfo.MsgContentBean.CddBean cddBean = newDisasterInfo.getMsgContent().getCdd();
                    if (ConstData.ORGANID.equals(cddBean.getJsdwdm())) {
                        NewDisasterInfo.MsgContentBean msgContentBean = newDisasterInfo.getMsgContent();
                        MqttListenerManager.getInstance().sendNewDisasterArrived(msgContentBean);
                        /**
                         * 2019/05/09
                         * 新增
                         * 收到新警情
                         * 第一次回执
                         */
                        ZqSureMessage zqSureMessage = new ZqSureMessage();
                        zqSureMessage.setBeanData(ZqSureMessage.STATUS_RECEIVE, msgContentBean);
                        MyMqttService.publishMessage(zqSureMessage.toJson());
                    }
                }
            }
            //收到新的报警信息
            else if (msgType.equals(MessageType.RECEIVE_CALL_POLICE_MESSAGE)) {
                Log.e("msgType", "RECEIVE_CALL_POLICE_MESSAGE: " + msgType);
                Gson gson = new Gson();
                String msgContent = jsonObject.getString("MsgContent");
                CallPoliceMessage callPoliceMessage = gson.fromJson(msgContent, CallPoliceMessage.class);
                String organid = callPoliceMessage.getOrganid();
                String detachmentId = callPoliceMessage.getDetachmentId();
                if (!TextUtils.isEmpty(organid) && ConstData.ORGANID.equals(organid)) {
                    MqttListenerManager.getInstance().sendCallPoliceInfoArrived(callPoliceMessage);
                }
                if (!TextUtils.isEmpty(detachmentId) && ConstData.ORGANID.equals(detachmentId)) {
                    MqttListenerManager.getInstance().sendCallPoliceInfoArrived(callPoliceMessage);
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

