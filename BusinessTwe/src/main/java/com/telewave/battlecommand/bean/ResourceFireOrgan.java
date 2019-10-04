package com.telewave.battlecommand.bean;

import java.util.List;

/**
 * 资源 消防机构 实体类
 *
 * @author liwh
 * @date 2018/12/25
 */
public class ResourceFireOrgan {


    /**
     * type : Feature
     * id : sys_office.fid-562b6968_141f5468f9a_79bf
     * geometry : {"type":"Point","coordinates":[91.1149,29.6556]}
     * geometry_name : GEOM
     * properties : {"RESOURCENAME":"拉萨市公安消防支队布达拉宫大队","ID":"47eb674ff07945a8ba05c925a339aa36","ORGANID":"47eb674ff07945a8ba05c925a339aa36","ORGANNAME":"拉萨市公安消防支队布达拉宫大队","AREACODE":"540102","ADDRESS":"西藏自治区拉萨市","CONTACTS":"李明","SHORTNAME":"布达拉宫大队","CONTACTNUMBER":"6826119","CATEGORY":"0502","GISX":91.11488623,"GISY":29.65563942,"parentids":"0,1,a91670095afe4801a1255361bcf3d0fb,0c91e4525db542ed933d0d2e6b0f5c3d,47eb674ff07945a8ba05c925a339aa36","DISTANCE":706.113303923111}
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
         * coordinates : [91.1149,29.6556]
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
         * RESOURCENAME : 拉萨市公安消防支队布达拉宫大队
         * ID : 47eb674ff07945a8ba05c925a339aa36
         * ORGANID : 47eb674ff07945a8ba05c925a339aa36
         * ORGANNAME : 拉萨市公安消防支队布达拉宫大队
         * AREACODE : 540102
         * ADDRESS : 西藏自治区拉萨市
         * CONTACTS : 李明
         * SHORTNAME : 布达拉宫大队
         * CONTACTNUMBER : 6826119
         * CATEGORY : 0502
         * GISX : 91.11488623
         * GISY : 29.65563942
         * parentids : 0,1,a91670095afe4801a1255361bcf3d0fb,0c91e4525db542ed933d0d2e6b0f5c3d,47eb674ff07945a8ba05c925a339aa36
         * DISTANCE : 706.113303923111
         */

        private String RESOURCENAME;
        private String ID;
        private String ORGANID;
        private String ORGANNAME;
        private String AREACODE;
        private String ADDRESS;
        private String CONTACTS;
        private String SHORTNAME;
        private String CONTACTNUMBER;
        private String CATEGORY;
        private double GISX;
        private double GISY;
        private String parentids;
        private double DISTANCE;

        public String getRESOURCENAME() {
            return RESOURCENAME;
        }

        public void setRESOURCENAME(String RESOURCENAME) {
            this.RESOURCENAME = RESOURCENAME;
        }

        public String getID() {
            return ID;
        }

        public void setID(String ID) {
            this.ID = ID;
        }

        public String getORGANID() {
            return ORGANID;
        }

        public void setORGANID(String ORGANID) {
            this.ORGANID = ORGANID;
        }

        public String getORGANNAME() {
            return ORGANNAME;
        }

        public void setORGANNAME(String ORGANNAME) {
            this.ORGANNAME = ORGANNAME;
        }

        public String getAREACODE() {
            return AREACODE;
        }

        public void setAREACODE(String AREACODE) {
            this.AREACODE = AREACODE;
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

        public String getSHORTNAME() {
            return SHORTNAME;
        }

        public void setSHORTNAME(String SHORTNAME) {
            this.SHORTNAME = SHORTNAME;
        }

        public String getCONTACTNUMBER() {
            return CONTACTNUMBER;
        }

        public void setCONTACTNUMBER(String CONTACTNUMBER) {
            this.CONTACTNUMBER = CONTACTNUMBER;
        }

        public String getCATEGORY() {
            return CATEGORY;
        }

        public void setCATEGORY(String CATEGORY) {
            this.CATEGORY = CATEGORY;
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

        public String getParentids() {
            return parentids;
        }

        public void setParentids(String parentids) {
            this.parentids = parentids;
        }

        public double getDISTANCE() {
            return DISTANCE;
        }

        public void setDISTANCE(double DISTANCE) {
            this.DISTANCE = DISTANCE;
        }
    }
}
