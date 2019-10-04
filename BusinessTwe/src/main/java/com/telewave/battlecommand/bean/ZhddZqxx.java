package com.telewave.battlecommand.bean;

/**
 * @author liwh
 * @date 2018/12/24
 */
public class ZhddZqxx {
    //灾害地点，模糊匹配
    private String zhdd;
    //开始报警时间
    private String beginBjsj;
    //结束报警时间
    private String endBjsj;
    //灾情类型代码；
    private String zqlxdm;
    //灾害等级代码；
    private String zhdjdm;
    //被困人数情况，可选值（0、1、2、3）---0(无伤亡)、1(3人以内)、2(4-10人)、3(10人以上)
    private String bkqk;
    //是否只查询未结案，为否时查询已结案,为空时查询所有状态的警情
    private String isUnfinished;//(默认为空代表全部状态，1表示已结案，2表示未结案)
    private int pageNo;
    private int pageSize;

    private String officeId;    //指挥人员所属机构ID
    private String wxxfzId;        //微站的机构ID
    private String massesUserid;        //群组用户ID


    public ZhddZqxx() {
    }

    public String getZhdd() {
        return zhdd;
    }

    public void setZhdd(String zhdd) {
        this.zhdd = zhdd;
    }

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

    public String getZqlxdm() {
        return zqlxdm;
    }

    public void setZqlxdm(String zqlxdm) {
        this.zqlxdm = zqlxdm;
    }

    public String getZhdjdm() {
        return zhdjdm;
    }

    public void setZhdjdm(String zhdjdm) {
        this.zhdjdm = zhdjdm;
    }

    public String getBkqk() {
        return bkqk;
    }

    public void setBkqk(String bkqk) {
        this.bkqk = bkqk;
    }

    public String isUnfinished() {
        return isUnfinished;
    }

    public void setUnfinished(String unfinished) {
        isUnfinished = unfinished;
    }

    public int getPageNo() {
        return pageNo;
    }

    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public String getOfficeId() {
        return officeId;
    }

    public void setOfficeId(String officeId) {
        this.officeId = officeId;
    }

    public String getWxxfzId() {
        return wxxfzId;
    }

    public void setWxxfzId(String wxxfzId) {
        this.wxxfzId = wxxfzId;
    }

    public String getMassesUserid() {
        return massesUserid;
    }

    public void setMassesUserid(String massesUserid) {
        this.massesUserid = massesUserid;
    }
}
