package com.telewave.battlecommand.http;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.telewave.lib.base.util.ToastUtils;
import com.yanzhenjie.nohttp.error.NetworkError;
import com.yanzhenjie.nohttp.error.NotFoundCacheError;
import com.yanzhenjie.nohttp.error.ServerError;
import com.yanzhenjie.nohttp.error.StorageReadWriteError;
import com.yanzhenjie.nohttp.error.StorageSpaceNotEnoughError;
import com.yanzhenjie.nohttp.error.TimeoutError;
import com.yanzhenjie.nohttp.error.URLError;
import com.yanzhenjie.nohttp.error.UnKnownHostError;


/**
 * Created by LiJing on 2017/2/21.
 */

public class HttpResponseUtil {
    public static void showResponse(Context context, Exception exception, String notice) {
        if (exception instanceof ServerError) {// 服务器错误
            ToastUtils.toastShortMessage("服务器异常。" + notice);
        } else if (exception instanceof NetworkError) {// 网络不好
            ToastUtils.toastShortMessage("网络不好，请检查网络。" + notice);
        } else if (exception instanceof TimeoutError) {// 请求超时
            ToastUtils.toastShortMessage("请求超时，网络不好或者服务器不稳定。" + notice);
        } else if (exception instanceof UnKnownHostError) {// 找不到服务器
            ToastUtils.toastShortMessage("未发现指定服务器。" + notice);
        } else if (exception instanceof URLError) {// URL是错的
            ToastUtils.toastShortMessage("URL错误。" + notice);
        } else if (exception instanceof NotFoundCacheError) {
            // 这个异常只会在仅仅查找缓存时没有找到缓存时返回
            ToastUtils.toastShortMessage("没有发现缓存。" + notice);
        } else {
            ToastUtils.toastShortMessage("未知错误。" + notice);
        }
    }

    public static void downloadErrorNotice(Exception exception, Activity activity, String notice, boolean isBroadcast) {
        String message = "下载出错：";
        if (exception instanceof ServerError) {
            message += "服务器发生内部错误";
        } else if (exception instanceof NetworkError) {
            message += "网络不可用，检查网络";
        } else if (exception instanceof StorageReadWriteError) {
            message += "存储卡错误，检查存储卡";
        } else if (exception instanceof StorageSpaceNotEnoughError) {
            message += "存储位置空间不足";
        } else if (exception instanceof TimeoutError) {
            message += "下载超时";
        } else if (exception instanceof UnKnownHostError) {
            message += "服务器找不到";
        } else if (exception instanceof URLError) {
            message += "url地址错误";
        } else if (exception instanceof IllegalArgumentException) {
            message += "下载参数错误";
        } else {
            message += "未知错误";
        }
        ToastUtils.toastShortMessage(message + notice);
        if (isBroadcast) {
            Intent intent = new Intent();
            intent.setAction("isDownloadDBbroadcast");
            activity.sendBroadcast(intent);
        }
    }
}
