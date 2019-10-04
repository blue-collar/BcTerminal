package com.telewave.battlecommand.bean;

import java.util.List;

public class YuAnPlanBean {


    /**
     * code : 1
     * data : [{"officeId":"d6d40ebf20f74a96ab83147a90f8cc37","num":2,"name":"新余支队"}]
     * data1 : [{"officeId":"f57ab5f5973146ac8ec640426cf1ad44","num":2,"name":"下村中队"},{"officeId":"02e3ffa5d4924ea3955f2eee2c6e3e31","num":0,"name":"仙女湖中队"},{"officeId":"241371a3d5874132b3dabd7925e0f9e1","num":0,"name":"平安路中队"},{"officeId":"2b4558afd3e7440f842cc1fb47417523","num":0,"name":"袁河中队"},{"officeId":"59fd4a747b1b491cb860db4da465d7d8","num":0,"name":"龙池墅中队"},{"officeId":"61a4eab7fc864513ac60b797fc539f3a","num":0,"name":"经济开发区中队"},{"officeId":"99e0ac28ee864477b34e6cb43ded2aae","num":0,"name":"测试中队"},{"officeId":"a31bb79161d64942825789b0d9813622","num":0,"name":"分宜县中队"},{"officeId":"e5757f22f70543ea84b7c4d68f881df0","num":0,"name":"新亚新专职队"}]
     */

    private String code;
    private List<DataBean> data;
    private List<DataBean> data1;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public List<DataBean> getData1() {
        return data1;
    }

    public void setData1(List<DataBean> data1) {
        this.data1 = data1;
    }

    public static class DataBean {
        /**
         * officeId : d6d40ebf20f74a96ab83147a90f8cc37
         * num : 2
         * name : 新余支队
         */

        private String officeId;
        private int num;
        private String name;

        public String getOfficeId() {
            return officeId;
        }

        public void setOfficeId(String officeId) {
            this.officeId = officeId;
        }

        public int getNum() {
            return num;
        }

        public void setNum(int num) {
            this.num = num;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    public static class Data1Bean {
        /**
         * officeId : f57ab5f5973146ac8ec640426cf1ad44
         * num : 2
         * name : 下村中队
         */

        private String officeId;
        private int num;
        private String name;

        public String getOfficeId() {
            return officeId;
        }

        public void setOfficeId(String officeId) {
            this.officeId = officeId;
        }

        public int getNum() {
            return num;
        }

        public void setNum(int num) {
            this.num = num;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
