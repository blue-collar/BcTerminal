package com.telewave.battlecommand.bean;

import java.util.List;

/**
 * 资源 水源 消火栓 实体类
 *
 * @author liwh
 * @date 2018/12/25
 */
public class ResourceHydrant {

    /**
     * type : Feature
     * id : mhjy_syjbxx.fid--31191a61_167e4b06d12_-7fc5
     * geometry : {"type":"Point","coordinates":[91.1222804,29.66155238]}
     * geometry_name : GEOM
     * properties : {"CONTACTS":null,"CONTACTNUMBER":"6383674","AREACODE":"540102","ORGANNAME":"拉萨市公安消防支队特勤大队","ID":"439bb48f7b7b44239b84f5d8d9946504","SYBH":"54001201S0096","RESOURCENAME":"娘热路与林廓北路交界处","ADDRESS":"娘热路与林廓北路交界处以北20米（建设银行对面）","RESOURCETYPE":"1100","SYSXXXID":"439bb48f7b7b44239b84f5d8d9946504","ORGANID":"e1606731acc34c0d9992fe4aa9cbd8b7","QSXS":"地下取水","KYZT":"1","SYZT":"1","XZ":"1111","CZYID":"b8eb790c70f6449fa73f6bd6a533b1d8","SHZT":"0","ZPWJURL":"71fd19bd672a4341aa588b019eede55e","GIS_X":91.1222804,"GIS_Y":29.66155238,"GIS_Z":0,"GLID":null,"SSLDID":"170e3a14f1dc42d0a8c6440157ba1935","JZSJ":"2000-06-19T16:00:00Z","QYSJ":"2012-05-24T02:53:28Z","FWTD":"a00fd73ec75e404a8d6f5600cabd4d95","FWTN":"fd2b166394bd4218b23206d0cbd3fa4e","FWTX":"1e64677332f746dfa18f928e2c887e44","FWTB":"5f2bd2ac8fc6467db18feb96f108f7d0","BLGXD":null,"BLGXN":null,"BLGXX":null,"BLGXB":null,"SJC":"2012-05-24T03:58:59Z","SJBB":2,"YWXTBSID":"mh.ywgl@xz.xf","ISSUBMIT":null,"ISCONFIRM":null,"DATAFROM":"1","CREATEBY":"twadmin","CREATEDATE":"2012-05-22T01:34:03Z","UPDATEBY":"twadmin","UPDATEDATE":"2012-05-22T01:34:03Z","REMARKS":null,"DELFLAG":"1","DISTANCE":89.48836893184628}
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
         * coordinates : [91.1222804,29.66155238]
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
         * CONTACTNUMBER : 6383674
         * AREACODE : 540102
         * ORGANNAME : 拉萨市公安消防支队特勤大队
         * ID : 439bb48f7b7b44239b84f5d8d9946504
         * SYBH : 54001201S0096
         * RESOURCENAME : 娘热路与林廓北路交界处
         * ADDRESS : 娘热路与林廓北路交界处以北20米（建设银行对面）
         * RESOURCETYPE : 1100
         * SYSXXXID : 439bb48f7b7b44239b84f5d8d9946504
         * ORGANID : e1606731acc34c0d9992fe4aa9cbd8b7
         * QSXS : 地下取水
         * KYZT : 1
         * SYZT : 1
         * XZ : 1111
         * CZYID : b8eb790c70f6449fa73f6bd6a533b1d8
         * SHZT : 0
         * ZPWJURL : 71fd19bd672a4341aa588b019eede55e
         * GIS_X : 91.1222804
         * GIS_Y : 29.66155238
         * GIS_Z : 0.0
         * GLID : null
         * SSLDID : 170e3a14f1dc42d0a8c6440157ba1935
         * JZSJ : 2000-06-19T16:00:00Z
         * QYSJ : 2012-05-24T02:53:28Z
         * FWTD : a00fd73ec75e404a8d6f5600cabd4d95
         * FWTN : fd2b166394bd4218b23206d0cbd3fa4e
         * FWTX : 1e64677332f746dfa18f928e2c887e44
         * FWTB : 5f2bd2ac8fc6467db18feb96f108f7d0
         * BLGXD : null
         * BLGXN : null
         * BLGXX : null
         * BLGXB : null
         * SJC : 2012-05-24T03:58:59Z
         * SJBB : 2
         * YWXTBSID : mh.ywgl@xz.xf
         * ISSUBMIT : null
         * ISCONFIRM : null
         * DATAFROM : 1
         * CREATEBY : twadmin
         * CREATEDATE : 2012-05-22T01:34:03Z
         * UPDATEBY : twadmin
         * UPDATEDATE : 2012-05-22T01:34:03Z
         * REMARKS : null
         * DELFLAG : 1
         * DISTANCE : 89.48836893184628
         */

        private Object CONTACTS;
        private String CONTACTNUMBER;
        private String AREACODE;
        private String ORGANNAME;
        private String ID;
        private String SYBH;
        private String RESOURCENAME;
        private String ADDRESS;
        private String RESOURCETYPE;
        private String SYSXXXID;
        private String ORGANID;
        private String QSXS;
        private String KYZT;
        private String SYZT;
        private String XZ;
        private String CZYID;
        private String SHZT;
        private String ZPWJURL;
        private double GIS_X;
        private double GIS_Y;
        private double GIS_Z;
        private Object GLID;
        private String SSLDID;
        private String JZSJ;
        private String QYSJ;
        private String FWTD;
        private String FWTN;
        private String FWTX;
        private String FWTB;
        private Object BLGXD;
        private Object BLGXN;
        private Object BLGXX;
        private Object BLGXB;
        private String SJC;
        private int SJBB;
        private String YWXTBSID;
        private Object ISSUBMIT;
        private Object ISCONFIRM;
        private String DATAFROM;
        private String CREATEBY;
        private String CREATEDATE;
        private String UPDATEBY;
        private String UPDATEDATE;
        private Object REMARKS;
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

        public String getSYBH() {
            return SYBH;
        }

        public void setSYBH(String SYBH) {
            this.SYBH = SYBH;
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

        public String getRESOURCETYPE() {
            return RESOURCETYPE;
        }

        public void setRESOURCETYPE(String RESOURCETYPE) {
            this.RESOURCETYPE = RESOURCETYPE;
        }

        public String getSYSXXXID() {
            return SYSXXXID;
        }

        public void setSYSXXXID(String SYSXXXID) {
            this.SYSXXXID = SYSXXXID;
        }

        public String getORGANID() {
            return ORGANID;
        }

        public void setORGANID(String ORGANID) {
            this.ORGANID = ORGANID;
        }

        public String getQSXS() {
            return QSXS;
        }

        public void setQSXS(String QSXS) {
            this.QSXS = QSXS;
        }

        public String getKYZT() {
            return KYZT;
        }

        public void setKYZT(String KYZT) {
            this.KYZT = KYZT;
        }

        public String getSYZT() {
            return SYZT;
        }

        public void setSYZT(String SYZT) {
            this.SYZT = SYZT;
        }

        public String getXZ() {
            return XZ;
        }

        public void setXZ(String XZ) {
            this.XZ = XZ;
        }

        public String getCZYID() {
            return CZYID;
        }

        public void setCZYID(String CZYID) {
            this.CZYID = CZYID;
        }

        public String getSHZT() {
            return SHZT;
        }

        public void setSHZT(String SHZT) {
            this.SHZT = SHZT;
        }

        public String getZPWJURL() {
            return ZPWJURL;
        }

        public void setZPWJURL(String ZPWJURL) {
            this.ZPWJURL = ZPWJURL;
        }

        public double getGIS_X() {
            return GIS_X;
        }

        public void setGIS_X(double GIS_X) {
            this.GIS_X = GIS_X;
        }

        public double getGIS_Y() {
            return GIS_Y;
        }

        public void setGIS_Y(double GIS_Y) {
            this.GIS_Y = GIS_Y;
        }

        public double getGIS_Z() {
            return GIS_Z;
        }

        public void setGIS_Z(double GIS_Z) {
            this.GIS_Z = GIS_Z;
        }

        public Object getGLID() {
            return GLID;
        }

        public void setGLID(Object GLID) {
            this.GLID = GLID;
        }

        public String getSSLDID() {
            return SSLDID;
        }

        public void setSSLDID(String SSLDID) {
            this.SSLDID = SSLDID;
        }

        public String getJZSJ() {
            return JZSJ;
        }

        public void setJZSJ(String JZSJ) {
            this.JZSJ = JZSJ;
        }

        public String getQYSJ() {
            return QYSJ;
        }

        public void setQYSJ(String QYSJ) {
            this.QYSJ = QYSJ;
        }

        public String getFWTD() {
            return FWTD;
        }

        public void setFWTD(String FWTD) {
            this.FWTD = FWTD;
        }

        public String getFWTN() {
            return FWTN;
        }

        public void setFWTN(String FWTN) {
            this.FWTN = FWTN;
        }

        public String getFWTX() {
            return FWTX;
        }

        public void setFWTX(String FWTX) {
            this.FWTX = FWTX;
        }

        public String getFWTB() {
            return FWTB;
        }

        public void setFWTB(String FWTB) {
            this.FWTB = FWTB;
        }

        public Object getBLGXD() {
            return BLGXD;
        }

        public void setBLGXD(Object BLGXD) {
            this.BLGXD = BLGXD;
        }

        public Object getBLGXN() {
            return BLGXN;
        }

        public void setBLGXN(Object BLGXN) {
            this.BLGXN = BLGXN;
        }

        public Object getBLGXX() {
            return BLGXX;
        }

        public void setBLGXX(Object BLGXX) {
            this.BLGXX = BLGXX;
        }

        public Object getBLGXB() {
            return BLGXB;
        }

        public void setBLGXB(Object BLGXB) {
            this.BLGXB = BLGXB;
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

        public String getYWXTBSID() {
            return YWXTBSID;
        }

        public void setYWXTBSID(String YWXTBSID) {
            this.YWXTBSID = YWXTBSID;
        }

        public Object getISSUBMIT() {
            return ISSUBMIT;
        }

        public void setISSUBMIT(Object ISSUBMIT) {
            this.ISSUBMIT = ISSUBMIT;
        }

        public Object getISCONFIRM() {
            return ISCONFIRM;
        }

        public void setISCONFIRM(Object ISCONFIRM) {
            this.ISCONFIRM = ISCONFIRM;
        }

        public String getDATAFROM() {
            return DATAFROM;
        }

        public void setDATAFROM(String DATAFROM) {
            this.DATAFROM = DATAFROM;
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

        public Object getREMARKS() {
            return REMARKS;
        }

        public void setREMARKS(Object REMARKS) {
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

    @Override
    public String toString() {
        return "Hydrant{" +
                "type='" + type + '\'' +
                ", id='" + id + '\'' +
                ", geometry=" + geometry +
                ", geometry_name='" + geometry_name + '\'' +
                ", properties=" + properties +
                '}';
    }
}
