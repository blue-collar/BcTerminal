package com.telewave.battlecommand.mqtt.MqttMessageDto;

/**
 * 传输的消息类型
 *
 * @author liwh
 * @date 2018/12/27
 */
public class MessageType {
    /**
     * 纠偏
     */
    public final static String CORRECT_DISASTER_ADDRESS = "Z203";
    /**
     * IM 及时通讯类型 消息
     */
    public final static String CHAT_MESSAGE = "Z210";
    /**
     * 新的警情
     */
    public final static String NEW_DISASTER_INFO = "Z302";

    /**
     * 确定接收新的警情
     */
    public final static String SURE_RECEIVE_NEW_DISASTER_INFO = "Z208";

    /**
     * 接收发给指定成员的通知
     */
    public final static String NOTIFY_MEMBER_MESSAGE = "Z310";
    /**
     * 接收发给指定微站的通知
     */
    public final static String NOTIFY_WEIZHAN_MESSAGE = "Z311";
    /**
     * 接收点名消息
     */
    public final static String RECEIVE_ROLL_CALL_MESSAGE = "Z313";

    /**
     * 发送点名应答消息
     */
    public final static String SEND_ROLL_CALL_MESSAGE = "Z314";

    /**
     * 发送定位消息
     */
    public final static String SEND_LOCATION_MESSAGE = "Z902";

    /**
     * 接收退出登录用户消息
     * APP 要退出登录
     */
    public final static String RECEIVE_EXITSYSTEM_MESSAGE = "Z401";

    /**
     * 接收终端解绑消息
     * APP 要退出登录且要重新扫码连接服务
     */
    public final static String RECEIVE_UNBIND_AND_EXITSYSTEM_MESSAGE = "Z402";

    /**
     * App请求更新灾情状态后
     * 发送消息到其他App
     */
    public final static String APP_DISASTER_STATUS_MESSAGE = "Z800";

    /**
     * 新的报警
     */
    public final static String RECEIVE_CALL_POLICE_MESSAGE = "Z303";


}
