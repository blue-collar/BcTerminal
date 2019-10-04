package com.telewave.battlecommand.bean;

import java.util.List;

public class ResourceHighBuild {

    /**
     * type : Feature
     * id : high_buildings.fid-764489f5_16c04360c8f_-7fbb
     * geometry : {"type":"Point","coordinates":[108.94987106,34.25881259]}
     * geometry_name : GEOM
     * properties : {"CONTACTS":null,"CONTACTNUMBER":"7422304","AREACODE":"513223","ORGANNAME":"茂县公安局凤仪镇派出所","ID":"a4f86140d2aa4072a9134ad4fd370009","ARCHITECTURENAME":"医院","ADDRESS":"西安市灞桥区东郊纺织城纺东街167号","UNITTYPE":"高层","ORGANID":"002537ae740749a4b62067b0bb5a9af9","LATITUDEX":null,"DECIMALY":null,"UNITFAX":"","DUTYPHONE":null,"SERVICESCOPE":"","CREATEBY":"1","CREATEDATE":"2019-07-17T11:12:01Z","UPDATEBY":"1","UPDATEDATE":"2019-07-17T11:12:01Z","REMARKS":"","DELFLAG":"1","DISTANCE":811608.6760307556}
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
         * coordinates : [108.94987106,34.25881259]
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
         * CONTACTS : null
         * CONTACTNUMBER : 7422304
         * AREACODE : 513223
         * ORGANNAME : 茂县公安局凤仪镇派出所
         * ID : a4f86140d2aa4072a9134ad4fd370009
         * ARCHITECTURENAME : 医院
         * ADDRESS : 西安市灞桥区东郊纺织城纺东街167号
         * UNITTYPE : 高层
         * ORGANID : 002537ae740749a4b62067b0bb5a9af9
         * LATITUDEX : null
         * DECIMALY : null
         * UNITFAX :
         * DUTYPHONE : null
         * SERVICESCOPE :
         * CREATEBY : 1
         * CREATEDATE : 2019-07-17T11:12:01Z
         * UPDATEBY : 1
         * UPDATEDATE : 2019-07-17T11:12:01Z
         * REMARKS :
         * DELFLAG : 1
         * DISTANCE : 811608.6760307556
         */

        private Object CONTACTS;
        private String CONTACTNUMBER;
        private String AREACODE;
        private String ORGANNAME;
        private String ID;
        private String ARCHITECTURENAME;
        private String ADDRESS;
        private String UNITTYPE;
        private String ORGANID;
        private Object LATITUDEX;
        private Object DECIMALY;
        private String UNITFAX;
        private Object DUTYPHONE;
        private String SERVICESCOPE;
        private String CREATEBY;
        private String CREATEDATE;
        private String UPDATEBY;
        private String UPDATEDATE;
        private String REMARKS;
        private String DELFLAG;
        private double DISTANCE;

        public Object getCONTACTS() {
            return CONTACTS;
        }

        public void setCONTACTS(Object CONTACTS) {
            this.CONTACTS = CONTACTS;
        }

        public String getCONTACTNUMBER() {
            return CONTACTNUMBER;
        }

        public void setCONTACTNUMBER(String CONTACTNUMBER) {
            this.CONTACTNUMBER = CONTACTNUMBER;
        }

        public String getAREACODE() {
            return AREACODE;
        }

        public void setAREACODE(String AREACODE) {
            this.AREACODE = AREACODE;
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

        public String getARCHITECTURENAME() {
            return ARCHITECTURENAME;
        }

        public void setARCHITECTURENAME(String ARCHITECTURENAME) {
            this.ARCHITECTURENAME = ARCHITECTURENAME;
        }

        public String getADDRESS() {
            return ADDRESS;
        }

        public void setADDRESS(String ADDRESS) {
            this.ADDRESS = ADDRESS;
        }

        public String getUNITTYPE() {
            return UNITTYPE;
        }

        public void setUNITTYPE(String UNITTYPE) {
            this.UNITTYPE = UNITTYPE;
        }

        public String getORGANID() {
            return ORGANID;
        }

        public void setORGANID(String ORGANID) {
            this.ORGANID = ORGANID;
        }

        public Object getLATITUDEX() {
            return LATITUDEX;
        }

        public void setLATITUDEX(Object LATITUDEX) {
            this.LATITUDEX = LATITUDEX;
        }

        public Object getDECIMALY() {
            return DECIMALY;
        }

        public void setDECIMALY(Object DECIMALY) {
            this.DECIMALY = DECIMALY;
        }

        public String getUNITFAX() {
            return UNITFAX;
        }

        public void setUNITFAX(String UNITFAX) {
            this.UNITFAX = UNITFAX;
        }

        public Object getDUTYPHONE() {
            return DUTYPHONE;
        }

        public void setDUTYPHONE(Object DUTYPHONE) {
            this.DUTYPHONE = DUTYPHONE;
        }

        public String getSERVICESCOPE() {
            return SERVICESCOPE;
        }

        public void setSERVICESCOPE(String SERVICESCOPE) {
            this.SERVICESCOPE = SERVICESCOPE;
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
