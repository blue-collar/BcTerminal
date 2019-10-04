package com.telewave.battlecommand.http;

/**
 * @author liwh
 * @date 2018/12/18
 */
public class HttpCode {

    //系统出错
    public final static int SYSTEM_ERROR = -1;
    //请求失败
    public final static int REQUEST_FAILURE = 0;
    //请求成功
    public final static int REQUEST_SUCCESS = 1;
    //用户信息过期
    public final static int USER_INFO_EXPIRES = 2;
    //用户不存在
    public final static int USER_NOT_EXIST = 3;
    //用户名或密码错误
    public final static int INCORRECT_USERNAME_OR_PASSWORD_ERROR = 4;
    //用户已绑定
    public final static int USER_HAVE_BOUND = 5;
    //终端已绑定
    public final static int TERMINAL_HAVE_BOUND = 6;
    //终端与用户不匹配
    public final static int TERMINAL_NOT_MATCH_USER = 7;
    //终端未注册
    public final static int TERMINAL_NOT_MATCH_REGISTER = 8;
    //国际移动用户识别码格式错误
    public final static int IDENTIFICATION_CODE_ERROR = 9;
    //用户已登出
    public final static int USER_LOG_OUT = 10;
}
