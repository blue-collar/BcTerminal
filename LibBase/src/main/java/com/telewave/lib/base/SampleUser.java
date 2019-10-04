package com.telewave.lib.base;

import java.io.Serializable;

/**
 * 简单用户类
 * 用于语音、视频通话
 * 检索显示用户名
 */
public class SampleUser implements Serializable {
    public String userid;
    public String name;

    public SampleUser(String userid, String name) {
        this.userid = userid;
        this.name = name;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "SampleUser{" +
                "userid='" + userid + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
