package com.telewave.battlecommand.bean;

import java.io.Serializable;

public class FightPicInfo implements Serializable {


    /**
     * id : 048fe7f9cfe34e489121aa3954207105
     * isNewRecord : false
     * createDate : 2019-07-19 19:23:22
     * updateDate : 2019-07-19 19:23:22
     * mid : 8059d6a1a0ac4f4e9ad305b51a243a8e
     * tableName : zhdd_zqxx
     * type : 02
     * savepath : zqxx/20190719/1563535402134_c531f2fdaa4e42b7bfb4a30f55edaf06_PictureSelector_20190719_183813.png
     * filename : PictureSelector_20190719_183813.png
     * updatetime : 2019-07-19 19:23:22
     */

    private String id;
    private boolean isNewRecord;
    private String createDate;
    private String updateDate;
    private String mid;
    private String tableName;
    private String type;
    private String savepath;
    private String filename;
    private String updatetime;

    //新增图片保存本地地址和url
    private String filePhonePath;
    private String url;//根据IP端口和savepath组合的url地址
    //判断图片是否已上传，默认false未上传，已上传则为true
    private boolean isUploaded = false;

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

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(String updateDate) {
        this.updateDate = updateDate;
    }

    public String getMid() {
        return mid;
    }

    public void setMid(String mid) {
        this.mid = mid;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSavepath() {
        return savepath;
    }

    public void setSavepath(String savepath) {
        this.savepath = savepath;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(String updatetime) {
        this.updatetime = updatetime;
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

    public boolean isUploaded() {
        return isUploaded;
    }

    public void setUploaded(boolean uploaded) {
        isUploaded = uploaded;
    }
}
