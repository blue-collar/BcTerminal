package com.telewave.battlecommand.bean;

import java.io.Serializable;

/**
 * 成员实体类
 *
 * @author liwh
 * @date 2018/12/20
 */
public class MemberInfo implements Serializable {

    /**
     * id : 4b663ca57dd8463c9c34a46d80bb069f
     * isNewRecord : false
     * remarks :
     * createDate : 2019-01-15 17:15:25
     * updateDate : 2019-03-01 18:32:47
     * firestation : {"id":"c36f443b940e486f9779c381e7e08f71","isNewRecord":false,"xfzmc":"大昭寺微型消防站1号","szdxfjg":{"id":"5de68c2bdf0544f880bbb741a8194c9b","isNewRecord":false,"name":"拉萨市公安消防支队大昭寺大队","sort":30,"type":"2","address":"","range":0,"ordernoInt":0,"jgqzInt":0,"parentId":"0"},"fddbr":"","fddbrdh":"","xfaqfzr":"","xfaqfzrdh":"","xfaqglr":"","xfaqglrdh":"","kzsdh":""}
     * loginName : sj1
     * password : ddd5d146ea52dd4392b3826d1bb905e0032fca8c59d1cf830a077a44
     * name : 盛杰一
     * sex : 0
     * age :
     * officephone :
     * mobilephone :
     * idnumber :
     * isLeader : 1
     * isLogin : 1
     * status : 1
     * postdutyId : cdcae17467b64947acec9b65d15f068b
     * postduty : 站长
     */

    private String id;
    private boolean isNewRecord;
    private String remarks;
    private String createDate;
    private String updateDate;
    private FirestationBean firestation;
    private String loginName;
    private String password;
    private String name;
    private String sex;
    private String age;
    private String officephone;
    private String mobilephone;
    private String idnumber;
    private String isLeader;
    private String isLogin;
    private String status;
    private String postdutyId;
    private String postduty;

    public MemberInfo() {
    }

    public MemberInfo(String id) {
        this.id = id;
    }

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

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
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

    public FirestationBean getFirestation() {
        return firestation;
    }

    public void setFirestation(FirestationBean firestation) {
        this.firestation = firestation;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getOfficephone() {
        return officephone;
    }

    public void setOfficephone(String officephone) {
        this.officephone = officephone;
    }

    public String getMobilephone() {
        return mobilephone;
    }

    public void setMobilephone(String mobilephone) {
        this.mobilephone = mobilephone;
    }

    public String getIdnumber() {
        return idnumber;
    }

    public void setIdnumber(String idnumber) {
        this.idnumber = idnumber;
    }

    public String getIsLeader() {
        return isLeader;
    }

    public void setIsLeader(String isLeader) {
        this.isLeader = isLeader;
    }

    public String getIsLogin() {
        return isLogin;
    }

    public void setIsLogin(String isLogin) {
        this.isLogin = isLogin;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPostdutyId() {
        return postdutyId;
    }

    public void setPostdutyId(String postdutyId) {
        this.postdutyId = postdutyId;
    }

    public String getPostduty() {
        return postduty;
    }

    public void setPostduty(String postduty) {
        this.postduty = postduty;
    }

    public static class FirestationBean implements Serializable {
        /**
         * id : c36f443b940e486f9779c381e7e08f71
         * isNewRecord : false
         * xfzmc : 大昭寺微型消防站1号
         * szdxfjg : {"id":"5de68c2bdf0544f880bbb741a8194c9b","isNewRecord":false,"name":"拉萨市公安消防支队大昭寺大队","sort":30,"type":"2","address":"","range":0,"ordernoInt":0,"jgqzInt":0,"parentId":"0"}
         * fddbr :
         * fddbrdh :
         * xfaqfzr :
         * xfaqfzrdh :
         * xfaqglr :
         * xfaqglrdh :
         * kzsdh :
         */

        private String id;
        private boolean isNewRecord;
        private String xfzmc;
        private SzdxfjgBean szdxfjg;
        private String fddbr;
        private String fddbrdh;
        private String xfaqfzr;
        private String xfaqfzrdh;
        private String xfaqglr;
        private String xfaqglrdh;
        private String kzsdh;

        public FirestationBean(String id) {
            this.id = id;
        }

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

        public String getXfzmc() {
            return xfzmc;
        }

        public void setXfzmc(String xfzmc) {
            this.xfzmc = xfzmc;
        }

        public SzdxfjgBean getSzdxfjg() {
            return szdxfjg;
        }

        public void setSzdxfjg(SzdxfjgBean szdxfjg) {
            this.szdxfjg = szdxfjg;
        }

        public String getFddbr() {
            return fddbr;
        }

        public void setFddbr(String fddbr) {
            this.fddbr = fddbr;
        }

        public String getFddbrdh() {
            return fddbrdh;
        }

        public void setFddbrdh(String fddbrdh) {
            this.fddbrdh = fddbrdh;
        }

        public String getXfaqfzr() {
            return xfaqfzr;
        }

        public void setXfaqfzr(String xfaqfzr) {
            this.xfaqfzr = xfaqfzr;
        }

        public String getXfaqfzrdh() {
            return xfaqfzrdh;
        }

        public void setXfaqfzrdh(String xfaqfzrdh) {
            this.xfaqfzrdh = xfaqfzrdh;
        }

        public String getXfaqglr() {
            return xfaqglr;
        }

        public void setXfaqglr(String xfaqglr) {
            this.xfaqglr = xfaqglr;
        }

        public String getXfaqglrdh() {
            return xfaqglrdh;
        }

        public void setXfaqglrdh(String xfaqglrdh) {
            this.xfaqglrdh = xfaqglrdh;
        }

        public String getKzsdh() {
            return kzsdh;
        }

        public void setKzsdh(String kzsdh) {
            this.kzsdh = kzsdh;
        }

        public static class SzdxfjgBean implements Serializable {
            /**
             * id : 5de68c2bdf0544f880bbb741a8194c9b
             * isNewRecord : false
             * name : 拉萨市公安消防支队大昭寺大队
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
    }
}
