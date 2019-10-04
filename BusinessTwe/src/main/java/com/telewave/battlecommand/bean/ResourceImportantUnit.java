package com.telewave.battlecommand.bean;

import java.util.List;

/**
 * 资源 重点单位 实体类
 *
 * @author liwh
 * @date 2018/12/25
 */
public class ResourceImportantUnit {

    /**
     * type : Feature
     * id : obj_objectinfo.fid-562b6968_141f091d459_4431
     * geometry : {"type":"Point","coordinates":[91.1215,29.6612]}
     * geometry_name : GEOM
     * properties : {"ID":"0dde6e7c948549858b95f9b0fe618af8","RESOURCENAME":"西藏麟源大酒店有限公司","ADDRESS":"西藏自治区拉萨市拉萨市林廓北路25号","CONTACTS":"米玛次仁","CONTACTNUMBER":"6284999","AREACODE":"540102","ORGANID":null,"CATEGORY":"重点单位","CREATE_TIME":"2013-08-19T10:00:30Z","ORGANNAME":null,"AREANAME":"城关区","DISTANCE":77.79214799081493}
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
         * coordinates : [91.1215,29.6612]
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
         * ID : 0dde6e7c948549858b95f9b0fe618af8
         * RESOURCENAME : 西藏麟源大酒店有限公司
         * ADDRESS : 西藏自治区拉萨市拉萨市林廓北路25号
         * CONTACTS : 米玛次仁
         * CONTACTNUMBER : 6284999
         * AREACODE : 540102
         * ORGANID : null
         * CATEGORY : 重点单位
         * CREATE_TIME : 2013-08-19T10:00:30Z
         * ORGANNAME : null
         * AREANAME : 城关区
         * DISTANCE : 77.79214799081493
         */

        private String ID;
        private String RESOURCENAME;
        private String ADDRESS;
        private String CONTACTS;
        private String CONTACTNUMBER;
        private String AREACODE;
        private Object ORGANID;
        private String CATEGORY;
        private String CREATE_TIME;
        private Object ORGANNAME;
        private String AREANAME;
        private double DISTANCE;

        public String getID() {
            return ID;
        }

        public void setID(String ID) {
            this.ID = ID;
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

        public String getAREACODE() {
            return AREACODE;
        }

        public void setAREACODE(String AREACODE) {
            this.AREACODE = AREACODE;
        }

        public Object getORGANID() {
            return ORGANID;
        }

        public void setORGANID(Object ORGANID) {
            this.ORGANID = ORGANID;
        }

        public String getCATEGORY() {
            return CATEGORY;
        }

        public void setCATEGORY(String CATEGORY) {
            this.CATEGORY = CATEGORY;
        }

        public String getCREATE_TIME() {
            return CREATE_TIME;
        }

        public void setCREATE_TIME(String CREATE_TIME) {
            this.CREATE_TIME = CREATE_TIME;
        }

        public Object getORGANNAME() {
            return ORGANNAME;
        }

        public void setORGANNAME(Object ORGANNAME) {
            this.ORGANNAME = ORGANNAME;
        }

        public String getAREANAME() {
            return AREANAME;
        }

        public void setAREANAME(String AREANAME) {
            this.AREANAME = AREANAME;
        }

        public double getDISTANCE() {
            return DISTANCE;
        }

        public void setDISTANCE(double DISTANCE) {
            this.DISTANCE = DISTANCE;
        }
    }

    @Override
    public String toString() {
        return "ResourceImportantUnit{" +
                "type='" + type + '\'' +
                ", id='" + id + '\'' +
                ", geometry=" + geometry +
                ", geometry_name='" + geometry_name + '\'' +
                ", properties=" + properties +
                '}';
    }
}
