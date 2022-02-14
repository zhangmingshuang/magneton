package org.magneton.foundation.util;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

import javax.annotation.Nullable;

/**
 * .
 *
 * @author zhangmsh
 * @version 1.0.0
 * @since 2020/12/23
 */
public class Arrays {

	/**
	 * An empty immutable {@code boolean} array.
	 */
	public static final boolean[] EMPTY_BOOLEAN_ARRAY = new boolean[0];

	/**
	 * An empty immutable {@code Boolean} array.
	 */
	public static final Boolean[] EMPTY_BOOLEAN_OBJECT_ARRAY = new Boolean[0];

	/**
	 * An empty immutable {@code byte} array.
	 */
	public static final byte[] EMPTY_BYTE_ARRAY = new byte[0];

	/**
	 * An empty immutable {@code Byte} array.
	 */
	public static final Byte[] EMPTY_BYTE_OBJECT_ARRAY = new Byte[0];

	/**
	 * An empty immutable {@code char} array.
	 */
	public static final char[] EMPTY_CHAR_ARRAY = new char[0];

	/**
	 * An empty immutable {@code Character} array.
	 */
	public static final Character[] EMPTY_CHARACTER_OBJECT_ARRAY = new Character[0];

	/**
	 * An empty immutable {@code Class} array.
	 */
	public static final Class<?>[] EMPTY_CLASS_ARRAY = new Class[0];

	/**
	 * An empty immutable {@code double} array.
	 */
	public static final double[] EMPTY_DOUBLE_ARRAY = new double[0];

	/**
	 * An empty immutable {@code Double} array.
	 */
	public static final Double[] EMPTY_DOUBLE_OBJECT_ARRAY = new Double[0];

	/**
	 * An empty immutable {@code Field} array.
	 *
	 * @since 3.10
	 */
	public static final Field[] EMPTY_FIELD_ARRAY = new Field[0];

	/**
	 * An empty immutable {@code Method} array.
	 *
	 * @since 3.10
	 */
	public static final Method[] EMPTY_METHOD_ARRAY = new Method[0];

	/**
	 * An empty immutable {@code float} array.
	 */
	public static final float[] EMPTY_FLOAT_ARRAY = new float[0];

	/**
	 * An empty immutable {@code Float} array.
	 */
	public static final Float[] EMPTY_FLOAT_OBJECT_ARRAY = new Float[0];

	/**
	 * An empty immutable {@code int} array.
	 */
	public static final int[] EMPTY_INT_ARRAY = new int[0];

	/**
	 * An empty immutable {@code Integer} array.
	 */
	public static final Integer[] EMPTY_INTEGER_OBJECT_ARRAY = new Integer[0];

	/**
	 * An empty immutable {@code long} array.
	 */
	public static final long[] EMPTY_LONG_ARRAY = new long[0];

	/**
	 * An empty immutable {@code Long} array.
	 */
	public static final Long[] EMPTY_LONG_OBJECT_ARRAY = new Long[0];

	/**
	 * An empty immutable {@code Object} array.
	 */
	public static final Object[] EMPTY_OBJECT_ARRAY = new Object[0];

	/**
	 * An empty immutable {@code short} array.
	 */
	public static final short[] EMPTY_SHORT_ARRAY = new short[0];

	/**
	 * An empty immutable {@code Short} array.
	 */
	public static final Short[] EMPTY_SHORT_OBJECT_ARRAY = new Short[0];

	/**
	 * An empty immutable {@code String} array.
	 */
	public static final String[] EMPTY_STRING_ARRAY = new String[0];

	/**
	 * An empty immutable {@code Throwable} array.
	 *
	 * @since 3.10
	 */
	public static final Throwable[] EMPTY_THROWABLE_ARRAY = new Throwable[0];

	/**
	 * An empty immutable {@code Type} array.
	 *
	 * @since 3.10
	 */
	public static final Type[] EMPTY_TYPE_ARRAY = new Type[0];

	/**
	 * The index value when an element is not found in a list or array: {@code -1}. This
	 * value is returned by methods in this class and can also be used in comparisons with
	 * values returned by various method from {@link java.util.List}.
	 */
	public static final int INDEX_NOT_FOUND = -1;

	private Arrays() {
	}

	/**
	 * Checks if an array of primitive booleans is empty or {@code null}.
	 * @param array the array to test
	 * @return {@code true} if the array is empty or {@code null}
	 * @since 2.1
	 */
	public static boolean isNullOrEmpty(@Nullable boolean[] array) {
		return getLength(array) == 0;
	}

	/**
	 * Checks if an array of primitive bytes is empty or {@code null}.
	 * @param array the array to test
	 * @return {@code true} if the array is empty or {@code null}
	 * @since 2.1
	 */
	public static boolean isNullOrEmpty(@Nullable byte[] array) {
		return getLength(array) == 0;
	}

	/**
	 * Checks if an array of primitive chars is empty or {@code null}.
	 * @param array the array to test
	 * @return {@code true} if the array is empty or {@code null}
	 * @since 2.1
	 */
	public static boolean isNullOrEmpty(@Nullable char[] array) {
		return getLength(array) == 0;
	}

	/**
	 * Checks if an array of primitive doubles is empty or {@code null}.
	 * @param array the array to test
	 * @return {@code true} if the array is empty or {@code null}
	 * @since 2.1
	 */
	public static boolean isNullOrEmpty(@Nullable double[] array) {
		return getLength(array) == 0;
	}

	/**
	 * Checks if an array of primitive floats is empty or {@code null}.
	 * @param array the array to test
	 * @return {@code true} if the array is empty or {@code null}
	 * @since 2.1
	 */
	public static boolean isNullOrEmpty(@Nullable float[] array) {
		return getLength(array) == 0;
	}

	/**
	 * Checks if an array of primitive ints is empty or {@code null}.
	 * @param array the array to test
	 * @return {@code true} if the array is empty or {@code null}
	 * @since 2.1
	 */
	public static boolean isNullOrEmpty(@Nullable int[] array) {
		return getLength(array) == 0;
	}

	/**
	 * Checks if an array of objects is empty or {@code null}.
	 *
	 * <p>
	 * recommend use the {@link Array#getLength(Object)} method instead.
	 * {@code Array#getLength} is a native method in jvm, it's more better performance.
	 * @param array the array to test
	 * @return {@code true} if the array is empty or {@code null}
	 */
	public static boolean isNullOrEmpty(@Nullable Object[] array) {
		return getLength(array) == 0;
	}

	/**
	 * join a char sequence into a array's every element.
	 * @param array array data.
	 * @param join join char sequence.
	 * @return the processed string.
	 */
	public static String join(@Nullable int[] array, CharSequence join) {
		if (isNullOrEmpty(array)) {
			return "";
		}
		StringBuilder builder = new StringBuilder(Objects.requireNonNull(array).length >> 1);
		for (int i : array) {
			builder.append(i).append(join);
		}
		return builder.substring(0, builder.length() - 1);
	}

	private static int getLength(Object array) {
		return array == null ? 0 : Array.getLength(array);
	}

	/**
	 * 将集合转为数组
	 * @param <T> 数组元素类型
	 * @param collection 集合
	 * @param componentType 集合元素类型
	 * @return 数组
	 * @since 3.0.9
	 */
	public static <T> T[] toArray(Collection<T> collection, Class<?> componentType) {
		return collection.toArray(newArray(componentType, 0));
	}

	/**
	 * 新建一个空数组
	 * @param <T> 数组元素类型
	 * @param componentType 元素类型
	 * @param newSize 大小
	 * @return 空数组
	 */
	public static <T> T[] newArray(Class<?> componentType, int newSize) {
		return (T[]) Array.newInstance(componentType, newSize);
	}

	public static <T> List<T> asList(T... values) {
		return java.util.Arrays.asList(values);
	}

}
