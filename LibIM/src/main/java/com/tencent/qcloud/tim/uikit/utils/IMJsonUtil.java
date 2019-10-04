package com.tencent.qcloud.tim.uikit.utils;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * json数据解析
 *
 * @author nanPengFei
 */
public class IMJsonUtil {

    /**
     * 把一个map解析成为json字符串
     *
     * @param map
     * @return String
     */
    public static String parseMapToJson(Map<?, ?> map) {
        try {
            Gson gson = new Gson();
            return gson.toJson(map);
        } catch (Exception e) {
        }
        return null;
    }

    /**
     * 把一个json字符串解析成为对象
     *
     * @param json
     * @param cls
     * @return <T> T
     */
    public static <T> T parseJsonToBean(String json, Class<T> cls) {
        Gson gson = new Gson();
        T t = null;
        try {
            t = gson.fromJson(json, cls);
        } catch (Exception e) {
            try {
                GsonBuilder gb = new GsonBuilder();
                gson = gb.create();
                t = gson.fromJson(json, cls);
            } catch (JsonSyntaxException ex) {
                ex.printStackTrace();
            }
        }
        return t;
    }

    /**
     * 把json字符串解析成为map
     *
     * @param json
     * @return HashMap<String, Object>
     */
    public static HashMap<String, Object> parseJsonToMap(String json) {
        Gson gson = new Gson();
        Type type = new TypeToken<HashMap<String, Object>>() {
        }.getType();
        HashMap<String, Object> map = null;
        try {
            map = gson.fromJson(json, type);
        } catch (Exception e) {
        }
        return map;
    }

    /**
     * 把json字符串解析成为map
     *
     * @param json
     * @param type new TypeToken<HashMap<String, JsonObject>>(){}.getType()
     * @return HashMap<?, ?>
     */

    public static HashMap<?, ?> parseJsonToMap(String json, Type type) {
        Gson gson = new Gson();
        HashMap<?, ?> map = gson.fromJson(json, type);
        return map;
    }

    /**
     * 把json字符串解析成为集合 params: new TypeToken<List<yourbean>>(){}.getType(),
     *
     * @param json
     * @param type new TypeToken<List<yourbean>>(){}.getType()
     * @return List<?>
     */
    public static List<?> parseJsonToList(String json, Type type) {
        Gson gson = new Gson();
        List<?> list = gson.fromJson(json, type);
        return list;
    }

    /**
     * 把json字符串解析成为集合
     *
     * @param json  json字符串
     * @param clazz 集合中包含的java类
     * @return ArrayList<T>
     */
    public static <T> ArrayList<T> jsonToArrayList(String json, Class<T> clazz) {
        try {
            Type type = new TypeToken<ArrayList<JsonObject>>() {
            }.getType();
            ArrayList<JsonObject> jsonObjects = new Gson().fromJson(json, type);

            ArrayList<T> arrayList = null;
            if (jsonObjects != null) {
                arrayList = new ArrayList<>();
                for (JsonObject jsonObject : jsonObjects) {
                    arrayList.add(new Gson().fromJson(jsonObject, clazz));
                }
            }
            return arrayList;
        } catch (JsonSyntaxException e) {
            return null;
        }
    }

    /**
     * 把集合转换为JsonString
     *
     * @param lists java类集合
     */
    public static <T> String arrayListToJsonString(ArrayList<T> lists) {
        try {
            String jsonString = new Gson().toJson(lists);
            return jsonString;
        } catch (JsonSyntaxException e) {
            return "";
        }
    }

    /**
     * 获取json串中某个字段的值!注意:只能获取同一层级的value
     *
     * @param json
     * @param key
     * @return String
     */
    public static String getFieldValue(String json, String key) {
        if (TextUtils.isEmpty(json)) {
            return null;
        }
        if (!json.contains(key)) {
            return "";
        }
        JSONObject jsonObject = null;
        String value = null;
        try {
            jsonObject = new JSONObject(json);
            value = jsonObject.getString(key);
        } catch (JSONException e) {
        }
        return value;
    }


    /**
     * Object转换Json
     *
     * @param object
     * @return
     */
    public static String obj2Json(Object object) {
        String jsonStr;
        try {
            jsonStr = null == object ? "" : new Gson().toJson(object);
        } catch (Exception e) {
            e.printStackTrace();
            jsonStr = "";
        }
        return jsonStr;
    }


    /**
     * Json解析为List
     *
     * @param listJson
     * @param tClass
     * @param <T>
     * @return
     */
    public static <T> List<T> json2List(String listJson, Class<T> tClass) {
        try {
            Type type = new TypeToken<List<JsonObject>>() {
            }.getType();
            List<JsonObject> jsonObjects = new Gson().fromJson(listJson, type);
            List<T> arrayList = null;
            if (jsonObjects != null) {
                arrayList = new ArrayList<>();
                for (JsonObject jsonObject : jsonObjects) {
                    arrayList.add(new Gson().fromJson(jsonObject, tClass));
                }
            }
            return arrayList;
        } catch (Exception e) {
            return null;
        }
    }
}
