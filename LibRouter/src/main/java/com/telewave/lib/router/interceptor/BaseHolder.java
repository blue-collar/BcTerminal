package com.telewave.lib.router.interceptor;

import com.alibaba.android.arouter.facade.Postcard;
import com.alibaba.android.arouter.facade.callback.InterceptorCallback;

/**
 * @Author: rick_tan
 * @Date: 19-7-29
 * @Version: v1.0
 * @Des BaseHolder
 */
public class BaseHolder {
    public InterceptorCallback getCallback() {
        return callback;
    }

    public Postcard getPostcard() {
        return postcard;
    }

    public void setPostcard(Postcard postcard) {
        this.postcard = postcard;
    }

    public void setCallback(InterceptorCallback callback) {
        this.callback = callback;
    }

    private Postcard postcard;
    private InterceptorCallback callback;

    public void clear() {
        postcard = null;
        callback = null;
    }
}
