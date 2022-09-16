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

import javax.annotation.Nullable;

/**
 * .
 *
 * @author zhangmsh 2021/8/4
 * @since 2.0.0
 */
public class StringUtil {

	private StringUtil() {
	}

	/** The maximum size to which the padding constant(s) can expand. */
	private static final int PAD_LIMIT = 8192;

	/**
	 * A String for a space character.
	 *
	 * @since 3.2
	 */
	public static final String SPACE = " ";

	/**
	 * Checks if the CharSequence contains only Unicode digits. A decimal point is not a
	 * Unicode digit and returns false.
	 *
	 * <p>
	 * {@code null} will return {@code false}. An empty CharSequence (length()=0) will
	 * return {@code false}.
	 *
	 * <p>
	 * Note that the method does not allow for a leading sign, either positive or
	 * negative. Also, if a String passes the numeric test, it may still generate a
	 * NumberFormatException when parsed by Integer.parseInt or Long.parseLong, e.g. if
	 * the value is outside the range for int or long respectively.
	 *
	 * <pre>
	 * StringUtils.isNumeric(null)   = false
	 * StringUtils.isNumeric("")     = false
	 * StringUtils.isNumeric("  ")   = false
	 * StringUtils.isNumeric("123")  = true
	 * StringUtils.isNumeric("\u0967\u0968\u0969")  = true
	 * StringUtils.isNumeric("12 3") = false
	 * StringUtils.isNumeric("ab2c") = false
	 * StringUtils.isNumeric("12-3") = false
	 * StringUtils.isNumeric("12.3") = false
	 * StringUtils.isNumeric("-123") = false
	 * StringUtils.isNumeric("+123") = false
	 * </pre>
	 * @param cs the CharSequence to check, may be null
	 * @return {@code true} if only contains digits, and is non-null
	 * @since 3.0 Changed signature from isNumeric(String) to isNumeric(CharSequence)
	 * @since 3.0 Changed "" to return false and not true
	 */
	public static boolean isNumeric(final CharSequence cs) {
		if (!hasLength(cs)) {
			return false;
		}
		final int sz = cs.length();
		for (int i = 0; i < sz; i++) {
			if (!Character.isDigit(cs.charAt(i))) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Left pad a String with spaces (' ').
	 *
	 * <p>
	 * The String is padded to the size of {@code size}.
	 *
	 * <pre>
	 * StringUtils.leftPad(null, *)   = null
	 * StringUtils.leftPad("", 3)     = "   "
	 * StringUtils.leftPad("bat", 3)  = "bat"
	 * StringUtils.leftPad("bat", 5)  = "  bat"
	 * StringUtils.leftPad("bat", 1)  = "bat"
	 * StringUtils.leftPad("bat", -1) = "bat"
	 * </pre>
	 * @param str the String to pad out, may be null
	 * @param size the size to pad to
	 * @return left padded String or original String if no padding is necessary,
	 * {@code null} if null String input
	 */
	public static String leftPad(final String str, final int size) {
		return leftPad(str, size, ' ');
	}

	/**
	 * Left pad a String with a specified character.
	 *
	 * <p>
	 * Pad to a size of {@code size}.
	 *
	 * <pre>
	 * StringUtils.leftPad(null, *, *)     = null
	 * StringUtils.leftPad("", 3, 'z')     = "zzz"
	 * StringUtils.leftPad("bat", 3, 'z')  = "bat"
	 * StringUtils.leftPad("bat", 5, 'z')  = "zzbat"
	 * StringUtils.leftPad("bat", 1, 'z')  = "bat"
	 * StringUtils.leftPad("bat", -1, 'z') = "bat"
	 * </pre>
	 * @param str the String to pad out, may be null
	 * @param size the size to pad to
	 * @param padChar the character to pad with
	 * @return left padded String or original String if no padding is necessary,
	 * {@code null} if null String input
	 * @since 2.0
	 */
	public static String leftPad(final String str, final int size, final char padChar) {
		if (str == null) {
			return null;
		}
		final int pads = size - str.length();
		if (pads <= 0) {
			return str; // returns original String when possible
		}
		if (pads > PAD_LIMIT) {
			return leftPad(str, size, String.valueOf(padChar));
		}
		return repeat(padChar, pads).concat(str);
	}

	/**
	 * Left pad a String with a specified String.
	 *
	 * <p>
	 * Pad to a size of {@code size}.
	 *
	 * <pre>
	 * StringUtils.leftPad(null, *, *)      = null
	 * StringUtils.leftPad("", 3, "z")      = "zzz"
	 * StringUtils.leftPad("bat", 3, "yz")  = "bat"
	 * StringUtils.leftPad("bat", 5, "yz")  = "yzbat"
	 * StringUtils.leftPad("bat", 8, "yz")  = "yzyzybat"
	 * StringUtils.leftPad("bat", 1, "yz")  = "bat"
	 * StringUtils.leftPad("bat", -1, "yz") = "bat"
	 * StringUtils.leftPad("bat", 5, null)  = "  bat"
	 * StringUtils.leftPad("bat", 5, "")    = "  bat"
	 * </pre>
	 * @param str the String to pad out, may be null
	 * @param size the size to pad to
	 * @param padStr the String to pad with, null or empty treated as single space
	 * @return left padded String or original String if no padding is necessary,
	 * {@code null} if null String input
	 */
	public static String leftPad(final String str, final int size, String padStr) {
		if (str == null) {
			return null;
		}
		if (!hasLength(padStr)) {
			padStr = SPACE;
		}
		final int padLen = padStr.length();
		final int strLen = str.length();
		final int pads = size - strLen;
		if (pads <= 0) {
			return str; // returns original String when possible
		}
		if (padLen == 1 && pads <= PAD_LIMIT) {
			return leftPad(str, size, padStr.charAt(0));
		}

		if (pads == padLen) {
			return padStr.concat(str);
		}
		else if (pads < padLen) {
			return padStr.substring(0, pads).concat(str);
		}
		else {
			final char[] padding = new char[pads];
			final char[] padChars = padStr.toCharArray();
			for (int i = 0; i < pads; i++) {
				padding[i] = padChars[i % padLen];
			}
			return new String(padding).concat(str);
		}
	}

	/**
	 * Returns padding using the specified delimiter repeated to a given length.
	 *
	 * <pre>
	 * StringUtils.repeat('e', 0)  = ""
	 * StringUtils.repeat('e', 3)  = "eee"
	 * StringUtils.repeat('e', -2) = ""
	 * </pre>
	 *
	 * <p>
	 * Note: this method does not support padding with
	 * <a href="http://www.unicode.org/glossary/#supplementary_character">Unicode
	 * Supplementary Characters</a> as they require a pair of {@code char}s to be
	 * represented. If you are needing to support full I18N of your applications consider
	 * using {@link #repeat(String, int)} instead.
	 * @param ch character to repeat
	 * @param repeat number of times to repeat char, negative treated as zero
	 * @return String with repeated character
	 * @see #repeat(String, int)
	 */
	public static String repeat(final char ch, final int repeat) {
		if (repeat <= 0) {
			return "";
		}
		final char[] buf = new char[repeat];
		for (int i = repeat - 1; i >= 0; i--) {
			buf[i] = ch;
		}
		return new String(buf);
	}

	/**
	 * Check that the given {@code String} is neither {@code null} nor of length 0.
	 *
	 * <p>
	 * Note: this method returns {@code true} for a {@code String} that purely consists of
	 * whitespace.
	 * @param str the {@code String} to check (may be {@code null})
	 * @return {@code true} if the {@code String} is not {@code null} and has length
	 * @see #hasLength(CharSequence)
	 * @see #hasText(String)
	 */
	public static boolean hasLength(@Nullable String str) {
		return (str != null && !str.isEmpty());
	}

	public static boolean hasLength(@Nullable CharSequence str) {
		return (str != null && str.length() > 0);
	}

	/**
	 * Trim <i>all</i> whitespace from the given {@code String}: leading, trailing, and in
	 * between characters.
	 * @param str the {@code String} to check
	 * @return the trimmed {@code String}
	 * @see java.lang.Character#isWhitespace
	 */
	public static String trimAllWhitespace(String str) {
		if (!hasLength(str)) {
			return str;
		}

		int len = str.length();
		StringBuilder sb = new StringBuilder(str.length());
		for (int i = 0; i < len; i++) {
			char c = str.charAt(i);
			if (!Character.isWhitespace(c)) {
				sb.append(c);
			}
		}
		return sb.toString();
	}

}
