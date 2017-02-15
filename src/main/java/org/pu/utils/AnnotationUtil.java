package org.pu.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.apache.commons.lang3.StringUtils;
import org.pu.annotation.NotBlank;

public class AnnotationUtil {
	private static String methodNameGet = "get";

	/**
	 * 校验非空属性是否为空
	 * 
	 * @throws IllegalArgumentException
	 */
	public static boolean checkNotBlankFields(Object object) throws IllegalArgumentException {
		Class<?> objClass = object.getClass();
		Field[] fields = objClass.getDeclaredFields();
		int blankCount = 0;
		StringBuilder blankFieldsBuilder = new StringBuilder();
		for (Field field : fields) {
			if (field.getAnnotation(NotBlank.class) != null) {
				try {
					String fieldname = field.getName();
					Method method = objClass
							.getMethod(methodNameGet + StringUtils.capitalize(fieldname));
					Object result = method.invoke(object);
					if (result == null || StringUtils.isBlank(result.toString())) {
						blankCount++;
						blankFieldsBuilder.append(fieldname).append(",");
					}
				} catch (Exception ignore) {
				}
			}
		}
		
		if (blankCount > 0) {
			blankFieldsBuilder.setLength(blankFieldsBuilder.length() - 1);
			String msg = blankFieldsBuilder.toString() + " cannot be null";
			throw new IllegalArgumentException(msg);
		} else {
			return true;
		}
	}
}
