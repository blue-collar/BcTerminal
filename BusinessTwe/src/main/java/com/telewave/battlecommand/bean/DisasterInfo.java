package com.telewave.battlecommand.bean;

import java.io.Serializable;

public class DisasterInfo implements Serializable {


    /**
     * id : 8059d6a1a0ac4f4e9ad305b51a243a8e
     * isNewRecord : false
     * bjsj : 2019-07-20 10:58:45
     * bjr :
     * xqzdjgdm : {"id":"61a4eab7fc864513ac60b797fc539f3a","isNewRecord":false,"name":"新余市消防支队经济开发区大队经济开发区中队","sort":30,"type":"2","address":"","range":0,"ordernoInt":0,"jgqzInt":0,"parentId":"0"}
     * zqlx : {"isNewRecord":true,"dictId":"","codeValue":"10000","codeName":"火灾扑救","dictsourceType":0}
     * zhdj : {"isNewRecord":true,"dictId":"","codeValue":"1","codeName":"一级","dictsourceType":0}
     * zhdd : 江西省新余市渝水区东兴路
     * gisX : 114.99667118
     * gisY : 27.86130965
     * geom : POINT(114.99667118 27.86130965)
     * groupId : g80001
     * extend : {"isNewRecord":true,"smogstatus":{"isNewRecord":true,"dictId":"","codeValue":"","codeName":"","dictsourceType":0},"firematterclass":{"isNewRecord":true,"dictId":"","codeValue":"","codeName":"","dictsourceType":0}}
     */

    private String id;
    private boolean isNewRecord;
    private String bjsj;
    private String bjr;
    private XqzdjgdmBean xqzdjgdm;
    private ZqlxBean zqlx;
    private ZhdjBean zhdj;
    private String zhdd;
    private String gisX = "";
    private String gisY = "";
    private String geom;
    private String groupId;
//    private ExtendBean extend;


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

    public String getBjsj() {
        return bjsj;
    }

    public void setBjsj(String bjsj) {
        this.bjsj = bjsj;
    }

    public String getBjr() {
        return bjr;
    }

    public void setBjr(String bjr) {
        this.bjr = bjr;
    }

    public XqzdjgdmBean getXqzdjgdm() {
        return xqzdjgdm;
    }

    public void setXqzdjgdm(XqzdjgdmBean xqzdjgdm) {
        this.xqzdjgdm = xqzdjgdm;
    }

    public ZqlxBean getZqlx() {
        return zqlx;
    }

    public void setZqlx(ZqlxBean zqlx) {
        this.zqlx = zqlx;
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

    public String getGeom() {
        return geom;
    }

    public void setGeom(String geom) {
        this.geom = geom;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

//    public ExtendBean getExtend() {
//        return extend;
//    }
//
//    public void setExtend(ExtendBean extend) {
//        this.extend = extend;
//    }

    public static class XqzdjgdmBean implements Serializable {
        /**
         * id : 61a4eab7fc864513ac60b797fc539f3a
         * isNewRecord : false
         * name : 新余市消防支队经济开发区大队经济开发区中队
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

//    public static class ExtendBean implements Serializable{
//        /**
//         * isNewRecord : true
//         * smogstatus : {"isNewRecord":true,"dictId":"","codeValue":"","codeName":"","dictsourceType":0}
//         * firematterclass : {"isNewRecord":true,"dictId":"","codeValue":"","codeName":"","dictsourceType":0}
//         */
//
//        private boolean isNewRecord;
//        private SmogstatusBean smogstatus;
//        private FirematterclassBean firematterclass;
//
//        public boolean isIsNewRecord() {
//            return isNewRecord;
//        }
//
//        public void setIsNewRecord(boolean isNewRecord) {
//            this.isNewRecord = isNewRecord;
//        }
//
//        public SmogstatusBean getSmogstatus() {
//            return smogstatus;
//        }
//
//        public void setSmogstatus(SmogstatusBean smogstatus) {
//            this.smogstatus = smogstatus;
//        }
//
//        public FirematterclassBean getFirematterclass() {
//            return firematterclass;
//        }
//
//        public void setFirematterclass(FirematterclassBean firematterclass) {
//            this.firematterclass = firematterclass;
//        }
//
//        public static class SmogstatusBean implements Serializable{
//            /**
//             * isNewRecord : true
//             * dictId :
//             * codeValue :
//             * codeName :
//             * dictsourceType : 0
//             */
//
//            private boolean isNewRecord;
//            private String dictId;
//            private String codeValue;
//            private String codeName;
//            private int dictsourceType;
//
//            public boolean isIsNewRecord() {
//                return isNewRecord;
//            }
//
//            public void setIsNewRecord(boolean isNewRecord) {
//                this.isNewRecord = isNewRecord;
//            }
//
//            public String getDictId() {
//                return dictId;
//            }
//
//            public void setDictId(String dictId) {
//                this.dictId = dictId;
//            }
//
//            public String getCodeValue() {
//                return codeValue;
//            }
//
//            public void setCodeValue(String codeValue) {
//                this.codeValue = codeValue;
//            }
//
//            public String getCodeName() {
//                return codeName;
//            }
//
//            public void setCodeName(String codeName) {
//                this.codeName = codeName;
//            }
//
//            public int getDictsourceType() {
//                return dictsourceType;
//            }
//
//            public void setDictsourceType(int dictsourceType) {
//                this.dictsourceType = dictsourceType;
//            }
//        }
//
//        public static class FirematterclassBean implements Serializable{
//            /**
//             * isNewRecord : true
//             * dictId :
//             * codeValue :
//             * codeName :
//             * dictsourceType : 0
//             */
//
//            private boolean isNewRecord;
//            private String dictId;
//            private String codeValue;
//            private String codeName;
//            private int dictsourceType;
//
//            public boolean isIsNewRecord() {
//                return isNewRecord;
//            }
//
//            public void setIsNewRecord(boolean isNewRecord) {
//                this.isNewRecord = isNewRecord;
//            }
//
//            public String getDictId() {
//                return dictId;
//            }
//
//            public void setDictId(String dictId) {
//                this.dictId = dictId;
//            }
//
//            public String getCodeValue() {
//                return codeValue;
//            }
//
//            public void setCodeValue(String codeValue) {
//                this.codeValue = codeValue;
//            }
//
//            public String getCodeName() {
//                return codeName;
//            }
//
//            public void setCodeName(String codeName) {
//                this.codeName = codeName;
//            }
//
//            public int getDictsourceType() {
//                return dictsourceType;
//            }
//
//            public void setDictsourceType(int dictsourceType) {
//                this.dictsourceType = dictsourceType;
//            }
//        }
//    }
}
