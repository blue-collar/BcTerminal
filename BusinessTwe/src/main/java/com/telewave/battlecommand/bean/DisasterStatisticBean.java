package com.telewave.battlecommand.bean;

public class DisasterStatisticBean {


    /**
     * beginBjsj : 2019-08-02 00:00:00
     * endBjsj : 2019-08-07 00:00:00
     */

    private String beginBjsj;
    private String endBjsj;
    //新增一个机构ID
    private String officeId;

    public String getBeginBjsj() {
        return beginBjsj;
    }

    public void setBeginBjsj(String beginBjsj) {
        this.beginBjsj = beginBjsj;
    }

    public String getEndBjsj() {
        return endBjsj;
    }

    public void setEndBjsj(String endBjsj) {
        this.endBjsj = endBjsj;
    }

    public String getOfficeId() {
        return officeId;
    }

    public void setOfficeId(String officeId) {
        this.officeId = officeId;
    }
}
