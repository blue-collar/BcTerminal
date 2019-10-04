package com.telewave.battlecommand.bean;

import java.io.Serializable;

public class NineSmallPlace implements Serializable {


    /**
     * id : bf9fd0dde71241538a794557c44d52a4
     * isNewRecord : false
     * remarks : 4
     * createDate : 2019-07-24 16:23:51
     * updateDate : 2019-07-25 17:17:43
     * placeName : 新余小雪
     * address : 西域社区
     * unitType : 01
     * organ : {"id":"a31bb79161d64942825789b0d9813622","isNewRecord":false,"name":"分宜县消防大队分宜县中队","sort":30,"type":"2","address":"","range":0,"ordernoInt":0,"jgqzInt":0,"parentId":"0"}
     * geom : POINT(113.28002931 26.96124577)
     * latitudeX : 26.96124577
     * decimalY : 113.28002931
     * unitFax : 11
     * serviceScope : 33
     * dutyPhone : 22
     */

    private String id;
    private boolean isNewRecord;
    private String remarks;
    private String createDate;
    private String updateDate;
    private String placeName;
    private String address;
    private String unitType;
    private OrganBean organ;
    private String geom;
    private String latitudeX;
    private String decimalY;
    private String unitFax;
    private String serviceScope;
    private String dutyPhone;

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

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
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

    public String getPlaceName() {
        return placeName;
    }

    public void setPlaceName(String placeName) {
        this.placeName = placeName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getUnitType() {
        return unitType;
    }

    public void setUnitType(String unitType) {
        this.unitType = unitType;
    }

    public OrganBean getOrgan() {
        return organ;
    }

    public void setOrgan(OrganBean organ) {
        this.organ = organ;
    }

    public String getGeom() {
        return geom;
    }

    public void setGeom(String geom) {
        this.geom = geom;
    }

    public String getLatitudeX() {
        return latitudeX;
    }

    public void setLatitudeX(String latitudeX) {
        this.latitudeX = latitudeX;
    }

    public String getDecimalY() {
        return decimalY;
    }

    public void setDecimalY(String decimalY) {
        this.decimalY = decimalY;
    }

    public String getUnitFax() {
        return unitFax;
    }

    public void setUnitFax(String unitFax) {
        this.unitFax = unitFax;
    }

    public String getServiceScope() {
        return serviceScope;
    }

    public void setServiceScope(String serviceScope) {
        this.serviceScope = serviceScope;
    }

    public String getDutyPhone() {
        return dutyPhone;
    }

    public void setDutyPhone(String dutyPhone) {
        this.dutyPhone = dutyPhone;
    }

    public static class OrganBean implements Serializable {
        /**
         * id : a31bb79161d64942825789b0d9813622
         * isNewRecord : false
         * name : 分宜县消防大队分宜县中队
         * sort : 30
         * type : 2
         * address :
         * range : 0
         * ordernoInt : 0
         * jgqzInt : 0
         * parentId : 0
         */

        private String id;
        private boolean isNewRecord;
        private String name;
        private int sort;
        private String type;
        private String address;
        private int range;
        private int ordernoInt;
        private int jgqzInt;
        private String parentId;

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

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getSort() {
            return sort;
        }

        public void setSort(int sort) {
            this.sort = sort;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public int getRange() {
            return range;
        }

        public void setRange(int range) {
            this.range = range;
        }

        public int getOrdernoInt() {
            return ordernoInt;
        }

        public void setOrdernoInt(int ordernoInt) {
            this.ordernoInt = ordernoInt;
        }

        public int getJgqzInt() {
            return jgqzInt;
        }

        public void setJgqzInt(int jgqzInt) {
            this.jgqzInt = jgqzInt;
        }

        public String getParentId() {
            return parentId;
        }

        public void setParentId(String parentId) {
            this.parentId = parentId;
        }
    }
}
