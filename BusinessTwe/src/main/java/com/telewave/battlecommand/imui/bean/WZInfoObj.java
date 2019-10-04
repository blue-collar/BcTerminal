package com.telewave.battlecommand.imui.bean;

import java.util.List;

/**
 * @author PF-NAN
 * @date 2019-08-02
 */
public class WZInfoObj {

//                    "id":"042425b2be8743188000e3958086426e",
//                    "isNewRecord":false,
//                    "xfzmc":"新余市体育中心微型消防站",
//                    "xfzdz":"陕西省西安市雁塔区科技六路198号",
//                    "xzqhdm":"360500",
//                    "fddbr":"",
//                    "fddbrdh":"",
//                    "xfaqfzr":"",
//                    "xfaqfzrdh":"",
//                    "xfaqglr":"",
//                    "xfaqglrdh":"",
//                    "kzsdh":"",
//                    "isSelected":false,


    public String id;
    public boolean isNewRecord;
    public String xfzmc;
    public String xfzdz;
    public String xzqhdm;
    public String fddbr;
    public String fddbrdh;
    public String xfaqfzr;
    public String xfaqfzrdh;
    public String xfaqglr;
    public String xfaqglrdh;
    public String kzsdh;
    public boolean isSelected;
    public List<MfsListObj> mfsList;

    public class MfsListObj {
//"id":"520aa64a283b4969a44b59ab25af07cc",
//        "isNewRecord":false,
//        "loginName":"18700937810",
//        "name":"刘姣姣",
//        "mobilephone":"18700937810",
//        "isLeader":"0",
//        "postdutyId":"",
//        "postduty":"",
//        "isAllowDelete":false


        public String id;
        public boolean isNewRecord;
        public String loginName;
        public String name;
        public String officephone;
        public String mobilephone;
        public String isLeader;
        public String postdutyId;
        public String postduty;
        public String online;
        public boolean isAllowDelete;


    }

}
