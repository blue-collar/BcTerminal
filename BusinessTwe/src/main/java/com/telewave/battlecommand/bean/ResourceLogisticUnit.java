package com.telewave.battlecommand.bean;

import java.util.List;

/**
 * @author liwh
 * @date 2019/8/12
 */
public class ResourceLogisticUnit {

    /**
     * type : Feature
     * id : logistic_unit.fid-5b677aee_16c8375a62c_-684c
     * geometry : {"type":"Point","coordinates":[114.916569,27.84089]}
     * geometry_name : GEOM
     * properties : {"ID":"8a08a53936de3a220136f15d37633d1c","DWMC":"新余市第六医院","SFWNBJG":"0","NBJGID":null,"WBDWID":"8a08a53936de3a220136f15d37633d1c","YYDJDM":null,"ZYBZLBDM":"09","BZNL":"医疗救护","YJZBDH":"07906491800","GISX":114.916569,"gisY":27.84089,"GLID":"b29576aad4764705a3562b95b233c43b","WHXFJG":"241371a3d5874132b3dabd7925e0f9e1","WHXFJGMC":"平安路中队","JLZT":1,"CSZT":2,"SJC":"2019-08-02T16:00:00Z","BZ":null,"CJSJ":"2019-08-02T16:00:00Z","SJBB":1,"YWXTBSID":"bdgl.zbgl@xy.jx.xf","VERCOL":"2019-08-08T13:01:36Z","JKSJBB":null,"DISTANCE":462.8878696258643}
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
         * coordinates : [114.916569,27.84089]
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
         * ID : 8a08a53936de3a220136f15d37633d1c
         * DWMC : 新余市第六医院
         * SFWNBJG : 0
         * NBJGID : null
         * WBDWID : 8a08a53936de3a220136f15d37633d1c
         * YYDJDM : null
         * ZYBZLBDM : 09
         * BZNL : 医疗救护
         * YJZBDH : 07906491800
         * GISX : 114.916569
         * gisY : 27.84089
         * GLID : b29576aad4764705a3562b95b233c43b
         * WHXFJG : 241371a3d5874132b3dabd7925e0f9e1
         * WHXFJGMC : 平安路中队
         * JLZT : 1
         * CSZT : 2
         * SJC : 2019-08-02T16:00:00Z
         * BZ : null
         * CJSJ : 2019-08-02T16:00:00Z
         * SJBB : 1
         * YWXTBSID : bdgl.zbgl@xy.jx.xf
         * VERCOL : 2019-08-08T13:01:36Z
         * JKSJBB : null
         * DISTANCE : 462.8878696258643
         */

        private String ID;
        private String DWMC;
        private String SFWNBJG;
        private Object NBJGID;
        private String WBDWID;
        private Object YYDJDM;
        private String ZYBZLBDM;
        private String BZNL;
        private String YJZBDH;
        private double GISX;
        private double gisY;
        private String GLID;
        private String WHXFJG;
        private String WHXFJGMC;
        private int JLZT;
        private int CSZT;
        private String SJC;
        private Object BZ;
        private String CJSJ;
        private int SJBB;
        private String YWXTBSID;
        private String VERCOL;
        private Object JKSJBB;
        private double DISTANCE;

        public String getID() {
            return ID;
        }

        public void setID(String ID) {
            this.ID = ID;
        }

        public String getDWMC() {
            return DWMC;
        }

        public void setDWMC(String DWMC) {
            this.DWMC = DWMC;
        }

        public String getSFWNBJG() {
            return SFWNBJG;
        }

        public void setSFWNBJG(String SFWNBJG) {
            this.SFWNBJG = SFWNBJG;
        }

        public Object getNBJGID() {
            return NBJGID;
        }

        public void setNBJGID(Object NBJGID) {
            this.NBJGID = NBJGID;
        }

        public String getWBDWID() {
            return WBDWID;
        }

        public void setWBDWID(String WBDWID) {
            this.WBDWID = WBDWID;
        }

        public Object getYYDJDM() {
            return YYDJDM;
        }

        public void setYYDJDM(Object YYDJDM) {
            this.YYDJDM = YYDJDM;
        }

        public String getZYBZLBDM() {
            return ZYBZLBDM;
        }

        public void setZYBZLBDM(String ZYBZLBDM) {
            this.ZYBZLBDM = ZYBZLBDM;
        }

        public String getBZNL() {
            return BZNL;
        }

        public void setBZNL(String BZNL) {
            this.BZNL = BZNL;
        }

        public String getYJZBDH() {
            return YJZBDH;
        }

        public void setYJZBDH(String YJZBDH) {
            this.YJZBDH = YJZBDH;
        }

        public double getGISX() {
            return GISX;
        }

        public void setGISX(double GISX) {
            this.GISX = GISX;
        }

        public double getGisY() {
            return gisY;
        }

        public void setGisY(double gisY) {
            this.gisY = gisY;
        }

        public String getGLID() {
            return GLID;
        }

        public void setGLID(String GLID) {
            this.GLID = GLID;
        }

        public String getWHXFJG() {
            return WHXFJG;
        }

        public void setWHXFJG(String WHXFJG) {
            this.WHXFJG = WHXFJG;
        }

        public String getWHXFJGMC() {
            return WHXFJGMC;
        }

        public void setWHXFJGMC(String WHXFJGMC) {
            this.WHXFJGMC = WHXFJGMC;
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

        public Object getBZ() {
            return BZ;
        }

        public void setBZ(Object BZ) {
            this.BZ = BZ;
        }

        public String getCJSJ() {
            return CJSJ;
        }

        public void setCJSJ(String CJSJ) {
            this.CJSJ = CJSJ;
        }

        public int getSJBB() {
            return SJBB;
        }

        public void setSJBB(int SJBB) {
            this.SJBB = SJBB;
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

        public Object getJKSJBB() {
            return JKSJBB;
        }

        public void setJKSJBB(Object JKSJBB) {
            this.JKSJBB = JKSJBB;
        }

        public double getDISTANCE() {
            return DISTANCE;
        }

        public void setDISTANCE(double DISTANCE) {
            this.DISTANCE = DISTANCE;
        }
    }
}
