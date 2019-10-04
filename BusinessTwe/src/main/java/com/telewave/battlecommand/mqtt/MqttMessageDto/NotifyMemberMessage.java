package com.telewave.battlecommand.mqtt.MqttMessageDto;

/**
 * 通知消息（指定成员）
 *
 * @author liwh
 * @date 2019/1/16
 */
public class NotifyMemberMessage extends BaseMessage {
    //成员ID
    private String member;
    //通知ID
    private String noticeid;
    // 发送时间
    private String sendtime;
    //标题
    private String title;
    //内容
    private String content;
    //通知发起用户id
    private String userid;
    //通知发起用户名称
    private String username;
    //通知发起用户所属机构id
    private String organid;
    //通知发起用户所属机构名称
    private String organname;


    public NotifyMemberMessage() {
        this.setMsgType(MessageType.NOTIFY_MEMBER_MESSAGE);
    }

    public String getMember() {
        return member;
    }

    public void setMember(String member) {
        this.member = member;
    }

    public String getNoticeid() {
        return noticeid;
    }

    public void setNoticeid(String noticeid) {
        this.noticeid = noticeid;
    }

    public String getSendtime() {
        return sendtime;
    }

    public void setSendtime(String sendtime) {
        this.sendtime = sendtime;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getOrganid() {
        return organid;
    }

    public void setOrganid(String organid) {
        this.organid = organid;
    }

    public String getOrganname() {
        return organname;
    }

    public void setOrganname(String organname) {
        this.organname = organname;
    }
}
