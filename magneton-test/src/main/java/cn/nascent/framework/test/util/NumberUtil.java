package cn.nascent.framework.test.util;

import com.google.common.base.Preconditions;
import com.google.common.reflect.TypeToken;
import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * .
 *
 * @author zhangmsh 2021/8/3
 * @since 2.0.0
 */
@SuppressWarnings("All")
public class NumberUtil {

	private NumberUtil() {
	}

	public static Number minValue(Class clazz) {
		if (byte.class.equals(clazz) || Byte.class.equals(clazz)) {
			return Byte.MIN_VALUE;
		}
		else if (short.class.equals(clazz) || Short.class.equals(clazz)) {
			return Short.MIN_VALUE;
		}
		else if (int.class.equals(clazz) || Integer.class.equals(clazz)) {
			return Integer.MIN_VALUE;
		}
		else if (long.class.equals(clazz) || Long.class.equals(clazz)) {
			return Long.MIN_VALUE;
		}
		else if (float.class.equals(clazz) || Float.class.equals(clazz)) {
			return Float.MIN_VALUE;
		}
		else if (double.class.equals(clazz) || Double.class.equals(clazz)) {
			return Double.MIN_VALUE;
		}
		else if (BigDecimal.class.equals(clazz)) {
			return BigDecimal.valueOf(Long.MIN_VALUE);
		}
		else if (BigInteger.class.equals(clazz)) {
			return BigInteger.valueOf(Long.MIN_VALUE);
		}
		return -1;
	}

	public static Number maxValue(Class clazz) {
		if (byte.class.equals(clazz) || Byte.class.equals(clazz)) {
			return Byte.MAX_VALUE;
		}
		else if (short.class.equals(clazz) || Short.class.equals(clazz)) {
			return Short.MAX_VALUE;
		}
		else if (int.class.equals(clazz) || Integer.class.equals(clazz)) {
			return Integer.MAX_VALUE;
		}
		else if (long.class.equals(clazz) || Long.class.equals(clazz)) {
			return Long.MAX_VALUE;
		}
		else if (float.class.equals(clazz) || Float.class.equals(clazz)) {
			return Float.MAX_VALUE;
		}
		else if (double.class.equals(clazz) || Double.class.equals(clazz)) {
			return Double.MAX_VALUE;
		}
		else if (BigDecimal.class.equals(clazz)) {
			return BigDecimal.valueOf(Long.MAX_VALUE);
		}
		else if (BigInteger.class.equals(clazz)) {
			return BigInteger.valueOf(Long.MAX_VALUE);
		}
		return Long.MAX_VALUE;
	}

	/**
	 * Determine whether the given {@code value} String indicates a hex number, i.e. needs
	 * to be passed into {@code Integer.decode} instead of {@code Integer.valueOf}, etc.
	 */
	private static boolean isHexNumber(String value) {
		int index = (value.startsWith("-") ? 1 : 0);
		return (value.startsWith("0x", index) || value.startsWith("0X", index) || value.startsWith("#", index));
	}

	/**
	 * Decode a {@link java.math.BigInteger} from the supplied {@link String} value.
	 *
	 * <p>
	 * Supports decimal, hex, and octal notation.
	 *
	 * @see BigInteger#BigInteger(String, int)
	 */
	private static BigInteger decodeBigInteger(String value) {
		int radix = 10;
		int index = 0;
		boolean negative = false;

		// Handle minus sign, if present.
		if (!value.isEmpty() && value.charAt(0) == '-') {
			negative = true;
			index++;
		}

		// Handle radix specifier, if present.
		if (value.startsWith("0x", index) || value.startsWith("0X", index)) {
			index += 2;
			radix = 16;
		}
		else if (value.startsWith("#", index)) {
			index++;
			radix = 16;
		}
		else if (value.startsWith("0", index) && value.length() > 1 + index) {
			index++;
			radix = 8;
		}

		BigInteger result = new BigInteger(value.substring(index), radix);
		return (negative ? result.negate() : result);
	}

	public static boolean isNumberType(Class clazz) {
		if (Number.class.isAssignableFrom(clazz)) {
			return true;
		}
		else if (byte.class.equals(clazz) || Byte.class.equals(clazz)) {
			return true;
		}
		else if (short.class.equals(clazz) || Short.class.equals(clazz)) {
			return true;
		}
		else if (int.class.equals(clazz) || Integer.class.equals(clazz)) {
			return true;
		}
		else if (long.class.equals(clazz) || Long.class.equals(clazz)) {
			return true;
		}
		else if (float.class.equals(clazz) || Float.class.equals(clazz)) {
			return true;
		}
		else if (double.class.equals(clazz) || Double.class.equals(clazz)) {
			return true;
		}
		else if (BigDecimal.class.equals(clazz)) {
			return true;
		}
		else {
			return BigInteger.class.equals(clazz);
		}
	}

	public static Number cast(Class targetClass, Number value) {
		if (TypeToken.of(value.getClass()).wrap().equals(TypeToken.of(targetClass).wrap())) {
			return value;
		}
		return cast(targetClass, value.toString());
	}

	public static Number cast(Class targetClass, String value) {
		String trimmed = StringUtil.trimAllWhitespace(value.toString());
		BigDecimal bigDecimal = new BigDecimal(trimmed);
		return cast(targetClass, bigDecimal.doubleValue());
	}

	public static Number cast(Class targetClass, double value) {
		Preconditions.checkNotNull(targetClass);

		BigDecimal decimal = BigDecimal.valueOf(value);
		if (byte.class.equals(targetClass) || Byte.class.equals(targetClass)) {
			return decimal.byteValue();
		}
		else if (short.class.equals(targetClass) || Short.class.equals(targetClass)) {
			return decimal.shortValue();
		}
		else if (int.class.equals(targetClass) || Integer.class.equals(targetClass)) {
			return decimal.intValue();
		}
		else if (long.class.equals(targetClass) || Long.class.equals(targetClass)) {
			return decimal.longValue();
		}
		else if (float.class.equals(targetClass) || Float.class.equals(targetClass)) {
			return decimal.floatValue();
		}
		else if (double.class.equals(targetClass) || Double.class.equals(targetClass)) {
			return decimal.doubleValue();
		}
		else if (BigDecimal.class.equals(targetClass)) {
			return decimal;
		}
		else if (BigInteger.class.equals(targetClass)) {
			return BigInteger.valueOf(decimal.longValue());
		}
		throw new IllegalArgumentException(
				"Cannot convert value [" + value + "] to target class [" + targetClass.getName() + "]");
	}

}
