package com.telewave.battlecommand.imui.bean.other;

import android.support.annotation.IntRange;
import android.text.TextUtils;

import com.telewave.battlecommand.imui.bean.ContactInfoObj;

import java.util.Collections;
import java.util.List;

/**
 * 联系人外层实体
 *
 * @author PF-NAN
 * @date 2019-08-05
 */
public class ContactOutObj {
    /*昵称*/
    public String name;
    /*联系人信息集合*/
    public List<ContactInfoObj> contactInfoObjs;

    public int onlineNumber;
    public int totalNumber;
    /*用户类型：0：在线人员  1：本单位人员*/
    @IntRange(from = 0, to = 1)
    public int userType;

    /**
     * 构造处理数据
     *
     * @param name
     * @param contactInfoObjs
     */
    public ContactOutObj(String name, List<ContactInfoObj> contactInfoObjs) {
        this.name = name;
        this.contactInfoObjs = contactInfoObjs;
        if (null != this.contactInfoObjs && !this.contactInfoObjs.isEmpty()) {
            Collections.sort(this.contactInfoObjs);

            onlineNumber = 0;
            for (ContactInfoObj orgContact : this.contactInfoObjs) {
                if (TextUtils.equals(orgContact.online, "1")) {
                    onlineNumber += 1;
                }
            }
            totalNumber = this.contactInfoObjs.size();
        } else {
            onlineNumber = 0;
            totalNumber = 0;
        }

    }
}
