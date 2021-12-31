package org.magneton.test.test.util;

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
