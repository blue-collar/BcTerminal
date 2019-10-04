package com.telewave.battlecommand.bean;

import java.util.List;

public class NewFireDocument {


    /**
     * id : d549922d13304d1b898f490c63c9b97e
     * isNewRecord : false
     * zquuid : 8059d6a1a0ac4f4e9ad305b51a243a8e
     * fkr : a72f83ec99124d388afd399901142a6d
     * office : {"id":"241371a3d5874132b3dabd7925e0f9e1","isNewRecord":false,"name":"新余市消防支队渝水区大队平安路中队","sort":30,"type":"2","address":"","range":0,"ordernoInt":0,"jgqzInt":0,"parentId":"0"}
     * fksj : 2019-07-20 12:42:43
     * fknr : 入场灭火
     * jlzt : 1
     * cszt : 2
     * sjc : 2019-07-20 12:46:21
     * xxlx : 系统记录
     * vercol : 2019-07-20 12:46:20
     * recordtype : 增援请求
     * isreportedFlag : 0
     * file : [{"id":"65e84d72dbed485f923c8f09b5b0dca8","isNewRecord":false,"createDate":"2019-07-20 12:46:23","updateDate":"2019-07-20 12:46:23","mid":"d549922d13304d1b898f490c63c9b97e","tableName":"zhdd_hcws","type":"02","savepath":"hcws/20190720/1563597983303_4c7c840dae4443d2a5dfe2e8e244d429_Screenshot_20190121-170613.jpg","filename":"Screenshot_20190121-170613.jpg","updatetime":"2019-07-20 12:46:23"},{"id":"6d81c268d0d34504a082db14739b68dc","isNewRecord":false,"createDate":"2019-07-20 12:46:24","updateDate":"2019-07-20 12:46:24","mid":"d549922d13304d1b898f490c63c9b97e","tableName":"zhdd_hcws","type":"02","savepath":"hcws/20190720/1563597983303_1446a4537fcc442d9c9a516f8b382d21_Screenshot_20181207-180827.jpg","filename":"Screenshot_20181207-180827.jpg","updatetime":"2019-07-20 12:46:24"}]
     * fksjStr : 2019-07-20 12:42:43
     */

    private String id;
    private boolean isNewRecord;
    private String zquuid;
    private String fkr;
    private OfficeBean office;
    private String fksj;
    private String fknr;
    private int jlzt;
    private int cszt;
    private String sjc;
    private String xxlx;
    private String vercol;
    private String recordtype;
    private String isreportedFlag;
    private String fksjStr;
    private List<FileBean> file;

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

    public OfficeBean getOffice() {
        return office;
    }

    public void setOffice(OfficeBean office) {
        this.office = office;
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

    public int getJlzt() {
        return jlzt;
    }

    public void setJlzt(int jlzt) {
        this.jlzt = jlzt;
    }

    public int getCszt() {
        return cszt;
    }

    public void setCszt(int cszt) {
        this.cszt = cszt;
    }

    public String getSjc() {
        return sjc;
    }

    public void setSjc(String sjc) {
        this.sjc = sjc;
    }

    public String getXxlx() {
        return xxlx;
    }

    public void setXxlx(String xxlx) {
        this.xxlx = xxlx;
    }

    public String getVercol() {
        return vercol;
    }

    public void setVercol(String vercol) {
        this.vercol = vercol;
    }

    public String getRecordtype() {
        return recordtype;
    }

    public void setRecordtype(String recordtype) {
        this.recordtype = recordtype;
    }

    public String getIsreportedFlag() {
        return isreportedFlag;
    }

    public void setIsreportedFlag(String isreportedFlag) {
        this.isreportedFlag = isreportedFlag;
    }

    public String getFksjStr() {
        return fksjStr;
    }

    public void setFksjStr(String fksjStr) {
        this.fksjStr = fksjStr;
    }

    public List<FileBean> getFile() {
        return file;
    }

    public void setFile(List<FileBean> file) {
        this.file = file;
    }

    public static class OfficeBean {
        /**
         * id : 241371a3d5874132b3dabd7925e0f9e1
         * isNewRecord : false
         * name : 新余市消防支队渝水区大队平安路中队
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

    public static class FileBean {
        /**
         * id : 65e84d72dbed485f923c8f09b5b0dca8
         * isNewRecord : false
         * createDate : 2019-07-20 12:46:23
         * updateDate : 2019-07-20 12:46:23
         * mid : d549922d13304d1b898f490c63c9b97e
         * tableName : zhdd_hcws
         * type : 02
         * savepath : hcws/20190720/1563597983303_4c7c840dae4443d2a5dfe2e8e244d429_Screenshot_20190121-170613.jpg
         * filename : Screenshot_20190121-170613.jpg
         * updatetime : 2019-07-20 12:46:23
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
    }
}
