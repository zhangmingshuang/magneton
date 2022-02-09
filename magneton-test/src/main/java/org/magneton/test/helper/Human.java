package org.magneton.test.helper;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
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
public class Human {

	private Human() {
	}

	public static void sout(Object... objects) {
		if (objects == null) {
			System.out.println("null");
			return;
		}
		for (Object object : objects) {
			if (object == null) {
				System.out.println("null");
			}
			else {
				String body = getSoutBody(object);
				System.out.println(body);
			}
		}
	}

	@SneakyThrows
	public static String getSoutBody(Object object) {
		StringBuilder builder = new StringBuilder(128);
		Class<?> clazz = object == null ? null : object.getClass();
		if (clazz == null) {
			return "null";
		}
		else if (PrimitiveUtil.isPrimitive(clazz)) {
			builder.append(clazz.getSimpleName()).append("=").append(humanValue(object));
			return builder.toString();
		}
		builder.append(clazz.getSimpleName()).append("(");
		Set<Field> fields = FieldUtil.getFields(clazz);
		if (fields.isEmpty()) {
			builder.append(humanValue(object)).append(")");
		}
		else {
			for (Field field : fields) {
				field.setAccessible(true);
				String name = field.getName();
				Object value = field.get(object);
				String body = value == null ? "null"
						: PrimitiveUtil.isPrimitive(field.getType()) ? humanValue(value) : getSoutBody(value);
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

	private static String humanValue(Object object) {
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
					builder.append(humanValue(obj)).append(",");
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
