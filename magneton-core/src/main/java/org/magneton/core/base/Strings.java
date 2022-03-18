/*
 * Copyright (C) 2010 The Guava Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package org.magneton.core.base;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.annotation.CheckForNull;
import javax.annotation.Nullable;
import javax.annotations.VisibleForTesting;

import static java.util.logging.Level.WARNING;

/**
 * Static utility methods pertaining to {@code String} or {@code CharSequence} instances.
 *
 * @author Kevin Bourrillion
 * @since 3.0
 */
@ElementTypesAreNonnullByDefault
public final class Strings {

	private Strings() {
	}

	/**
	 * Returns the given string if it is non-null; the empty string otherwise.
	 * @param string the string to test and possibly return
	 * @return {@code string} itself if it is non-null; {@code ""} if it is null
	 */
	public static String nullToEmpty(@CheckForNull String string) {
		return Platform.nullToEmpty(string);
	}

	/**
	 * <p>
	 * Returns either the passed in CharSequence, or if the CharSequence is empty or
	 * {@code null}, the value of {@code defaultStr}.
	 * </p>
	 *
	 * <pre>
	 * StringUtils.defaultIfEmpty(null, "NULL")  = "NULL"
	 * StringUtils.defaultIfEmpty("", "NULL")    = "NULL"
	 * StringUtils.defaultIfEmpty(" ", "NULL")   = " "
	 * StringUtils.defaultIfEmpty("bat", "NULL") = "bat"
	 * StringUtils.defaultIfEmpty("", null)      = null
	 * </pre>
	 * @param str the CharSequence to check, may be null
	 * @param defaultStr the default CharSequence to return if the input is empty ("") or
	 * {@code null}, may be null
	 * @return the passed in CharSequence, or the default
	 */
	public static String defaultIfNullOrEmpty(String str, String defaultStr) {
		return isNullOrEmpty(str) ? defaultStr : str;
	}

	/**
	 * Returns the given string if it is nonempty; {@code null} otherwise.
	 * @param string the string to test and possibly return
	 * @return {@code string} itself if it is nonempty; {@code null} if it is empty or
	 * null
	 */
	@CheckForNull
	public static String emptyToNull(@CheckForNull String string) {
		return Platform.emptyToNull(string);
	}

	/**
	 * Returns {@code true} if the given string is null or is the empty string.
	 *
	 * <p>
	 * Consider normalizing your string references with {@link #nullToEmpty}. If you do,
	 * you can use {@link String#isEmpty()} instead of this method, and you won't need
	 * special null-safe forms of methods like {@link String#toUpperCase} either. Or, if
	 * you'd like to normalize "in the other direction," converting empty strings to
	 * {@code null}, you can use {@link #emptyToNull}.
	 * @param string a string reference to check
	 * @return {@code true} if the string is null or is the empty string
	 */
	public static boolean isNullOrEmpty(@CheckForNull String string) {
		return Platform.stringIsNullOrEmpty(string);
	}

	/**
	 * Check that the given {@code CharSequence} is neither {@code null} nor of length 0.
	 * <p>
	 * Note: this method returns {@code true} for a {@code CharSequence} that purely
	 * consists of whitespace.
	 * <p>
	 * <pre class="code">
	 * StringUtils.hasLength(null) = false
	 * StringUtils.hasLength("") = false
	 * StringUtils.hasLength(" ") = true
	 * StringUtils.hasLength("Hello") = true
	 * </pre>
	 * @param str the {@code CharSequence} to check (may be {@code null})
	 * @return {@code true} if the {@code CharSequence} is not {@code null} and has length
	 * @see #hasLength(String)
	 */
	public static boolean hasLength(@Nullable CharSequence str) {
		return (str != null && str.length() > 0);
	}

	/**
	 * Check that the given {@code String} is neither {@code null} nor of length 0.
	 * <p>
	 * Note: this method returns {@code true} for a {@code String} that purely consists of
	 * whitespace.
	 * @param str the {@code String} to check (may be {@code null})
	 * @return {@code true} if the {@code String} is not {@code null} and has length
	 * @see #hasLength(CharSequence)
	 */
	public static boolean hasLength(@Nullable String str) {
		return (str != null && !str.isEmpty());
	}

	/**
	 * Returns a string, of length at least {@code minLength}, consisting of
	 * {@code string} prepended with as many copies of {@code padChar} as are necessary to
	 * reach that length. For example,
	 *
	 * <ul>
	 * <li>{@code padStart("7", 3, '0')} returns {@code "007"}
	 * <li>{@code padStart("2010", 3, '0')} returns {@code "2010"}
	 * </ul>
	 *
	 * <p>
	 * See {@link java.util.Formatter} for a richer set of formatting capabilities.
	 * @param string the string which should appear at the end of the result
	 * @param minLength the minimum length the resulting string must have. Can be zero or
	 * negative, in which case the input string is always returned.
	 * @param padChar the character to insert at the beginning of the result until the
	 * minimum length is reached
	 * @return the padded string
	 */
	public static String padStart(String string, int minLength, char padChar) {
		Preconditions.checkNotNull(string); // eager for GWT.
		if (string.length() >= minLength) {
			return string;
		}
		StringBuilder sb = new StringBuilder(minLength);
		for (int i = string.length(); i < minLength; i++) {
			sb.append(padChar);
		}
		sb.append(string);
		return sb.toString();
	}

	/**
	 * Returns a string, of length at least {@code minLength}, consisting of
	 * {@code string} appended with as many copies of {@code padChar} as are necessary to
	 * reach that length. For example,
	 *
	 * <ul>
	 * <li>{@code padEnd("4.", 5, '0')} returns {@code "4.000"}
	 * <li>{@code padEnd("2010", 3, '!')} returns {@code "2010"}
	 * </ul>
	 *
	 * <p>
	 * See {@link java.util.Formatter} for a richer set of formatting capabilities.
	 * @param string the string which should appear at the beginning of the result
	 * @param minLength the minimum length the resulting string must have. Can be zero or
	 * negative, in which case the input string is always returned.
	 * @param padChar the character to append to the end of the result until the minimum
	 * length is reached
	 * @return the padded string
	 */
	public static String padEnd(String string, int minLength, char padChar) {
		Preconditions.checkNotNull(string); // eager for GWT.
		if (string.length() >= minLength) {
			return string;
		}
		StringBuilder sb = new StringBuilder(minLength);
		sb.append(string);
		for (int i = string.length(); i < minLength; i++) {
			sb.append(padChar);
		}
		return sb.toString();
	}

	/**
	 * Returns a string consisting of a specific number of concatenated copies of an input
	 * string. For example, {@code repeat("hey", 3)} returns the string
	 * {@code "heyheyhey"}.
	 *
	 * <p>
	 * <b>Java 11+ users:</b> use {@code string.repeat(count)} instead.
	 * @param string any non-null string
	 * @param count the number of times to repeat it; a nonnegative integer
	 * @return a string containing {@code string} repeated {@code count} times (the empty
	 * string if {@code count} is zero)
	 * @throws IllegalArgumentException if {@code count} is negative
	 */
	public static String repeat(String string, int count) {
		Preconditions.checkNotNull(string); // eager for GWT.

		if (count <= 1) {
			Preconditions.checkArgument(count >= 0, "invalid count: %s", count);
			return (count == 0) ? "" : string;
		}

		// IF YOU MODIFY THE CODE HERE, you must update StringsRepeatBenchmark
		int len = string.length();
		long longSize = (long) len * (long) count;
		int size = (int) longSize;
		if (size != longSize) {
			throw new ArrayIndexOutOfBoundsException("Required array size too large: " + longSize);
		}

		char[] array = new char[size];
		string.getChars(0, len, array, 0);
		int n;
		for (n = len; n < size - n; n <<= 1) {
			System.arraycopy(array, 0, array, n, n);
		}
		System.arraycopy(array, 0, array, n, size - n);
		return new String(array);
	}

	/**
	 * Returns the longest string {@code prefix} such that
	 * {@code a.toString().startsWith(prefix) &&
	 * b.toString().startsWith(prefix)}, taking care not to split surrogate pairs. If
	 * {@code a} and {@code b} have no common prefix, returns the empty string.
	 *
	 * @since 11.0
	 */
	public static String commonPrefix(CharSequence a, CharSequence b) {
		Preconditions.checkNotNull(a);
		Preconditions.checkNotNull(b);

		int maxPrefixLength = Math.min(a.length(), b.length());
		int p = 0;
		while (p < maxPrefixLength && a.charAt(p) == b.charAt(p)) {
			p++;
		}
		if (validSurrogatePairAt(a, p - 1) || validSurrogatePairAt(b, p - 1)) {
			p--;
		}
		return a.subSequence(0, p).toString();
	}

	/**
	 * Returns the longest string {@code suffix} such that
	 * {@code a.toString().endsWith(suffix) &&
	 * b.toString().endsWith(suffix)}, taking care not to split surrogate pairs. If
	 * {@code a} and {@code b} have no common suffix, returns the empty string.
	 *
	 * @since 11.0
	 */
	public static String commonSuffix(CharSequence a, CharSequence b) {
		Preconditions.checkNotNull(a);
		Preconditions.checkNotNull(b);

		int maxSuffixLength = Math.min(a.length(), b.length());
		int s = 0;
		while (s < maxSuffixLength && a.charAt(a.length() - s - 1) == b.charAt(b.length() - s - 1)) {
			s++;
		}
		if (validSurrogatePairAt(a, a.length() - s - 1) || validSurrogatePairAt(b, b.length() - s - 1)) {
			s--;
		}
		return a.subSequence(a.length() - s, a.length()).toString();
	}

	/**
	 * True when a valid surrogate pair starts at the given {@code index} in the given
	 * {@code string}. Out-of-range indexes return false.
	 */
	@VisibleForTesting
	static boolean validSurrogatePairAt(CharSequence string, int index) {
		return index >= 0 && index <= (string.length() - 2) && Character.isHighSurrogate(string.charAt(index))
				&& Character.isLowSurrogate(string.charAt(index + 1));
	}

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
	 * sequences. {@code
	 *     null} is treated as the four-character string {@code "null"}.
	 * @param args the arguments to be substituted into the message template. The first
	 * argument specified is substituted for the first occurrence of {@code "%s"} in the
	 * template, and so forth. A {@code null} argument is converted to the four-character
	 * string {@code "null"}; non-null values are converted to strings using
	 * {@link Object#toString()}.
	 * @since 25.1
	 */
	// TODO(diamondm) consider using Arrays.toString() for array parameters
	public static String lenientFormat(@CheckForNull String template, @CheckForNull @Nullable Object... args) {
		template = String.valueOf(template); // null -> "null"

		if (args == null) {
			args = new Object[] { "(Object[])null" };
		}
		else {
			for (int i = 0; i < args.length; i++) {
				args[i] = lenientToString(args[i]);
			}
		}

		// start substituting the arguments into the '%s' placeholders
		StringBuilder builder = new StringBuilder(template.length() + 16 * args.length);
		int templateStart = 0;
		int i = 0;
		while (i < args.length) {
			int placeholderStart = template.indexOf("%s", templateStart);
			if (placeholderStart == -1) {
				break;
			}
			builder.append(template, templateStart, placeholderStart);
			builder.append(args[i++]);
			templateStart = placeholderStart + 2;
		}
		builder.append(template, templateStart, template.length());

		// if we run out of placeholders, append the extra args in square braces
		if (i < args.length) {
			builder.append(" [");
			builder.append(args[i++]);
			while (i < args.length) {
				builder.append(", ");
				builder.append(args[i++]);
			}
			builder.append(']');
		}

		return builder.toString();
	}

	/**
	 * Capitalize a {@code String}, changing the first letter to upper case as per
	 * {@link Character#toUpperCase(char)}. No other letters are changed.
	 * @param str the {@code String} to capitalize
	 * @return the capitalized {@code String}
	 */
	public static String capitalize(String str) {
		return changeFirstCharacterCase(str, true);
	}

	/**
	 * Uncapitalize a {@code String}, changing the first letter to lower case as per
	 * {@link Character#toLowerCase(char)}. No other letters are changed.
	 * @param str the {@code String} to uncapitalize
	 * @return the uncapitalized {@code String}
	 */
	public static String uncapitalize(String str) {
		return changeFirstCharacterCase(str, false);
	}

	/**
	 * Replace all occurrences of a substring within a string with another string.
	 * @param inString {@code String} to examine
	 * @param oldPattern {@code String} to replace
	 * @param newPattern {@code String} to insert
	 * @return a {@code String} with the replacements
	 */
	public static String replace(String inString, String oldPattern, @Nullable String newPattern) {
		if (!Strings.isNullOrEmpty(inString) || !hasLength(oldPattern) || newPattern == null) {
			return inString;
		}
		int index = inString.indexOf(oldPattern);
		if (index == -1) {
			// no occurrence -> can return input as-is
			return inString;
		}

		int capacity = inString.length();
		if (newPattern.length() > oldPattern.length()) {
			capacity += 16;
		}
		StringBuilder sb = new StringBuilder(capacity);

		int pos = 0; // our position in the old string
		int patLen = oldPattern.length();
		while (index >= 0) {
			sb.append(inString, pos, index);
			sb.append(newPattern);
			pos = index + patLen;
			index = inString.indexOf(oldPattern, pos);
		}

		// append any characters to the right of a match
		sb.append(inString, pos, inString.length());
		return sb.toString();
	}

	private static String changeFirstCharacterCase(String str, boolean capitalize) {
		if (!isNullOrEmpty(str)) {
			return str;
		}

		char baseChar = str.charAt(0);
		char updatedChar;
		if (capitalize) {
			updatedChar = Character.toUpperCase(baseChar);
		}
		else {
			updatedChar = Character.toLowerCase(baseChar);
		}
		if (baseChar == updatedChar) {
			return str;
		}

		char[] chars = str.toCharArray();
		chars[0] = updatedChar;
		return new String(chars, 0, chars.length);
	}

	private static String lenientToString(@CheckForNull Object o) {
		if (o == null) {
			return "null";
		}
		try {
			return o.toString();
		}
		catch (Exception e) {
			// Default toString() behavior - see Object.toString()
			String objectToString = o.getClass().getName() + '@' + Integer.toHexString(System.identityHashCode(o));
			// Logger is created inline with fixed name to avoid forcing Proguard to
			// create another class.
			Logger.getLogger("com.google.common.base.Strings").log(WARNING,
					"Exception during lenientFormat for " + objectToString, e);
			return "<" + objectToString + " threw " + e.getClass().getName() + ">";
		}
	}

	// Splitting
	// -----------------------------------------------------------------------
	/**
	 * <p>
	 * Splits the provided text into an array, using whitespace as the separator.
	 * Whitespace is defined by {@link Character#isWhitespace(char)}.
	 * </p>
	 *
	 * <p>
	 * The separator is not included in the returned String array. Adjacent separators are
	 * treated as one separator. For more control over the split use the StrTokenizer
	 * class.
	 * </p>
	 *
	 * <p>
	 * A {@code null} input String returns {@code null}.
	 * </p>
	 *
	 * <pre>
	 * StringUtils.split(null)       = null
	 * StringUtils.split("")         = []
	 * StringUtils.split("abc def")  = ["abc", "def"]
	 * StringUtils.split("abc  def") = ["abc", "def"]
	 * StringUtils.split(" abc ")    = ["abc"]
	 * </pre>
	 * @param str the String to parse, may be null
	 * @return an array of parsed Strings, {@code null} if null String input
	 */
	public static String[] split(final String str) {
		return split(str, null, -1);
	}

	/**
	 * <p>
	 * Splits the provided text into an array, separator specified. This is an alternative
	 * to using StringTokenizer.
	 * </p>
	 *
	 * <p>
	 * The separator is not included in the returned String array. Adjacent separators are
	 * treated as one separator. For more control over the split use the StrTokenizer
	 * class.
	 * </p>
	 *
	 * <p>
	 * A {@code null} input String returns {@code null}.
	 * </p>
	 *
	 * <pre>
	 * StringUtils.split(null, *)         = null
	 * StringUtils.split("", *)           = []
	 * StringUtils.split("a.b.c", '.')    = ["a", "b", "c"]
	 * StringUtils.split("a..b.c", '.')   = ["a", "b", "c"]
	 * StringUtils.split("a:b:c", '.')    = ["a:b:c"]
	 * StringUtils.split("a b c", ' ')    = ["a", "b", "c"]
	 * </pre>
	 * @param str the String to parse, may be null
	 * @param separatorChar the character used as the delimiter
	 * @return an array of parsed Strings, {@code null} if null String input
	 * @since 2.0
	 */
	public static String[] split(final String str, final char separatorChar) {
		return splitWorker(str, separatorChar, false);
	}

	/**
	 * <p>
	 * Splits the provided text into an array, separators specified. This is an
	 * alternative to using StringTokenizer.
	 * </p>
	 *
	 * <p>
	 * The separator is not included in the returned String array. Adjacent separators are
	 * treated as one separator. For more control over the split use the StrTokenizer
	 * class.
	 * </p>
	 *
	 * <p>
	 * A {@code null} input String returns {@code null}. A {@code null} separatorChars
	 * splits on whitespace.
	 * </p>
	 *
	 * <pre>
	 * StringUtils.split(null, *)         = null
	 * StringUtils.split("", *)           = []
	 * StringUtils.split("abc def", null) = ["abc", "def"]
	 * StringUtils.split("abc def", " ")  = ["abc", "def"]
	 * StringUtils.split("abc  def", " ") = ["abc", "def"]
	 * StringUtils.split("ab:cd:ef", ":") = ["ab", "cd", "ef"]
	 * </pre>
	 * @param str the String to parse, may be null
	 * @param separatorChars the characters used as the delimiters, {@code null} splits on
	 * whitespace
	 * @return an array of parsed Strings, {@code null} if null String input
	 */
	public static String[] split(final String str, final String separatorChars) {
		return splitWorker(str, separatorChars, -1, false);
	}

	/**
	 * <p>
	 * Splits the provided text into an array with a maximum length, separators specified.
	 * </p>
	 *
	 * <p>
	 * The separator is not included in the returned String array. Adjacent separators are
	 * treated as one separator.
	 * </p>
	 *
	 * <p>
	 * A {@code null} input String returns {@code null}. A {@code null} separatorChars
	 * splits on whitespace.
	 * </p>
	 *
	 * <p>
	 * If more than {@code max} delimited substrings are found, the last returned string
	 * includes all characters after the first {@code max - 1} returned strings (including
	 * separator characters).
	 * </p>
	 *
	 * <pre>
	 * StringUtils.split(null, *, *)            = null
	 * StringUtils.split("", *, *)              = []
	 * StringUtils.split("ab cd ef", null, 0)   = ["ab", "cd", "ef"]
	 * StringUtils.split("ab   cd ef", null, 0) = ["ab", "cd", "ef"]
	 * StringUtils.split("ab:cd:ef", ":", 0)    = ["ab", "cd", "ef"]
	 * StringUtils.split("ab:cd:ef", ":", 2)    = ["ab", "cd:ef"]
	 * </pre>
	 * @param str the String to parse, may be null
	 * @param separatorChars the characters used as the delimiters, {@code null} splits on
	 * whitespace
	 * @param max the maximum number of elements to include in the array. A zero or
	 * negative value implies no limit
	 * @return an array of parsed Strings, {@code null} if null String input
	 */
	public static String[] split(final String str, final String separatorChars, final int max) {
		return splitWorker(str, separatorChars, max, false);
	}

	/**
	 * Performs the logic for the {@code split} and {@code splitPreserveAllTokens} methods
	 * that do not return a maximum array length.
	 * @param str the String to parse, may be {@code null}
	 * @param separatorChar the separate character
	 * @param preserveAllTokens if {@code true}, adjacent separators are treated as empty
	 * token separators; if {@code false}, adjacent separators are treated as one
	 * separator.
	 * @return an array of parsed Strings, {@code null} if null String input
	 */
	private static String[] splitWorker(final String str, final char separatorChar, final boolean preserveAllTokens) {
		// Performance tuned for 2.0 (JDK1.4)

		if (str == null) {
			return null;
		}
		final int len = str.length();
		if (len == 0) {
			return Arrays.EMPTY_STRING_ARRAY;
		}
		final List<String> list = new ArrayList<>();
		int i = 0, start = 0;
		boolean match = false;
		boolean lastMatch = false;
		while (i < len) {
			if (str.charAt(i) == separatorChar) {
				if (match || preserveAllTokens) {
					list.add(str.substring(start, i));
					match = false;
					lastMatch = true;
				}
				start = ++i;
				continue;
			}
			lastMatch = false;
			match = true;
			i++;
		}
		if (match || preserveAllTokens && lastMatch) {
			list.add(str.substring(start, i));
		}
		return list.toArray(Arrays.EMPTY_STRING_ARRAY);
	}

	/**
	 * Performs the logic for the {@code split} and {@code splitPreserveAllTokens} methods
	 * that return a maximum array length.
	 * @param str the String to parse, may be {@code null}
	 * @param separatorChars the separate character
	 * @param max the maximum number of elements to include in the array. A zero or
	 * negative value implies no limit.
	 * @param preserveAllTokens if {@code true}, adjacent separators are treated as empty
	 * token separators; if {@code false}, adjacent separators are treated as one
	 * separator.
	 * @return an array of parsed Strings, {@code null} if null String input
	 */
	private static String[] splitWorker(final String str, final String separatorChars, final int max,
			final boolean preserveAllTokens) {
		// Performance tuned for 2.0 (JDK1.4)
		// Direct code is quicker than StringTokenizer.
		// Also, StringTokenizer uses isSpace() not isWhitespace()

		if (str == null) {
			return null;
		}
		final int len = str.length();
		if (len == 0) {
			return Arrays.EMPTY_STRING_ARRAY;
		}
		final List<String> list = new ArrayList<>();
		int sizePlus1 = 1;
		int i = 0, start = 0;
		boolean match = false;
		boolean lastMatch = false;
		if (separatorChars == null) {
			// Null separator means use whitespace
			while (i < len) {
				if (Character.isWhitespace(str.charAt(i))) {
					if (match || preserveAllTokens) {
						lastMatch = true;
						if (sizePlus1++ == max) {
							i = len;
							lastMatch = false;
						}
						list.add(str.substring(start, i));
						match = false;
					}
					start = ++i;
					continue;
				}
				lastMatch = false;
				match = true;
				i++;
			}
		}
		else if (separatorChars.length() == 1) {
			// Optimise 1 character case
			final char sep = separatorChars.charAt(0);
			while (i < len) {
				if (str.charAt(i) == sep) {
					if (match || preserveAllTokens) {
						lastMatch = true;
						if (sizePlus1++ == max) {
							i = len;
							lastMatch = false;
						}
						list.add(str.substring(start, i));
						match = false;
					}
					start = ++i;
					continue;
				}
				lastMatch = false;
				match = true;
				i++;
			}
		}
		else {
			// standard case
			while (i < len) {
				if (separatorChars.indexOf(str.charAt(i)) >= 0) {
					if (match || preserveAllTokens) {
						lastMatch = true;
						if (sizePlus1++ == max) {
							i = len;
							lastMatch = false;
						}
						list.add(str.substring(start, i));
						match = false;
					}
					start = ++i;
					continue;
				}
				lastMatch = false;
				match = true;
				i++;
			}
		}
		if (match || preserveAllTokens && lastMatch) {
			list.add(str.substring(start, i));
		}
		return list.toArray(Arrays.EMPTY_STRING_ARRAY);
	}

}
