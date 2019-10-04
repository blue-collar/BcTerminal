package com.telewave.battlecommand.bean;

import java.util.List;

/**
 * 资源 微站 实体类
 *
 * @author liwh
 * @date 2018/12/25
 */
public class ResourceMicroStation {

    /**
     * type : Feature
     * id : b_wxxfz.fid-65ab2d9b_16bf3a6dc79_71bb
     * geometry : {"type":"Point","coordinates":[91.114884,29.655203]}
     * geometry_name : GEOM
     * properties : {"AREANAME":"城关区","ORGANNAME":"城关区二中队","ID":"82bef9f3397e458f8782b1457a80c505","XFZBH":"2","RESOURCENAME":"城关区微型消防站2号","ADDRESS":"拉萨市城关区慈松塘西路","XFZLB":"01","XFZXS":"01","AREACODE":"540102","ORGANID":"3851f889af244a478c3de2d6a0d936ec","CONTACTS":"张三","CONTACTNUMBER":"17313096799","XFZRS":22,"XFZCS":22,"JD":22,"WD":22,"CREATEBY":"1","CREATEDATE":"2018-08-12T02:18:53Z","UPDATEBY":"1","UPDATEDATE":"2018-08-12T02:19:16Z","REMARKS":"22","DELFLAG":"1","DISTANCE":933.3951453354142}
     */

    private String type;
    private String id;
    private GeometryBean geometry;
    private String geometry_name;
    private PropertiesBean properties;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public GeometryBean getGeometry() {
        return geometry;
    }

    public void setGeometry(GeometryBean geometry) {
        this.geometry = geometry;
    }

    public String getGeometry_name() {
        return geometry_name;
    }

    public void setGeometry_name(String geometry_name) {
        this.geometry_name = geometry_name;
    }

    public PropertiesBean getProperties() {
        return properties;
    }

    public void setProperties(PropertiesBean properties) {
        this.properties = properties;
    }

    public static class GeometryBean {
        /**
         * type : Point
         * coordinates : [91.114884,29.655203]
         */

        private String type;
        private List<Double> coordinates;

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public List<Double> getCoordinates() {
            return coordinates;
        }

        public void setCoordinates(List<Double> coordinates) {
            this.coordinates = coordinates;
        }
    }

    public static class PropertiesBean {
        /**
         * AREANAME : 城关区
         * ORGANNAME : 城关区二中队
         * ID : 82bef9f3397e458f8782b1457a80c505
         * XFZBH : 2
         * RESOURCENAME : 城关区微型消防站2号
         * ADDRESS : 拉萨市城关区慈松塘西路
         * XFZLB : 01
         * XFZXS : 01
         * AREACODE : 540102
         * ORGANID : 3851f889af244a478c3de2d6a0d936ec
         * CONTACTS : 张三
         * CONTACTNUMBER : 17313096799
         * XFZRS : 22
         * XFZCS : 22
         * JD : 22
         * WD : 22
         * CREATEBY : 1
         * CREATEDATE : 2018-08-12T02:18:53Z
         * UPDATEBY : 1
         * UPDATEDATE : 2018-08-12T02:19:16Z
         * REMARKS : 22
         * DELFLAG : 1
         * DISTANCE : 933.3951453354142
         */

        private String AREANAME;
        private String ORGANNAME;
        private String ID;
        private String XFZBH;
        private String RESOURCENAME;
        private String ADDRESS;
        private String XFZLB;
        private String XFZXS;
        private String AREACODE;
        private String ORGANID;
        private String CONTACTS;
        private String CONTACTNUMBER;
        private int XFZRS;
        private int XFZCS;
        private double JD;
        private double WD;
        private String CREATEBY;
        private String CREATEDATE;
        private String UPDATEBY;
        private String UPDATEDATE;
        private String REMARKS;
        private String DELFLAG;
        private double DISTANCE;

        public String getAREANAME() {
            return AREANAME;
        }

        public void setAREANAME(String AREANAME) {
            this.AREANAME = AREANAME;
        }

        public String getORGANNAME() {
            return ORGANNAME;
        }

        public void setORGANNAME(String ORGANNAME) {
            this.ORGANNAME = ORGANNAME;
        }

        public String getID() {
            return ID;
        }

        public void setID(String ID) {
            this.ID = ID;
        }

        public String getXFZBH() {
            return XFZBH;
        }

        public void setXFZBH(String XFZBH) {
            this.XFZBH = XFZBH;
        }

        public String getRESOURCENAME() {
            return RESOURCENAME;
        }

        public void setRESOURCENAME(String RESOURCENAME) {
            this.RESOURCENAME = RESOURCENAME;
        }

        public String getADDRESS() {
            return ADDRESS;
        }

        public void setADDRESS(String ADDRESS) {
            this.ADDRESS = ADDRESS;
        }

        public String getXFZLB() {
            return XFZLB;
        }

        public void setXFZLB(String XFZLB) {
            this.XFZLB = XFZLB;
        }

        public String getXFZXS() {
            return XFZXS;
        }

        public void setXFZXS(String XFZXS) {
            this.XFZXS = XFZXS;
        }

        public String getAREACODE() {
            return AREACODE;
        }

        public void setAREACODE(String AREACODE) {
            this.AREACODE = AREACODE;
        }

        public String getORGANID() {
            return ORGANID;
        }

        public void setORGANID(String ORGANID) {
            this.ORGANID = ORGANID;
        }

        public String getCONTACTS() {
            return CONTACTS;
        }

        public void setCONTACTS(String CONTACTS) {
            this.CONTACTS = CONTACTS;
        }

        public String getCONTACTNUMBER() {
            return CONTACTNUMBER;
        }

        public void setCONTACTNUMBER(String CONTACTNUMBER) {
            this.CONTACTNUMBER = CONTACTNUMBER;
        }

        public int getXFZRS() {
            return XFZRS;
        }

        public void setXFZRS(int XFZRS) {
            this.XFZRS = XFZRS;
        }

        public int getXFZCS() {
            return XFZCS;
        }

        public void setXFZCS(int XFZCS) {
            this.XFZCS = XFZCS;
        }

        public double getJD() {
            return JD;
        }

        public void setJD(double JD) {
            this.JD = JD;
        }

        public double getWD() {
            return WD;
        }

        public void setWD(double WD) {
            this.WD = WD;
        }

        public String getCREATEBY() {
            return CREATEBY;
        }

        public void setCREATEBY(String CREATEBY) {
            this.CREATEBY = CREATEBY;
        }

        public String getCREATEDATE() {
            return CREATEDATE;
        }

        public void setCREATEDATE(String CREATEDATE) {
            this.CREATEDATE = CREATEDATE;
        }

        public String getUPDATEBY() {
            return UPDATEBY;
        }

        public void setUPDATEBY(String UPDATEBY) {
            this.UPDATEBY = UPDATEBY;
        }

        public String getUPDATEDATE() {
            return UPDATEDATE;
        }

        public void setUPDATEDATE(String UPDATEDATE) {
            this.UPDATEDATE = UPDATEDATE;
        }

        public String getREMARKS() {
            return REMARKS;
        }

        public void setREMARKS(String REMARKS) {
            this.REMARKS = REMARKS;
        }

        public String getDELFLAG() {
            return DELFLAG;
        }

        public void setDELFLAG(String DELFLAG) {
            this.DELFLAG = DELFLAG;
        }

        public double getDISTANCE() {
            return DISTANCE;
        }

        public void setDISTANCE(double DISTANCE) {
            this.DISTANCE = DISTANCE;
        }
    }
}
