package com.telewave.battlecommand.bean;

import java.util.List;

/**
 * 政策规范实体类
 *
 * @author liwh
 * @date 2019/1/9
 */
public class PolicyGuiFan {

    /**
     * id : 036883f689e24c5e96ce479ede40d58b
     * isNewRecord : false
     * remarks :
     * createDate : 2018-12-20 17:13:00
     * updateDate : 2018-12-20 18:59:06
     * title : 政策法规测试
     * content : 是是是是是是是
     * fileList : [{"id":"10ba111511e54baf8570d788bd58a1e8","isNewRecord":false,"type":"01","savepath":"/twmfs/20181220/EjHuhLM4xHIgMM7c/测试2.docx","filename":"测试2.docx","updatetime":"2018-12-20 18:59:06"}]
     */

    private String id;
    private boolean isNewRecord;
    private String remarks;
    private String createDate;
    private String updateDate;
    private String title;
    private String content;
    private List<FileListBean> fileList;

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

    public List<FileListBean> getFileList() {
        return fileList;
    }

    public void setFileList(List<FileListBean> fileList) {
        this.fileList = fileList;
    }

    public static class FileListBean {
        /**
         * id : 10ba111511e54baf8570d788bd58a1e8
         * isNewRecord : false
         * type : 01
         * savepath : /twmfs/20181220/EjHuhLM4xHIgMM7c/测试2.docx
         * filename : 测试2.docx
         * updatetime : 2018-12-20 18:59:06
         */

        private String id;
        private boolean isNewRecord;
        private String type;
        private String savepath;
        private String filename;
        private String updatetime;

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

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getSavepath() {
            return savepath;
        }

        public void setSavepath(String savepath) {
            this.savepath = savepath;
        }

        public String getFilename() {
            return filename;
        }

        public void setFilename(String filename) {
            this.filename = filename;
        }

        public String getUpdatetime() {
            return updatetime;
        }

        public void setUpdatetime(String updatetime) {
            this.updatetime = updatetime;
        }
    }
}
