package org.magneton.core.lang;

import java.util.Iterator;
import java.util.List;
import java.util.Objects;

import javax.annotation.Nullable;

import com.google.common.base.Preconditions;
import org.magneton.core.pool.StringPool;

/**
 * .
 *
 * @author zhangmsh 2022/1/12
 * @since 2.
 */
public class UString {

	/**
	 * Represents a failed index search.
	 */
	public static final int INDEX_NOT_FOUND = -1;

	private static final int STRING_BUILDER_SIZE = 256;

	private UString() {
	}

	/**
	 * <p>
	 * Checks if a CharSequence is empty ("") or null.
	 * </p>
	 *
	 * <pre>
	 * UStrings.isEmpty(null)      = true
	 * UStrings.isEmpty("")        = true
	 * UStrings.isEmpty(" ")       = false
	 * UStrings.isEmpty("bob")     = false
	 * UStrings.isEmpty("  bob  ") = false
	 * </pre>
	 * @param cs the CharSequence to check, may be null
	 * @return {@code true} if the CharSequence is empty or null
	 */
	public static boolean isEmpty(@Nullable CharSequence cs) {
		return cs == null || cs.length() == 0;
	}

	/**
	 * <p>
	 * Checks if all of the CharSequences are empty ("") or null.
	 * </p>
	 *
	 * <pre>
	 * UStrings.isAllEmpty(null)             = true
	 * UStrings.isAllEmpty(null, "")         = true
	 * UStrings.isAllEmpty(new String[] {})  = true
	 * UStrings.isAllEmpty(null, "foo")      = false
	 * UStrings.isAllEmpty("", "bar")        = false
	 * UStrings.isAllEmpty("bob", "")        = false
	 * UStrings.isAllEmpty("  bob  ", null)  = false
	 * UStrings.isAllEmpty(" ", "bar")       = false
	 * UStrings.isAllEmpty("foo", "bar")     = false
	 * </pre>
	 * @param css the CharSequences to check, may be null or empty
	 * @return {@code true} if all of the CharSequences are empty or null
	 */
	public static boolean isAllEmpty(@Nullable CharSequence... css) {
		if (UArray.isEmpty(css)) {
			return true;
		}
		for (CharSequence cs : css) {
			if (!isEmpty(cs)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * <p>
	 * Checks if any of the CharSequences are empty ("") or null.
	 * </p>
	 *
	 * <pre>
	 * UStrings.isAnyEmpty((String) null)    = true
	 * UStrings.isAnyEmpty((String[]) null)  = false
	 * UStrings.isAnyEmpty(null, "foo")      = true
	 * UStrings.isAnyEmpty("", "bar")        = true
	 * UStrings.isAnyEmpty("bob", "")        = true
	 * UStrings.isAnyEmpty("  bob  ", null)  = true
	 * UStrings.isAnyEmpty(" ", "bar")       = false
	 * UStrings.isAnyEmpty("foo", "bar")     = false
	 * UStrings.isAnyEmpty(new String[]{})   = false
	 * UStrings.isAnyEmpty(new String[]{""}) = true
	 * </pre>
	 * @param css the CharSequences to check, may be null or empty
	 * @return {@code true} if any of the CharSequences are empty or null
	 */
	public static boolean isAnyEmpty(@Nullable CharSequence... css) {
		if (UArray.isEmpty(css)) {
			return false;
		}
		for (CharSequence cs : css) {
			if (isEmpty(cs)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * <p>
	 * Checks if a CharSequence is empty (""), null or whitespace only.
	 * </p>
	 *
	 * <p>
	 * Whitespace is defined by {@link Character#isWhitespace(char)}.
	 * </p>
	 *
	 * <pre>
	 * UStrings.isBlank(null)      = true
	 * UStrings.isBlank("")        = true
	 * UStrings.isBlank(" ")       = true
	 * UStrings.isBlank("bob")     = false
	 * UStrings.isBlank("  bob  ") = false
	 * </pre>
	 * @param cs the CharSequence to check, may be null
	 * @return {@code true} if the CharSequence is null, empty or whitespace only
	 */
	public static boolean isBlank(@Nullable CharSequence cs) {
		int strLen = length(cs);
		if (strLen == 0) {
			return true;
		}
		for (int i = 0; i < strLen; i++) {
			if (!Character.isWhitespace(cs.charAt(i))) {
				return false;
			}
		}
		return true;
	}

	/**
	 * <p>
	 * Checks if any of the CharSequences are empty ("") or null or whitespace only.
	 * </p>
	 *
	 * <p>
	 * Whitespace is defined by {@link Character#isWhitespace(char)}.
	 * </p>
	 *
	 * <pre>
	 * UStrings.isAnyBlank((String) null)    = true
	 * UStrings.isAnyBlank((String[]) null)  = false
	 * UStrings.isAnyBlank(null, "foo")      = true
	 * UStrings.isAnyBlank(null, null)       = true
	 * UStrings.isAnyBlank("", "bar")        = true
	 * UStrings.isAnyBlank("bob", "")        = true
	 * UStrings.isAnyBlank("  bob  ", null)  = true
	 * UStrings.isAnyBlank(" ", "bar")       = true
	 * UStrings.isAnyBlank(new String[] {})  = false
	 * UStrings.isAnyBlank(new String[]{""}) = true
	 * UStrings.isAnyBlank("foo", "bar")     = false
	 * </pre>
	 * @param css the CharSequences to check, may be null or empty
	 * @return {@code true} if any of the CharSequences are empty or null or whitespace
	 * only
	 */
	public static boolean isAnyBlank(@Nullable CharSequence... css) {
		if (UArray.isEmpty(css)) {
			return false;
		}
		for (CharSequence cs : css) {
			if (isBlank(cs)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * <p>
	 * Checks if all of the CharSequences are empty (""), null or whitespace only.
	 * </p>
	 *
	 * <p>
	 * Whitespace is defined by {@link Character#isWhitespace(char)}.
	 * </p>
	 *
	 * <pre>
	 * UStrings.isAllBlank(null)             = true
	 * UStrings.isAllBlank(null, "foo")      = false
	 * UStrings.isAllBlank(null, null)       = true
	 * UStrings.isAllBlank("", "bar")        = false
	 * UStrings.isAllBlank("bob", "")        = false
	 * UStrings.isAllBlank("  bob  ", null)  = false
	 * UStrings.isAllBlank(" ", "bar")       = false
	 * UStrings.isAllBlank("foo", "bar")     = false
	 * UStrings.isAllBlank(new String[] {})  = true
	 * </pre>
	 * @param css the CharSequences to check, may be null or empty
	 * @return {@code true} if all of the CharSequences are empty or null or whitespace
	 * only
	 */
	public static boolean isAllBlank(@Nullable CharSequence... css) {
		if (UArray.isEmpty(css)) {
			return true;
		}
		for (CharSequence cs : css) {
			if (!isBlank(cs)) {
				return false;
			}
		}
		return true;
	}

	// ======================= join ===========================
	/**
	 * <p>
	 * Joins the elements of the provided array into a single String containing the
	 * provided list of elements.
	 * </p>
	 *
	 * <p>
	 * No delimiter is added before or after the list. Null objects or empty strings
	 * within the array are represented by empty strings.
	 * </p>
	 *
	 * <pre>
	 * UStrings.join(null, *)               = null
	 * UStrings.join([], *)                 = ""
	 * UStrings.join([null], *)             = ""
	 * UStrings.join([1, 2, 3], ';')  = "1;2;3"
	 * UStrings.join([1, 2, 3], null) = "123"
	 * </pre>
	 * @param array the array of values to join together, may be null
	 * @param separator the separator character to use
	 * @return the joined String, {@code null} if null array input
	 */
	public static String join(@Nullable byte[] array, char separator) {
		if (array == null) {
			return null;
		}
		return join(array, separator, 0, array.length);
	}

	/**
	 * <p>
	 * Joins the elements of the provided array into a single String containing the
	 * provided list of elements.
	 * </p>
	 *
	 * <p>
	 * No delimiter is added before or after the list. Null objects or empty strings
	 * within the array are represented by empty strings.
	 * </p>
	 *
	 * <pre>
	 * UStrings.join(null, *)               = null
	 * UStrings.join([], *)                 = ""
	 * UStrings.join([null], *)             = ""
	 * UStrings.join([1, 2, 3], ';')  = "1;2;3"
	 * UStrings.join([1, 2, 3], null) = "123"
	 * </pre>
	 * @param array the array of values to join together, may be null
	 * @param separator the separator character to use
	 * @param startIndex the first index to start joining from. It is an error to pass in
	 * a start index past the end of the array
	 * @param endIndex the index to stop joining from (exclusive). It is an error to pass
	 * in an end index past the end of the array
	 * @return the joined String, {@code null} if null array input
	 */
	public static String join(@Nullable byte[] array, char separator, int startIndex, int endIndex) {
		if (array == null) {
			return null;
		}
		int noOfItems = endIndex - startIndex;
		if (noOfItems <= 0) {
			return StringPool.EMPTY;
		}
		StringBuilder buf = newStringBuilder(noOfItems);
		buf.append(array[startIndex]);
		for (int i = startIndex + 1; i < endIndex; i++) {
			buf.append(separator);
			buf.append(array[i]);
		}
		return buf.toString();
	}

	/**
	 * <p>
	 * Joins the elements of the provided array into a single String containing the
	 * provided list of elements.
	 * </p>
	 *
	 * <p>
	 * No delimiter is added before or after the list. Null objects or empty strings
	 * within the array are represented by empty strings.
	 * </p>
	 *
	 * <pre>
	 * UStrings.join(null, *)               = null
	 * UStrings.join([], *)                 = ""
	 * UStrings.join([null], *)             = ""
	 * UStrings.join([1, 2, 3], ';')  = "1;2;3"
	 * UStrings.join([1, 2, 3], null) = "123"
	 * </pre>
	 * @param array the array of values to join together, may be null
	 * @param separator the separator character to use
	 * @return the joined String, {@code null} if null array input
	 */
	public static String join(@Nullable char[] array, char separator) {
		if (array == null) {
			return null;
		}
		return join(array, separator, 0, array.length);
	}

	/**
	 * <p>
	 * Joins the elements of the provided array into a single String containing the
	 * provided list of elements.
	 * </p>
	 *
	 * <p>
	 * No delimiter is added before or after the list. Null objects or empty strings
	 * within the array are represented by empty strings.
	 * </p>
	 *
	 * <pre>
	 * UStrings.join(null, *)               = null
	 * UStrings.join([], *)                 = ""
	 * UStrings.join([null], *)             = ""
	 * UStrings.join([1, 2, 3], ';')  = "1;2;3"
	 * UStrings.join([1, 2, 3], null) = "123"
	 * </pre>
	 * @param array the array of values to join together, may be null
	 * @param separator the separator character to use
	 * @param startIndex the first index to start joining from. It is an error to pass in
	 * a start index past the end of the array
	 * @param endIndex the index to stop joining from (exclusive). It is an error to pass
	 * in an end index past the end of the array
	 * @return the joined String, {@code null} if null array input
	 */
	public static String join(@Nullable char[] array, char separator, int startIndex, int endIndex) {
		if (array == null) {
			return null;
		}
		int noOfItems = endIndex - startIndex;
		if (noOfItems <= 0) {
			return StringPool.EMPTY;
		}
		StringBuilder buf = newStringBuilder(noOfItems);
		buf.append(array[startIndex]);
		for (int i = startIndex + 1; i < endIndex; i++) {
			buf.append(separator);
			buf.append(array[i]);
		}
		return buf.toString();
	}

	/**
	 * <p>
	 * Joins the elements of the provided array into a single String containing the
	 * provided list of elements.
	 * </p>
	 *
	 * <p>
	 * No delimiter is added before or after the list. Null objects or empty strings
	 * within the array are represented by empty strings.
	 * </p>
	 *
	 * <pre>
	 * UStrings.join(null, *)               = null
	 * UStrings.join([], *)                 = ""
	 * UStrings.join([null], *)             = ""
	 * UStrings.join([1, 2, 3], ';')  = "1;2;3"
	 * UStrings.join([1, 2, 3], null) = "123"
	 * </pre>
	 * @param array the array of values to join together, may be null
	 * @param separator the separator character to use
	 * @return the joined String, {@code null} if null array input
	 */
	public static String join(@Nullable double[] array, char separator) {
		if (array == null) {
			return null;
		}
		return join(array, separator, 0, array.length);
	}

	/**
	 * <p>
	 * Joins the elements of the provided array into a single String containing the
	 * provided list of elements.
	 * </p>
	 *
	 * <p>
	 * No delimiter is added before or after the list. Null objects or empty strings
	 * within the array are represented by empty strings.
	 * </p>
	 *
	 * <pre>
	 * UStrings.join(null, *)               = null
	 * UStrings.join([], *)                 = ""
	 * UStrings.join([null], *)             = ""
	 * UStrings.join([1, 2, 3], ';')  = "1;2;3"
	 * UStrings.join([1, 2, 3], null) = "123"
	 * </pre>
	 * @param array the array of values to join together, may be null
	 * @param separator the separator character to use
	 * @param startIndex the first index to start joining from. It is an error to pass in
	 * a start index past the end of the array
	 * @param endIndex the index to stop joining from (exclusive). It is an error to pass
	 * in an end index past the end of the array
	 * @return the joined String, {@code null} if null array input
	 */
	public static String join(@Nullable double[] array, char separator, int startIndex, int endIndex) {
		if (array == null) {
			return null;
		}
		int noOfItems = endIndex - startIndex;
		if (noOfItems <= 0) {
			return StringPool.EMPTY;
		}
		StringBuilder buf = newStringBuilder(noOfItems);
		buf.append(array[startIndex]);
		for (int i = startIndex + 1; i < endIndex; i++) {
			buf.append(separator);
			buf.append(array[i]);
		}
		return buf.toString();
	}

	/**
	 * <p>
	 * Joins the elements of the provided array into a single String containing the
	 * provided list of elements.
	 * </p>
	 *
	 * <p>
	 * No delimiter is added before or after the list. Null objects or empty strings
	 * within the array are represented by empty strings.
	 * </p>
	 *
	 * <pre>
	 * UStrings.join(null, *)               = null
	 * UStrings.join([], *)                 = ""
	 * UStrings.join([null], *)             = ""
	 * UStrings.join([1, 2, 3], ';')  = "1;2;3"
	 * UStrings.join([1, 2, 3], null) = "123"
	 * </pre>
	 * @param array the array of values to join together, may be null
	 * @param separator the separator character to use
	 * @return the joined String, {@code null} if null array input
	 */
	public static String join(@Nullable float[] array, char separator) {
		if (array == null) {
			return null;
		}
		return join(array, separator, 0, array.length);
	}

	/**
	 * <p>
	 * Joins the elements of the provided array into a single String containing the
	 * provided list of elements.
	 * </p>
	 *
	 * <p>
	 * No delimiter is added before or after the list. Null objects or empty strings
	 * within the array are represented by empty strings.
	 * </p>
	 *
	 * <pre>
	 * UStrings.join(null, *)               = null
	 * UStrings.join([], *)                 = ""
	 * UStrings.join([null], *)             = ""
	 * UStrings.join([1, 2, 3], ';')  = "1;2;3"
	 * UStrings.join([1, 2, 3], null) = "123"
	 * </pre>
	 * @param array the array of values to join together, may be null
	 * @param separator the separator character to use
	 * @param startIndex the first index to start joining from. It is an error to pass in
	 * a start index past the end of the array
	 * @param endIndex the index to stop joining from (exclusive). It is an error to pass
	 * in an end index past the end of the array
	 * @return the joined String, {@code null} if null array input
	 */
	public static String join(@Nullable float[] array, char separator, int startIndex, int endIndex) {
		if (array == null) {
			return null;
		}
		int noOfItems = endIndex - startIndex;
		if (noOfItems <= 0) {
			return StringPool.EMPTY;
		}
		StringBuilder buf = newStringBuilder(noOfItems);
		buf.append(array[startIndex]);
		for (int i = startIndex + 1; i < endIndex; i++) {
			buf.append(separator);
			buf.append(array[i]);
		}
		return buf.toString();
	}

	/**
	 * <p>
	 * Joins the elements of the provided array into a single String containing the
	 * provided list of elements.
	 * </p>
	 *
	 * <p>
	 * No delimiter is added before or after the list. Null objects or empty strings
	 * within the array are represented by empty strings.
	 * </p>
	 *
	 * <pre>
	 * UStrings.join(null, *)               = null
	 * UStrings.join([], *)                 = ""
	 * UStrings.join([null], *)             = ""
	 * UStrings.join([1, 2, 3], ';')  = "1;2;3"
	 * UStrings.join([1, 2, 3], null) = "123"
	 * </pre>
	 * @param array the array of values to join together, may be null
	 * @param separator the separator character to use
	 * @return the joined String, {@code null} if null array input
	 */
	public static String join(@Nullable int[] array, char separator) {
		if (array == null) {
			return null;
		}
		return join(array, separator, 0, array.length);
	}

	/**
	 * <p>
	 * Joins the elements of the provided array into a single String containing the
	 * provided list of elements.
	 * </p>
	 *
	 * <p>
	 * No delimiter is added before or after the list. Null objects or empty strings
	 * within the array are represented by empty strings.
	 * </p>
	 *
	 * <pre>
	 * UStrings.join(null, *)               = null
	 * UStrings.join([], *)                 = ""
	 * UStrings.join([null], *)             = ""
	 * UStrings.join([1, 2, 3], ';')  = "1;2;3"
	 * UStrings.join([1, 2, 3], null) = "123"
	 * </pre>
	 * @param array the array of values to join together, may be null
	 * @param separator the separator character to use
	 * @param startIndex the first index to start joining from. It is an error to pass in
	 * a start index past the end of the array
	 * @param endIndex the index to stop joining from (exclusive). It is an error to pass
	 * in an end index past the end of the array
	 * @return the joined String, {@code null} if null array input
	 */
	public static String join(@Nullable int[] array, char separator, int startIndex, int endIndex) {
		if (array == null) {
			return null;
		}
		int noOfItems = endIndex - startIndex;
		if (noOfItems <= 0) {
			return StringPool.EMPTY;
		}
		StringBuilder buf = newStringBuilder(noOfItems);
		buf.append(array[startIndex]);
		for (int i = startIndex + 1; i < endIndex; i++) {
			buf.append(separator);
			buf.append(array[i]);
		}
		return buf.toString();
	}

	/**
	 * <p>
	 * Joins the elements of the provided {@code Iterable} into a single String containing
	 * the provided elements.
	 * </p>
	 *
	 * <p>
	 * No delimiter is added before or after the list. Null objects or empty strings
	 * within the iteration are represented by empty strings.
	 * </p>
	 *
	 * <p>
	 * See the examples here: {@link #join(Object[],char)}.
	 * </p>
	 * @param iterable the {@code Iterable} providing the values to join together, may be
	 * null
	 * @param separator the separator character to use
	 * @return the joined String, {@code null} if null iterator input
	 */
	public static String join(@Nullable Iterable<?> iterable, char separator) {
		if (iterable == null) {
			return null;
		}
		return join(iterable.iterator(), separator);
	}

	/**
	 * <p>
	 * Joins the elements of the provided {@code Iterable} into a single String containing
	 * the provided elements.
	 * </p>
	 *
	 * <p>
	 * No delimiter is added before or after the list. A {@code null} separator is the
	 * same as an empty String ("").
	 * </p>
	 *
	 * <p>
	 * See the examples here: {@link #join(Object[],String)}.
	 * </p>
	 * @param iterable the {@code Iterable} providing the values to join together, may be
	 * null
	 * @param separator the separator character to use, null treated as ""
	 * @return the joined String, {@code null} if null iterator input
	 */
	public static String join(@Nullable Iterable<?> iterable, String separator) {
		if (iterable == null) {
			return null;
		}
		return join(iterable.iterator(), separator);
	}

	/**
	 * <p>
	 * Joins the elements of the provided {@code Iterator} into a single String containing
	 * the provided elements.
	 * </p>
	 *
	 * <p>
	 * No delimiter is added before or after the list. Null objects or empty strings
	 * within the iteration are represented by empty strings.
	 * </p>
	 *
	 * <p>
	 * See the examples here: {@link #join(Object[],char)}.
	 * </p>
	 * @param iterator the {@code Iterator} of values to join together, may be null
	 * @param separator the separator character to use
	 * @return the joined String, {@code null} if null iterator input
	 */
	public static String join(@Nullable Iterator<?> iterator, char separator) {

		// handle null, zero and one elements before building a buffer
		if (iterator == null) {
			return null;
		}
		if (!iterator.hasNext()) {
			return StringPool.EMPTY;
		}
		Object first = iterator.next();
		if (!iterator.hasNext()) {
			return Objects.toString(first, StringPool.EMPTY);
		}

		// two or more elements
		StringBuilder buf = new StringBuilder(STRING_BUILDER_SIZE); // Java default
																	// is 16,
																	// probably
																	// too small
		if (first != null) {
			buf.append(first);
		}

		while (iterator.hasNext()) {
			buf.append(separator);
			Object obj = iterator.next();
			if (obj != null) {
				buf.append(obj);
			}
		}

		return buf.toString();
	}

	/**
	 * <p>
	 * Joins the elements of the provided {@code Iterator} into a single String containing
	 * the provided elements.
	 * </p>
	 *
	 * <p>
	 * No delimiter is added before or after the list. A {@code null} separator is the
	 * same as an empty String ("").
	 * </p>
	 *
	 * <p>
	 * See the examples here: {@link #join(Object[],String)}.
	 * </p>
	 * @param iterator the {@code Iterator} of values to join together, may be null
	 * @param separator the separator character to use, null treated as ""
	 * @return the joined String, {@code null} if null iterator input
	 */
	public static String join(@Nullable Iterator<?> iterator, String separator) {

		// handle null, zero and one elements before building a buffer
		if (iterator == null) {
			return null;
		}
		if (!iterator.hasNext()) {
			return StringPool.EMPTY;
		}
		Object first = iterator.next();
		if (!iterator.hasNext()) {
			return Objects.toString(first, "");
		}

		// two or more elements
		StringBuilder buf = new StringBuilder(STRING_BUILDER_SIZE); // Java default
																	// is 16,
																	// probably
																	// too small
		if (first != null) {
			buf.append(first);
		}

		while (iterator.hasNext()) {
			if (separator != null) {
				buf.append(separator);
			}
			Object obj = iterator.next();
			if (obj != null) {
				buf.append(obj);
			}
		}
		return buf.toString();
	}

	/**
	 * <p>
	 * Joins the elements of the provided {@code List} into a single String containing the
	 * provided list of elements.
	 * </p>
	 *
	 * <p>
	 * No delimiter is added before or after the list. Null objects or empty strings
	 * within the array are represented by empty strings.
	 * </p>
	 *
	 * <pre>
	 * UStrings.join(null, *)               = null
	 * UStrings.join([], *)                 = ""
	 * UStrings.join([null], *)             = ""
	 * UStrings.join(["a", "b", "c"], ';')  = "a;b;c"
	 * UStrings.join(["a", "b", "c"], null) = "abc"
	 * UStrings.join([null, "", "a"], ';')  = ";;a"
	 * </pre>
	 * @param list the {@code List} of values to join together, may be null
	 * @param separator the separator character to use
	 * @param startIndex the first index to start joining from. It is an error to pass in
	 * a start index past the end of the list
	 * @param endIndex the index to stop joining from (exclusive). It is an error to pass
	 * in an end index past the end of the list
	 * @return the joined String, {@code null} if null list input
	 */
	public static String join(@Nullable List<?> list, char separator, int startIndex, int endIndex) {
		if (list == null) {
			return null;
		}
		int noOfItems = endIndex - startIndex;
		if (noOfItems <= 0) {
			return StringPool.EMPTY;
		}
		List<?> subList = list.subList(startIndex, endIndex);
		return join(subList.iterator(), separator);
	}

	/**
	 * <p>
	 * Joins the elements of the provided {@code List} into a single String containing the
	 * provided list of elements.
	 * </p>
	 *
	 * <p>
	 * No delimiter is added before or after the list. Null objects or empty strings
	 * within the array are represented by empty strings.
	 * </p>
	 *
	 * <pre>
	 * UStrings.join(null, *)               = null
	 * UStrings.join([], *)                 = ""
	 * UStrings.join([null], *)             = ""
	 * UStrings.join(["a", "b", "c"], ';')  = "a;b;c"
	 * UStrings.join(["a", "b", "c"], null) = "abc"
	 * UStrings.join([null, "", "a"], ';')  = ";;a"
	 * </pre>
	 * @param list the {@code List} of values to join together, may be null
	 * @param separator the separator character to use
	 * @param startIndex the first index to start joining from. It is an error to pass in
	 * a start index past the end of the list
	 * @param endIndex the index to stop joining from (exclusive). It is an error to pass
	 * in an end index past the end of the list
	 * @return the joined String, {@code null} if null list input
	 */
	public static String join(@Nullable List<?> list, String separator, int startIndex, int endIndex) {
		if (list == null) {
			return null;
		}
		int noOfItems = endIndex - startIndex;
		if (noOfItems <= 0) {
			return StringPool.EMPTY;
		}
		List<?> subList = list.subList(startIndex, endIndex);
		return join(subList.iterator(), separator);
	}

	/**
	 * <p>
	 * Joins the elements of the provided array into a single String containing the
	 * provided list of elements.
	 * </p>
	 *
	 * <p>
	 * No delimiter is added before or after the list. Null objects or empty strings
	 * within the array are represented by empty strings.
	 * </p>
	 *
	 * <pre>
	 * UStrings.join(null, *)               = null
	 * UStrings.join([], *)                 = ""
	 * UStrings.join([null], *)             = ""
	 * UStrings.join([1, 2, 3], ';')  = "1;2;3"
	 * UStrings.join([1, 2, 3], null) = "123"
	 * </pre>
	 * @param array the array of values to join together, may be null
	 * @param separator the separator character to use
	 * @return the joined String, {@code null} if null array input
	 */
	public static String join(@Nullable long[] array, char separator) {
		if (array == null) {
			return null;
		}
		return join(array, separator, 0, array.length);
	}

	/**
	 * <p>
	 * Joins the elements of the provided array into a single String containing the
	 * provided list of elements.
	 * </p>
	 *
	 * <p>
	 * No delimiter is added before or after the list. Null objects or empty strings
	 * within the array are represented by empty strings.
	 * </p>
	 *
	 * <pre>
	 * UStrings.join(null, *)               = null
	 * UStrings.join([], *)                 = ""
	 * UStrings.join([null], *)             = ""
	 * UStrings.join([1, 2, 3], ';')  = "1;2;3"
	 * UStrings.join([1, 2, 3], null) = "123"
	 * </pre>
	 * @param array the array of values to join together, may be null
	 * @param separator the separator character to use
	 * @param startIndex the first index to start joining from. It is an error to pass in
	 * a start index past the end of the array
	 * @param endIndex the index to stop joining from (exclusive). It is an error to pass
	 * in an end index past the end of the array
	 * @return the joined String, {@code null} if null array input
	 */
	public static String join(@Nullable long[] array, char separator, int startIndex, int endIndex) {
		if (array == null) {
			return null;
		}
		int noOfItems = endIndex - startIndex;
		if (noOfItems <= 0) {
			return StringPool.EMPTY;
		}
		StringBuilder buf = newStringBuilder(noOfItems);
		buf.append(array[startIndex]);
		for (int i = startIndex + 1; i < endIndex; i++) {
			buf.append(separator);
			buf.append(array[i]);
		}
		return buf.toString();
	}

	/**
	 * <p>
	 * Joins the elements of the provided array into a single String containing the
	 * provided list of elements.
	 * </p>
	 *
	 * <p>
	 * No delimiter is added before or after the list. Null objects or empty strings
	 * within the array are represented by empty strings.
	 * </p>
	 *
	 * <pre>
	 * UStrings.join(null, *)               = null
	 * UStrings.join([], *)                 = ""
	 * UStrings.join([null], *)             = ""
	 * UStrings.join(["a", "b", "c"], ';')  = "a;b;c"
	 * UStrings.join(["a", "b", "c"], null) = "abc"
	 * UStrings.join([null, "", "a"], ';')  = ";;a"
	 * </pre>
	 * @param array the array of values to join together, may be null
	 * @param separator the separator character to use
	 * @return the joined String, {@code null} if null array input
	 */
	public static String join(@Nullable Object[] array, char separator) {
		if (array == null) {
			return null;
		}
		return join(array, separator, 0, array.length);
	}

	/**
	 * <p>
	 * Joins the elements of the provided array into a single String containing the
	 * provided list of elements.
	 * </p>
	 *
	 * <p>
	 * No delimiter is added before or after the list. Null objects or empty strings
	 * within the array are represented by empty strings.
	 * </p>
	 *
	 * <pre>
	 * UStrings.join(null, *)               = null
	 * UStrings.join([], *)                 = ""
	 * UStrings.join([null], *)             = ""
	 * UStrings.join(["a", "b", "c"], ';')  = "a;b;c"
	 * UStrings.join(["a", "b", "c"], null) = "abc"
	 * UStrings.join([null, "", "a"], ';')  = ";;a"
	 * </pre>
	 * @param array the array of values to join together, may be null
	 * @param separator the separator character to use
	 * @param startIndex the first index to start joining from. It is an error to pass in
	 * a start index past the end of the array
	 * @param endIndex the index to stop joining from (exclusive). It is an error to pass
	 * in an end index past the end of the array
	 * @return the joined String, {@code null} if null array input
	 */
	public static String join(@Nullable Object[] array, char separator, int startIndex, int endIndex) {
		if (array == null) {
			return null;
		}
		int noOfItems = endIndex - startIndex;
		if (noOfItems <= 0) {
			return StringPool.EMPTY;
		}
		StringBuilder buf = newStringBuilder(noOfItems);
		if (array[startIndex] != null) {
			buf.append(array[startIndex]);
		}
		for (int i = startIndex + 1; i < endIndex; i++) {
			buf.append(separator);
			if (array[i] != null) {
				buf.append(array[i]);
			}
		}
		return buf.toString();
	}

	/**
	 * <p>
	 * Joins the elements of the provided array into a single String containing the
	 * provided list of elements.
	 * </p>
	 *
	 * <p>
	 * No delimiter is added before or after the list. A {@code null} separator is the
	 * same as an empty String (""). Null objects or empty strings within the array are
	 * represented by empty strings.
	 * </p>
	 *
	 * <pre>
	 * UStrings.join(null, *)                = null
	 * UStrings.join([], *)                  = ""
	 * UStrings.join([null], *)              = ""
	 * UStrings.join(["a", "b", "c"], "--")  = "a--b--c"
	 * UStrings.join(["a", "b", "c"], null)  = "abc"
	 * UStrings.join(["a", "b", "c"], "")    = "abc"
	 * UStrings.join([null, "", "a"], ',')   = ",,a"
	 * </pre>
	 * @param array the array of values to join together, may be null
	 * @param separator the separator character to use, null treated as ""
	 * @return the joined String, {@code null} if null array input
	 */
	public static String join(@Nullable Object[] array, String separator) {
		if (array == null) {
			return null;
		}
		return join(array, separator, 0, array.length);
	}

	/**
	 * <p>
	 * Joins the elements of the provided array into a single String containing the
	 * provided list of elements.
	 * </p>
	 *
	 * <p>
	 * No delimiter is added before or after the list. A {@code null} separator is the
	 * same as an empty String (""). Null objects or empty strings within the array are
	 * represented by empty strings.
	 * </p>
	 *
	 * <pre>
	 * UStrings.join(null, *, *, *)                = null
	 * UStrings.join([], *, *, *)                  = ""
	 * UStrings.join([null], *, *, *)              = ""
	 * UStrings.join(["a", "b", "c"], "--", 0, 3)  = "a--b--c"
	 * UStrings.join(["a", "b", "c"], "--", 1, 3)  = "b--c"
	 * UStrings.join(["a", "b", "c"], "--", 2, 3)  = "c"
	 * UStrings.join(["a", "b", "c"], "--", 2, 2)  = ""
	 * UStrings.join(["a", "b", "c"], null, 0, 3)  = "abc"
	 * UStrings.join(["a", "b", "c"], "", 0, 3)    = "abc"
	 * UStrings.join([null, "", "a"], ',', 0, 3)   = ",,a"
	 * </pre>
	 * @param array the array of values to join together, may be null
	 * @param separator the separator character to use, null treated as ""
	 * @param startIndex the first index to start joining from.
	 * @param endIndex the index to stop joining from (exclusive).
	 * @return the joined String, {@code null} if null array input; or the empty string if
	 * {@code endIndex - startIndex <= 0}. The number of joined entries is given by
	 * {@code endIndex - startIndex}
	 * @throws ArrayIndexOutOfBoundsException ife<br>
	 * {@code startIndex < 0} or <br>
	 * {@code startIndex >= array.length()} or <br>
	 * {@code endIndex < 0} or <br>
	 * {@code endIndex > array.length()}
	 */
	public static String join(@Nullable Object[] array, String separator, int startIndex, int endIndex) {
		if (array == null) {
			return null;
		}
		if (separator == null) {
			separator = StringPool.EMPTY;
		}

		// endIndex - startIndex > 0: Len = NofStrings *(len(firstString) +
		// len(separator))
		// (Assuming that all Strings are roughly equally long)
		int noOfItems = endIndex - startIndex;
		if (noOfItems <= 0) {
			return StringPool.EMPTY;
		}

		StringBuilder buf = newStringBuilder(noOfItems);

		if (array[startIndex] != null) {
			buf.append(array[startIndex]);
		}

		for (int i = startIndex + 1; i < endIndex; i++) {
			buf.append(separator);

			if (array[i] != null) {
				buf.append(array[i]);
			}
		}
		return buf.toString();
	}

	/**
	 * <p>
	 * Joins the elements of the provided array into a single String containing the
	 * provided list of elements.
	 * </p>
	 *
	 * <p>
	 * No delimiter is added before or after the list. Null objects or empty strings
	 * within the array are represented by empty strings.
	 * </p>
	 *
	 * <pre>
	 * UStrings.join(null, *)               = null
	 * UStrings.join([], *)                 = ""
	 * UStrings.join([null], *)             = ""
	 * UStrings.join([1, 2, 3], ';')  = "1;2;3"
	 * UStrings.join([1, 2, 3], null) = "123"
	 * </pre>
	 * @param array the array of values to join together, may be null
	 * @param separator the separator character to use
	 * @return the joined String, {@code null} if null array input
	 */
	public static String join(@Nullable short[] array, char separator) {
		if (array == null) {
			return null;
		}
		return join(array, separator, 0, array.length);
	}

	/**
	 * <p>
	 * Joins the elements of the provided array into a single String containing the
	 * provided list of elements.
	 * </p>
	 *
	 * <p>
	 * No delimiter is added before or after the list. Null objects or empty strings
	 * within the array are represented by empty strings.
	 * </p>
	 *
	 * <pre>
	 * UStrings.join(null, *)               = null
	 * UStrings.join([], *)                 = ""
	 * UStrings.join([null], *)             = ""
	 * UStrings.join([1, 2, 3], ';')  = "1;2;3"
	 * UStrings.join([1, 2, 3], null) = "123"
	 * </pre>
	 * @param array the array of values to join together, may be null
	 * @param separator the separator character to use
	 * @param startIndex the first index to start joining from. It is an error to pass in
	 * a start index past the end of the array
	 * @param endIndex the index to stop joining from (exclusive). It is an error to pass
	 * in an end index past the end of the array
	 * @return the joined String, {@code null} if null array input
	 */
	public static String join(@Nullable short[] array, char separator, int startIndex, int endIndex) {
		if (array == null) {
			return null;
		}
		int noOfItems = endIndex - startIndex;
		if (noOfItems <= 0) {
			return StringPool.EMPTY;
		}
		StringBuilder buf = newStringBuilder(noOfItems);
		buf.append(array[startIndex]);
		for (int i = startIndex + 1; i < endIndex; i++) {
			buf.append(separator);
			buf.append(array[i]);
		}
		return buf.toString();
	}

	// Joining
	// -----------------------------------------------------------------------
	/**
	 * <p>
	 * Joins the elements of the provided array into a single String containing the
	 * provided list of elements.
	 * </p>
	 *
	 * <p>
	 * No separator is added to the joined String. Null objects or empty strings within
	 * the array are represented by empty strings.
	 * </p>
	 *
	 * <pre>
	 * UStrings.join(null)            = null
	 * UStrings.join([])              = ""
	 * UStrings.join([null])          = ""
	 * UStrings.join(["a", "b", "c"]) = "abc"
	 * UStrings.join([null, "", "a"]) = "a"
	 * </pre>
	 * @param <T> the specific type of values to join together
	 * @param elements the values to join together, may be null
	 * @return the joined String, {@code null} if null array input
	 */
	@SafeVarargs
	public static <T> String join(T... elements) {
		return join(elements, null);
	}

	/**
	 * <p>
	 * Joins the elements of the provided varargs into a single String containing the
	 * provided elements.
	 * </p>
	 *
	 * <p>
	 * No delimiter is added before or after the list. {@code null} elements and separator
	 * are treated as empty Strings ("").
	 * </p>
	 *
	 * <pre>
	 * UStrings.joinWith(",", {"a", "b"})        = "a,b"
	 * UStrings.joinWith(",", {"a", "b",""})     = "a,b,"
	 * UStrings.joinWith(",", {"a", null, "b"})  = "a,,b"
	 * UStrings.joinWith(null, {"a", "b"})       = "ab"
	 * </pre>
	 * @param separator the separator character to use, null treated as ""
	 * @param objects the varargs providing the values to join together. {@code null}
	 * elements are treated as ""
	 * @return the joined String.
	 * @throws java.lang.IllegalArgumentException if a null varargs is provided
	 */
	public static String joinWith(String separator, Object... objects) {
		if (objects == null) {
			throw new IllegalArgumentException("Object varargs must not be null");
		}

		String sanitizedSeparator = defaultString(separator);

		StringBuilder result = new StringBuilder();

		Iterator<Object> iterator = java.util.Arrays.asList(objects).iterator();
		while (iterator.hasNext()) {
			String value = Objects.toString(iterator.next(), "");
			result.append(value);

			if (iterator.hasNext()) {
				result.append(sanitizedSeparator);
			}
		}

		return result.toString();
	}

	// =============== contains ====================
	/**
	 * <p>
	 * Checks if CharSequence contains a search CharSequence, handling {@code null}. This
	 * method uses {@link String#indexOf(String)} if possible.
	 * </p>
	 *
	 * <p>
	 * A {@code null} CharSequence will return {@code false}.
	 * </p>
	 *
	 * <pre>
	 * StringUtils.contains(null, *)     = false
	 * StringUtils.contains(*, null)     = false
	 * StringUtils.contains("", "")      = true
	 * StringUtils.contains("abc", "")   = true
	 * StringUtils.contains("abc", "a")  = true
	 * StringUtils.contains("abc", "z")  = false
	 * </pre>
	 * @param seq the CharSequence to check, may be null
	 * @param searchSeq the CharSequence to find, may be null
	 * @return true if the CharSequence contains the search CharSequence, false if not or
	 * {@code null} string input
	 * @since 2.0
	 * @since 3.0 Changed signature from contains(String, String) to
	 * contains(CharSequence, CharSequence)
	 */
	public static boolean contains(@Nullable CharSequence seq, @Nullable CharSequence searchSeq) {
		if (seq == null || searchSeq == null) {
			return false;
		}
		return UCharSequence.indexOf(seq, searchSeq, 0) >= 0;
	}

	// Contains
	// -----------------------------------------------------------------------
	/**
	 * <p>
	 * Checks if CharSequence contains a search character, handling {@code null}. This
	 * method uses {@link String#indexOf(int)} if possible.
	 * </p>
	 *
	 * <p>
	 * A {@code null} or empty ("") CharSequence will return {@code false}.
	 * </p>
	 *
	 * <pre>
	 * StringUtils.contains(null, *)    = false
	 * StringUtils.contains("", *)      = false
	 * StringUtils.contains("abc", 'a') = true
	 * StringUtils.contains("abc", 'z') = false
	 * </pre>
	 * @param seq the CharSequence to check, may be null
	 * @param searchChar the character to find
	 * @return true if the CharSequence contains the search character, false if not or
	 * {@code null} string input
	 * @since 2.0
	 * @since 3.0 Changed signature from contains(String, int) to contains(CharSequence,
	 * int)
	 */
	public static boolean contains(@Nullable CharSequence seq, int searchChar) {
		if (isEmpty(seq)) {
			return false;
		}
		return UCharSequence.indexOf(seq, searchChar, 0) >= 0;
	}

	// ContainsAny
	// -----------------------------------------------------------------------
	/**
	 * <p>
	 * Checks if the CharSequence contains any character in the given set of characters.
	 * </p>
	 *
	 * <p>
	 * A {@code null} CharSequence will return {@code false}. A {@code null} or zero
	 * length search array will return {@code false}.
	 * </p>
	 *
	 * <pre>
	 * StringUtils.containsAny(null, *)                  = false
	 * StringUtils.containsAny("", *)                    = false
	 * StringUtils.containsAny(*, null)                  = false
	 * StringUtils.containsAny(*, [])                    = false
	 * StringUtils.containsAny("zzabyycdxx", ['z', 'a']) = true
	 * StringUtils.containsAny("zzabyycdxx", ['b', 'y']) = true
	 * StringUtils.containsAny("zzabyycdxx", ['z', 'y']) = true
	 * StringUtils.containsAny("aba", ['z'])             = false
	 * </pre>
	 * @param cs the CharSequence to check, may be null
	 * @param searchChars the chars to search for, may be null
	 * @return the {@code true} if any of the chars are found, {@code false} if no match
	 * or null input
	 * @since 2.4
	 * @since 3.0 Changed signature from containsAny(String, char[]) to
	 * containsAny(CharSequence, char...)
	 */
	public static boolean containsAny(@Nullable CharSequence cs, @Nullable char... searchChars) {
		if (isEmpty(cs) || UArray.isEmpty(searchChars)) {
			return false;
		}
		final int csLength = cs.length();
		final int searchLength = searchChars.length;
		final int csLast = csLength - 1;
		final int searchLast = searchLength - 1;
		for (int i = 0; i < csLength; i++) {
			final char ch = cs.charAt(i);
			for (int j = 0; j < searchLength; j++) {
				if (searchChars[j] == ch) {
					if (Character.isHighSurrogate(ch)) {
						if (j == searchLast) {
							// missing low surrogate, fine, like String.indexOf(String)
							return true;
						}
						if (i < csLast && searchChars[j + 1] == cs.charAt(i + 1)) {
							return true;
						}
					}
					else {
						// ch is in the Basic Multilingual Plane
						return true;
					}
				}
			}
		}
		return false;
	}

	/**
	 * <p>
	 * Checks if the CharSequence contains any character in the given set of characters.
	 * </p>
	 *
	 * <p>
	 * A {@code null} CharSequence will return {@code false}. A {@code null} search
	 * CharSequence will return {@code false}.
	 * </p>
	 *
	 * <pre>
	 * StringUtils.containsAny(null, *)               = false
	 * StringUtils.containsAny("", *)                 = false
	 * StringUtils.containsAny(*, null)               = false
	 * StringUtils.containsAny(*, "")                 = false
	 * StringUtils.containsAny("zzabyycdxx", "za")    = true
	 * StringUtils.containsAny("zzabyycdxx", "by")    = true
	 * StringUtils.containsAny("zzabyycdxx", "zy")    = true
	 * StringUtils.containsAny("zzabyycdxx", "\tx")   = true
	 * StringUtils.containsAny("zzabyycdxx", "$.#yF") = true
	 * StringUtils.containsAny("aba", "z")            = false
	 * </pre>
	 * @param cs the CharSequence to check, may be null
	 * @param searchChars the chars to search for, may be null
	 * @return the {@code true} if any of the chars are found, {@code false} if no match
	 * or null input
	 * @since 2.4
	 * @since 3.0 Changed signature from containsAny(String, String) to
	 * containsAny(CharSequence, CharSequence)
	 */
	public static boolean containsAny(@Nullable CharSequence cs, @Nullable CharSequence searchChars) {
		if (searchChars == null) {
			return false;
		}
		return containsAny(cs, UCharSequence.toCharArray(searchChars));
	}

	/**
	 * <p>
	 * Checks if the CharSequence contains any of the CharSequences in the given array.
	 * </p>
	 *
	 * <p>
	 * A {@code null} {@code cs} CharSequence will return {@code false}. A {@code null} or
	 * zero length search array will return {@code false}.
	 * </p>
	 *
	 * <pre>
	 * StringUtils.containsAny(null, *)            = false
	 * StringUtils.containsAny("", *)              = false
	 * StringUtils.containsAny(*, null)            = false
	 * StringUtils.containsAny(*, [])              = false
	 * StringUtils.containsAny("abcd", "ab", null) = true
	 * StringUtils.containsAny("abcd", "ab", "cd") = true
	 * StringUtils.containsAny("abc", "d", "abc")  = true
	 * </pre>
	 * @param cs The CharSequence to check, may be null
	 * @param searchCharSequences The array of CharSequences to search for, may be null.
	 * Individual CharSequences may be null as well.
	 * @return {@code true} if any of the search CharSequences are found, {@code false}
	 * otherwise
	 * @since 3.4
	 */
	public static boolean containsAny(@Nullable CharSequence cs, @Nullable CharSequence... searchCharSequences) {
		if (isEmpty(cs) || UArray.isEmpty(searchCharSequences)) {
			return false;
		}
		for (final CharSequence searchCharSequence : searchCharSequences) {
			if (contains(cs, searchCharSequence)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * <p>
	 * Checks if CharSequence contains a search CharSequence irrespective of case,
	 * handling {@code null}. Case-insensitivity is defined as by
	 * {@link String#equalsIgnoreCase(String)}.
	 *
	 * <p>
	 * A {@code null} CharSequence will return {@code false}.
	 * </p>
	 *
	 * <pre>
	 * StringUtils.containsIgnoreCase(null, *) = false
	 * StringUtils.containsIgnoreCase(*, null) = false
	 * StringUtils.containsIgnoreCase("", "") = true
	 * StringUtils.containsIgnoreCase("abc", "") = true
	 * StringUtils.containsIgnoreCase("abc", "a") = true
	 * StringUtils.containsIgnoreCase("abc", "z") = false
	 * StringUtils.containsIgnoreCase("abc", "A") = true
	 * StringUtils.containsIgnoreCase("abc", "Z") = false
	 * </pre>
	 * @param str the CharSequence to check, may be null
	 * @param searchStr the CharSequence to find, may be null
	 * @return true if the CharSequence contains the search CharSequence irrespective of
	 * case or false if not or {@code null} string input
	 * @since 3.0 Changed signature from containsIgnoreCase(String, String) to
	 * containsIgnoreCase(CharSequence, CharSequence)
	 */
	public static boolean containsIgnoreCase(@Nullable CharSequence str, @Nullable CharSequence searchStr) {
		if (str == null || searchStr == null) {
			return false;
		}
		final int len = searchStr.length();
		final int max = str.length() - len;
		for (int i = 0; i <= max; i++) {
			if (UCharSequence.regionMatches(str, true, i, searchStr, 0, len)) {
				return true;
			}
		}
		return false;
	}

	// ContainsNone
	// -----------------------------------------------------------------------
	/**
	 * <p>
	 * Checks that the CharSequence does not contain certain characters.
	 * </p>
	 *
	 * <p>
	 * A {@code null} CharSequence will return {@code true}. A {@code null} invalid
	 * character array will return {@code true}. An empty CharSequence (length()=0) always
	 * returns true.
	 * </p>
	 *
	 * <pre>
	 * StringUtils.containsNone(null, *)       = true
	 * StringUtils.containsNone(*, null)       = true
	 * StringUtils.containsNone("", *)         = true
	 * StringUtils.containsNone("ab", '')      = true
	 * StringUtils.containsNone("abab", 'xyz') = true
	 * StringUtils.containsNone("ab1", 'xyz')  = true
	 * StringUtils.containsNone("abz", 'xyz')  = false
	 * </pre>
	 * @param cs the CharSequence to check, may be null
	 * @param searchChars an array of invalid chars, may be null
	 * @return true if it contains none of the invalid chars, or is null
	 * @since 2.0
	 * @since 3.0 Changed signature from containsNone(String, char[]) to
	 * containsNone(CharSequence, char...)
	 */
	public static boolean containsNone(@Nullable CharSequence cs, @Nullable char... searchChars) {
		if (cs == null || searchChars == null) {
			return true;
		}
		final int csLen = cs.length();
		final int csLast = csLen - 1;
		final int searchLen = searchChars.length;
		final int searchLast = searchLen - 1;
		for (int i = 0; i < csLen; i++) {
			final char ch = cs.charAt(i);
			for (int j = 0; j < searchLen; j++) {
				if (searchChars[j] == ch) {
					if (Character.isHighSurrogate(ch)) {
						if (j == searchLast) {
							// missing low surrogate, fine, like String.indexOf(String)
							return false;
						}
						if (i < csLast && searchChars[j + 1] == cs.charAt(i + 1)) {
							return false;
						}
					}
					else {
						// ch is in the Basic Multilingual Plane
						return false;
					}
				}
			}
		}
		return true;
	}

	/**
	 * <p>
	 * Checks that the CharSequence does not contain certain characters.
	 * </p>
	 *
	 * <p>
	 * A {@code null} CharSequence will return {@code true}. A {@code null} invalid
	 * character array will return {@code true}. An empty String ("") always returns true.
	 * </p>
	 *
	 * <pre>
	 * StringUtils.containsNone(null, *)       = true
	 * StringUtils.containsNone(*, null)       = true
	 * StringUtils.containsNone("", *)         = true
	 * StringUtils.containsNone("ab", "")      = true
	 * StringUtils.containsNone("abab", "xyz") = true
	 * StringUtils.containsNone("ab1", "xyz")  = true
	 * StringUtils.containsNone("abz", "xyz")  = false
	 * </pre>
	 * @param cs the CharSequence to check, may be null
	 * @param invalidChars a String of invalid chars, may be null
	 * @return true if it contains none of the invalid chars, or is null
	 * @since 2.0
	 * @since 3.0 Changed signature from containsNone(String, String) to
	 * containsNone(CharSequence, String)
	 */
	public static boolean containsNone(@Nullable CharSequence cs, @Nullable String invalidChars) {
		if (cs == null || invalidChars == null) {
			return true;
		}
		return containsNone(cs, invalidChars.toCharArray());
	}

	// ContainsOnly
	// -----------------------------------------------------------------------
	/**
	 * <p>
	 * Checks if the CharSequence contains only certain characters.
	 * </p>
	 *
	 * <p>
	 * A {@code null} CharSequence will return {@code false}. A {@code null} valid
	 * character array will return {@code false}. An empty CharSequence (length()=0)
	 * always returns {@code true}.
	 * </p>
	 *
	 * <pre>
	 * StringUtils.containsOnly(null, *)       = false
	 * StringUtils.containsOnly(*, null)       = false
	 * StringUtils.containsOnly("", *)         = true
	 * StringUtils.containsOnly("ab", '')      = false
	 * StringUtils.containsOnly("abab", 'abc') = true
	 * StringUtils.containsOnly("ab1", 'abc')  = false
	 * StringUtils.containsOnly("abz", 'abc')  = false
	 * </pre>
	 * @param cs the String to check, may be null
	 * @param valid an array of valid chars, may be null
	 * @return true if it only contains valid chars and is non-null
	 * @since 3.0 Changed signature from containsOnly(String, char[]) to
	 * containsOnly(CharSequence, char...)
	 */
	public static boolean containsOnly(@Nullable CharSequence cs, @Nullable char... valid) {
		// All these pre-checks are to maintain API with an older version
		if (valid == null || cs == null) {
			return false;
		}
		if (cs.length() == 0) {
			return true;
		}
		if (valid.length == 0) {
			return false;
		}
		return indexOfAnyBut(cs, valid) == INDEX_NOT_FOUND;
	}

	/**
	 * <p>
	 * Checks if the CharSequence contains only certain characters.
	 * </p>
	 *
	 * <p>
	 * A {@code null} CharSequence will return {@code false}. A {@code null} valid
	 * character String will return {@code false}. An empty String (length()=0) always
	 * returns {@code true}.
	 * </p>
	 *
	 * <pre>
	 * StringUtils.containsOnly(null, *)       = false
	 * StringUtils.containsOnly(*, null)       = false
	 * StringUtils.containsOnly("", *)         = true
	 * StringUtils.containsOnly("ab", "")      = false
	 * StringUtils.containsOnly("abab", "abc") = true
	 * StringUtils.containsOnly("ab1", "abc")  = false
	 * StringUtils.containsOnly("abz", "abc")  = false
	 * </pre>
	 * @param cs the CharSequence to check, may be null
	 * @param validChars a String of valid chars, may be null
	 * @return true if it only contains valid chars and is non-null
	 */
	public static boolean containsOnly(@Nullable CharSequence cs, @Nullable String validChars) {
		if (cs == null || validChars == null) {
			return false;
		}
		return containsOnly(cs, validChars.toCharArray());
	}

	/**
	 * <p>
	 * Check whether the given CharSequence contains any whitespace characters.
	 * </p>
	 *
	 * <p>
	 * Whitespace is defined by {@link Character#isWhitespace(char)}.
	 * </p>
	 * @param seq the CharSequence to check (may be {@code null})
	 * @return {@code true} if the CharSequence is not empty and contains at least 1
	 * (breaking) whitespace character
	 */
	public static boolean containsWhitespace(@Nullable CharSequence seq) {
		if (isEmpty(seq)) {
			return false;
		}
		final int strLen = seq.length();
		for (int i = 0; i < strLen; i++) {
			if (Character.isWhitespace(seq.charAt(i))) {
				return true;
			}
		}
		return false;
	}

	// =============== indexOf =====================
	// IndexOfAnyBut chars
	// -----------------------------------------------------------------------
	/**
	 * <p>
	 * Searches a CharSequence to find the first index of any character not in the given
	 * set of characters.
	 * </p>
	 *
	 * <p>
	 * A {@code null} CharSequence will return {@code -1}. A {@code null} or zero length
	 * search array will return {@code -1}.
	 * </p>
	 *
	 * <pre>
	 * StringUtils.indexOfAnyBut(null, *)                              = -1
	 * StringUtils.indexOfAnyBut("", *)                                = -1
	 * StringUtils.indexOfAnyBut(*, null)                              = -1
	 * StringUtils.indexOfAnyBut(*, [])                                = -1
	 * StringUtils.indexOfAnyBut("zzabyycdxx", new char[] {'z', 'a'} ) = 3
	 * StringUtils.indexOfAnyBut("aba", new char[] {'z'} )             = 0
	 * StringUtils.indexOfAnyBut("aba", new char[] {'a', 'b'} )        = -1

	 * </pre>
	 * @param cs the CharSequence to check, may be null
	 * @param searchChars the chars to search for, may be null
	 * @return the index of any of the chars, -1 if no match or null input
	 * @since 2.0
	 * @since 3.0 Changed signature from indexOfAnyBut(String, char[]) to
	 * indexOfAnyBut(CharSequence, char...)
	 */
	public static int indexOfAnyBut(@Nullable CharSequence cs, @Nullable char... searchChars) {
		if (isEmpty(cs) || UArray.isEmpty(searchChars)) {
			return INDEX_NOT_FOUND;
		}
		final int csLen = cs.length();
		final int csLast = csLen - 1;
		final int searchLen = searchChars.length;
		final int searchLast = searchLen - 1;
		outer: for (int i = 0; i < csLen; i++) {
			final char ch = cs.charAt(i);
			for (int j = 0; j < searchLen; j++) {
				if (searchChars[j] == ch) {
					if (i < csLast && j < searchLast && Character.isHighSurrogate(ch)) {
						if (searchChars[j + 1] == cs.charAt(i + 1)) {
							continue outer;
						}
					}
					else {
						continue outer;
					}
				}
			}
			return i;
		}
		return INDEX_NOT_FOUND;
	}

	/**
	 * <p>
	 * Search a CharSequence to find the first index of any character not in the given set
	 * of characters.
	 * </p>
	 *
	 * <p>
	 * A {@code null} CharSequence will return {@code -1}. A {@code null} or empty search
	 * string will return {@code -1}.
	 * </p>
	 *
	 * <pre>
	 * StringUtils.indexOfAnyBut(null, *)            = -1
	 * StringUtils.indexOfAnyBut("", *)              = -1
	 * StringUtils.indexOfAnyBut(*, null)            = -1
	 * StringUtils.indexOfAnyBut(*, "")              = -1
	 * StringUtils.indexOfAnyBut("zzabyycdxx", "za") = 3
	 * StringUtils.indexOfAnyBut("zzabyycdxx", "")   = -1
	 * StringUtils.indexOfAnyBut("aba", "ab")        = -1
	 * </pre>
	 * @param seq the CharSequence to check, may be null
	 * @param searchChars the chars to search for, may be null
	 * @return the index of any of the chars, -1 if no match or null input
	 * @since 2.0
	 * @since 3.0 Changed signature from indexOfAnyBut(String, String) to
	 * indexOfAnyBut(CharSequence, CharSequence)
	 */
	public static int indexOfAnyBut(@Nullable CharSequence seq, @Nullable CharSequence searchChars) {
		if (isEmpty(seq) || isEmpty(searchChars)) {
			return INDEX_NOT_FOUND;
		}
		final int strLen = seq.length();
		for (int i = 0; i < strLen; i++) {
			final char ch = seq.charAt(i);
			final boolean chFound = UCharSequence.indexOf(searchChars, ch, 0) >= 0;
			if (i + 1 < strLen && Character.isHighSurrogate(ch)) {
				final char ch2 = seq.charAt(i + 1);
				if (chFound && UCharSequence.indexOf(searchChars, ch2, 0) < 0) {
					return i;
				}
			}
			else {
				if (!chFound) {
					return i;
				}
			}
		}
		return INDEX_NOT_FOUND;
	}

	/**
	 * <p>
	 * Compares all CharSequences in an array and returns the index at which the
	 * CharSequences begin to differ.
	 * </p>
	 *
	 * <p>
	 * For example, {@code indexOfDifference(new String[] {"i am a machine", "i am a
	 * robot"}) -> 7}
	 * </p>
	 *
	 * <pre>
	 * StringUtils.indexOfDifference(null) = -1
	 * StringUtils.indexOfDifference(new String[] {}) = -1
	 * StringUtils.indexOfDifference(new String[] {"abc"}) = -1
	 * StringUtils.indexOfDifference(new String[] {null, null}) = -1
	 * StringUtils.indexOfDifference(new String[] {"", ""}) = -1
	 * StringUtils.indexOfDifference(new String[] {"", null}) = 0
	 * StringUtils.indexOfDifference(new String[] {"abc", null, null}) = 0
	 * StringUtils.indexOfDifference(new String[] {null, null, "abc"}) = 0
	 * StringUtils.indexOfDifference(new String[] {"", "abc"}) = 0
	 * StringUtils.indexOfDifference(new String[] {"abc", ""}) = 0
	 * StringUtils.indexOfDifference(new String[] {"abc", "abc"}) = -1
	 * StringUtils.indexOfDifference(new String[] {"abc", "a"}) = 1
	 * StringUtils.indexOfDifference(new String[] {"ab", "abxyz"}) = 2
	 * StringUtils.indexOfDifference(new String[] {"abcde", "abxyz"}) = 2
	 * StringUtils.indexOfDifference(new String[] {"abcde", "xyz"}) = 0
	 * StringUtils.indexOfDifference(new String[] {"xyz", "abcde"}) = 0
	 * StringUtils.indexOfDifference(new String[] {"i am a machine", "i am a robot"}) = 7
	 * </pre>
	 * @param css array of CharSequences, entries may be null
	 * @return the index where the strings begin to differ; -1 if they are all equal
	 * @since 2.4
	 * @since 3.0 Changed signature from indexOfDifference(String...) to
	 * indexOfDifference(CharSequence...)
	 */
	public static int indexOfDifference(@Nullable CharSequence... css) {
		if (UArray.length(css) <= 1) {
			return INDEX_NOT_FOUND;
		}
		boolean anyStringNull = false;
		boolean allStringsNull = true;
		final int arrayLen = css.length;
		int shortestStrLen = Integer.MAX_VALUE;
		int longestStrLen = 0;

		// find the min and max string lengths; this avoids checking to make
		// sure we are not exceeding the length of the string each time through
		// the bottom loop.
		for (final CharSequence cs : css) {
			if (cs == null) {
				anyStringNull = true;
				shortestStrLen = 0;
			}
			else {
				allStringsNull = false;
				shortestStrLen = Math.min(cs.length(), shortestStrLen);
				longestStrLen = Math.max(cs.length(), longestStrLen);
			}
		}

		// handle lists containing all nulls or all empty strings
		if (allStringsNull || longestStrLen == 0 && !anyStringNull) {
			return INDEX_NOT_FOUND;
		}

		// handle lists containing some nulls or some empty strings
		if (shortestStrLen == 0) {
			return 0;
		}

		// find the position with the first difference across all strings
		int firstDiff = -1;
		for (int stringPos = 0; stringPos < shortestStrLen; stringPos++) {
			final char comparisonChar = css[0].charAt(stringPos);
			for (int arrayPos = 1; arrayPos < arrayLen; arrayPos++) {
				if (css[arrayPos].charAt(stringPos) != comparisonChar) {
					firstDiff = stringPos;
					break;
				}
			}
			if (firstDiff != -1) {
				break;
			}
		}

		if (firstDiff == -1 && shortestStrLen != longestStrLen) {
			// we compared all of the characters up to the length of the
			// shortest string and didn't find a match, but the string lengths
			// vary, so return the length of the shortest string.
			return shortestStrLen;
		}
		return firstDiff;
	}

	/**
	 * <p>
	 * Compares two CharSequences, and returns the index at which the CharSequences begin
	 * to differ.
	 * </p>
	 *
	 * <p>
	 * For example, {@code indexOfDifference("i am a machine", "i am a robot") -> 7}
	 * </p>
	 *
	 * <pre>
	 * StringUtils.indexOfDifference(null, null) = -1
	 * StringUtils.indexOfDifference("", "") = -1
	 * StringUtils.indexOfDifference("", "abc") = 0
	 * StringUtils.indexOfDifference("abc", "") = 0
	 * StringUtils.indexOfDifference("abc", "abc") = -1
	 * StringUtils.indexOfDifference("ab", "abxyz") = 2
	 * StringUtils.indexOfDifference("abcde", "abxyz") = 2
	 * StringUtils.indexOfDifference("abcde", "xyz") = 0
	 * </pre>
	 * @param cs1 the first CharSequence, may be null
	 * @param cs2 the second CharSequence, may be null
	 * @return the index where cs1 and cs2 begin to differ; -1 if they are equal
	 * @since 2.0
	 * @since 3.0 Changed signature from indexOfDifference(String, String) to
	 * indexOfDifference(CharSequence, CharSequence)
	 */
	public static int indexOfDifference(@Nullable CharSequence cs1, @Nullable CharSequence cs2) {
		if (cs1 == cs2) {
			return INDEX_NOT_FOUND;
		}
		if (cs1 == null || cs2 == null) {
			return 0;
		}
		int i;
		for (i = 0; i < cs1.length() && i < cs2.length(); ++i) {
			if (cs1.charAt(i) != cs2.charAt(i)) {
				break;
			}
		}
		if (i < cs2.length() || i < cs1.length()) {
			return i;
		}
		return INDEX_NOT_FOUND;
	}

	/**
	 * <p>
	 * Case in-sensitive find of the first index within a CharSequence.
	 * </p>
	 *
	 * <p>
	 * A {@code null} CharSequence will return {@code -1}. A negative start position is
	 * treated as zero. An empty ("") search CharSequence always matches. A start position
	 * greater than the string length only matches an empty search CharSequence.
	 * </p>
	 *
	 * <pre>
	 * StringUtils.indexOfIgnoreCase(null, *)          = -1
	 * StringUtils.indexOfIgnoreCase(*, null)          = -1
	 * StringUtils.indexOfIgnoreCase("", "")           = 0
	 * StringUtils.indexOfIgnoreCase("aabaabaa", "a")  = 0
	 * StringUtils.indexOfIgnoreCase("aabaabaa", "b")  = 2
	 * StringUtils.indexOfIgnoreCase("aabaabaa", "ab") = 1
	 * </pre>
	 * @param str the CharSequence to check, may be null
	 * @param searchStr the CharSequence to find, may be null
	 * @return the first index of the search CharSequence, -1 if no match or {@code null}
	 * string input
	 * @since 2.5
	 * @since 3.0 Changed signature from indexOfIgnoreCase(String, String) to
	 * indexOfIgnoreCase(CharSequence, CharSequence)
	 */
	public static int indexOfIgnoreCase(@Nullable CharSequence str, @Nullable CharSequence searchStr) {
		return indexOfIgnoreCase(str, searchStr, 0);
	}

	/**
	 * <p>
	 * Case in-sensitive find of the first index within a CharSequence from the specified
	 * position.
	 * </p>
	 *
	 * <p>
	 * A {@code null} CharSequence will return {@code -1}. A negative start position is
	 * treated as zero. An empty ("") search CharSequence always matches. A start position
	 * greater than the string length only matches an empty search CharSequence.
	 * </p>
	 *
	 * <pre>
	 * StringUtils.indexOfIgnoreCase(null, *, *)          = -1
	 * StringUtils.indexOfIgnoreCase(*, null, *)          = -1
	 * StringUtils.indexOfIgnoreCase("", "", 0)           = 0
	 * StringUtils.indexOfIgnoreCase("aabaabaa", "A", 0)  = 0
	 * StringUtils.indexOfIgnoreCase("aabaabaa", "B", 0)  = 2
	 * StringUtils.indexOfIgnoreCase("aabaabaa", "AB", 0) = 1
	 * StringUtils.indexOfIgnoreCase("aabaabaa", "B", 3)  = 5
	 * StringUtils.indexOfIgnoreCase("aabaabaa", "B", 9)  = -1
	 * StringUtils.indexOfIgnoreCase("aabaabaa", "B", -1) = 2
	 * StringUtils.indexOfIgnoreCase("aabaabaa", "", 2)   = 2
	 * StringUtils.indexOfIgnoreCase("abc", "", 9)        = -1
	 * </pre>
	 * @param str the CharSequence to check, may be null
	 * @param searchStr the CharSequence to find, may be null
	 * @param startPos the start position, negative treated as zero
	 * @return the first index of the search CharSequence (always &ge; startPos), -1 if no
	 * match or {@code null} string input
	 * @since 2.5
	 * @since 3.0 Changed signature from indexOfIgnoreCase(String, String, int) to
	 * indexOfIgnoreCase(CharSequence, CharSequence, int)
	 */
	public static int indexOfIgnoreCase(@Nullable CharSequence str, @Nullable CharSequence searchStr, int startPos) {
		if (str == null || searchStr == null) {
			return INDEX_NOT_FOUND;
		}
		if (startPos < 0) {
			startPos = 0;
		}
		final int endLimit = str.length() - searchStr.length() + 1;
		if (startPos > endLimit) {
			return INDEX_NOT_FOUND;
		}
		if (searchStr.length() == 0) {
			return startPos;
		}
		for (int i = startPos; i < endLimit; i++) {
			if (UCharSequence.regionMatches(str, true, i, searchStr, 0, searchStr.length())) {
				return i;
			}
		}
		return INDEX_NOT_FOUND;
	}

	// =============== default ======================
	/**
	 * <p>
	 * Returns either the passed in CharSequence, or if the CharSequence is whitespace,
	 * empty ("") or {@code null}, the value of {@code defaultStr}.
	 * </p>
	 *
	 * <p>
	 * Whitespace is defined by {@link Character#isWhitespace(char)}.
	 * </p>
	 *
	 * <pre>
	 * UStrings.defaultIfBlank(null, "NULL")  = "NULL"
	 * UStrings.defaultIfBlank("", "NULL")    = "NULL"
	 * UStrings.defaultIfBlank(" ", "NULL")   = "NULL"
	 * UStrings.defaultIfBlank("bat", "NULL") = "bat"
	 * UStrings.defaultIfBlank("", null)      = null
	 * </pre>
	 * @param <T> the specific kind of CharSequence
	 * @param str the CharSequence to check, may be null
	 * @param defaultStr the default CharSequence to return if the input is whitespace,
	 * empty ("") or {@code null}, may be null
	 * @return the passed in CharSequence, or the default
	 * @see UString#defaultString(String, String)
	 */
	public static <T extends CharSequence> T defaultIfBlank(@Nullable T str, T defaultStr) {
		return isBlank(str) ? defaultStr : str;
	}

	/**
	 * <p>
	 * Returns either the passed in CharSequence, or if the CharSequence is empty or
	 * {@code null}, the value of {@code defaultStr}.
	 * </p>
	 *
	 * <pre>
	 * UStrings.defaultIfEmpty(null, "NULL")  = "NULL"
	 * UStrings.defaultIfEmpty("", "NULL")    = "NULL"
	 * UStrings.defaultIfEmpty(" ", "NULL")   = " "
	 * UStrings.defaultIfEmpty("bat", "NULL") = "bat"
	 * UStrings.defaultIfEmpty("", null)      = null
	 * </pre>
	 * @param <T> the specific kind of CharSequence
	 * @param str the CharSequence to check, may be null
	 * @param defaultStr the default CharSequence to return if the input is empty ("") or
	 * {@code null}, may be null
	 * @return the passed in CharSequence, or the default
	 * @see UString#defaultString(String, String)
	 */
	public static <T extends CharSequence> T defaultIfEmpty(@Nullable T str, T defaultStr) {
		return isEmpty(str) ? defaultStr : str;
	}

	/**
	 * <p>
	 * Returns either the passed in String, or if the String is {@code null}, an empty
	 * String ("").
	 * </p>
	 *
	 * <pre>
	 * UStrings.defaultString(null)  = ""
	 * UStrings.defaultString("")    = ""
	 * UStrings.defaultString("bat") = "bat"
	 * </pre>
	 *
	 * @see String#valueOf(Object)
	 * @param str the String to check, may be null
	 * @return the passed in String, or the empty String if it was {@code null}
	 */
	public static String defaultString(String str) {
		return defaultString(str, StringPool.EMPTY);
	}

	/**
	 * <p>
	 * Returns either the passed in String, or if the String is {@code null}, the value of
	 * {@code defaultStr}.
	 * </p>
	 *
	 * <pre>
	 * UStrings.defaultString(null, "NULL")  = "NULL"
	 * UStrings.defaultString("", "NULL")    = ""
	 * UStrings.defaultString("bat", "NULL") = "bat"
	 * </pre>
	 * @param str the String to check, may be null
	 * @param defaultStr the default String to return if the input is {@code null}, may be
	 * null
	 * @return the passed in String, or the default if it was {@code null}
	 */
	public static String defaultString(String str, String defaultStr) {
		return str == null ? defaultStr : str;
	}

	// =========== format ===============
	/**
	 * Returns the given {@code template} string with each occurrence of {@code "%s"}
	 * replaced with the corresponding argument value from {@code args}; or, if the
	 * placeholder and argument counts do not match, returns a best-effort form of that
	 * string. Will not throw an exception under normal conditions.
	 *
	 * <p>
	 * <b>Note:</b> For most string-formatting needs, use {@link String#format
	 * String.format}, {@link java.io.PrintWriter#format PrintWriter.format}, and related
	 * methods. These support the full range of <a href=
	 * "https://docs.oracle.com/javase/9/docs/api/java/util/Formatter.html#syntax">format
	 * specifiers</a>, and alert you to usage errors by throwing
	 * {@link java.util.IllegalFormatException}.
	 *
	 * <p>
	 * In certain cases, such as outputting debugging information or constructing a
	 * message to be used for another unchecked exception, an exception during string
	 * formatting would serve little purpose except to supplant the real information you
	 * were trying to provide. These are the cases this method is made for; it instead
	 * generates a best-effort string with all supplied argument values present. This
	 * method is also useful in environments such as GWT where {@code
	 * String.format} is not available. As an example, method implementations of the
	 * {@link Preconditions} class use this formatter, for both of the reasons just
	 * discussed.
	 *
	 * <p>
	 * <b>Warning:</b> Only the exact two-character placeholder sequence {@code "%s"} is
	 * recognized.
	 * @param template a string containing zero or more {@code "%s"} placeholder
	 * sequences. {@code null} is treated as the four-character string {@code "null"}.
	 * @param args the arguments to be substituted into the message template. The first
	 * argument specified is substituted for the first occurrence of {@code "%s"} in the
	 * template, and so forth. A {@code null} argument is converted to the four-character
	 * string {@code "null"}; non-null values are converted to strings using
	 * {@link Object#toString()}.
	 */
	// TODO(diamondm) consider using Arrays.toString() for array parameters
	public static String lenientFormat(@Nullable String template, @Nullable Object... args) {
		return UStringFormatter.lenientFormat(template, args);
	}

	/**
	 * Returns the given {@code template} string with each occurrence of
	 * {@code "%s0","%s1" or "%sx"} replaced with the corresponding argument value from
	 * {@code args}; or if the placeholder and argument count do not match, keep the
	 * placeholder of that string.
	 *
	 * <pre>eg: {@code
	 * UStrings.format("hai %s0", "zhangsan") return "hai zhangsan"
	 * UStrings.format("hai %s0, I'm %s1", "zhangsan", "lisi") return "hia zhangsan, I'm lisi"
	 * UStrings.format("hai %s0, I'm %s1,%s1", "zhangsan","lisi") return "hai zhangsan, I'm lisi,lisi"
	 * UStrings.format("hai %s0, I'm %s1", "zhangsan") return "hai zhangsan, I'm %s1"
	 *
	 * }</pre>
	 * @param template a string containing zero or more {@code "%s0"} placeholder
	 * sequences.
	 * @param args the arguments to be substituted into the message template. The first
	 * argument specified is substituted for the occurrence of {@code "%s0"} in the
	 * template, and so forth. A {@code null} argement is keep the placeholder.
	 */
	public static String indexedFormat(@Nullable String template, @Nullable Object... args) {
		return UStringFormatter.indexedFormat(template, args);
	}

	// ============ other ===============
	/**
	 * Gets a CharSequence length or {@code 0} if the CharSequence is {@code null}.
	 * @param cs a CharSequence or {@code null}
	 * @return CharSequence length or {@code 0} if the CharSequence is {@code null}.
	 */
	public static int length(@Nullable CharSequence cs) {
		return cs == null ? 0 : cs.length();
	}

	private static StringBuilder newStringBuilder(int noOfItems) {
		return new StringBuilder(noOfItems * 16);
	}

}
