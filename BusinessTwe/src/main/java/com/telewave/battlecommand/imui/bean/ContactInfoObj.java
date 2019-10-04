package com.telewave.battlecommand.imui.bean;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.text.TextUtils;

/**
 * 联系人信息
 *
 * @author PF-NAN
 * @date 2019-07-22
 */
public class ContactInfoObj implements Comparable<ContactInfoObj>, Parcelable {
    public String userid;
    public String acount;
    public String name;
    /*1 在线  0：不在线*/
    public String online;
    public String organId;
    public String organName;
    /*-----用户列表使用上面属性-----*/


    /*-----用户详情使用下面属性-----*/
    /*用户名*/
    public String username;
    /*登录账号*/
    public String loginname;
    /*组织id*/
    public String organid;
    /*组织名称*/
    public String organname;


    protected ContactInfoObj(Parcel in) {
        userid = in.readString();
        acount = in.readString();
        name = in.readString();
        online = in.readString();
        organId = in.readString();
        organName = in.readString();
        username = in.readString();
        loginname = in.readString();
        organid = in.readString();
        organname = in.readString();
    }

    public static final Creator<ContactInfoObj> CREATOR = new Creator<ContactInfoObj>() {
        @Override
        public ContactInfoObj createFromParcel(Parcel in) {
            return new ContactInfoObj(in);
        }

        @Override
        public ContactInfoObj[] newArray(int size) {
            return new ContactInfoObj[size];
        }
    };

    @Override
    public int compareTo(@NonNull ContactInfoObj contactInfoObj) {
        if (TextUtils.equals(this.online, contactInfoObj.online)) {
            return 0;
        } else if (TextUtils.equals(this.online, "1")) {
            return -1;

        } else {
            return 1;
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(userid);
        parcel.writeString(acount);
        parcel.writeString(name);
        parcel.writeString(online);
        parcel.writeString(organId);
        parcel.writeString(organName);
        parcel.writeString(username);
        parcel.writeString(loginname);
        parcel.writeString(organid);
        parcel.writeString(organname);
    }
}
