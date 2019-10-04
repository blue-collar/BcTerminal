package com.telewave.lib.router.interceptor;

/**
 * @Author: rick_tan
 * @Date: 19-7-29
 * @Version: v1.0
 * @Des LoginHolder
 */
class LoginHolder extends BaseHolder {

    private LoginHolder() {

    }

    private final static class SingleHolder {
        private static final LoginHolder INTERCEPTOR_HOLDER = new LoginHolder();
    }

    public static LoginHolder instance() {
        return SingleHolder.INTERCEPTOR_HOLDER;
    }

}
