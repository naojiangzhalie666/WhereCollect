package com.gongwu.wherecollect.util;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * @author pengqm
 * @name film
 * @class name：com.baidu.iov.dueros.film.utils
 * @time 2018/10/11 17:52
 * @change
 * @class describe
 */

public class CommonUtils {


	private static String getFieldValueByFieldName(String fieldName, Object object, Class clazz) {
		try {
			Field field = clazz.getDeclaredField(fieldName);
			field.setAccessible(true);
			if ("java.util.List".equals(field.getType().getName())){
				return JsonUtils.jsonFromObject(field.get(object));
			}
			return field.get(object).toString();
		} catch (Exception e) {
			return null;
		}
	}

	public static <T> Map<String, String> getAllFields(T request) {
		if (request ==null) return null;
		TreeMap<String, String> map = new TreeMap<>();
		Class clazz = request.getClass();
		while (clazz != null) {
			ArrayList<Field> list = new ArrayList<>(Arrays.asList(clazz.getDeclaredFields()));
			for (Field field : list) {
				String fieldName = field.getName();
				if (fieldName.startsWith("shadow$")) {
					continue;
				}
				if (field.isSynthetic()) {
					continue;
				}
				if (field.getName().equals("serialVersionUID")) {
					continue;
				}
				String attr = getFieldValueByFieldName(fieldName, request, clazz);
				if (attr == null) {
					continue;
				}
				map.put(fieldName, attr);
			}
			clazz = clazz.getSuperclass();
		}
		return map;
	}
}
