package org.magneton.core.lang;

import java.lang.reflect.Array;
import java.util.Objects;

import javax.annotation.Nullable;

/**
 * .
 *
 * @author zhangmsh 2022/1/12
 * @since 2.
 */
public class UArray {

	// ============================= empty =====================================
	/**
	 * <p>
	 * Checks if an array of primitive booleans is empty or {@code null}.
	 * @param array the array to test
	 * @return {@code true} if the array is empty or {@code null}
	 * @since 2.1
	 */
	public static boolean isEmpty(@Nullable boolean[] array) {
		return length(array) == 0;
	}

	/**
	 * <p>
	 * Checks if an array of primitive bytes is empty or {@code null}.
	 * @param array the array to test
	 * @return {@code true} if the array is empty or {@code null}
	 * @since 2.1
	 */
	public static boolean isEmpty(@Nullable byte[] array) {
		return length(array) == 0;
	}

	/**
	 * <p>
	 * Checks if an array of primitive chars is empty or {@code null}.
	 * @param array the array to test
	 * @return {@code true} if the array is empty or {@code null}
	 * @since 2.1
	 */
	public static boolean isEmpty(@Nullable char[] array) {
		return length(array) == 0;
	}

	/**
	 * <p>
	 * Checks if an array of primitive doubles is empty or {@code null}.
	 * @param array the array to test
	 * @return {@code true} if the array is empty or {@code null}
	 * @since 2.1
	 */
	public static boolean isEmpty(@Nullable double[] array) {
		return length(array) == 0;
	}

	/**
	 * <p>
	 * Checks if an array of primitive floats is empty or {@code null}.
	 * @param array the array to test
	 * @return {@code true} if the array is empty or {@code null}
	 * @since 2.1
	 */
	public static boolean isEmpty(@Nullable float[] array) {
		return length(array) == 0;
	}

	/**
	 * <p>
	 * Checks if an array of primitive ints is empty or {@code null}.
	 * @param array the array to test
	 * @return {@code true} if the array is empty or {@code null}
	 * @since 2.1
	 */
	public static boolean isEmpty(@Nullable int[] array) {
		return length(array) == 0;
	}

	/**
	 * <p>
	 * Checks if an array of primitive longs is empty or {@code null}.
	 * @param array the array to test
	 * @return {@code true} if the array is empty or {@code null}
	 * @since 2.1
	 */
	public static boolean isEmpty(@Nullable long[] array) {
		return length(array) == 0;
	}

	// ----------------------------------------------------------------------
	/**
	 * <p>
	 * Checks if an array of Objects is empty or {@code null}.
	 * @param array the array to test
	 * @return {@code true} if the array is empty or {@code null}
	 * @since 2.1
	 */
	public static boolean isEmpty(@Nullable Object[] array) {
		return length(array) == 0;
	}

	/**
	 * <p>
	 * Checks if an array of primitive shorts is empty or {@code null}.
	 * @param array the array to test
	 * @return {@code true} if the array is empty or {@code null}
	 * @since 2.1
	 */
	public static boolean isEmpty(@Nullable short[] array) {
		return length(array) == 0;
	}

	/**
	 * 是否包含{@code null}元素
	 * @param <T> 数组元素类型
	 * @param array 被检查的数组
	 * @return 是否包含{@code null}元素
	 * @since 3.0.7
	 */
	@SuppressWarnings("unchecked")
	public static <T> boolean hasNull(T... array) {
		if (!isEmpty(array)) {
			for (T element : array) {
				if (Objects.isNull(element)) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * <p>
	 * Returns the length of the specified array. This method can deal with {@code Object}
	 * arrays and with primitive arrays.
	 *
	 * <p>
	 * If the input array is {@code null}, {@code 0} is returned.
	 *
	 * <pre>
	 * MArrays.length(null)            = 0
	 * MArrays.length([])              = 0
	 * MArrays.length([null])          = 1
	 * MArrays.length([true, false])   = 2
	 * MArrays.length([1, 2, 3])       = 3
	 * MArrays.length(["a", "b", "c"]) = 3
	 * </pre>
	 * @param array the array to retrieve the length from, may be null
	 * @return The length of the array, or {@code 0} if the array is {@code null}
	 * @throws IllegalArgumentException if the object argument is not an array.
	 * @since 2.1
	 */
	public static int length(@Nullable Object array) {
		if (array == null) {
			return 0;
		}
		return Array.getLength(array);
	}

}
