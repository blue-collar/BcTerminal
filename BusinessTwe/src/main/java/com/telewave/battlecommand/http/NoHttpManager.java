package com.telewave.battlecommand.http;

import android.content.Context;

import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.download.DownloadQueue;
import com.yanzhenjie.nohttp.rest.Request;
import com.yanzhenjie.nohttp.rest.RequestQueue;

/**
 * Created by wc on 2016/11/4.
 */

public class NoHttpManager {


    private static NoHttpManager noHttpManager;

    /**
     * 请求队列.
     */
    private RequestQueue requestQueue;

    /**
     * 下载队列.
     */
    private static DownloadQueue downloadQueue;

    private NoHttpManager() {
        requestQueue = NoHttp.newRequestQueue();
    }

    /**
     * 请求队列.
     */
    public synchronized static NoHttpManager getRequestInstance() {
        if (noHttpManager == null)
            noHttpManager = new NoHttpManager();
        return noHttpManager;
    }

    /**
     * 下载队列.
     */
    public static DownloadQueue getDownloadInstance() {
        if (downloadQueue == null)
            downloadQueue = NoHttp.newDownloadQueue();
        return downloadQueue;
    }

    /**
     * 添加一个请求到请求队列.
     *
     * @param context   context用来实例化dialog.
     * @param what      用来标志请求, 当多个请求使用同一个{@link HttpListener}时, 在回调方法中会返回这个what.
     * @param request   请求对象.
     * @param callback  结果回调对象.
     * @param canCancel 是否允许用户取消请求.
     * @param isLoading 是否显示dialog.
     */
    public <T> void add(Context context, int what, Request<T> request, HttpListener<T> callback, boolean canCancel, boolean isLoading) {
        requestQueue.add(what, request, new HttpResponseListener<T>(context, request, callback, canCancel, isLoading));
    }

    /**
     * 取消这个sign标记的所有请求.
     */
    public void cancelBySign(Object sign) {
        requestQueue.cancelBySign(sign);
    }

    /**
     * 退出app时停止所有请求.
     */
    public void stopAll() {
        requestQueue.stop();
    }

}
