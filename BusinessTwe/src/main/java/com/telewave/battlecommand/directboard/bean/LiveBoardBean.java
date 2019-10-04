package com.telewave.battlecommand.directboard.bean;

import java.io.Serializable;

/**
 * 正在直播列表实体类
 *
 * @author liwh
 * @date 2019/8/8
 */
public class LiveBoardBean implements Serializable {

    /**
     * id : b0e817da3404491d96b481636bb4a0fe
     * isNewRecord : false
     * createDate : 2019-08-08 11:14:58
     * updateDate : 2019-08-08 11:15:57
     * roomNumber : 10008
     * isOpenLive : 1
     * liveUserid : d82f9f3a7bcb437f8556e13b59b6aa1d
     * liveUsername : 纪君
     * liveOfficeid : d6d40ebf20f74a96ab83147a90f8cc37
     * liveOfficename : 新余市消防支队
     */

    private String id;
    private boolean isNewRecord;
    private String createDate;
    private String updateDate;
    private String roomNumber;
    private String isOpenLive;
    private String liveUserid;
    private String liveUsername;
    private String liveOfficeid;
    private String liveOfficename;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isIsNewRecord() {
        return isNewRecord;
    }

    public void setIsNewRecord(boolean isNewRecord) {
        this.isNewRecord = isNewRecord;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(String updateDate) {
        this.updateDate = updateDate;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }

    public String getIsOpenLive() {
        return isOpenLive;
    }

    public void setIsOpenLive(String isOpenLive) {
        this.isOpenLive = isOpenLive;
    }

    public String getLiveUserid() {
        return liveUserid;
    }

    public void setLiveUserid(String liveUserid) {
        this.liveUserid = liveUserid;
    }

    public String getLiveUsername() {
        return liveUsername;
    }

    public void setLiveUsername(String liveUsername) {
        this.liveUsername = liveUsername;
    }

    public String getLiveOfficeid() {
        return liveOfficeid;
    }

    public void setLiveOfficeid(String liveOfficeid) {
        this.liveOfficeid = liveOfficeid;
    }

    public String getLiveOfficename() {
        return liveOfficename;
    }

    public void setLiveOfficename(String liveOfficename) {
        this.liveOfficename = liveOfficename;
    }
}
