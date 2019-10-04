package com.telewave.battlecommand.mqtt.MqttMessageDto;

import java.io.Serializable;

public class CallPoliceMessage extends BaseMessage implements Serializable {

    /**
     * cmd : syncalarminfo
     * detachmentId : d6d40ebf20f74a96ab83147a90f8cc37
     * msgid : 932838b5c7864ac3ae9ab50c28b6a17f
     * organid : 241371a3d5874132b3dabd7925e0f9e1
     * senderseatno :
     * sendersyscode :
     * wzid : 042425b2be8743188000e3958086426e
     * zq : {"acceptuserid":"","alarmid":"d9b20c664c0d42c4a95a8f7b38429fa5","arcflag":0,"bdssrs":0,"bdswrs":0,"bjdh":"13759856111","bjfs":"视频报警","bjr":"微站测试用户","bjsj":"2019-07-22 20:07:13","bkrs":10,"cwjjclbs":"null","cwjsmdxfjg":"","czdxdm":"","dcsj":"","firematterclass":"","gis_h":0,"gis_x":108.889284,"gis_y":34.228175,"ifsb":0,"incepttype":"","jlzt":1,"lasj":"","new_gis_h":0,"new_gis_x":0,"new_gis_y":0,"procseat":"","procuserid":"","qhlc":0,"rsmj":0,"selectcarflag":0,"smogstatus":"","ssrs":0,"substation":"","swrs":0,"tagreinforce":false,"tagreqreinforce":false,"tagsendmsg":false,"tagsendwd":false,"tarrecwd":false,"xqzdjgdm":"241371a3d5874132b3dabd7925e0f9e1","xqzdjgmc":"新余市消防支队渝水区大队平安路中队","xqzdjgname":"平安路中队","xzqydm":"360500","zddwmc":"大方地市公司","zhcsdm":"","zhdd":"大方地市公司1楼","zhdjdm":"3","zqbm":"20190722200713819","zqbs":1,"zqjssj":"","zqlxdm":"1000","zqms":"电器爆炸燃烧","zqxldm":"","zqzt":"01"}
     */

    private String cmd;
    private String detachmentId;
    private String msgid;
    private String organid;
    private String senderseatno;
    private String sendersyscode;
    private String wzid;
    private ZqBean zq;

    public CallPoliceMessage() {
        this.setMsgType(MessageType.RECEIVE_CALL_POLICE_MESSAGE);
    }

    public String getCmd() {
        return cmd;
    }

    public void setCmd(String cmd) {
        this.cmd = cmd;
    }

    public String getDetachmentId() {
        return detachmentId;
    }

    public void setDetachmentId(String detachmentId) {
        this.detachmentId = detachmentId;
    }

    public String getMsgid() {
        return msgid;
    }

    public void setMsgid(String msgid) {
        this.msgid = msgid;
    }

    public String getOrganid() {
        return organid;
    }

    public void setOrganid(String organid) {
        this.organid = organid;
    }

    public String getSenderseatno() {
        return senderseatno;
    }

    public void setSenderseatno(String senderseatno) {
        this.senderseatno = senderseatno;
    }

    public String getSendersyscode() {
        return sendersyscode;
    }

    public void setSendersyscode(String sendersyscode) {
        this.sendersyscode = sendersyscode;
    }

    public String getWzid() {
        return wzid;
    }

    public void setWzid(String wzid) {
        this.wzid = wzid;
    }

    public ZqBean getZq() {
        return zq;
    }

    public void setZq(ZqBean zq) {
        this.zq = zq;
    }

    public static class ZqBean implements Serializable {
        /**
         * acceptuserid :
         * alarmid : d9b20c664c0d42c4a95a8f7b38429fa5
         * arcflag : 0
         * bdssrs : 0
         * bdswrs : 0
         * bjdh : 13759856111
         * bjfs : 视频报警
         * bjr : 微站测试用户
         * bjsj : 2019-07-22 20:07:13
         * bkrs : 10
         * cwjjclbs : null
         * cwjsmdxfjg :
         * czdxdm :
         * dcsj :
         * firematterclass :
         * gis_h : 0.0
         * gis_x : 108.889284
         * gis_y : 34.228175
         * ifsb : 0
         * incepttype :
         * jlzt : 1
         * lasj :
         * new_gis_h : 0.0
         * new_gis_x : 0.0
         * new_gis_y : 0.0
         * procseat :
         * procuserid :
         * qhlc : 0
         * rsmj : 0.0
         * selectcarflag : 0
         * smogstatus :
         * ssrs : 0
         * substation :
         * swrs : 0
         * tagreinforce : false
         * tagreqreinforce : false
         * tagsendmsg : false
         * tagsendwd : false
         * tarrecwd : false
         * xqzdjgdm : 241371a3d5874132b3dabd7925e0f9e1
         * xqzdjgmc : 新余市消防支队渝水区大队平安路中队
         * xqzdjgname : 平安路中队
         * xzqydm : 360500
         * zddwmc : 大方地市公司
         * zhcsdm :
         * zhdd : 大方地市公司1楼
         * zhdjdm : 3
         * zqbm : 20190722200713819
         * zqbs : 1
         * zqjssj :
         * zqlxdm : 1000
         * zqms : 电器爆炸燃烧
         * zqxldm :
         * zqzt : 01
         */

        private String acceptuserid;
        private String alarmid;
        private int arcflag;
        private int bdssrs;
        private int bdswrs;
        private String bjdh;
        private String bjfs;
        private String bjr;
        private String bjsj;
        private int bkrs;
        private String cwjjclbs;
        private String cwjsmdxfjg;
        private String czdxdm;
        private String dcsj;
        private String firematterclass;
        private double gis_h;
        private double gis_x;
        private double gis_y;
        private int ifsb;
        private String incepttype;
        private int jlzt;
        private String lasj;
        private double new_gis_h;
        private double new_gis_x;
        private double new_gis_y;
        private String procseat;
        private String procuserid;
        private int qhlc;
        private double rsmj;
        private int selectcarflag;
        private String smogstatus;
        private int ssrs;
        private String substation;
        private int swrs;
        private boolean tagreinforce;
        private boolean tagreqreinforce;
        private boolean tagsendmsg;
        private boolean tagsendwd;
        private boolean tarrecwd;
        private String xqzdjgdm;
        private String xqzdjgmc;
        private String xqzdjgname;
        private String xzqydm;
        private String zddwmc;
        private String zhcsdm;
        private String zhdd;
        private String zhdjdm;
        private String zqbm;
        private int zqbs;
        private String zqjssj;
        private String zqlxdm;
        private String zqms;
        private String zqxldm;
        private String zqzt;

        public String getAcceptuserid() {
            return acceptuserid;
        }

        public void setAcceptuserid(String acceptuserid) {
            this.acceptuserid = acceptuserid;
        }

        public String getAlarmid() {
            return alarmid;
        }

        public void setAlarmid(String alarmid) {
            this.alarmid = alarmid;
        }

        public int getArcflag() {
            return arcflag;
        }

        public void setArcflag(int arcflag) {
            this.arcflag = arcflag;
        }

        public int getBdssrs() {
            return bdssrs;
        }

        public void setBdssrs(int bdssrs) {
            this.bdssrs = bdssrs;
        }

        public int getBdswrs() {
            return bdswrs;
        }

        public void setBdswrs(int bdswrs) {
            this.bdswrs = bdswrs;
        }

        public String getBjdh() {
            return bjdh;
        }

        public void setBjdh(String bjdh) {
            this.bjdh = bjdh;
        }

        public String getBjfs() {
            return bjfs;
        }

        public void setBjfs(String bjfs) {
            this.bjfs = bjfs;
        }

        public String getBjr() {
            return bjr;
        }

        public void setBjr(String bjr) {
            this.bjr = bjr;
        }

        public String getBjsj() {
            return bjsj;
        }

        public void setBjsj(String bjsj) {
            this.bjsj = bjsj;
        }

        public int getBkrs() {
            return bkrs;
        }

        public void setBkrs(int bkrs) {
            this.bkrs = bkrs;
        }

        public String getCwjjclbs() {
            return cwjjclbs;
        }

        public void setCwjjclbs(String cwjjclbs) {
            this.cwjjclbs = cwjjclbs;
        }

        public String getCwjsmdxfjg() {
            return cwjsmdxfjg;
        }

        public void setCwjsmdxfjg(String cwjsmdxfjg) {
            this.cwjsmdxfjg = cwjsmdxfjg;
        }

        public String getCzdxdm() {
            return czdxdm;
        }

        public void setCzdxdm(String czdxdm) {
            this.czdxdm = czdxdm;
        }

        public String getDcsj() {
            return dcsj;
        }

        public void setDcsj(String dcsj) {
            this.dcsj = dcsj;
        }

        public String getFirematterclass() {
            return firematterclass;
        }

        public void setFirematterclass(String firematterclass) {
            this.firematterclass = firematterclass;
        }

        public double getGis_h() {
            return gis_h;
        }

        public void setGis_h(double gis_h) {
            this.gis_h = gis_h;
        }

        public double getGis_x() {
            return gis_x;
        }

        public void setGis_x(double gis_x) {
            this.gis_x = gis_x;
        }

        public double getGis_y() {
            return gis_y;
        }

        public void setGis_y(double gis_y) {
            this.gis_y = gis_y;
        }

        public int getIfsb() {
            return ifsb;
        }

        public void setIfsb(int ifsb) {
            this.ifsb = ifsb;
        }

        public String getIncepttype() {
            return incepttype;
        }

        public void setIncepttype(String incepttype) {
            this.incepttype = incepttype;
        }

        public int getJlzt() {
            return jlzt;
        }

        public void setJlzt(int jlzt) {
            this.jlzt = jlzt;
        }

        public String getLasj() {
            return lasj;
        }

        public void setLasj(String lasj) {
            this.lasj = lasj;
        }

        public double getNew_gis_h() {
            return new_gis_h;
        }

        public void setNew_gis_h(double new_gis_h) {
            this.new_gis_h = new_gis_h;
        }

        public double getNew_gis_x() {
            return new_gis_x;
        }

        public void setNew_gis_x(double new_gis_x) {
            this.new_gis_x = new_gis_x;
        }

        public double getNew_gis_y() {
            return new_gis_y;
        }

        public void setNew_gis_y(double new_gis_y) {
            this.new_gis_y = new_gis_y;
        }

        public String getProcseat() {
            return procseat;
        }

        public void setProcseat(String procseat) {
            this.procseat = procseat;
        }

        public String getProcuserid() {
            return procuserid;
        }

        public void setProcuserid(String procuserid) {
            this.procuserid = procuserid;
        }

        public int getQhlc() {
            return qhlc;
        }

        public void setQhlc(int qhlc) {
            this.qhlc = qhlc;
        }

        public double getRsmj() {
            return rsmj;
        }

        public void setRsmj(double rsmj) {
            this.rsmj = rsmj;
        }

        public int getSelectcarflag() {
            return selectcarflag;
        }

        public void setSelectcarflag(int selectcarflag) {
            this.selectcarflag = selectcarflag;
        }

        public String getSmogstatus() {
            return smogstatus;
        }

        public void setSmogstatus(String smogstatus) {
            this.smogstatus = smogstatus;
        }

        public int getSsrs() {
            return ssrs;
        }

        public void setSsrs(int ssrs) {
            this.ssrs = ssrs;
        }

        public String getSubstation() {
            return substation;
        }

        public void setSubstation(String substation) {
            this.substation = substation;
        }

        public int getSwrs() {
            return swrs;
        }

        public void setSwrs(int swrs) {
            this.swrs = swrs;
        }

        public boolean isTagreinforce() {
            return tagreinforce;
        }

        public void setTagreinforce(boolean tagreinforce) {
            this.tagreinforce = tagreinforce;
        }

        public boolean isTagreqreinforce() {
            return tagreqreinforce;
        }

        public void setTagreqreinforce(boolean tagreqreinforce) {
            this.tagreqreinforce = tagreqreinforce;
        }

        public boolean isTagsendmsg() {
            return tagsendmsg;
        }

        public void setTagsendmsg(boolean tagsendmsg) {
            this.tagsendmsg = tagsendmsg;
        }

        public boolean isTagsendwd() {
            return tagsendwd;
        }

        public void setTagsendwd(boolean tagsendwd) {
            this.tagsendwd = tagsendwd;
        }

        public boolean isTarrecwd() {
            return tarrecwd;
        }

        public void setTarrecwd(boolean tarrecwd) {
            this.tarrecwd = tarrecwd;
        }

        public String getXqzdjgdm() {
            return xqzdjgdm;
        }

        public void setXqzdjgdm(String xqzdjgdm) {
            this.xqzdjgdm = xqzdjgdm;
        }

        public String getXqzdjgmc() {
            return xqzdjgmc;
        }

        public void setXqzdjgmc(String xqzdjgmc) {
            this.xqzdjgmc = xqzdjgmc;
        }

        public String getXqzdjgname() {
            return xqzdjgname;
        }

        public void setXqzdjgname(String xqzdjgname) {
            this.xqzdjgname = xqzdjgname;
        }

        public String getXzqydm() {
            return xzqydm;
        }

        public void setXzqydm(String xzqydm) {
            this.xzqydm = xzqydm;
        }

        public String getZddwmc() {
            return zddwmc;
        }

        public void setZddwmc(String zddwmc) {
            this.zddwmc = zddwmc;
        }

        public String getZhcsdm() {
            return zhcsdm;
        }

        public void setZhcsdm(String zhcsdm) {
            this.zhcsdm = zhcsdm;
        }

        public String getZhdd() {
            return zhdd;
        }

        public void setZhdd(String zhdd) {
            this.zhdd = zhdd;
        }

        public String getZhdjdm() {
            return zhdjdm;
        }

        public void setZhdjdm(String zhdjdm) {
            this.zhdjdm = zhdjdm;
        }

        public String getZqbm() {
            return zqbm;
        }

        public void setZqbm(String zqbm) {
            this.zqbm = zqbm;
        }

        public int getZqbs() {
            return zqbs;
        }

        public void setZqbs(int zqbs) {
            this.zqbs = zqbs;
        }

        public String getZqjssj() {
            return zqjssj;
        }

        public void setZqjssj(String zqjssj) {
            this.zqjssj = zqjssj;
        }

        public String getZqlxdm() {
            return zqlxdm;
        }

        public void setZqlxdm(String zqlxdm) {
            this.zqlxdm = zqlxdm;
        }

        public String getZqms() {
            return zqms;
        }

        public void setZqms(String zqms) {
            this.zqms = zqms;
        }

        public String getZqxldm() {
            return zqxldm;
        }

        public void setZqxldm(String zqxldm) {
            this.zqxldm = zqxldm;
        }

        public String getZqzt() {
            return zqzt;
        }

        public void setZqzt(String zqzt) {
            this.zqzt = zqzt;
        }
    }
}
