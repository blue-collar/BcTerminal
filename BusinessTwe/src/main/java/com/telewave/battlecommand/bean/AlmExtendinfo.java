package com.telewave.battlecommand.bean;

import java.util.Date;

public class AlmExtendinfo {

    private String alarmid;        // 警情ID
    private String alarmsource;        // 警情来源1：电话，2：短信，3：人工报警，4：跨区域接口输入，5：110转入，6：技防
    private String refid;        // 警情来源关联ID,根据ALARMSOURCE而定1：电话id，2：短信id，4：跨区域接口输入，5：110转入id，6：技防记录id
    private String isGravenessCase;        // 重大警情
    //private String incepttype;			// 灾害判定
//    private SysDictDetail incepttype;	//灾害判定
    private String customtype1;        // 自定义分类1
    private String customtype2;        // 自定义分类2
    //    private Office substation;		// 主管支队
    private String distributingarea;        // 环线分布
    private String limitarea;        // 限放区
    private String beacon;        // 烟花引起

    //private String smogstatus;		// 烟雾情况
    //private String firematterclass;		// 燃烧物资
//    private SysDictDetail smogstatus;		// 烟雾情况
//    private SysDictDetail firematterclass;		// 燃烧物资

    private String selectcarflag;        // 派车标志
    private String tagsendmsg;        // 是否已发送短信
    private String tagreqreinforce;        // 是否已发送增援请求
    private String tagreinforce;        // 是否已接收支援
    private String tagsendwd;        // 是否已发送文电
    private String tagrecwd;        // 是否已接收文电
    private String acceptuserid;        // 接警员ID
    private String acceptseat;        // 接警坐席
    private String procuserid;        // 处警员ID
    private String procseat;        // 处警席位
    private String arcflag;        // 归档标志0：未归档1：已经归档（该警情有关数据不可修改）
    private Date sjc;        // 时间戳
    private String isreportedFlag;        // 0--未上报，1--已上报
    private String ldg;                // 路灯杆
    private String mph;                // 门牌号
    private String lkyw;            // 两客一危
    private String bjdz;            // 报警地址
    //private String zhcsdm;			// 灾害场所代码
    //private String zqxldm;			// 灾情细类字典代码
//    private SysDictDetail zhcs;    //灾害场所
//    private SysDictDetail zqxl;    //灾情细类
}
