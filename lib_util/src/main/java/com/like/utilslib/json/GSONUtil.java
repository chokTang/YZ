package com.like.utilslib.json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.like.utilslib.json.helper.GsonType;
import com.like.utilslib.json.helper.NullStringToEmptyAdapterFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * gson数据解析类
 *
 * @author longshao
 */
public final class GSONUtil {

    private GSONUtil() {
    }

    /**
     * json数据转换为实体
     *
     * @param json   数据源
     * @param entity 实体
     * @return
     */
    public static <T> T getEntity(String json, Class<T> entity) {
        Gson gson = new GsonBuilder().registerTypeAdapterFactory(new NullStringToEmptyAdapterFactory()).create();
        T jsonEntity = null;
        try {
            jsonEntity = gson.fromJson(json, entity);
        } catch (Exception e) {
            return null;
        }
        return jsonEntity;
    }

    /**
     * json数据转换为实体集合
     *
     * @param jsonString
     * @return
     */
    public static <T> List<T> getEntityList(String jsonString, Class<T> entity) {
        Gson gson = new GsonBuilder().registerTypeAdapterFactory(new NullStringToEmptyAdapterFactory()).create();
        List<T> list = null;
        try {
            list = gson.fromJson(jsonString, new GsonType<T>(entity));
        } catch (Exception e) {
            return new ArrayList<T>();
        }
        return list;
    }

    /**
     * json转换为map对象集合
     *
     * @param jsonString
     * @return
     */
    public static List<Map<String, Object>> getEntityMap(String jsonString) {
        Gson gson = new GsonBuilder().registerTypeAdapterFactory(new NullStringToEmptyAdapterFactory()).create();
        List<Map<String, Object>> list = null;
        try {
            list = gson.fromJson(jsonString,
                    new TypeToken<List<Map<String, Object>>>() {
                    }.getType());
        } catch (Exception e) {
            return new ArrayList<Map<String, Object>>();
        }
        return list;
    }

    public static String toJson(Object object) {
        Gson gson = new Gson();
        return gson.toJson(object);
    }
}
