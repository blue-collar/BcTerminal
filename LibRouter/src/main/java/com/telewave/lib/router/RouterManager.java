package com.telewave.lib.router;

import android.app.Application;

import com.alibaba.android.arouter.launcher.ARouter;
import com.telewave.lib.router.interceptor.InterceptorDepend;

/**
 * @Author: rick_tan
 * @Date: 19-7-29
 * @Version: v1.0
 * @Des RouterManager
 */
public class RouterManager {
    /**
     * 初始化路由
     */
    public static void initARouter(Application application, boolean debug) {
        if (debug) {
            ARouter.openLog();     // 打印日志
            ARouter.openDebug();   // 开启调试模式(如果在InstantRun模式下运行，必须开启调试模式！线上版本需要关闭,否则有安全风险)
        }
        ARouter.init(application); // 尽可能早，推荐在Application中初始化
    }

    /**
     * 销毁路由
     */
    public static void destroyRouter() {
        ARouter.getInstance().destroy();
    }

    private InterceptorDepend interceptorDepend = new InterceptorDepend() {
    };

    public void setInterceptorDepend(InterceptorDepend interceptorDepend) {
        this.interceptorDepend = interceptorDepend;
    }


    public InterceptorDepend getInterceptorDepend() {
        return interceptorDepend;
    }

    private final static class SingleHolder {
        private static final RouterManager ROUTER_MANAGER = new RouterManager();
    }

    public static RouterManager instance() {
        return SingleHolder.ROUTER_MANAGER;
    }

    private RouterManager() {
    }
}
