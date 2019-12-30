package com.like.utilslib.json.helper;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

/**
 * gson 数据解析辅助类
 * @author longshao
 * @param <X>
 */
public class GsonType<X> implements ParameterizedType {
	private Class<?> wrapped;

	public GsonType(Class<X> wrapped) {
		this.wrapped = wrapped;
	}

	public Type[] getActualTypeArguments() {
		return new Type[] { wrapped };
	}

	public Type getRawType() {
		return List.class;
	}

	public Type getOwnerType() {
		return null;
	}
}
