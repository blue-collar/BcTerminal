package com.telewave.battlecommand.http;

import com.telewave.lib.base.util.LogToFile;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;


/**
 * 返回数据通用格式
 */
public class ResponseBean extends JSONObject {
    public final static String CODE = "code";
    public final static String MSG = "msg";
    public final static String DATA = "data";
    public final static String LIST = "list";
    public final static String DATA1 = "data1";

    public ResponseBean() {
        super();
    }

    public ResponseBean(JSONObject copyFrom, String[] names)
            throws JSONException {
        super(copyFrom, names);
    }

    public ResponseBean(JSONTokener readFrom) throws JSONException {
        super(readFrom);
    }

    public ResponseBean(String json) throws JSONException {
        super(json);
    }

    /**
     * 判断是否返回成功
     *
     * @return
     */
    public boolean isSuccess() {
        return getCode() == HttpCode.REQUEST_SUCCESS;
    }

    public boolean isFailure() {
        return getCode() == HttpCode.REQUEST_FAILURE;
    }

    /**
     * 获取CODE
     *
     * @return
     */
    public int getCode() {
        try {
            return Integer.parseInt(this.getString(CODE));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * 获取返回信息
     *
     * @return
     */
    public String getMsg() {
        try {
            return this.getString(MSG);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "";
    }

    public String getData() {
        String data = "";
        try {
            data = getString(DATA);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return data;
    }

    public String getData1() {
        String data = "";
        try {
            data = getString(DATA1);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return data;
    }

    public String getList() {
        String list = "";
        try {
            list = getString(LIST);
            LogToFile.i("toCheckUpdate", "getList: " + list);
        } catch (JSONException e) {
            e.printStackTrace();
            LogToFile.i("toCheckUpdate", "JSONException:" + e.getMessage().toString());
        }
        return list;
    }
}
