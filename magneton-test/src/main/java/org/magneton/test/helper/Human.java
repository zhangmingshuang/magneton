/*
 * Copyright (c) 2020-2030  Xiamen Nascent Corporation. All rights reserved.
 *
 * https://www.nascent.cn
 *
 * 厦门南讯股份有限公司创立于2010年，是一家始终以技术和产品为驱动，帮助大消费领域企业提供客户资源管理（CRM）解决方案的公司。
 * 福建省厦门市软件园二期观日路22号401
 * 客服电话 400-009-2300
 * 电话 +86（592）5971731 传真 +86（592）5971710
 *
 * All source code copyright of this system belongs to Xiamen Nascent Co., Ltd.
 * Any organization or individual is not allowed to reprint, publish, disclose, embezzle, sell and use it for other illegal purposes without permission!
 */

package org.magneton.test.helper;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.Set;

import lombok.SneakyThrows;
import org.magneton.test.util.FieldUtil;
import org.magneton.test.util.PrimitiveUtil;

/**
 * .
 *
 * @author zhangmsh 2021/8/24
 * @since 2.0.0
 */
@SuppressWarnings("UseOfSystemOutOrSystemErr")
public class Human {

	private static final String BR = System.lineSeparator();

	private Human() {
	}

	public static void sout(Object... objects) {
		if (objects == null) {
			System.out.println("null");
			return;
		}
		StringBuilder info = new StringBuilder(64);
		try {
			for (Object object : objects) {
				if (object == null) {
					System.out.println("null");
				}
				else {
					String body = getSoutBody(object, info, "");
					System.out.println(body);
				}
			}
		}
		catch (Throwable e) {
			System.out.println(info);
			System.out.println("=====");
			e.printStackTrace();
		}
	}

	@SneakyThrows
	public static String getSoutBody(Object object, StringBuilder info, String prefix) {
		StringBuilder builder = new StringBuilder(128);
		Class<?> clazz = object == null ? null : object.getClass();
		if (clazz == null) {
			info.append(prefix).append("null").append(BR);
			return "null";
		}
		else if (PrimitiveUtil.isPrimitive(clazz)) {
			info.append(prefix).append(object).append(BR);
			builder.append(clazz.getSimpleName()).append("=").append(humanValue(object, info, prefix));
			return builder.toString();
		}
		builder.append(clazz.getSimpleName()).append("(");
		Set<Field> fields = FieldUtil.getFields(clazz);
		if (fields.isEmpty()) {
			builder.append(humanValue(object, info, prefix)).append(")");
		}
		else {
			for (Field field : fields) {
				if (Modifier.isStatic(field.getModifiers())) {
					continue;
				}
				info.append(prefix).append(field).append(BR);
				field.setAccessible(true);
				String name = field.getName();
				Object value = field.get(object);
				String body = value == null ? "null" : PrimitiveUtil.isPrimitive(field.getType())
						? humanValue(value, info, prefix + "-") : getSoutBody(value, info, prefix + "-");
				builder.append(name).append("=").append(body).append(",");
			}
			builder.setLength(builder.length() - 1);
			builder.append(")");
		}
		if (object instanceof Collection) {
			builder.append(", size=").append(((Collection) object).size());
		}
		else if (object instanceof Map) {
			builder.append(", size=").append(((Map) object).size());
		}
		return builder.toString();
	}

	private static String humanValue(Object object, StringBuilder info, String prefix) {
		info.append(prefix).append(object).append(BR);
		if (object instanceof Date) {
			return object + " >> " + formatYMDHMS((Date) object);
		}
		if (object instanceof Calendar) {
			Calendar calendar = (Calendar) object;
			return formatYMDHMS(calendar.getTime());
		}
		if (object.getClass().isArray()) {
			int len = Array.getLength(object);
			StringBuilder builder = new StringBuilder(64);
			if (len > 0) {
				for (int i = 0; i < len; i++) {
					Object obj = Array.get(object, i);
					if (obj == null) {
						continue;
					}
					builder.append(humanValue(obj, info, prefix + "-")).append(",");
				}
				builder.setLength(builder.length() - 1);
			}
			return builder.toString();
		}
		return String.valueOf(object);
	}

	private static String formatYMDHMS(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.format(date);
	}

}
