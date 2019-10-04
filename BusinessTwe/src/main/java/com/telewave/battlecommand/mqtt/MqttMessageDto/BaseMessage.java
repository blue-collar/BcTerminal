package com.telewave.battlecommand.mqtt.MqttMessageDto;


import com.telewave.lib.base.util.DateUtils;

/**
 * 消息基类
 *
 * @author liwh
 * @date 2018/12/27
 */
public abstract class BaseMessage {
    /**
     * 请求Id
     */
    private String MsgId;
    /**
     * 请求类型
     */
    private String MsgType;

    //"2016-11-08 10:32:22",
    private String MsgSendTime;

    /**
     * 发送者
     */
    private String MsgSender;


    public BaseMessage() {
        this.MsgId = DateUtils.getUUID();
        this.MsgSendTime = DateUtils.getNowTime(DateUtils.FORMAT_STYLE_F);
        this.MsgSender = "WZ";
    }

    public void setMsgType(String msgType) {
        MsgType = msgType;
    }

    public String getMsgId() {
        return MsgId;
    }

    public String getMsgType() {
        return MsgType;
    }

    public String getMsgSendTime() {
        return MsgSendTime;
    }

    public String getMsgSender() {
        return MsgSender;
    }

    public void setMsgSender(String msgSender) {
        MsgSender = msgSender;
    }
}
