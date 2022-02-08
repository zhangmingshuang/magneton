/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.magneton.test.util;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.BitSet;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Operations on arrays, primitive arrays (like {@code int[]}) and primitive wrapper
 * arrays (like {@code Integer[]}).
 *
 * <p>
 * This class tries to handle {@code null} input gracefully. An exception will not be
 * thrown for a {@code null} array input. However, an Object array that contains a
 * {@code null} element may throw an exception. Each method documents its behaviour.
 *
 * <p>
 * #ThreadSafe#
 *
 * @since 2.0
 */
public class ArrayUtils {

	/** An empty immutable {@code boolean} array. */
	public static final boolean[] EMPTY_BOOLEAN_ARRAY = new boolean[0];

	/** An empty immutable {@code Boolean} array. */
	public static final Boolean[] EMPTY_BOOLEAN_OBJECT_ARRAY = new Boolean[0];

	/** An empty immutable {@code byte} array. */
	public static final byte[] EMPTY_BYTE_ARRAY = new byte[0];

	/** An empty immutable {@code Byte} array. */
	public static final Byte[] EMPTY_BYTE_OBJECT_ARRAY = new Byte[0];

	/** An empty immutable {@code char} array. */
	public static final char[] EMPTY_CHAR_ARRAY = new char[0];

	/** An empty immutable {@code Character} array. */
	public static final Character[] EMPTY_CHARACTER_OBJECT_ARRAY = new Character[0];

	/** An empty immutable {@code Class} array. */
	public static final Class<?>[] EMPTY_CLASS_ARRAY = new Class[0];

	/** An empty immutable {@code double} array. */
	public static final double[] EMPTY_DOUBLE_ARRAY = new double[0];

	/** An empty immutable {@code Double} array. */
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

	/** An empty immutable {@code float} array. */
	public static final float[] EMPTY_FLOAT_ARRAY = new float[0];

	/** An empty immutable {@code Float} array. */
	public static final Float[] EMPTY_FLOAT_OBJECT_ARRAY = new Float[0];

	/** An empty immutable {@code int} array. */
	public static final int[] EMPTY_INT_ARRAY = new int[0];

	/** An empty immutable {@code Integer} array. */
	public static final Integer[] EMPTY_INTEGER_OBJECT_ARRAY = new Integer[0];

	/** An empty immutable {@code long} array. */
	public static final long[] EMPTY_LONG_ARRAY = new long[0];

	/** An empty immutable {@code Long} array. */
	public static final Long[] EMPTY_LONG_OBJECT_ARRAY = new Long[0];

	/** An empty immutable {@code Object} array. */
	public static final Object[] EMPTY_OBJECT_ARRAY = new Object[0];

	/** An empty immutable {@code short} array. */
	public static final short[] EMPTY_SHORT_ARRAY = new short[0];

	/** An empty immutable {@code Short} array. */
	public static final Short[] EMPTY_SHORT_OBJECT_ARRAY = new Short[0];

	/** An empty immutable {@code String} array. */
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

	/**
	 * Copies the given array and adds the given element at the end of the new array.
	 *
	 * <p>
	 * The new array contains the same elements of the input array plus the given element
	 * in the last position. The component type of the new array is the same as that of
	 * the input array.
	 *
	 * <p>
	 * If the input array is {@code null}, a new one element array is returned whose
	 * component type is the same as the element.
	 *
	 * <pre>
	 * ArrayUtils.add(null, true)          = [true]
	 * ArrayUtils.add([true], false)       = [true, false]
	 * ArrayUtils.add([true, false], true) = [true, false, true]
	 * </pre>
	 * @param array the array to copy and add the element to, may be {@code null}
	 * @param element the object to add at the last index of the new array
	 * @return A new array containing the existing elements plus the new element
	 * @since 2.1
	 */
	public static boolean[] add(boolean[] array, boolean element) {
		boolean[] newArray = (boolean[]) copyArrayGrow1(array, Boolean.TYPE);
		newArray[newArray.length - 1] = element;
		return newArray;
	}

	/**
	 * Inserts the specified element at the specified position in the array. Shifts the
	 * element currently at that position (if any) and any subsequent elements to the
	 * right (adds one to their indices).
	 *
	 * <p>
	 * This method returns a new array with the same elements of the input array plus the
	 * given element on the specified position. The component type of the returned array
	 * is always the same as that of the input array.
	 *
	 * <p>
	 * If the input array is {@code null}, a new one element array is returned whose
	 * component type is the same as the element.
	 *
	 * <pre>
	 * ArrayUtils.add(null, 0, true)          = [true]
	 * ArrayUtils.add([true], 0, false)       = [false, true]
	 * ArrayUtils.add([false], 1, true)       = [false, true]
	 * ArrayUtils.add([true, false], 1, true) = [true, true, false]
	 * </pre>
	 * @param array the array to add the element to, may be {@code null}
	 * @param index the position of the new object
	 * @param element the object to add
	 * @return A new array containing the existing elements and the new element
	 * @throws IndexOutOfBoundsException if the index is out of range (index &lt; 0 ||
	 * index &gt; array.length).
	 * @deprecated this method has been superseded by
	 * {@link #insert(int, boolean[], boolean...)} and may be removed in a future release.
	 * Please note the handling of {@code null} input arrays differs in the new method:
	 * inserting {@code X} into a {@code null} array results in {@code
	 *     null} not {@code X}.
	 */
	@Deprecated
	public static boolean[] add(boolean[] array, int index, boolean element) {
		return (boolean[]) add(array, index, Boolean.valueOf(element), Boolean.TYPE);
	}

	/**
	 * Copies the given array and adds the given element at the end of the new array.
	 *
	 * <p>
	 * The new array contains the same elements of the input array plus the given element
	 * in the last position. The component type of the new array is the same as that of
	 * the input array.
	 *
	 * <p>
	 * If the input array is {@code null}, a new one element array is returned whose
	 * component type is the same as the element.
	 *
	 * <pre>
	 * ArrayUtils.add(null, 0)   = [0]
	 * ArrayUtils.add([1], 0)    = [1, 0]
	 * ArrayUtils.add([1, 0], 1) = [1, 0, 1]
	 * </pre>
	 * @param array the array to copy and add the element to, may be {@code null}
	 * @param element the object to add at the last index of the new array
	 * @return A new array containing the existing elements plus the new element
	 * @since 2.1
	 */
	public static byte[] add(byte[] array, byte element) {
		byte[] newArray = (byte[]) copyArrayGrow1(array, Byte.TYPE);
		newArray[newArray.length - 1] = element;
		return newArray;
	}

	/**
	 * Inserts the specified element at the specified position in the array. Shifts the
	 * element currently at that position (if any) and any subsequent elements to the
	 * right (adds one to their indices).
	 *
	 * <p>
	 * This method returns a new array with the same elements of the input array plus the
	 * given element on the specified position. The component type of the returned array
	 * is always the same as that of the input array.
	 *
	 * <p>
	 * If the input array is {@code null}, a new one element array is returned whose
	 * component type is the same as the element.
	 *
	 * <pre>
	 * ArrayUtils.add([1], 0, 2)         = [2, 1]
	 * ArrayUtils.add([2, 6], 2, 3)      = [2, 6, 3]
	 * ArrayUtils.add([2, 6], 0, 1)      = [1, 2, 6]
	 * ArrayUtils.add([2, 6, 3], 2, 1)   = [2, 6, 1, 3]
	 * </pre>
	 * @param array the array to add the element to, may be {@code null}
	 * @param index the position of the new object
	 * @param element the object to add
	 * @return A new array containing the existing elements and the new element
	 * @throws IndexOutOfBoundsException if the index is out of range (index &lt; 0 ||
	 * index &gt; array.length).
	 * @deprecated this method has been superseded by
	 * {@link #insert(int, byte[], byte...)} and may be removed in a future release.
	 * Please note the handling of {@code null} input arrays differs in the new method:
	 * inserting {@code X} into a {@code null} array results in {@code null} not
	 * {@code X}.
	 */
	@Deprecated
	public static byte[] add(byte[] array, int index, byte element) {
		return (byte[]) add(array, index, Byte.valueOf(element), Byte.TYPE);
	}

	/**
	 * Copies the given array and adds the given element at the end of the new array.
	 *
	 * <p>
	 * The new array contains the same elements of the input array plus the given element
	 * in the last position. The component type of the new array is the same as that of
	 * the input array.
	 *
	 * <p>
	 * If the input array is {@code null}, a new one element array is returned whose
	 * component type is the same as the element.
	 *
	 * <pre>
	 * ArrayUtils.add(null, '0')       = ['0']
	 * ArrayUtils.add(['1'], '0')      = ['1', '0']
	 * ArrayUtils.add(['1', '0'], '1') = ['1', '0', '1']
	 * </pre>
	 * @param array the array to copy and add the element to, may be {@code null}
	 * @param element the object to add at the last index of the new array
	 * @return A new array containing the existing elements plus the new element
	 * @since 2.1
	 */
	public static char[] add(char[] array, char element) {
		char[] newArray = (char[]) copyArrayGrow1(array, Character.TYPE);
		newArray[newArray.length - 1] = element;
		return newArray;
	}

	/**
	 * Inserts the specified element at the specified position in the array. Shifts the
	 * element currently at that position (if any) and any subsequent elements to the
	 * right (adds one to their indices).
	 *
	 * <p>
	 * This method returns a new array with the same elements of the input array plus the
	 * given element on the specified position. The component type of the returned array
	 * is always the same as that of the input array.
	 *
	 * <p>
	 * If the input array is {@code null}, a new one element array is returned whose
	 * component type is the same as the element.
	 *
	 * <pre>
	 * ArrayUtils.add(null, 0, 'a')            = ['a']
	 * ArrayUtils.add(['a'], 0, 'b')           = ['b', 'a']
	 * ArrayUtils.add(['a', 'b'], 0, 'c')      = ['c', 'a', 'b']
	 * ArrayUtils.add(['a', 'b'], 1, 'k')      = ['a', 'k', 'b']
	 * ArrayUtils.add(['a', 'b', 'c'], 1, 't') = ['a', 't', 'b', 'c']
	 * </pre>
	 * @param array the array to add the element to, may be {@code null}
	 * @param index the position of the new object
	 * @param element the object to add
	 * @return A new array containing the existing elements and the new element
	 * @throws IndexOutOfBoundsException if the index is out of range (index &lt; 0 ||
	 * index &gt; array.length).
	 * @deprecated this method has been superseded by
	 * {@link #insert(int, char[], char...)} and may be removed in a future release.
	 * Please note the handling of {@code null} input arrays differs in the new method:
	 * inserting {@code X} into a {@code null} array results in {@code null} not
	 * {@code X}.
	 */
	@Deprecated
	public static char[] add(char[] array, int index, char element) {
		return (char[]) add(array, index, Character.valueOf(element), Character.TYPE);
	}

	/**
	 * Copies the given array and adds the given element at the end of the new array.
	 *
	 * <p>
	 * The new array contains the same elements of the input array plus the given element
	 * in the last position. The component type of the new array is the same as that of
	 * the input array.
	 *
	 * <p>
	 * If the input array is {@code null}, a new one element array is returned whose
	 * component type is the same as the element.
	 *
	 * <pre>
	 * ArrayUtils.add(null, 0)   = [0]
	 * ArrayUtils.add([1], 0)    = [1, 0]
	 * ArrayUtils.add([1, 0], 1) = [1, 0, 1]
	 * </pre>
	 * @param array the array to copy and add the element to, may be {@code null}
	 * @param element the object to add at the last index of the new array
	 * @return A new array containing the existing elements plus the new element
	 * @since 2.1
	 */
	public static double[] add(double[] array, double element) {
		double[] newArray = (double[]) copyArrayGrow1(array, Double.TYPE);
		newArray[newArray.length - 1] = element;
		return newArray;
	}

	/**
	 * Inserts the specified element at the specified position in the array. Shifts the
	 * element currently at that position (if any) and any subsequent elements to the
	 * right (adds one to their indices).
	 *
	 * <p>
	 * This method returns a new array with the same elements of the input array plus the
	 * given element on the specified position. The component type of the returned array
	 * is always the same as that of the input array.
	 *
	 * <p>
	 * If the input array is {@code null}, a new one element array is returned whose
	 * component type is the same as the element.
	 *
	 * <pre>
	 * ArrayUtils.add([1.1], 0, 2.2)              = [2.2, 1.1]
	 * ArrayUtils.add([2.3, 6.4], 2, 10.5)        = [2.3, 6.4, 10.5]
	 * ArrayUtils.add([2.6, 6.7], 0, -4.8)        = [-4.8, 2.6, 6.7]
	 * ArrayUtils.add([2.9, 6.0, 0.3], 2, 1.0)    = [2.9, 6.0, 1.0, 0.3]
	 * </pre>
	 * @param array the array to add the element to, may be {@code null}
	 * @param index the position of the new object
	 * @param element the object to add
	 * @return A new array containing the existing elements and the new element
	 * @throws IndexOutOfBoundsException if the index is out of range (index &lt; 0 ||
	 * index &gt; array.length).
	 * @deprecated this method has been superseded by
	 * {@link #insert(int, double[], double...)} and may be removed in a future release.
	 * Please note the handling of {@code null} input arrays differs in the new method:
	 * inserting {@code X} into a {@code null} array results in {@code
	 *     null} not {@code X}.
	 */
	@Deprecated
	public static double[] add(double[] array, int index, double element) {
		return (double[]) add(array, index, Double.valueOf(element), Double.TYPE);
	}

	/**
	 * Copies the given array and adds the given element at the end of the new array.
	 *
	 * <p>
	 * The new array contains the same elements of the input array plus the given element
	 * in the last position. The component type of the new array is the same as that of
	 * the input array.
	 *
	 * <p>
	 * If the input array is {@code null}, a new one element array is returned whose
	 * component type is the same as the element.
	 *
	 * <pre>
	 * ArrayUtils.add(null, 0)   = [0]
	 * ArrayUtils.add([1], 0)    = [1, 0]
	 * ArrayUtils.add([1, 0], 1) = [1, 0, 1]
	 * </pre>
	 * @param array the array to copy and add the element to, may be {@code null}
	 * @param element the object to add at the last index of the new array
	 * @return A new array containing the existing elements plus the new element
	 * @since 2.1
	 */
	public static float[] add(float[] array, float element) {
		float[] newArray = (float[]) copyArrayGrow1(array, Float.TYPE);
		newArray[newArray.length - 1] = element;
		return newArray;
	}

	/**
	 * Inserts the specified element at the specified position in the array. Shifts the
	 * element currently at that position (if any) and any subsequent elements to the
	 * right (adds one to their indices).
	 *
	 * <p>
	 * This method returns a new array with the same elements of the input array plus the
	 * given element on the specified position. The component type of the returned array
	 * is always the same as that of the input array.
	 *
	 * <p>
	 * If the input array is {@code null}, a new one element array is returned whose
	 * component type is the same as the element.
	 *
	 * <pre>
	 * ArrayUtils.add([1.1f], 0, 2.2f)               = [2.2f, 1.1f]
	 * ArrayUtils.add([2.3f, 6.4f], 2, 10.5f)        = [2.3f, 6.4f, 10.5f]
	 * ArrayUtils.add([2.6f, 6.7f], 0, -4.8f)        = [-4.8f, 2.6f, 6.7f]
	 * ArrayUtils.add([2.9f, 6.0f, 0.3f], 2, 1.0f)   = [2.9f, 6.0f, 1.0f, 0.3f]
	 * </pre>
	 * @param array the array to add the element to, may be {@code null}
	 * @param index the position of the new object
	 * @param element the object to add
	 * @return A new array containing the existing elements and the new element
	 * @throws IndexOutOfBoundsException if the index is out of range (index &lt; 0 ||
	 * index &gt; array.length).
	 * @deprecated this method has been superseded by
	 * {@link #insert(int, float[], float...)} and may be removed in a future release.
	 * Please note the handling of {@code null} input arrays differs in the new method:
	 * inserting {@code X} into a {@code null} array results in {@code
	 *     null} not {@code X}.
	 */
	@Deprecated
	public static float[] add(float[] array, int index, float element) {
		return (float[]) add(array, index, Float.valueOf(element), Float.TYPE);
	}

	/**
	 * Copies the given array and adds the given element at the end of the new array.
	 *
	 * <p>
	 * The new array contains the same elements of the input array plus the given element
	 * in the last position. The component type of the new array is the same as that of
	 * the input array.
	 *
	 * <p>
	 * If the input array is {@code null}, a new one element array is returned whose
	 * component type is the same as the element.
	 *
	 * <pre>
	 * ArrayUtils.add(null, 0)   = [0]
	 * ArrayUtils.add([1], 0)    = [1, 0]
	 * ArrayUtils.add([1, 0], 1) = [1, 0, 1]
	 * </pre>
	 * @param array the array to copy and add the element to, may be {@code null}
	 * @param element the object to add at the last index of the new array
	 * @return A new array containing the existing elements plus the new element
	 * @since 2.1
	 */
	public static int[] add(int[] array, int element) {
		int[] newArray = (int[]) copyArrayGrow1(array, Integer.TYPE);
		newArray[newArray.length - 1] = element;
		return newArray;
	}

	/**
	 * Inserts the specified element at the specified position in the array. Shifts the
	 * element currently at that position (if any) and any subsequent elements to the
	 * right (adds one to their indices).
	 *
	 * <p>
	 * This method returns a new array with the same elements of the input array plus the
	 * given element on the specified position. The component type of the returned array
	 * is always the same as that of the input array.
	 *
	 * <p>
	 * If the input array is {@code null}, a new one element array is returned whose
	 * component type is the same as the element.
	 *
	 * <pre>
	 * ArrayUtils.add([1], 0, 2)         = [2, 1]
	 * ArrayUtils.add([2, 6], 2, 10)     = [2, 6, 10]
	 * ArrayUtils.add([2, 6], 0, -4)     = [-4, 2, 6]
	 * ArrayUtils.add([2, 6, 3], 2, 1)   = [2, 6, 1, 3]
	 * </pre>
	 * @param array the array to add the element to, may be {@code null}
	 * @param index the position of the new object
	 * @param element the object to add
	 * @return A new array containing the existing elements and the new element
	 * @throws IndexOutOfBoundsException if the index is out of range (index &lt; 0 ||
	 * index &gt; array.length).
	 * @deprecated this method has been superseded by {@link #insert(int, int[], int...)}
	 * and may be removed in a future release. Please note the handling of {@code null}
	 * input arrays differs in the new method: inserting {@code X} into a {@code null}
	 * array results in {@code null} not {@code X}.
	 */
	@Deprecated
	public static int[] add(int[] array, int index, int element) {
		return (int[]) add(array, index, Integer.valueOf(element), Integer.TYPE);
	}

	/**
	 * Inserts the specified element at the specified position in the array. Shifts the
	 * element currently at that position (if any) and any subsequent elements to the
	 * right (adds one to their indices).
	 *
	 * <p>
	 * This method returns a new array with the same elements of the input array plus the
	 * given element on the specified position. The component type of the returned array
	 * is always the same as that of the input array.
	 *
	 * <p>
	 * If the input array is {@code null}, a new one element array is returned whose
	 * component type is the same as the element.
	 *
	 * <pre>
	 * ArrayUtils.add([1L], 0, 2L)           = [2L, 1L]
	 * ArrayUtils.add([2L, 6L], 2, 10L)      = [2L, 6L, 10L]
	 * ArrayUtils.add([2L, 6L], 0, -4L)      = [-4L, 2L, 6L]
	 * ArrayUtils.add([2L, 6L, 3L], 2, 1L)   = [2L, 6L, 1L, 3L]
	 * </pre>
	 * @param array the array to add the element to, may be {@code null}
	 * @param index the position of the new object
	 * @param element the object to add
	 * @return A new array containing the existing elements and the new element
	 * @throws IndexOutOfBoundsException if the index is out of range (index &lt; 0 ||
	 * index &gt; array.length).
	 * @deprecated this method has been superseded by
	 * {@link #insert(int, long[], long...)} and may be removed in a future release.
	 * Please note the handling of {@code null} input arrays differs in the new method:
	 * inserting {@code X} into a {@code null} array results in {@code null} not
	 * {@code X}.
	 */
	@Deprecated
	public static long[] add(long[] array, int index, long element) {
		return (long[]) add(array, index, Long.valueOf(element), Long.TYPE);
	}

	/**
	 * Copies the given array and adds the given element at the end of the new array.
	 *
	 * <p>
	 * The new array contains the same elements of the input array plus the given element
	 * in the last position. The component type of the new array is the same as that of
	 * the input array.
	 *
	 * <p>
	 * If the input array is {@code null}, a new one element array is returned whose
	 * component type is the same as the element.
	 *
	 * <pre>
	 * ArrayUtils.add(null, 0)   = [0]
	 * ArrayUtils.add([1], 0)    = [1, 0]
	 * ArrayUtils.add([1, 0], 1) = [1, 0, 1]
	 * </pre>
	 * @param array the array to copy and add the element to, may be {@code null}
	 * @param element the object to add at the last index of the new array
	 * @return A new array containing the existing elements plus the new element
	 * @since 2.1
	 */
	public static long[] add(long[] array, long element) {
		long[] newArray = (long[]) copyArrayGrow1(array, Long.TYPE);
		newArray[newArray.length - 1] = element;
		return newArray;
	}

	/**
	 * Underlying implementation of add(array, index, element) methods. The last parameter
	 * is the class, which may not equal element.getClass for primitives.
	 * @param array the array to add the element to, may be {@code null}
	 * @param index the position of the new object
	 * @param element the object to add
	 * @param clss the type of the element being added
	 * @return A new array containing the existing elements and the new element
	 */
	private static Object add(Object array, int index, Object element, Class<?> clss) {
		if (array == null) {
			if (index != 0) {
				throw new IndexOutOfBoundsException("Index: " + index + ", Length: 0");
			}
			Object joinedArray = Array.newInstance(clss, 1);
			Array.set(joinedArray, 0, element);
			return joinedArray;
		}
		int length = Array.getLength(array);
		if (index > length || index < 0) {
			throw new IndexOutOfBoundsException("Index: " + index + ", Length: " + length);
		}
		Object result = Array.newInstance(clss, length + 1);
		System.arraycopy(array, 0, result, 0, index);
		Array.set(result, index, element);
		if (index < length) {
			System.arraycopy(array, index, result, index + 1, length - index);
		}
		return result;
	}

	/**
	 * Inserts the specified element at the specified position in the array. Shifts the
	 * element currently at that position (if any) and any subsequent elements to the
	 * right (adds one to their indices).
	 *
	 * <p>
	 * This method returns a new array with the same elements of the input array plus the
	 * given element on the specified position. The component type of the returned array
	 * is always the same as that of the input array.
	 *
	 * <p>
	 * If the input array is {@code null}, a new one element array is returned whose
	 * component type is the same as the element.
	 *
	 * <pre>
	 * ArrayUtils.add([1], 0, 2)         = [2, 1]
	 * ArrayUtils.add([2, 6], 2, 10)     = [2, 6, 10]
	 * ArrayUtils.add([2, 6], 0, -4)     = [-4, 2, 6]
	 * ArrayUtils.add([2, 6, 3], 2, 1)   = [2, 6, 1, 3]
	 * </pre>
	 * @param array the array to add the element to, may be {@code null}
	 * @param index the position of the new object
	 * @param element the object to add
	 * @return A new array containing the existing elements and the new element
	 * @throws IndexOutOfBoundsException if the index is out of range (index &lt; 0 ||
	 * index &gt; array.length).
	 * @deprecated this method has been superseded by
	 * {@link #insert(int, short[], short...)} and may be removed in a future release.
	 * Please note the handling of {@code null} input arrays differs in the new method:
	 * inserting {@code X} into a {@code null} array results in {@code
	 *     null} not {@code X}.
	 */
	@Deprecated
	public static short[] add(short[] array, int index, short element) {
		return (short[]) add(array, index, Short.valueOf(element), Short.TYPE);
	}

	/**
	 * Copies the given array and adds the given element at the end of the new array.
	 *
	 * <p>
	 * The new array contains the same elements of the input array plus the given element
	 * in the last position. The component type of the new array is the same as that of
	 * the input array.
	 *
	 * <p>
	 * If the input array is {@code null}, a new one element array is returned whose
	 * component type is the same as the element.
	 *
	 * <pre>
	 * ArrayUtils.add(null, 0)   = [0]
	 * ArrayUtils.add([1], 0)    = [1, 0]
	 * ArrayUtils.add([1, 0], 1) = [1, 0, 1]
	 * </pre>
	 * @param array the array to copy and add the element to, may be {@code null}
	 * @param element the object to add at the last index of the new array
	 * @return A new array containing the existing elements plus the new element
	 * @since 2.1
	 */
	public static short[] add(short[] array, short element) {
		short[] newArray = (short[]) copyArrayGrow1(array, Short.TYPE);
		newArray[newArray.length - 1] = element;
		return newArray;
	}

	/**
	 * Inserts the specified element at the specified position in the array. Shifts the
	 * element currently at that position (if any) and any subsequent elements to the
	 * right (adds one to their indices).
	 *
	 * <p>
	 * This method returns a new array with the same elements of the input array plus the
	 * given element on the specified position. The component type of the returned array
	 * is always the same as that of the input array.
	 *
	 * <p>
	 * If the input array is {@code null}, a new one element array is returned whose
	 * component type is the same as the element.
	 *
	 * <pre>
	 * ArrayUtils.add(null, 0, null)      = IllegalArgumentException
	 * ArrayUtils.add(null, 0, "a")       = ["a"]
	 * ArrayUtils.add(["a"], 1, null)     = ["a", null]
	 * ArrayUtils.add(["a"], 1, "b")      = ["a", "b"]
	 * ArrayUtils.add(["a", "b"], 3, "c") = ["a", "b", "c"]
	 * </pre>
	 * @param <T> the component type of the array
	 * @param array the array to add the element to, may be {@code null}
	 * @param index the position of the new object
	 * @param element the object to add
	 * @return A new array containing the existing elements and the new element
	 * @throws IndexOutOfBoundsException if the index is out of range (index &lt; 0 ||
	 * index &gt; array.length).
	 * @throws IllegalArgumentException if both array and element are null
	 * @deprecated this method has been superseded by
	 * {@link #insert(int, Object[], Object...) insert(int, T[], T...)} and may be removed
	 * in a future release. Please note the handling of {@code null} input arrays differs
	 * in the new method: inserting {@code X} into a {@code
	 *     null} array results in {@code null} not {@code X}.
	 */
	@Deprecated
	public static <T> T[] add(T[] array, int index, T element) {
		Class<?> clss = null;
		if (array != null) {
			clss = array.getClass().getComponentType();
		}
		else if (element != null) {
			clss = element.getClass();
		}
		else {
			throw new IllegalArgumentException("Array and element cannot both be null");
		}
		// the add method creates an array of type clss,
		// which is type T
		T[] newArray = (T[]) add(array, index, element, clss);
		return newArray;
	}

	/**
	 * Copies the given array and adds the given element at the end of the new array.
	 *
	 * <p>
	 * The new array contains the same elements of the input array plus the given element
	 * in the last position. The component type of the new array is the same as that of
	 * the input array.
	 *
	 * <p>
	 * If the input array is {@code null}, a new one element array is returned whose
	 * component type is the same as the element, unless the element itself is null, in
	 * which case the return type is Object[]
	 *
	 * <pre>
	 * ArrayUtils.add(null, null)      = IllegalArgumentException
	 * ArrayUtils.add(null, "a")       = ["a"]
	 * ArrayUtils.add(["a"], null)     = ["a", null]
	 * ArrayUtils.add(["a"], "b")      = ["a", "b"]
	 * ArrayUtils.add(["a", "b"], "c") = ["a", "b", "c"]
	 * </pre>
	 * @param <T> the component type of the array
	 * @param array the array to "add" the element to, may be {@code null}
	 * @param element the object to add, may be {@code null}
	 * @return A new array containing the existing elements plus the new element The
	 * returned array type will be that of the input array (unless null), in which case it
	 * will have the same type as the element. If both are null, an
	 * IllegalArgumentException is thrown
	 * @since 2.1
	 * @throws IllegalArgumentException if both arguments are null
	 */
	public static <T> T[] add(T[] array, T element) {
		Class<?> type;
		if (array != null) {
			type = array.getClass().getComponentType();
		}
		else if (element != null) {
			type = element.getClass();
		}
		else {
			throw new IllegalArgumentException("Arguments cannot both be null");
		}
		// type must be T
		T[] newArray = (T[]) copyArrayGrow1(array, type);
		newArray[newArray.length - 1] = element;
		return newArray;
	}

	/**
	 * Adds all the elements of the given arrays into a new array.
	 *
	 * <p>
	 * The new array contains all of the element of {@code array1} followed by all of the
	 * elements {@code array2}. When an array is returned, it is always a new array.
	 *
	 * <pre>
	 * ArrayUtils.addAll(array1, null)   = cloned copy of array1
	 * ArrayUtils.addAll(null, array2)   = cloned copy of array2
	 * ArrayUtils.addAll([], [])         = []
	 * </pre>
	 * @param array1 the first array whose elements are added to the new array.
	 * @param array2 the second array whose elements are added to the new array.
	 * @return The new boolean[] array.
	 * @since 2.1
	 */
	public static boolean[] addAll(boolean[] array1, boolean... array2) {
		if (array1 == null) {
			return clone(array2);
		}
		else if (array2 == null) {
			return clone(array1);
		}
		boolean[] joinedArray = new boolean[array1.length + array2.length];
		System.arraycopy(array1, 0, joinedArray, 0, array1.length);
		System.arraycopy(array2, 0, joinedArray, array1.length, array2.length);
		return joinedArray;
	}

	/**
	 * Adds all the elements of the given arrays into a new array.
	 *
	 * <p>
	 * The new array contains all of the element of {@code array1} followed by all of the
	 * elements {@code array2}. When an array is returned, it is always a new array.
	 *
	 * <pre>
	 * ArrayUtils.addAll(array1, null)   = cloned copy of array1
	 * ArrayUtils.addAll(null, array2)   = cloned copy of array2
	 * ArrayUtils.addAll([], [])         = []
	 * </pre>
	 * @param array1 the first array whose elements are added to the new array.
	 * @param array2 the second array whose elements are added to the new array.
	 * @return The new byte[] array.
	 * @since 2.1
	 */
	public static byte[] addAll(byte[] array1, byte... array2) {
		if (array1 == null) {
			return clone(array2);
		}
		else if (array2 == null) {
			return clone(array1);
		}
		byte[] joinedArray = new byte[array1.length + array2.length];
		System.arraycopy(array1, 0, joinedArray, 0, array1.length);
		System.arraycopy(array2, 0, joinedArray, array1.length, array2.length);
		return joinedArray;
	}

	/**
	 * Adds all the elements of the given arrays into a new array.
	 *
	 * <p>
	 * The new array contains all of the element of {@code array1} followed by all of the
	 * elements {@code array2}. When an array is returned, it is always a new array.
	 *
	 * <pre>
	 * ArrayUtils.addAll(array1, null)   = cloned copy of array1
	 * ArrayUtils.addAll(null, array2)   = cloned copy of array2
	 * ArrayUtils.addAll([], [])         = []
	 * </pre>
	 * @param array1 the first array whose elements are added to the new array.
	 * @param array2 the second array whose elements are added to the new array.
	 * @return The new char[] array.
	 * @since 2.1
	 */
	public static char[] addAll(char[] array1, char... array2) {
		if (array1 == null) {
			return clone(array2);
		}
		else if (array2 == null) {
			return clone(array1);
		}
		char[] joinedArray = new char[array1.length + array2.length];
		System.arraycopy(array1, 0, joinedArray, 0, array1.length);
		System.arraycopy(array2, 0, joinedArray, array1.length, array2.length);
		return joinedArray;
	}

	/**
	 * Adds all the elements of the given arrays into a new array.
	 *
	 * <p>
	 * The new array contains all of the element of {@code array1} followed by all of the
	 * elements {@code array2}. When an array is returned, it is always a new array.
	 *
	 * <pre>
	 * ArrayUtils.addAll(array1, null)   = cloned copy of array1
	 * ArrayUtils.addAll(null, array2)   = cloned copy of array2
	 * ArrayUtils.addAll([], [])         = []
	 * </pre>
	 * @param array1 the first array whose elements are added to the new array.
	 * @param array2 the second array whose elements are added to the new array.
	 * @return The new double[] array.
	 * @since 2.1
	 */
	public static double[] addAll(double[] array1, double... array2) {
		if (array1 == null) {
			return clone(array2);
		}
		else if (array2 == null) {
			return clone(array1);
		}
		double[] joinedArray = new double[array1.length + array2.length];
		System.arraycopy(array1, 0, joinedArray, 0, array1.length);
		System.arraycopy(array2, 0, joinedArray, array1.length, array2.length);
		return joinedArray;
	}

	/**
	 * Adds all the elements of the given arrays into a new array.
	 *
	 * <p>
	 * The new array contains all of the element of {@code array1} followed by all of the
	 * elements {@code array2}. When an array is returned, it is always a new array.
	 *
	 * <pre>
	 * ArrayUtils.addAll(array1, null)   = cloned copy of array1
	 * ArrayUtils.addAll(null, array2)   = cloned copy of array2
	 * ArrayUtils.addAll([], [])         = []
	 * </pre>
	 * @param array1 the first array whose elements are added to the new array.
	 * @param array2 the second array whose elements are added to the new array.
	 * @return The new float[] array.
	 * @since 2.1
	 */
	public static float[] addAll(float[] array1, float... array2) {
		if (array1 == null) {
			return clone(array2);
		}
		else if (array2 == null) {
			return clone(array1);
		}
		float[] joinedArray = new float[array1.length + array2.length];
		System.arraycopy(array1, 0, joinedArray, 0, array1.length);
		System.arraycopy(array2, 0, joinedArray, array1.length, array2.length);
		return joinedArray;
	}

	/**
	 * Adds all the elements of the given arrays into a new array.
	 *
	 * <p>
	 * The new array contains all of the element of {@code array1} followed by all of the
	 * elements {@code array2}. When an array is returned, it is always a new array.
	 *
	 * <pre>
	 * ArrayUtils.addAll(array1, null)   = cloned copy of array1
	 * ArrayUtils.addAll(null, array2)   = cloned copy of array2
	 * ArrayUtils.addAll([], [])         = []
	 * </pre>
	 * @param array1 the first array whose elements are added to the new array.
	 * @param array2 the second array whose elements are added to the new array.
	 * @return The new int[] array.
	 * @since 2.1
	 */
	public static int[] addAll(int[] array1, int... array2) {
		if (array1 == null) {
			return clone(array2);
		}
		else if (array2 == null) {
			return clone(array1);
		}
		int[] joinedArray = new int[array1.length + array2.length];
		System.arraycopy(array1, 0, joinedArray, 0, array1.length);
		System.arraycopy(array2, 0, joinedArray, array1.length, array2.length);
		return joinedArray;
	}

	/**
	 * Adds all the elements of the given arrays into a new array.
	 *
	 * <p>
	 * The new array contains all of the element of {@code array1} followed by all of the
	 * elements {@code array2}. When an array is returned, it is always a new array.
	 *
	 * <pre>
	 * ArrayUtils.addAll(array1, null)   = cloned copy of array1
	 * ArrayUtils.addAll(null, array2)   = cloned copy of array2
	 * ArrayUtils.addAll([], [])         = []
	 * </pre>
	 * @param array1 the first array whose elements are added to the new array.
	 * @param array2 the second array whose elements are added to the new array.
	 * @return The new long[] array.
	 * @since 2.1
	 */
	public static long[] addAll(long[] array1, long... array2) {
		if (array1 == null) {
			return clone(array2);
		}
		else if (array2 == null) {
			return clone(array1);
		}
		long[] joinedArray = new long[array1.length + array2.length];
		System.arraycopy(array1, 0, joinedArray, 0, array1.length);
		System.arraycopy(array2, 0, joinedArray, array1.length, array2.length);
		return joinedArray;
	}

	/**
	 * Adds all the elements of the given arrays into a new array.
	 *
	 * <p>
	 * The new array contains all of the element of {@code array1} followed by all of the
	 * elements {@code array2}. When an array is returned, it is always a new array.
	 *
	 * <pre>
	 * ArrayUtils.addAll(array1, null)   = cloned copy of array1
	 * ArrayUtils.addAll(null, array2)   = cloned copy of array2
	 * ArrayUtils.addAll([], [])         = []
	 * </pre>
	 * @param array1 the first array whose elements are added to the new array.
	 * @param array2 the second array whose elements are added to the new array.
	 * @return The new short[] array.
	 * @since 2.1
	 */
	public static short[] addAll(short[] array1, short... array2) {
		if (array1 == null) {
			return clone(array2);
		}
		else if (array2 == null) {
			return clone(array1);
		}
		short[] joinedArray = new short[array1.length + array2.length];
		System.arraycopy(array1, 0, joinedArray, 0, array1.length);
		System.arraycopy(array2, 0, joinedArray, array1.length, array2.length);
		return joinedArray;
	}

	/**
	 * Adds all the elements of the given arrays into a new array.
	 *
	 * <p>
	 * The new array contains all of the element of {@code array1} followed by all of the
	 * elements {@code array2}. When an array is returned, it is always a new array.
	 *
	 * <pre>
	 * ArrayUtils.addAll(null, null)     = null
	 * ArrayUtils.addAll(array1, null)   = cloned copy of array1
	 * ArrayUtils.addAll(null, array2)   = cloned copy of array2
	 * ArrayUtils.addAll([], [])         = []
	 * ArrayUtils.addAll([null], [null]) = [null, null]
	 * ArrayUtils.addAll(["a", "b", "c"], ["1", "2", "3"]) = ["a", "b", "c", "1", "2", "3"]
	 * </pre>
	 * @param <T> the component type of the array
	 * @param array1 the first array whose elements are added to the new array, may be
	 * {@code null}
	 * @param array2 the second array whose elements are added to the new array, may be
	 * {@code null}
	 * @return The new array, {@code null} if both arrays are {@code null}. The type of
	 * the new array is the type of the first array, unless the first array is null, in
	 * which case the type is the same as the second array.
	 * @since 2.1
	 * @throws IllegalArgumentException if the array types are incompatible
	 */
	public static <T> T[] addAll(T[] array1, T... array2) {
		if (array1 == null) {
			return clone(array2);
		}
		else if (array2 == null) {
			return clone(array1);
		}
		Class<?> type1 = array1.getClass().getComponentType();
		// OK, because array is of type T
		T[] joinedArray = (T[]) Array.newInstance(type1, array1.length + array2.length);
		System.arraycopy(array1, 0, joinedArray, 0, array1.length);
		try {
			System.arraycopy(array2, 0, joinedArray, array1.length, array2.length);
		}
		catch (ArrayStoreException ase) {
			// Check if problem was due to incompatible types
			/*
			 * We do this here, rather than before the copy because: - it would be a
			 * wasted check most of the time - safer, in case check turns out to be too
			 * strict
			 */
			Class<?> type2 = array2.getClass().getComponentType();
			if (!type1.isAssignableFrom(type2)) {
				throw new IllegalArgumentException(
						"Cannot store " + type2.getName() + " in an array of " + type1.getName(), ase);
			}
			throw ase; // No, so rethrow original
		}
		return joinedArray;
	}

	/**
	 * Copies the given array and adds the given element at the beginning of the new
	 * array.
	 *
	 * <p>
	 * The new array contains the same elements of the input array plus the given element
	 * in the first position. The component type of the new array is the same as that of
	 * the input array.
	 *
	 * <p>
	 * If the input array is {@code null}, a new one element array is returned whose
	 * component type is the same as the element.
	 *
	 * <pre>
	 * ArrayUtils.add(null, true)          = [true]
	 * ArrayUtils.add([true], false)       = [false, true]
	 * ArrayUtils.add([true, false], true) = [true, true, false]
	 * </pre>
	 * @param array the array to "add" the element to, may be {@code null}.
	 * @param element the object to add.
	 * @return A new array containing the existing elements plus the new element The
	 * returned array type will be that of the input array (unless null), in which case it
	 * will have the same type as the element.
	 * @since 3.10
	 */
	public static boolean[] addFirst(boolean[] array, boolean element) {
		return array == null ? add(array, element) : insert(0, array, element);
	}

	/**
	 * Copies the given array and adds the given element at the beginning of the new
	 * array.
	 *
	 * <p>
	 * The new array contains the same elements of the input array plus the given element
	 * in the first position. The component type of the new array is the same as that of
	 * the input array.
	 *
	 * <p>
	 * If the input array is {@code null}, a new one element array is returned whose
	 * component type is the same as the element.
	 *
	 * <pre>
	 * ArrayUtils.add(null, 1)   = [1]
	 * ArrayUtils.add([1], 0)    = [0, 1]
	 * ArrayUtils.add([1, 0], 1) = [1, 1, 0]
	 * </pre>
	 * @param array the array to "add" the element to, may be {@code null}.
	 * @param element the object to add.
	 * @return A new array containing the existing elements plus the new element The
	 * returned array type will be that of the input array (unless null), in which case it
	 * will have the same type as the element.
	 * @since 3.10
	 */
	public static byte[] addFirst(byte[] array, byte element) {
		return array == null ? add(array, element) : insert(0, array, element);
	}

	/**
	 * Copies the given array and adds the given element at the beginning of the new
	 * array.
	 *
	 * <p>
	 * The new array contains the same elements of the input array plus the given element
	 * in the first position. The component type of the new array is the same as that of
	 * the input array.
	 *
	 * <p>
	 * If the input array is {@code null}, a new one element array is returned whose
	 * component type is the same as the element.
	 *
	 * <pre>
	 * ArrayUtils.add(null, '1')       = ['1']
	 * ArrayUtils.add(['1'], '0')      = ['0', '1']
	 * ArrayUtils.add(['1', '0'], '1') = ['1', '1', '0']
	 * </pre>
	 * @param array the array to "add" the element to, may be {@code null}.
	 * @param element the object to add.
	 * @return A new array containing the existing elements plus the new element The
	 * returned array type will be that of the input array (unless null), in which case it
	 * will have the same type as the element.
	 * @since 3.10
	 */
	public static char[] addFirst(char[] array, char element) {
		return array == null ? add(array, element) : insert(0, array, element);
	}

	/**
	 * Copies the given array and adds the given element at the beginning of the new
	 * array.
	 *
	 * <p>
	 * The new array contains the same elements of the input array plus the given element
	 * in the first position. The component type of the new array is the same as that of
	 * the input array.
	 *
	 * <p>
	 * If the input array is {@code null}, a new one element array is returned whose
	 * component type is the same as the element.
	 *
	 * <pre>
	 * ArrayUtils.add(null, 1)   = [1]
	 * ArrayUtils.add([1], 0)    = [0, 1]
	 * ArrayUtils.add([1, 0], 1) = [1, 1, 0]
	 * </pre>
	 * @param array the array to "add" the element to, may be {@code null}.
	 * @param element the object to add.
	 * @return A new array containing the existing elements plus the new element The
	 * returned array type will be that of the input array (unless null), in which case it
	 * will have the same type as the element.
	 * @since 3.10
	 */
	public static double[] addFirst(double[] array, double element) {
		return array == null ? add(array, element) : insert(0, array, element);
	}

	/**
	 * Copies the given array and adds the given element at the beginning of the new
	 * array.
	 *
	 * <p>
	 * The new array contains the same elements of the input array plus the given element
	 * in the first position. The component type of the new array is the same as that of
	 * the input array.
	 *
	 * <p>
	 * If the input array is {@code null}, a new one element array is returned whose
	 * component type is the same as the element.
	 *
	 * <pre>
	 * ArrayUtils.add(null, 1)   = [1]
	 * ArrayUtils.add([1], 0)    = [0, 1]
	 * ArrayUtils.add([1, 0], 1) = [1, 1, 0]
	 * </pre>
	 * @param array the array to "add" the element to, may be {@code null}.
	 * @param element the object to add.
	 * @return A new array containing the existing elements plus the new element The
	 * returned array type will be that of the input array (unless null), in which case it
	 * will have the same type as the element.
	 * @since 3.10
	 */
	public static float[] addFirst(float[] array, float element) {
		return array == null ? add(array, element) : insert(0, array, element);
	}

	/**
	 * Copies the given array and adds the given element at the beginning of the new
	 * array.
	 *
	 * <p>
	 * The new array contains the same elements of the input array plus the given element
	 * in the first position. The component type of the new array is the same as that of
	 * the input array.
	 *
	 * <p>
	 * If the input array is {@code null}, a new one element array is returned whose
	 * component type is the same as the element.
	 *
	 * <pre>
	 * ArrayUtils.add(null, 1)   = [1]
	 * ArrayUtils.add([1], 0)    = [0, 1]
	 * ArrayUtils.add([1, 0], 1) = [1, 1, 0]
	 * </pre>
	 * @param array the array to "add" the element to, may be {@code null}.
	 * @param element the object to add.
	 * @return A new array containing the existing elements plus the new element The
	 * returned array type will be that of the input array (unless null), in which case it
	 * will have the same type as the element.
	 * @since 3.10
	 */
	public static int[] addFirst(int[] array, int element) {
		return array == null ? add(array, element) : insert(0, array, element);
	}

	/**
	 * Copies the given array and adds the given element at the beginning of the new
	 * array.
	 *
	 * <p>
	 * The new array contains the same elements of the input array plus the given element
	 * in the first position. The component type of the new array is the same as that of
	 * the input array.
	 *
	 * <p>
	 * If the input array is {@code null}, a new one element array is returned whose
	 * component type is the same as the element.
	 *
	 * <pre>
	 * ArrayUtils.add(null, 1)   = [1]
	 * ArrayUtils.add([1], 0)    = [0, 1]
	 * ArrayUtils.add([1, 0], 1) = [1, 1, 0]
	 * </pre>
	 * @param array the array to "add" the element to, may be {@code null}.
	 * @param element the object to add.
	 * @return A new array containing the existing elements plus the new element The
	 * returned array type will be that of the input array (unless null), in which case it
	 * will have the same type as the element.
	 * @since 3.10
	 */
	public static long[] addFirst(long[] array, long element) {
		return array == null ? add(array, element) : insert(0, array, element);
	}

	/**
	 * Copies the given array and adds the given element at the beginning of the new
	 * array.
	 *
	 * <p>
	 * The new array contains the same elements of the input array plus the given element
	 * in the first position. The component type of the new array is the same as that of
	 * the input array.
	 *
	 * <p>
	 * If the input array is {@code null}, a new one element array is returned whose
	 * component type is the same as the element.
	 *
	 * <pre>
	 * ArrayUtils.add(null, 1)   = [1]
	 * ArrayUtils.add([1], 0)    = [0, 1]
	 * ArrayUtils.add([1, 0], 1) = [1, 1, 0]
	 * </pre>
	 * @param array the array to "add" the element to, may be {@code null}.
	 * @param element the object to add.
	 * @return A new array containing the existing elements plus the new element The
	 * returned array type will be that of the input array (unless null), in which case it
	 * will have the same type as the element.
	 * @since 3.10
	 */
	public static short[] addFirst(short[] array, short element) {
		return array == null ? add(array, element) : insert(0, array, element);
	}

	/**
	 * Copies the given array and adds the given element at the beginning of the new
	 * array.
	 *
	 * <p>
	 * The new array contains the same elements of the input array plus the given element
	 * in the first positioaddFirstaddFirstaddFirstn. The component type of the new array
	 * is the same as that of the input array.
	 *
	 * <p>
	 * If the input array is {@code null}, a new one element array is returned whose
	 * component type is the same as the element, unless the element itself is null, in
	 * which case the return type is Object[]
	 *
	 * <pre>
	 * ArrayUtils.add(null, null)      = IllegalArgumentException
	 * ArrayUtils.add(null, "a")       = ["a"]
	 * ArrayUtils.add(["a"], null)     = [null, "a"]
	 * ArrayUtils.add(["a"], "b")      = ["b", "a"]
	 * ArrayUtils.add(["a", "b"], "c") = ["c", "a", "b"]
	 * </pre>
	 * @param <T> the component type of the array
	 * @param array the array to "add" the element to, may be {@code null}
	 * @param element the object to add, may be {@code null}
	 * @return A new array containing the existing elements plus the new element The
	 * returned array type will be that of the input array (unless null), in which case it
	 * will have the same type as the element. If both are null, an
	 * IllegalArgumentException is thrown
	 * @since 3.10
	 * @throws IllegalArgumentException if both arguments are null
	 */
	public static <T> T[] addFirst(T[] array, T element) {
		return array == null ? add(array, element) : insert(0, array, element);
	}

	/**
	 * Clones an array returning a typecast result and handling {@code null}.
	 *
	 * <p>
	 * This method returns {@code null} for a {@code null} input array.
	 * @param array the array to clone, may be {@code null}
	 * @return the cloned array, {@code null} if {@code null} input
	 */
	public static boolean[] clone(boolean[] array) {
		if (array == null) {
			return null;
		}
		return array.clone();
	}

	/**
	 * Clones an array returning a typecast result and handling {@code null}.
	 *
	 * <p>
	 * This method returns {@code null} for a {@code null} input array.
	 * @param array the array to clone, may be {@code null}
	 * @return the cloned array, {@code null} if {@code null} input
	 */
	public static byte[] clone(byte[] array) {
		if (array == null) {
			return null;
		}
		return array.clone();
	}

	/**
	 * Clones an array returning a typecast result and handling {@code null}.
	 *
	 * <p>
	 * This method returns {@code null} for a {@code null} input array.
	 * @param array the array to clone, may be {@code null}
	 * @return the cloned array, {@code null} if {@code null} input
	 */
	public static char[] clone(char[] array) {
		if (array == null) {
			return null;
		}
		return array.clone();
	}

	/**
	 * Clones an array returning a typecast result and handling {@code null}.
	 *
	 * <p>
	 * This method returns {@code null} for a {@code null} input array.
	 * @param array the array to clone, may be {@code null}
	 * @return the cloned array, {@code null} if {@code null} input
	 */
	public static double[] clone(double[] array) {
		if (array == null) {
			return null;
		}
		return array.clone();
	}

	/**
	 * Clones an array returning a typecast result and handling {@code null}.
	 *
	 * <p>
	 * This method returns {@code null} for a {@code null} input array.
	 * @param array the array to clone, may be {@code null}
	 * @return the cloned array, {@code null} if {@code null} input
	 */
	public static float[] clone(float[] array) {
		if (array == null) {
			return null;
		}
		return array.clone();
	}

	/**
	 * Clones an array returning a typecast result and handling {@code null}.
	 *
	 * <p>
	 * This method returns {@code null} for a {@code null} input array.
	 * @param array the array to clone, may be {@code null}
	 * @return the cloned array, {@code null} if {@code null} input
	 */
	public static int[] clone(int[] array) {
		if (array == null) {
			return null;
		}
		return array.clone();
	}

	/**
	 * Clones an array returning a typecast result and handling {@code null}.
	 *
	 * <p>
	 * This method returns {@code null} for a {@code null} input array.
	 * @param array the array to clone, may be {@code null}
	 * @return the cloned array, {@code null} if {@code null} input
	 */
	public static long[] clone(long[] array) {
		if (array == null) {
			return null;
		}
		return array.clone();
	}

	/**
	 * Clones an array returning a typecast result and handling {@code null}.
	 *
	 * <p>
	 * This method returns {@code null} for a {@code null} input array.
	 * @param array the array to clone, may be {@code null}
	 * @return the cloned array, {@code null} if {@code null} input
	 */
	public static short[] clone(short[] array) {
		if (array == null) {
			return null;
		}
		return array.clone();
	}

	// Clone
	// -----------------------------------------------------------------------
	/**
	 * Shallow clones an array returning a typecast result and handling {@code null}.
	 *
	 * <p>
	 * The objects in the array are not cloned, thus there is no special handling for
	 * multi-dimensional arrays.
	 *
	 * <p>
	 * This method returns {@code null} for a {@code null} input array.
	 * @param <T> the component type of the array
	 * @param array the array to shallow clone, may be {@code null}
	 * @return the cloned array, {@code null} if {@code null} input
	 */
	public static <T> T[] clone(T[] array) {
		if (array == null) {
			return null;
		}
		return array.clone();
	}

	/**
	 * Checks if the value is in the given array.
	 *
	 * <p>
	 * The method returns {@code false} if a {@code null} array is passed in.
	 * @param array the array to search through
	 * @param valueToFind the value to find
	 * @return {@code true} if the array contains the object
	 */
	public static boolean contains(boolean[] array, boolean valueToFind) {
		return indexOf(array, valueToFind) != INDEX_NOT_FOUND;
	}

	/**
	 * Checks if the value is in the given array.
	 *
	 * <p>
	 * The method returns {@code false} if a {@code null} array is passed in.
	 * @param array the array to search through
	 * @param valueToFind the value to find
	 * @return {@code true} if the array contains the object
	 */
	public static boolean contains(byte[] array, byte valueToFind) {
		return indexOf(array, valueToFind) != INDEX_NOT_FOUND;
	}

	/**
	 * Checks if the value is in the given array.
	 *
	 * <p>
	 * The method returns {@code false} if a {@code null} array is passed in.
	 * @param array the array to search through
	 * @param valueToFind the value to find
	 * @return {@code true} if the array contains the object
	 * @since 2.1
	 */
	public static boolean contains(char[] array, char valueToFind) {
		return indexOf(array, valueToFind) != INDEX_NOT_FOUND;
	}

	/**
	 * Checks if the value is in the given array.
	 *
	 * <p>
	 * The method returns {@code false} if a {@code null} array is passed in.
	 * @param array the array to search through
	 * @param valueToFind the value to find
	 * @return {@code true} if the array contains the object
	 */
	public static boolean contains(double[] array, double valueToFind) {
		return indexOf(array, valueToFind) != INDEX_NOT_FOUND;
	}

	/**
	 * Checks if a value falling within the given tolerance is in the given array. If the
	 * array contains a value within the inclusive range defined by (value - tolerance) to
	 * (value + tolerance).
	 *
	 * <p>
	 * The method returns {@code false} if a {@code null} array is passed in.
	 * @param array the array to search
	 * @param valueToFind the value to find
	 * @param tolerance the array contains the tolerance of the search
	 * @return true if value falling within tolerance is in array
	 */
	public static boolean contains(double[] array, double valueToFind, double tolerance) {
		return indexOf(array, valueToFind, 0, tolerance) != INDEX_NOT_FOUND;
	}

	/**
	 * Checks if the value is in the given array.
	 *
	 * <p>
	 * The method returns {@code false} if a {@code null} array is passed in.
	 * @param array the array to search through
	 * @param valueToFind the value to find
	 * @return {@code true} if the array contains the object
	 */
	public static boolean contains(float[] array, float valueToFind) {
		return indexOf(array, valueToFind) != INDEX_NOT_FOUND;
	}

	/**
	 * Checks if the value is in the given array.
	 *
	 * <p>
	 * The method returns {@code false} if a {@code null} array is passed in.
	 * @param array the array to search through
	 * @param valueToFind the value to find
	 * @return {@code true} if the array contains the object
	 */
	public static boolean contains(int[] array, int valueToFind) {
		return indexOf(array, valueToFind) != INDEX_NOT_FOUND;
	}

	/**
	 * Checks if the value is in the given array.
	 *
	 * <p>
	 * The method returns {@code false} if a {@code null} array is passed in.
	 * @param array the array to search through
	 * @param valueToFind the value to find
	 * @return {@code true} if the array contains the object
	 */
	public static boolean contains(long[] array, long valueToFind) {
		return indexOf(array, valueToFind) != INDEX_NOT_FOUND;
	}

	/**
	 * Checks if the object is in the given array.
	 *
	 * <p>
	 * The method returns {@code false} if a {@code null} array is passed in.
	 * @param array the array to search through
	 * @param objectToFind the object to find
	 * @return {@code true} if the array contains the object
	 */
	public static boolean contains(Object[] array, Object objectToFind) {
		return indexOf(array, objectToFind) != INDEX_NOT_FOUND;
	}

	/**
	 * Checks if the value is in the given array.
	 *
	 * <p>
	 * The method returns {@code false} if a {@code null} array is passed in.
	 * @param array the array to search through
	 * @param valueToFind the value to find
	 * @return {@code true} if the array contains the object
	 */
	public static boolean contains(short[] array, short valueToFind) {
		return indexOf(array, valueToFind) != INDEX_NOT_FOUND;
	}

	/**
	 * Returns a copy of the given array of size 1 greater than the argument. The last
	 * value of the array is left to the default value.
	 * @param array The array to copy, must not be {@code null}.
	 * @param newArrayComponentType If {@code array} is {@code null}, create a size 1
	 * array of this type.
	 * @return A new copy of the array of size 1 greater than the input.
	 */
	private static Object copyArrayGrow1(Object array, Class<?> newArrayComponentType) {
		if (array != null) {
			int arrayLength = Array.getLength(array);
			Object newArray = Array.newInstance(array.getClass().getComponentType(), arrayLength + 1);
			System.arraycopy(array, 0, newArray, 0, arrayLength);
			return newArray;
		}
		return Array.newInstance(newArrayComponentType, 1);
	}

	// -----------------------------------------------------------------------
	/**
	 * Returns the length of the specified array. This method can deal with {@code Object}
	 * arrays and with primitive arrays.
	 *
	 * <p>
	 * If the input array is {@code null}, {@code 0} is returned.
	 *
	 * <pre>
	 * ArrayUtils.getLength(null)            = 0
	 * ArrayUtils.getLength([])              = 0
	 * ArrayUtils.getLength([null])          = 1
	 * ArrayUtils.getLength([true, false])   = 2
	 * ArrayUtils.getLength([1, 2, 3])       = 3
	 * ArrayUtils.getLength(["a", "b", "c"]) = 3
	 * </pre>
	 * @param array the array to retrieve the length from, may be null
	 * @return The length of the array, or {@code 0} if the array is {@code null}
	 * @throws IllegalArgumentException if the object argument is not an array.
	 * @since 2.1
	 */
	public static int getLength(Object array) {
		if (array == null) {
			return 0;
		}
		return Array.getLength(array);
	}

	/**
	 * Finds the indices of the given value in the array.
	 *
	 * <p>
	 * This method returns an empty BitSet for a {@code null} input array.
	 * @param array the array to search through for the object, may be {@code null}
	 * @param valueToFind the value to find
	 * @return a BitSet of all the the indices of the value within the array, an empty
	 * BitSet if not found or {@code null} array input
	 * @since 3.10
	 */
	public static BitSet indexesOf(boolean[] array, boolean valueToFind) {
		return indexesOf(array, valueToFind, 0);
	}

	/**
	 * Finds the indices of the given value in the array starting at the given index.
	 *
	 * <p>
	 * This method returns an empty BitSet for a {@code null} input array.
	 *
	 * <p>
	 * A negative startIndex is treated as zero. A startIndex larger than the array length
	 * will return an empty BitSet ({@code -1}).
	 * @param array the array to search through for the object, may be {@code null}
	 * @param valueToFind the value to find
	 * @param startIndex the index to start searching at
	 * @return a BitSet of all the indices of the value within the array, an empty BitSet
	 * if not found or {@code null} array input
	 * @since 3.10
	 */
	public static BitSet indexesOf(boolean[] array, boolean valueToFind, int startIndex) {
		BitSet bitSet = new BitSet();

		if (array == null) {
			return bitSet;
		}

		while (startIndex < array.length) {
			startIndex = indexOf(array, valueToFind, startIndex);

			if (startIndex == INDEX_NOT_FOUND) {
				break;
			}

			bitSet.set(startIndex);
			++startIndex;
		}

		return bitSet;
	}

	/**
	 * Finds the indices of the given value in the array.
	 *
	 * <p>
	 * This method returns an empty BitSet for a {@code null} input array.
	 * @param array the array to search through for the object, may be {@code null}
	 * @param valueToFind the value to find
	 * @return a BitSet of all the indices of the value within the array, an empty BitSet
	 * if not found or {@code null} array input
	 * @since 3.10
	 */
	public static BitSet indexesOf(byte[] array, byte valueToFind) {
		return indexesOf(array, valueToFind, 0);
	}

	/**
	 * Finds the indices of the given value in the array starting at the given index.
	 *
	 * <p>
	 * This method returns an empty BitSet for a {@code null} input array.
	 *
	 * <p>
	 * A negative startIndex is treated as zero. A startIndex larger than the array length
	 * will return an empty BitSet.
	 * @param array the array to search through for the object, may be {@code null}
	 * @param valueToFind the value to find
	 * @param startIndex the index to start searching at
	 * @return a BitSet of all the indices of the value within the array, an empty BitSet
	 * if not found or {@code null} array input
	 * @since 3.10
	 */
	public static BitSet indexesOf(byte[] array, byte valueToFind, int startIndex) {
		BitSet bitSet = new BitSet();

		if (array == null) {
			return bitSet;
		}

		while (startIndex < array.length) {
			startIndex = indexOf(array, valueToFind, startIndex);

			if (startIndex == INDEX_NOT_FOUND) {
				break;
			}

			bitSet.set(startIndex);
			++startIndex;
		}

		return bitSet;
	}

	/**
	 * Finds the indices of the given value in the array.
	 *
	 * <p>
	 * This method returns an empty BitSet for a {@code null} input array.
	 * @param array the array to search through for the object, may be {@code null}
	 * @param valueToFind the value to find
	 * @return a BitSet of all the indices of the value within the array, an empty BitSet
	 * if not found or {@code null} array input
	 * @since 3.10
	 */
	public static BitSet indexesOf(char[] array, char valueToFind) {
		return indexesOf(array, valueToFind, 0);
	}

	/**
	 * Finds the indices of the given value in the array starting at the given index.
	 *
	 * <p>
	 * This method returns an empty BitSet for a {@code null} input array.
	 *
	 * <p>
	 * A negative startIndex is treated as zero. A startIndex larger than the array length
	 * will return an empty BitSet.
	 * @param array the array to search through for the object, may be {@code null}
	 * @param valueToFind the value to find
	 * @param startIndex the index to start searching at
	 * @return a BitSet of all the indices of the value within the array, an empty BitSet
	 * if not found or {@code null} array input
	 * @since 3.10
	 */
	public static BitSet indexesOf(char[] array, char valueToFind, int startIndex) {
		BitSet bitSet = new BitSet();

		if (array == null) {
			return bitSet;
		}

		while (startIndex < array.length) {
			startIndex = indexOf(array, valueToFind, startIndex);

			if (startIndex == INDEX_NOT_FOUND) {
				break;
			}

			bitSet.set(startIndex);
			++startIndex;
		}

		return bitSet;
	}

	/**
	 * Finds the indices of the given value in the array.
	 *
	 * <p>
	 * This method returns empty BitSet for a {@code null} input array.
	 * @param array the array to search through for the object, may be {@code null}
	 * @param valueToFind the value to find
	 * @return a BitSet of all the indices of the value within the array, an empty BitSet
	 * if not found or {@code null} array input
	 * @since 3.10
	 */
	public static BitSet indexesOf(double[] array, double valueToFind) {
		return indexesOf(array, valueToFind, 0);
	}

	/**
	 * Finds the indices of the given value within a given tolerance in the array.
	 *
	 * <p>
	 * This method will return all the indices of the value which fall between the region
	 * defined by valueToFind - tolerance and valueToFind + tolerance, each time between
	 * the nearest integers.
	 *
	 * <p>
	 * This method returns an empty BitSet for a {@code null} input array.
	 * @param array the array to search through for the object, may be {@code null}
	 * @param valueToFind the value to find
	 * @param tolerance tolerance of the search
	 * @return a BitSet of all the indices of the value within the array, an empty BitSet
	 * if not found or {@code null} array input
	 * @since 3.10
	 */
	public static BitSet indexesOf(double[] array, double valueToFind, double tolerance) {
		return indexesOf(array, valueToFind, 0, tolerance);
	}

	/**
	 * Finds the indices of the given value in the array starting at the given index.
	 *
	 * <p>
	 * This method returns an empty BitSet for a {@code null} input array.
	 *
	 * <p>
	 * A negative startIndex is treated as zero. A startIndex larger than the array length
	 * will return an empty BitSet.
	 * @param array the array to search through for the object, may be {@code null}
	 * @param valueToFind the value to find
	 * @param startIndex the index to start searching at
	 * @return a BitSet of the indices of the value within the array, an empty BitSet if
	 * not found or {@code null} array input
	 * @since 3.10
	 */
	public static BitSet indexesOf(double[] array, double valueToFind, int startIndex) {
		BitSet bitSet = new BitSet();

		if (array == null) {
			return bitSet;
		}

		while (startIndex < array.length) {
			startIndex = indexOf(array, valueToFind, startIndex);

			if (startIndex == INDEX_NOT_FOUND) {
				break;
			}

			bitSet.set(startIndex);
			++startIndex;
		}

		return bitSet;
	}

	/**
	 * Finds the indices of the given value in the array starting at the given index.
	 *
	 * <p>
	 * This method will return the indices of the values which fall between the region
	 * defined by valueToFind - tolerance and valueToFind + tolerance, between the nearest
	 * integers.
	 *
	 * <p>
	 * This method returns an empty BitSet for a {@code null} input array.
	 *
	 * <p>
	 * A negative startIndex is treated as zero. A startIndex larger than the array length
	 * will return an empty BitSet.
	 * @param array the array to search through for the object, may be {@code null}
	 * @param valueToFind the value to find
	 * @param startIndex the index to start searching at
	 * @param tolerance tolerance of the search
	 * @return a BitSet of the indices of the value within the array, an empty BitSet if
	 * not found or {@code null} array input
	 * @since 3.10
	 */
	public static BitSet indexesOf(double[] array, double valueToFind, int startIndex,
			double tolerance) {
		BitSet bitSet = new BitSet();

		if (array == null) {
			return bitSet;
		}

		while (startIndex < array.length) {
			startIndex = indexOf(array, valueToFind, startIndex, tolerance);

			if (startIndex == INDEX_NOT_FOUND) {
				break;
			}

			bitSet.set(startIndex);
			++startIndex;
		}

		return bitSet;
	}

	/**
	 * Finds the indices of the given value in the array.
	 *
	 * <p>
	 * This method returns an empty BitSet for a {@code null} input array.
	 * @param array the array to search through for the object, may be {@code null}
	 * @param valueToFind the value to find
	 * @return a BitSet of all the indices of the value within the array, an empty BitSet
	 * if not found or {@code null} array input
	 * @since 3.10
	 */
	public static BitSet indexesOf(float[] array, float valueToFind) {
		return indexesOf(array, valueToFind, 0);
	}

	/**
	 * Finds the indices of the given value in the array starting at the given index.
	 *
	 * <p>
	 * This method returns an empty BitSet for a {@code null} input array.
	 *
	 * <p>
	 * A negative startIndex is treated as zero. A startIndex larger than the array length
	 * will return empty BitSet.
	 * @param array the array to search through for the object, may be {@code null}
	 * @param valueToFind the value to find
	 * @param startIndex the index to start searching at
	 * @return a BitSet of all the indices of the value within the array, an empty BitSet
	 * if not found or {@code null} array input
	 * @since 3.10
	 */
	public static BitSet indexesOf(float[] array, float valueToFind, int startIndex) {
		BitSet bitSet = new BitSet();

		if (array == null) {
			return bitSet;
		}

		while (startIndex < array.length) {
			startIndex = indexOf(array, valueToFind, startIndex);

			if (startIndex == INDEX_NOT_FOUND) {
				break;
			}

			bitSet.set(startIndex);
			++startIndex;
		}

		return bitSet;
	}

	/**
	 * Finds the indices of the given value in the array.
	 *
	 * <p>
	 * This method returns an empty BitSet for a {@code null} input array.
	 * @param array the array to search through for the object, may be {@code null}
	 * @param valueToFind the value to find
	 * @return a BitSet of all the indices of the value within the array, an empty BitSet
	 * if not found or {@code null} array input
	 * @since 3.10
	 */
	public static BitSet indexesOf(int[] array, int valueToFind) {
		return indexesOf(array, valueToFind, 0);
	}

	/**
	 * Finds the indices of the given value in the array starting at the given index.
	 *
	 * <p>
	 * This method returns an empty BitSet for a {@code null} input array.
	 *
	 * <p>
	 * A negative startIndex is treated as zero. A startIndex larger than the array length
	 * will return an empty BitSet.
	 * @param array the array to search through for the object, may be {@code null}
	 * @param valueToFind the value to find
	 * @param startIndex the index to start searching at
	 * @return a BitSet of all the indices of the value within the array, an empty BitSet
	 * if not found or {@code null} array input
	 * @since 3.10
	 */
	public static BitSet indexesOf(int[] array, int valueToFind, int startIndex) {
		BitSet bitSet = new BitSet();

		if (array == null) {
			return bitSet;
		}

		while (startIndex < array.length) {
			startIndex = indexOf(array, valueToFind, startIndex);

			if (startIndex == INDEX_NOT_FOUND) {
				break;
			}

			bitSet.set(startIndex);
			++startIndex;
		}

		return bitSet;
	}

	/**
	 * Finds the indices of the given value in the array.
	 *
	 * <p>
	 * This method returns an empty BitSet for a {@code null} input array.
	 * @param array the array to search through for the object, may be {@code null}
	 * @param valueToFind the value to find
	 * @return a BitSet of all the indices of the value within the array, an empty BitSet
	 * if not found or {@code null} array input
	 * @since 3.10
	 */
	public static BitSet indexesOf(long[] array, long valueToFind) {
		return indexesOf(array, valueToFind, 0);
	}

	/**
	 * Finds the indices of the given value in the array starting at the given index.
	 *
	 * <p>
	 * This method returns an empty BitSet for a {@code null} input array.
	 *
	 * <p>
	 * A negative startIndex is treated as zero. A startIndex larger than the array length
	 * will return an empty BitSet.
	 * @param array the array to search through for the object, may be {@code null}
	 * @param valueToFind the value to find
	 * @param startIndex the index to start searching at
	 * @return a BitSet of all the indices of the value within the array, an empty BitSet
	 * if not found or {@code null} array input
	 * @since 3.10
	 */
	public static BitSet indexesOf(long[] array, long valueToFind, int startIndex) {
		BitSet bitSet = new BitSet();

		if (array == null) {
			return bitSet;
		}

		while (startIndex < array.length) {
			startIndex = indexOf(array, valueToFind, startIndex);

			if (startIndex == INDEX_NOT_FOUND) {
				break;
			}

			bitSet.set(startIndex);
			++startIndex;
		}

		return bitSet;
	}

	/**
	 * Finds the indices of the given object in the array.
	 *
	 * <p>
	 * This method returns an empty BitSet for a {@code null} input array.
	 * @param array the array to search through for the object, may be {@code null}
	 * @param objectToFind the object to find, may be {@code null}
	 * @return a BitSet of all the indices of the object within the array, an empty BitSet
	 * if not found or {@code null} array input
	 * @since 3.10
	 */
	public static BitSet indexesOf(Object[] array, Object objectToFind) {
		return indexesOf(array, objectToFind, 0);
	}

	/**
	 * Finds the indices of the given object in the array starting at the given index.
	 *
	 * <p>
	 * This method returns an empty BitSet for a {@code null} input array.
	 *
	 * <p>
	 * A negative startIndex is treated as zero. A startIndex larger than the array length
	 * will return an empty BitSet.
	 * @param array the array to search through for the object, may be {@code null}
	 * @param objectToFind the object to find, may be {@code null}
	 * @param startIndex the index to start searching at
	 * @return a BitSet of all the indices of the object within the array starting at the
	 * index, an empty BitSet if not found or {@code null} array input
	 * @since 3.10
	 */
	public static BitSet indexesOf(Object[] array, Object objectToFind, int startIndex) {
		BitSet bitSet = new BitSet();

		if (array == null) {
			return bitSet;
		}

		while (startIndex < array.length) {
			startIndex = indexOf(array, objectToFind, startIndex);

			if (startIndex == INDEX_NOT_FOUND) {
				break;
			}

			bitSet.set(startIndex);
			++startIndex;
		}

		return bitSet;
	}

	/**
	 * Finds the indices of the given value in the array.
	 *
	 * <p>
	 * This method returns an empty BitSet for a {@code null} input array.
	 * @param array the array to search through for the object, may be {@code null}
	 * @param valueToFind the value to find
	 * @return a BitSet of all the indices of the value within the array, an empty BitSet
	 * if not found or {@code null} array input
	 * @since 3.10
	 */
	public static BitSet indexesOf(short[] array, short valueToFind) {
		return indexesOf(array, valueToFind, 0);
	}

	/**
	 * Finds the indices of the given value in the array starting at the given index.
	 *
	 * <p>
	 * This method returns an empty BitSet for a {@code null} input array.
	 *
	 * <p>
	 * A negative startIndex is treated as zero. A startIndex larger than the array length
	 * will return an empty BitSet.
	 * @param array the array to search through for the object, may be {@code null}
	 * @param valueToFind the value to find
	 * @param startIndex the index to start searching at
	 * @return a BitSet of all the indices of the value within the array, an empty BitSet
	 * if not found or {@code null} array input
	 * @since 3.10
	 */
	public static BitSet indexesOf(short[] array, short valueToFind, int startIndex) {
		BitSet bitSet = new BitSet();

		if (array == null) {
			return bitSet;
		}

		while (startIndex < array.length) {
			startIndex = indexOf(array, valueToFind, startIndex);

			if (startIndex == INDEX_NOT_FOUND) {
				break;
			}

			bitSet.set(startIndex);
			++startIndex;
		}

		return bitSet;
	}

	// boolean IndexOf
	// -----------------------------------------------------------------------
	/**
	 * Finds the index of the given value in the array.
	 *
	 * <p>
	 * This method returns {@link #INDEX_NOT_FOUND} ({@code -1}) for a {@code null} input
	 * array.
	 * @param array the array to search through for the object, may be {@code null}
	 * @param valueToFind the value to find
	 * @return the index of the value within the array, {@link #INDEX_NOT_FOUND}
	 * ({@code -1}) if not found or {@code null} array input
	 */
	public static int indexOf(boolean[] array, boolean valueToFind) {
		return indexOf(array, valueToFind, 0);
	}

	/**
	 * Finds the index of the given value in the array starting at the given index.
	 *
	 * <p>
	 * This method returns {@link #INDEX_NOT_FOUND} ({@code -1}) for a {@code null} input
	 * array.
	 *
	 * <p>
	 * A negative startIndex is treated as zero. A startIndex larger than the array length
	 * will return {@link #INDEX_NOT_FOUND} ({@code -1}).
	 * @param array the array to search through for the object, may be {@code null}
	 * @param valueToFind the value to find
	 * @param startIndex the index to start searching at
	 * @return the index of the value within the array, {@link #INDEX_NOT_FOUND}
	 * ({@code -1}) if not found or {@code null} array input
	 */
	public static int indexOf(boolean[] array, boolean valueToFind, int startIndex) {
		if (isEmpty(array)) {
			return INDEX_NOT_FOUND;
		}
		if (startIndex < 0) {
			startIndex = 0;
		}
		for (int i = startIndex; i < array.length; i++) {
			if (valueToFind == array[i]) {
				return i;
			}
		}
		return INDEX_NOT_FOUND;
	}

	// byte IndexOf
	// -----------------------------------------------------------------------
	/**
	 * Finds the index of the given value in the array.
	 *
	 * <p>
	 * This method returns {@link #INDEX_NOT_FOUND} ({@code -1}) for a {@code null} input
	 * array.
	 * @param array the array to search through for the object, may be {@code null}
	 * @param valueToFind the value to find
	 * @return the index of the value within the array, {@link #INDEX_NOT_FOUND}
	 * ({@code -1}) if not found or {@code null} array input
	 */
	public static int indexOf(byte[] array, byte valueToFind) {
		return indexOf(array, valueToFind, 0);
	}

	/**
	 * Finds the index of the given value in the array starting at the given index.
	 *
	 * <p>
	 * This method returns {@link #INDEX_NOT_FOUND} ({@code -1}) for a {@code null} input
	 * array.
	 *
	 * <p>
	 * A negative startIndex is treated as zero. A startIndex larger than the array length
	 * will return {@link #INDEX_NOT_FOUND} ({@code -1}).
	 * @param array the array to search through for the object, may be {@code null}
	 * @param valueToFind the value to find
	 * @param startIndex the index to start searching at
	 * @return the index of the value within the array, {@link #INDEX_NOT_FOUND}
	 * ({@code -1}) if not found or {@code null} array input
	 */
	public static int indexOf(byte[] array, byte valueToFind, int startIndex) {
		if (array == null) {
			return INDEX_NOT_FOUND;
		}
		if (startIndex < 0) {
			startIndex = 0;
		}
		for (int i = startIndex; i < array.length; i++) {
			if (valueToFind == array[i]) {
				return i;
			}
		}
		return INDEX_NOT_FOUND;
	}

	// char IndexOf
	// -----------------------------------------------------------------------
	/**
	 * Finds the index of the given value in the array.
	 *
	 * <p>
	 * This method returns {@link #INDEX_NOT_FOUND} ({@code -1}) for a {@code null} input
	 * array.
	 * @param array the array to search through for the object, may be {@code null}
	 * @param valueToFind the value to find
	 * @return the index of the value within the array, {@link #INDEX_NOT_FOUND}
	 * ({@code -1}) if not found or {@code null} array input
	 * @since 2.1
	 */
	public static int indexOf(char[] array, char valueToFind) {
		return indexOf(array, valueToFind, 0);
	}

	/**
	 * Finds the index of the given value in the array starting at the given index.
	 *
	 * <p>
	 * This method returns {@link #INDEX_NOT_FOUND} ({@code -1}) for a {@code null} input
	 * array.
	 *
	 * <p>
	 * A negative startIndex is treated as zero. A startIndex larger than the array length
	 * will return {@link #INDEX_NOT_FOUND} ({@code -1}).
	 * @param array the array to search through for the object, may be {@code null}
	 * @param valueToFind the value to find
	 * @param startIndex the index to start searching at
	 * @return the index of the value within the array, {@link #INDEX_NOT_FOUND}
	 * ({@code -1}) if not found or {@code null} array input
	 * @since 2.1
	 */
	public static int indexOf(char[] array, char valueToFind, int startIndex) {
		if (array == null) {
			return INDEX_NOT_FOUND;
		}
		if (startIndex < 0) {
			startIndex = 0;
		}
		for (int i = startIndex; i < array.length; i++) {
			if (valueToFind == array[i]) {
				return i;
			}
		}
		return INDEX_NOT_FOUND;
	}

	// double IndexOf
	// -----------------------------------------------------------------------
	/**
	 * Finds the index of the given value in the array.
	 *
	 * <p>
	 * This method returns {@link #INDEX_NOT_FOUND} ({@code -1}) for a {@code null} input
	 * array.
	 * @param array the array to search through for the object, may be {@code null}
	 * @param valueToFind the value to find
	 * @return the index of the value within the array, {@link #INDEX_NOT_FOUND}
	 * ({@code -1}) if not found or {@code null} array input
	 */
	public static int indexOf(double[] array, double valueToFind) {
		return indexOf(array, valueToFind, 0);
	}

	/**
	 * Finds the index of the given value within a given tolerance in the array. This
	 * method will return the index of the first value which falls between the region
	 * defined by valueToFind - tolerance and valueToFind + tolerance.
	 *
	 * <p>
	 * This method returns {@link #INDEX_NOT_FOUND} ({@code -1}) for a {@code null} input
	 * array.
	 * @param array the array to search through for the object, may be {@code null}
	 * @param valueToFind the value to find
	 * @param tolerance tolerance of the search
	 * @return the index of the value within the array, {@link #INDEX_NOT_FOUND}
	 * ({@code -1}) if not found or {@code null} array input
	 */
	public static int indexOf(double[] array, double valueToFind, double tolerance) {
		return indexOf(array, valueToFind, 0, tolerance);
	}

	/**
	 * Finds the index of the given value in the array starting at the given index.
	 *
	 * <p>
	 * This method returns {@link #INDEX_NOT_FOUND} ({@code -1}) for a {@code null} input
	 * array.
	 *
	 * <p>
	 * A negative startIndex is treated as zero. A startIndex larger than the array length
	 * will return {@link #INDEX_NOT_FOUND} ({@code -1}).
	 * @param array the array to search through for the object, may be {@code null}
	 * @param valueToFind the value to find
	 * @param startIndex the index to start searching at
	 * @return the index of the value within the array, {@link #INDEX_NOT_FOUND}
	 * ({@code -1}) if not found or {@code null} array input
	 */
	public static int indexOf(double[] array, double valueToFind, int startIndex) {
		if (isEmpty(array)) {
			return INDEX_NOT_FOUND;
		}
		if (startIndex < 0) {
			startIndex = 0;
		}
		for (int i = startIndex; i < array.length; i++) {
			if (valueToFind == array[i]) {
				return i;
			}
		}
		return INDEX_NOT_FOUND;
	}

	/**
	 * Finds the index of the given value in the array starting at the given index. This
	 * method will return the index of the first value which falls between the region
	 * defined by valueToFind - tolerance and valueToFind + tolerance.
	 *
	 * <p>
	 * This method returns {@link #INDEX_NOT_FOUND} ({@code -1}) for a {@code null} input
	 * array.
	 *
	 * <p>
	 * A negative startIndex is treated as zero. A startIndex larger than the array length
	 * will return {@link #INDEX_NOT_FOUND} ({@code -1}).
	 * @param array the array to search through for the object, may be {@code null}
	 * @param valueToFind the value to find
	 * @param startIndex the index to start searching at
	 * @param tolerance tolerance of the search
	 * @return the index of the value within the array, {@link #INDEX_NOT_FOUND}
	 * ({@code -1}) if not found or {@code null} array input
	 */
	public static int indexOf(double[] array, double valueToFind, int startIndex, double tolerance) {
		if (isEmpty(array)) {
			return INDEX_NOT_FOUND;
		}
		if (startIndex < 0) {
			startIndex = 0;
		}
		double min = valueToFind - tolerance;
		double max = valueToFind + tolerance;
		for (int i = startIndex; i < array.length; i++) {
			if (array[i] >= min && array[i] <= max) {
				return i;
			}
		}
		return INDEX_NOT_FOUND;
	}

	// float IndexOf
	// -----------------------------------------------------------------------
	/**
	 * Finds the index of the given value in the array.
	 *
	 * <p>
	 * This method returns {@link #INDEX_NOT_FOUND} ({@code -1}) for a {@code null} input
	 * array.
	 * @param array the array to search through for the object, may be {@code null}
	 * @param valueToFind the value to find
	 * @return the index of the value within the array, {@link #INDEX_NOT_FOUND}
	 * ({@code -1}) if not found or {@code null} array input
	 */
	public static int indexOf(float[] array, float valueToFind) {
		return indexOf(array, valueToFind, 0);
	}

	/**
	 * Finds the index of the given value in the array starting at the given index.
	 *
	 * <p>
	 * This method returns {@link #INDEX_NOT_FOUND} ({@code -1}) for a {@code null} input
	 * array.
	 *
	 * <p>
	 * A negative startIndex is treated as zero. A startIndex larger than the array length
	 * will return {@link #INDEX_NOT_FOUND} ({@code -1}).
	 * @param array the array to search through for the object, may be {@code null}
	 * @param valueToFind the value to find
	 * @param startIndex the index to start searching at
	 * @return the index of the value within the array, {@link #INDEX_NOT_FOUND}
	 * ({@code -1}) if not found or {@code null} array input
	 */
	public static int indexOf(float[] array, float valueToFind, int startIndex) {
		if (isEmpty(array)) {
			return INDEX_NOT_FOUND;
		}
		if (startIndex < 0) {
			startIndex = 0;
		}
		for (int i = startIndex; i < array.length; i++) {
			if (valueToFind == array[i]) {
				return i;
			}
		}
		return INDEX_NOT_FOUND;
	}

	// int IndexOf
	// -----------------------------------------------------------------------
	/**
	 * Finds the index of the given value in the array.
	 *
	 * <p>
	 * This method returns {@link #INDEX_NOT_FOUND} ({@code -1}) for a {@code null} input
	 * array.
	 * @param array the array to search through for the object, may be {@code null}
	 * @param valueToFind the value to find
	 * @return the index of the value within the array, {@link #INDEX_NOT_FOUND}
	 * ({@code -1}) if not found or {@code null} array input
	 */
	public static int indexOf(int[] array, int valueToFind) {
		return indexOf(array, valueToFind, 0);
	}

	/**
	 * Finds the index of the given value in the array starting at the given index.
	 *
	 * <p>
	 * This method returns {@link #INDEX_NOT_FOUND} ({@code -1}) for a {@code null} input
	 * array.
	 *
	 * <p>
	 * A negative startIndex is treated as zero. A startIndex larger than the array length
	 * will return {@link #INDEX_NOT_FOUND} ({@code -1}).
	 * @param array the array to search through for the object, may be {@code null}
	 * @param valueToFind the value to find
	 * @param startIndex the index to start searching at
	 * @return the index of the value within the array, {@link #INDEX_NOT_FOUND}
	 * ({@code -1}) if not found or {@code null} array input
	 */
	public static int indexOf(int[] array, int valueToFind, int startIndex) {
		if (array == null) {
			return INDEX_NOT_FOUND;
		}
		if (startIndex < 0) {
			startIndex = 0;
		}
		for (int i = startIndex; i < array.length; i++) {
			if (valueToFind == array[i]) {
				return i;
			}
		}
		return INDEX_NOT_FOUND;
	}

	// long IndexOf
	// -----------------------------------------------------------------------
	/**
	 * Finds the index of the given value in the array.
	 *
	 * <p>
	 * This method returns {@link #INDEX_NOT_FOUND} ({@code -1}) for a {@code null} input
	 * array.
	 * @param array the array to search through for the object, may be {@code null}
	 * @param valueToFind the value to find
	 * @return the index of the value within the array, {@link #INDEX_NOT_FOUND}
	 * ({@code -1}) if not found or {@code null} array input
	 */
	public static int indexOf(long[] array, long valueToFind) {
		return indexOf(array, valueToFind, 0);
	}

	/**
	 * Finds the index of the given value in the array starting at the given index.
	 *
	 * <p>
	 * This method returns {@link #INDEX_NOT_FOUND} ({@code -1}) for a {@code null} input
	 * array.
	 *
	 * <p>
	 * A negative startIndex is treated as zero. A startIndex larger than the array length
	 * will return {@link #INDEX_NOT_FOUND} ({@code -1}).
	 * @param array the array to search through for the object, may be {@code null}
	 * @param valueToFind the value to find
	 * @param startIndex the index to start searching at
	 * @return the index of the value within the array, {@link #INDEX_NOT_FOUND}
	 * ({@code -1}) if not found or {@code null} array input
	 */
	public static int indexOf(long[] array, long valueToFind, int startIndex) {
		if (array == null) {
			return INDEX_NOT_FOUND;
		}
		if (startIndex < 0) {
			startIndex = 0;
		}
		for (int i = startIndex; i < array.length; i++) {
			if (valueToFind == array[i]) {
				return i;
			}
		}
		return INDEX_NOT_FOUND;
	}

	// Object IndexOf
	// -----------------------------------------------------------------------
	/**
	 * Finds the index of the given object in the array.
	 *
	 * <p>
	 * This method returns {@link #INDEX_NOT_FOUND} ({@code -1}) for a {@code null} input
	 * array.
	 * @param array the array to search through for the object, may be {@code null}
	 * @param objectToFind the object to find, may be {@code null}
	 * @return the index of the object within the array, {@link #INDEX_NOT_FOUND}
	 * ({@code -1}) if not found or {@code null} array input
	 */
	public static int indexOf(Object[] array, Object objectToFind) {
		return indexOf(array, objectToFind, 0);
	}

	/**
	 * Finds the index of the given object in the array starting at the given index.
	 *
	 * <p>
	 * This method returns {@link #INDEX_NOT_FOUND} ({@code -1}) for a {@code null} input
	 * array.
	 *
	 * <p>
	 * A negative startIndex is treated as zero. A startIndex larger than the array length
	 * will return {@link #INDEX_NOT_FOUND} ({@code -1}).
	 * @param array the array to search through for the object, may be {@code null}
	 * @param objectToFind the object to find, may be {@code null}
	 * @param startIndex the index to start searching at
	 * @return the index of the object within the array starting at the index,
	 * {@link #INDEX_NOT_FOUND} ({@code -1}) if not found or {@code null} array input
	 */
	public static int indexOf(Object[] array, Object objectToFind, int startIndex) {
		if (array == null) {
			return INDEX_NOT_FOUND;
		}
		if (startIndex < 0) {
			startIndex = 0;
		}
		if (objectToFind == null) {
			for (int i = startIndex; i < array.length; i++) {
				if (array[i] == null) {
					return i;
				}
			}
		}
		else {
			for (int i = startIndex; i < array.length; i++) {
				if (objectToFind.equals(array[i])) {
					return i;
				}
			}
		}
		return INDEX_NOT_FOUND;
	}

	// short IndexOf
	// -----------------------------------------------------------------------
	/**
	 * Finds the index of the given value in the array.
	 *
	 * <p>
	 * This method returns {@link #INDEX_NOT_FOUND} ({@code -1}) for a {@code null} input
	 * array.
	 * @param array the array to search through for the object, may be {@code null}
	 * @param valueToFind the value to find
	 * @return the index of the value within the array, {@link #INDEX_NOT_FOUND}
	 * ({@code -1}) if not found or {@code null} array input
	 */
	public static int indexOf(short[] array, short valueToFind) {
		return indexOf(array, valueToFind, 0);
	}

	/**
	 * Finds the index of the given value in the array starting at the given index.
	 *
	 * <p>
	 * This method returns {@link #INDEX_NOT_FOUND} ({@code -1}) for a {@code null} input
	 * array.
	 *
	 * <p>
	 * A negative startIndex is treated as zero. A startIndex larger than the array length
	 * will return {@link #INDEX_NOT_FOUND} ({@code -1}).
	 * @param array the array to search through for the object, may be {@code null}
	 * @param valueToFind the value to find
	 * @param startIndex the index to start searching at
	 * @return the index of the value within the array, {@link #INDEX_NOT_FOUND}
	 * ({@code -1}) if not found or {@code null} array input
	 */
	public static int indexOf(short[] array, short valueToFind, int startIndex) {
		if (array == null) {
			return INDEX_NOT_FOUND;
		}
		if (startIndex < 0) {
			startIndex = 0;
		}
		for (int i = startIndex; i < array.length; i++) {
			if (valueToFind == array[i]) {
				return i;
			}
		}
		return INDEX_NOT_FOUND;
	}

	/**
	 * Inserts elements into an array at the given index (starting from zero).
	 *
	 * <p>
	 * When an array is returned, it is always a new array.
	 *
	 * <pre>
	 * ArrayUtils.insert(index, null, null)      = null
	 * ArrayUtils.insert(index, array, null)     = cloned copy of 'array'
	 * ArrayUtils.insert(index, null, values)    = null
	 * </pre>
	 * @param index the position within {@code array} to insert the new values
	 * @param array the array to insert the values into, may be {@code null}
	 * @param values the new values to insert, may be {@code null}
	 * @return The new array.
	 * @throws IndexOutOfBoundsException if {@code array} is provided and either
	 * {@code index < 0} or {@code index > array.length}
	 * @since 3.6
	 */
	public static boolean[] insert(int index, boolean[] array, boolean... values) {
		if (array == null) {
			return null;
		}
		if (ArrayUtils.isEmpty(values)) {
			return clone(array);
		}
		if (index < 0 || index > array.length) {
			throw new IndexOutOfBoundsException("Index: " + index + ", Length: " + array.length);
		}

		boolean[] result = new boolean[array.length + values.length];

		System.arraycopy(values, 0, result, index, values.length);
		if (index > 0) {
			System.arraycopy(array, 0, result, 0, index);
		}
		if (index < array.length) {
			System.arraycopy(array, index, result, index + values.length, array.length - index);
		}
		return result;
	}

	/**
	 * Inserts elements into an array at the given index (starting from zero).
	 *
	 * <p>
	 * When an array is returned, it is always a new array.
	 *
	 * <pre>
	 * ArrayUtils.insert(index, null, null)      = null
	 * ArrayUtils.insert(index, array, null)     = cloned copy of 'array'
	 * ArrayUtils.insert(index, null, values)    = null
	 * </pre>
	 * @param index the position within {@code array} to insert the new values
	 * @param array the array to insert the values into, may be {@code null}
	 * @param values the new values to insert, may be {@code null}
	 * @return The new array.
	 * @throws IndexOutOfBoundsException if {@code array} is provided and either
	 * {@code index < 0} or {@code index > array.length}
	 * @since 3.6
	 */
	public static byte[] insert(int index, byte[] array, byte... values) {
		if (array == null) {
			return null;
		}
		if (ArrayUtils.isEmpty(values)) {
			return clone(array);
		}
		if (index < 0 || index > array.length) {
			throw new IndexOutOfBoundsException("Index: " + index + ", Length: " + array.length);
		}

		byte[] result = new byte[array.length + values.length];

		System.arraycopy(values, 0, result, index, values.length);
		if (index > 0) {
			System.arraycopy(array, 0, result, 0, index);
		}
		if (index < array.length) {
			System.arraycopy(array, index, result, index + values.length, array.length - index);
		}
		return result;
	}

	/**
	 * Inserts elements into an array at the given index (starting from zero).
	 *
	 * <p>
	 * When an array is returned, it is always a new array.
	 *
	 * <pre>
	 * ArrayUtils.insert(index, null, null)      = null
	 * ArrayUtils.insert(index, array, null)     = cloned copy of 'array'
	 * ArrayUtils.insert(index, null, values)    = null
	 * </pre>
	 * @param index the position within {@code array} to insert the new values
	 * @param array the array to insert the values into, may be {@code null}
	 * @param values the new values to insert, may be {@code null}
	 * @return The new array.
	 * @throws IndexOutOfBoundsException if {@code array} is provided and either
	 * {@code index < 0} or {@code index > array.length}
	 * @since 3.6
	 */
	public static char[] insert(int index, char[] array, char... values) {
		if (array == null) {
			return null;
		}
		if (ArrayUtils.isEmpty(values)) {
			return clone(array);
		}
		if (index < 0 || index > array.length) {
			throw new IndexOutOfBoundsException("Index: " + index + ", Length: " + array.length);
		}

		char[] result = new char[array.length + values.length];

		System.arraycopy(values, 0, result, index, values.length);
		if (index > 0) {
			System.arraycopy(array, 0, result, 0, index);
		}
		if (index < array.length) {
			System.arraycopy(array, index, result, index + values.length, array.length - index);
		}
		return result;
	}

	/**
	 * Inserts elements into an array at the given index (starting from zero).
	 *
	 * <p>
	 * When an array is returned, it is always a new array.
	 *
	 * <pre>
	 * ArrayUtils.insert(index, null, null)      = null
	 * ArrayUtils.insert(index, array, null)     = cloned copy of 'array'
	 * ArrayUtils.insert(index, null, values)    = null
	 * </pre>
	 * @param index the position within {@code array} to insert the new values
	 * @param array the array to insert the values into, may be {@code null}
	 * @param values the new values to insert, may be {@code null}
	 * @return The new array.
	 * @throws IndexOutOfBoundsException if {@code array} is provided and either
	 * {@code index < 0} or {@code index > array.length}
	 * @since 3.6
	 */
	public static double[] insert(int index, double[] array, double... values) {
		if (array == null) {
			return null;
		}
		if (ArrayUtils.isEmpty(values)) {
			return clone(array);
		}
		if (index < 0 || index > array.length) {
			throw new IndexOutOfBoundsException("Index: " + index + ", Length: " + array.length);
		}

		double[] result = new double[array.length + values.length];

		System.arraycopy(values, 0, result, index, values.length);
		if (index > 0) {
			System.arraycopy(array, 0, result, 0, index);
		}
		if (index < array.length) {
			System.arraycopy(array, index, result, index + values.length, array.length - index);
		}
		return result;
	}

	/**
	 * Inserts elements into an array at the given index (starting from zero).
	 *
	 * <p>
	 * When an array is returned, it is always a new array.
	 *
	 * <pre>
	 * ArrayUtils.insert(index, null, null)      = null
	 * ArrayUtils.insert(index, array, null)     = cloned copy of 'array'
	 * ArrayUtils.insert(index, null, values)    = null
	 * </pre>
	 * @param index the position within {@code array} to insert the new values
	 * @param array the array to insert the values into, may be {@code null}
	 * @param values the new values to insert, may be {@code null}
	 * @return The new array.
	 * @throws IndexOutOfBoundsException if {@code array} is provided and either
	 * {@code index < 0} or {@code index > array.length}
	 * @since 3.6
	 */
	public static float[] insert(int index, float[] array, float... values) {
		if (array == null) {
			return null;
		}
		if (ArrayUtils.isEmpty(values)) {
			return clone(array);
		}
		if (index < 0 || index > array.length) {
			throw new IndexOutOfBoundsException("Index: " + index + ", Length: " + array.length);
		}

		float[] result = new float[array.length + values.length];

		System.arraycopy(values, 0, result, index, values.length);
		if (index > 0) {
			System.arraycopy(array, 0, result, 0, index);
		}
		if (index < array.length) {
			System.arraycopy(array, index, result, index + values.length, array.length - index);
		}
		return result;
	}

	/**
	 * Inserts elements into an array at the given index (starting from zero).
	 *
	 * <p>
	 * When an array is returned, it is always a new array.
	 *
	 * <pre>
	 * ArrayUtils.insert(index, null, null)      = null
	 * ArrayUtils.insert(index, array, null)     = cloned copy of 'array'
	 * ArrayUtils.insert(index, null, values)    = null
	 * </pre>
	 * @param index the position within {@code array} to insert the new values
	 * @param array the array to insert the values into, may be {@code null}
	 * @param values the new values to insert, may be {@code null}
	 * @return The new array.
	 * @throws IndexOutOfBoundsException if {@code array} is provided and either
	 * {@code index < 0} or {@code index > array.length}
	 * @since 3.6
	 */
	public static int[] insert(int index, int[] array, int... values) {
		if (array == null) {
			return null;
		}
		if (ArrayUtils.isEmpty(values)) {
			return clone(array);
		}
		if (index < 0 || index > array.length) {
			throw new IndexOutOfBoundsException("Index: " + index + ", Length: " + array.length);
		}

		int[] result = new int[array.length + values.length];

		System.arraycopy(values, 0, result, index, values.length);
		if (index > 0) {
			System.arraycopy(array, 0, result, 0, index);
		}
		if (index < array.length) {
			System.arraycopy(array, index, result, index + values.length, array.length - index);
		}
		return result;
	}

	/**
	 * Inserts elements into an array at the given index (starting from zero).
	 *
	 * <p>
	 * When an array is returned, it is always a new array.
	 *
	 * <pre>
	 * ArrayUtils.insert(index, null, null)      = null
	 * ArrayUtils.insert(index, array, null)     = cloned copy of 'array'
	 * ArrayUtils.insert(index, null, values)    = null
	 * </pre>
	 * @param index the position within {@code array} to insert the new values
	 * @param array the array to insert the values into, may be {@code null}
	 * @param values the new values to insert, may be {@code null}
	 * @return The new array.
	 * @throws IndexOutOfBoundsException if {@code array} is provided and either
	 * {@code index < 0} or {@code index > array.length}
	 * @since 3.6
	 */
	public static long[] insert(int index, long[] array, long... values) {
		if (array == null) {
			return null;
		}
		if (ArrayUtils.isEmpty(values)) {
			return clone(array);
		}
		if (index < 0 || index > array.length) {
			throw new IndexOutOfBoundsException("Index: " + index + ", Length: " + array.length);
		}

		long[] result = new long[array.length + values.length];

		System.arraycopy(values, 0, result, index, values.length);
		if (index > 0) {
			System.arraycopy(array, 0, result, 0, index);
		}
		if (index < array.length) {
			System.arraycopy(array, index, result, index + values.length, array.length - index);
		}
		return result;
	}

	/**
	 * Inserts elements into an array at the given index (starting from zero).
	 *
	 * <p>
	 * When an array is returned, it is always a new array.
	 *
	 * <pre>
	 * ArrayUtils.insert(index, null, null)      = null
	 * ArrayUtils.insert(index, array, null)     = cloned copy of 'array'
	 * ArrayUtils.insert(index, null, values)    = null
	 * </pre>
	 * @param index the position within {@code array} to insert the new values
	 * @param array the array to insert the values into, may be {@code null}
	 * @param values the new values to insert, may be {@code null}
	 * @return The new array.
	 * @throws IndexOutOfBoundsException if {@code array} is provided and either
	 * {@code index < 0} or {@code index > array.length}
	 * @since 3.6
	 */
	public static short[] insert(int index, short[] array, short... values) {
		if (array == null) {
			return null;
		}
		if (ArrayUtils.isEmpty(values)) {
			return clone(array);
		}
		if (index < 0 || index > array.length) {
			throw new IndexOutOfBoundsException("Index: " + index + ", Length: " + array.length);
		}

		short[] result = new short[array.length + values.length];

		System.arraycopy(values, 0, result, index, values.length);
		if (index > 0) {
			System.arraycopy(array, 0, result, 0, index);
		}
		if (index < array.length) {
			System.arraycopy(array, index, result, index + values.length, array.length - index);
		}
		return result;
	}

	/**
	 * Inserts elements into an array at the given index (starting from zero).
	 *
	 * <p>
	 * When an array is returned, it is always a new array.
	 *
	 * <pre>
	 * ArrayUtils.insert(index, null, null)      = null
	 * ArrayUtils.insert(index, array, null)     = cloned copy of 'array'
	 * ArrayUtils.insert(index, null, values)    = null
	 * </pre>
	 * @param <T> The type of elements in {@code array} and {@code values}
	 * @param index the position within {@code array} to insert the new values
	 * @param array the array to insert the values into, may be {@code null}
	 * @param values the new values to insert, may be {@code null}
	 * @return The new array.
	 * @throws IndexOutOfBoundsException if {@code array} is provided and either
	 * {@code index < 0} or {@code index > array.length}
	 * @since 3.6
	 */
	@SafeVarargs
	public static <T> T[] insert(int index, T[] array, T... values) {
		/*
		 * Note on use of @SafeVarargs:
		 *
		 * By returning null when 'array' is null, we avoid returning the vararg array to
		 * the caller. We also avoid relying on the type of the vararg array, by
		 * inspecting the component type of 'array'.
		 */

		if (array == null) {
			return null;
		}
		if (ArrayUtils.isEmpty(values)) {
			return clone(array);
		}
		if (index < 0 || index > array.length) {
			throw new IndexOutOfBoundsException("Index: " + index + ", Length: " + array.length);
		}

		Class<?> type = array.getClass().getComponentType();
		// OK, because array and values are of type T
		T[] result = (T[]) Array.newInstance(type, array.length + values.length);

		System.arraycopy(values, 0, result, index, values.length);
		if (index > 0) {
			System.arraycopy(array, 0, result, 0, index);
		}
		if (index < array.length) {
			System.arraycopy(array, index, result, index + values.length, array.length - index);
		}
		return result;
	}

	/**
	 * Returns whether a given array can safely be accessed at the given index.
	 * @param <T> the component type of the array
	 * @param array the array to inspect, may be null
	 * @param index the index of the array to be inspected
	 * @return Whether the given index is safely-accessible in the given array
	 * @since 3.8
	 */
	public static <T> boolean isArrayIndexValid(T[] array, int index) {
		if (getLength(array) == 0 || array.length <= index) {
			return false;
		}

		return index >= 0;
	}

	/**
	 * Checks if an array of primitive booleans is empty or {@code null}.
	 * @param array the array to test
	 * @return {@code true} if the array is empty or {@code null}
	 * @since 2.1
	 */
	public static boolean isEmpty(boolean[] array) {
		return getLength(array) == 0;
	}

	// IndexOf search
	// ----------------------------------------------------------------------

	/**
	 * Checks if an array of primitive bytes is empty or {@code null}.
	 * @param array the array to test
	 * @return {@code true} if the array is empty or {@code null}
	 * @since 2.1
	 */
	public static boolean isEmpty(byte[] array) {
		return getLength(array) == 0;
	}

	/**
	 * Checks if an array of primitive chars is empty or {@code null}.
	 * @param array the array to test
	 * @return {@code true} if the array is empty or {@code null}
	 * @since 2.1
	 */
	public static boolean isEmpty(char[] array) {
		return getLength(array) == 0;
	}

	/**
	 * Checks if an array of primitive doubles is empty or {@code null}.
	 * @param array the array to test
	 * @return {@code true} if the array is empty or {@code null}
	 * @since 2.1
	 */
	public static boolean isEmpty(double[] array) {
		return getLength(array) == 0;
	}

	/**
	 * Checks if an array of primitive floats is empty or {@code null}.
	 * @param array the array to test
	 * @return {@code true} if the array is empty or {@code null}
	 * @since 2.1
	 */
	public static boolean isEmpty(float[] array) {
		return getLength(array) == 0;
	}

	/**
	 * Checks if an array of primitive ints is empty or {@code null}.
	 * @param array the array to test
	 * @return {@code true} if the array is empty or {@code null}
	 * @since 2.1
	 */
	public static boolean isEmpty(int[] array) {
		return getLength(array) == 0;
	}

	/**
	 * Checks if an array of primitive longs is empty or {@code null}.
	 * @param array the array to test
	 * @return {@code true} if the array is empty or {@code null}
	 * @since 2.1
	 */
	public static boolean isEmpty(long[] array) {
		return getLength(array) == 0;
	}

	// ----------------------------------------------------------------------
	/**
	 * Checks if an array of Objects is empty or {@code null}.
	 * @param array the array to test
	 * @return {@code true} if the array is empty or {@code null}
	 * @since 2.1
	 */
	public static boolean isEmpty(Object[] array) {
		return getLength(array) == 0;
	}

	/**
	 * Checks if an array of primitive shorts is empty or {@code null}.
	 * @param array the array to test
	 * @return {@code true} if the array is empty or {@code null}
	 * @since 2.1
	 */
	public static boolean isEmpty(short[] array) {
		return getLength(array) == 0;
	}

	/**
	 * Checks if an array of primitive booleans is not empty and not {@code null}.
	 * @param array the array to test
	 * @return {@code true} if the array is not empty and not {@code null}
	 * @since 2.5
	 */
	public static boolean isNotEmpty(boolean[] array) {
		return !isEmpty(array);
	}

	/**
	 * Checks if an array of primitive bytes is not empty and not {@code null}.
	 * @param array the array to test
	 * @return {@code true} if the array is not empty and not {@code null}
	 * @since 2.5
	 */
	public static boolean isNotEmpty(byte[] array) {
		return !isEmpty(array);
	}

	/**
	 * Checks if an array of primitive chars is not empty and not {@code null}.
	 * @param array the array to test
	 * @return {@code true} if the array is not empty and not {@code null}
	 * @since 2.5
	 */
	public static boolean isNotEmpty(char[] array) {
		return !isEmpty(array);
	}

	/**
	 * Checks if an array of primitive doubles is not empty and not {@code null}.
	 * @param array the array to test
	 * @return {@code true} if the array is not empty and not {@code null}
	 * @since 2.5
	 */
	public static boolean isNotEmpty(double[] array) {
		return !isEmpty(array);
	}

	/**
	 * Checks if an array of primitive floats is not empty and not {@code null}.
	 * @param array the array to test
	 * @return {@code true} if the array is not empty and not {@code null}
	 * @since 2.5
	 */
	public static boolean isNotEmpty(float[] array) {
		return !isEmpty(array);
	}

	/**
	 * Checks if an array of primitive ints is not empty and not {@code null}.
	 * @param array the array to test
	 * @return {@code true} if the array is not empty and not {@code null}
	 * @since 2.5
	 */
	public static boolean isNotEmpty(int[] array) {
		return !isEmpty(array);
	}

	/**
	 * Checks if an array of primitive longs is not empty and not {@code null}.
	 * @param array the array to test
	 * @return {@code true} if the array is not empty and not {@code null}
	 * @since 2.5
	 */
	public static boolean isNotEmpty(long[] array) {
		return !isEmpty(array);
	}

	/**
	 * Checks if an array of primitive shorts is not empty and not {@code null}.
	 * @param array the array to test
	 * @return {@code true} if the array is not empty and not {@code null}
	 * @since 2.5
	 */
	public static boolean isNotEmpty(short[] array) {
		return !isEmpty(array);
	}

	// ----------------------------------------------------------------------
	/**
	 * Checks if an array of Objects is not empty and not {@code null}.
	 * @param <T> the component type of the array
	 * @param array the array to test
	 * @return {@code true} if the array is not empty and not {@code null}
	 * @since 2.5
	 */
	public static <T> boolean isNotEmpty(T[] array) {
		return !isEmpty(array);
	}

	/**
	 * Checks whether two arrays are the same length, treating {@code null} arrays as
	 * length {@code
	 * 0}.
	 * @param array1 the first array, may be {@code null}
	 * @param array2 the second array, may be {@code null}
	 * @return {@code true} if length of arrays matches, treating {@code null} as an empty
	 * array
	 */
	public static boolean isSameLength(boolean[] array1, boolean[] array2) {
		return getLength(array1) == getLength(array2);
	}

	/**
	 * Checks whether two arrays are the same length, treating {@code null} arrays as
	 * length {@code
	 * 0}.
	 * @param array1 the first array, may be {@code null}
	 * @param array2 the second array, may be {@code null}
	 * @return {@code true} if length of arrays matches, treating {@code null} as an empty
	 * array
	 */
	public static boolean isSameLength(byte[] array1, byte[] array2) {
		return getLength(array1) == getLength(array2);
	}

	/**
	 * Checks whether two arrays are the same length, treating {@code null} arrays as
	 * length {@code
	 * 0}.
	 * @param array1 the first array, may be {@code null}
	 * @param array2 the second array, may be {@code null}
	 * @return {@code true} if length of arrays matches, treating {@code null} as an empty
	 * array
	 */
	public static boolean isSameLength(char[] array1, char[] array2) {
		return getLength(array1) == getLength(array2);
	}

	/**
	 * Checks whether two arrays are the same length, treating {@code null} arrays as
	 * length {@code
	 * 0}.
	 * @param array1 the first array, may be {@code null}
	 * @param array2 the second array, may be {@code null}
	 * @return {@code true} if length of arrays matches, treating {@code null} as an empty
	 * array
	 */
	public static boolean isSameLength(double[] array1, double[] array2) {
		return getLength(array1) == getLength(array2);
	}

	/**
	 * Checks whether two arrays are the same length, treating {@code null} arrays as
	 * length {@code
	 * 0}.
	 * @param array1 the first array, may be {@code null}
	 * @param array2 the second array, may be {@code null}
	 * @return {@code true} if length of arrays matches, treating {@code null} as an empty
	 * array
	 */
	public static boolean isSameLength(float[] array1, float[] array2) {
		return getLength(array1) == getLength(array2);
	}

	/**
	 * Checks whether two arrays are the same length, treating {@code null} arrays as
	 * length {@code
	 * 0}.
	 * @param array1 the first array, may be {@code null}
	 * @param array2 the second array, may be {@code null}
	 * @return {@code true} if length of arrays matches, treating {@code null} as an empty
	 * array
	 */
	public static boolean isSameLength(int[] array1, int[] array2) {
		return getLength(array1) == getLength(array2);
	}

	/**
	 * Checks whether two arrays are the same length, treating {@code null} arrays as
	 * length {@code
	 * 0}.
	 * @param array1 the first array, may be {@code null}
	 * @param array2 the second array, may be {@code null}
	 * @return {@code true} if length of arrays matches, treating {@code null} as an empty
	 * array
	 */
	public static boolean isSameLength(long[] array1, long[] array2) {
		return getLength(array1) == getLength(array2);
	}

	// Is same length
	// -----------------------------------------------------------------------
	/**
	 * Checks whether two arrays are the same length, treating {@code null} arrays as
	 * length {@code
	 * 0}.
	 *
	 * <p>
	 * Any multi-dimensional aspects of the arrays are ignored.
	 * @param array1 the first array, may be {@code null}
	 * @param array2 the second array, may be {@code null}
	 * @return {@code true} if length of arrays matches, treating {@code null} as an empty
	 * array
	 */
	public static boolean isSameLength(Object[] array1, Object[] array2) {
		return getLength(array1) == getLength(array2);
	}

	/**
	 * Checks whether two arrays are the same length, treating {@code null} arrays as
	 * length {@code
	 * 0}.
	 * @param array1 the first array, may be {@code null}
	 * @param array2 the second array, may be {@code null}
	 * @return {@code true} if length of arrays matches, treating {@code null} as an empty
	 * array
	 */
	public static boolean isSameLength(short[] array1, short[] array2) {
		return getLength(array1) == getLength(array2);
	}

	/**
	 * Checks whether two arrays are the same type taking into account multi-dimensional
	 * arrays.
	 * @param array1 the first array, must not be {@code null}
	 * @param array2 the second array, must not be {@code null}
	 * @return {@code true} if type of arrays matches
	 * @throws IllegalArgumentException if either array is {@code null}
	 */
	public static boolean isSameType(Object array1, Object array2) {
		if (array1 == null || array2 == null) {
			throw new IllegalArgumentException("The Array must not be null");
		}
		return array1.getClass().getName().equals(array2.getClass().getName());
	}

	/**
	 * This method checks whether the provided array is sorted according to natural
	 * ordering.
	 * @param array the array to check
	 * @return whether the array is sorted according to natural ordering
	 * @since 3.4
	 */
	public static boolean isSorted(double[] array) {
		if (array == null || array.length < 2) {
			return true;
		}

		double previous = array[0];
		int n = array.length;
		for (int i = 1; i < n; i++) {
			double current = array[i];
			if (Double.compare(previous, current) > 0) {
				return false;
			}

			previous = current;
		}
		return true;
	}

	/**
	 * This method checks whether the provided array is sorted according to natural
	 * ordering.
	 * @param array the array to check
	 * @return whether the array is sorted according to natural ordering
	 * @since 3.4
	 */
	public static boolean isSorted(float[] array) {
		if (array == null || array.length < 2) {
			return true;
		}

		float previous = array[0];
		int n = array.length;
		for (int i = 1; i < n; i++) {
			float current = array[i];
			if (Float.compare(previous, current) > 0) {
				return false;
			}

			previous = current;
		}
		return true;
	}

	/**
	 * This method checks whether the provided array is sorted according to the class's
	 * {@code
	 * compareTo} method.
	 * @param array the array to check
	 * @param <T> the datatype of the array to check, it must implement {@code Comparable}
	 * @return whether the array is sorted
	 * @since 3.4
	 */
	public static <T extends Comparable<? super T>> boolean isSorted(T[] array) {
		return isSorted(array, (o1, o2) -> o1.compareTo(o2));
	}

	/**
	 * This method checks whether the provided array is sorted according to the provided
	 * {@code
	 * Comparator}.
	 * @param array the array to check
	 * @param comparator the {@code Comparator} to compare over
	 * @param <T> the datatype of the array
	 * @return whether the array is sorted
	 * @since 3.4
	 */
	public static <T> boolean isSorted(T[] array, Comparator<T> comparator) {
		if (comparator == null) {
			throw new IllegalArgumentException("Comparator should not be null.");
		}

		if (array == null || array.length < 2) {
			return true;
		}

		T previous = array[0];
		int n = array.length;
		for (int i = 1; i < n; i++) {
			T current = array[i];
			if (comparator.compare(previous, current) > 0) {
				return false;
			}

			previous = current;
		}
		return true;
	}

	/**
	 * Finds the last index of the given value within the array.
	 *
	 * <p>
	 * This method returns {@link #INDEX_NOT_FOUND} ({@code -1}) if {@code null} array
	 * input.
	 * @param array the array to traverse backwards looking for the object, may be
	 * {@code null}
	 * @param valueToFind the object to find
	 * @return the last index of the value within the array, {@link #INDEX_NOT_FOUND}
	 * ({@code -1}) if not found or {@code null} array input
	 */
	public static int lastIndexOf(boolean[] array, boolean valueToFind) {
		return lastIndexOf(array, valueToFind, Integer.MAX_VALUE);
	}

	/**
	 * Finds the last index of the given value in the array starting at the given index.
	 *
	 * <p>
	 * This method returns {@link #INDEX_NOT_FOUND} ({@code -1}) for a {@code null} input
	 * array.
	 *
	 * <p>
	 * A negative startIndex will return {@link #INDEX_NOT_FOUND} ({@code -1}). A
	 * startIndex larger than the array length will search from the end of the array.
	 * @param array the array to traverse for looking for the object, may be {@code null}
	 * @param valueToFind the value to find
	 * @param startIndex the start index to traverse backwards from
	 * @return the last index of the value within the array, {@link #INDEX_NOT_FOUND}
	 * ({@code -1}) if not found or {@code null} array input
	 */
	public static int lastIndexOf(boolean[] array, boolean valueToFind, int startIndex) {
		if (isEmpty(array)) {
			return INDEX_NOT_FOUND;
		}
		if (startIndex < 0) {
			return INDEX_NOT_FOUND;
		}
		else if (startIndex >= array.length) {
			startIndex = array.length - 1;
		}
		for (int i = startIndex; i >= 0; i--) {
			if (valueToFind == array[i]) {
				return i;
			}
		}
		return INDEX_NOT_FOUND;
	}

	/**
	 * Finds the last index of the given value within the array.
	 *
	 * <p>
	 * This method returns {@link #INDEX_NOT_FOUND} ({@code -1}) for a {@code null} input
	 * array.
	 * @param array the array to traverse backwards looking for the object, may be
	 * {@code null}
	 * @param valueToFind the object to find
	 * @return the last index of the value within the array, {@link #INDEX_NOT_FOUND}
	 * ({@code -1}) if not found or {@code null} array input
	 */
	public static int lastIndexOf(byte[] array, byte valueToFind) {
		return lastIndexOf(array, valueToFind, Integer.MAX_VALUE);
	}

	/**
	 * Finds the last index of the given value in the array starting at the given index.
	 *
	 * <p>
	 * This method returns {@link #INDEX_NOT_FOUND} ({@code -1}) for a {@code null} input
	 * array.
	 *
	 * <p>
	 * A negative startIndex will return {@link #INDEX_NOT_FOUND} ({@code -1}). A
	 * startIndex larger than the array length will search from the end of the array.
	 * @param array the array to traverse for looking for the object, may be {@code null}
	 * @param valueToFind the value to find
	 * @param startIndex the start index to traverse backwards from
	 * @return the last index of the value within the array, {@link #INDEX_NOT_FOUND}
	 * ({@code -1}) if not found or {@code null} array input
	 */
	public static int lastIndexOf(byte[] array, byte valueToFind, int startIndex) {
		if (array == null) {
			return INDEX_NOT_FOUND;
		}
		if (startIndex < 0) {
			return INDEX_NOT_FOUND;
		}
		else if (startIndex >= array.length) {
			startIndex = array.length - 1;
		}
		for (int i = startIndex; i >= 0; i--) {
			if (valueToFind == array[i]) {
				return i;
			}
		}
		return INDEX_NOT_FOUND;
	}

	/**
	 * Finds the last index of the given value within the array.
	 *
	 * <p>
	 * This method returns {@link #INDEX_NOT_FOUND} ({@code -1}) for a {@code null} input
	 * array.
	 * @param array the array to traverse backwards looking for the object, may be
	 * {@code null}
	 * @param valueToFind the object to find
	 * @return the last index of the value within the array, {@link #INDEX_NOT_FOUND}
	 * ({@code -1}) if not found or {@code null} array input
	 * @since 2.1
	 */
	public static int lastIndexOf(char[] array, char valueToFind) {
		return lastIndexOf(array, valueToFind, Integer.MAX_VALUE);
	}

	/**
	 * Finds the last index of the given value in the array starting at the given index.
	 *
	 * <p>
	 * This method returns {@link #INDEX_NOT_FOUND} ({@code -1}) for a {@code null} input
	 * array.
	 *
	 * <p>
	 * A negative startIndex will return {@link #INDEX_NOT_FOUND} ({@code -1}). A
	 * startIndex larger than the array length will search from the end of the array.
	 * @param array the array to traverse for looking for the object, may be {@code null}
	 * @param valueToFind the value to find
	 * @param startIndex the start index to traverse backwards from
	 * @return the last index of the value within the array, {@link #INDEX_NOT_FOUND}
	 * ({@code -1}) if not found or {@code null} array input
	 * @since 2.1
	 */
	public static int lastIndexOf(char[] array, char valueToFind, int startIndex) {
		if (array == null) {
			return INDEX_NOT_FOUND;
		}
		if (startIndex < 0) {
			return INDEX_NOT_FOUND;
		}
		else if (startIndex >= array.length) {
			startIndex = array.length - 1;
		}
		for (int i = startIndex; i >= 0; i--) {
			if (valueToFind == array[i]) {
				return i;
			}
		}
		return INDEX_NOT_FOUND;
	}

	/**
	 * Finds the last index of the given value within the array.
	 *
	 * <p>
	 * This method returns {@link #INDEX_NOT_FOUND} ({@code -1}) for a {@code null} input
	 * array.
	 * @param array the array to traverse backwards looking for the object, may be
	 * {@code null}
	 * @param valueToFind the object to find
	 * @return the last index of the value within the array, {@link #INDEX_NOT_FOUND}
	 * ({@code -1}) if not found or {@code null} array input
	 */
	public static int lastIndexOf(double[] array, double valueToFind) {
		return lastIndexOf(array, valueToFind, Integer.MAX_VALUE);
	}

	/**
	 * Finds the last index of the given value within a given tolerance in the array. This
	 * method will return the index of the last value which falls between the region
	 * defined by valueToFind - tolerance and valueToFind + tolerance.
	 *
	 * <p>
	 * This method returns {@link #INDEX_NOT_FOUND} ({@code -1}) for a {@code null} input
	 * array.
	 * @param array the array to search through for the object, may be {@code null}
	 * @param valueToFind the value to find
	 * @param tolerance tolerance of the search
	 * @return the index of the value within the array, {@link #INDEX_NOT_FOUND}
	 * ({@code -1}) if not found or {@code null} array input
	 */
	public static int lastIndexOf(double[] array, double valueToFind, double tolerance) {
		return lastIndexOf(array, valueToFind, Integer.MAX_VALUE, tolerance);
	}

	/**
	 * Finds the last index of the given value in the array starting at the given index.
	 *
	 * <p>
	 * This method returns {@link #INDEX_NOT_FOUND} ({@code -1}) for a {@code null} input
	 * array.
	 *
	 * <p>
	 * A negative startIndex will return {@link #INDEX_NOT_FOUND} ({@code -1}). A
	 * startIndex larger than the array length will search from the end of the array.
	 * @param array the array to traverse for looking for the object, may be {@code null}
	 * @param valueToFind the value to find
	 * @param startIndex the start index to traverse backwards from
	 * @return the last index of the value within the array, {@link #INDEX_NOT_FOUND}
	 * ({@code -1}) if not found or {@code null} array input
	 */
	public static int lastIndexOf(double[] array, double valueToFind, int startIndex) {
		if (isEmpty(array)) {
			return INDEX_NOT_FOUND;
		}
		if (startIndex < 0) {
			return INDEX_NOT_FOUND;
		}
		else if (startIndex >= array.length) {
			startIndex = array.length - 1;
		}
		for (int i = startIndex; i >= 0; i--) {
			if (valueToFind == array[i]) {
				return i;
			}
		}
		return INDEX_NOT_FOUND;
	}

	/**
	 * Finds the last index of the given value in the array starting at the given index.
	 * This method will return the index of the last value which falls between the region
	 * defined by valueToFind - tolerance and valueToFind + tolerance.
	 *
	 * <p>
	 * This method returns {@link #INDEX_NOT_FOUND} ({@code -1}) for a {@code null} input
	 * array.
	 *
	 * <p>
	 * A negative startIndex will return {@link #INDEX_NOT_FOUND} ({@code -1}). A
	 * startIndex larger than the array length will search from the end of the array.
	 * @param array the array to traverse for looking for the object, may be {@code null}
	 * @param valueToFind the value to find
	 * @param startIndex the start index to traverse backwards from
	 * @param tolerance search for value within plus/minus this amount
	 * @return the last index of the value within the array, {@link #INDEX_NOT_FOUND}
	 * ({@code -1}) if not found or {@code null} array input
	 */
	public static int lastIndexOf(double[] array, double valueToFind, int startIndex,
			double tolerance) {
		if (isEmpty(array)) {
			return INDEX_NOT_FOUND;
		}
		if (startIndex < 0) {
			return INDEX_NOT_FOUND;
		}
		else if (startIndex >= array.length) {
			startIndex = array.length - 1;
		}
		double min = valueToFind - tolerance;
		double max = valueToFind + tolerance;
		for (int i = startIndex; i >= 0; i--) {
			if (array[i] >= min && array[i] <= max) {
				return i;
			}
		}
		return INDEX_NOT_FOUND;
	}

	/**
	 * Finds the last index of the given value within the array.
	 *
	 * <p>
	 * This method returns {@link #INDEX_NOT_FOUND} ({@code -1}) for a {@code null} input
	 * array.
	 * @param array the array to traverse backwards looking for the object, may be
	 * {@code null}
	 * @param valueToFind the object to find
	 * @return the last index of the value within the array, {@link #INDEX_NOT_FOUND}
	 * ({@code -1}) if not found or {@code null} array input
	 */
	public static int lastIndexOf(float[] array, float valueToFind) {
		return lastIndexOf(array, valueToFind, Integer.MAX_VALUE);
	}

	/**
	 * Finds the last index of the given value in the array starting at the given index.
	 *
	 * <p>
	 * This method returns {@link #INDEX_NOT_FOUND} ({@code -1}) for a {@code null} input
	 * array.
	 *
	 * <p>
	 * A negative startIndex will return {@link #INDEX_NOT_FOUND} ({@code -1}). A
	 * startIndex larger than the array length will search from the end of the array.
	 * @param array the array to traverse for looking for the object, may be {@code null}
	 * @param valueToFind the value to find
	 * @param startIndex the start index to traverse backwards from
	 * @return the last index of the value within the array, {@link #INDEX_NOT_FOUND}
	 * ({@code -1}) if not found or {@code null} array input
	 */
	public static int lastIndexOf(float[] array, float valueToFind, int startIndex) {
		if (isEmpty(array)) {
			return INDEX_NOT_FOUND;
		}
		if (startIndex < 0) {
			return INDEX_NOT_FOUND;
		}
		else if (startIndex >= array.length) {
			startIndex = array.length - 1;
		}
		for (int i = startIndex; i >= 0; i--) {
			if (valueToFind == array[i]) {
				return i;
			}
		}
		return INDEX_NOT_FOUND;
	}

	/**
	 * Finds the last index of the given value within the array.
	 *
	 * <p>
	 * This method returns {@link #INDEX_NOT_FOUND} ({@code -1}) for a {@code null} input
	 * array.
	 * @param array the array to traverse backwards looking for the object, may be
	 * {@code null}
	 * @param valueToFind the object to find
	 * @return the last index of the value within the array, {@link #INDEX_NOT_FOUND}
	 * ({@code -1}) if not found or {@code null} array input
	 */
	public static int lastIndexOf(int[] array, int valueToFind) {
		return lastIndexOf(array, valueToFind, Integer.MAX_VALUE);
	}

	/**
	 * Finds the last index of the given value in the array starting at the given index.
	 *
	 * <p>
	 * This method returns {@link #INDEX_NOT_FOUND} ({@code -1}) for a {@code null} input
	 * array.
	 *
	 * <p>
	 * A negative startIndex will return {@link #INDEX_NOT_FOUND} ({@code -1}). A
	 * startIndex larger than the array length will search from the end of the array.
	 * @param array the array to traverse for looking for the object, may be {@code null}
	 * @param valueToFind the value to find
	 * @param startIndex the start index to traverse backwards from
	 * @return the last index of the value within the array, {@link #INDEX_NOT_FOUND}
	 * ({@code -1}) if not found or {@code null} array input
	 */
	public static int lastIndexOf(int[] array, int valueToFind, int startIndex) {
		if (array == null) {
			return INDEX_NOT_FOUND;
		}
		if (startIndex < 0) {
			return INDEX_NOT_FOUND;
		}
		else if (startIndex >= array.length) {
			startIndex = array.length - 1;
		}
		for (int i = startIndex; i >= 0; i--) {
			if (valueToFind == array[i]) {
				return i;
			}
		}
		return INDEX_NOT_FOUND;
	}

	/**
	 * Finds the last index of the given value within the array.
	 *
	 * <p>
	 * This method returns {@link #INDEX_NOT_FOUND} ({@code -1}) for a {@code null} input
	 * array.
	 * @param array the array to traverse backwards looking for the object, may be
	 * {@code null}
	 * @param valueToFind the object to find
	 * @return the last index of the value within the array, {@link #INDEX_NOT_FOUND}
	 * ({@code -1}) if not found or {@code null} array input
	 */
	public static int lastIndexOf(long[] array, long valueToFind) {
		return lastIndexOf(array, valueToFind, Integer.MAX_VALUE);
	}

	/**
	 * Finds the last index of the given value in the array starting at the given index.
	 *
	 * <p>
	 * This method returns {@link #INDEX_NOT_FOUND} ({@code -1}) for a {@code null} input
	 * array.
	 *
	 * <p>
	 * A negative startIndex will return {@link #INDEX_NOT_FOUND} ({@code -1}). A
	 * startIndex larger than the array length will search from the end of the array.
	 * @param array the array to traverse for looking for the object, may be {@code null}
	 * @param valueToFind the value to find
	 * @param startIndex the start index to traverse backwards from
	 * @return the last index of the value within the array, {@link #INDEX_NOT_FOUND}
	 * ({@code -1}) if not found or {@code null} array input
	 */
	public static int lastIndexOf(long[] array, long valueToFind, int startIndex) {
		if (array == null) {
			return INDEX_NOT_FOUND;
		}
		if (startIndex < 0) {
			return INDEX_NOT_FOUND;
		}
		else if (startIndex >= array.length) {
			startIndex = array.length - 1;
		}
		for (int i = startIndex; i >= 0; i--) {
			if (valueToFind == array[i]) {
				return i;
			}
		}
		return INDEX_NOT_FOUND;
	}

	/**
	 * Finds the last index of the given object within the array.
	 *
	 * <p>
	 * This method returns {@link #INDEX_NOT_FOUND} ({@code -1}) for a {@code null} input
	 * array.
	 * @param array the array to traverse backwards looking for the object, may be
	 * {@code null}
	 * @param objectToFind the object to find, may be {@code null}
	 * @return the last index of the object within the array, {@link #INDEX_NOT_FOUND}
	 * ({@code -1}) if not found or {@code null} array input
	 */
	public static int lastIndexOf(Object[] array, Object objectToFind) {
		return lastIndexOf(array, objectToFind, Integer.MAX_VALUE);
	}

	/**
	 * Finds the last index of the given object in the array starting at the given index.
	 *
	 * <p>
	 * This method returns {@link #INDEX_NOT_FOUND} ({@code -1}) for a {@code null} input
	 * array.
	 *
	 * <p>
	 * A negative startIndex will return {@link #INDEX_NOT_FOUND} ({@code -1}). A
	 * startIndex larger than the array length will search from the end of the array.
	 * @param array the array to traverse for looking for the object, may be {@code null}
	 * @param objectToFind the object to find, may be {@code null}
	 * @param startIndex the start index to traverse backwards from
	 * @return the last index of the object within the array, {@link #INDEX_NOT_FOUND}
	 * ({@code -1}) if not found or {@code null} array input
	 */
	public static int lastIndexOf(Object[] array, Object objectToFind, int startIndex) {
		if (array == null) {
			return INDEX_NOT_FOUND;
		}
		if (startIndex < 0) {
			return INDEX_NOT_FOUND;
		}
		else if (startIndex >= array.length) {
			startIndex = array.length - 1;
		}
		if (objectToFind == null) {
			for (int i = startIndex; i >= 0; i--) {
				if (array[i] == null) {
					return i;
				}
			}
		}
		else if (array.getClass().getComponentType().isInstance(objectToFind)) {
			for (int i = startIndex; i >= 0; i--) {
				if (objectToFind.equals(array[i])) {
					return i;
				}
			}
		}
		return INDEX_NOT_FOUND;
	}

	/**
	 * Finds the last index of the given value within the array.
	 *
	 * <p>
	 * This method returns {@link #INDEX_NOT_FOUND} ({@code -1}) for a {@code null} input
	 * array.
	 * @param array the array to traverse backwards looking for the object, may be
	 * {@code null}
	 * @param valueToFind the object to find
	 * @return the last index of the value within the array, {@link #INDEX_NOT_FOUND}
	 * ({@code -1}) if not found or {@code null} array input
	 */
	public static int lastIndexOf(short[] array, short valueToFind) {
		return lastIndexOf(array, valueToFind, Integer.MAX_VALUE);
	}

	/**
	 * Finds the last index of the given value in the array starting at the given index.
	 *
	 * <p>
	 * This method returns {@link #INDEX_NOT_FOUND} ({@code -1}) for a {@code null} input
	 * array.
	 *
	 * <p>
	 * A negative startIndex will return {@link #INDEX_NOT_FOUND} ({@code -1}). A
	 * startIndex larger than the array length will search from the end of the array.
	 * @param array the array to traverse for looking for the object, may be {@code null}
	 * @param valueToFind the value to find
	 * @param startIndex the start index to traverse backwards from
	 * @return the last index of the value within the array, {@link #INDEX_NOT_FOUND}
	 * ({@code -1}) if not found or {@code null} array input
	 */
	public static int lastIndexOf(short[] array, short valueToFind, int startIndex) {
		if (array == null) {
			return INDEX_NOT_FOUND;
		}
		if (startIndex < 0) {
			return INDEX_NOT_FOUND;
		}
		else if (startIndex >= array.length) {
			startIndex = array.length - 1;
		}
		for (int i = startIndex; i >= 0; i--) {
			if (valueToFind == array[i]) {
				return i;
			}
		}
		return INDEX_NOT_FOUND;
	}

	/**
	 * Defensive programming technique to change a {@code null} reference to an empty one.
	 *
	 * <p>
	 * This method returns an empty array for a {@code null} input array.
	 *
	 * <p>
	 * As a memory optimizing technique an empty array passed in will be overridden with
	 * the empty {@code public static} references in this class.
	 * @param array the array to check for {@code null} or empty
	 * @return the same array, {@code public static} empty array if {@code null} or empty
	 * input
	 * @since 2.5
	 */
	public static boolean[] nullToEmpty(boolean[] array) {
		if (isEmpty(array)) {
			return EMPTY_BOOLEAN_ARRAY;
		}
		return array;
	}

	/**
	 * Defensive programming technique to change a {@code null} reference to an empty one.
	 *
	 * <p>
	 * This method returns an empty array for a {@code null} input array.
	 *
	 * <p>
	 * As a memory optimizing technique an empty array passed in will be overridden with
	 * the empty {@code public static} references in this class.
	 * @param array the array to check for {@code null} or empty
	 * @return the same array, {@code public static} empty array if {@code null} or empty
	 * input
	 * @since 2.5
	 */
	public static Boolean[] nullToEmpty(Boolean[] array) {
		if (isEmpty(array)) {
			return EMPTY_BOOLEAN_OBJECT_ARRAY;
		}
		return array;
	}

	/**
	 * Defensive programming technique to change a {@code null} reference to an empty one.
	 *
	 * <p>
	 * This method returns an empty array for a {@code null} input array.
	 *
	 * <p>
	 * As a memory optimizing technique an empty array passed in will be overridden with
	 * the empty {@code public static} references in this class.
	 * @param array the array to check for {@code null} or empty
	 * @return the same array, {@code public static} empty array if {@code null} or empty
	 * input
	 * @since 2.5
	 */
	public static byte[] nullToEmpty(byte[] array) {
		if (isEmpty(array)) {
			return EMPTY_BYTE_ARRAY;
		}
		return array;
	}

	/**
	 * Defensive programming technique to change a {@code null} reference to an empty one.
	 *
	 * <p>
	 * This method returns an empty array for a {@code null} input array.
	 *
	 * <p>
	 * As a memory optimizing technique an empty array passed in will be overridden with
	 * the empty {@code public static} references in this class.
	 * @param array the array to check for {@code null} or empty
	 * @return the same array, {@code public static} empty array if {@code null} or empty
	 * input
	 * @since 2.5
	 */
	public static Byte[] nullToEmpty(Byte[] array) {
		if (isEmpty(array)) {
			return EMPTY_BYTE_OBJECT_ARRAY;
		}
		return array;
	}

	/**
	 * Defensive programming technique to change a {@code null} reference to an empty one.
	 *
	 * <p>
	 * This method returns an empty array for a {@code null} input array.
	 *
	 * <p>
	 * As a memory optimizing technique an empty array passed in will be overridden with
	 * the empty {@code public static} references in this class.
	 * @param array the array to check for {@code null} or empty
	 * @return the same array, {@code public static} empty array if {@code null} or empty
	 * input
	 * @since 2.5
	 */
	public static char[] nullToEmpty(char[] array) {
		if (isEmpty(array)) {
			return EMPTY_CHAR_ARRAY;
		}
		return array;
	}

	/**
	 * Defensive programming technique to change a {@code null} reference to an empty one.
	 *
	 * <p>
	 * This method returns an empty array for a {@code null} input array.
	 *
	 * <p>
	 * As a memory optimizing technique an empty array passed in will be overridden with
	 * the empty {@code public static} references in this class.
	 * @param array the array to check for {@code null} or empty
	 * @return the same array, {@code public static} empty array if {@code null} or empty
	 * input
	 * @since 2.5
	 */
	public static Character[] nullToEmpty(Character[] array) {
		if (isEmpty(array)) {
			return EMPTY_CHARACTER_OBJECT_ARRAY;
		}
		return array;
	}

	/**
	 * Defensive programming technique to change a {@code null} reference to an empty one.
	 *
	 * <p>
	 * This method returns an empty array for a {@code null} input array.
	 *
	 * <p>
	 * As a memory optimizing technique an empty array passed in will be overridden with
	 * the empty {@code public static} references in this class.
	 * @param array the array to check for {@code null} or empty
	 * @return the same array, {@code public static} empty array if {@code null} or empty
	 * input
	 * @since 3.2
	 */
	public static Class<?>[] nullToEmpty(Class<?>[] array) {
		if (isEmpty(array)) {
			return EMPTY_CLASS_ARRAY;
		}
		return array;
	}

	/**
	 * Defensive programming technique to change a {@code null} reference to an empty one.
	 *
	 * <p>
	 * This method returns an empty array for a {@code null} input array.
	 *
	 * <p>
	 * As a memory optimizing technique an empty array passed in will be overridden with
	 * the empty {@code public static} references in this class.
	 * @param array the array to check for {@code null} or empty
	 * @return the same array, {@code public static} empty array if {@code null} or empty
	 * input
	 * @since 2.5
	 */
	public static double[] nullToEmpty(double[] array) {
		if (isEmpty(array)) {
			return EMPTY_DOUBLE_ARRAY;
		}
		return array;
	}

	/**
	 * Defensive programming technique to change a {@code null} reference to an empty one.
	 *
	 * <p>
	 * This method returns an empty array for a {@code null} input array.
	 *
	 * <p>
	 * As a memory optimizing technique an empty array passed in will be overridden with
	 * the empty {@code public static} references in this class.
	 * @param array the array to check for {@code null} or empty
	 * @return the same array, {@code public static} empty array if {@code null} or empty
	 * input
	 * @since 2.5
	 */
	public static Double[] nullToEmpty(Double[] array) {
		if (isEmpty(array)) {
			return EMPTY_DOUBLE_OBJECT_ARRAY;
		}
		return array;
	}

	/**
	 * Defensive programming technique to change a {@code null} reference to an empty one.
	 *
	 * <p>
	 * This method returns an empty array for a {@code null} input array.
	 *
	 * <p>
	 * As a memory optimizing technique an empty array passed in will be overridden with
	 * the empty {@code public static} references in this class.
	 * @param array the array to check for {@code null} or empty
	 * @return the same array, {@code public static} empty array if {@code null} or empty
	 * input
	 * @since 2.5
	 */
	public static float[] nullToEmpty(float[] array) {
		if (isEmpty(array)) {
			return EMPTY_FLOAT_ARRAY;
		}
		return array;
	}

	/**
	 * Defensive programming technique to change a {@code null} reference to an empty one.
	 *
	 * <p>
	 * This method returns an empty array for a {@code null} input array.
	 *
	 * <p>
	 * As a memory optimizing technique an empty array passed in will be overridden with
	 * the empty {@code public static} references in this class.
	 * @param array the array to check for {@code null} or empty
	 * @return the same array, {@code public static} empty array if {@code null} or empty
	 * input
	 * @since 2.5
	 */
	public static Float[] nullToEmpty(Float[] array) {
		if (isEmpty(array)) {
			return EMPTY_FLOAT_OBJECT_ARRAY;
		}
		return array;
	}

	/**
	 * Defensive programming technique to change a {@code null} reference to an empty one.
	 *
	 * <p>
	 * This method returns an empty array for a {@code null} input array.
	 *
	 * <p>
	 * As a memory optimizing technique an empty array passed in will be overridden with
	 * the empty {@code public static} references in this class.
	 * @param array the array to check for {@code null} or empty
	 * @return the same array, {@code public static} empty array if {@code null} or empty
	 * input
	 * @since 2.5
	 */
	public static int[] nullToEmpty(int[] array) {
		if (isEmpty(array)) {
			return EMPTY_INT_ARRAY;
		}
		return array;
	}

	// Primitive/Object array converters
	// ----------------------------------------------------------------------

	/**
	 * Defensive programming technique to change a {@code null} reference to an empty one.
	 *
	 * <p>
	 * This method returns an empty array for a {@code null} input array.
	 *
	 * <p>
	 * As a memory optimizing technique an empty array passed in will be overridden with
	 * the empty {@code public static} references in this class.
	 * @param array the array to check for {@code null} or empty
	 * @return the same array, {@code public static} empty array if {@code null} or empty
	 * input
	 * @since 2.5
	 */
	public static Integer[] nullToEmpty(Integer[] array) {
		if (isEmpty(array)) {
			return EMPTY_INTEGER_OBJECT_ARRAY;
		}
		return array;
	}

	/**
	 * Defensive programming technique to change a {@code null} reference to an empty one.
	 *
	 * <p>
	 * This method returns an empty array for a {@code null} input array.
	 *
	 * <p>
	 * As a memory optimizing technique an empty array passed in will be overridden with
	 * the empty {@code public static} references in this class.
	 * @param array the array to check for {@code null} or empty
	 * @return the same array, {@code public static} empty array if {@code null} or empty
	 * input
	 * @since 2.5
	 */
	public static long[] nullToEmpty(long[] array) {
		if (isEmpty(array)) {
			return EMPTY_LONG_ARRAY;
		}
		return array;
	}

	/**
	 * Defensive programming technique to change a {@code null} reference to an empty one.
	 *
	 * <p>
	 * This method returns an empty array for a {@code null} input array.
	 *
	 * <p>
	 * As a memory optimizing technique an empty array passed in will be overridden with
	 * the empty {@code public static} references in this class.
	 * @param array the array to check for {@code null} or empty
	 * @return the same array, {@code public static} empty array if {@code null} or empty
	 * input
	 * @since 2.5
	 */
	public static Long[] nullToEmpty(Long[] array) {
		if (isEmpty(array)) {
			return EMPTY_LONG_OBJECT_ARRAY;
		}
		return array;
	}

	/**
	 * Defensive programming technique to change a {@code null} reference to an empty one.
	 *
	 * <p>
	 * This method returns an empty array for a {@code null} input array.
	 *
	 * <p>
	 * As a memory optimizing technique an empty array passed in will be overridden with
	 * the empty {@code public static} references in this class.
	 * @param array the array to check for {@code null} or empty
	 * @return the same array, {@code public static} empty array if {@code null} or empty
	 * input
	 * @since 2.5
	 */
	public static Object[] nullToEmpty(Object[] array) {
		if (isEmpty(array)) {
			return EMPTY_OBJECT_ARRAY;
		}
		return array;
	}

	/**
	 * Defensive programming technique to change a {@code null} reference to an empty one.
	 *
	 * <p>
	 * This method returns an empty array for a {@code null} input array.
	 *
	 * <p>
	 * As a memory optimizing technique an empty array passed in will be overridden with
	 * the empty {@code public static} references in this class.
	 * @param array the array to check for {@code null} or empty
	 * @return the same array, {@code public static} empty array if {@code null} or empty
	 * input
	 * @since 2.5
	 */
	public static short[] nullToEmpty(short[] array) {
		if (isEmpty(array)) {
			return EMPTY_SHORT_ARRAY;
		}
		return array;
	}

	/**
	 * Defensive programming technique to change a {@code null} reference to an empty one.
	 *
	 * <p>
	 * This method returns an empty array for a {@code null} input array.
	 *
	 * <p>
	 * As a memory optimizing technique an empty array passed in will be overridden with
	 * the empty {@code public static} references in this class.
	 * @param array the array to check for {@code null} or empty
	 * @return the same array, {@code public static} empty array if {@code null} or empty
	 * input
	 * @since 2.5
	 */
	public static Short[] nullToEmpty(Short[] array) {
		if (isEmpty(array)) {
			return EMPTY_SHORT_OBJECT_ARRAY;
		}
		return array;
	}

	/**
	 * Defensive programming technique to change a {@code null} reference to an empty one.
	 *
	 * <p>
	 * This method returns an empty array for a {@code null} input array.
	 *
	 * <p>
	 * As a memory optimizing technique an empty array passed in will be overridden with
	 * the empty {@code public static} references in this class.
	 * @param array the array to check for {@code null} or empty
	 * @return the same array, {@code public static} empty array if {@code null} or empty
	 * input
	 * @since 2.5
	 */
	public static String[] nullToEmpty(String[] array) {
		if (isEmpty(array)) {
			return EMPTY_STRING_ARRAY;
		}
		return array;
	}

	// nullToEmpty
	// -----------------------------------------------------------------------
	/**
	 * Defensive programming technique to change a {@code null} reference to an empty one.
	 *
	 * <p>
	 * This method returns an empty array for a {@code null} input array.
	 * @param array the array to check for {@code null} or empty
	 * @param type the class representation of the desired array
	 * @param <T> the class type
	 * @return the same array, {@code public static} empty array if {@code null}
	 * @throws IllegalArgumentException if the type argument is null
	 * @since 3.5
	 */
	public static <T> T[] nullToEmpty(T[] array, Class<T[]> type) {
		if (type == null) {
			throw new IllegalArgumentException("The type must not be null");
		}

		if (array == null) {
			return type.cast(Array.newInstance(type.getComponentType(), 0));
		}
		return array;
	}

	/**
	 * Removes the element at the specified position from the specified array. All
	 * subsequent elements are shifted to the left (subtracts one from their indices).
	 *
	 * <p>
	 * This method returns a new array with the same elements of the input array except
	 * the element on the specified position. The component type of the returned array is
	 * always the same as that of the input array.
	 *
	 * <p>
	 * If the input array is {@code null}, an IndexOutOfBoundsException will be thrown,
	 * because in that case no valid index can be specified.
	 *
	 * <pre>
	 * ArrayUtils.remove([true], 0)              = []
	 * ArrayUtils.remove([true, false], 0)       = [false]
	 * ArrayUtils.remove([true, false], 1)       = [true]
	 * ArrayUtils.remove([true, true, false], 1) = [true, false]
	 * </pre>
	 * @param array the array to remove the element from, may not be {@code null}
	 * @param index the position of the element to be removed
	 * @return A new array containing the existing elements except the element at the
	 * specified position.
	 * @throws IndexOutOfBoundsException if the index is out of range (index &lt; 0 ||
	 * index &gt;= array.length), or if the array is {@code null}.
	 * @since 2.1
	 */
	public static boolean[] remove(boolean[] array, int index) {
		return (boolean[]) remove((Object) array, index);
	}

	/**
	 * Removes the element at the specified position from the specified array. All
	 * subsequent elements are shifted to the left (subtracts one from their indices).
	 *
	 * <p>
	 * This method returns a new array with the same elements of the input array except
	 * the element on the specified position. The component type of the returned array is
	 * always the same as that of the input array.
	 *
	 * <p>
	 * If the input array is {@code null}, an IndexOutOfBoundsException will be thrown,
	 * because in that case no valid index can be specified.
	 *
	 * <pre>
	 * ArrayUtils.remove([1], 0)          = []
	 * ArrayUtils.remove([1, 0], 0)       = [0]
	 * ArrayUtils.remove([1, 0], 1)       = [1]
	 * ArrayUtils.remove([1, 0, 1], 1)    = [1, 1]
	 * </pre>
	 * @param array the array to remove the element from, may not be {@code null}
	 * @param index the position of the element to be removed
	 * @return A new array containing the existing elements except the element at the
	 * specified position.
	 * @throws IndexOutOfBoundsException if the index is out of range (index &lt; 0 ||
	 * index &gt;= array.length), or if the array is {@code null}.
	 * @since 2.1
	 */
	public static byte[] remove(byte[] array, int index) {
		return (byte[]) remove((Object) array, index);
	}

	/**
	 * Removes the element at the specified position from the specified array. All
	 * subsequent elements are shifted to the left (subtracts one from their indices).
	 *
	 * <p>
	 * This method returns a new array with the same elements of the input array except
	 * the element on the specified position. The component type of the returned array is
	 * always the same as that of the input array.
	 *
	 * <p>
	 * If the input array is {@code null}, an IndexOutOfBoundsException will be thrown,
	 * because in that case no valid index can be specified.
	 *
	 * <pre>
	 * ArrayUtils.remove(['a'], 0)           = []
	 * ArrayUtils.remove(['a', 'b'], 0)      = ['b']
	 * ArrayUtils.remove(['a', 'b'], 1)      = ['a']
	 * ArrayUtils.remove(['a', 'b', 'c'], 1) = ['a', 'c']
	 * </pre>
	 * @param array the array to remove the element from, may not be {@code null}
	 * @param index the position of the element to be removed
	 * @return A new array containing the existing elements except the element at the
	 * specified position.
	 * @throws IndexOutOfBoundsException if the index is out of range (index &lt; 0 ||
	 * index &gt;= array.length), or if the array is {@code null}.
	 * @since 2.1
	 */
	public static char[] remove(char[] array, int index) {
		return (char[]) remove((Object) array, index);
	}

	/**
	 * Removes the element at the specified position from the specified array. All
	 * subsequent elements are shifted to the left (subtracts one from their indices).
	 *
	 * <p>
	 * This method returns a new array with the same elements of the input array except
	 * the element on the specified position. The component type of the returned array is
	 * always the same as that of the input array.
	 *
	 * <p>
	 * If the input array is {@code null}, an IndexOutOfBoundsException will be thrown,
	 * because in that case no valid index can be specified.
	 *
	 * <pre>
	 * ArrayUtils.remove([1.1], 0)           = []
	 * ArrayUtils.remove([2.5, 6.0], 0)      = [6.0]
	 * ArrayUtils.remove([2.5, 6.0], 1)      = [2.5]
	 * ArrayUtils.remove([2.5, 6.0, 3.8], 1) = [2.5, 3.8]
	 * </pre>
	 * @param array the array to remove the element from, may not be {@code null}
	 * @param index the position of the element to be removed
	 * @return A new array containing the existing elements except the element at the
	 * specified position.
	 * @throws IndexOutOfBoundsException if the index is out of range (index &lt; 0 ||
	 * index &gt;= array.length), or if the array is {@code null}.
	 * @since 2.1
	 */
	public static double[] remove(double[] array, int index) {
		return (double[]) remove((Object) array, index);
	}

	/**
	 * Removes the element at the specified position from the specified array. All
	 * subsequent elements are shifted to the left (subtracts one from their indices).
	 *
	 * <p>
	 * This method returns a new array with the same elements of the input array except
	 * the element on the specified position. The component type of the returned array is
	 * always the same as that of the input array.
	 *
	 * <p>
	 * If the input array is {@code null}, an IndexOutOfBoundsException will be thrown,
	 * because in that case no valid index can be specified.
	 *
	 * <pre>
	 * ArrayUtils.remove([1.1], 0)           = []
	 * ArrayUtils.remove([2.5, 6.0], 0)      = [6.0]
	 * ArrayUtils.remove([2.5, 6.0], 1)      = [2.5]
	 * ArrayUtils.remove([2.5, 6.0, 3.8], 1) = [2.5, 3.8]
	 * </pre>
	 * @param array the array to remove the element from, may not be {@code null}
	 * @param index the position of the element to be removed
	 * @return A new array containing the existing elements except the element at the
	 * specified position.
	 * @throws IndexOutOfBoundsException if the index is out of range (index &lt; 0 ||
	 * index &gt;= array.length), or if the array is {@code null}.
	 * @since 2.1
	 */
	public static float[] remove(float[] array, int index) {
		return (float[]) remove((Object) array, index);
	}

	/**
	 * Removes the element at the specified position from the specified array. All
	 * subsequent elements are shifted to the left (subtracts one from their indices).
	 *
	 * <p>
	 * This method returns a new array with the same elements of the input array except
	 * the element on the specified position. The component type of the returned array is
	 * always the same as that of the input array.
	 *
	 * <p>
	 * If the input array is {@code null}, an IndexOutOfBoundsException will be thrown,
	 * because in that case no valid index can be specified.
	 *
	 * <pre>
	 * ArrayUtils.remove([1], 0)         = []
	 * ArrayUtils.remove([2, 6], 0)      = [6]
	 * ArrayUtils.remove([2, 6], 1)      = [2]
	 * ArrayUtils.remove([2, 6, 3], 1)   = [2, 3]
	 * </pre>
	 * @param array the array to remove the element from, may not be {@code null}
	 * @param index the position of the element to be removed
	 * @return A new array containing the existing elements except the element at the
	 * specified position.
	 * @throws IndexOutOfBoundsException if the index is out of range (index &lt; 0 ||
	 * index &gt;= array.length), or if the array is {@code null}.
	 * @since 2.1
	 */
	public static int[] remove(int[] array, int index) {
		return (int[]) remove((Object) array, index);
	}

	/**
	 * Removes the element at the specified position from the specified array. All
	 * subsequent elements are shifted to the left (subtracts one from their indices).
	 *
	 * <p>
	 * This method returns a new array with the same elements of the input array except
	 * the element on the specified position. The component type of the returned array is
	 * always the same as that of the input array.
	 *
	 * <p>
	 * If the input array is {@code null}, an IndexOutOfBoundsException will be thrown,
	 * because in that case no valid index can be specified.
	 *
	 * <pre>
	 * ArrayUtils.remove([1], 0)         = []
	 * ArrayUtils.remove([2, 6], 0)      = [6]
	 * ArrayUtils.remove([2, 6], 1)      = [2]
	 * ArrayUtils.remove([2, 6, 3], 1)   = [2, 3]
	 * </pre>
	 * @param array the array to remove the element from, may not be {@code null}
	 * @param index the position of the element to be removed
	 * @return A new array containing the existing elements except the element at the
	 * specified position.
	 * @throws IndexOutOfBoundsException if the index is out of range (index &lt; 0 ||
	 * index &gt;= array.length), or if the array is {@code null}.
	 * @since 2.1
	 */
	public static long[] remove(long[] array, int index) {
		return (long[]) remove((Object) array, index);
	}

	/**
	 * Removes the element at the specified position from the specified array. All
	 * subsequent elements are shifted to the left (subtracts one from their indices).
	 *
	 * <p>
	 * This method returns a new array with the same elements of the input array except
	 * the element on the specified position. The component type of the returned array is
	 * always the same as that of the input array.
	 *
	 * <p>
	 * If the input array is {@code null}, an IndexOutOfBoundsException will be thrown,
	 * because in that case no valid index can be specified.
	 * @param array the array to remove the element from, may not be {@code null}
	 * @param index the position of the element to be removed
	 * @return A new array containing the existing elements except the element at the
	 * specified position.
	 * @throws IndexOutOfBoundsException if the index is out of range (index &lt; 0 ||
	 * index &gt;= array.length), or if the array is {@code null}.
	 * @since 2.1
	 */
	private static Object remove(Object array, int index) {
		int length = getLength(array);
		if (index < 0 || index >= length) {
			throw new IndexOutOfBoundsException("Index: " + index + ", Length: " + length);
		}

		Object result = Array.newInstance(array.getClass().getComponentType(), length - 1);
		System.arraycopy(array, 0, result, 0, index);
		if (index < length - 1) {
			System.arraycopy(array, index + 1, result, index, length - index - 1);
		}

		return result;
	}

	/**
	 * Removes the element at the specified position from the specified array. All
	 * subsequent elements are shifted to the left (subtracts one from their indices).
	 *
	 * <p>
	 * This method returns a new array with the same elements of the input array except
	 * the element on the specified position. The component type of the returned array is
	 * always the same as that of the input array.
	 *
	 * <p>
	 * If the input array is {@code null}, an IndexOutOfBoundsException will be thrown,
	 * because in that case no valid index can be specified.
	 *
	 * <pre>
	 * ArrayUtils.remove([1], 0)         = []
	 * ArrayUtils.remove([2, 6], 0)      = [6]
	 * ArrayUtils.remove([2, 6], 1)      = [2]
	 * ArrayUtils.remove([2, 6, 3], 1)   = [2, 3]
	 * </pre>
	 * @param array the array to remove the element from, may not be {@code null}
	 * @param index the position of the element to be removed
	 * @return A new array containing the existing elements except the element at the
	 * specified position.
	 * @throws IndexOutOfBoundsException if the index is out of range (index &lt; 0 ||
	 * index &gt;= array.length), or if the array is {@code null}.
	 * @since 2.1
	 */
	public static short[] remove(short[] array, int index) {
		return (short[]) remove((Object) array, index);
	}

	/**
	 * Removes the element at the specified position from the specified array. All
	 * subsequent elements are shifted to the left (subtracts one from their indices).
	 *
	 * <p>
	 * This method returns a new array with the same elements of the input array except
	 * the element on the specified position. The component type of the returned array is
	 * always the same as that of the input array.
	 *
	 * <p>
	 * If the input array is {@code null}, an IndexOutOfBoundsException will be thrown,
	 * because in that case no valid index can be specified.
	 *
	 * <pre>
	 * ArrayUtils.remove(["a"], 0)           = []
	 * ArrayUtils.remove(["a", "b"], 0)      = ["b"]
	 * ArrayUtils.remove(["a", "b"], 1)      = ["a"]
	 * ArrayUtils.remove(["a", "b", "c"], 1) = ["a", "c"]
	 * </pre>
	 * @param <T> the component type of the array
	 * @param array the array to remove the element from, may not be {@code null}
	 * @param index the position of the element to be removed
	 * @return A new array containing the existing elements except the element at the
	 * specified position.
	 * @throws IndexOutOfBoundsException if the index is out of range (index &lt; 0 ||
	 * index &gt;= array.length), or if the array is {@code null}.
	 * @since 2.1
	 */
	// remove() always creates an array of the same type as
	// its input
	public static <T> T[] remove(T[] array, int index) {
		return (T[]) remove((Object) array, index);
	}

	/**
	 * Removes the elements at the specified positions from the specified array. All
	 * remaining elements are shifted to the left.
	 *
	 * <p>
	 * This method returns a new array with the same elements of the input array except
	 * those at the specified positions. The component type of the returned array is
	 * always the same as that of the input array.
	 *
	 * <p>
	 * If the input array is {@code null}, an IndexOutOfBoundsException will be thrown,
	 * because in that case no valid index can be specified.
	 *
	 * <pre>
	 * ArrayUtils.removeAll([true, false, true], 0, 2) = [false]
	 * ArrayUtils.removeAll([true, false, true], 1, 2) = [true]
	 * </pre>
	 * @param array the array to remove the element from, may not be {@code null}
	 * @param indices the positions of the elements to be removed
	 * @return A new array containing the existing elements except those at the specified
	 * positions.
	 * @throws IndexOutOfBoundsException if any index is out of range (index &lt; 0 ||
	 * index &gt;= array.length), or if the array is {@code null}.
	 * @since 3.0.1
	 */
	public static boolean[] removeAll(boolean[] array, int... indices) {
		return (boolean[]) removeAll((Object) array, indices);
	}

	/**
	 * Removes the elements at the specified positions from the specified array. All
	 * remaining elements are shifted to the left.
	 *
	 * <p>
	 * This method returns a new array with the same elements of the input array except
	 * those at the specified positions. The component type of the returned array is
	 * always the same as that of the input array.
	 *
	 * <p>
	 * If the input array is {@code null}, an IndexOutOfBoundsException will be thrown,
	 * because in that case no valid index can be specified.
	 *
	 * <pre>
	 * ArrayUtils.removeAll([1], 0)             = []
	 * ArrayUtils.removeAll([2, 6], 0)          = [6]
	 * ArrayUtils.removeAll([2, 6], 0, 1)       = []
	 * ArrayUtils.removeAll([2, 6, 3], 1, 2)    = [2]
	 * ArrayUtils.removeAll([2, 6, 3], 0, 2)    = [6]
	 * ArrayUtils.removeAll([2, 6, 3], 0, 1, 2) = []
	 * </pre>
	 * @param array the array to remove the element from, may not be {@code null}
	 * @param indices the positions of the elements to be removed
	 * @return A new array containing the existing elements except those at the specified
	 * positions.
	 * @throws IndexOutOfBoundsException if any index is out of range (index &lt; 0 ||
	 * index &gt;= array.length), or if the array is {@code null}.
	 * @since 3.0.1
	 */
	public static byte[] removeAll(byte[] array, int... indices) {
		return (byte[]) removeAll((Object) array, indices);
	}

	/**
	 * Removes the elements at the specified positions from the specified array. All
	 * remaining elements are shifted to the left.
	 *
	 * <p>
	 * This method returns a new array with the same elements of the input array except
	 * those at the specified positions. The component type of the returned array is
	 * always the same as that of the input array.
	 *
	 * <p>
	 * If the input array is {@code null}, an IndexOutOfBoundsException will be thrown,
	 * because in that case no valid index can be specified.
	 *
	 * <pre>
	 * ArrayUtils.removeAll([1], 0)             = []
	 * ArrayUtils.removeAll([2, 6], 0)          = [6]
	 * ArrayUtils.removeAll([2, 6], 0, 1)       = []
	 * ArrayUtils.removeAll([2, 6, 3], 1, 2)    = [2]
	 * ArrayUtils.removeAll([2, 6, 3], 0, 2)    = [6]
	 * ArrayUtils.removeAll([2, 6, 3], 0, 1, 2) = []
	 * </pre>
	 * @param array the array to remove the element from, may not be {@code null}
	 * @param indices the positions of the elements to be removed
	 * @return A new array containing the existing elements except those at the specified
	 * positions.
	 * @throws IndexOutOfBoundsException if any index is out of range (index &lt; 0 ||
	 * index &gt;= array.length), or if the array is {@code null}.
	 * @since 3.0.1
	 */
	public static char[] removeAll(char[] array, int... indices) {
		return (char[]) removeAll((Object) array, indices);
	}

	/**
	 * Removes the elements at the specified positions from the specified array. All
	 * remaining elements are shifted to the left.
	 *
	 * <p>
	 * This method returns a new array with the same elements of the input array except
	 * those at the specified positions. The component type of the returned array is
	 * always the same as that of the input array.
	 *
	 * <p>
	 * If the input array is {@code null}, an IndexOutOfBoundsException will be thrown,
	 * because in that case no valid index can be specified.
	 *
	 * <pre>
	 * ArrayUtils.removeAll([1], 0)             = []
	 * ArrayUtils.removeAll([2, 6], 0)          = [6]
	 * ArrayUtils.removeAll([2, 6], 0, 1)       = []
	 * ArrayUtils.removeAll([2, 6, 3], 1, 2)    = [2]
	 * ArrayUtils.removeAll([2, 6, 3], 0, 2)    = [6]
	 * ArrayUtils.removeAll([2, 6, 3], 0, 1, 2) = []
	 * </pre>
	 * @param array the array to remove the element from, may not be {@code null}
	 * @param indices the positions of the elements to be removed
	 * @return A new array containing the existing elements except those at the specified
	 * positions.
	 * @throws IndexOutOfBoundsException if any index is out of range (index &lt; 0 ||
	 * index &gt;= array.length), or if the array is {@code null}.
	 * @since 3.0.1
	 */
	public static double[] removeAll(double[] array, int... indices) {
		return (double[]) removeAll((Object) array, indices);
	}

	/**
	 * Removes the elements at the specified positions from the specified array. All
	 * remaining elements are shifted to the left.
	 *
	 * <p>
	 * This method returns a new array with the same elements of the input array except
	 * those at the specified positions. The component type of the returned array is
	 * always the same as that of the input array.
	 *
	 * <p>
	 * If the input array is {@code null}, an IndexOutOfBoundsException will be thrown,
	 * because in that case no valid index can be specified.
	 *
	 * <pre>
	 * ArrayUtils.removeAll([1], 0)             = []
	 * ArrayUtils.removeAll([2, 6], 0)          = [6]
	 * ArrayUtils.removeAll([2, 6], 0, 1)       = []
	 * ArrayUtils.removeAll([2, 6, 3], 1, 2)    = [2]
	 * ArrayUtils.removeAll([2, 6, 3], 0, 2)    = [6]
	 * ArrayUtils.removeAll([2, 6, 3], 0, 1, 2) = []
	 * </pre>
	 * @param array the array to remove the element from, may not be {@code null}
	 * @param indices the positions of the elements to be removed
	 * @return A new array containing the existing elements except those at the specified
	 * positions.
	 * @throws IndexOutOfBoundsException if any index is out of range (index &lt; 0 ||
	 * index &gt;= array.length), or if the array is {@code null}.
	 * @since 3.0.1
	 */
	public static float[] removeAll(float[] array, int... indices) {
		return (float[]) removeAll((Object) array, indices);
	}

	/**
	 * Removes the elements at the specified positions from the specified array. All
	 * remaining elements are shifted to the left.
	 *
	 * <p>
	 * This method returns a new array with the same elements of the input array except
	 * those at the specified positions. The component type of the returned array is
	 * always the same as that of the input array.
	 *
	 * <p>
	 * If the input array is {@code null}, an IndexOutOfBoundsException will be thrown,
	 * because in that case no valid index can be specified.
	 *
	 * <pre>
	 * ArrayUtils.removeAll([1], 0)             = []
	 * ArrayUtils.removeAll([2, 6], 0)          = [6]
	 * ArrayUtils.removeAll([2, 6], 0, 1)       = []
	 * ArrayUtils.removeAll([2, 6, 3], 1, 2)    = [2]
	 * ArrayUtils.removeAll([2, 6, 3], 0, 2)    = [6]
	 * ArrayUtils.removeAll([2, 6, 3], 0, 1, 2) = []
	 * </pre>
	 * @param array the array to remove the element from, may not be {@code null}
	 * @param indices the positions of the elements to be removed
	 * @return A new array containing the existing elements except those at the specified
	 * positions.
	 * @throws IndexOutOfBoundsException if any index is out of range (index &lt; 0 ||
	 * index &gt;= array.length), or if the array is {@code null}.
	 * @since 3.0.1
	 */
	public static int[] removeAll(int[] array, int... indices) {
		return (int[]) removeAll((Object) array, indices);
	}

	/**
	 * Removes the elements at the specified positions from the specified array. All
	 * remaining elements are shifted to the left.
	 *
	 * <p>
	 * This method returns a new array with the same elements of the input array except
	 * those at the specified positions. The component type of the returned array is
	 * always the same as that of the input array.
	 *
	 * <p>
	 * If the input array is {@code null}, an IndexOutOfBoundsException will be thrown,
	 * because in that case no valid index can be specified.
	 *
	 * <pre>
	 * ArrayUtils.removeAll([1], 0)             = []
	 * ArrayUtils.removeAll([2, 6], 0)          = [6]
	 * ArrayUtils.removeAll([2, 6], 0, 1)       = []
	 * ArrayUtils.removeAll([2, 6, 3], 1, 2)    = [2]
	 * ArrayUtils.removeAll([2, 6, 3], 0, 2)    = [6]
	 * ArrayUtils.removeAll([2, 6, 3], 0, 1, 2) = []
	 * </pre>
	 * @param array the array to remove the element from, may not be {@code null}
	 * @param indices the positions of the elements to be removed
	 * @return A new array containing the existing elements except those at the specified
	 * positions.
	 * @throws IndexOutOfBoundsException if any index is out of range (index &lt; 0 ||
	 * index &gt;= array.length), or if the array is {@code null}.
	 * @since 3.0.1
	 */
	public static long[] removeAll(long[] array, int... indices) {
		return (long[]) removeAll((Object) array, indices);
	}

	/**
	 * Removes multiple array elements specified by indices.
	 * @param array source
	 * @param indices to remove
	 * @return new array of same type minus elements specified by the set bits in
	 * {@code indices}
	 * @since 3.2
	 */
	// package protected for access by unit tests
	static Object removeAll(Object array, BitSet indices) {
		if (array == null) {
			return null;
		}

		int srcLength = getLength(array);
		// No need to check maxIndex here, because method only currently called from
		// removeElements()
		// which guarantee to generate on;y valid bit entries.
		// final int maxIndex = indices.length();
		// if (maxIndex > srcLength) {
		// throw new IndexOutOfBoundsException("Index: " + (maxIndex-1) + ", Length: " +
		// srcLength);
		// }
		int removals = indices.cardinality(); // true bits are items to remove
		Object result = Array.newInstance(array.getClass().getComponentType(), srcLength - removals);
		int srcIndex = 0;
		int destIndex = 0;
		int count;
		int set;
		while ((set = indices.nextSetBit(srcIndex)) != -1) {
			count = set - srcIndex;
			if (count > 0) {
				System.arraycopy(array, srcIndex, result, destIndex, count);
				destIndex += count;
			}
			srcIndex = indices.nextClearBit(set);
		}
		count = srcLength - srcIndex;
		if (count > 0) {
			System.arraycopy(array, srcIndex, result, destIndex, count);
		}
		return result;
	}

	/**
	 * Removes multiple array elements specified by index.
	 * @param array source
	 * @param indices to remove
	 * @return new array of same type minus elements specified by unique values of
	 * {@code indices}
	 * @since 3.0.1
	 */
	// package protected for access by unit tests
	static Object removeAll(Object array, int... indices) {
		int length = getLength(array);
		int diff = 0; // number of distinct indexes, i.e. number of entries that will be
		// removed
		int[] clonedIndices = clone(indices);
		Arrays.sort(clonedIndices);

		// identify length of result array
		if (isNotEmpty(clonedIndices)) {
			int i = clonedIndices.length;
			int prevIndex = length;
			while (--i >= 0) {
				int index = clonedIndices[i];
				if (index < 0 || index >= length) {
					throw new IndexOutOfBoundsException("Index: " + index + ", Length: " + length);
				}
				if (index >= prevIndex) {
					continue;
				}
				diff++;
				prevIndex = index;
			}
		}

		// create result array
		Object result = Array.newInstance(array.getClass().getComponentType(), length - diff);
		if (diff < length) {
			int end = length; // index just after last copy
			int dest = length - diff; // number of entries so far not copied
			for (int i = clonedIndices.length - 1; i >= 0; i--) {
				int index = clonedIndices[i];
				if (end - index > 1) { // same as (cp > 0)
					int cp = end - index - 1;
					dest -= cp;
					System.arraycopy(array, index + 1, result, dest, cp);
					// Afer this copy, we still have room for dest items.
				}
				end = index;
			}
			if (end > 0) {
				System.arraycopy(array, 0, result, 0, end);
			}
		}
		return result;
	}

	/**
	 * Removes the elements at the specified positions from the specified array. All
	 * remaining elements are shifted to the left.
	 *
	 * <p>
	 * This method returns a new array with the same elements of the input array except
	 * those at the specified positions. The component type of the returned array is
	 * always the same as that of the input array.
	 *
	 * <p>
	 * If the input array is {@code null}, an IndexOutOfBoundsException will be thrown,
	 * because in that case no valid index can be specified.
	 *
	 * <pre>
	 * ArrayUtils.removeAll([1], 0)             = []
	 * ArrayUtils.removeAll([2, 6], 0)          = [6]
	 * ArrayUtils.removeAll([2, 6], 0, 1)       = []
	 * ArrayUtils.removeAll([2, 6, 3], 1, 2)    = [2]
	 * ArrayUtils.removeAll([2, 6, 3], 0, 2)    = [6]
	 * ArrayUtils.removeAll([2, 6, 3], 0, 1, 2) = []
	 * </pre>
	 * @param array the array to remove the element from, may not be {@code null}
	 * @param indices the positions of the elements to be removed
	 * @return A new array containing the existing elements except those at the specified
	 * positions.
	 * @throws IndexOutOfBoundsException if any index is out of range (index &lt; 0 ||
	 * index &gt;= array.length), or if the array is {@code null}.
	 * @since 3.0.1
	 */
	public static short[] removeAll(short[] array, int... indices) {
		return (short[]) removeAll((Object) array, indices);
	}

	/**
	 * Removes the elements at the specified positions from the specified array. All
	 * remaining elements are shifted to the left.
	 *
	 * <p>
	 * This method returns a new array with the same elements of the input array except
	 * those at the specified positions. The component type of the returned array is
	 * always the same as that of the input array.
	 *
	 * <p>
	 * If the input array is {@code null}, an IndexOutOfBoundsException will be thrown,
	 * because in that case no valid index can be specified.
	 *
	 * <pre>
	 * ArrayUtils.removeAll(["a", "b", "c"], 0, 2) = ["b"]
	 * ArrayUtils.removeAll(["a", "b", "c"], 1, 2) = ["a"]
	 * </pre>
	 * @param <T> the component type of the array
	 * @param array the array to remove the element from, may not be {@code null}
	 * @param indices the positions of the elements to be removed
	 * @return A new array containing the existing elements except those at the specified
	 * positions.
	 * @throws IndexOutOfBoundsException if any index is out of range (index &lt; 0 ||
	 * index &gt;= array.length), or if the array is {@code null}.
	 * @since 3.0.1
	 */
	// removeAll() always creates an array of the same type
	// as its input
	public static <T> T[] removeAll(T[] array, int... indices) {
		return (T[]) removeAll((Object) array, indices);
	}

	/**
	 * Removes the occurrences of the specified element from the specified boolean array.
	 *
	 * <p>
	 * All subsequent elements are shifted to the left (subtracts one from their indices).
	 * If the array doesn't contains such an element, no elements are removed from the
	 * array. {@code null} will be returned if the input array is {@code null}.
	 * @param element the element to remove
	 * @param array the input array
	 * @return A new array containing the existing elements except the occurrences of the
	 * specified element.
	 * @since 3.5
	 * @deprecated Use {@link #removeAllOccurrences(boolean[], boolean)}
	 */
	public static boolean[] removeAllOccurences(boolean[] array, boolean element) {
		return (boolean[]) removeAll((Object) array, indexesOf(array, element));
	}

	/**
	 * Removes the occurrences of the specified element from the specified byte array.
	 *
	 * <p>
	 * All subsequent elements are shifted to the left (subtracts one from their indices).
	 * If the array doesn't contains such an element, no elements are removed from the
	 * array. {@code null} will be returned if the input array is {@code null}.
	 * @param element the element to remove
	 * @param array the input array
	 * @return A new array containing the existing elements except the occurrences of the
	 * specified element.
	 * @since 3.5
	 * @deprecated Use {@link #removeAllOccurrences(byte[], byte)}
	 */
	public static byte[] removeAllOccurences(byte[] array, byte element) {
		return (byte[]) removeAll((Object) array, indexesOf(array, element));
	}

	/**
	 * Removes the occurrences of the specified element from the specified char array.
	 *
	 * <p>
	 * All subsequent elements are shifted to the left (subtracts one from their indices).
	 * If the array doesn't contains such an element, no elements are removed from the
	 * array. {@code null} will be returned if the input array is {@code null}.
	 * @param element the element to remove
	 * @param array the input array
	 * @return A new array containing the existing elements except the occurrences of the
	 * specified element.
	 * @since 3.5
	 * @deprecated Use {@link #removeAllOccurrences(char[], char)}
	 */
	public static char[] removeAllOccurences(char[] array, char element) {
		return (char[]) removeAll((Object) array, indexesOf(array, element));
	}

	/**
	 * Removes the occurrences of the specified element from the specified double array.
	 *
	 * <p>
	 * All subsequent elements are shifted to the left (subtracts one from their indices).
	 * If the array doesn't contains such an element, no elements are removed from the
	 * array. {@code null} will be returned if the input array is {@code null}.
	 * @param element the element to remove
	 * @param array the input array
	 * @return A new array containing the existing elements except the occurrences of the
	 * specified element.
	 * @since 3.5
	 * @deprecated Use {@link #removeAllOccurrences(double[], double)}
	 */
	public static double[] removeAllOccurences(double[] array, double element) {
		return (double[]) removeAll((Object) array, indexesOf(array, element));
	}

	/**
	 * Removes the occurrences of the specified element from the specified float array.
	 *
	 * <p>
	 * All subsequent elements are shifted to the left (subtracts one from their indices).
	 * If the array doesn't contains such an element, no elements are removed from the
	 * array. {@code null} will be returned if the input array is {@code null}.
	 * @param element the element to remove
	 * @param array the input array
	 * @return A new array containing the existing elements except the occurrences of the
	 * specified element.
	 * @since 3.5
	 * @deprecated Use {@link #removeAllOccurrences(float[], float)}
	 */
	public static float[] removeAllOccurences(float[] array, float element) {
		return (float[]) removeAll((Object) array, indexesOf(array, element));
	}

	/**
	 * Removes the occurrences of the specified element from the specified int array.
	 *
	 * <p>
	 * All subsequent elements are shifted to the left (subtracts one from their indices).
	 * If the array doesn't contains such an element, no elements are removed from the
	 * array. {@code null} will be returned if the input array is {@code null}.
	 * @param element the element to remove
	 * @param array the input array
	 * @return A new array containing the existing elements except the occurrences of the
	 * specified element.
	 * @since 3.5
	 * @deprecated Use {@link #removeAllOccurrences(int[], int)}
	 */
	public static int[] removeAllOccurences(int[] array, int element) {
		return (int[]) removeAll((Object) array, indexesOf(array, element));
	}

	/**
	 * Removes the occurrences of the specified element from the specified long array.
	 *
	 * <p>
	 * All subsequent elements are shifted to the left (subtracts one from their indices).
	 * If the array doesn't contains such an element, no elements are removed from the
	 * array. {@code null} will be returned if the input array is {@code null}.
	 * @param element the element to remove
	 * @param array the input array
	 * @return A new array containing the existing elements except the occurrences of the
	 * specified element.
	 * @since 3.5
	 * @deprecated Use {@link #removeAllOccurrences(long[], long)}
	 */
	public static long[] removeAllOccurences(long[] array, long element) {
		return (long[]) removeAll((Object) array, indexesOf(array, element));
	}

	/**
	 * Removes the occurrences of the specified element from the specified short array.
	 *
	 * <p>
	 * All subsequent elements are shifted to the left (subtracts one from their indices).
	 * If the array doesn't contains such an element, no elements are removed from the
	 * array. {@code null} will be returned if the input array is {@code null}.
	 * @param element the element to remove
	 * @param array the input array
	 * @return A new array containing the existing elements except the occurrences of the
	 * specified element.
	 * @since 3.5
	 * @deprecated Use {@link #removeAllOccurrences(short[], short)}
	 */
	public static short[] removeAllOccurences(short[] array, short element) {
		return (short[]) removeAll((Object) array, indexesOf(array, element));
	}

	/**
	 * Removes the occurrences of the specified element from the specified array.
	 *
	 * <p>
	 * All subsequent elements are shifted to the left (subtracts one from their indices).
	 * If the array doesn't contains such an element, no elements are removed from the
	 * array. {@code null} will be returned if the input array is {@code null}.
	 * @param <T> the type of object in the array
	 * @param element the element to remove
	 * @param array the input array
	 * @return A new array containing the existing elements except the occurrences of the
	 * specified element.
	 * @since 3.5
	 * @deprecated Use {@link #removeAllOccurrences(Object[], Object)}
	 */
	public static <T> T[] removeAllOccurences(T[] array, T element) {
		return (T[]) removeAll((Object) array, indexesOf(array, element));
	}

	/**
	 * Removes the occurrences of the specified element from the specified boolean array.
	 *
	 * <p>
	 * All subsequent elements are shifted to the left (subtracts one from their indices).
	 * If the array doesn't contains such an element, no elements are removed from the
	 * array. {@code null} will be returned if the input array is {@code null}.
	 * @param element the element to remove
	 * @param array the input array
	 * @return A new array containing the existing elements except the occurrences of the
	 * specified element.
	 * @since 3.10
	 */
	public static boolean[] removeAllOccurrences(boolean[] array, boolean element) {
		return (boolean[]) removeAll((Object) array, indexesOf(array, element));
	}

	/**
	 * Removes the occurrences of the specified element from the specified byte array.
	 *
	 * <p>
	 * All subsequent elements are shifted to the left (subtracts one from their indices).
	 * If the array doesn't contains such an element, no elements are removed from the
	 * array. {@code null} will be returned if the input array is {@code null}.
	 * @param element the element to remove
	 * @param array the input array
	 * @return A new array containing the existing elements except the occurrences of the
	 * specified element.
	 * @since 3.10
	 */
	public static byte[] removeAllOccurrences(byte[] array, byte element) {
		return (byte[]) removeAll((Object) array, indexesOf(array, element));
	}

	/**
	 * Removes the occurrences of the specified element from the specified char array.
	 *
	 * <p>
	 * All subsequent elements are shifted to the left (subtracts one from their indices).
	 * If the array doesn't contains such an element, no elements are removed from the
	 * array. {@code null} will be returned if the input array is {@code null}.
	 * @param element the element to remove
	 * @param array the input array
	 * @return A new array containing the existing elements except the occurrences of the
	 * specified element.
	 * @since 3.10
	 */
	public static char[] removeAllOccurrences(char[] array, char element) {
		return (char[]) removeAll((Object) array, indexesOf(array, element));
	}

	/**
	 * Removes the occurrences of the specified element from the specified double array.
	 *
	 * <p>
	 * All subsequent elements are shifted to the left (subtracts one from their indices).
	 * If the array doesn't contains such an element, no elements are removed from the
	 * array. {@code null} will be returned if the input array is {@code null}.
	 * @param element the element to remove
	 * @param array the input array
	 * @return A new array containing the existing elements except the occurrences of the
	 * specified element.
	 * @since 3.10
	 */
	public static double[] removeAllOccurrences(double[] array, double element) {
		return (double[]) removeAll((Object) array, indexesOf(array, element));
	}

	/**
	 * Removes the occurrences of the specified element from the specified float array.
	 *
	 * <p>
	 * All subsequent elements are shifted to the left (subtracts one from their indices).
	 * If the array doesn't contains such an element, no elements are removed from the
	 * array. {@code null} will be returned if the input array is {@code null}.
	 * @param element the element to remove
	 * @param array the input array
	 * @return A new array containing the existing elements except the occurrences of the
	 * specified element.
	 * @since 3.10
	 */
	public static float[] removeAllOccurrences(float[] array, float element) {
		return (float[]) removeAll((Object) array, indexesOf(array, element));
	}

	/**
	 * Removes the occurrences of the specified element from the specified int array.
	 *
	 * <p>
	 * All subsequent elements are shifted to the left (subtracts one from their indices).
	 * If the array doesn't contains such an element, no elements are removed from the
	 * array. {@code null} will be returned if the input array is {@code null}.
	 * @param element the element to remove
	 * @param array the input array
	 * @return A new array containing the existing elements except the occurrences of the
	 * specified element.
	 * @since 3.10
	 */
	public static int[] removeAllOccurrences(int[] array, int element) {
		return (int[]) removeAll((Object) array, indexesOf(array, element));
	}

	/**
	 * Removes the occurrences of the specified element from the specified long array.
	 *
	 * <p>
	 * All subsequent elements are shifted to the left (subtracts one from their indices).
	 * If the array doesn't contains such an element, no elements are removed from the
	 * array. {@code null} will be returned if the input array is {@code null}.
	 * @param element the element to remove
	 * @param array the input array
	 * @return A new array containing the existing elements except the occurrences of the
	 * specified element.
	 * @since 3.10
	 */
	public static long[] removeAllOccurrences(long[] array, long element) {
		return (long[]) removeAll((Object) array, indexesOf(array, element));
	}

	/**
	 * Removes the occurrences of the specified element from the specified short array.
	 *
	 * <p>
	 * All subsequent elements are shifted to the left (subtracts one from their indices).
	 * If the array doesn't contains such an element, no elements are removed from the
	 * array. {@code null} will be returned if the input array is {@code null}.
	 * @param element the element to remove
	 * @param array the input array
	 * @return A new array containing the existing elements except the occurrences of the
	 * specified element.
	 * @since 3.10
	 */
	public static short[] removeAllOccurrences(short[] array, short element) {
		return (short[]) removeAll((Object) array, indexesOf(array, element));
	}

	/**
	 * Removes the occurrences of the specified element from the specified array.
	 *
	 * <p>
	 * All subsequent elements are shifted to the left (subtracts one from their indices).
	 * If the array doesn't contains such an element, no elements are removed from the
	 * array. {@code null} will be returned if the input array is {@code null}.
	 * @param <T> the type of object in the array
	 * @param element the element to remove
	 * @param array the input array
	 * @return A new array containing the existing elements except the occurrences of the
	 * specified element.
	 * @since 3.10
	 */
	public static <T> T[] removeAllOccurrences(T[] array, T element) {
		return (T[]) removeAll((Object) array, indexesOf(array, element));
	}

	/**
	 * Removes the first occurrence of the specified element from the specified array. All
	 * subsequent elements are shifted to the left (subtracts one from their indices). If
	 * the array doesn't contains such an element, no elements are removed from the array.
	 *
	 * <p>
	 * This method returns a new array with the same elements of the input array except
	 * the first occurrence of the specified element. The component type of the returned
	 * array is always the same as that of the input array.
	 *
	 * <pre>
	 * ArrayUtils.removeElement(null, true)                = null
	 * ArrayUtils.removeElement([], true)                  = []
	 * ArrayUtils.removeElement([true], false)             = [true]
	 * ArrayUtils.removeElement([true, false], false)      = [true]
	 * ArrayUtils.removeElement([true, false, true], true) = [false, true]
	 * </pre>
	 * @param array the array to remove the element from, may be {@code null}
	 * @param element the element to be removed
	 * @return A new array containing the existing elements except the first occurrence of
	 * the specified element.
	 * @since 2.1
	 */
	public static boolean[] removeElement(boolean[] array, boolean element) {
		int index = indexOf(array, element);
		if (index == INDEX_NOT_FOUND) {
			return clone(array);
		}
		return remove(array, index);
	}

	/**
	 * Removes the first occurrence of the specified element from the specified array. All
	 * subsequent elements are shifted to the left (subtracts one from their indices). If
	 * the array doesn't contains such an element, no elements are removed from the array.
	 *
	 * <p>
	 * This method returns a new array with the same elements of the input array except
	 * the first occurrence of the specified element. The component type of the returned
	 * array is always the same as that of the input array.
	 *
	 * <pre>
	 * ArrayUtils.removeElement(null, 1)        = null
	 * ArrayUtils.removeElement([], 1)          = []
	 * ArrayUtils.removeElement([1], 0)         = [1]
	 * ArrayUtils.removeElement([1, 0], 0)      = [1]
	 * ArrayUtils.removeElement([1, 0, 1], 1)   = [0, 1]
	 * </pre>
	 * @param array the array to remove the element from, may be {@code null}
	 * @param element the element to be removed
	 * @return A new array containing the existing elements except the first occurrence of
	 * the specified element.
	 * @since 2.1
	 */
	public static byte[] removeElement(byte[] array, byte element) {
		int index = indexOf(array, element);
		if (index == INDEX_NOT_FOUND) {
			return clone(array);
		}
		return remove(array, index);
	}

	/**
	 * Removes the first occurrence of the specified element from the specified array. All
	 * subsequent elements are shifted to the left (subtracts one from their indices). If
	 * the array doesn't contains such an element, no elements are removed from the array.
	 *
	 * <p>
	 * This method returns a new array with the same elements of the input array except
	 * the first occurrence of the specified element. The component type of the returned
	 * array is always the same as that of the input array.
	 *
	 * <pre>
	 * ArrayUtils.removeElement(null, 'a')            = null
	 * ArrayUtils.removeElement([], 'a')              = []
	 * ArrayUtils.removeElement(['a'], 'b')           = ['a']
	 * ArrayUtils.removeElement(['a', 'b'], 'a')      = ['b']
	 * ArrayUtils.removeElement(['a', 'b', 'a'], 'a') = ['b', 'a']
	 * </pre>
	 * @param array the array to remove the element from, may be {@code null}
	 * @param element the element to be removed
	 * @return A new array containing the existing elements except the first occurrence of
	 * the specified element.
	 * @since 2.1
	 */
	public static char[] removeElement(char[] array, char element) {
		int index = indexOf(array, element);
		if (index == INDEX_NOT_FOUND) {
			return clone(array);
		}
		return remove(array, index);
	}

	/**
	 * Removes the first occurrence of the specified element from the specified array. All
	 * subsequent elements are shifted to the left (subtracts one from their indices). If
	 * the array doesn't contains such an element, no elements are removed from the array.
	 *
	 * <p>
	 * This method returns a new array with the same elements of the input array except
	 * the first occurrence of the specified element. The component type of the returned
	 * array is always the same as that of the input array.
	 *
	 * <pre>
	 * ArrayUtils.removeElement(null, 1.1)            = null
	 * ArrayUtils.removeElement([], 1.1)              = []
	 * ArrayUtils.removeElement([1.1], 1.2)           = [1.1]
	 * ArrayUtils.removeElement([1.1, 2.3], 1.1)      = [2.3]
	 * ArrayUtils.removeElement([1.1, 2.3, 1.1], 1.1) = [2.3, 1.1]
	 * </pre>
	 * @param array the array to remove the element from, may be {@code null}
	 * @param element the element to be removed
	 * @return A new array containing the existing elements except the first occurrence of
	 * the specified element.
	 * @since 2.1
	 */
	public static double[] removeElement(double[] array, double element) {
		int index = indexOf(array, element);
		if (index == INDEX_NOT_FOUND) {
			return clone(array);
		}
		return remove(array, index);
	}

	/**
	 * Removes the first occurrence of the specified element from the specified array. All
	 * subsequent elements are shifted to the left (subtracts one from their indices). If
	 * the array doesn't contains such an element, no elements are removed from the array.
	 *
	 * <p>
	 * This method returns a new array with the same elements of the input array except
	 * the first occurrence of the specified element. The component type of the returned
	 * array is always the same as that of the input array.
	 *
	 * <pre>
	 * ArrayUtils.removeElement(null, 1.1)            = null
	 * ArrayUtils.removeElement([], 1.1)              = []
	 * ArrayUtils.removeElement([1.1], 1.2)           = [1.1]
	 * ArrayUtils.removeElement([1.1, 2.3], 1.1)      = [2.3]
	 * ArrayUtils.removeElement([1.1, 2.3, 1.1], 1.1) = [2.3, 1.1]
	 * </pre>
	 * @param array the array to remove the element from, may be {@code null}
	 * @param element the element to be removed
	 * @return A new array containing the existing elements except the first occurrence of
	 * the specified element.
	 * @since 2.1
	 */
	public static float[] removeElement(float[] array, float element) {
		int index = indexOf(array, element);
		if (index == INDEX_NOT_FOUND) {
			return clone(array);
		}
		return remove(array, index);
	}

	/**
	 * Removes the first occurrence of the specified element from the specified array. All
	 * subsequent elements are shifted to the left (subtracts one from their indices). If
	 * the array doesn't contains such an element, no elements are removed from the array.
	 *
	 * <p>
	 * This method returns a new array with the same elements of the input array except
	 * the first occurrence of the specified element. The component type of the returned
	 * array is always the same as that of the input array.
	 *
	 * <pre>
	 * ArrayUtils.removeElement(null, 1)      = null
	 * ArrayUtils.removeElement([], 1)        = []
	 * ArrayUtils.removeElement([1], 2)       = [1]
	 * ArrayUtils.removeElement([1, 3], 1)    = [3]
	 * ArrayUtils.removeElement([1, 3, 1], 1) = [3, 1]
	 * </pre>
	 * @param array the array to remove the element from, may be {@code null}
	 * @param element the element to be removed
	 * @return A new array containing the existing elements except the first occurrence of
	 * the specified element.
	 * @since 2.1
	 */
	public static int[] removeElement(int[] array, int element) {
		int index = indexOf(array, element);
		if (index == INDEX_NOT_FOUND) {
			return clone(array);
		}
		return remove(array, index);
	}

	/**
	 * Removes the first occurrence of the specified element from the specified array. All
	 * subsequent elements are shifted to the left (subtracts one from their indices). If
	 * the array doesn't contains such an element, no elements are removed from the array.
	 *
	 * <p>
	 * This method returns a new array with the same elements of the input array except
	 * the first occurrence of the specified element. The component type of the returned
	 * array is always the same as that of the input array.
	 *
	 * <pre>
	 * ArrayUtils.removeElement(null, 1)      = null
	 * ArrayUtils.removeElement([], 1)        = []
	 * ArrayUtils.removeElement([1], 2)       = [1]
	 * ArrayUtils.removeElement([1, 3], 1)    = [3]
	 * ArrayUtils.removeElement([1, 3, 1], 1) = [3, 1]
	 * </pre>
	 * @param array the array to remove the element from, may be {@code null}
	 * @param element the element to be removed
	 * @return A new array containing the existing elements except the first occurrence of
	 * the specified element.
	 * @since 2.1
	 */
	public static long[] removeElement(long[] array, long element) {
		int index = indexOf(array, element);
		if (index == INDEX_NOT_FOUND) {
			return clone(array);
		}
		return remove(array, index);
	}

	/**
	 * Removes the first occurrence of the specified element from the specified array. All
	 * subsequent elements are shifted to the left (subtracts one from their indices). If
	 * the array doesn't contains such an element, no elements are removed from the array.
	 *
	 * <p>
	 * This method returns a new array with the same elements of the input array except
	 * the first occurrence of the specified element. The component type of the returned
	 * array is always the same as that of the input array.
	 *
	 * <pre>
	 * ArrayUtils.removeElement(null, 1)      = null
	 * ArrayUtils.removeElement([], 1)        = []
	 * ArrayUtils.removeElement([1], 2)       = [1]
	 * ArrayUtils.removeElement([1, 3], 1)    = [3]
	 * ArrayUtils.removeElement([1, 3, 1], 1) = [3, 1]
	 * </pre>
	 * @param array the array to remove the element from, may be {@code null}
	 * @param element the element to be removed
	 * @return A new array containing the existing elements except the first occurrence of
	 * the specified element.
	 * @since 2.1
	 */
	public static short[] removeElement(short[] array, short element) {
		int index = indexOf(array, element);
		if (index == INDEX_NOT_FOUND) {
			return clone(array);
		}
		return remove(array, index);
	}

	/**
	 * Removes the first occurrence of the specified element from the specified array. All
	 * subsequent elements are shifted to the left (subtracts one from their indices). If
	 * the array doesn't contains such an element, no elements are removed from the array.
	 *
	 * <p>
	 * This method returns a new array with the same elements of the input array except
	 * the first occurrence of the specified element. The component type of the returned
	 * array is always the same as that of the input array.
	 *
	 * <pre>
	 * ArrayUtils.removeElement(null, "a")            = null
	 * ArrayUtils.removeElement([], "a")              = []
	 * ArrayUtils.removeElement(["a"], "b")           = ["a"]
	 * ArrayUtils.removeElement(["a", "b"], "a")      = ["b"]
	 * ArrayUtils.removeElement(["a", "b", "a"], "a") = ["b", "a"]
	 * </pre>
	 * @param <T> the component type of the array
	 * @param array the array to remove the element from, may be {@code null}
	 * @param element the element to be removed
	 * @return A new array containing the existing elements except the first occurrence of
	 * the specified element.
	 * @since 2.1
	 */
	public static <T> T[] removeElement(T[] array, Object element) {
		int index = indexOf(array, element);
		if (index == INDEX_NOT_FOUND) {
			return clone(array);
		}
		return remove(array, index);
	}

	/**
	 * Reverses the order of the given array.
	 *
	 * <p>
	 * This method does nothing for a {@code null} input array.
	 * @param array the array to reverse, may be {@code null}
	 */
	public static void reverse(boolean[] array) {
		if (array == null) {
			return;
		}
		reverse(array, 0, array.length);
	}

	/**
	 * Reverses the order of the given array in the given range.
	 *
	 * <p>
	 * This method does nothing for a {@code null} input array.
	 * @param array the array to reverse, may be {@code null}
	 * @param startIndexInclusive the starting index. Undervalue (&lt;0) is promoted to 0,
	 * overvalue (&gt;array.length) results in no change.
	 * @param endIndexExclusive elements up to endIndex-1 are reversed in the array.
	 * Undervalue (&lt; start index) results in no change. Overvalue (&gt;array.length) is
	 * demoted to array length.
	 * @since 3.2
	 */
	public static void reverse(boolean[] array, int startIndexInclusive, int endIndexExclusive) {
		if (array == null) {
			return;
		}
		int i = startIndexInclusive < 0 ? 0 : startIndexInclusive;
		int j = Math.min(array.length, endIndexExclusive) - 1;
		boolean tmp;
		while (j > i) {
			tmp = array[j];
			array[j] = array[i];
			array[i] = tmp;
			j--;
			i++;
		}
	}

	/**
	 * Reverses the order of the given array.
	 *
	 * <p>
	 * This method does nothing for a {@code null} input array.
	 * @param array the array to reverse, may be {@code null}
	 */
	public static void reverse(byte[] array) {
		if (array == null) {
			return;
		}
		reverse(array, 0, array.length);
	}

	/**
	 * Reverses the order of the given array in the given range.
	 *
	 * <p>
	 * This method does nothing for a {@code null} input array.
	 * @param array the array to reverse, may be {@code null}
	 * @param startIndexInclusive the starting index. Undervalue (&lt;0) is promoted to 0,
	 * overvalue (&gt;array.length) results in no change.
	 * @param endIndexExclusive elements up to endIndex-1 are reversed in the array.
	 * Undervalue (&lt; start index) results in no change. Overvalue (&gt;array.length) is
	 * demoted to array length.
	 * @since 3.2
	 */
	public static void reverse(byte[] array, int startIndexInclusive, int endIndexExclusive) {
		if (array == null) {
			return;
		}
		int i = startIndexInclusive < 0 ? 0 : startIndexInclusive;
		int j = Math.min(array.length, endIndexExclusive) - 1;
		byte tmp;
		while (j > i) {
			tmp = array[j];
			array[j] = array[i];
			array[i] = tmp;
			j--;
			i++;
		}
	}

	/**
	 * Reverses the order of the given array.
	 *
	 * <p>
	 * This method does nothing for a {@code null} input array.
	 * @param array the array to reverse, may be {@code null}
	 */
	public static void reverse(char[] array) {
		if (array == null) {
			return;
		}
		reverse(array, 0, array.length);
	}

	/**
	 * Reverses the order of the given array in the given range.
	 *
	 * <p>
	 * This method does nothing for a {@code null} input array.
	 * @param array the array to reverse, may be {@code null}
	 * @param startIndexInclusive the starting index. Undervalue (&lt;0) is promoted to 0,
	 * overvalue (&gt;array.length) results in no change.
	 * @param endIndexExclusive elements up to endIndex-1 are reversed in the array.
	 * Undervalue (&lt; start index) results in no change. Overvalue (&gt;array.length) is
	 * demoted to array length.
	 * @since 3.2
	 */
	public static void reverse(char[] array, int startIndexInclusive, int endIndexExclusive) {
		if (array == null) {
			return;
		}
		int i = startIndexInclusive < 0 ? 0 : startIndexInclusive;
		int j = Math.min(array.length, endIndexExclusive) - 1;
		char tmp;
		while (j > i) {
			tmp = array[j];
			array[j] = array[i];
			array[i] = tmp;
			j--;
			i++;
		}
	}

	/**
	 * Reverses the order of the given array.
	 *
	 * <p>
	 * This method does nothing for a {@code null} input array.
	 * @param array the array to reverse, may be {@code null}
	 */
	public static void reverse(double[] array) {
		if (array == null) {
			return;
		}
		reverse(array, 0, array.length);
	}

	/**
	 * Reverses the order of the given array in the given range.
	 *
	 * <p>
	 * This method does nothing for a {@code null} input array.
	 * @param array the array to reverse, may be {@code null}
	 * @param startIndexInclusive the starting index. Undervalue (&lt;0) is promoted to 0,
	 * overvalue (&gt;array.length) results in no change.
	 * @param endIndexExclusive elements up to endIndex-1 are reversed in the array.
	 * Undervalue (&lt; start index) results in no change. Overvalue (&gt;array.length) is
	 * demoted to array length.
	 * @since 3.2
	 */
	public static void reverse(double[] array, int startIndexInclusive, int endIndexExclusive) {
		if (array == null) {
			return;
		}
		int i = startIndexInclusive < 0 ? 0 : startIndexInclusive;
		int j = Math.min(array.length, endIndexExclusive) - 1;
		double tmp;
		while (j > i) {
			tmp = array[j];
			array[j] = array[i];
			array[i] = tmp;
			j--;
			i++;
		}
	}

	/**
	 * Reverses the order of the given array.
	 *
	 * <p>
	 * This method does nothing for a {@code null} input array.
	 * @param array the array to reverse, may be {@code null}
	 */
	public static void reverse(float[] array) {
		if (array == null) {
			return;
		}
		reverse(array, 0, array.length);
	}

	/**
	 * Reverses the order of the given array in the given range.
	 *
	 * <p>
	 * This method does nothing for a {@code null} input array.
	 * @param array the array to reverse, may be {@code null}
	 * @param startIndexInclusive the starting index. Undervalue (&lt;0) is promoted to 0,
	 * overvalue (&gt;array.length) results in no change.
	 * @param endIndexExclusive elements up to endIndex-1 are reversed in the array.
	 * Undervalue (&lt; start index) results in no change. Overvalue (&gt;array.length) is
	 * demoted to array length.
	 * @since 3.2
	 */
	public static void reverse(float[] array, int startIndexInclusive, int endIndexExclusive) {
		if (array == null) {
			return;
		}
		int i = startIndexInclusive < 0 ? 0 : startIndexInclusive;
		int j = Math.min(array.length, endIndexExclusive) - 1;
		float tmp;
		while (j > i) {
			tmp = array[j];
			array[j] = array[i];
			array[i] = tmp;
			j--;
			i++;
		}
	}

	/**
	 * Reverses the order of the given array.
	 *
	 * <p>
	 * This method does nothing for a {@code null} input array.
	 * @param array the array to reverse, may be {@code null}
	 */
	public static void reverse(int[] array) {
		if (array == null) {
			return;
		}
		reverse(array, 0, array.length);
	}

	/**
	 * Reverses the order of the given array in the given range.
	 *
	 * <p>
	 * This method does nothing for a {@code null} input array.
	 * @param array the array to reverse, may be {@code null}
	 * @param startIndexInclusive the starting index. Undervalue (&lt;0) is promoted to 0,
	 * overvalue (&gt;array.length) results in no change.
	 * @param endIndexExclusive elements up to endIndex-1 are reversed in the array.
	 * Undervalue (&lt; start index) results in no change. Overvalue (&gt;array.length) is
	 * demoted to array length.
	 * @since 3.2
	 */
	public static void reverse(int[] array, int startIndexInclusive, int endIndexExclusive) {
		if (array == null) {
			return;
		}
		int i = startIndexInclusive < 0 ? 0 : startIndexInclusive;
		int j = Math.min(array.length, endIndexExclusive) - 1;
		int tmp;
		while (j > i) {
			tmp = array[j];
			array[j] = array[i];
			array[i] = tmp;
			j--;
			i++;
		}
	}

	/**
	 * Reverses the order of the given array.
	 *
	 * <p>
	 * This method does nothing for a {@code null} input array.
	 * @param array the array to reverse, may be {@code null}
	 */
	public static void reverse(long[] array) {
		if (array == null) {
			return;
		}
		reverse(array, 0, array.length);
	}

	/**
	 * Reverses the order of the given array in the given range.
	 *
	 * <p>
	 * This method does nothing for a {@code null} input array.
	 * @param array the array to reverse, may be {@code null}
	 * @param startIndexInclusive the starting index. Undervalue (&lt;0) is promoted to 0,
	 * overvalue (&gt;array.length) results in no change.
	 * @param endIndexExclusive elements up to endIndex-1 are reversed in the array.
	 * Undervalue (&lt; start index) results in no change. Overvalue (&gt;array.length) is
	 * demoted to array length.
	 * @since 3.2
	 */
	public static void reverse(long[] array, int startIndexInclusive, int endIndexExclusive) {
		if (array == null) {
			return;
		}
		int i = startIndexInclusive < 0 ? 0 : startIndexInclusive;
		int j = Math.min(array.length, endIndexExclusive) - 1;
		long tmp;
		while (j > i) {
			tmp = array[j];
			array[j] = array[i];
			array[i] = tmp;
			j--;
			i++;
		}
	}

	// Reverse
	// -----------------------------------------------------------------------
	/**
	 * Reverses the order of the given array.
	 *
	 * <p>
	 * There is no special handling for multi-dimensional arrays.
	 *
	 * <p>
	 * This method does nothing for a {@code null} input array.
	 * @param array the array to reverse, may be {@code null}
	 */
	public static void reverse(Object[] array) {
		if (array == null) {
			return;
		}
		reverse(array, 0, array.length);
	}

	/**
	 * Reverses the order of the given array in the given range.
	 *
	 * <p>
	 * This method does nothing for a {@code null} input array.
	 * @param array the array to reverse, may be {@code null}
	 * @param startIndexInclusive the starting index. Under value (&lt;0) is promoted to
	 * 0, over value (&gt;array.length) results in no change.
	 * @param endIndexExclusive elements up to endIndex-1 are reversed in the array. Under
	 * value (&lt; start index) results in no change. Over value (&gt;array.length) is
	 * demoted to array length.
	 * @since 3.2
	 */
	public static void reverse(Object[] array, int startIndexInclusive, int endIndexExclusive) {
		if (array == null) {
			return;
		}
		int i = startIndexInclusive < 0 ? 0 : startIndexInclusive;
		int j = Math.min(array.length, endIndexExclusive) - 1;
		Object tmp;
		while (j > i) {
			tmp = array[j];
			array[j] = array[i];
			array[i] = tmp;
			j--;
			i++;
		}
	}

	/**
	 * Reverses the order of the given array.
	 *
	 * <p>
	 * This method does nothing for a {@code null} input array.
	 * @param array the array to reverse, may be {@code null}
	 */
	public static void reverse(short[] array) {
		if (array == null) {
			return;
		}
		reverse(array, 0, array.length);
	}

	/**
	 * Reverses the order of the given array in the given range.
	 *
	 * <p>
	 * This method does nothing for a {@code null} input array.
	 * @param array the array to reverse, may be {@code null}
	 * @param startIndexInclusive the starting index. Undervalue (&lt;0) is promoted to 0,
	 * overvalue (&gt;array.length) results in no change.
	 * @param endIndexExclusive elements up to endIndex-1 are reversed in the array.
	 * Undervalue (&lt; start index) results in no change. Overvalue (&gt;array.length) is
	 * demoted to array length.
	 * @since 3.2
	 */
	public static void reverse(short[] array, int startIndexInclusive, int endIndexExclusive) {
		if (array == null) {
			return;
		}
		int i = startIndexInclusive < 0 ? 0 : startIndexInclusive;
		int j = Math.min(array.length, endIndexExclusive) - 1;
		short tmp;
		while (j > i) {
			tmp = array[j];
			array[j] = array[i];
			array[i] = tmp;
			j--;
			i++;
		}
	}

	/**
	 * Shifts the order of the given boolean array.
	 *
	 * <p>
	 * There is no special handling for multi-dimensional arrays. This method does nothing
	 * for {@code null} or empty input arrays.
	 * @param array the array to shift, may be {@code null}
	 * @param offset The number of positions to rotate the elements. If the offset is
	 * larger than the number of elements to rotate, than the effective offset is modulo
	 * the number of elements to rotate.
	 * @since 3.5
	 */
	public static void shift(boolean[] array, int offset) {
		if (array == null) {
			return;
		}
		shift(array, 0, array.length, offset);
	}

	/**
	 * Shifts the order of a series of elements in the given boolean array.
	 *
	 * <p>
	 * There is no special handling for multi-dimensional arrays. This method does nothing
	 * for {@code null} or empty input arrays.
	 * @param array the array to shift, may be {@code null}
	 * @param startIndexInclusive the starting index. Undervalue (&lt;0) is promoted to 0,
	 * overvalue (&gt;array.length) results in no change.
	 * @param endIndexExclusive elements up to endIndex-1 are shifted in the array.
	 * Undervalue (&lt; start index) results in no change. Overvalue (&gt;array.length) is
	 * demoted to array length.
	 * @param offset The number of positions to rotate the elements. If the offset is
	 * larger than the number of elements to rotate, than the effective offset is modulo
	 * the number of elements to rotate.
	 * @since 3.5
	 */
	public static void shift(boolean[] array, int startIndexInclusive, int endIndexExclusive, int offset) {
		if (array == null) {
			return;
		}
		if (startIndexInclusive >= array.length - 1 || endIndexExclusive <= 0) {
			return;
		}
		if (startIndexInclusive < 0) {
			startIndexInclusive = 0;
		}
		if (endIndexExclusive >= array.length) {
			endIndexExclusive = array.length;
		}
		int n = endIndexExclusive - startIndexInclusive;
		if (n <= 1) {
			return;
		}
		offset %= n;
		if (offset < 0) {
			offset += n;
		}
		// For algorithm explanations and proof of O(n) time complexity and O(1) space
		// complexity
		// see https://beradrian.wordpress.com/2015/04/07/shift-an-array-in-on-in-place/
		while (n > 1 && offset > 0) {
			int n_offset = n - offset;

			if (offset > n_offset) {
				swap(array, startIndexInclusive, startIndexInclusive + n - n_offset, n_offset);
				n = offset;
				offset -= n_offset;
			}
			else if (offset < n_offset) {
				swap(array, startIndexInclusive, startIndexInclusive + n_offset, offset);
				startIndexInclusive += offset;
				n = n_offset;
			}
			else {
				swap(array, startIndexInclusive, startIndexInclusive + n_offset, offset);
				break;
			}
		}
	}

	/**
	 * Shifts the order of the given byte array.
	 *
	 * <p>
	 * There is no special handling for multi-dimensional arrays. This method does nothing
	 * for {@code null} or empty input arrays.
	 * @param array the array to shift, may be {@code null}
	 * @param offset The number of positions to rotate the elements. If the offset is
	 * larger than the number of elements to rotate, than the effective offset is modulo
	 * the number of elements to rotate.
	 * @since 3.5
	 */
	public static void shift(byte[] array, int offset) {
		if (array == null) {
			return;
		}
		shift(array, 0, array.length, offset);
	}

	/**
	 * Shifts the order of a series of elements in the given byte array.
	 *
	 * <p>
	 * There is no special handling for multi-dimensional arrays. This method does nothing
	 * for {@code null} or empty input arrays.
	 * @param array the array to shift, may be {@code null}
	 * @param startIndexInclusive the starting index. Undervalue (&lt;0) is promoted to 0,
	 * overvalue (&gt;array.length) results in no change.
	 * @param endIndexExclusive elements up to endIndex-1 are shifted in the array.
	 * Undervalue (&lt; start index) results in no change. Overvalue (&gt;array.length) is
	 * demoted to array length.
	 * @param offset The number of positions to rotate the elements. If the offset is
	 * larger than the number of elements to rotate, than the effective offset is modulo
	 * the number of elements to rotate.
	 * @since 3.5
	 */
	public static void shift(byte[] array, int startIndexInclusive, int endIndexExclusive, int offset) {
		if (array == null) {
			return;
		}
		if (startIndexInclusive >= array.length - 1 || endIndexExclusive <= 0) {
			return;
		}
		if (startIndexInclusive < 0) {
			startIndexInclusive = 0;
		}
		if (endIndexExclusive >= array.length) {
			endIndexExclusive = array.length;
		}
		int n = endIndexExclusive - startIndexInclusive;
		if (n <= 1) {
			return;
		}
		offset %= n;
		if (offset < 0) {
			offset += n;
		}
		// For algorithm explanations and proof of O(n) time complexity and O(1) space
		// complexity
		// see https://beradrian.wordpress.com/2015/04/07/shift-an-array-in-on-in-place/
		while (n > 1 && offset > 0) {
			int n_offset = n - offset;

			if (offset > n_offset) {
				swap(array, startIndexInclusive, startIndexInclusive + n - n_offset, n_offset);
				n = offset;
				offset -= n_offset;
			}
			else if (offset < n_offset) {
				swap(array, startIndexInclusive, startIndexInclusive + n_offset, offset);
				startIndexInclusive += offset;
				n = n_offset;
			}
			else {
				swap(array, startIndexInclusive, startIndexInclusive + n_offset, offset);
				break;
			}
		}
	}

	/**
	 * Shifts the order of the given char array.
	 *
	 * <p>
	 * There is no special handling for multi-dimensional arrays. This method does nothing
	 * for {@code null} or empty input arrays.
	 * @param array the array to shift, may be {@code null}
	 * @param offset The number of positions to rotate the elements. If the offset is
	 * larger than the number of elements to rotate, than the effective offset is modulo
	 * the number of elements to rotate.
	 * @since 3.5
	 */
	public static void shift(char[] array, int offset) {
		if (array == null) {
			return;
		}
		shift(array, 0, array.length, offset);
	}

	/**
	 * Shifts the order of a series of elements in the given char array.
	 *
	 * <p>
	 * There is no special handling for multi-dimensional arrays. This method does nothing
	 * for {@code null} or empty input arrays.
	 * @param array the array to shift, may be {@code null}
	 * @param startIndexInclusive the starting index. Undervalue (&lt;0) is promoted to 0,
	 * overvalue (&gt;array.length) results in no change.
	 * @param endIndexExclusive elements up to endIndex-1 are shifted in the array.
	 * Undervalue (&lt; start index) results in no change. Overvalue (&gt;array.length) is
	 * demoted to array length.
	 * @param offset The number of positions to rotate the elements. If the offset is
	 * larger than the number of elements to rotate, than the effective offset is modulo
	 * the number of elements to rotate.
	 * @since 3.5
	 */
	public static void shift(char[] array, int startIndexInclusive, int endIndexExclusive, int offset) {
		if (array == null) {
			return;
		}
		if (startIndexInclusive >= array.length - 1 || endIndexExclusive <= 0) {
			return;
		}
		if (startIndexInclusive < 0) {
			startIndexInclusive = 0;
		}
		if (endIndexExclusive >= array.length) {
			endIndexExclusive = array.length;
		}
		int n = endIndexExclusive - startIndexInclusive;
		if (n <= 1) {
			return;
		}
		offset %= n;
		if (offset < 0) {
			offset += n;
		}
		// For algorithm explanations and proof of O(n) time complexity and O(1) space
		// complexity
		// see https://beradrian.wordpress.com/2015/04/07/shift-an-array-in-on-in-place/
		while (n > 1 && offset > 0) {
			int n_offset = n - offset;

			if (offset > n_offset) {
				swap(array, startIndexInclusive, startIndexInclusive + n - n_offset, n_offset);
				n = offset;
				offset -= n_offset;
			}
			else if (offset < n_offset) {
				swap(array, startIndexInclusive, startIndexInclusive + n_offset, offset);
				startIndexInclusive += offset;
				n = n_offset;
			}
			else {
				swap(array, startIndexInclusive, startIndexInclusive + n_offset, offset);
				break;
			}
		}
	}

	/**
	 * Shifts the order of the given double array.
	 *
	 * <p>
	 * There is no special handling for multi-dimensional arrays. This method does nothing
	 * for {@code null} or empty input arrays.
	 * @param array the array to shift, may be {@code null}
	 * @param offset The number of positions to rotate the elements. If the offset is
	 * larger than the number of elements to rotate, than the effective offset is modulo
	 * the number of elements to rotate.
	 * @since 3.5
	 */
	public static void shift(double[] array, int offset) {
		if (array == null) {
			return;
		}
		shift(array, 0, array.length, offset);
	}

	/**
	 * Shifts the order of a series of elements in the given double array.
	 *
	 * <p>
	 * There is no special handling for multi-dimensional arrays. This method does nothing
	 * for {@code null} or empty input arrays.
	 * @param array the array to shift, may be {@code null}
	 * @param startIndexInclusive the starting index. Undervalue (&lt;0) is promoted to 0,
	 * overvalue (&gt;array.length) results in no change.
	 * @param endIndexExclusive elements up to endIndex-1 are shifted in the array.
	 * Undervalue (&lt; start index) results in no change. Overvalue (&gt;array.length) is
	 * demoted to array length.
	 * @param offset The number of positions to rotate the elements. If the offset is
	 * larger than the number of elements to rotate, than the effective offset is modulo
	 * the number of elements to rotate.
	 * @since 3.5
	 */
	public static void shift(double[] array, int startIndexInclusive, int endIndexExclusive, int offset) {
		if (array == null) {
			return;
		}
		if (startIndexInclusive >= array.length - 1 || endIndexExclusive <= 0) {
			return;
		}
		if (startIndexInclusive < 0) {
			startIndexInclusive = 0;
		}
		if (endIndexExclusive >= array.length) {
			endIndexExclusive = array.length;
		}
		int n = endIndexExclusive - startIndexInclusive;
		if (n <= 1) {
			return;
		}
		offset %= n;
		if (offset < 0) {
			offset += n;
		}
		// For algorithm explanations and proof of O(n) time complexity and O(1) space
		// complexity
		// see https://beradrian.wordpress.com/2015/04/07/shift-an-array-in-on-in-place/
		while (n > 1 && offset > 0) {
			int n_offset = n - offset;

			if (offset > n_offset) {
				swap(array, startIndexInclusive, startIndexInclusive + n - n_offset, n_offset);
				n = offset;
				offset -= n_offset;
			}
			else if (offset < n_offset) {
				swap(array, startIndexInclusive, startIndexInclusive + n_offset, offset);
				startIndexInclusive += offset;
				n = n_offset;
			}
			else {
				swap(array, startIndexInclusive, startIndexInclusive + n_offset, offset);
				break;
			}
		}
	}

	/**
	 * Shifts the order of the given float array.
	 *
	 * <p>
	 * There is no special handling for multi-dimensional arrays. This method does nothing
	 * for {@code null} or empty input arrays.
	 * @param array the array to shift, may be {@code null}
	 * @param offset The number of positions to rotate the elements. If the offset is
	 * larger than the number of elements to rotate, than the effective offset is modulo
	 * the number of elements to rotate.
	 * @since 3.5
	 */
	public static void shift(float[] array, int offset) {
		if (array == null) {
			return;
		}
		shift(array, 0, array.length, offset);
	}

	/**
	 * Shifts the order of a series of elements in the given float array.
	 *
	 * <p>
	 * There is no special handling for multi-dimensional arrays. This method does nothing
	 * for {@code null} or empty input arrays.
	 * @param array the array to shift, may be {@code null}
	 * @param startIndexInclusive the starting index. Undervalue (&lt;0) is promoted to 0,
	 * overvalue (&gt;array.length) results in no change.
	 * @param endIndexExclusive elements up to endIndex-1 are shifted in the array.
	 * Undervalue (&lt; start index) results in no change. Overvalue (&gt;array.length) is
	 * demoted to array length.
	 * @param offset The number of positions to rotate the elements. If the offset is
	 * larger than the number of elements to rotate, than the effective offset is modulo
	 * the number of elements to rotate.
	 * @since 3.5
	 */
	public static void shift(float[] array, int startIndexInclusive, int endIndexExclusive, int offset) {
		if (array == null) {
			return;
		}
		if (startIndexInclusive >= array.length - 1 || endIndexExclusive <= 0) {
			return;
		}
		if (startIndexInclusive < 0) {
			startIndexInclusive = 0;
		}
		if (endIndexExclusive >= array.length) {
			endIndexExclusive = array.length;
		}
		int n = endIndexExclusive - startIndexInclusive;
		if (n <= 1) {
			return;
		}
		offset %= n;
		if (offset < 0) {
			offset += n;
		}
		// For algorithm explanations and proof of O(n) time complexity and O(1) space
		// complexity
		// see https://beradrian.wordpress.com/2015/04/07/shift-an-array-in-on-in-place/
		while (n > 1 && offset > 0) {
			int n_offset = n - offset;

			if (offset > n_offset) {
				swap(array, startIndexInclusive, startIndexInclusive + n - n_offset, n_offset);
				n = offset;
				offset -= n_offset;
			}
			else if (offset < n_offset) {
				swap(array, startIndexInclusive, startIndexInclusive + n_offset, offset);
				startIndexInclusive += offset;
				n = n_offset;
			}
			else {
				swap(array, startIndexInclusive, startIndexInclusive + n_offset, offset);
				break;
			}
		}
	}

	/**
	 * Shifts the order of the given int array.
	 *
	 * <p>
	 * There is no special handling for multi-dimensional arrays. This method does nothing
	 * for {@code null} or empty input arrays.
	 * @param array the array to shift, may be {@code null}
	 * @param offset The number of positions to rotate the elements. If the offset is
	 * larger than the number of elements to rotate, than the effective offset is modulo
	 * the number of elements to rotate.
	 * @since 3.5
	 */
	public static void shift(int[] array, int offset) {
		if (array == null) {
			return;
		}
		shift(array, 0, array.length, offset);
	}

	/**
	 * Shifts the order of a series of elements in the given int array.
	 *
	 * <p>
	 * There is no special handling for multi-dimensional arrays. This method does nothing
	 * for {@code null} or empty input arrays.
	 * @param array the array to shift, may be {@code null}
	 * @param startIndexInclusive the starting index. Undervalue (&lt;0) is promoted to 0,
	 * overvalue (&gt;array.length) results in no change.
	 * @param endIndexExclusive elements up to endIndex-1 are shifted in the array.
	 * Undervalue (&lt; start index) results in no change. Overvalue (&gt;array.length) is
	 * demoted to array length.
	 * @param offset The number of positions to rotate the elements. If the offset is
	 * larger than the number of elements to rotate, than the effective offset is modulo
	 * the number of elements to rotate.
	 * @since 3.5
	 */
	public static void shift(int[] array, int startIndexInclusive, int endIndexExclusive, int offset) {
		if (array == null) {
			return;
		}
		if (startIndexInclusive >= array.length - 1 || endIndexExclusive <= 0) {
			return;
		}
		if (startIndexInclusive < 0) {
			startIndexInclusive = 0;
		}
		if (endIndexExclusive >= array.length) {
			endIndexExclusive = array.length;
		}
		int n = endIndexExclusive - startIndexInclusive;
		if (n <= 1) {
			return;
		}
		offset %= n;
		if (offset < 0) {
			offset += n;
		}
		// For algorithm explanations and proof of O(n) time complexity and O(1) space
		// complexity
		// see https://beradrian.wordpress.com/2015/04/07/shift-an-array-in-on-in-place/
		while (n > 1 && offset > 0) {
			int n_offset = n - offset;

			if (offset > n_offset) {
				swap(array, startIndexInclusive, startIndexInclusive + n - n_offset, n_offset);
				n = offset;
				offset -= n_offset;
			}
			else if (offset < n_offset) {
				swap(array, startIndexInclusive, startIndexInclusive + n_offset, offset);
				startIndexInclusive += offset;
				n = n_offset;
			}
			else {
				swap(array, startIndexInclusive, startIndexInclusive + n_offset, offset);
				break;
			}
		}
	}

	/**
	 * Shifts the order of the given long array.
	 *
	 * <p>
	 * There is no special handling for multi-dimensional arrays. This method does nothing
	 * for {@code null} or empty input arrays.
	 * @param array the array to shift, may be {@code null}
	 * @param offset The number of positions to rotate the elements. If the offset is
	 * larger than the number of elements to rotate, than the effective offset is modulo
	 * the number of elements to rotate.
	 * @since 3.5
	 */
	public static void shift(long[] array, int offset) {
		if (array == null) {
			return;
		}
		shift(array, 0, array.length, offset);
	}

	/**
	 * Shifts the order of a series of elements in the given long array.
	 *
	 * <p>
	 * There is no special handling for multi-dimensional arrays. This method does nothing
	 * for {@code null} or empty input arrays.
	 * @param array the array to shift, may be {@code null}
	 * @param startIndexInclusive the starting index. Undervalue (&lt;0) is promoted to 0,
	 * overvalue (&gt;array.length) results in no change.
	 * @param endIndexExclusive elements up to endIndex-1 are shifted in the array.
	 * Undervalue (&lt; start index) results in no change. Overvalue (&gt;array.length) is
	 * demoted to array length.
	 * @param offset The number of positions to rotate the elements. If the offset is
	 * larger than the number of elements to rotate, than the effective offset is modulo
	 * the number of elements to rotate.
	 * @since 3.5
	 */
	public static void shift(long[] array, int startIndexInclusive, int endIndexExclusive, int offset) {
		if (array == null) {
			return;
		}
		if (startIndexInclusive >= array.length - 1 || endIndexExclusive <= 0) {
			return;
		}
		if (startIndexInclusive < 0) {
			startIndexInclusive = 0;
		}
		if (endIndexExclusive >= array.length) {
			endIndexExclusive = array.length;
		}
		int n = endIndexExclusive - startIndexInclusive;
		if (n <= 1) {
			return;
		}
		offset %= n;
		if (offset < 0) {
			offset += n;
		}
		// For algorithm explanations and proof of O(n) time complexity and O(1) space
		// complexity
		// see https://beradrian.wordpress.com/2015/04/07/shift-an-array-in-on-in-place/
		while (n > 1 && offset > 0) {
			int n_offset = n - offset;

			if (offset > n_offset) {
				swap(array, startIndexInclusive, startIndexInclusive + n - n_offset, n_offset);
				n = offset;
				offset -= n_offset;
			}
			else if (offset < n_offset) {
				swap(array, startIndexInclusive, startIndexInclusive + n_offset, offset);
				startIndexInclusive += offset;
				n = n_offset;
			}
			else {
				swap(array, startIndexInclusive, startIndexInclusive + n_offset, offset);
				break;
			}
		}
	}

	// Shift
	// -----------------------------------------------------------------------
	/**
	 * Shifts the order of the given array.
	 *
	 * <p>
	 * There is no special handling for multi-dimensional arrays. This method does nothing
	 * for {@code null} or empty input arrays.
	 * @param array the array to shift, may be {@code null}
	 * @param offset The number of positions to rotate the elements. If the offset is
	 * larger than the number of elements to rotate, than the effective offset is modulo
	 * the number of elements to rotate.
	 * @since 3.5
	 */
	public static void shift(Object[] array, int offset) {
		if (array == null) {
			return;
		}
		shift(array, 0, array.length, offset);
	}

	/**
	 * Shifts the order of a series of elements in the given array.
	 *
	 * <p>
	 * There is no special handling for multi-dimensional arrays. This method does nothing
	 * for {@code null} or empty input arrays.
	 * @param array the array to shift, may be {@code null}
	 * @param startIndexInclusive the starting index. Undervalue (&lt;0) is promoted to 0,
	 * overvalue (&gt;array.length) results in no change.
	 * @param endIndexExclusive elements up to endIndex-1 are shifted in the array.
	 * Undervalue (&lt; start index) results in no change. Overvalue (&gt;array.length) is
	 * demoted to array length.
	 * @param offset The number of positions to rotate the elements. If the offset is
	 * larger than the number of elements to rotate, than the effective offset is modulo
	 * the number of elements to rotate.
	 * @since 3.5
	 */
	public static void shift(Object[] array, int startIndexInclusive, int endIndexExclusive, int offset) {
		if (array == null) {
			return;
		}
		if (startIndexInclusive >= array.length - 1 || endIndexExclusive <= 0) {
			return;
		}
		if (startIndexInclusive < 0) {
			startIndexInclusive = 0;
		}
		if (endIndexExclusive >= array.length) {
			endIndexExclusive = array.length;
		}
		int n = endIndexExclusive - startIndexInclusive;
		if (n <= 1) {
			return;
		}
		offset %= n;
		if (offset < 0) {
			offset += n;
		}
		// For algorithm explanations and proof of O(n) time complexity and O(1) space
		// complexity
		// see https://beradrian.wordpress.com/2015/04/07/shift-an-array-in-on-in-place/
		while (n > 1 && offset > 0) {
			int n_offset = n - offset;

			if (offset > n_offset) {
				swap(array, startIndexInclusive, startIndexInclusive + n - n_offset, n_offset);
				n = offset;
				offset -= n_offset;
			}
			else if (offset < n_offset) {
				swap(array, startIndexInclusive, startIndexInclusive + n_offset, offset);
				startIndexInclusive += offset;
				n = n_offset;
			}
			else {
				swap(array, startIndexInclusive, startIndexInclusive + n_offset, offset);
				break;
			}
		}
	}

	/**
	 * Shifts the order of the given short array.
	 *
	 * <p>
	 * There is no special handling for multi-dimensional arrays. This method does nothing
	 * for {@code null} or empty input arrays.
	 * @param array the array to shift, may be {@code null}
	 * @param offset The number of positions to rotate the elements. If the offset is
	 * larger than the number of elements to rotate, than the effective offset is modulo
	 * the number of elements to rotate.
	 * @since 3.5
	 */
	public static void shift(short[] array, int offset) {
		if (array == null) {
			return;
		}
		shift(array, 0, array.length, offset);
	}

	/**
	 * Shifts the order of a series of elements in the given short array.
	 *
	 * <p>
	 * There is no special handling for multi-dimensional arrays. This method does nothing
	 * for {@code null} or empty input arrays.
	 * @param array the array to shift, may be {@code null}
	 * @param startIndexInclusive the starting index. Undervalue (&lt;0) is promoted to 0,
	 * overvalue (&gt;array.length) results in no change.
	 * @param endIndexExclusive elements up to endIndex-1 are shifted in the array.
	 * Undervalue (&lt; start index) results in no change. Overvalue (&gt;array.length) is
	 * demoted to array length.
	 * @param offset The number of positions to rotate the elements. If the offset is
	 * larger than the number of elements to rotate, than the effective offset is modulo
	 * the number of elements to rotate.
	 * @since 3.5
	 */
	public static void shift(short[] array, int startIndexInclusive, int endIndexExclusive, int offset) {
		if (array == null) {
			return;
		}
		if (startIndexInclusive >= array.length - 1 || endIndexExclusive <= 0) {
			return;
		}
		if (startIndexInclusive < 0) {
			startIndexInclusive = 0;
		}
		if (endIndexExclusive >= array.length) {
			endIndexExclusive = array.length;
		}
		int n = endIndexExclusive - startIndexInclusive;
		if (n <= 1) {
			return;
		}
		offset %= n;
		if (offset < 0) {
			offset += n;
		}
		// For algorithm explanations and proof of O(n) time complexity and O(1) space
		// complexity
		// see https://beradrian.wordpress.com/2015/04/07/shift-an-array-in-on-in-place/
		while (n > 1 && offset > 0) {
			int n_offset = n - offset;

			if (offset > n_offset) {
				swap(array, startIndexInclusive, startIndexInclusive + n - n_offset, n_offset);
				n = offset;
				offset -= n_offset;
			}
			else if (offset < n_offset) {
				swap(array, startIndexInclusive, startIndexInclusive + n_offset, offset);
				startIndexInclusive += offset;
				n = n_offset;
			}
			else {
				swap(array, startIndexInclusive, startIndexInclusive + n_offset, offset);
				break;
			}
		}
	}

	/**
	 * Randomly permutes the elements of the specified array using the Fisher-Yates
	 * algorithm.
	 * @param array the array to shuffle
	 * @param random the source of randomness used to permute the elements
	 * @see <a href=
	 * "https://en.wikipedia.org/wiki/Fisher%E2%80%93Yates_shuffle">Fisher-Yates shuffle
	 * algorithm</a>
	 * @since 3.6
	 */
	public static void shuffle(boolean[] array, Random random) {
		for (int i = array.length; i > 1; i--) {
			swap(array, i - 1, random.nextInt(i), 1);
		}
	}

	/**
	 * Randomly permutes the elements of the specified array using the Fisher-Yates
	 * algorithm.
	 * @param array the array to shuffle
	 * @param random the source of randomness used to permute the elements
	 * @see <a href=
	 * "https://en.wikipedia.org/wiki/Fisher%E2%80%93Yates_shuffle">Fisher-Yates shuffle
	 * algorithm</a>
	 * @since 3.6
	 */
	public static void shuffle(byte[] array, Random random) {
		for (int i = array.length; i > 1; i--) {
			swap(array, i - 1, random.nextInt(i), 1);
		}
	}

	/**
	 * Randomly permutes the elements of the specified array using the Fisher-Yates
	 * algorithm.
	 * @param array the array to shuffle
	 * @param random the source of randomness used to permute the elements
	 * @see <a href=
	 * "https://en.wikipedia.org/wiki/Fisher%E2%80%93Yates_shuffle">Fisher-Yates shuffle
	 * algorithm</a>
	 * @since 3.6
	 */
	public static void shuffle(char[] array, Random random) {
		for (int i = array.length; i > 1; i--) {
			swap(array, i - 1, random.nextInt(i), 1);
		}
	}

	/**
	 * Randomly permutes the elements of the specified array using the Fisher-Yates
	 * algorithm.
	 * @param array the array to shuffle
	 * @param random the source of randomness used to permute the elements
	 * @see <a href=
	 * "https://en.wikipedia.org/wiki/Fisher%E2%80%93Yates_shuffle">Fisher-Yates shuffle
	 * algorithm</a>
	 * @since 3.6
	 */
	public static void shuffle(double[] array, Random random) {
		for (int i = array.length; i > 1; i--) {
			swap(array, i - 1, random.nextInt(i), 1);
		}
	}

	/**
	 * Randomly permutes the elements of the specified array using the Fisher-Yates
	 * algorithm.
	 * @param array the array to shuffle
	 * @param random the source of randomness used to permute the elements
	 * @see <a href=
	 * "https://en.wikipedia.org/wiki/Fisher%E2%80%93Yates_shuffle">Fisher-Yates shuffle
	 * algorithm</a>
	 * @since 3.6
	 */
	public static void shuffle(float[] array, Random random) {
		for (int i = array.length; i > 1; i--) {
			swap(array, i - 1, random.nextInt(i), 1);
		}
	}

	/**
	 * Randomly permutes the elements of the specified array using the Fisher-Yates
	 * algorithm.
	 * @param array the array to shuffle
	 * @param random the source of randomness used to permute the elements
	 * @see <a href=
	 * "https://en.wikipedia.org/wiki/Fisher%E2%80%93Yates_shuffle">Fisher-Yates shuffle
	 * algorithm</a>
	 * @since 3.6
	 */
	public static void shuffle(int[] array, Random random) {
		for (int i = array.length; i > 1; i--) {
			swap(array, i - 1, random.nextInt(i), 1);
		}
	}

	/**
	 * Randomly permutes the elements of the specified array using the Fisher-Yates
	 * algorithm.
	 * @param array the array to shuffle
	 * @param random the source of randomness used to permute the elements
	 * @see <a href=
	 * "https://en.wikipedia.org/wiki/Fisher%E2%80%93Yates_shuffle">Fisher-Yates shuffle
	 * algorithm</a>
	 * @since 3.6
	 */
	public static void shuffle(long[] array, Random random) {
		for (int i = array.length; i > 1; i--) {
			swap(array, i - 1, random.nextInt(i), 1);
		}
	}

	/**
	 * Randomly permutes the elements of the specified array using the Fisher-Yates
	 * algorithm.
	 * @param array the array to shuffle
	 * @param random the source of randomness used to permute the elements
	 * @see <a href=
	 * "https://en.wikipedia.org/wiki/Fisher%E2%80%93Yates_shuffle">Fisher-Yates shuffle
	 * algorithm</a>
	 * @since 3.6
	 */
	public static void shuffle(Object[] array, Random random) {
		for (int i = array.length; i > 1; i--) {
			swap(array, i - 1, random.nextInt(i), 1);
		}
	}

	/**
	 * Randomly permutes the elements of the specified array using the Fisher-Yates
	 * algorithm.
	 * @param array the array to shuffle
	 * @param random the source of randomness used to permute the elements
	 * @see <a href=
	 * "https://en.wikipedia.org/wiki/Fisher%E2%80%93Yates_shuffle">Fisher-Yates shuffle
	 * algorithm</a>
	 * @since 3.6
	 */
	public static void shuffle(short[] array, Random random) {
		for (int i = array.length; i > 1; i--) {
			swap(array, i - 1, random.nextInt(i), 1);
		}
	}

	/**
	 * Produces a new {@code boolean} array containing the elements between the start and
	 * end indices.
	 *
	 * <p>
	 * The start index is inclusive, the end index exclusive. Null array input produces
	 * null output.
	 * @param array the array
	 * @param startIndexInclusive the starting index. Undervalue (&lt;0) is promoted to 0,
	 * overvalue (&gt;array.length) results in an empty array.
	 * @param endIndexExclusive elements up to endIndex-1 are present in the returned
	 * subarray. Undervalue (&lt; startIndex) produces empty array, overvalue
	 * (&gt;array.length) is demoted to array length.
	 * @return a new array containing the elements between the start and end indices.
	 * @since 2.1
	 * @see Arrays#copyOfRange(boolean[], int, int)
	 */
	public static boolean[] subarray(boolean[] array, int startIndexInclusive, int endIndexExclusive) {
		if (array == null) {
			return null;
		}
		if (startIndexInclusive < 0) {
			startIndexInclusive = 0;
		}
		if (endIndexExclusive > array.length) {
			endIndexExclusive = array.length;
		}
		int newSize = endIndexExclusive - startIndexInclusive;
		if (newSize <= 0) {
			return EMPTY_BOOLEAN_ARRAY;
		}

		boolean[] subarray = new boolean[newSize];
		System.arraycopy(array, startIndexInclusive, subarray, 0, newSize);
		return subarray;
	}

	/**
	 * Produces a new {@code byte} array containing the elements between the start and end
	 * indices.
	 *
	 * <p>
	 * The start index is inclusive, the end index exclusive. Null array input produces
	 * null output.
	 * @param array the array
	 * @param startIndexInclusive the starting index. Undervalue (&lt;0) is promoted to 0,
	 * overvalue (&gt;array.length) results in an empty array.
	 * @param endIndexExclusive elements up to endIndex-1 are present in the returned
	 * subarray. Undervalue (&lt; startIndex) produces empty array, overvalue
	 * (&gt;array.length) is demoted to array length.
	 * @return a new array containing the elements between the start and end indices.
	 * @since 2.1
	 * @see Arrays#copyOfRange(byte[], int, int)
	 */
	public static byte[] subarray(byte[] array, int startIndexInclusive, int endIndexExclusive) {
		if (array == null) {
			return null;
		}
		if (startIndexInclusive < 0) {
			startIndexInclusive = 0;
		}
		if (endIndexExclusive > array.length) {
			endIndexExclusive = array.length;
		}
		int newSize = endIndexExclusive - startIndexInclusive;
		if (newSize <= 0) {
			return EMPTY_BYTE_ARRAY;
		}

		byte[] subarray = new byte[newSize];
		System.arraycopy(array, startIndexInclusive, subarray, 0, newSize);
		return subarray;
	}

	/**
	 * Produces a new {@code char} array containing the elements between the start and end
	 * indices.
	 *
	 * <p>
	 * The start index is inclusive, the end index exclusive. Null array input produces
	 * null output.
	 * @param array the array
	 * @param startIndexInclusive the starting index. Undervalue (&lt;0) is promoted to 0,
	 * overvalue (&gt;array.length) results in an empty array.
	 * @param endIndexExclusive elements up to endIndex-1 are present in the returned
	 * subarray. Undervalue (&lt; startIndex) produces empty array, overvalue
	 * (&gt;array.length) is demoted to array length.
	 * @return a new array containing the elements between the start and end indices.
	 * @since 2.1
	 * @see Arrays#copyOfRange(char[], int, int)
	 */
	public static char[] subarray(char[] array, int startIndexInclusive, int endIndexExclusive) {
		if (array == null) {
			return null;
		}
		if (startIndexInclusive < 0) {
			startIndexInclusive = 0;
		}
		if (endIndexExclusive > array.length) {
			endIndexExclusive = array.length;
		}
		int newSize = endIndexExclusive - startIndexInclusive;
		if (newSize <= 0) {
			return EMPTY_CHAR_ARRAY;
		}

		char[] subarray = new char[newSize];
		System.arraycopy(array, startIndexInclusive, subarray, 0, newSize);
		return subarray;
	}

	/**
	 * Produces a new {@code double} array containing the elements between the start and
	 * end indices.
	 *
	 * <p>
	 * The start index is inclusive, the end index exclusive. Null array input produces
	 * null output.
	 * @param array the array
	 * @param startIndexInclusive the starting index. Undervalue (&lt;0) is promoted to 0,
	 * overvalue (&gt;array.length) results in an empty array.
	 * @param endIndexExclusive elements up to endIndex-1 are present in the returned
	 * subarray. Undervalue (&lt; startIndex) produces empty array, overvalue
	 * (&gt;array.length) is demoted to array length.
	 * @return a new array containing the elements between the start and end indices.
	 * @since 2.1
	 * @see Arrays#copyOfRange(double[], int, int)
	 */
	public static double[] subarray(double[] array, int startIndexInclusive, int endIndexExclusive) {
		if (array == null) {
			return null;
		}
		if (startIndexInclusive < 0) {
			startIndexInclusive = 0;
		}
		if (endIndexExclusive > array.length) {
			endIndexExclusive = array.length;
		}
		int newSize = endIndexExclusive - startIndexInclusive;
		if (newSize <= 0) {
			return EMPTY_DOUBLE_ARRAY;
		}

		double[] subarray = new double[newSize];
		System.arraycopy(array, startIndexInclusive, subarray, 0, newSize);
		return subarray;
	}

	/**
	 * Produces a new {@code float} array containing the elements between the start and
	 * end indices.
	 *
	 * <p>
	 * The start index is inclusive, the end index exclusive. Null array input produces
	 * null output.
	 * @param array the array
	 * @param startIndexInclusive the starting index. Undervalue (&lt;0) is promoted to 0,
	 * overvalue (&gt;array.length) results in an empty array.
	 * @param endIndexExclusive elements up to endIndex-1 are present in the returned
	 * subarray. Undervalue (&lt; startIndex) produces empty array, overvalue
	 * (&gt;array.length) is demoted to array length.
	 * @return a new array containing the elements between the start and end indices.
	 * @since 2.1
	 * @see Arrays#copyOfRange(float[], int, int)
	 */
	public static float[] subarray(float[] array, int startIndexInclusive, int endIndexExclusive) {
		if (array == null) {
			return null;
		}
		if (startIndexInclusive < 0) {
			startIndexInclusive = 0;
		}
		if (endIndexExclusive > array.length) {
			endIndexExclusive = array.length;
		}
		int newSize = endIndexExclusive - startIndexInclusive;
		if (newSize <= 0) {
			return EMPTY_FLOAT_ARRAY;
		}

		float[] subarray = new float[newSize];
		System.arraycopy(array, startIndexInclusive, subarray, 0, newSize);
		return subarray;
	}

	/**
	 * Produces a new {@code int} array containing the elements between the start and end
	 * indices.
	 *
	 * <p>
	 * The start index is inclusive, the end index exclusive. Null array input produces
	 * null output.
	 * @param array the array
	 * @param startIndexInclusive the starting index. Undervalue (&lt;0) is promoted to 0,
	 * overvalue (&gt;array.length) results in an empty array.
	 * @param endIndexExclusive elements up to endIndex-1 are present in the returned
	 * subarray. Undervalue (&lt; startIndex) produces empty array, overvalue
	 * (&gt;array.length) is demoted to array length.
	 * @return a new array containing the elements between the start and end indices.
	 * @since 2.1
	 * @see Arrays#copyOfRange(int[], int, int)
	 */
	public static int[] subarray(int[] array, int startIndexInclusive, int endIndexExclusive) {
		if (array == null) {
			return null;
		}
		if (startIndexInclusive < 0) {
			startIndexInclusive = 0;
		}
		if (endIndexExclusive > array.length) {
			endIndexExclusive = array.length;
		}
		int newSize = endIndexExclusive - startIndexInclusive;
		if (newSize <= 0) {
			return EMPTY_INT_ARRAY;
		}

		int[] subarray = new int[newSize];
		System.arraycopy(array, startIndexInclusive, subarray, 0, newSize);
		return subarray;
	}

	/**
	 * Produces a new {@code long} array containing the elements between the start and end
	 * indices.
	 *
	 * <p>
	 * The start index is inclusive, the end index exclusive. Null array input produces
	 * null output.
	 * @param array the array
	 * @param startIndexInclusive the starting index. Undervalue (&lt;0) is promoted to 0,
	 * overvalue (&gt;array.length) results in an empty array.
	 * @param endIndexExclusive elements up to endIndex-1 are present in the returned
	 * subarray. Undervalue (&lt; startIndex) produces empty array, overvalue
	 * (&gt;array.length) is demoted to array length.
	 * @return a new array containing the elements between the start and end indices.
	 * @since 2.1
	 * @see Arrays#copyOfRange(long[], int, int)
	 */
	public static long[] subarray(long[] array, int startIndexInclusive, int endIndexExclusive) {
		if (array == null) {
			return null;
		}
		if (startIndexInclusive < 0) {
			startIndexInclusive = 0;
		}
		if (endIndexExclusive > array.length) {
			endIndexExclusive = array.length;
		}
		int newSize = endIndexExclusive - startIndexInclusive;
		if (newSize <= 0) {
			return EMPTY_LONG_ARRAY;
		}

		long[] subarray = new long[newSize];
		System.arraycopy(array, startIndexInclusive, subarray, 0, newSize);
		return subarray;
	}

	/**
	 * Produces a new {@code short} array containing the elements between the start and
	 * end indices.
	 *
	 * <p>
	 * The start index is inclusive, the end index exclusive. Null array input produces
	 * null output.
	 * @param array the array
	 * @param startIndexInclusive the starting index. Undervalue (&lt;0) is promoted to 0,
	 * overvalue (&gt;array.length) results in an empty array.
	 * @param endIndexExclusive elements up to endIndex-1 are present in the returned
	 * subarray. Undervalue (&lt; startIndex) produces empty array, overvalue
	 * (&gt;array.length) is demoted to array length.
	 * @return a new array containing the elements between the start and end indices.
	 * @since 2.1
	 * @see Arrays#copyOfRange(short[], int, int)
	 */
	public static short[] subarray(short[] array, int startIndexInclusive, int endIndexExclusive) {
		if (array == null) {
			return null;
		}
		if (startIndexInclusive < 0) {
			startIndexInclusive = 0;
		}
		if (endIndexExclusive > array.length) {
			endIndexExclusive = array.length;
		}
		int newSize = endIndexExclusive - startIndexInclusive;
		if (newSize <= 0) {
			return EMPTY_SHORT_ARRAY;
		}

		short[] subarray = new short[newSize];
		System.arraycopy(array, startIndexInclusive, subarray, 0, newSize);
		return subarray;
	}

	// Subarrays
	// -----------------------------------------------------------------------
	/**
	 * Produces a new array containing the elements between the start and end indices.
	 *
	 * <p>
	 * The start index is inclusive, the end index exclusive. Null array input produces
	 * null output.
	 *
	 * <p>
	 * The component type of the subarray is always the same as that of the input array.
	 * Thus, if the input is an array of type {@code Date}, the following usage is
	 * envisaged:
	 *
	 * <pre>
	 * Date[] someDates = (Date[]) ArrayUtils.subarray(allDates, 2, 5);
	 * </pre>
	 * @param <T> the component type of the array
	 * @param array the array
	 * @param startIndexInclusive the starting index. Undervalue (&lt;0) is promoted to 0,
	 * overvalue (&gt;array.length) results in an empty array.
	 * @param endIndexExclusive elements up to endIndex-1 are present in the returned
	 * subarray. Undervalue (&lt; startIndex) produces empty array, overvalue
	 * (&gt;array.length) is demoted to array length.
	 * @return a new array containing the elements between the start and end indices.
	 * @since 2.1
	 * @see Arrays#copyOfRange(Object[], int, int)
	 */
	public static <T> T[] subarray(T[] array, int startIndexInclusive, int endIndexExclusive) {
		if (array == null) {
			return null;
		}
		if (startIndexInclusive < 0) {
			startIndexInclusive = 0;
		}
		if (endIndexExclusive > array.length) {
			endIndexExclusive = array.length;
		}
		int newSize = endIndexExclusive - startIndexInclusive;
		Class<?> type = array.getClass().getComponentType();
		if (newSize <= 0) {
			// OK, because array is of type T
			T[] emptyArray = (T[]) Array.newInstance(type, 0);
			return emptyArray;
		}
		// OK, because array is of type T
		T[] subarray = (T[]) Array.newInstance(type, newSize);
		System.arraycopy(array, startIndexInclusive, subarray, 0, newSize);
		return subarray;
	}

	/**
	 * Swaps two elements in the given boolean array.
	 *
	 * <p>
	 * There is no special handling for multi-dimensional arrays. This method does nothing
	 * for a {@code null} or empty input array or for overflow indices. Negative indices
	 * are promoted to 0(zero). Examples:
	 *
	 * <ul>
	 * <li>ArrayUtils.swap([1, 2, 3], 0, 2) -&gt; [3, 2, 1]
	 * <li>ArrayUtils.swap([1, 2, 3], 0, 0) -&gt; [1, 2, 3]
	 * <li>ArrayUtils.swap([1, 2, 3], 1, 0) -&gt; [2, 1, 3]
	 * <li>ArrayUtils.swap([1, 2, 3], 0, 5) -&gt; [1, 2, 3]
	 * <li>ArrayUtils.swap([1, 2, 3], -1, 1) -&gt; [2, 1, 3]
	 * </ul>
	 * @param array the array to swap, may be {@code null}
	 * @param offset1 the index of the first element to swap
	 * @param offset2 the index of the second element to swap
	 * @since 3.5
	 */
	public static void swap(boolean[] array, int offset1, int offset2) {
		if (isEmpty(array)) {
			return;
		}
		swap(array, offset1, offset2, 1);
	}

	/**
	 * Swaps a series of elements in the given boolean array.
	 *
	 * <p>
	 * This method does nothing for a {@code null} or empty input array or for overflow
	 * indices. Negative indices are promoted to 0(zero). If any of the sub-arrays to swap
	 * falls outside of the given array, then the swap is stopped at the end of the array
	 * and as many as possible elements are swapped. Examples:
	 *
	 * <ul>
	 * <li>ArrayUtils.swap([true, false, true, false], 0, 2, 1) -&gt; [true, false, true,
	 * false]
	 * <li>ArrayUtils.swap([true, false, true, false], 0, 0, 1) -&gt; [true, false, true,
	 * false]
	 * <li>ArrayUtils.swap([true, false, true, false], 0, 2, 2) -&gt; [true, false, true,
	 * false]
	 * <li>ArrayUtils.swap([true, false, true, false], -3, 2, 2) -&gt; [true, false, true,
	 * false]
	 * <li>ArrayUtils.swap([true, false, true, false], 0, 3, 3) -&gt; [false, false, true,
	 * true]
	 * </ul>
	 * @param array the array to swap, may be {@code null}
	 * @param offset1 the index of the first element in the series to swap
	 * @param offset2 the index of the second element in the series to swap
	 * @param len the number of elements to swap starting with the given indices
	 * @since 3.5
	 */
	public static void swap(boolean[] array, int offset1, int offset2, int len) {
		if (isEmpty(array) || offset1 >= array.length || offset2 >= array.length) {
			return;
		}
		if (offset1 < 0) {
			offset1 = 0;
		}
		if (offset2 < 0) {
			offset2 = 0;
		}
		len = Math.min(Math.min(len, array.length - offset1), array.length - offset2);
		for (int i = 0; i < len; i++, offset1++, offset2++) {
			boolean aux = array[offset1];
			array[offset1] = array[offset2];
			array[offset2] = aux;
		}
	}

	/**
	 * Swaps two elements in the given byte array.
	 *
	 * <p>
	 * There is no special handling for multi-dimensional arrays. This method does nothing
	 * for a {@code null} or empty input array or for overflow indices. Negative indices
	 * are promoted to 0(zero). Examples:
	 *
	 * <ul>
	 * <li>ArrayUtils.swap([1, 2, 3], 0, 2) -&gt; [3, 2, 1]
	 * <li>ArrayUtils.swap([1, 2, 3], 0, 0) -&gt; [1, 2, 3]
	 * <li>ArrayUtils.swap([1, 2, 3], 1, 0) -&gt; [2, 1, 3]
	 * <li>ArrayUtils.swap([1, 2, 3], 0, 5) -&gt; [1, 2, 3]
	 * <li>ArrayUtils.swap([1, 2, 3], -1, 1) -&gt; [2, 1, 3]
	 * </ul>
	 * @param array the array to swap, may be {@code null}
	 * @param offset1 the index of the first element to swap
	 * @param offset2 the index of the second element to swap
	 * @since 3.5
	 */
	public static void swap(byte[] array, int offset1, int offset2) {
		if (isEmpty(array)) {
			return;
		}
		swap(array, offset1, offset2, 1);
	}

	/**
	 * Swaps a series of elements in the given byte array.
	 *
	 * <p>
	 * This method does nothing for a {@code null} or empty input array or for overflow
	 * indices. Negative indices are promoted to 0(zero). If any of the sub-arrays to swap
	 * falls outside of the given array, then the swap is stopped at the end of the array
	 * and as many as possible elements are swapped. Examples:
	 *
	 * <ul>
	 * <li>ArrayUtils.swap([1, 2, 3, 4], 0, 2, 1) -&gt; [3, 2, 1, 4]
	 * <li>ArrayUtils.swap([1, 2, 3, 4], 0, 0, 1) -&gt; [1, 2, 3, 4]
	 * <li>ArrayUtils.swap([1, 2, 3, 4], 2, 0, 2) -&gt; [3, 4, 1, 2]
	 * <li>ArrayUtils.swap([1, 2, 3, 4], -3, 2, 2) -&gt; [3, 4, 1, 2]
	 * <li>ArrayUtils.swap([1, 2, 3, 4], 0, 3, 3) -&gt; [4, 2, 3, 1]
	 * </ul>
	 * @param array the array to swap, may be {@code null}
	 * @param offset1 the index of the first element in the series to swap
	 * @param offset2 the index of the second element in the series to swap
	 * @param len the number of elements to swap starting with the given indices
	 * @since 3.5
	 */
	public static void swap(byte[] array, int offset1, int offset2, int len) {
		if (isEmpty(array) || offset1 >= array.length || offset2 >= array.length) {
			return;
		}
		if (offset1 < 0) {
			offset1 = 0;
		}
		if (offset2 < 0) {
			offset2 = 0;
		}
		len = Math.min(Math.min(len, array.length - offset1), array.length - offset2);
		for (int i = 0; i < len; i++, offset1++, offset2++) {
			byte aux = array[offset1];
			array[offset1] = array[offset2];
			array[offset2] = aux;
		}
	}

	/**
	 * Swaps two elements in the given char array.
	 *
	 * <p>
	 * There is no special handling for multi-dimensional arrays. This method does nothing
	 * for a {@code null} or empty input array or for overflow indices. Negative indices
	 * are promoted to 0(zero). Examples:
	 *
	 * <ul>
	 * <li>ArrayUtils.swap([1, 2, 3], 0, 2) -&gt; [3, 2, 1]
	 * <li>ArrayUtils.swap([1, 2, 3], 0, 0) -&gt; [1, 2, 3]
	 * <li>ArrayUtils.swap([1, 2, 3], 1, 0) -&gt; [2, 1, 3]
	 * <li>ArrayUtils.swap([1, 2, 3], 0, 5) -&gt; [1, 2, 3]
	 * <li>ArrayUtils.swap([1, 2, 3], -1, 1) -&gt; [2, 1, 3]
	 * </ul>
	 * @param array the array to swap, may be {@code null}
	 * @param offset1 the index of the first element to swap
	 * @param offset2 the index of the second element to swap
	 * @since 3.5
	 */
	public static void swap(char[] array, int offset1, int offset2) {
		if (isEmpty(array)) {
			return;
		}
		swap(array, offset1, offset2, 1);
	}

	/**
	 * Swaps a series of elements in the given char array.
	 *
	 * <p>
	 * This method does nothing for a {@code null} or empty input array or for overflow
	 * indices. Negative indices are promoted to 0(zero). If any of the sub-arrays to swap
	 * falls outside of the given array, then the swap is stopped at the end of the array
	 * and as many as possible elements are swapped. Examples:
	 *
	 * <ul>
	 * <li>ArrayUtils.swap([1, 2, 3, 4], 0, 2, 1) -&gt; [3, 2, 1, 4]
	 * <li>ArrayUtils.swap([1, 2, 3, 4], 0, 0, 1) -&gt; [1, 2, 3, 4]
	 * <li>ArrayUtils.swap([1, 2, 3, 4], 2, 0, 2) -&gt; [3, 4, 1, 2]
	 * <li>ArrayUtils.swap([1, 2, 3, 4], -3, 2, 2) -&gt; [3, 4, 1, 2]
	 * <li>ArrayUtils.swap([1, 2, 3, 4], 0, 3, 3) -&gt; [4, 2, 3, 1]
	 * </ul>
	 * @param array the array to swap, may be {@code null}
	 * @param offset1 the index of the first element in the series to swap
	 * @param offset2 the index of the second element in the series to swap
	 * @param len the number of elements to swap starting with the given indices
	 * @since 3.5
	 */
	public static void swap(char[] array, int offset1, int offset2, int len) {
		if (isEmpty(array) || offset1 >= array.length || offset2 >= array.length) {
			return;
		}
		if (offset1 < 0) {
			offset1 = 0;
		}
		if (offset2 < 0) {
			offset2 = 0;
		}
		len = Math.min(Math.min(len, array.length - offset1), array.length - offset2);
		for (int i = 0; i < len; i++, offset1++, offset2++) {
			char aux = array[offset1];
			array[offset1] = array[offset2];
			array[offset2] = aux;
		}
	}

	/**
	 * Swaps two elements in the given double array.
	 *
	 * <p>
	 * There is no special handling for multi-dimensional arrays. This method does nothing
	 * for a {@code null} or empty input array or for overflow indices. Negative indices
	 * are promoted to 0(zero). Examples:
	 *
	 * <ul>
	 * <li>ArrayUtils.swap([1, 2, 3], 0, 2) -&gt; [3, 2, 1]
	 * <li>ArrayUtils.swap([1, 2, 3], 0, 0) -&gt; [1, 2, 3]
	 * <li>ArrayUtils.swap([1, 2, 3], 1, 0) -&gt; [2, 1, 3]
	 * <li>ArrayUtils.swap([1, 2, 3], 0, 5) -&gt; [1, 2, 3]
	 * <li>ArrayUtils.swap([1, 2, 3], -1, 1) -&gt; [2, 1, 3]
	 * </ul>
	 * @param array the array to swap, may be {@code null}
	 * @param offset1 the index of the first element to swap
	 * @param offset2 the index of the second element to swap
	 * @since 3.5
	 */
	public static void swap(double[] array, int offset1, int offset2) {
		if (isEmpty(array)) {
			return;
		}
		swap(array, offset1, offset2, 1);
	}

	/**
	 * Swaps a series of elements in the given double array.
	 *
	 * <p>
	 * This method does nothing for a {@code null} or empty input array or for overflow
	 * indices. Negative indices are promoted to 0(zero). If any of the sub-arrays to swap
	 * falls outside of the given array, then the swap is stopped at the end of the array
	 * and as many as possible elements are swapped. Examples:
	 *
	 * <ul>
	 * <li>ArrayUtils.swap([1, 2, 3, 4], 0, 2, 1) -&gt; [3, 2, 1, 4]
	 * <li>ArrayUtils.swap([1, 2, 3, 4], 0, 0, 1) -&gt; [1, 2, 3, 4]
	 * <li>ArrayUtils.swap([1, 2, 3, 4], 2, 0, 2) -&gt; [3, 4, 1, 2]
	 * <li>ArrayUtils.swap([1, 2, 3, 4], -3, 2, 2) -&gt; [3, 4, 1, 2]
	 * <li>ArrayUtils.swap([1, 2, 3, 4], 0, 3, 3) -&gt; [4, 2, 3, 1]
	 * </ul>
	 * @param array the array to swap, may be {@code null}
	 * @param offset1 the index of the first element in the series to swap
	 * @param offset2 the index of the second element in the series to swap
	 * @param len the number of elements to swap starting with the given indices
	 * @since 3.5
	 */
	public static void swap(double[] array, int offset1, int offset2, int len) {
		if (isEmpty(array) || offset1 >= array.length || offset2 >= array.length) {
			return;
		}
		if (offset1 < 0) {
			offset1 = 0;
		}
		if (offset2 < 0) {
			offset2 = 0;
		}
		len = Math.min(Math.min(len, array.length - offset1), array.length - offset2);
		for (int i = 0; i < len; i++, offset1++, offset2++) {
			double aux = array[offset1];
			array[offset1] = array[offset2];
			array[offset2] = aux;
		}
	}

	/**
	 * Swaps two elements in the given float array.
	 *
	 * <p>
	 * There is no special handling for multi-dimensional arrays. This method does nothing
	 * for a {@code null} or empty input array or for overflow indices. Negative indices
	 * are promoted to 0(zero). Examples:
	 *
	 * <ul>
	 * <li>ArrayUtils.swap([1, 2, 3], 0, 2) -&gt; [3, 2, 1]
	 * <li>ArrayUtils.swap([1, 2, 3], 0, 0) -&gt; [1, 2, 3]
	 * <li>ArrayUtils.swap([1, 2, 3], 1, 0) -&gt; [2, 1, 3]
	 * <li>ArrayUtils.swap([1, 2, 3], 0, 5) -&gt; [1, 2, 3]
	 * <li>ArrayUtils.swap([1, 2, 3], -1, 1) -&gt; [2, 1, 3]
	 * </ul>
	 * @param array the array to swap, may be {@code null}
	 * @param offset1 the index of the first element to swap
	 * @param offset2 the index of the second element to swap
	 * @since 3.5
	 */
	public static void swap(float[] array, int offset1, int offset2) {
		if (isEmpty(array)) {
			return;
		}
		swap(array, offset1, offset2, 1);
	}

	/**
	 * Swaps a series of elements in the given float array.
	 *
	 * <p>
	 * This method does nothing for a {@code null} or empty input array or for overflow
	 * indices. Negative indices are promoted to 0(zero). If any of the sub-arrays to swap
	 * falls outside of the given array, then the swap is stopped at the end of the array
	 * and as many as possible elements are swapped. Examples:
	 *
	 * <ul>
	 * <li>ArrayUtils.swap([1, 2, 3, 4], 0, 2, 1) -&gt; [3, 2, 1, 4]
	 * <li>ArrayUtils.swap([1, 2, 3, 4], 0, 0, 1) -&gt; [1, 2, 3, 4]
	 * <li>ArrayUtils.swap([1, 2, 3, 4], 2, 0, 2) -&gt; [3, 4, 1, 2]
	 * <li>ArrayUtils.swap([1, 2, 3, 4], -3, 2, 2) -&gt; [3, 4, 1, 2]
	 * <li>ArrayUtils.swap([1, 2, 3, 4], 0, 3, 3) -&gt; [4, 2, 3, 1]
	 * </ul>
	 * @param array the array to swap, may be {@code null}
	 * @param offset1 the index of the first element in the series to swap
	 * @param offset2 the index of the second element in the series to swap
	 * @param len the number of elements to swap starting with the given indices
	 * @since 3.5
	 */
	public static void swap(float[] array, int offset1, int offset2, int len) {
		if (isEmpty(array) || offset1 >= array.length || offset2 >= array.length) {
			return;
		}
		if (offset1 < 0) {
			offset1 = 0;
		}
		if (offset2 < 0) {
			offset2 = 0;
		}
		len = Math.min(Math.min(len, array.length - offset1), array.length - offset2);
		for (int i = 0; i < len; i++, offset1++, offset2++) {
			float aux = array[offset1];
			array[offset1] = array[offset2];
			array[offset2] = aux;
		}
	}

	/**
	 * Swaps two elements in the given int array.
	 *
	 * <p>
	 * There is no special handling for multi-dimensional arrays. This method does nothing
	 * for a {@code null} or empty input array or for overflow indices. Negative indices
	 * are promoted to 0(zero). Examples:
	 *
	 * <ul>
	 * <li>ArrayUtils.swap([1, 2, 3], 0, 2) -&gt; [3, 2, 1]
	 * <li>ArrayUtils.swap([1, 2, 3], 0, 0) -&gt; [1, 2, 3]
	 * <li>ArrayUtils.swap([1, 2, 3], 1, 0) -&gt; [2, 1, 3]
	 * <li>ArrayUtils.swap([1, 2, 3], 0, 5) -&gt; [1, 2, 3]
	 * <li>ArrayUtils.swap([1, 2, 3], -1, 1) -&gt; [2, 1, 3]
	 * </ul>
	 * @param array the array to swap, may be {@code null}
	 * @param offset1 the index of the first element to swap
	 * @param offset2 the index of the second element to swap
	 * @since 3.5
	 */
	public static void swap(int[] array, int offset1, int offset2) {
		if (isEmpty(array)) {
			return;
		}
		swap(array, offset1, offset2, 1);
	}

	/**
	 * Swaps a series of elements in the given int array.
	 *
	 * <p>
	 * This method does nothing for a {@code null} or empty input array or for overflow
	 * indices. Negative indices are promoted to 0(zero). If any of the sub-arrays to swap
	 * falls outside of the given array, then the swap is stopped at the end of the array
	 * and as many as possible elements are swapped. Examples:
	 *
	 * <ul>
	 * <li>ArrayUtils.swap([1, 2, 3, 4], 0, 2, 1) -&gt; [3, 2, 1, 4]
	 * <li>ArrayUtils.swap([1, 2, 3, 4], 0, 0, 1) -&gt; [1, 2, 3, 4]
	 * <li>ArrayUtils.swap([1, 2, 3, 4], 2, 0, 2) -&gt; [3, 4, 1, 2]
	 * <li>ArrayUtils.swap([1, 2, 3, 4], -3, 2, 2) -&gt; [3, 4, 1, 2]
	 * <li>ArrayUtils.swap([1, 2, 3, 4], 0, 3, 3) -&gt; [4, 2, 3, 1]
	 * </ul>
	 * @param array the array to swap, may be {@code null}
	 * @param offset1 the index of the first element in the series to swap
	 * @param offset2 the index of the second element in the series to swap
	 * @param len the number of elements to swap starting with the given indices
	 * @since 3.5
	 */
	public static void swap(int[] array, int offset1, int offset2, int len) {
		if (isEmpty(array) || offset1 >= array.length || offset2 >= array.length) {
			return;
		}
		if (offset1 < 0) {
			offset1 = 0;
		}
		if (offset2 < 0) {
			offset2 = 0;
		}
		len = Math.min(Math.min(len, array.length - offset1), array.length - offset2);
		for (int i = 0; i < len; i++, offset1++, offset2++) {
			int aux = array[offset1];
			array[offset1] = array[offset2];
			array[offset2] = aux;
		}
	}

	/**
	 * Swaps two elements in the given long array.
	 *
	 * <p>
	 * There is no special handling for multi-dimensional arrays. This method does nothing
	 * for a {@code null} or empty input array or for overflow indices. Negative indices
	 * are promoted to 0(zero). Examples:
	 *
	 * <ul>
	 * <li>ArrayUtils.swap([true, false, true], 0, 2) -&gt; [true, false, true]
	 * <li>ArrayUtils.swap([true, false, true], 0, 0) -&gt; [true, false, true]
	 * <li>ArrayUtils.swap([true, false, true], 1, 0) -&gt; [false, true, true]
	 * <li>ArrayUtils.swap([true, false, true], 0, 5) -&gt; [true, false, true]
	 * <li>ArrayUtils.swap([true, false, true], -1, 1) -&gt; [false, true, true]
	 * </ul>
	 * @param array the array to swap, may be {@code null}
	 * @param offset1 the index of the first element to swap
	 * @param offset2 the index of the second element to swap
	 * @since 3.5
	 */
	public static void swap(long[] array, int offset1, int offset2) {
		if (isEmpty(array)) {
			return;
		}
		swap(array, offset1, offset2, 1);
	}

	/**
	 * Swaps a series of elements in the given long array.
	 *
	 * <p>
	 * This method does nothing for a {@code null} or empty input array or for overflow
	 * indices. Negative indices are promoted to 0(zero). If any of the sub-arrays to swap
	 * falls outside of the given array, then the swap is stopped at the end of the array
	 * and as many as possible elements are swapped. Examples:
	 *
	 * <ul>
	 * <li>ArrayUtils.swap([1, 2, 3, 4], 0, 2, 1) -&gt; [3, 2, 1, 4]
	 * <li>ArrayUtils.swap([1, 2, 3, 4], 0, 0, 1) -&gt; [1, 2, 3, 4]
	 * <li>ArrayUtils.swap([1, 2, 3, 4], 2, 0, 2) -&gt; [3, 4, 1, 2]
	 * <li>ArrayUtils.swap([1, 2, 3, 4], -3, 2, 2) -&gt; [3, 4, 1, 2]
	 * <li>ArrayUtils.swap([1, 2, 3, 4], 0, 3, 3) -&gt; [4, 2, 3, 1]
	 * </ul>
	 * @param array the array to swap, may be {@code null}
	 * @param offset1 the index of the first element in the series to swap
	 * @param offset2 the index of the second element in the series to swap
	 * @param len the number of elements to swap starting with the given indices
	 * @since 3.5
	 */
	public static void swap(long[] array, int offset1, int offset2, int len) {
		if (isEmpty(array) || offset1 >= array.length || offset2 >= array.length) {
			return;
		}
		if (offset1 < 0) {
			offset1 = 0;
		}
		if (offset2 < 0) {
			offset2 = 0;
		}
		len = Math.min(Math.min(len, array.length - offset1), array.length - offset2);
		for (int i = 0; i < len; i++, offset1++, offset2++) {
			long aux = array[offset1];
			array[offset1] = array[offset2];
			array[offset2] = aux;
		}
	}

	// Swap
	// -----------------------------------------------------------------------
	/**
	 * Swaps two elements in the given array.
	 *
	 * <p>
	 * There is no special handling for multi-dimensional arrays. This method does nothing
	 * for a {@code null} or empty input array or for overflow indices. Negative indices
	 * are promoted to 0(zero). Examples:
	 *
	 * <ul>
	 * <li>ArrayUtils.swap(["1", "2", "3"], 0, 2) -&gt; ["3", "2", "1"]
	 * <li>ArrayUtils.swap(["1", "2", "3"], 0, 0) -&gt; ["1", "2", "3"]
	 * <li>ArrayUtils.swap(["1", "2", "3"], 1, 0) -&gt; ["2", "1", "3"]
	 * <li>ArrayUtils.swap(["1", "2", "3"], 0, 5) -&gt; ["1", "2", "3"]
	 * <li>ArrayUtils.swap(["1", "2", "3"], -1, 1) -&gt; ["2", "1", "3"]
	 * </ul>
	 * @param array the array to swap, may be {@code null}
	 * @param offset1 the index of the first element to swap
	 * @param offset2 the index of the second element to swap
	 * @since 3.5
	 */
	public static void swap(Object[] array, int offset1, int offset2) {
		if (isEmpty(array)) {
			return;
		}
		swap(array, offset1, offset2, 1);
	}

	/**
	 * Swaps a series of elements in the given array.
	 *
	 * <p>
	 * This method does nothing for a {@code null} or empty input array or for overflow
	 * indices. Negative indices are promoted to 0(zero). If any of the sub-arrays to swap
	 * falls outside of the given array, then the swap is stopped at the end of the array
	 * and as many as possible elements are swapped. Examples:
	 *
	 * <ul>
	 * <li>ArrayUtils.swap(["1", "2", "3", "4"], 0, 2, 1) -&gt; ["3", "2", "1", "4"]
	 * <li>ArrayUtils.swap(["1", "2", "3", "4"], 0, 0, 1) -&gt; ["1", "2", "3", "4"]
	 * <li>ArrayUtils.swap(["1", "2", "3", "4"], 2, 0, 2) -&gt; ["3", "4", "1", "2"]
	 * <li>ArrayUtils.swap(["1", "2", "3", "4"], -3, 2, 2) -&gt; ["3", "4", "1", "2"]
	 * <li>ArrayUtils.swap(["1", "2", "3", "4"], 0, 3, 3) -&gt; ["4", "2", "3", "1"]
	 * </ul>
	 * @param array the array to swap, may be {@code null}
	 * @param offset1 the index of the first element in the series to swap
	 * @param offset2 the index of the second element in the series to swap
	 * @param len the number of elements to swap starting with the given indices
	 * @since 3.5
	 */
	public static void swap(Object[] array, int offset1, int offset2, int len) {
		if (isEmpty(array) || offset1 >= array.length || offset2 >= array.length) {
			return;
		}
		if (offset1 < 0) {
			offset1 = 0;
		}
		if (offset2 < 0) {
			offset2 = 0;
		}
		len = Math.min(Math.min(len, array.length - offset1), array.length - offset2);
		for (int i = 0; i < len; i++, offset1++, offset2++) {
			Object aux = array[offset1];
			array[offset1] = array[offset2];
			array[offset2] = aux;
		}
	}

	/**
	 * Swaps two elements in the given short array.
	 *
	 * <p>
	 * There is no special handling for multi-dimensional arrays. This method does nothing
	 * for a {@code null} or empty input array or for overflow indices. Negative indices
	 * are promoted to 0(zero). Examples:
	 *
	 * <ul>
	 * <li>ArrayUtils.swap([1, 2, 3], 0, 2) -&gt; [3, 2, 1]
	 * <li>ArrayUtils.swap([1, 2, 3], 0, 0) -&gt; [1, 2, 3]
	 * <li>ArrayUtils.swap([1, 2, 3], 1, 0) -&gt; [2, 1, 3]
	 * <li>ArrayUtils.swap([1, 2, 3], 0, 5) -&gt; [1, 2, 3]
	 * <li>ArrayUtils.swap([1, 2, 3], -1, 1) -&gt; [2, 1, 3]
	 * </ul>
	 * @param array the array to swap, may be {@code null}
	 * @param offset1 the index of the first element to swap
	 * @param offset2 the index of the second element to swap
	 * @since 3.5
	 */
	public static void swap(short[] array, int offset1, int offset2) {
		if (isEmpty(array)) {
			return;
		}
		swap(array, offset1, offset2, 1);
	}

	/**
	 * Swaps a series of elements in the given short array.
	 *
	 * <p>
	 * This method does nothing for a {@code null} or empty input array or for overflow
	 * indices. Negative indices are promoted to 0(zero). If any of the sub-arrays to swap
	 * falls outside of the given array, then the swap is stopped at the end of the array
	 * and as many as possible elements are swapped. Examples:
	 *
	 * <ul>
	 * <li>ArrayUtils.swap([1, 2, 3, 4], 0, 2, 1) -&gt; [3, 2, 1, 4]
	 * <li>ArrayUtils.swap([1, 2, 3, 4], 0, 0, 1) -&gt; [1, 2, 3, 4]
	 * <li>ArrayUtils.swap([1, 2, 3, 4], 2, 0, 2) -&gt; [3, 4, 1, 2]
	 * <li>ArrayUtils.swap([1, 2, 3, 4], -3, 2, 2) -&gt; [3, 4, 1, 2]
	 * <li>ArrayUtils.swap([1, 2, 3, 4], 0, 3, 3) -&gt; [4, 2, 3, 1]
	 * </ul>
	 * @param array the array to swap, may be {@code null}
	 * @param offset1 the index of the first element in the series to swap
	 * @param offset2 the index of the second element in the series to swap
	 * @param len the number of elements to swap starting with the given indices
	 * @since 3.5
	 */
	public static void swap(short[] array, int offset1, int offset2, int len) {
		if (isEmpty(array) || offset1 >= array.length || offset2 >= array.length) {
			return;
		}
		if (offset1 < 0) {
			offset1 = 0;
		}
		if (offset2 < 0) {
			offset2 = 0;
		}
		if (offset1 == offset2) {
			return;
		}
		len = Math.min(Math.min(len, array.length - offset1), array.length - offset2);
		for (int i = 0; i < len; i++, offset1++, offset2++) {
			short aux = array[offset1];
			array[offset1] = array[offset2];
			array[offset2] = aux;
		}
	}

	// Generic array
	// -----------------------------------------------------------------------
	/**
	 * Create a type-safe generic array.
	 *
	 * <p>
	 * The Java language does not allow an array to be created from a generic type:
	 *
	 * <pre>
	 * public static &lt;T&gt; T[] createAnArray(int size) {
	 * return new T[size]; // compiler error here
	 * }
	 * public static &lt;T&gt; T[] createAnArray(int size) {
	 * return (T[]) new Object[size]; // ClassCastException at runtime
	 * }
	 * </pre>
	 *
	 * <p>
	 * Therefore new arrays of generic types can be created with this method. For example,
	 * an array of Strings can be created:
	 *
	 * <pre>
	 * String[] array = ArrayUtils.toArray("1", "2");
	 * String[] emptyArray = ArrayUtils.&lt;String&gt;toArray();
	 * </pre>
	 *
	 * <p>
	 * The method is typically used in scenarios, where the caller itself uses generic
	 * types that have to be combined into an array.
	 *
	 * <p>
	 * Note, this method makes only sense to provide arguments of the same type so that
	 * the compiler can deduce the type of the array itself. While it is possible to
	 * select the type explicitly like in {@code Number[] array =
	 * ArrayUtils.&lt;Number&gt;toArray(Integer.valueOf(42), Double.valueOf(Math.PI))},
	 * there is no real advantage when compared to {@code new Number[]
	 * {Integer.valueOf(42), Double.valueOf(Math.PI)}}.
	 * @param <T> the array's element type
	 * @param items the varargs array items, null allowed
	 * @return the array, not null unless a null array is passed in
	 * @since 3.0
	 */
	public static <T> T[] toArray(T... items) {
		return items;
	}

	// To map
	// -----------------------------------------------------------------------
	/**
	 * Converts the given array into a {@link Map}. Each element of the array must be
	 * either a {@link Map.Entry} or an Array, containing at least two elements, where the
	 * first element is used as key and the second as value.
	 *
	 * <p>
	 * This method can be used to initialize:
	 *
	 * <pre>
	 * // Create a Map mapping colors.
	 * Map colorMap = ArrayUtils.toMap(new String[][] {
	 *     {"RED", "#FF0000"},
	 *     {"GREEN", "#00FF00"},
	 *     {"BLUE", "#0000FF"}});
	 * </pre>
	 *
	 * <p>
	 * This method returns {@code null} for a {@code null} input array.
	 * @param array an array whose elements are either a {@link Map.Entry} or an Array
	 * containing at least two elements, may be {@code null}
	 * @return a {@code Map} that was created from the array
	 * @throws IllegalArgumentException if one element of this Array is itself an Array
	 * containing less then two elements
	 * @throws IllegalArgumentException if the array contains elements other than
	 * {@link Map.Entry} and an Array
	 */
	public static Map<Object, Object> toMap(Object[] array) {
		if (array == null) {
			return null;
		}
		Map<Object, Object> map = new HashMap<>((int) (array.length * 1.5));
		for (int i = 0; i < array.length; i++) {
			Object object = array[i];
			if (object instanceof Map.Entry<?, ?>) {
				Map.Entry<?, ?> entry = (Map.Entry<?, ?>) object;
				map.put(entry.getKey(), entry.getValue());
			}
			else if (object instanceof Object[]) {
				Object[] entry = (Object[]) object;
				if (entry.length < 2) {
					throw new IllegalArgumentException(
							"Array element " + i + ", '" + object + "', has a length less than 2");
				}
				map.put(entry[0], entry[1]);
			}
			else {
				throw new IllegalArgumentException(
						"Array element " + i + ", '" + object + "', is neither of type Map.Entry nor an Array");
			}
		}
		return map;
	}

	/**
	 * Converts an array of primitive booleans to objects.
	 *
	 * <p>
	 * This method returns {@code null} for a {@code null} input array.
	 * @param array a {@code boolean} array
	 * @return a {@code Boolean} array, {@code null} if null array input
	 */
	public static Boolean[] toObject(boolean[] array) {
		if (array == null) {
			return null;
		}
		else if (array.length == 0) {
			return EMPTY_BOOLEAN_OBJECT_ARRAY;
		}
		Boolean[] result = new Boolean[array.length];
		for (int i = 0; i < array.length; i++) {
			result[i] = (array[i] ? Boolean.TRUE : Boolean.FALSE);
		}
		return result;
	}

	/**
	 * Converts an array of primitive bytes to objects.
	 *
	 * <p>
	 * This method returns {@code null} for a {@code null} input array.
	 * @param array a {@code byte} array
	 * @return a {@code Byte} array, {@code null} if null array input
	 */
	public static Byte[] toObject(byte[] array) {
		if (array == null) {
			return null;
		}
		else if (array.length == 0) {
			return EMPTY_BYTE_OBJECT_ARRAY;
		}
		Byte[] result = new Byte[array.length];
		for (int i = 0; i < array.length; i++) {
			result[i] = Byte.valueOf(array[i]);
		}
		return result;
	}

	/**
	 * Converts an array of primitive chars to objects.
	 *
	 * <p>
	 * This method returns {@code null} for a {@code null} input array.
	 * @param array a {@code char} array
	 * @return a {@code Character} array, {@code null} if null array input
	 */
	public static Character[] toObject(char[] array) {
		if (array == null) {
			return null;
		}
		else if (array.length == 0) {
			return EMPTY_CHARACTER_OBJECT_ARRAY;
		}
		Character[] result = new Character[array.length];
		for (int i = 0; i < array.length; i++) {
			result[i] = Character.valueOf(array[i]);
		}
		return result;
	}

	/**
	 * Converts an array of primitive doubles to objects.
	 *
	 * <p>
	 * This method returns {@code null} for a {@code null} input array.
	 * @param array a {@code double} array
	 * @return a {@code Double} array, {@code null} if null array input
	 */
	public static Double[] toObject(double[] array) {
		if (array == null) {
			return null;
		}
		else if (array.length == 0) {
			return EMPTY_DOUBLE_OBJECT_ARRAY;
		}
		Double[] result = new Double[array.length];
		for (int i = 0; i < array.length; i++) {
			result[i] = Double.valueOf(array[i]);
		}
		return result;
	}

	/**
	 * Converts an array of primitive floats to objects.
	 *
	 * <p>
	 * This method returns {@code null} for a {@code null} input array.
	 * @param array a {@code float} array
	 * @return a {@code Float} array, {@code null} if null array input
	 */
	public static Float[] toObject(float[] array) {
		if (array == null) {
			return null;
		}
		else if (array.length == 0) {
			return EMPTY_FLOAT_OBJECT_ARRAY;
		}
		Float[] result = new Float[array.length];
		for (int i = 0; i < array.length; i++) {
			result[i] = Float.valueOf(array[i]);
		}
		return result;
	}

	/**
	 * Converts an array of primitive ints to objects.
	 *
	 * <p>
	 * This method returns {@code null} for a {@code null} input array.
	 * @param array an {@code int} array
	 * @return an {@code Integer} array, {@code null} if null array input
	 */
	public static Integer[] toObject(int[] array) {
		if (array == null) {
			return null;
		}
		else if (array.length == 0) {
			return EMPTY_INTEGER_OBJECT_ARRAY;
		}
		Integer[] result = new Integer[array.length];
		for (int i = 0; i < array.length; i++) {
			result[i] = Integer.valueOf(array[i]);
		}
		return result;
	}

	/**
	 * Converts an array of primitive longs to objects.
	 *
	 * <p>
	 * This method returns {@code null} for a {@code null} input array.
	 * @param array a {@code long} array
	 * @return a {@code Long} array, {@code null} if null array input
	 */
	public static Long[] toObject(long[] array) {
		if (array == null) {
			return null;
		}
		else if (array.length == 0) {
			return EMPTY_LONG_OBJECT_ARRAY;
		}
		Long[] result = new Long[array.length];
		for (int i = 0; i < array.length; i++) {
			result[i] = Long.valueOf(array[i]);
		}
		return result;
	}

	/**
	 * Converts an array of primitive shorts to objects.
	 *
	 * <p>
	 * This method returns {@code null} for a {@code null} input array.
	 * @param array a {@code short} array
	 * @return a {@code Short} array, {@code null} if null array input
	 */
	public static Short[] toObject(short[] array) {
		if (array == null) {
			return null;
		}
		else if (array.length == 0) {
			return EMPTY_SHORT_OBJECT_ARRAY;
		}
		Short[] result = new Short[array.length];
		for (int i = 0; i < array.length; i++) {
			result[i] = Short.valueOf(array[i]);
		}
		return result;
	}

	// Boolean array converters
	// ----------------------------------------------------------------------
	/**
	 * Converts an array of object Booleans to primitives.
	 *
	 * <p>
	 * This method returns {@code null} for a {@code null} input array.
	 * @param array a {@code Boolean} array, may be {@code null}
	 * @return a {@code boolean} array, {@code null} if null array input
	 * @throws NullPointerException if array content is {@code null}
	 */
	public static boolean[] toPrimitive(Boolean[] array) {
		if (array == null) {
			return null;
		}
		else if (array.length == 0) {
			return EMPTY_BOOLEAN_ARRAY;
		}
		boolean[] result = new boolean[array.length];
		for (int i = 0; i < array.length; i++) {
			result[i] = array[i].booleanValue();
		}
		return result;
	}

	/**
	 * Converts an array of object Booleans to primitives handling {@code null}.
	 *
	 * <p>
	 * This method returns {@code null} for a {@code null} input array.
	 * @param array a {@code Boolean} array, may be {@code null}
	 * @param valueForNull the value to insert if {@code null} found
	 * @return a {@code boolean} array, {@code null} if null array input
	 */
	public static boolean[] toPrimitive(Boolean[] array, boolean valueForNull) {
		if (array == null) {
			return null;
		}
		else if (array.length == 0) {
			return EMPTY_BOOLEAN_ARRAY;
		}
		boolean[] result = new boolean[array.length];
		for (int i = 0; i < array.length; i++) {
			Boolean b = array[i];
			result[i] = (b == null ? valueForNull : b.booleanValue());
		}
		return result;
	}

	// Byte array converters
	// ----------------------------------------------------------------------
	/**
	 * Converts an array of object Bytes to primitives.
	 *
	 * <p>
	 * This method returns {@code null} for a {@code null} input array.
	 * @param array a {@code Byte} array, may be {@code null}
	 * @return a {@code byte} array, {@code null} if null array input
	 * @throws NullPointerException if array content is {@code null}
	 */
	public static byte[] toPrimitive(Byte[] array) {
		if (array == null) {
			return null;
		}
		else if (array.length == 0) {
			return EMPTY_BYTE_ARRAY;
		}
		byte[] result = new byte[array.length];
		for (int i = 0; i < array.length; i++) {
			result[i] = array[i].byteValue();
		}
		return result;
	}

	/**
	 * Converts an array of object Bytes to primitives handling {@code null}.
	 *
	 * <p>
	 * This method returns {@code null} for a {@code null} input array.
	 * @param array a {@code Byte} array, may be {@code null}
	 * @param valueForNull the value to insert if {@code null} found
	 * @return a {@code byte} array, {@code null} if null array input
	 */
	public static byte[] toPrimitive(Byte[] array, byte valueForNull) {
		if (array == null) {
			return null;
		}
		else if (array.length == 0) {
			return EMPTY_BYTE_ARRAY;
		}
		byte[] result = new byte[array.length];
		for (int i = 0; i < array.length; i++) {
			Byte b = array[i];
			result[i] = (b == null ? valueForNull : b.byteValue());
		}
		return result;
	}

	// Character array converters
	// ----------------------------------------------------------------------
	/**
	 * Converts an array of object Characters to primitives.
	 *
	 * <p>
	 * This method returns {@code null} for a {@code null} input array.
	 * @param array a {@code Character} array, may be {@code null}
	 * @return a {@code char} array, {@code null} if null array input
	 * @throws NullPointerException if array content is {@code null}
	 */
	public static char[] toPrimitive(Character[] array) {
		if (array == null) {
			return null;
		}
		else if (array.length == 0) {
			return EMPTY_CHAR_ARRAY;
		}
		char[] result = new char[array.length];
		for (int i = 0; i < array.length; i++) {
			result[i] = array[i].charValue();
		}
		return result;
	}

	/**
	 * Converts an array of object Character to primitives handling {@code null}.
	 *
	 * <p>
	 * This method returns {@code null} for a {@code null} input array.
	 * @param array a {@code Character} array, may be {@code null}
	 * @param valueForNull the value to insert if {@code null} found
	 * @return a {@code char} array, {@code null} if null array input
	 */
	public static char[] toPrimitive(Character[] array, char valueForNull) {
		if (array == null) {
			return null;
		}
		else if (array.length == 0) {
			return EMPTY_CHAR_ARRAY;
		}
		char[] result = new char[array.length];
		for (int i = 0; i < array.length; i++) {
			Character b = array[i];
			result[i] = (b == null ? valueForNull : b.charValue());
		}
		return result;
	}

	// Double array converters
	// ----------------------------------------------------------------------
	/**
	 * Converts an array of object Doubles to primitives.
	 *
	 * <p>
	 * This method returns {@code null} for a {@code null} input array.
	 * @param array a {@code Double} array, may be {@code null}
	 * @return a {@code double} array, {@code null} if null array input
	 * @throws NullPointerException if array content is {@code null}
	 */
	public static double[] toPrimitive(Double[] array) {
		if (array == null) {
			return null;
		}
		else if (array.length == 0) {
			return EMPTY_DOUBLE_ARRAY;
		}
		double[] result = new double[array.length];
		for (int i = 0; i < array.length; i++) {
			result[i] = array[i].doubleValue();
		}
		return result;
	}

	/**
	 * Converts an array of object Doubles to primitives handling {@code null}.
	 *
	 * <p>
	 * This method returns {@code null} for a {@code null} input array.
	 * @param array a {@code Double} array, may be {@code null}
	 * @param valueForNull the value to insert if {@code null} found
	 * @return a {@code double} array, {@code null} if null array input
	 */
	public static double[] toPrimitive(Double[] array, double valueForNull) {
		if (array == null) {
			return null;
		}
		else if (array.length == 0) {
			return EMPTY_DOUBLE_ARRAY;
		}
		double[] result = new double[array.length];
		for (int i = 0; i < array.length; i++) {
			Double b = array[i];
			result[i] = (b == null ? valueForNull : b.doubleValue());
		}
		return result;
	}

	// Float array converters
	// ----------------------------------------------------------------------
	/**
	 * Converts an array of object Floats to primitives.
	 *
	 * <p>
	 * This method returns {@code null} for a {@code null} input array.
	 * @param array a {@code Float} array, may be {@code null}
	 * @return a {@code float} array, {@code null} if null array input
	 * @throws NullPointerException if array content is {@code null}
	 */
	public static float[] toPrimitive(Float[] array) {
		if (array == null) {
			return null;
		}
		else if (array.length == 0) {
			return EMPTY_FLOAT_ARRAY;
		}
		float[] result = new float[array.length];
		for (int i = 0; i < array.length; i++) {
			result[i] = array[i].floatValue();
		}
		return result;
	}

	/**
	 * Converts an array of object Floats to primitives handling {@code null}.
	 *
	 * <p>
	 * This method returns {@code null} for a {@code null} input array.
	 * @param array a {@code Float} array, may be {@code null}
	 * @param valueForNull the value to insert if {@code null} found
	 * @return a {@code float} array, {@code null} if null array input
	 */
	public static float[] toPrimitive(Float[] array, float valueForNull) {
		if (array == null) {
			return null;
		}
		else if (array.length == 0) {
			return EMPTY_FLOAT_ARRAY;
		}
		float[] result = new float[array.length];
		for (int i = 0; i < array.length; i++) {
			Float b = array[i];
			result[i] = (b == null ? valueForNull : b.floatValue());
		}
		return result;
	}

	// Int array converters
	// ----------------------------------------------------------------------
	/**
	 * Converts an array of object Integers to primitives.
	 *
	 * <p>
	 * This method returns {@code null} for a {@code null} input array.
	 * @param array a {@code Integer} array, may be {@code null}
	 * @return an {@code int} array, {@code null} if null array input
	 * @throws NullPointerException if array content is {@code null}
	 */
	public static int[] toPrimitive(Integer[] array) {
		if (array == null) {
			return null;
		}
		else if (array.length == 0) {
			return EMPTY_INT_ARRAY;
		}
		int[] result = new int[array.length];
		for (int i = 0; i < array.length; i++) {
			result[i] = array[i].intValue();
		}
		return result;
	}

	/**
	 * Converts an array of object Integer to primitives handling {@code null}.
	 *
	 * <p>
	 * This method returns {@code null} for a {@code null} input array.
	 * @param array a {@code Integer} array, may be {@code null}
	 * @param valueForNull the value to insert if {@code null} found
	 * @return an {@code int} array, {@code null} if null array input
	 */
	public static int[] toPrimitive(Integer[] array, int valueForNull) {
		if (array == null) {
			return null;
		}
		else if (array.length == 0) {
			return EMPTY_INT_ARRAY;
		}
		int[] result = new int[array.length];
		for (int i = 0; i < array.length; i++) {
			Integer b = array[i];
			result[i] = (b == null ? valueForNull : b.intValue());
		}
		return result;
	}

	// Long array converters
	// ----------------------------------------------------------------------
	/**
	 * Converts an array of object Longs to primitives.
	 *
	 * <p>
	 * This method returns {@code null} for a {@code null} input array.
	 * @param array a {@code Long} array, may be {@code null}
	 * @return a {@code long} array, {@code null} if null array input
	 * @throws NullPointerException if array content is {@code null}
	 */
	public static long[] toPrimitive(Long[] array) {
		if (array == null) {
			return null;
		}
		else if (array.length == 0) {
			return EMPTY_LONG_ARRAY;
		}
		long[] result = new long[array.length];
		for (int i = 0; i < array.length; i++) {
			result[i] = array[i].longValue();
		}
		return result;
	}

	/**
	 * Converts an array of object Long to primitives handling {@code null}.
	 *
	 * <p>
	 * This method returns {@code null} for a {@code null} input array.
	 * @param array a {@code Long} array, may be {@code null}
	 * @param valueForNull the value to insert if {@code null} found
	 * @return a {@code long} array, {@code null} if null array input
	 */
	public static long[] toPrimitive(Long[] array, long valueForNull) {
		if (array == null) {
			return null;
		}
		else if (array.length == 0) {
			return EMPTY_LONG_ARRAY;
		}
		long[] result = new long[array.length];
		for (int i = 0; i < array.length; i++) {
			Long b = array[i];
			result[i] = (b == null ? valueForNull : b.longValue());
		}
		return result;
	}

	// Short array converters
	// ----------------------------------------------------------------------
	/**
	 * Converts an array of object Shorts to primitives.
	 *
	 * <p>
	 * This method returns {@code null} for a {@code null} input array.
	 * @param array a {@code Short} array, may be {@code null}
	 * @return a {@code byte} array, {@code null} if null array input
	 * @throws NullPointerException if array content is {@code null}
	 */
	public static short[] toPrimitive(Short[] array) {
		if (array == null) {
			return null;
		}
		else if (array.length == 0) {
			return EMPTY_SHORT_ARRAY;
		}
		short[] result = new short[array.length];
		for (int i = 0; i < array.length; i++) {
			result[i] = array[i].shortValue();
		}
		return result;
	}

	/**
	 * Converts an array of object Short to primitives handling {@code null}.
	 *
	 * <p>
	 * This method returns {@code null} for a {@code null} input array.
	 * @param array a {@code Short} array, may be {@code null}
	 * @param valueForNull the value to insert if {@code null} found
	 * @return a {@code byte} array, {@code null} if null array input
	 */
	public static short[] toPrimitive(Short[] array, short valueForNull) {
		if (array == null) {
			return null;
		}
		else if (array.length == 0) {
			return EMPTY_SHORT_ARRAY;
		}
		short[] result = new short[array.length];
		for (int i = 0; i < array.length; i++) {
			Short b = array[i];
			result[i] = (b == null ? valueForNull : b.shortValue());
		}
		return result;
	}

	// Basic methods handling multi-dimensional arrays
	// -----------------------------------------------------------------------

	/**
	 * Returns an array containing the string representation of each element in the
	 * argument array.
	 *
	 * <p>
	 * This method returns {@code null} for a {@code null} input array.
	 * @param array the {@code Object[]} to be processed, may be null
	 * @return {@code String[]} of the same size as the source with its element's string
	 * representation, {@code null} if null array input
	 * @throws NullPointerException if array contains {@code null}
	 * @since 3.6
	 */
	public static String[] toStringArray(Object[] array) {
		if (array == null) {
			return null;
		}
		else if (array.length == 0) {
			return EMPTY_STRING_ARRAY;
		}

		String[] result = new String[array.length];
		for (int i = 0; i < array.length; i++) {
			result[i] = array[i].toString();
		}

		return result;
	}

	/**
	 * Returns an array containing the string representation of each element in the
	 * argument array handling {@code null} elements.
	 *
	 * <p>
	 * This method returns {@code null} for a {@code null} input array.
	 * @param array the Object[] to be processed, may be null
	 * @param valueForNullElements the value to insert if {@code null} is found
	 * @return a {@code String} array, {@code null} if null array input
	 * @since 3.6
	 */
	public static String[] toStringArray(Object[] array, String valueForNullElements) {
		if (null == array) {
			return null;
		}
		else if (array.length == 0) {
			return EMPTY_STRING_ARRAY;
		}

		String[] result = new String[array.length];
		for (int i = 0; i < array.length; i++) {
			Object object = array[i];
			result[i] = (object == null ? valueForNullElements : object.toString());
		}

		return result;
	}

	/**
	 * ArrayUtils instances should NOT be constructed in standard programming. Instead,
	 * the class should be used as {@code ArrayUtils.clone(new int[] {2})}.
	 *
	 * <p>
	 * This constructor is public to permit tools that require a JavaBean instance to
	 * operate.
	 */
	public ArrayUtils() {
		super();
	}

}
