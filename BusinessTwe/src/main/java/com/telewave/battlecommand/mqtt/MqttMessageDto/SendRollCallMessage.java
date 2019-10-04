package com.telewave.battlecommand.mqtt.MqttMessageDto;

import android.util.Log;

import com.telewave.lib.base.ConstData;
import com.telewave.lib.base.util.DateUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * 发送点名应答消息
 *
 * @author liwh
 * @date 2019/1/16
 */
public class SendRollCallMessage extends BaseMessage implements Serializable {

    //MQ发送给后台要用
    private String rollcallid;
    private String firestationid;
    private String answertime;
    private String filepath;
    private String mapx;
    private String mapy;
    private String address;

    public SendRollCallMessage() {
        this.setMsgType(MessageType.SEND_ROLL_CALL_MESSAGE);
    }

    public String toJson() {
        String jsonString = "";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("MsgId", this.getMsgId());
            jsonObject.put("MsgType", this.getMsgType());
            jsonObject.put("MsgSendTime", this.getMsgSendTime());
            jsonObject.put("MsgSender", this.getMsgSender());

            JSONObject MsgContent = new JSONObject();
            MsgContent.put("rollcallid", this.getRollcallid());
            MsgContent.put("firestationid", ConstData.ORGANID);
            MsgContent.put("answertime", DateUtils.getNowTime(DateUtils.FORMAT_STYLE_F));
            MsgContent.put("filepath", this.getFilepath());
            MsgContent.put("mapx", ConstData.locationLongitude);
            MsgContent.put("mapy", ConstData.locationLatitude);
            MsgContent.put("address", ConstData.locationAddress);
            jsonObject.put("MsgContent", MsgContent);
            jsonString = jsonObject.toString();

        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.e("toJson", "toJson: " + jsonString);
        return jsonString;
    }

    public void setBeanData(String filepath, ReceiveRollCallMessage msgContentBean) {
        this.setRollcallid(msgContentBean.getRollcallid());
        this.setFilepath(filepath);
    }

    public String getRollcallid() {
        return rollcallid;
    }

    public void setRollcallid(String rollcallid) {
        this.rollcallid = rollcallid;
    }

    public String getFirestationid() {
        return firestationid;
    }

    public void setFirestationid(String firestationid) {
        this.firestationid = firestationid;
    }

    public String getAnswertime() {
        return answertime;
    }

    public void setAnswertime(String answertime) {
        this.answertime = answertime;
    }

    public String getFilepath() {
        return filepath;
    }

    public void setFilepath(String filepath) {
        this.filepath = filepath;
    }

    public String getMapx() {
        return mapx;
    }

    public void setMapx(String mapx) {
        this.mapx = mapx;
    }

    public String getMapy() {
        return mapy;
    }

    public void setMapy(String mapy) {
        this.mapy = mapy;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
