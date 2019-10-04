package com.telewave.battlecommand.mqtt.MqttListener;


import com.telewave.battlecommand.mqtt.MqttMessageDto.CallPoliceMessage;
import com.telewave.battlecommand.mqtt.MqttMessageDto.NewDisasterInfo;
import com.telewave.battlecommand.mqtt.MqttMessageDto.NotifyMemberMessage;
import com.telewave.battlecommand.mqtt.MqttMessageDto.NotifyWeiZhanMessage;
import com.telewave.battlecommand.mqtt.MqttMessageDto.ReceiveRollCallMessage;

import org.eclipse.paho.client.mqttv3.MqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttTopic;

/**
 * mqtt接口 处理后台返回的消息
 * Created by wc on 2016/10/17.
 */
public class MqttListener {
    /**
     * mqtt接受数据回调
     */
    public interface MqttCallBackListener {
        /**
         * 向后台发送数据后 返回 token.isComplete()发送是否成功
         *
         * @param token
         */
        public void MqttDeliveryComplete(MqttDeliveryToken token);

        /**
         * 收到后台返回的消息
         *
         * @param topic
         * @param message
         */
        public void MqttMessageArrived(MqttTopic topic, MqttMessage message);
    }

    /**
     * Mqtt初始化连接返回状态 连接成功则可进行主题订阅（用于通用主题连接）
     */
    public interface MqttInitConnectCallBackListener {
        public void MqttInitConnectCallBack(boolean isSuccess);
    }


    /**
     * 新灾情
     */
    public interface NewDisasterInfoListener {
        /**
         * 服务器推送过来的新灾情
         *
         * @param msgContentBean 新灾情的信息
         */
        void onNewDisasterInfoArrived(NewDisasterInfo.MsgContentBean msgContentBean);
    }


    /**
     * 接收报警信息
     */
    public interface ReceiveCallPoliceInfoListener {
        /**
         * 服务器推送过来的新报警
         *
         * @param callPoliceMessage 新报警的信息
         */
        void onCallPoliceInfo(CallPoliceMessage callPoliceMessage);
    }


    /**
     * 指定成员通知
     */
    public interface NotifyMemberListener {
        //接收通知
        void onNotifyMemberArrived(NotifyMemberMessage message);
    }

    /**
     * 指定微站通知
     */
    public interface NotifyWeiZhanListener {
        //接收通知
        void onNotifyWeiZhanArrived(NotifyWeiZhanMessage message);
    }

    /**
     * 点名消息
     */
    public interface RollCallListener {
        //接收消息
        void onRollCallArrived(ReceiveRollCallMessage message);
    }

    /**
     * 退出系统消息
     */
    public interface ExitSystemListener {
        //接收消息
        void onExitSystemCallBack(String imei);
    }

    /**
     * 解绑系统消息
     */
    public interface UnBindSystemListener {
        //接收消息
        void onUnBindSystemCallBack(String imei);
    }

}
