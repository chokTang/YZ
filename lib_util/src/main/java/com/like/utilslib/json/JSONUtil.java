package com.like.utilslib.json;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * json数据解析类
 *
 * @author longshao
 *
 */
public final class JSONUtil {

	private JSONUtil(){

	}

	/**
	 * 支持 Boolean int double String类型的解析
	 *
	 * @param object
	 * @param key
	 * @param defaultValue
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> T get(JSONObject object, String key, T defaultValue) {

		if (object == null)
			return defaultValue;
		try {
			Object object2 = object.get(key);
			if (object2 == null) {
				return defaultValue;
			}
			Class<?> valueClass = defaultValue.getClass();
			if (valueClass == int.class || valueClass == Integer.class) {
				return (T) (Integer) object.getInt(key);
			} else if (valueClass == double.class || valueClass == Double.class) {
				return (T) (Double) object.getDouble(key);
			} else if (valueClass == boolean.class
					|| valueClass == Boolean.class) {
				return (T) (Boolean) object.getBoolean(key);
			}
			return (T) object.get(key).toString();

		} catch (JSONException e) {
			return defaultValue;
		}
	}

	/**
	 * 获取String 集合
	 *
	 * @param object
	 * @param key
	 * @return
	 */
	public static List<String> getStrings(JSONObject object, String key) {

		List<String> list = new ArrayList<String>();
		try {
			JSONArray array = object.getJSONArray(key);

			for (int index = 0; index < array.length(); index++) {
				list.add(array.getString(index));
			}

		} catch (JSONException e) {
		}
		return list;
	}

	/***
	 * 获取String 集合
	 *
	 * @param array
	 * @param key
	 * @return
	 */
	public static List<String> getStrings(JSONArray array, String key) {

		List<String> list = new ArrayList<String>();
		try {

			for (int index = 0; index < array.length(); index++) {
				list.add(array.getJSONObject(index).getString(key));
			}

		} catch (JSONException e) {
		}
		return list;
	}

	/**
	 * 获取JSONObject集合
	 *
	 * @param object
	 * @param key
	 * @return
	 */
	public static List<JSONObject> getList(JSONObject object, String key) {

		List<JSONObject> list = new ArrayList<JSONObject>();

		try {
			JSONArray array = object.getJSONArray(key);

			for (int index = 0; index < array.length(); index++) {
				list.add(array.getJSONObject(index));
			}

		} catch (JSONException e) {
		}
		return list;
	}

	/**
	 * 获取JSONArray对象
	 *
	 * @param object
	 * @param key
	 * @return
	 */
	public static JSONArray getJsonArray(JSONObject object, String key) {

		try {
			return object.getJSONArray(key);
		} catch (JSONException e) {
		}
		return new JSONArray();
	}

	/**
	 * 获取JSONObject对象
	 *
	 * @param object
	 * @param key
	 * @return
	 */
	public static JSONObject getJsonObject(JSONObject object, String key) {

		try {
			return object.getJSONObject(key);

		} catch (JSONException e) {
			return null;
		}
	}

	/**
	 * String 转化为JSONObject
	 *
	 * @param value
	 * @return
	 */
	public static JSONObject toJsonObject(String value) {

		if (value == null || value.length() == 0)
			return null;

		try {
			return new JSONObject(value);
		} catch (JSONException e) {
			return null;
		}
	}

	/**
	 * 获取array 中第几个JSONObject
	 *
	 * @param array
	 * @param index
	 * @return
	 */
	public static JSONObject getJsonObject(JSONArray array, int index) {
		try {
			return array.getJSONObject(index);
		} catch (JSONException e) {
			return new JSONObject();
		}
	}

	/**
	 * 获取array 中第几个String
	 * @param array
	 * @param index
	 * @return
	 */
	public static String getString(JSONArray array, int index) {
		try {
			return array.getString(index);
		} catch (Exception e) {
			return "";
		}
	}
}
