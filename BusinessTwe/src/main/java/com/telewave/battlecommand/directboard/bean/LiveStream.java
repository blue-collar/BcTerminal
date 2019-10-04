package com.telewave.battlecommand.directboard.bean;

/**
 * 直播信息Entity
 *
 * @author liwh
 * @version 2019-08-08
 */
public class LiveStream {

    // 直播房间号
    private String roomNumber;
    // 是否开播（0未开播，1正在直播）
    private String isOpenLive;
    // 房间号主题
    private String roomTitle;
    // 直播类型
    private String liveType;
    // 直播用户id
    private String liveUserid;
    // 直播用户名字
    private String liveUsername;
    // 机构id
    private String liveOfficeid;
    // 机构名称
    private String liveOfficename;

    public LiveStream() {
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

    public String getRoomTitle() {
        return roomTitle;
    }

    public void setRoomTitle(String roomTitle) {
        this.roomTitle = roomTitle;
    }

    public String getLiveType() {
        return liveType;
    }

    public void setLiveType(String liveType) {
        this.liveType = liveType;
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