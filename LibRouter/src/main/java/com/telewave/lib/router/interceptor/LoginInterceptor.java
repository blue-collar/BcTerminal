package com.telewave.lib.router.interceptor;

import android.content.Context;
import android.os.Bundle;

import com.alibaba.android.arouter.facade.Postcard;
import com.alibaba.android.arouter.facade.annotation.Interceptor;
import com.alibaba.android.arouter.facade.callback.InterceptorCallback;
import com.alibaba.android.arouter.facade.template.IInterceptor;
import com.telewave.lib.router.RouterManager;
import com.telewave.lib.router.RouterPath;

@Interceptor(priority = 1)
public class LoginInterceptor implements IInterceptor {

    @Override
    public void process(Postcard postcard, InterceptorCallback callback) {
        final Bundle bundle = postcard.getExtras();
        RouterManager.instance().getInterceptorDepend().beforeInterceptor(postcard.getPath(), bundle);

        /***是否需要登陆***/
        boolean needLogin = postcard.getExtra() == RouterPath.Flag.FLAG_NEED_LOGIN;
        if (!needLogin && postcard != null && bundle != null) {
            Object needLoginObj = bundle.get(RouterPath.BundleKey.KEY_NEED_LOGIN);
            if (needLoginObj != null) {
                if (needLoginObj instanceof Boolean) {
                    needLogin = (boolean) needLoginObj;
                } else if (needLoginObj instanceof String) {
                    if ("true".equals(((String) needLoginObj).trim().toLowerCase())) {
                        needLogin = true;
                    }
                }
            }
        }

        if (needLogin) {
            /***是否已经登陆***/
            boolean isLogin = RouterManager.instance().getInterceptorDepend().isLogin();
            if (isLogin) {
                callback.onContinue(postcard);
            } else {
                if (RouterManager.instance().getInterceptorDepend().enableInterceptorNotifyCallBack())
                    notifyCallBack(false);
                bundle.remove(RouterPath.BundleKey.KEY_NEED_LOGIN);
                RouterManager.instance().getInterceptorDepend().gotoLogin(postcard.getPath(), bundle);

                LoginHolder.instance().setPostcard(postcard);
                LoginHolder.instance().setCallback(callback);
            }
        } else {
            callback.onContinue(postcard);
        }
    }

    @Override
    public void init(Context context) {
        reset();
    }

    static void reset() {
        LoginHolder.instance().clear();
    }

    public static void notifyCallBack(boolean pass) {
        if (LoginHolder.instance().getPostcard() != null && LoginHolder.instance().getCallback() != null) {
            if (pass) {
                LoginHolder.instance().getCallback().onContinue(LoginHolder.instance().getPostcard());
            } else {
                LoginHolder.instance().getCallback().onInterrupt(null);
            }
            reset();
        }
    }
}
