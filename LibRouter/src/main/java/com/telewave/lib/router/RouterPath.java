package com.telewave.lib.router;


public interface RouterPath {
    String LOGIN_MAIN_PATH = "/login/main";
    String LOGIN_FIRST_PATH = "/login/first";
    String OTA_MAIN_PATH = "/ota/main";
    String SPLASH_ACTIVITY_PATH = "/app/activity/splash";

    public interface Flag {
        /*** 是否需要登陆 flag***/
        int FLAG_NEED_LOGIN = 1;
    }

    public interface BundleKey {
        /*** 是否需要登陆 intent key***/
        String KEY_NEED_LOGIN = "needLogin";
        /**
         * 是否需要实名认证 intent key
         *
         * @deprecated 这是旧的字段，为了做兼容保留，新的请用KEY_NEED_CERT
         **/
        String KEY_NEED_IDENTITY = "needIdentity";
    }

    interface Path {
        String PATH_INTERCEPTOR_ACTIVITY = "/tweInterceptor/router/activity";
    }
}
