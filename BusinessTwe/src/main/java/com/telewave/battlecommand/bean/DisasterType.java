package com.telewave.battlecommand.bean;


public class DisasterType {

    /**
     * isNewRecord : true
     * bjr :
     * extend : {"isNewRecord":true,"smogstatus":{"isNewRecord":true,"dictId":"","codeValue":"","codeName":"","dictsourceType":0},"firematterclass":{"isNewRecord":true,"dictId":"","codeValue":"","codeName":"","dictsourceType":0}}
     * countWja : 3
     * countYja : 0
     * countHzpj : 3
     * countQxjy : 0
     * countShjz : 0
     */

    private boolean isNewRecord;
    private String bjr;
    private ExtendBean extend;
    private int countWja;
    private int countYja;
    private int countHzpj;
    private int countQxjy;
    private int countShjz;

    public boolean isIsNewRecord() {
        return isNewRecord;
    }

    public void setIsNewRecord(boolean isNewRecord) {
        this.isNewRecord = isNewRecord;
    }

    public String getBjr() {
        return bjr;
    }

    public void setBjr(String bjr) {
        this.bjr = bjr;
    }

    public ExtendBean getExtend() {
        return extend;
    }

    public void setExtend(ExtendBean extend) {
        this.extend = extend;
    }

    public int getCountWja() {
        return countWja;
    }

    public void setCountWja(int countWja) {
        this.countWja = countWja;
    }

    public int getCountYja() {
        return countYja;
    }

    public void setCountYja(int countYja) {
        this.countYja = countYja;
    }

    public int getCountHzpj() {
        return countHzpj;
    }

    public void setCountHzpj(int countHzpj) {
        this.countHzpj = countHzpj;
    }

    public int getCountQxjy() {
        return countQxjy;
    }

    public void setCountQxjy(int countQxjy) {
        this.countQxjy = countQxjy;
    }

    public int getCountShjz() {
        return countShjz;
    }

    public void setCountShjz(int countShjz) {
        this.countShjz = countShjz;
    }

    public static class ExtendBean {
        /**
         * isNewRecord : true
         * smogstatus : {"isNewRecord":true,"dictId":"","codeValue":"","codeName":"","dictsourceType":0}
         * firematterclass : {"isNewRecord":true,"dictId":"","codeValue":"","codeName":"","dictsourceType":0}
         */

        private boolean isNewRecord;
        private SmogstatusBean smogstatus;
        private FirematterclassBean firematterclass;

        public boolean isIsNewRecord() {
            return isNewRecord;
        }

        public void setIsNewRecord(boolean isNewRecord) {
            this.isNewRecord = isNewRecord;
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

        public static class SmogstatusBean {
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

        public static class FirematterclassBean {
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
    }
}
