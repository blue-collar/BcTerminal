package com.telewave.battlecommand.bean;

import java.util.List;

/**
 * 上传文件返回实体类
 *
 * @author liwh
 * @date 2018/12/29
 */
public class UploadFileReturnBean {

    /**
     * code : 0
     * success : true
     * uploads : [{"context":"voice","path":"/voice/20181229/ta36jg0FxOTiCPvv/chat1546061215020.amr","name":"chat1546061215020.amr","state":1,"size":4646}]
     */

    private int code;
    private boolean success;
    private List<UploadsBean> uploads;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public List<UploadsBean> getUploads() {
        return uploads;
    }

    public void setUploads(List<UploadsBean> uploads) {
        this.uploads = uploads;
    }

    public static class UploadsBean {
        /**
         * context : voice
         * path : /voice/20181229/ta36jg0FxOTiCPvv/chat1546061215020.amr
         * name : chat1546061215020.amr
         * state : 1
         * size : 4646
         */

        private String context;
        private String path;
        private String name;
        private int state;
        private int size;

        public String getContext() {
            return context;
        }

        public void setContext(String context) {
            this.context = context;
        }

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getState() {
            return state;
        }

        public void setState(int state) {
            this.state = state;
        }

        public int getSize() {
            return size;
        }

        public void setSize(int size) {
            this.size = size;
        }

        @Override
        public String toString() {
            return "UploadsBean{" +
                    "context='" + context + '\'' +
                    ", path='" + path + '\'' +
                    ", name='" + name + '\'' +
                    ", state=" + state +
                    ", size=" + size +
                    '}';
        }
    }
}
