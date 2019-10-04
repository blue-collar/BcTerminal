package com.telewave.lib.base.util;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * json解析工具类
 * Created by wc on 2016/10/26.
 */
public class ParseJsonUtils {

    private static JSONObject o;

    /**
     * @param s    json对象，数据类型：对象中只包含一个Json数组对象
     * @param list runnable类中传入的List对象，与具体的Entity类无关，因为在Runnable类的具体代码中负值给Entity类的集合对象
     * @param obj  list集合中存放的类型
     * @param cls  具体的Entity类的class对象
     * @return 返回object类型的集合
     */
    public static ArrayList<Object> getArrayJson(String s, ArrayList<Object> list, Object obj, Class<?> cls) {
        try {
            JSONObject jo = new JSONObject(s);
            JSONArray jsonArray = (JSONArray) jo.get("array");
            for (int i = 0; i < jsonArray.length(); ++i) {
                o = (JSONObject) jsonArray.get(i);
                Gson gson = new Gson();
                obj = gson.fromJson(o.toString(), cls);
                list.add(obj);
            }
            return list;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * json数据中只含有一个对象
     *
     * @param s         json字符串
     * @param cls       对象类
     * @param arrayName 含对象的节点名
     * @return
     */
    public static Object getObjectFromArrayJson(String s, Class<?> cls, String arrayName) {
        List<Object> list = new ArrayList<Object>();
        Object obj = new Object();
        try {
            JSONObject jsonObject = new JSONObject(s);
            JSONObject msgObj = jsonObject.optJSONObject(arrayName);
            Gson gson = new Gson();
            obj = gson.fromJson(msgObj.toString(), cls);
            return obj;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 解析复杂的json对象
     *
     * @param s    json对象，数据类型：对象中只包含一个Json数组对象
     * @param list runnable类中传入的List对象，与具体的Entity类无关，因为在Runnable类的具体代码中负值给Entity类的集合对象
     * @param obj  list集合中存放的类型
     * @param cls  具体的Entity类的class对象
     * @return 返回object类型的集合
     */
    public static List<Object> getComplexArrayJson(String s, Class<?> cls, String arrayName) {
        try {
            List<Object> list = new ArrayList<Object>();
            Object obj = new Object();
            JSONArray array1 = new JSONArray(s);
            JSONObject obj1 = array1.getJSONObject(0);
            JSONArray array2 = obj1.getJSONArray("result");
            JSONArray array3 = new JSONArray();
            if (arrayName != null && !"".equals(arrayName)) {
                JSONObject obj2 = array2.getJSONObject(0);
                array3 = obj2.getJSONArray(arrayName);
            } else {
                array3 = array2;
            }
            for (int i = 0; i < array3.length(); ++i) {
                o = (JSONObject) array3.get(i);
                Gson gson = new Gson();
                obj = gson.fromJson(o.toString(), cls);
                list.add(obj);
            }
            return list;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @param s   通过网络获取的json对象，数据类型：对象中只包含简单的key-value键值对
     * @param obj json对象中所有Key组成的Java类的对象
     * @param cls json对象解析之后，所有value要存入的类，即，json对象中所有Key组成的Java类
     * @return obj json对象中所有Key组成的Java类的对象
     * 例如：getSingleJson(test, mDetail, DetailOpen.class)
     */
    public static Object getSingleJson(String s, Object obj, Class<?> cls) {
        Gson gson = new Gson();
        obj = gson.fromJson(s, cls);
        return obj;

    }

    public static <T> Object getObject(String jsonString, Class<T> cls) {
        Gson gson = new Gson();
        Object object = gson.fromJson(jsonString, cls);
        return object;
    }

    public static Object getSingleKey(String s, Object obj, String value) {
        try {
            JSONObject jsonObj = new JSONObject(s);
            obj = (Object) jsonObj.getInt(value);
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return obj;
    }

}
