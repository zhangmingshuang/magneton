package org.magneton.foundation;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Objects;
import javax.annotation.Nullable;

/**
 * @author zhangmsh 2022/5/18
 * @since 1.0.0
 */
public class MoreArrays {

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

}
