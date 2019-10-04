package com.telewave.battlecommand.http;


import com.yanzhenjie.nohttp.rest.Response;

/**
 * <p>接受回调结果.</p>
 * Created by wc on 2016/11/4.
 */

public interface HttpListener<T> {
    void onSucceed(int what, Response<T> response);

    void onFailed(int what, String url, Object tag, Exception exception, long networkMillis);

}
