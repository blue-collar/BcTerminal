package com.telewave.battlecommand.bean;

/**
 * Created by LiJing on 2017/3/29.
 */

public class LoginInfo {
    private String rzzt;
    private String username;
    private String loginname;
    private String userid;
    private String lxfs;
    private double mapx;
    private double mapy;
    private String name;
    private String wzid;
    private String organid;
    private String organname;
    private String isLeader;
    private String postduty;
    private String address;
    private String userSig;

    public LoginInfo() {
    }

    public String getOrganName() {
        return organname;
    }

    public void setOrganName(String organName) {
        this.organname = organName;
    }

    public String getRzzt() {
        return rzzt;
    }

    public void setRzzt(String rzzt) {
        this.rzzt = rzzt;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getLoginname() {
        return loginname;
    }

    public void setLoginname(String loginname) {
        this.loginname = loginname;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getLxfs() {
        return lxfs;
    }

    public void setLxfs(String lxfs) {
        this.lxfs = lxfs;
    }

    public double getMapx() {
        return mapx;
    }

    public void setMapx(double mapx) {
        this.mapx = mapx;
    }

    public double getMapy() {
        return mapy;
    }

    public void setMapy(double mapy) {
        this.mapy = mapy;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getWzid() {
        return wzid;
    }

    public void setWzid(String wzid) {
        this.wzid = wzid;
    }

    public String getOrganid() {
        return organid;
    }

    public void setOrganid(String organid) {
        this.organid = organid;
    }

    public String getIsLeader() {
        return isLeader;
    }

    public void setIsLeader(String isLeader) {
        this.isLeader = isLeader;
    }

    public String getPostduty() {
        return postduty;
    }

    public void setPostduty(String postduty) {
        this.postduty = postduty;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getUserSig() {
        return userSig;
    }

    public void setUserSig(String userSig) {
        this.userSig = userSig;
    }

    @Override
    public String toString() {
        return "LoginInfo{" +
                "rzzt='" + rzzt + '\'' +
                ", username='" + username + '\'' +
                ", loginname='" + loginname + '\'' +
                ", userid='" + userid + '\'' +
                ", lxfs='" + lxfs + '\'' +
                ", mapx=" + mapx +
                ", mapy=" + mapy +
                ", name='" + name + '\'' +
                ", wzid='" + wzid + '\'' +
                ", organid='" + organid + '\'' +
                ", isLeader='" + isLeader + '\'' +
                ", postduty='" + postduty + '\'' +
                ", userSig='" + userSig + '\'' +
                ", address='" + address + '\'' +
                '}';
    }
}
