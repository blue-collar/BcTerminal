package com.telewave.battlecommand.bean;

/**
 * @author liwh
 * @date 2019/1/11
 */
public class WeiZhanSearch {

    /**
     * id : c27f4433940e486f9779c381e7e08f7e
     * isNewRecord : false
     * remarks :
     * createDate : 2018-11-27 09:30:47
     * updateDate : 2018-12-21 17:16:13
     * xfzbh : 540104694949404
     * xfzmc : 大昭寺消防微站
     * xfzdz : 西藏自治区拉萨市城关区大昭路
     * xfzlb :
     * xfzxs :
     * xzqhdm : 540102
     * szdxfjg : {"id":"5de68c2bdf0544f880bbb741a8194c9b","isNewRecord":false,"name":"拉萨市公安消防支队大昭寺大队","sort":30,"type":"2","range":0,"ordernoInt":0,"jgqzInt":0,"parentId":"0"}
     * lxr : 少室山
     * lxrdh : 1598941964198
     * xfzrs : 0
     * xfzcs : 0
     * jd : 103.257
     * wd : 29.361
     * geom : POINT(103.257 29.361)
     */

    private String id;
    private boolean isNewRecord;
    private String remarks;
    private String createDate;
    private String updateDate;
    private String xfzbh;
    private String xfzmc;
    private String xfzdz;
    private String xfzlb;
    private String xfzxs;
    private String xzqhdm;
    private SzdxfjgBean szdxfjg;
    private String lxr;
    private String lxrdh;
    private int xfzrs;
    private int xfzcs;
    private double jd;
    private double wd;
    private String geom;

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

    public String getXfzbh() {
        return xfzbh;
    }

    public void setXfzbh(String xfzbh) {
        this.xfzbh = xfzbh;
    }

    public String getXfzmc() {
        return xfzmc;
    }

    public void setXfzmc(String xfzmc) {
        this.xfzmc = xfzmc;
    }

    public String getXfzdz() {
        return xfzdz;
    }

    public void setXfzdz(String xfzdz) {
        this.xfzdz = xfzdz;
    }

    public String getXfzlb() {
        return xfzlb;
    }

    public void setXfzlb(String xfzlb) {
        this.xfzlb = xfzlb;
    }

    public String getXfzxs() {
        return xfzxs;
    }

    public void setXfzxs(String xfzxs) {
        this.xfzxs = xfzxs;
    }

    public String getXzqhdm() {
        return xzqhdm;
    }

    public void setXzqhdm(String xzqhdm) {
        this.xzqhdm = xzqhdm;
    }

    public SzdxfjgBean getSzdxfjg() {
        return szdxfjg;
    }

    public void setSzdxfjg(SzdxfjgBean szdxfjg) {
        this.szdxfjg = szdxfjg;
    }

    public String getLxr() {
        return lxr;
    }

    public void setLxr(String lxr) {
        this.lxr = lxr;
    }

    public String getLxrdh() {
        return lxrdh;
    }

    public void setLxrdh(String lxrdh) {
        this.lxrdh = lxrdh;
    }

    public int getXfzrs() {
        return xfzrs;
    }

    public void setXfzrs(int xfzrs) {
        this.xfzrs = xfzrs;
    }

    public int getXfzcs() {
        return xfzcs;
    }

    public void setXfzcs(int xfzcs) {
        this.xfzcs = xfzcs;
    }

    public double getJd() {
        return jd;
    }

    public void setJd(double jd) {
        this.jd = jd;
    }

    public double getWd() {
        return wd;
    }

    public void setWd(double wd) {
        this.wd = wd;
    }

    public String getGeom() {
        return geom;
    }

    public void setGeom(String geom) {
        this.geom = geom;
    }

    public static class SzdxfjgBean {
        /**
         * id : 5de68c2bdf0544f880bbb741a8194c9b
         * isNewRecord : false
         * name : 拉萨市公安消防支队大昭寺大队
         * sort : 30
         * type : 2
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
