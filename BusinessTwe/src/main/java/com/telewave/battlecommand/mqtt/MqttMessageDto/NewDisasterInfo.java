package com.telewave.battlecommand.mqtt.MqttMessageDto;

import java.io.Serializable;
import java.util.List;

/**
 * @author liwh
 * @date 2018/12/27
 */
public class NewDisasterInfo extends BaseMessage {

    /**
     * MsgContent : {"cdd":{"cddbm":"D540101201811010067434005","dpbs":"2","dpfaid":"f4c307ef690b44079dad57e7359ecfdf","dpwz":[{"actiontime":"2018-12-27 11:11:52","cddid":"94e2b9502275446f9193c97ede65cc3e","wzdh":"13687421368","wzdz":"广东省深圳市南山区高新中三道2-东门","wzid":"c36f443b940e486f9779c381e7e08f71","wzmc":"大昭寺微型消防站1号","wzssjg":"5de68c2bdf0544f880bbb741a8194c9b"}],"fightgraph":"","fsdwdm":"","fssj":"2018-12-27 11:11:52","id":"94e2b9502275446f9193c97ede65cc3e","jlzt":1,"jsdwdm":"c36f443b940e486f9779c381e7e08f71","notice":"","procseat":"","procuserid":"","zquuid":"5d49c6a1a549481d98711d5c5bd2f123"},"cmd":"syncdispatchinfo","msgid":"1d5a052f5d6347e29077b127fb4415a7","organid":"a91670095afe4801a1255361bcf3d0fb","senderseatno":"","sendersyscode":"","zq":{"acceptseat":"","acceptuserid":"","alarmid":"5d49c6a1a549481d98711d5c5bd2f123","alarmsource":"","arcflag":0,"bdssrs":0,"bdswrs":0,"beacon":"","bjfs":"人工报警","bjsj":"2018-11-01 02:16:03","bkrs":373471,"bsdwdm":"0c91e4525db542ed933d0d2e6b0f5c3d","cwjjclbs":"null","cwjsmdxfjg":"","czdxdm":"","distributingarea":"","firematterclass":"","gis_h":0,"gis_x":0,"gis_y":0,"ifsb":0,"incepttype":"","is_graveness_case":"","jlzt":1,"lasj":"2018-11-01 02:16:03","limitarea":"","lxdh":"457374","lxr":"620994","procseat":"","procuserid":"","qhlc":0,"rsmj":0,"selectcarflag":0,"smogstatus":"","ssrs":0,"substation":"","swrs":0,"tagreinforce":false,"tagreqreinforce":false,"tagsendmsg":false,"tagsendwd":false,"tarrecwd":false,"xqzdjgdm":"f8b32efde2cd4feb9a8f0e106f955417","xqzdjgmc":"特勤一中队","xzqydm":"540101","zddwid":"e19cf4d6d37c45c3ade0613ae8308d72","zddwmc":"拉萨吉祥宝马宾馆","zhcsdm":"","zhdd":"西藏自治区拉萨市拉萨市林廓北路二十四巷","zhdjdm":"3","zqbm":"5401012018110100674","zqbs":1,"zqjssj":"","zqlxdm":"30000","zqlydwdm":"0c91e4525db542ed933d0d2e6b0f5c3d","zqms":"其他206549","zqxldm":"","zqzt":"02"}}
     */

    private MsgContentBean MsgContent;

    public MsgContentBean getMsgContent() {
        return MsgContent;
    }

    public void setMsgContent(MsgContentBean MsgContent) {
        this.MsgContent = MsgContent;
    }

    public static class MsgContentBean implements Serializable {
        /**
         * cdd : {"cddbm":"D540101201811010067434005","dpbs":"2","dpfaid":"f4c307ef690b44079dad57e7359ecfdf","dpwz":[{"actiontime":"2018-12-27 11:11:52","cddid":"94e2b9502275446f9193c97ede65cc3e","wzdh":"13687421368","wzdz":"广东省深圳市南山区高新中三道2-东门","wzid":"c36f443b940e486f9779c381e7e08f71","wzmc":"大昭寺微型消防站1号","wzssjg":"5de68c2bdf0544f880bbb741a8194c9b"}],"fightgraph":"","fsdwdm":"","fssj":"2018-12-27 11:11:52","id":"94e2b9502275446f9193c97ede65cc3e","jlzt":1,"jsdwdm":"c36f443b940e486f9779c381e7e08f71","notice":"","procseat":"","procuserid":"","zquuid":"5d49c6a1a549481d98711d5c5bd2f123"}
         * cmd : syncdispatchinfo
         * msgid : 1d5a052f5d6347e29077b127fb4415a7
         * organid : a91670095afe4801a1255361bcf3d0fb
         * senderseatno :
         * sendersyscode :
         * zq : {"acceptseat":"","acceptuserid":"","alarmid":"5d49c6a1a549481d98711d5c5bd2f123","alarmsource":"","arcflag":0,"bdssrs":0,"bdswrs":0,"beacon":"","bjfs":"人工报警","bjsj":"2018-11-01 02:16:03","bkrs":373471,"bsdwdm":"0c91e4525db542ed933d0d2e6b0f5c3d","cwjjclbs":"null","cwjsmdxfjg":"","czdxdm":"","distributingarea":"","firematterclass":"","gis_h":0,"gis_x":0,"gis_y":0,"ifsb":0,"incepttype":"","is_graveness_case":"","jlzt":1,"lasj":"2018-11-01 02:16:03","limitarea":"","lxdh":"457374","lxr":"620994","procseat":"","procuserid":"","qhlc":0,"rsmj":0,"selectcarflag":0,"smogstatus":"","ssrs":0,"substation":"","swrs":0,"tagreinforce":false,"tagreqreinforce":false,"tagsendmsg":false,"tagsendwd":false,"tarrecwd":false,"xqzdjgdm":"f8b32efde2cd4feb9a8f0e106f955417","xqzdjgmc":"特勤一中队","xzqydm":"540101","zddwid":"e19cf4d6d37c45c3ade0613ae8308d72","zddwmc":"拉萨吉祥宝马宾馆","zhcsdm":"","zhdd":"西藏自治区拉萨市拉萨市林廓北路二十四巷","zhdjdm":"3","zqbm":"5401012018110100674","zqbs":1,"zqjssj":"","zqlxdm":"30000","zqlydwdm":"0c91e4525db542ed933d0d2e6b0f5c3d","zqms":"其他206549","zqxldm":"","zqzt":"02"}
         */

        private CddBean cdd;
        private String cmd;
        private String msgid;
        private String organid;
        private String senderseatno;
        private String sendersyscode;
        private ZqBean zq;

        public CddBean getCdd() {
            return cdd;
        }

        public void setCdd(CddBean cdd) {
            this.cdd = cdd;
        }

        public String getCmd() {
            return cmd;
        }

        public void setCmd(String cmd) {
            this.cmd = cmd;
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

        public ZqBean getZq() {
            return zq;
        }

        public void setZq(ZqBean zq) {
            this.zq = zq;
        }

        public static class CddBean implements Serializable {
            /**
             * cddbm : D540101201811010067434005
             * dpbs : 2
             * dpfaid : f4c307ef690b44079dad57e7359ecfdf
             * dpwz : [{"actiontime":"2018-12-27 11:11:52","cddid":"94e2b9502275446f9193c97ede65cc3e","wzdh":"13687421368","wzdz":"广东省深圳市南山区高新中三道2-东门","wzid":"c36f443b940e486f9779c381e7e08f71","wzmc":"大昭寺微型消防站1号","wzssjg":"5de68c2bdf0544f880bbb741a8194c9b"}]
             * fightgraph :
             * fsdwdm :
             * fssj : 2018-12-27 11:11:52
             * id : 94e2b9502275446f9193c97ede65cc3e
             * jlzt : 1
             * jsdwdm : c36f443b940e486f9779c381e7e08f71
             * notice :
             * procseat :
             * procuserid :
             * zquuid : 5d49c6a1a549481d98711d5c5bd2f123
             */

            private String cddbm;
            private String dpbs;
            private String dpfaid;
            private String fightgraph;
            private String fsdwdm;
            private String fssj;
            private String id;
            private int jlzt;
            private String jsdwdm;
            private String notice;
            private String procseat;
            private String procuserid;
            private String zquuid;
            private List<DpwzBean> dpwz;

            public String getCddbm() {
                return cddbm;
            }

            public void setCddbm(String cddbm) {
                this.cddbm = cddbm;
            }

            public String getDpbs() {
                return dpbs;
            }

            public void setDpbs(String dpbs) {
                this.dpbs = dpbs;
            }

            public String getDpfaid() {
                return dpfaid;
            }

            public void setDpfaid(String dpfaid) {
                this.dpfaid = dpfaid;
            }

            public String getFightgraph() {
                return fightgraph;
            }

            public void setFightgraph(String fightgraph) {
                this.fightgraph = fightgraph;
            }

            public String getFsdwdm() {
                return fsdwdm;
            }

            public void setFsdwdm(String fsdwdm) {
                this.fsdwdm = fsdwdm;
            }

            public String getFssj() {
                return fssj;
            }

            public void setFssj(String fssj) {
                this.fssj = fssj;
            }

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public int getJlzt() {
                return jlzt;
            }

            public void setJlzt(int jlzt) {
                this.jlzt = jlzt;
            }

            public String getJsdwdm() {
                return jsdwdm;
            }

            public void setJsdwdm(String jsdwdm) {
                this.jsdwdm = jsdwdm;
            }

            public String getNotice() {
                return notice;
            }

            public void setNotice(String notice) {
                this.notice = notice;
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

            public String getZquuid() {
                return zquuid;
            }

            public void setZquuid(String zquuid) {
                this.zquuid = zquuid;
            }

            public List<DpwzBean> getDpwz() {
                return dpwz;
            }

            public void setDpwz(List<DpwzBean> dpwz) {
                this.dpwz = dpwz;
            }

            public static class DpwzBean implements Serializable {
                /**
                 * actiontime : 2018-12-27 11:11:52
                 * cddid : 94e2b9502275446f9193c97ede65cc3e
                 * wzdh : 13687421368
                 * wzdz : 广东省深圳市南山区高新中三道2-东门
                 * wzid : c36f443b940e486f9779c381e7e08f71
                 * wzmc : 大昭寺微型消防站1号
                 * wzssjg : 5de68c2bdf0544f880bbb741a8194c9b
                 */

                private String actiontime;
                private String cddid;
                private String wzdh;
                private String wzdz;
                private String wzid;
                private String wzmc;
                private String wzssjg;

                public String getActiontime() {
                    return actiontime;
                }

                public void setActiontime(String actiontime) {
                    this.actiontime = actiontime;
                }

                public String getCddid() {
                    return cddid;
                }

                public void setCddid(String cddid) {
                    this.cddid = cddid;
                }

                public String getWzdh() {
                    return wzdh;
                }

                public void setWzdh(String wzdh) {
                    this.wzdh = wzdh;
                }

                public String getWzdz() {
                    return wzdz;
                }

                public void setWzdz(String wzdz) {
                    this.wzdz = wzdz;
                }

                public String getWzid() {
                    return wzid;
                }

                public void setWzid(String wzid) {
                    this.wzid = wzid;
                }

                public String getWzmc() {
                    return wzmc;
                }

                public void setWzmc(String wzmc) {
                    this.wzmc = wzmc;
                }

                public String getWzssjg() {
                    return wzssjg;
                }

                public void setWzssjg(String wzssjg) {
                    this.wzssjg = wzssjg;
                }
            }
        }

        public static class ZqBean implements Serializable {
            /**
             * acceptseat :
             * acceptuserid :
             * alarmid : 5d49c6a1a549481d98711d5c5bd2f123
             * alarmsource :
             * arcflag : 0
             * bdssrs : 0
             * bdswrs : 0
             * beacon :
             * bjfs : 人工报警
             * bjsj : 2018-11-01 02:16:03
             * bkrs : 373471
             * bsdwdm : 0c91e4525db542ed933d0d2e6b0f5c3d
             * cwjjclbs : null
             * cwjsmdxfjg :
             * czdxdm :
             * distributingarea :
             * firematterclass :
             * gis_h : 0.0
             * gis_x : 0.0
             * gis_y : 0.0
             * ifsb : 0
             * incepttype :
             * is_graveness_case :
             * jlzt : 1
             * lasj : 2018-11-01 02:16:03
             * limitarea :
             * lxdh : 457374
             * lxr : 620994
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
             * xqzdjgdm : f8b32efde2cd4feb9a8f0e106f955417
             * xqzdjgmc : 特勤一中队
             * xzqydm : 540101
             * zddwid : e19cf4d6d37c45c3ade0613ae8308d72
             * zddwmc : 拉萨吉祥宝马宾馆
             * zhcsdm :
             * zhdd : 西藏自治区拉萨市拉萨市林廓北路二十四巷
             * zhdjdm : 3
             * zqbm : 5401012018110100674
             * zqbs : 1
             * zqjssj :
             * zqlxdm : 30000
             * zqlydwdm : 0c91e4525db542ed933d0d2e6b0f5c3d
             * zqms : 其他206549
             * zqxldm :
             * zqzt : 02
             */

            private String acceptseat;
            private String acceptuserid;
            private String alarmid;
            private String alarmsource;
            private int arcflag;
            private int bdssrs;
            private int bdswrs;
            private String beacon;
            private String bjfs;
            private String bjsj;
            private int bkrs;
            private String bsdwdm;
            private String cwjjclbs;
            private String cwjsmdxfjg;
            private String czdxdm;
            private String distributingarea;
            private String firematterclass;
            private double gis_h;
            private double gis_x;
            private double gis_y;
            private int ifsb;
            private String incepttype;
            private String is_graveness_case;
            private int jlzt;
            private String lasj;
            private String limitarea;
            private String lxdh;
            private String lxr;
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
            private String xzqydm;
            private String zddwid;
            private String zddwmc;
            private String zhcsdm;
            private String zhdd;
            private String zhdjdm;
            private String zqbm;
            private int zqbs;
            private String zqjssj;
            private String zqlxdm;
            private String zqlydwdm;
            private String zqms;
            private String zqxldm;
            private String zqzt;

            public String getAcceptseat() {
                return acceptseat;
            }

            public void setAcceptseat(String acceptseat) {
                this.acceptseat = acceptseat;
            }

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

            public String getAlarmsource() {
                return alarmsource;
            }

            public void setAlarmsource(String alarmsource) {
                this.alarmsource = alarmsource;
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

            public String getBeacon() {
                return beacon;
            }

            public void setBeacon(String beacon) {
                this.beacon = beacon;
            }

            public String getBjfs() {
                return bjfs;
            }

            public void setBjfs(String bjfs) {
                this.bjfs = bjfs;
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

            public String getBsdwdm() {
                return bsdwdm;
            }

            public void setBsdwdm(String bsdwdm) {
                this.bsdwdm = bsdwdm;
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

            public String getDistributingarea() {
                return distributingarea;
            }

            public void setDistributingarea(String distributingarea) {
                this.distributingarea = distributingarea;
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

            public String getIs_graveness_case() {
                return is_graveness_case;
            }

            public void setIs_graveness_case(String is_graveness_case) {
                this.is_graveness_case = is_graveness_case;
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

            public String getLimitarea() {
                return limitarea;
            }

            public void setLimitarea(String limitarea) {
                this.limitarea = limitarea;
            }

            public String getLxdh() {
                return lxdh;
            }

            public void setLxdh(String lxdh) {
                this.lxdh = lxdh;
            }

            public String getLxr() {
                return lxr;
            }

            public void setLxr(String lxr) {
                this.lxr = lxr;
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

            public String getXzqydm() {
                return xzqydm;
            }

            public void setXzqydm(String xzqydm) {
                this.xzqydm = xzqydm;
            }

            public String getZddwid() {
                return zddwid;
            }

            public void setZddwid(String zddwid) {
                this.zddwid = zddwid;
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

            public String getZqlydwdm() {
                return zqlydwdm;
            }

            public void setZqlydwdm(String zqlydwdm) {
                this.zqlydwdm = zqlydwdm;
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
}
