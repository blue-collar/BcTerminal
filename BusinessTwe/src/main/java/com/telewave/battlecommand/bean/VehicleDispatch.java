package com.telewave.battlecommand.bean;

public class VehicleDispatch {


    /**
     * id : 1
     * isNewRecord : false
     * createDate : 2019-07-19 11:19:35
     * updateDate : 2019-07-19 11:19:35
     * zdcdd : {"id":"af7e3c79d44e4cd7abfe9550ca421739","isNewRecord":false,"fssj":"2019-07-12 10:58:48"}
     * xfjgdm : {"id":"61a4eab7fc864513ac60b797fc539f3a","isNewRecord":false,"name":"新余市消防支队经济开发区大队经济开发区中队","sort":30,"type":"2","address":"","range":0,"ordernoInt":0,"jgqzInt":0,"parentId":"0"}
     * dpsl : 1
     * clmc : 抢险救援消防车
     * cphm : 赣KX5542应急
     * jlzt : 1
     * cszt : 1
     * vercol : 2019-07-19 14:24:07
     * isreportedFlag : 1
     */

    private String id;
    private boolean isNewRecord;
    private String createDate;
    private String updateDate;
    private ZdcddBean zdcdd;
    private XfjgdmBean xfjgdm;
    private String dpsl;
    private String clmc;
    private String cphm;
    private String jlzt;
    private String cszt;
    private String vercol;
    private String isreportedFlag;

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

    public ZdcddBean getZdcdd() {
        return zdcdd;
    }

    public void setZdcdd(ZdcddBean zdcdd) {
        this.zdcdd = zdcdd;
    }

    public XfjgdmBean getXfjgdm() {
        return xfjgdm;
    }

    public void setXfjgdm(XfjgdmBean xfjgdm) {
        this.xfjgdm = xfjgdm;
    }

    public String getDpsl() {
        return dpsl;
    }

    public void setDpsl(String dpsl) {
        this.dpsl = dpsl;
    }

    public String getClmc() {
        return clmc;
    }

    public void setClmc(String clmc) {
        this.clmc = clmc;
    }

    public String getCphm() {
        return cphm;
    }

    public void setCphm(String cphm) {
        this.cphm = cphm;
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

    public String getVercol() {
        return vercol;
    }

    public void setVercol(String vercol) {
        this.vercol = vercol;
    }

    public String getIsreportedFlag() {
        return isreportedFlag;
    }

    public void setIsreportedFlag(String isreportedFlag) {
        this.isreportedFlag = isreportedFlag;
    }

    public static class ZdcddBean {
        /**
         * id : af7e3c79d44e4cd7abfe9550ca421739
         * isNewRecord : false
         * fssj : 2019-07-12 10:58:48
         */

        private String id;
        private boolean isNewRecord;
        private String fssj;

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

        public String getFssj() {
            return fssj;
        }

        public void setFssj(String fssj) {
            this.fssj = fssj;
        }
    }

    public static class XfjgdmBean {
        /**
         * id : 61a4eab7fc864513ac60b797fc539f3a
         * isNewRecord : false
         * name : 新余市消防支队经济开发区大队经济开发区中队
         * sort : 30
         * type : 2
         * address :
         * range : 0.0
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
        private double range;
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

        public double getRange() {
            return range;
        }

        public void setRange(double range) {
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
