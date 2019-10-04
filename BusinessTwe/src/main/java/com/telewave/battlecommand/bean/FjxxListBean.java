package com.telewave.battlecommand.bean;

/**
 * 附件信息bean
 */
public class FjxxListBean {

    /**
     * id : 975570a65a4b4d84adc10b2d7a6455c9
     * isNewRecord : false
     * mhdwid : 00d554424aef42569afa3c1f58d665c1
     * fjmc : gkt_1.jpg
     * fjdz : /twmfs/20190812/2A3wGqlLYvwH5wUq/1.jpg
     * fjlx : 04
     * czyid : 1
     * jgid : d6d40ebf20f74a96ab83147a90f8cc37
     * cszt : 0
     * jlzt : 1
     * sjc : 2019-08-12 22:04:14
     * sjbb : 1
     * cjsj : 2019-08-12 22:04:15
     */

    private String id;
    private boolean isNewRecord;
    private String mhdwid;
    private String fjmc;
    private String fjdz;
    private String fjlx;
    private String czyid;
    private String jgid;
    private String cszt;
    private String jlzt;
    private String sjc;
    private String sjbb;
    private String cjsj;

    //新增图片保存本地地址和url
    private String filePhonePath;
    private String url;//根据IP端口和savepath组合的url地址

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isIsNewRecord() {
        return isNewRecord;
    }

    public void setIsNewRecord(boolean isNewRecord) {
        this.isNewRecord = isNewRecord;
    }

    public String getMhdwid() {
        return mhdwid;
    }

    public void setMhdwid(String mhdwid) {
        this.mhdwid = mhdwid;
    }

    public String getFjmc() {
        return fjmc;
    }

    public void setFjmc(String fjmc) {
        this.fjmc = fjmc;
    }

    public String getFjdz() {
        return fjdz;
    }

    public void setFjdz(String fjdz) {
        this.fjdz = fjdz;
    }

    public String getFjlx() {
        return fjlx;
    }

    public void setFjlx(String fjlx) {
        this.fjlx = fjlx;
    }

    public String getCzyid() {
        return czyid;
    }

    public void setCzyid(String czyid) {
        this.czyid = czyid;
    }

    public String getJgid() {
        return jgid;
    }

    public void setJgid(String jgid) {
        this.jgid = jgid;
    }

    public String getCszt() {
        return cszt;
    }

    public void setCszt(String cszt) {
        this.cszt = cszt;
    }

    public String getJlzt() {
        return jlzt;
    }

    public void setJlzt(String jlzt) {
        this.jlzt = jlzt;
    }

    public String getSjc() {
        return sjc;
    }

    public void setSjc(String sjc) {
        this.sjc = sjc;
    }

    public String getSjbb() {
        return sjbb;
    }

    public void setSjbb(String sjbb) {
        this.sjbb = sjbb;
    }

    public String getCjsj() {
        return cjsj;
    }

    public void setCjsj(String cjsj) {
        this.cjsj = cjsj;
    }

    public String getFilePhonePath() {
        return filePhonePath;
    }

    public void setFilePhonePath(String filePhonePath) {
        this.filePhonePath = filePhonePath;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
