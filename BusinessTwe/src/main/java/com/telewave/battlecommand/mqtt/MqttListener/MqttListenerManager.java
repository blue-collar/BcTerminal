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
 * 管理并实例化接口参数
 *
 * @author liwh
 * @date 2018/12/5
 */
public class MqttListenerManager {

    private static MqttListenerManager mqttListenerManager;

    private MqttListenerManager() {
    }

    public static MqttListenerManager getInstance() {
        if (mqttListenerManager == null) {
            mqttListenerManager = new MqttListenerManager();
        }
        return mqttListenerManager;
    }

    /********************************* Mqtt 回调接口dome 可不用 ***************************************************/
    private MqttListener.MqttCallBackListener mqttCallBackListener;

    /**
     * @param mqttCallBackListener
     */
    public void setMqttCallBackListener(MqttListener.MqttCallBackListener mqttCallBackListener) {
        this.mqttCallBackListener = mqttCallBackListener;
    }

    /**
     * @param token
     */
    public void SendMqttDeliveryComplete(MqttDeliveryToken token) {
        if (this.mqttCallBackListener != null) {
            this.mqttCallBackListener.MqttDeliveryComplete(token);
        }
    }

    /**
     * @param topic
     * @param message
     */
    public void SendMqttMessageArrived(MqttTopic topic, MqttMessage message) {
        if (this.mqttCallBackListener != null) {
            this.mqttCallBackListener.MqttMessageArrived(topic, message);
        }
    }

    /********************************* Mqtt初始化连接回调 ***************************************************/
    private MqttListener.MqttInitConnectCallBackListener mqttInitConnectCallBackListener;

    public void setMqttInitConnectCallBackListener(MqttListener.MqttInitConnectCallBackListener mqttInitConnectCallBackListener) {
        this.mqttInitConnectCallBackListener = mqttInitConnectCallBackListener;
    }

    public void SendMqttInitConnectCallBack(boolean isSuccess) {
        if (this.mqttInitConnectCallBackListener != null) {
            this.mqttInitConnectCallBackListener.MqttInitConnectCallBack(isSuccess);
        }
    }


    /**
     * 新灾情
     */
    private MqttListener.NewDisasterInfoListener newDisasterInfoListener;

    public void setNewDisasterInfoListener(MqttListener.NewDisasterInfoListener listener) {
        this.newDisasterInfoListener = listener;
    }

    public void sendNewDisasterArrived(NewDisasterInfo.MsgContentBean msgContentBean) {
        if (this.newDisasterInfoListener != null) {
            this.newDisasterInfoListener.onNewDisasterInfoArrived(msgContentBean);
        }
    }

    /**
     * 接收新报警信息监听
     */
    private MqttListener.ReceiveCallPoliceInfoListener receiveCallPoliceInfoListener;

    public void setReceiveCallPoliceInfoListener(MqttListener.ReceiveCallPoliceInfoListener listener) {
        this.receiveCallPoliceInfoListener = listener;
    }

    public void sendCallPoliceInfoArrived(CallPoliceMessage callPoliceMessage) {
        if (this.receiveCallPoliceInfoListener != null) {
            this.receiveCallPoliceInfoListener.onCallPoliceInfo(callPoliceMessage);
        }
    }


    /**
     * 指定成员通知
     */
    private MqttListener.NotifyMemberListener notifyMemberListener;

    public void setNotifyMemberListener(MqttListener.NotifyMemberListener listener) {
        this.notifyMemberListener = listener;
    }

    public void sendNotifyMember(NotifyMemberMessage message) {
        if (this.notifyMemberListener != null) {
            this.notifyMemberListener.onNotifyMemberArrived(message);
        }
    }

    /**
     * 指定微站通知
     */
    private MqttListener.NotifyWeiZhanListener notifyWeiZhanListener;

    public void setNotifyWeiZhanListener(MqttListener.NotifyWeiZhanListener listener) {
        this.notifyWeiZhanListener = listener;
    }

    public void sendNotifyWeiZhan(NotifyWeiZhanMessage message) {
        if (this.notifyWeiZhanListener != null) {
            this.notifyWeiZhanListener.onNotifyWeiZhanArrived(message);
        }
    }

    /**
     * 点名消息
     */
    private MqttListener.RollCallListener rollCallListener;

    public void setRollCallListener(MqttListener.RollCallListener listener) {
        this.rollCallListener = listener;
    }

    public void sendRollCall(ReceiveRollCallMessage message) {
        if (this.rollCallListener != null) {
            this.rollCallListener.onRollCallArrived(message);
        }
    }


    /**
     * 退出登录用户消息
     */
    private MqttListener.ExitSystemListener exitSystemListener;

    public void setExitSystemListener(MqttListener.ExitSystemListener listener) {
        this.exitSystemListener = listener;
    }

    public void sendExitSystem(String imei) {
        if (this.exitSystemListener != null) {
            this.exitSystemListener.onExitSystemCallBack(imei);
        }
    }

    /**
     * 解绑消息
     */
    private MqttListener.UnBindSystemListener unBindSystemListener;

    public void setUnBindSystemListener(MqttListener.UnBindSystemListener listener) {
        this.unBindSystemListener = listener;
    }

    public void sendUnBindSystem(String imei) {
        if (this.unBindSystemListener != null) {
            this.unBindSystemListener.onUnBindSystemCallBack(imei);
        }
    }


}
