package com.telewave.lib.log;

/**
 * @Author: rick_tan
 * @Date: 19-7-20
 * @Version: v1.0
 * @Des 日志上传的抽象接口
 * @Modify On 2019-xx-xx by *** for reason ...
 */
public interface IUploadLogUIStatCallBack {

    void onUploadStarted(int resId);

    void onUploadProgressUpdated(int resId, int progress);

    void onUploadEnd(int resId, String result);
}
