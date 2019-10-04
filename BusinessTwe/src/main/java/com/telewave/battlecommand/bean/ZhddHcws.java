/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.telewave.battlecommand.bean;


public class ZhddHcws {

    private String zquuid;        // 灾情UUID
    private String fkr;        // 反馈人

    private String file;
    private String officeId;    //机构id
    private String fksj;        // 反馈时间
    private String fknr;        // 反馈内容
    private String recordtype;        // 记录类型,接警单、调派单、现场情况、火场文书、文电传输、火场总结、警情归并、增援请求、接受跨区域调度、车辆出动到场返回记录等

    public String getZquuid() {
        return zquuid;
    }

    public void setZquuid(String zquuid) {
        this.zquuid = zquuid;
    }

    public String getFkr() {
        return fkr;
    }

    public void setFkr(String fkr) {
        this.fkr = fkr;
    }

    public String getFksj() {
        return fksj;
    }

    public void setFksj(String fksj) {
        this.fksj = fksj;
    }

    public String getFknr() {
        return fknr;
    }

    public void setFknr(String fknr) {
        this.fknr = fknr;
    }

    public String getRecordtype() {
        return recordtype;
    }

    public void setRecordtype(String recordtype) {
        this.recordtype = recordtype;
    }

    public String getOfficeId() {
        return officeId;
    }

    public void setOfficeId(String officeId) {
        this.officeId = officeId;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }
}