package com.tencent.qcloud.tim.uikit.bean;

/**
 * @author PF-NAN
 * @date 2019-08-06
 */
public class IMUserExtObj {
    /*所属机构id*/
    public String organId;
    /*所属机构名称*/
    public String organName;

    public IMUserExtObj(String organId, String organName) {
        this.organId = organId;
        this.organName = organName;
    }
}
