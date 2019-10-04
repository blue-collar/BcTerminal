package com.telewave.battlecommand.http;

import com.telewave.business.twe.R;
import com.telewave.lib.base.AppProxy;
import com.telewave.lib.base.util.ToastUtils;
import com.yanzhenjie.nohttp.error.NetworkError;
import com.yanzhenjie.nohttp.error.NotFoundCacheError;
import com.yanzhenjie.nohttp.error.TimeoutError;
import com.yanzhenjie.nohttp.error.URLError;
import com.yanzhenjie.nohttp.error.UnKnownHostError;

import java.net.ConnectException;
import java.net.ProtocolException;

import rx.Subscriber;

/**
 * Note:简单的订阅类
 * Created by Yuan on 2016/10/31,9:56.
 */

public abstract class SimpleRxSubscriber<T> extends Subscriber<T> {
    @Override
    public void onError(Throwable e) {
        dismissDialogOnError();
        OnErrorMessage();
        e.printStackTrace();
        if ("暂无数据".equals(e.getMessage())) {
            ToastUtils.toastShortMessage("暂无数据");
        } else if ("没有未结案灾情数据".equals(e.getMessage())) {
            ToastUtils.toastShortMessage("没有未结案灾情数据");
        } else if (e instanceof NullPointerException) {
            ToastUtils.toastShortMessage("数据异常");
        } else if (e instanceof NumberFormatException) {
//            ToastUtil.showMessage(MyApplication.getInstance(), "数据解析异常,无法在地图上标注");
        } else if (e instanceof ConnectException) {
            ToastUtils.toastShortMessage("服务器连接失败,可能服务器已经停用");
        }
        // 提示异常信息。
        else if (e instanceof NetworkError) {// 网络不好
            ToastUtils.toastShortMessage(AppProxy.getContext().getString(R.string.error_please_check_network));
        } else if (e instanceof TimeoutError) {// 请求超时
            ToastUtils.toastShortMessage(AppProxy.getContext().getString(R.string.error_timeout));
        } else if (e instanceof UnKnownHostError) {// 找不到服务器
            ToastUtils.toastShortMessage(AppProxy.getContext().getString(R.string.error_not_found_server));
        } else if (e instanceof URLError) {// URL是错的
            ToastUtils.toastShortMessage(AppProxy.getContext().getString(R.string.error_url_error));
        } else if (e instanceof NotFoundCacheError) {
            // 这个异常只会在仅仅查找缓存时没有找到缓存时返回
            ToastUtils.toastShortMessage(AppProxy.getContext().getString(R.string.error_not_found_cache));
        } else if (e instanceof ProtocolException) {
            ToastUtils.toastShortMessage(AppProxy.getContext().getString(R.string.error_system_unsupport_method));
        }
//        else if (e instanceof ParseError) {
//            ToastUtils.showMsg(MyApplication.getInstance(), R.string.error_parse_data_error);
//        }
        if (!this.isUnsubscribed()) {
            this.unsubscribe();
        }

    }

    /**
     * 取消对画框
     */
    protected abstract void dismissDialogOnError();

    protected void OnErrorMessage() {

    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onNext(T t) {

    }
}
