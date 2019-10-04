package com.telewave.battlecommand.bean;

import java.util.List;

/**
 * 案列实体类
 *
 * @author liwh
 * @date 2019/1/28
 */
public class ExampleBean {


    /**
     * id : 9a2a795056e94a1ca9e4964b34c71a59
     * isNewRecord : false
     * remarks :
     * createDate : 2019-01-24 09:47:22
     * updateDate : 2019-01-24 09:57:16
     * title : 测试在线打开PDF
     * content : 只是一个测试而已
     * fileList : [{"id":"6fd5fd36b728442aa6d46ac32adf39ef","isNewRecord":false,"type":"01","savepath":"/twmfs/20190124/Gmp9qKra7dCKzdkr/test.pdf","filename":"test.pdf","updatetime":"2019-01-24 09:57:16"}]
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
         * id : 6fd5fd36b728442aa6d46ac32adf39ef
         * isNewRecord : false
         * type : 01
         * savepath : /twmfs/20190124/Gmp9qKra7dCKzdkr/test.pdf
         * filename : test.pdf
         * updatetime : 2019-01-24 09:57:16
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
