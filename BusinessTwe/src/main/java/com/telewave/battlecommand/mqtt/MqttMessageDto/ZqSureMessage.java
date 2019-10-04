package com.telewave.battlecommand.mqtt.MqttMessageDto;


import android.util.Log;

import com.telewave.lib.base.ConstData;
import com.telewave.lib.base.util.DateUtils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 灾情确认消息
 *
 * @author liwh
 * @date 2018/12/27
 */
public class ZqSureMessage extends BaseMessage {

    //已确认
    public static final String STATUS_SURE = "1";
    //已收到
    public static final String STATUS_RECEIVE = "2";
    //不在线
    public static final String STATUS_THREE = "3";
    //确认超时
    public static final String STATUS_FOUR = "4";
    //警情ID
    private String alarmid;
    //出动单ID
    private String cddid;
    //出动单编码
    private String cddbm;
    //调派机构ID
    private String dispatchorganid;
    //调派席位号
    private String dispatchseatno;
    //出动单发送时间
    private String cddfssj;
    //"调派状态，1：已确认2：已收到3：不在线4：确认超时"
    private String status;
    //确认机构ID
    private String confirmorganid;
    //确认时间
    private String confirmtime;

    public ZqSureMessage() {
        this.setMsgType(MessageType.SURE_RECEIVE_NEW_DISASTER_INFO);
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
            MsgContent.put("alarmid", this.getAlarmid());
            MsgContent.put("cddid", this.getCddid());
            MsgContent.put("cddbm", this.getCddbm());
            MsgContent.put("dispatchorganid", this.getDispatchorganid());
            MsgContent.put("dispatchseatno", this.getDispatchseatno());
            MsgContent.put("cddfssj", this.getCddfssj());
            MsgContent.put("status", this.getStatus());
            MsgContent.put("confirmorganid", this.getConfirmorganid());
            MsgContent.put("confirmtime", this.getConfirmtime());
            jsonObject.put("MsgContent", MsgContent);
            jsonString = jsonObject.toString();

        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.e("toJson", "toJson: " + jsonString);
        return jsonString;
    }


    public String getAlarmid() {
        return alarmid;
    }

    public void setAlarmid(String alarmid) {
        this.alarmid = alarmid;
    }

    public String getCddid() {
        return cddid;
    }

    public void setCddid(String cddid) {
        this.cddid = cddid;
    }

    public String getCddbm() {
        return cddbm;
    }

    public void setCddbm(String cddbm) {
        this.cddbm = cddbm;
    }

    public String getDispatchorganid() {
        return dispatchorganid;
    }

    public void setDispatchorganid(String dispatchorganid) {
        this.dispatchorganid = dispatchorganid;
    }

    public String getDispatchseatno() {
        return dispatchseatno;
    }

    public void setDispatchseatno(String dispatchseatno) {
        this.dispatchseatno = dispatchseatno;
    }

    public String getCddfssj() {
        return cddfssj;
    }

    public void setCddfssj(String cddfssj) {
        this.cddfssj = cddfssj;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getConfirmorganid() {
        return confirmorganid;
    }

    public void setConfirmorganid(String confirmorganid) {
        this.confirmorganid = confirmorganid;
    }

    public String getConfirmtime() {
        return confirmtime;
    }

    public void setConfirmtime(String confirmtime) {
        this.confirmtime = confirmtime;
    }

    public void setBeanData(String status, NewDisasterInfo.MsgContentBean msgContentBean) {
        this.setAlarmid(msgContentBean.getZq().getAlarmid());
        this.setCddid(msgContentBean.getCdd().getId());
        this.setCddbm(msgContentBean.getCdd().getCddbm());
        this.setDispatchorganid(msgContentBean.getOrganid());
        this.setDispatchseatno(msgContentBean.getCdd().getProcseat());
        this.setCddfssj(msgContentBean.getCdd().getFssj());
        this.setStatus(status);
        this.setConfirmorganid(ConstData.ORGANID);
        this.setConfirmtime(DateUtils.getNowTime(DateUtils.FORMAT_STYLE_F));
    }
}
