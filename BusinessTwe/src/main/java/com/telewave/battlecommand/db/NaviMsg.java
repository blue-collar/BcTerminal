package com.telewave.battlecommand.db;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Property;

@Entity   //  用于标识这是一个需要Greendao帮我们生成代码的bean
public class NaviMsg {

    @Id(autoincrement = true)
    private Long id;

    @Property(nameInDb = "startAddress")
    private String startAddress;

    @Property(nameInDb = "startLat")
    private double startLat;

    @Property(nameInDb = "startLon")
    private double startLon;

    @Property(nameInDb = "endAddress")
    private String endAddress;

    @Property(nameInDb = "endLat")
    private double endLat;

    @Property(nameInDb = "endLon")
    private double endLon;


    public NaviMsg() {
    }

    public NaviMsg(String startAddress, double startLat, double startLon, String endAddress, double endLat, double endLon) {
        this.startAddress = startAddress;
        this.startLat = startLat;
        this.startLon = startLon;
        this.endAddress = endAddress;
        this.endLat = endLat;
        this.endLon = endLon;
    }

    @Generated(hash = 1108838116)
    public NaviMsg(Long id, String startAddress, double startLat, double startLon,
                   String endAddress, double endLat, double endLon) {
        this.id = id;
        this.startAddress = startAddress;
        this.startLat = startLat;
        this.startLon = startLon;
        this.endAddress = endAddress;
        this.endLat = endLat;
        this.endLon = endLon;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStartAddress() {
        return startAddress;
    }

    public void setStartAddress(String startAddress) {
        this.startAddress = startAddress;
    }

    public double getStartLat() {
        return startLat;
    }

    public void setStartLat(double startLat) {
        this.startLat = startLat;
    }

    public double getStartLon() {
        return startLon;
    }

    public void setStartLon(double startLon) {
        this.startLon = startLon;
    }

    public String getEndAddress() {
        return endAddress;
    }

    public void setEndAddress(String endAddress) {
        this.endAddress = endAddress;
    }

    public double getEndLat() {
        return endLat;
    }

    public void setEndLat(double endLat) {
        this.endLat = endLat;
    }

    public double getEndLon() {
        return endLon;
    }

    public void setEndLon(double endLon) {
        this.endLon = endLon;
    }

    @Override
    public String toString() {
        return "NaviMsg{" +
                "id=" + id +
                ", startAddress='" + startAddress + '\'' +
                ", startLat=" + startLat +
                ", startLon=" + startLon +
                ", endAddress='" + endAddress + '\'' +
                ", endLat=" + endLat +
                ", endLon=" + endLon +
                '}';
    }
}
