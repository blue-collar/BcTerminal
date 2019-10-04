package com.telewave.battlecommand.imui.bean;

import java.io.Serializable;

/**
 * IM 数据解析基类
 *
 * @author PF-NAN
 * @date 2019-07-22
 */
public class IMParseBaseObj<T> implements Serializable {
    public String code;
    public String msg;
    public T data;

}
