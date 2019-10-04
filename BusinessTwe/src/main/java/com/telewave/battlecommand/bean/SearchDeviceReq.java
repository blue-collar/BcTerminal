package com.telewave.battlecommand.bean;

public class SearchDeviceReq {
    /**
     * organname : 新余市消防支队渝水区大队平安路中队
     * ssxfjgid : 241371a3d5874132b3dabd7925e0f9e1
     * zbmc : 压缩空气泡沫消防车
     * gisx : 114.918599
     * gisy : 27.8371366
     * distance : 100
     */

    private String organname;
    private String ssxfjgid;
    private String zbmc;
    private double gisx;
    private double gisy;
    private String distance;

    public String getOrganname() {
        return organname;
    }

    public void setOrganname(String organname) {
        this.organname = organname;
    }

    public String getSsxfjgid() {
        return ssxfjgid;
    }

    public void setSsxfjgid(String ssxfjgid) {
        this.ssxfjgid = ssxfjgid;
    }

    public String getZbmc() {
        return zbmc;
    }

    public void setZbmc(String zbmc) {
        this.zbmc = zbmc;
    }

    public double getGisx() {
        return gisx;
    }

    public void setGisx(double gisx) {
        this.gisx = gisx;
    }

    public double getGisy() {
        return gisy;
    }

    public void setGisy(double gisy) {
        this.gisy = gisy;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }
}
