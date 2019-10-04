package com.telewave.battlecommand.mqtt.MqttMessageDto;

import android.util.Log;

import com.telewave.lib.base.ConstData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * 上报设备位置
 *
 * @author liwh
 * @date 2019/1/28
 */
public class CurrentLocationMessage extends BaseMessage {

    //登录状态
    public static final String TYPE_ONE = "1";
    //激活状态
    public static final String TYPE_TWO = "2";
    //警情状态
    public static final String TYPE_THREE = "3";

    private String gisx;
    private String gisy;
    private String wzcyid;
    private String imei;
    private String sendtime;
    //1、登录，2、激活，3、警情
    private String type;
    private String cddid;


    public CurrentLocationMessage() {
        this.setMsgType(MessageType.SEND_LOCATION_MESSAGE);
    }


    public void setBeanData(String status, String cddid) {
        this.setGisx(ConstData.locationLongitude + "");
        this.setGisy(ConstData.locationLatitude + "");
        this.setWzcyid(ConstData.userid);
        this.setImei(ConstData.deviceId);
        this.setSendtime(this.getMsgSendTime());
        this.setType(status);
        this.setCddid(cddid);
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
            MsgContent.put("gisx", this.getGisx());
            MsgContent.put("gisy", this.getGisy());
            MsgContent.put("wzcyid", this.getWzcyid());
            MsgContent.put("imei", this.getImei());
            MsgContent.put("sendtime", this.getMsgSendTime());
            MsgContent.put("type", this.getType());
            MsgContent.put("cddid", this.getCddid());
            jsonObject.put("MsgContent", MsgContent);
            jsonObject.put("msgsender", new JSONObject().put("softcode", "112102"));
            JSONArray jsonArray = new JSONArray();
            jsonArray.put(new JSONObject().put("softcode", "112101"));
            jsonArray.put(new JSONObject().put("softcode", "112202"));
            jsonObject.put("msgreceiver", jsonArray.toString());
            jsonString = jsonObject.toString();

        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.e("toJson", "toJson: " + jsonString);
        return jsonString;
    }


    public String getGisx() {
        return gisx;
    }

    public void setGisx(String gisx) {
        this.gisx = gisx;
    }

    public String getGisy() {
        return gisy;
    }

    public void setGisy(String gisy) {
        this.gisy = gisy;
    }

    public String getWzcyid() {
        return wzcyid;
    }

    public void setWzcyid(String wzcyid) {
        this.wzcyid = wzcyid;
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public String getSendtime() {
        return sendtime;
    }

    public void setSendtime(String sendtime) {
        this.sendtime = sendtime;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCddid() {
        return cddid;
    }

    public void setCddid(String cddid) {
        this.cddid = cddid;
    }


}
