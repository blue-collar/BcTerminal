package com.telewave.battlecommand.bean;

/**
 * 机构信息实体类
 *
 * @author liwh
 * @date 2019/1/8
 */
public class OrganInfo {

    /**
     * sname : 日喀则支队
     * name : 日喀则地区公安消防支队
     * pid : a91670095afe4801a1255361bcf3d0fb
     * id : 069e8164b117474d931a98cec84ff17b
     * type : 1
     */

    private String sname;
    private String name;
    private String pid;
    private String id;
    private String type;


    public String getSname() {
        return sname;
    }

    public void setSname(String sname) {
        this.sname = sname;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
