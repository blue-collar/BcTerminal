package com.telewave.battlecommand.trtc.bean;

import com.telewave.lib.base.SampleUser;

import java.io.Serializable;
import java.util.List;


/**
 * 腾讯实时音视频通话消息类
 *
 * @author liwh
 * @date 2019/6/13
 */
public class TRTCCallMessage implements Serializable {
    //通话发起人（拨号者）
    private String trtcCallFrom;
    //通话发起人名称
    private String trtcCallName;
    //通话接收人列表
    private List<SampleUser> trtcCallReceiveList;
    //通话 房间号
    private int trtcCallRoomId;

    //通话类型
    // 1、1对1语音通话 2、1多对语音通话  3、1对1视频通话  4、1对多视频通话
    private String trtcCallType;

    public TRTCCallMessage(String trtcCallType) {
        this.trtcCallType = trtcCallType;
    }

    public String getTrtcCallFrom() {
        return trtcCallFrom;
    }

    public void setTrtcCallFrom(String trtcCallFrom) {
        this.trtcCallFrom = trtcCallFrom;
    }

    public String getTrtcCallName() {
        return trtcCallName;
    }

    public void setTrtcCallName(String trtcCallName) {
        this.trtcCallName = trtcCallName;
    }

    public List<SampleUser> getTrtcCallReceiveList() {
        return trtcCallReceiveList;
    }

    public void setTrtcCallReceiveList(List<SampleUser> trtcCallReceiveList) {
        this.trtcCallReceiveList = trtcCallReceiveList;
    }

    public int getTrtcCallRoomId() {
        return trtcCallRoomId;
    }

    public void setTrtcCallRoomId(int trtcCallRoomId) {
        this.trtcCallRoomId = trtcCallRoomId;
    }

    public String getTrtcCallType() {
        return trtcCallType;
    }

    public void setTrtcCallType(String trtcCallType) {
        this.trtcCallType = trtcCallType;
    }
}
