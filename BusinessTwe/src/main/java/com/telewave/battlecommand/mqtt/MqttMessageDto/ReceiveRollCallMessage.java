package com.telewave.battlecommand.mqtt.MqttMessageDto;

import java.io.Serializable;
import java.util.List;

/**
 * 点名消息
 *
 * @author liwh
 * @date 2019/1/16
 */
public class ReceiveRollCallMessage extends BaseMessage implements Serializable {

    /**
     * calltime : 2019-01-16 17:14:51
     * cutofftime : 2019-01-16 17:08:50
     * firestation : c36f443b940e486f9779c381e7e08f71
     * firestations : [{"id":"c36f443b940e486f9779c381e7e08f71"}]
     * organid : a91670095afe4801a1255361bcf3d0fb
     * rollcallid : aa966a4b121a4c55b7582f8def03b739
     * userid : 1
     * username : 系统管理员
     */
    private String calltime;
    private String cutofftime;
    private String firestation;
    private String organid;
    private String rollcallid;
    private String userid;
    private String username;
    private String organname;
    private List<FirestationsBean> firestations;


    public String getCalltime() {
        return calltime;
    }

    public void setCalltime(String calltime) {
        this.calltime = calltime;
    }

    public String getCutofftime() {
        return cutofftime;
    }

    public void setCutofftime(String cutofftime) {
        this.cutofftime = cutofftime;
    }

    public String getFirestation() {
        return firestation;
    }

    public void setFirestation(String firestation) {
        this.firestation = firestation;
    }

    public String getOrganid() {
        return organid;
    }

    public void setOrganid(String organid) {
        this.organid = organid;
    }

    public String getRollcallid() {
        return rollcallid;
    }

    public void setRollcallid(String rollcallid) {
        this.rollcallid = rollcallid;
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

    public String getOrganname() {
        return organname;
    }

    public void setOrganname(String organname) {
        this.organname = organname;
    }

    public List<FirestationsBean> getFirestations() {
        return firestations;
    }

    public void setFirestations(List<FirestationsBean> firestations) {
        this.firestations = firestations;
    }

    public static class FirestationsBean implements Serializable {
        /**
         * id : c36f443b940e486f9779c381e7e08f71
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
