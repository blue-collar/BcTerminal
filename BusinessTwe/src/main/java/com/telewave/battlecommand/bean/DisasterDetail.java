package com.telewave.battlecommand.bean;

import java.io.Serializable;
import java.util.List;

public class DisasterDetail implements Serializable {


    /**
     * id : f91b43f99a794735ae660b851a26d368
     * isNewRecord : false
     * createDate : 2019-07-09 20:34:17
     * updateDate : 2019-07-09 20:34:41
     * ysid : 3600600020190709203534942001
     * zqbm : 3600600020190709203534942001
     * bjsj : 2019-07-22 20:33:59
     * bjdh : 18905694114
     * bjr : 刘浩
     * bjrId : ""
     * jzxm : 刘浩
     * bjfs : 微站图文报警
     * zjdz : 江西省新余市渝水区登丰路
     * xzqydm : 371002
     * xqzdjgdm : {"id":"d6d40ebf20f74a96ab83147a90f8cc37","isNewRecord":false,"name":"新余市消防支队","sort":30,"type":"2","address":"","range":0,"ordernoInt":0,"jgqzInt":0,"parentId":"0"}
     * lxdh : 18905694114
     * lxr : 刘浩
     * zqlx : {"isNewRecord":true,"dictId":"","codeValue":"10000","codeName":"火灾扑救","dictsourceType":0}
     * czdx : {"isNewRecord":true,"dictId":"","codeValue":"","codeName":"","dictsourceType":0}
     * zhdj : {"isNewRecord":true,"dictId":"","codeValue":"1","codeName":"一级","dictsourceType":0}
     * zhdd : 江西省新余市渝水区登丰路
     * zqms : 灾情描述一写信息
     * zqzt : {"isNewRecord":true,"dictId":"","codeValue":"01","codeName":"接警","dictsourceType":0}
     * ifsb : 1
     * tssj : 2019-07-09 20:34:41
     * bsdwdm : d6d40ebf20f74a96ab83147a90f8cc37
     * zqlydwdm : d6d40ebf20f74a96ab83147a90f8cc37
     * zddwid :
     * zddwmc : 融城大饭店微型消防站
     * bkrs : 0
     * swrs : 0
     * ssrs : 0
     * rsmj : 0
     * jzlxdm :
     * qhlc : 0
     * lasj : 2019-07-09 20:33:59
     * dcsj : 2019-07-21 15:19:52
     * zqtsbs : 0
     * zqpd : {"isNewRecord":true,"dictId":"","codeValue":"1","codeName":"真警","dictsourceType":0}
     * czr :
     * gisX : 114.94990467
     * gisY : 27.78658259
     * gisH : 0.00000000
     * cszt : 1
     * sjc : 2019-07-09 20:34:41
     * bz :
     * cjsj : 2019-07-09 20:34:41
     * vercol : 2019-07-22 15:19:55
     * geom : POINT(114.94990467 27.78658259)
     * oragn : {"id":"d6d40ebf20f74a96ab83147a90f8cc37","isNewRecord":false,"name":"新余市消防支队","sort":30,"type":"2","address":"","range":0,"ordernoInt":0,"jgqzInt":0,"parentId":"0"}
     * extend : {"isNewRecord":true,"createDate":"2019-07-09 20:34:19","updateDate":"2019-07-09 20:34:19","alarmid":"f91b43f99a794735ae660b851a26d368","alarmsource":"","refid":"3600600020190709203534942001","isGravenessCase":"1","incepttype":{"isNewRecord":true,"dictId":"","codeValue":"1","codeName":"真警","dictsourceType":0},"substation":{"id":"d6d40ebf20f74a96ab83147a90f8cc37","isNewRecord":false,"name":"新余市消防支队","sort":30,"type":"2","address":"","range":0,"ordernoInt":0,"jgqzInt":0,"parentId":"0"},"smogstatus":{"isNewRecord":true,"dictId":"","codeValue":"1","codeName":"有烟","dictsourceType":0},"firematterclass":{"isNewRecord":true,"dictId":"","codeValue":"200","codeName":"家俱、设备及竹、木等制品","dictsourceType":0},"selectcarflag":"0","tagsendmsg":"0","tagreqreinforce":"0","tagreinforce":"0","tagsendwd":"0","tagrecwd":"0","acceptuserid":"5cc33a12ffffff8a2628f5ec4d0d5d0f","acceptseat":"2001","procuserid":"5cc33a12ffffff8a2628f5ec4d0d5d0f","procseat":"2001","arcflag":"0","sjc":"2019-07-09 20:34:19","bjdz":"江西省新余市渝水区登丰路","zhcs":{"isNewRecord":true,"dictId":"","codeValue":"","codeName":"","dictsourceType":0},"zqxl":{"isNewRecord":true,"dictId":"","codeValue":"","codeName":"","dictsourceType":0}}
     * "files": [{"id": "697301967f964e05b85346dd979e0b4d","isNewRecord": false,"createDate":"updateDate": "2019-07-31 19:34:37","mid": "67dfcaf8be124690a5453e30d3a69967","tableName": "zhdd_zqxx","type": "01","savepath": "/callPolicePic/20190731/q4dsfYy73rgrej4q/PictureSelector_20190731_193242.mp4","filename": "PictureSelector_20190731_193242.mp4","updatetime": "2019-07-31 19:34:37"}
     */

    private String id;
    private boolean isNewRecord;
    private String createDate;
    private String updateDate;
    private String ysid;
    private String zqbm;
    private String bjsj;
    private String bjdh;
    private String bjr;
    private String bjrId;
    private String jzxm;
    private String bjfs;
    private String zjdz;
    private String xzqydm;
    private XqzdjgdmBean xqzdjgdm;
    private String lxdh;
    private String lxr;
    private ZqlxBean zqlx;
    private CzdxBean czdx;
    private ZhdjBean zhdj;
    private String zhdd;
    private String zqms;
    private ZqztBean zqzt;
    private String ifsb;
    private String tssj;
    private String bsdwdm;
    private String zqlydwdm;
    private String zddwid;
    private String zddwmc;
    private String bkrs;
    private String swrs;
    private String ssrs;
    private String rsmj;
    private String jzlxdm;
    private String qhlc;
    private String lasj;
    private String dcsj;
    private String zqtsbs;
    private ZqpdBean zqpd;
    private String czr;
    private String gisX;
    private String gisY;
    private String gisH;
    private String cszt;
    private String sjc;
    private String bz;
    private String cjsj;
    private String vercol;
    private String geom;
    private OragnBean oragn;
    private ExtendBean extend;
    private List<FightPicInfo> files;


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

    public String getYsid() {
        return ysid;
    }

    public void setYsid(String ysid) {
        this.ysid = ysid;
    }

    public String getZqbm() {
        return zqbm;
    }

    public void setZqbm(String zqbm) {
        this.zqbm = zqbm;
    }

    public String getBjsj() {
        return bjsj;
    }

    public void setBjsj(String bjsj) {
        this.bjsj = bjsj;
    }

    public String getBjdh() {
        return bjdh;
    }

    public void setBjdh(String bjdh) {
        this.bjdh = bjdh;
    }

    public String getBjr() {
        return bjr;
    }

    public void setBjr(String bjr) {
        this.bjr = bjr;
    }

    public String getBjrId() {
        return bjrId;
    }

    public void setBjrId(String bjrId) {
        this.bjrId = bjrId;
    }

    public String getJzxm() {
        return jzxm;
    }

    public void setJzxm(String jzxm) {
        this.jzxm = jzxm;
    }

    public String getBjfs() {
        return bjfs;
    }

    public void setBjfs(String bjfs) {
        this.bjfs = bjfs;
    }

    public String getZjdz() {
        return zjdz;
    }

    public void setZjdz(String zjdz) {
        this.zjdz = zjdz;
    }

    public String getXzqydm() {
        return xzqydm;
    }

    public void setXzqydm(String xzqydm) {
        this.xzqydm = xzqydm;
    }

    public XqzdjgdmBean getXqzdjgdm() {
        return xqzdjgdm;
    }

    public void setXqzdjgdm(XqzdjgdmBean xqzdjgdm) {
        this.xqzdjgdm = xqzdjgdm;
    }

    public String getLxdh() {
        return lxdh;
    }

    public void setLxdh(String lxdh) {
        this.lxdh = lxdh;
    }

    public String getLxr() {
        return lxr;
    }

    public void setLxr(String lxr) {
        this.lxr = lxr;
    }

    public ZqlxBean getZqlx() {
        return zqlx;
    }

    public void setZqlx(ZqlxBean zqlx) {
        this.zqlx = zqlx;
    }

    public CzdxBean getCzdx() {
        return czdx;
    }

    public void setCzdx(CzdxBean czdx) {
        this.czdx = czdx;
    }

    public ZhdjBean getZhdj() {
        return zhdj;
    }

    public void setZhdj(ZhdjBean zhdj) {
        this.zhdj = zhdj;
    }

    public String getZhdd() {
        return zhdd;
    }

    public void setZhdd(String zhdd) {
        this.zhdd = zhdd;
    }

    public String getZqms() {
        return zqms;
    }

    public void setZqms(String zqms) {
        this.zqms = zqms;
    }

    public ZqztBean getZqzt() {
        return zqzt;
    }

    public void setZqzt(ZqztBean zqzt) {
        this.zqzt = zqzt;
    }

    public String getIfsb() {
        return ifsb;
    }

    public void setIfsb(String ifsb) {
        this.ifsb = ifsb;
    }

    public String getTssj() {
        return tssj;
    }

    public void setTssj(String tssj) {
        this.tssj = tssj;
    }

    public String getBsdwdm() {
        return bsdwdm;
    }

    public void setBsdwdm(String bsdwdm) {
        this.bsdwdm = bsdwdm;
    }

    public String getZqlydwdm() {
        return zqlydwdm;
    }

    public void setZqlydwdm(String zqlydwdm) {
        this.zqlydwdm = zqlydwdm;
    }

    public String getZddwid() {
        return zddwid;
    }

    public void setZddwid(String zddwid) {
        this.zddwid = zddwid;
    }

    public String getZddwmc() {
        return zddwmc;
    }

    public void setZddwmc(String zddwmc) {
        this.zddwmc = zddwmc;
    }

    public String getBkrs() {
        return bkrs;
    }

    public void setBkrs(String bkrs) {
        this.bkrs = bkrs;
    }

    public String getSwrs() {
        return swrs;
    }

    public void setSwrs(String swrs) {
        this.swrs = swrs;
    }

    public String getSsrs() {
        return ssrs;
    }

    public void setSsrs(String ssrs) {
        this.ssrs = ssrs;
    }

    public String getRsmj() {
        return rsmj;
    }

    public void setRsmj(String rsmj) {
        this.rsmj = rsmj;
    }

    public String getJzlxdm() {
        return jzlxdm;
    }

    public void setJzlxdm(String jzlxdm) {
        this.jzlxdm = jzlxdm;
    }

    public String getQhlc() {
        return qhlc;
    }

    public void setQhlc(String qhlc) {
        this.qhlc = qhlc;
    }

    public String getLasj() {
        return lasj;
    }

    public void setLasj(String lasj) {
        this.lasj = lasj;
    }

    public String getDcsj() {
        return dcsj;
    }

    public void setDcsj(String dcsj) {
        this.dcsj = dcsj;
    }

    public String getZqtsbs() {
        return zqtsbs;
    }

    public void setZqtsbs(String zqtsbs) {
        this.zqtsbs = zqtsbs;
    }

    public ZqpdBean getZqpd() {
        return zqpd;
    }

    public void setZqpd(ZqpdBean zqpd) {
        this.zqpd = zqpd;
    }

    public String getCzr() {
        return czr;
    }

    public void setCzr(String czr) {
        this.czr = czr;
    }

    public String getGisX() {
        return gisX;
    }

    public void setGisX(String gisX) {
        this.gisX = gisX;
    }

    public String getGisY() {
        return gisY;
    }

    public void setGisY(String gisY) {
        this.gisY = gisY;
    }

    public String getGisH() {
        return gisH;
    }

    public void setGisH(String gisH) {
        this.gisH = gisH;
    }

    public String getCszt() {
        return cszt;
    }

    public void setCszt(String cszt) {
        this.cszt = cszt;
    }

    public String getSjc() {
        return sjc;
    }

    public void setSjc(String sjc) {
        this.sjc = sjc;
    }

    public String getBz() {
        return bz;
    }

    public void setBz(String bz) {
        this.bz = bz;
    }

    public String getCjsj() {
        return cjsj;
    }

    public void setCjsj(String cjsj) {
        this.cjsj = cjsj;
    }

    public String getVercol() {
        return vercol;
    }

    public void setVercol(String vercol) {
        this.vercol = vercol;
    }

    public String getGeom() {
        return geom;
    }

    public void setGeom(String geom) {
        this.geom = geom;
    }

    public OragnBean getOragn() {
        return oragn;
    }

    public void setOragn(OragnBean oragn) {
        this.oragn = oragn;
    }

    public ExtendBean getExtend() {
        return extend;
    }

    public void setExtend(ExtendBean extend) {
        this.extend = extend;
    }

    public List<FightPicInfo> getFiles() {
        return files;
    }

    public void setFiles(List<FightPicInfo> files) {
        this.files = files;
    }

    public static class XqzdjgdmBean implements Serializable {
        /**
         * id : d6d40ebf20f74a96ab83147a90f8cc37
         * isNewRecord : false
         * name : 新余市消防支队
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

    public static class ZqlxBean implements Serializable {
        /**
         * isNewRecord : true
         * dictId :
         * codeValue : 10000
         * codeName : 火灾扑救
         * dictsourceType : 0
         */

        private boolean isNewRecord;
        private String dictId;
        private String codeValue;
        private String codeName;
        private int dictsourceType;

        public boolean isIsNewRecord() {
            return isNewRecord;
        }

        public void setIsNewRecord(boolean isNewRecord) {
            this.isNewRecord = isNewRecord;
        }

        public String getDictId() {
            return dictId;
        }

        public void setDictId(String dictId) {
            this.dictId = dictId;
        }

        public String getCodeValue() {
            return codeValue;
        }

        public void setCodeValue(String codeValue) {
            this.codeValue = codeValue;
        }

        public String getCodeName() {
            return codeName;
        }

        public void setCodeName(String codeName) {
            this.codeName = codeName;
        }

        public int getDictsourceType() {
            return dictsourceType;
        }

        public void setDictsourceType(int dictsourceType) {
            this.dictsourceType = dictsourceType;
        }
    }

    public static class CzdxBean implements Serializable {
        /**
         * isNewRecord : true
         * dictId :
         * codeValue :
         * codeName :
         * dictsourceType : 0
         */

        private boolean isNewRecord;
        private String dictId;
        private String codeValue;
        private String codeName;
        private int dictsourceType;

        public boolean isIsNewRecord() {
            return isNewRecord;
        }

        public void setIsNewRecord(boolean isNewRecord) {
            this.isNewRecord = isNewRecord;
        }

        public String getDictId() {
            return dictId;
        }

        public void setDictId(String dictId) {
            this.dictId = dictId;
        }

        public String getCodeValue() {
            return codeValue;
        }

        public void setCodeValue(String codeValue) {
            this.codeValue = codeValue;
        }

        public String getCodeName() {
            return codeName;
        }

        public void setCodeName(String codeName) {
            this.codeName = codeName;
        }

        public int getDictsourceType() {
            return dictsourceType;
        }

        public void setDictsourceType(int dictsourceType) {
            this.dictsourceType = dictsourceType;
        }
    }

    public static class ZhdjBean implements Serializable {
        /**
         * isNewRecord : true
         * dictId :
         * codeValue : 1
         * codeName : 一级
         * dictsourceType : 0
         */

        private boolean isNewRecord;
        private String dictId;
        private String codeValue;
        private String codeName;
        private int dictsourceType;

        public boolean isIsNewRecord() {
            return isNewRecord;
        }

        public void setIsNewRecord(boolean isNewRecord) {
            this.isNewRecord = isNewRecord;
        }

        public String getDictId() {
            return dictId;
        }

        public void setDictId(String dictId) {
            this.dictId = dictId;
        }

        public String getCodeValue() {
            return codeValue;
        }

        public void setCodeValue(String codeValue) {
            this.codeValue = codeValue;
        }

        public String getCodeName() {
            return codeName;
        }

        public void setCodeName(String codeName) {
            this.codeName = codeName;
        }

        public int getDictsourceType() {
            return dictsourceType;
        }

        public void setDictsourceType(int dictsourceType) {
            this.dictsourceType = dictsourceType;
        }
    }

    public static class ZqztBean implements Serializable {
        /**
         * isNewRecord : true
         * dictId :
         * codeValue : 01
         * codeName : 接警
         * dictsourceType : 0
         */

        private boolean isNewRecord;
        private String dictId;
        private String codeValue;
        private String codeName;
        private int dictsourceType;

        public boolean isIsNewRecord() {
            return isNewRecord;
        }

        public void setIsNewRecord(boolean isNewRecord) {
            this.isNewRecord = isNewRecord;
        }

        public String getDictId() {
            return dictId;
        }

        public void setDictId(String dictId) {
            this.dictId = dictId;
        }

        public String getCodeValue() {
            return codeValue;
        }

        public void setCodeValue(String codeValue) {
            this.codeValue = codeValue;
        }

        public String getCodeName() {
            return codeName;
        }

        public void setCodeName(String codeName) {
            this.codeName = codeName;
        }

        public int getDictsourceType() {
            return dictsourceType;
        }

        public void setDictsourceType(int dictsourceType) {
            this.dictsourceType = dictsourceType;
        }
    }

    public static class ZqpdBean implements Serializable {
        /**
         * isNewRecord : true
         * dictId :
         * codeValue : 1
         * codeName : 真警
         * dictsourceType : 0
         */

        private boolean isNewRecord;
        private String dictId;
        private String codeValue;
        private String codeName;
        private int dictsourceType;

        public boolean isIsNewRecord() {
            return isNewRecord;
        }

        public void setIsNewRecord(boolean isNewRecord) {
            this.isNewRecord = isNewRecord;
        }

        public String getDictId() {
            return dictId;
        }

        public void setDictId(String dictId) {
            this.dictId = dictId;
        }

        public String getCodeValue() {
            return codeValue;
        }

        public void setCodeValue(String codeValue) {
            this.codeValue = codeValue;
        }

        public String getCodeName() {
            return codeName;
        }

        public void setCodeName(String codeName) {
            this.codeName = codeName;
        }

        public int getDictsourceType() {
            return dictsourceType;
        }

        public void setDictsourceType(int dictsourceType) {
            this.dictsourceType = dictsourceType;
        }
    }

    public static class OragnBean implements Serializable {
        /**
         * id : d6d40ebf20f74a96ab83147a90f8cc37
         * isNewRecord : false
         * name : 新余市消防支队
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

    public static class ExtendBean implements Serializable {
        /**
         * isNewRecord : true
         * createDate : 2019-07-09 20:34:19
         * updateDate : 2019-07-09 20:34:19
         * alarmid : f91b43f99a794735ae660b851a26d368
         * alarmsource :
         * refid : 3600600020190709203534942001
         * isGravenessCase : 1
         * incepttype : {"isNewRecord":true,"dictId":"","codeValue":"1","codeName":"真警","dictsourceType":0}
         * substation : {"id":"d6d40ebf20f74a96ab83147a90f8cc37","isNewRecord":false,"name":"新余市消防支队","sort":30,"type":"2","address":"","range":0,"ordernoInt":0,"jgqzInt":0,"parentId":"0"}
         * smogstatus : {"isNewRecord":true,"dictId":"","codeValue":"1","codeName":"有烟","dictsourceType":0}
         * firematterclass : {"isNewRecord":true,"dictId":"","codeValue":"200","codeName":"家俱、设备及竹、木等制品","dictsourceType":0}
         * selectcarflag : 0
         * tagsendmsg : 0
         * tagreqreinforce : 0
         * tagreinforce : 0
         * tagsendwd : 0
         * tagrecwd : 0
         * acceptuserid : 5cc33a12ffffff8a2628f5ec4d0d5d0f
         * acceptseat : 2001
         * procuserid : 5cc33a12ffffff8a2628f5ec4d0d5d0f
         * procseat : 2001
         * arcflag : 0
         * sjc : 2019-07-09 20:34:19
         * bjdz : 江西省新余市渝水区登丰路
         * zhcs : {"isNewRecord":true,"dictId":"","codeValue":"","codeName":"","dictsourceType":0}
         * zqxl : {"isNewRecord":true,"dictId":"","codeValue":"","codeName":"","dictsourceType":0}
         */

        private boolean isNewRecord;
        private String createDate;
        private String updateDate;
        private String alarmid;
        private String alarmsource;
        private String refid;
        private String isGravenessCase;
        private IncepttypeBean incepttype;
        private SubstationBean substation;
        private SmogstatusBean smogstatus;
        private FirematterclassBean firematterclass;
        private String selectcarflag;
        private String tagsendmsg;
        private String tagreqreinforce;
        private String tagreinforce;
        private String tagsendwd;
        private String tagrecwd;
        private String acceptuserid;
        private String acceptseat;
        private String procuserid;
        private String procseat;
        private String arcflag;
        private String sjc;
        private String bjdz;
        private ZhcsBean zhcs;
        private ZqxlBean zqxl;

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

        public String getAlarmid() {
            return alarmid;
        }

        public void setAlarmid(String alarmid) {
            this.alarmid = alarmid;
        }

        public String getAlarmsource() {
            return alarmsource;
        }

        public void setAlarmsource(String alarmsource) {
            this.alarmsource = alarmsource;
        }

        public String getRefid() {
            return refid;
        }

        public void setRefid(String refid) {
            this.refid = refid;
        }

        public String getIsGravenessCase() {
            return isGravenessCase;
        }

        public void setIsGravenessCase(String isGravenessCase) {
            this.isGravenessCase = isGravenessCase;
        }

        public IncepttypeBean getIncepttype() {
            return incepttype;
        }

        public void setIncepttype(IncepttypeBean incepttype) {
            this.incepttype = incepttype;
        }

        public SubstationBean getSubstation() {
            return substation;
        }

        public void setSubstation(SubstationBean substation) {
            this.substation = substation;
        }

        public SmogstatusBean getSmogstatus() {
            return smogstatus;
        }

        public void setSmogstatus(SmogstatusBean smogstatus) {
            this.smogstatus = smogstatus;
        }

        public FirematterclassBean getFirematterclass() {
            return firematterclass;
        }

        public void setFirematterclass(FirematterclassBean firematterclass) {
            this.firematterclass = firematterclass;
        }

        public String getSelectcarflag() {
            return selectcarflag;
        }

        public void setSelectcarflag(String selectcarflag) {
            this.selectcarflag = selectcarflag;
        }

        public String getTagsendmsg() {
            return tagsendmsg;
        }

        public void setTagsendmsg(String tagsendmsg) {
            this.tagsendmsg = tagsendmsg;
        }

        public String getTagreqreinforce() {
            return tagreqreinforce;
        }

        public void setTagreqreinforce(String tagreqreinforce) {
            this.tagreqreinforce = tagreqreinforce;
        }

        public String getTagreinforce() {
            return tagreinforce;
        }

        public void setTagreinforce(String tagreinforce) {
            this.tagreinforce = tagreinforce;
        }

        public String getTagsendwd() {
            return tagsendwd;
        }

        public void setTagsendwd(String tagsendwd) {
            this.tagsendwd = tagsendwd;
        }

        public String getTagrecwd() {
            return tagrecwd;
        }

        public void setTagrecwd(String tagrecwd) {
            this.tagrecwd = tagrecwd;
        }

        public String getAcceptuserid() {
            return acceptuserid;
        }

        public void setAcceptuserid(String acceptuserid) {
            this.acceptuserid = acceptuserid;
        }

        public String getAcceptseat() {
            return acceptseat;
        }

        public void setAcceptseat(String acceptseat) {
            this.acceptseat = acceptseat;
        }

        public String getProcuserid() {
            return procuserid;
        }

        public void setProcuserid(String procuserid) {
            this.procuserid = procuserid;
        }

        public String getProcseat() {
            return procseat;
        }

        public void setProcseat(String procseat) {
            this.procseat = procseat;
        }

        public String getArcflag() {
            return arcflag;
        }

        public void setArcflag(String arcflag) {
            this.arcflag = arcflag;
        }

        public String getSjc() {
            return sjc;
        }

        public void setSjc(String sjc) {
            this.sjc = sjc;
        }

        public String getBjdz() {
            return bjdz;
        }

        public void setBjdz(String bjdz) {
            this.bjdz = bjdz;
        }

        public ZhcsBean getZhcs() {
            return zhcs;
        }

        public void setZhcs(ZhcsBean zhcs) {
            this.zhcs = zhcs;
        }

        public ZqxlBean getZqxl() {
            return zqxl;
        }

        public void setZqxl(ZqxlBean zqxl) {
            this.zqxl = zqxl;
        }

        public static class IncepttypeBean implements Serializable {
            /**
             * isNewRecord : true
             * dictId :
             * codeValue : 1
             * codeName : 真警
             * dictsourceType : 0
             */

            private boolean isNewRecord;
            private String dictId;
            private String codeValue;
            private String codeName;
            private int dictsourceType;

            public boolean isIsNewRecord() {
                return isNewRecord;
            }

            public void setIsNewRecord(boolean isNewRecord) {
                this.isNewRecord = isNewRecord;
            }

            public String getDictId() {
                return dictId;
            }

            public void setDictId(String dictId) {
                this.dictId = dictId;
            }

            public String getCodeValue() {
                return codeValue;
            }

            public void setCodeValue(String codeValue) {
                this.codeValue = codeValue;
            }

            public String getCodeName() {
                return codeName;
            }

            public void setCodeName(String codeName) {
                this.codeName = codeName;
            }

            public int getDictsourceType() {
                return dictsourceType;
            }

            public void setDictsourceType(int dictsourceType) {
                this.dictsourceType = dictsourceType;
            }


            @Override
            public String toString() {
                return "IncepttypeBean{" +
                        "isNewRecord=" + isNewRecord +
                        ", dictId='" + dictId + '\'' +
                        ", codeValue='" + codeValue + '\'' +
                        ", codeName='" + codeName + '\'' +
                        ", dictsourceType=" + dictsourceType +
                        '}';
            }
        }

        public static class SubstationBean implements Serializable {
            /**
             * id : d6d40ebf20f74a96ab83147a90f8cc37
             * isNewRecord : false
             * name : 新余市消防支队
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

            @Override
            public String toString() {
                return "SubstationBean{" +
                        "id='" + id + '\'' +
                        ", isNewRecord=" + isNewRecord +
                        ", name='" + name + '\'' +
                        ", sort=" + sort +
                        ", type='" + type + '\'' +
                        ", address='" + address + '\'' +
                        ", range=" + range +
                        ", ordernoInt=" + ordernoInt +
                        ", jgqzInt=" + jgqzInt +
                        ", parentId='" + parentId + '\'' +
                        '}';
            }
        }

        public static class SmogstatusBean implements Serializable {
            /**
             * isNewRecord : true
             * dictId :
             * codeValue : 1
             * codeName : 有烟
             * dictsourceType : 0
             */

            private boolean isNewRecord;
            private String dictId;
            private String codeValue;
            private String codeName;
            private int dictsourceType;

            public boolean isIsNewRecord() {
                return isNewRecord;
            }

            public void setIsNewRecord(boolean isNewRecord) {
                this.isNewRecord = isNewRecord;
            }

            public String getDictId() {
                return dictId;
            }

            public void setDictId(String dictId) {
                this.dictId = dictId;
            }

            public String getCodeValue() {
                return codeValue;
            }

            public void setCodeValue(String codeValue) {
                this.codeValue = codeValue;
            }

            public String getCodeName() {
                return codeName;
            }

            public void setCodeName(String codeName) {
                this.codeName = codeName;
            }

            public int getDictsourceType() {
                return dictsourceType;
            }

            public void setDictsourceType(int dictsourceType) {
                this.dictsourceType = dictsourceType;
            }

            @Override
            public String toString() {
                return "SmogstatusBean{" +
                        "isNewRecord=" + isNewRecord +
                        ", dictId='" + dictId + '\'' +
                        ", codeValue='" + codeValue + '\'' +
                        ", codeName='" + codeName + '\'' +
                        ", dictsourceType=" + dictsourceType +
                        '}';
            }
        }

        public static class FirematterclassBean implements Serializable {
            /**
             * isNewRecord : true
             * dictId :
             * codeValue : 200
             * codeName : 家俱、设备及竹、木等制品
             * dictsourceType : 0
             */

            private boolean isNewRecord;
            private String dictId;
            private String codeValue;
            private String codeName;
            private int dictsourceType;

            public boolean isIsNewRecord() {
                return isNewRecord;
            }

            public void setIsNewRecord(boolean isNewRecord) {
                this.isNewRecord = isNewRecord;
            }

            public String getDictId() {
                return dictId;
            }

            public void setDictId(String dictId) {
                this.dictId = dictId;
            }

            public String getCodeValue() {
                return codeValue;
            }

            public void setCodeValue(String codeValue) {
                this.codeValue = codeValue;
            }

            public String getCodeName() {
                return codeName;
            }

            public void setCodeName(String codeName) {
                this.codeName = codeName;
            }

            public int getDictsourceType() {
                return dictsourceType;
            }

            public void setDictsourceType(int dictsourceType) {
                this.dictsourceType = dictsourceType;
            }

            @Override
            public String toString() {
                return "FirematterclassBean{" +
                        "isNewRecord=" + isNewRecord +
                        ", dictId='" + dictId + '\'' +
                        ", codeValue='" + codeValue + '\'' +
                        ", codeName='" + codeName + '\'' +
                        ", dictsourceType=" + dictsourceType +
                        '}';
            }
        }

        public static class ZhcsBean implements Serializable {
            /**
             * isNewRecord : true
             * dictId :
             * codeValue :
             * codeName :
             * dictsourceType : 0
             */

            private boolean isNewRecord;
            private String dictId;
            private String codeValue;
            private String codeName;
            private int dictsourceType;

            public boolean isIsNewRecord() {
                return isNewRecord;
            }

            public void setIsNewRecord(boolean isNewRecord) {
                this.isNewRecord = isNewRecord;
            }

            public String getDictId() {
                return dictId;
            }

            public void setDictId(String dictId) {
                this.dictId = dictId;
            }

            public String getCodeValue() {
                return codeValue;
            }

            public void setCodeValue(String codeValue) {
                this.codeValue = codeValue;
            }

            public String getCodeName() {
                return codeName;
            }

            public void setCodeName(String codeName) {
                this.codeName = codeName;
            }

            public int getDictsourceType() {
                return dictsourceType;
            }

            public void setDictsourceType(int dictsourceType) {
                this.dictsourceType = dictsourceType;
            }

            @Override
            public String toString() {
                return "ZhcsBean{" +
                        "isNewRecord=" + isNewRecord +
                        ", dictId='" + dictId + '\'' +
                        ", codeValue='" + codeValue + '\'' +
                        ", codeName='" + codeName + '\'' +
                        ", dictsourceType=" + dictsourceType +
                        '}';
            }
        }

        public static class ZqxlBean implements Serializable {
            /**
             * isNewRecord : true
             * dictId :
             * codeValue :
             * codeName :
             * dictsourceType : 0
             */

            private boolean isNewRecord;
            private String dictId;
            private String codeValue;
            private String codeName;
            private int dictsourceType;

            public boolean isIsNewRecord() {
                return isNewRecord;
            }

            public void setIsNewRecord(boolean isNewRecord) {
                this.isNewRecord = isNewRecord;
            }

            public String getDictId() {
                return dictId;
            }

            public void setDictId(String dictId) {
                this.dictId = dictId;
            }

            public String getCodeValue() {
                return codeValue;
            }

            public void setCodeValue(String codeValue) {
                this.codeValue = codeValue;
            }

            public String getCodeName() {
                return codeName;
            }

            public void setCodeName(String codeName) {
                this.codeName = codeName;
            }

            public int getDictsourceType() {
                return dictsourceType;
            }

            public void setDictsourceType(int dictsourceType) {
                this.dictsourceType = dictsourceType;
            }

            @Override
            public String toString() {
                return "ZqxlBean{" +
                        "isNewRecord=" + isNewRecord +
                        ", dictId='" + dictId + '\'' +
                        ", codeValue='" + codeValue + '\'' +
                        ", codeName='" + codeName + '\'' +
                        ", dictsourceType=" + dictsourceType +
                        '}';
            }
        }
    }

    @Override
    public String toString() {
        return "DisasterDetail{" +
                "id='" + id + '\'' +
                ", isNewRecord=" + isNewRecord +
                ", createDate='" + createDate + '\'' +
                ", updateDate='" + updateDate + '\'' +
                ", ysid='" + ysid + '\'' +
                ", zqbm='" + zqbm + '\'' +
                ", bjsj='" + bjsj + '\'' +
                ", bjdh='" + bjdh + '\'' +
                ", bjr='" + bjr + '\'' +
                ", bjrId='" + bjrId + '\'' +
                ", jzxm='" + jzxm + '\'' +
                ", bjfs='" + bjfs + '\'' +
                ", zjdz='" + zjdz + '\'' +
                ", xzqydm='" + xzqydm + '\'' +
                ", xqzdjgdm=" + xqzdjgdm +
                ", lxdh='" + lxdh + '\'' +
                ", lxr='" + lxr + '\'' +
                ", zqlx=" + zqlx +
                ", czdx=" + czdx +
                ", zhdj=" + zhdj +
                ", zhdd='" + zhdd + '\'' +
                ", zqms='" + zqms + '\'' +
                ", zqzt=" + zqzt +
                ", ifsb='" + ifsb + '\'' +
                ", tssj='" + tssj + '\'' +
                ", bsdwdm='" + bsdwdm + '\'' +
                ", zqlydwdm='" + zqlydwdm + '\'' +
                ", zddwid='" + zddwid + '\'' +
                ", zddwmc='" + zddwmc + '\'' +
                ", bkrs='" + bkrs + '\'' +
                ", swrs='" + swrs + '\'' +
                ", ssrs='" + ssrs + '\'' +
                ", rsmj='" + rsmj + '\'' +
                ", jzlxdm='" + jzlxdm + '\'' +
                ", qhlc='" + qhlc + '\'' +
                ", lasj='" + lasj + '\'' +
                ", dcsj='" + dcsj + '\'' +
                ", zqtsbs='" + zqtsbs + '\'' +
                ", zqpd=" + zqpd +
                ", czr='" + czr + '\'' +
                ", gisX='" + gisX + '\'' +
                ", gisY='" + gisY + '\'' +
                ", gisH='" + gisH + '\'' +
                ", cszt='" + cszt + '\'' +
                ", sjc='" + sjc + '\'' +
                ", bz='" + bz + '\'' +
                ", cjsj='" + cjsj + '\'' +
                ", vercol='" + vercol + '\'' +
                ", geom='" + geom + '\'' +
                ", oragn=" + oragn +
                ", extend=" + extend +
                '}';
    }
}
