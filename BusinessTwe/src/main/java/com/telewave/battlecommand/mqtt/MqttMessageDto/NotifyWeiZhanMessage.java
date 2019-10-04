package com.telewave.battlecommand.mqtt.MqttMessageDto;

import java.util.List;

/**
 * 通知消息（指定微站）
 *
 * @author liwh
 * @date 2019/1/16
 */
public class NotifyWeiZhanMessage extends BaseMessage {

    /**
     * content : sdsdgf
     * firestation : c36f443b940e486f9779c381e7e08f71
     * firestations : [{"id":"c27f4433940e486f9779c381e7e08f7e"},{"id":"c36f443b940e486f9779c381e7e08f71"}]
     * noticeid : 66c932e52e2b483d9be3e649aefd0ba3
     * organid : a91670095afe4801a1255361bcf3d0fb
     * organname : 西藏自治区公安消防总队
     * sendtime : 2019-01-16 16:40:23
     * title : fggs
     * userid : 1
     * username : 系统管理员
     */

    private String content;
    private String firestation;
    private String noticeid;
    private String organid;
    private String organname;
    private String sendtime;
    private String title;
    private String userid;
    private String username;
    private List<FirestationsBean> firestations;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getFirestation() {
        return firestation;
    }

    public void setFirestation(String firestation) {
        this.firestation = firestation;
    }

    public String getNoticeid() {
        return noticeid;
    }

    public void setNoticeid(String noticeid) {
        this.noticeid = noticeid;
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

    public List<FirestationsBean> getFirestations() {
        return firestations;
    }

    public void setFirestations(List<FirestationsBean> firestations) {
        this.firestations = firestations;
    }

    public static class FirestationsBean {
        /**
         * id : c27f4433940e486f9779c381e7e08f7e
         */

        private String id;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }
    }
}
