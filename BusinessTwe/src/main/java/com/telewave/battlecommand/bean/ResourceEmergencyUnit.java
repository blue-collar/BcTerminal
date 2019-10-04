package com.telewave.battlecommand.bean;

import java.util.List;

/**
 * 应急单位
 */
public class ResourceEmergencyUnit {

    /**
     * type : Feature
     * id : emergency_unit.fid-5b677aee_16c8375a62c_-6843
     * geometry : {"type":"Point","coordinates":[114.9174558,27.8284644]}
     * geometry_name : GEOM
     * properties : {"ID":"0aa8bcc81748407586f5d6f001bd588f","ORGANNAME":"新余市消防支队","DWMC":"市规划局","DWDZ":"江西省新余市新余市","DWLX":"99","YJFWNR":"提供相关技术支持","LXR":"张锐","LXDH":"13907908290","DWCZ":"6442132","GLID":"c4682047aea1ec9a53b7df7482ca509b","GISX":114.9174558,"GISY":27.8284644,"JLZT":1,"CSZT":0,"SJC":"2019-08-02T16:00:00Z","SJBB":1,"CJSJ":"2019-08-02T16:00:00Z","YWXTBSID":"mh.ywgl@jx.xf","VERCOL":"2019-08-06T16:00:00Z","DISTANCE":970.2539584657549}
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
         * coordinates : [114.9174558,27.8284644]
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
         * ID : 0aa8bcc81748407586f5d6f001bd588f
         * ORGANNAME : 新余市消防支队
         * DWMC : 市规划局
         * DWDZ : 江西省新余市新余市
         * DWLX : 99
         * YJFWNR : 提供相关技术支持
         * LXR : 张锐
         * LXDH : 13907908290
         * DWCZ : 6442132
         * GLID : c4682047aea1ec9a53b7df7482ca509b
         * GISX : 114.9174558
         * GISY : 27.8284644
         * JLZT : 1
         * CSZT : 0
         * SJC : 2019-08-02T16:00:00Z
         * SJBB : 1
         * CJSJ : 2019-08-02T16:00:00Z
         * YWXTBSID : mh.ywgl@jx.xf
         * VERCOL : 2019-08-06T16:00:00Z
         * DISTANCE : 970.2539584657549
         */

        private String ID;
        private String ORGANNAME;
        private String DWMC;
        private String DWDZ;
        private String DWLX;
        private String YJFWNR;
        private String LXR;
        private String LXDH;
        private String DWCZ;
        private String GLID;
        private double GISX;
        private double GISY;
        private int JLZT;
        private int CSZT;
        private String SJC;
        private int SJBB;
        private String CJSJ;
        private String YWXTBSID;
        private String VERCOL;
        private double DISTANCE;

        public String getID() {
            return ID;
        }

        public void setID(String ID) {
            this.ID = ID;
        }

        public String getORGANNAME() {
            return ORGANNAME;
        }

        public void setORGANNAME(String ORGANNAME) {
            this.ORGANNAME = ORGANNAME;
        }

        public String getDWMC() {
            return DWMC;
        }

        public void setDWMC(String DWMC) {
            this.DWMC = DWMC;
        }

        public String getDWDZ() {
            return DWDZ;
        }

        public void setDWDZ(String DWDZ) {
            this.DWDZ = DWDZ;
        }

        public String getDWLX() {
            return DWLX;
        }

        public void setDWLX(String DWLX) {
            this.DWLX = DWLX;
        }

        public String getYJFWNR() {
            return YJFWNR;
        }

        public void setYJFWNR(String YJFWNR) {
            this.YJFWNR = YJFWNR;
        }

        public String getLXR() {
            return LXR;
        }

        public void setLXR(String LXR) {
            this.LXR = LXR;
        }

        public String getLXDH() {
            return LXDH;
        }

        public void setLXDH(String LXDH) {
            this.LXDH = LXDH;
        }

        public String getDWCZ() {
            return DWCZ;
        }

        public void setDWCZ(String DWCZ) {
            this.DWCZ = DWCZ;
        }

        public String getGLID() {
            return GLID;
        }

        public void setGLID(String GLID) {
            this.GLID = GLID;
        }

        public double getGISX() {
            return GISX;
        }

        public void setGISX(double GISX) {
            this.GISX = GISX;
        }

        public double getGISY() {
            return GISY;
        }

        public void setGISY(double GISY) {
            this.GISY = GISY;
        }

        public int getJLZT() {
            return JLZT;
        }

        public void setJLZT(int JLZT) {
            this.JLZT = JLZT;
        }

        public int getCSZT() {
            return CSZT;
        }

        public void setCSZT(int CSZT) {
            this.CSZT = CSZT;
        }

        public String getSJC() {
            return SJC;
        }

        public void setSJC(String SJC) {
            this.SJC = SJC;
        }

        public int getSJBB() {
            return SJBB;
        }

        public void setSJBB(int SJBB) {
            this.SJBB = SJBB;
        }

        public String getCJSJ() {
            return CJSJ;
        }

        public void setCJSJ(String CJSJ) {
            this.CJSJ = CJSJ;
        }

        public String getYWXTBSID() {
            return YWXTBSID;
        }

        public void setYWXTBSID(String YWXTBSID) {
            this.YWXTBSID = YWXTBSID;
        }

        public String getVERCOL() {
            return VERCOL;
        }

        public void setVERCOL(String VERCOL) {
            this.VERCOL = VERCOL;
        }

        public double getDISTANCE() {
            return DISTANCE;
        }

        public void setDISTANCE(double DISTANCE) {
            this.DISTANCE = DISTANCE;
        }
    }
}
