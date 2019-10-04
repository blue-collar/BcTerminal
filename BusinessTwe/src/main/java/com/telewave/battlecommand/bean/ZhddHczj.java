/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.telewave.battlecommand.bean;

import java.util.Date;

/**
 * 火场总结Entity
 *
 * @author jiwl
 * @version 2019-07-20
 */
public class ZhddHczj {


    private String zquuid;        // 灾情UUID
    private Integer qzssrs;        // 群众受伤人数
    private Integer sxh;            // 水消耗
    private String ssrs;        // 疏散人数
    private String zjccss;        // 直接财产损失(万元)
    private Integer xhkhqsl;        // 消耗空呼器数量
    private String rscs;        // 燃烧场所
    private Date jsmlsj;        // 接收命令时间
    private Date ddxcsj;        // 到底现场时间
    private Date dccssj;        // 到场出水时间
    private Date phsj;            // 扑火时间
    private String jsmlsjStr;    // 接收命令时间 String类型
    private String ddxcsjStr;    // 到底现场时间	 String类型
    private String dccssjStr;    // 到场出水时间 String类型
    private String phsjStr;        // 扑火时间		 String类型
    private Integer qzswrs;        // 群众死亡人数
    private Integer jjrs;        // 解救人数
    private String pmxh;        // 泡沫消耗
    private String bhcc;        // 保护财产(万元)
    private String hzdyfl;        // 火灾地域分类
    private String jlzt;        // 删除标记(记录状态);做逻辑删除字段:(0--不可用,1--可用)
    private String cszt;        // 传输状态0：已传输 1：已更新 2：新添加
    //更新警情信息
    private String bkrs;        // 被困人数
    private String rsmj;        // 燃烧面积
    private String zqzt;        // 灾情状态


    public String getZquuid() {
        return zquuid;
    }

    public void setZquuid(String zquuid) {
        this.zquuid = zquuid;
    }

    public Integer getQzssrs() {
        return qzssrs;
    }

    public void setQzssrs(Integer qzssrs) {
        this.qzssrs = qzssrs;
    }

    public Integer getSxh() {
        return sxh;
    }

    public void setSxh(Integer sxh) {
        this.sxh = sxh;
    }

    public String getSsrs() {
        return ssrs;
    }

    public void setSsrs(String ssrs) {
        this.ssrs = ssrs;
    }

    public String getZjccss() {
        return zjccss;
    }

    public void setZjccss(String zjccss) {
        this.zjccss = zjccss;
    }

    public Integer getXhkhqsl() {
        return xhkhqsl;
    }

    public void setXhkhqsl(Integer xhkhqsl) {
        this.xhkhqsl = xhkhqsl;
    }

    public String getRscs() {
        return rscs;
    }

    public void setRscs(String rscs) {
        this.rscs = rscs;
    }

    public Date getJsmlsj() {
        return jsmlsj;
    }

    public void setJsmlsj(Date jsmlsj) {
        this.jsmlsj = jsmlsj;
    }

    public Date getDdxcsj() {
        return ddxcsj;
    }

    public void setDdxcsj(Date ddxcsj) {
        this.ddxcsj = ddxcsj;
    }

    public Date getDccssj() {
        return dccssj;
    }

    public void setDccssj(Date dccssj) {
        this.dccssj = dccssj;
    }

    public Date getPhsj() {
        return phsj;
    }

    public void setPhsj(Date phsj) {
        this.phsj = phsj;
    }

    public String getJsmlsjStr() {
        return jsmlsjStr;
    }

    public void setJsmlsjStr(String jsmlsjStr) {
        this.jsmlsjStr = jsmlsjStr;
    }

    public String getDdxcsjStr() {
        return ddxcsjStr;
    }

    public void setDdxcsjStr(String ddxcsjStr) {
        this.ddxcsjStr = ddxcsjStr;
    }

    public String getDccssjStr() {
        return dccssjStr;
    }

    public void setDccssjStr(String dccssjStr) {
        this.dccssjStr = dccssjStr;
    }

    public String getPhsjStr() {
        return phsjStr;
    }

    public void setPhsjStr(String phsjStr) {
        this.phsjStr = phsjStr;
    }

    public Integer getQzswrs() {
        return qzswrs;
    }

    public void setQzswrs(Integer qzswrs) {
        this.qzswrs = qzswrs;
    }

    public Integer getJjrs() {
        return jjrs;
    }

    public void setJjrs(Integer jjrs) {
        this.jjrs = jjrs;
    }

    public String getPmxh() {
        return pmxh;
    }

    public void setPmxh(String pmxh) {
        this.pmxh = pmxh;
    }

    public String getBhcc() {
        return bhcc;
    }

    public void setBhcc(String bhcc) {
        this.bhcc = bhcc;
    }

    public String getHzdyfl() {
        return hzdyfl;
    }

    public void setHzdyfl(String hzdyfl) {
        this.hzdyfl = hzdyfl;
    }

    public String getJlzt() {
        return jlzt;
    }

    public void setJlzt(String jlzt) {
        this.jlzt = jlzt;
    }

    public String getCszt() {
        return cszt;
    }

    public void setCszt(String cszt) {
        this.cszt = cszt;
    }

    public String getBkrs() {
        return bkrs;
    }

    public void setBkrs(String bkrs) {
        this.bkrs = bkrs;
    }

    public String getRsmj() {
        return rsmj;
    }

    public void setRsmj(String rsmj) {
        this.rsmj = rsmj;
    }

    public String getZqzt() {
        return zqzt;
    }

    public void setZqzt(String zqzt) {
        this.zqzt = zqzt;
    }

}