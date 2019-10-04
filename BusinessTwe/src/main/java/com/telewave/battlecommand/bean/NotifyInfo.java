package com.telewave.battlecommand.bean;

/**
 * 通知实体类
 *
 * @author liwh
 * @date 2019/1/14
 */
public class NotifyInfo {

    /**
     * id : 8824a6ee2cf34cc1a800d2e8665a853c
     * isNewRecord : false
     * remarks : 通知测试的备注信息哟喂
     * createDate : 2019-01-04 11:16:04
     * updateDate : 2019-01-04 11:16:04
     * sendTime : 2019-01-04 11:15:59
     * title : 通知测试
     * content : 通知测试的一个测试内容啊呀呀呀呀呀嘿，
     * 啊呀呀呀呀
     * user : {"id":"1","isNewRecord":false,"name":"系统管理员","loginFlag":"1","admin":true,"roleNames":""}
     * organname:西藏自治区公安消防总队
     */

    private String id;
    private boolean isNewRecord;
    private String remarks;
    private String createDate;
    private String updateDate;
    private String sendTime;
    private String title;
    private String content;
    private UserBean user;
    private String organname;

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

    public String getSendTime() {
        return sendTime;
    }

    public void setSendTime(String sendTime) {
        this.sendTime = sendTime;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public UserBean getUser() {
        return user;
    }

    public void setUser(UserBean user) {
        this.user = user;
    }

    public static class UserBean {
        /**
         * id : 1
         * isNewRecord : false
         * name : 系统管理员
         * loginFlag : 1
         * admin : true
         * roleNames :
         */

        private String id;
        private boolean isNewRecord;
        private String name;
        private String loginFlag;
        private boolean admin;
        private String roleNames;

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

        public String getLoginFlag() {
            return loginFlag;
        }

        public void setLoginFlag(String loginFlag) {
            this.loginFlag = loginFlag;
        }

        public boolean isAdmin() {
            return admin;
        }

        public void setAdmin(boolean admin) {
            this.admin = admin;
        }

        public String getRoleNames() {
            return roleNames;
        }

        public void setRoleNames(String roleNames) {
            this.roleNames = roleNames;
        }
    }

    public String getOrganname() {
        return organname;
    }

    public void setOrganname(String organname) {
        this.organname = organname;
    }
}
