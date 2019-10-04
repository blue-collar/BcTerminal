package com.telewave.lib.router.interceptor;

import android.os.Bundle;

/**
 * @Author: rick_tan
 * @Date: 19-7-29
 * @Version: v1.0
 * @Des Interceptor
 */
public abstract class InterceptorDepend {
    public boolean enableInterceptorNotifyCallBack() {
        return true;
    }

    public boolean isLogin() {
        return true;
    }

    public void beforeInterceptor(String path, Bundle bundle) {

    }

    public void gotoLogin(String targetPath, Bundle targetBundle) {
    }
}
