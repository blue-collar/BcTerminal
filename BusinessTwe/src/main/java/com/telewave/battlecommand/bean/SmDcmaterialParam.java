package com.telewave.battlecommand.bean;


/**
 * 危化品管理参数Entity
 *
 * @author lq
 * @version 2018-10-16
 */
public class SmDcmaterialParam {
    private String cname;        // 中文名称
    private String ename;        // 英文名称
    private String chemname;    // 分子式
    private String shape;        // 外观与性状
    private Integer pageNo = 1;    // 当前页码
    private Integer pageSize = 10; // 页面大小，设置为“-1”表示不进行分页（分页无效）

    public String getCname() {
        return cname;
    }

    public void setCname(String cname) {
        this.cname = cname;
    }

    public String getEname() {
        return ename;
    }

    public void setEname(String ename) {
        this.ename = ename;
    }

    public String getChemname() {
        return chemname;
    }

    public void setChemname(String chemname) {
        this.chemname = chemname;
    }

    public String getShape() {
        return shape;
    }

    public void setShape(String shape) {
        this.shape = shape;
    }

    public Integer getPageNo() {
        return pageNo;
    }

    public void setPageNo(Integer pageNo) {
        this.pageNo = pageNo;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }
}