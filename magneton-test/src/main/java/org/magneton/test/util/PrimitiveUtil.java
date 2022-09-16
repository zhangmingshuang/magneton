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

package org.magneton.test.util;

import com.google.common.collect.Maps;
import java.util.Map;

/**
 * .
 *
 * @author zhangmsh 2021/8/2
 * @since
 */
public class PrimitiveUtil {

	private static final Map<Class, Object> PRIMITIVES = Maps.newHashMap();

	static {
		PRIMITIVES.put(byte.class, 0B00);
		PRIMITIVES.put(short.class, (short) 0);
		PRIMITIVES.put(int.class, 0);
		PRIMITIVES.put(long.class, 0L);
		PRIMITIVES.put(float.class, 0.0F);
		PRIMITIVES.put(double.class, 0.0D);
		PRIMITIVES.put(char.class, (char) 0);
		PRIMITIVES.put(boolean.class, false);
		PRIMITIVES.put(Byte.class, null);
		PRIMITIVES.put(Short.class, null);
		PRIMITIVES.put(Integer.class, null);
		PRIMITIVES.put(Long.class, null);
		PRIMITIVES.put(Float.class, null);
		PRIMITIVES.put(Double.class, null);
		PRIMITIVES.put(Character.class, null);
		PRIMITIVES.put(Boolean.class, null);
	}

	private PrimitiveUtil() {
	}

	public static boolean isPrimitive(Class clazz) {
		if (clazz.isPrimitive()) {
			return true;
		}
		return PRIMITIVES.containsKey(clazz);
	}

	public static Object defaultValue(Class clazz) {
		return PRIMITIVES.get(clazz);
	}

}
