package org.magneton.foundiation;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;

import javax.annotation.CheckForNull;
import javax.annotation.Nullable;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * 字符串工具类.
 *
 * @author zhangmsh
 * @since 2020/11/13
 */

public final class MoreStrings {

	private static final char ZERO = '0';

	public static final String NULL = "null";

	private MoreStrings() {
		// private
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
	 * StringUtils.isBlank(null)      = true
	 * StringUtils.isBlank("")        = true
	 * StringUtils.isBlank(" ")       = true
	 * StringUtils.isBlank("bob")     = false
	 * StringUtils.isBlank("  bob  ") = false
	 * </pre>
	 * @param cs the CharSequence to check, may be null
	 * @return {@code true} if the CharSequence is null, empty or whitespace only
	 * @since 2.0
	 * @since 3.0 Changed signature from isBlank(String) to isBlank(CharSequence)
	 */
	public static boolean isNullOrBlank(final CharSequence cs) {
		final int strLen = length(cs);
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
	 * Gets a CharSequence length or {@code 0} if the CharSequence is {@code null}.
	 * @param cs a CharSequence or {@code null}
	 * @return CharSequence length or {@code 0} if the CharSequence is {@code null}.
	 * @since 2.4
	 * @since 3.0 Changed signature from length(String) to length(CharSequence)
	 */
	public static int length(final CharSequence cs) {
		return cs == null ? 0 : cs.length();
	}

	/**
	 * Checks if a CharSequence is empty ("") or null.
	 * </p>
	 *
	 * <pre>
	 * MoreStrings.isEmpty(null)      = true
	 * MoreStrings.isEmpty("")        = true
	 * MoreStrings.isEmpty(" ")       = false
	 * MoreStrings.isEmpty("bob")     = false
	 * MoreStringsg.isEmpty("  bob  ") = false
	 * </pre>
	 *
	 * <p>
	 * NOTE: This method changed in Lang version 2.0. It no longer trims the CharSequence.
	 * That functionality is available in isBlank().
	 * </p>
	 * @param cs the CharSequence to check, may be null
	 * @return {@code true} if the CharSequence is empty or null
	 * @since 3.0 Changed signature from isEmpty(String) to isEmpty(CharSequence)
	 */
	public static boolean isNullOrEmpty(final CharSequence cs) {
		return cs == null || cs.length() == 0;
	}

	/**
	 * Returns the given {@code template} string with each occurrence of
	 * {@code "%s0","%s1" or "%sx"} replaced with the corresponding argument value from
	 * {@code args}; or if the placeholder and argument count do not match, keep the
	 * placeholder of that string.
	 *
	 * <pre>eg: {@code
	 * #format("hai %s0", "zhangsan") return "hai zhangsan"
	 * #format("hai %s0, I'm %s1", "zhangsan", "lisi") return "hia zhangsan, I'm lisi"
	 * #format("hai %s0, I'm %s1,%s1", "zhangsan","lisi") return "hai zhangsan, I'm lisi,lisi"
	 * #format("hai %s0, I'm %s1", "zhangsan") return "hai zhangsan, I'm %s1"
	 *
	 * }</pre>
	 * @param template a string containing zero or more {@code "%s0"} placeholder
	 * sequences.
	 * @param args the arguments to be substituted into the message template. The first
	 * argument specified is substituted for the occurrence of {@code "%s0"} in the
	 * template, and so forth. A {@code null} argement is keep the placeholder.
	 * @return
	 */
	public static String format(String template, @Nullable Object... args) {
		if (args == null || args.length < 1) {
			return template;
		}
		int argsLength = args.length;
		// start substituting the arguments into the '%sx' placeholders
		StringBuilder builder = new StringBuilder(template.length() + 16 * argsLength);
		int templateStart = 0;
		int maxTemplatePosition = template.length() - 1;
		while (true) {
			int placeholderStart = template.indexOf("%s", templateStart);
			if (placeholderStart == -1) {
				break;
			}
			if (placeholderStart + 2 <= maxTemplatePosition) {
				char placeholderIndex = template.charAt(placeholderStart + 2);
				int index = (int) placeholderIndex - (int) ZERO;
				Object arg = index >= argsLength ? null : args[index];
				if (arg == null) {
					builder.append(template, templateStart, placeholderStart + 3);
				}
				else {
					builder.append(template, templateStart, placeholderStart);
					builder.append(arg);
				}
			}
			else {
				builder.append(template, templateStart, placeholderStart + 3);
			}
			templateStart = placeholderStart + 3;
		}
		builder.append(template, templateStart, template.length());
		return builder.toString();
	}

	public static String indexFormat(String format, Object... args) {
		if (isNullOrEmpty(format)) {
			return "null";
		}
		if (args != null && args.length > 0) {
			for (int i = 0; i < args.length; ++i) {
				String target = "{" + i + "}";
				format = format.replace(target, lenientToString(args[i]));
			}
		}

		return format;
	}

	public static String suffixIfNotNullOrEmpty(@Nullable String str, String suffix) {
		Preconditions.checkNotNull(suffix);
		if (Strings.isNullOrEmpty(str)) {
			return suffix;
		}
		return str + suffix;
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
		return Strings.isNullOrEmpty(string);
	}

	/**
	 * Determine whether given strings contains one or more {@code null} or empty string.
	 * @param css the given strings.
	 * @return {@code true} if the given strings contains one or more {@code null} or
	 * empty, {@code
	 *     false} it all the strings is not {@code null} and empty.
	 */
	public static boolean isAnyNullOrEmpty(String... css) {
		if (MoreArrays.isNullOrEmpty(css)) {
			return true;
		}
		for (String cs : css) {
			if (Strings.isNullOrEmpty(cs)) {
				return true;
			}
		}
		return false;
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
			Logger.getLogger("com.google.common.base.Strings").log(Level.WARNING,
					"Exception during lenientFormat for " + objectToString, e);
			return "<" + objectToString + " threw " + e.getClass().getName() + ">";
		}
	}

	/**
	 * 是否包含字符串
	 * @param str 验证字符串
	 * @param strs 字符串组
	 * @return 包含返回true
	 */
	public static boolean inStringIgnoreCase(String str, String... strs) {
		if (str != null && strs != null) {
			for (String s : strs) {
				if (str.equalsIgnoreCase(trim(s))) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * 去空格
	 */
	public static String trim(String str) {
		return (str == null ? "" : str.trim());
	}

	/**
	 * 截取字符串
	 * @param str 字符串
	 * @param start 开始
	 * @return 结果
	 */
	public static String substring(final String str, int start) {
		if (str == null) {
			return StringPool.EMPTY;
		}

		if (start < 0) {
			start = str.length() + start;
		}

		if (start < 0) {
			start = 0;
		}
		if (start > str.length()) {
			return StringPool.EMPTY;
		}

		return str.substring(start);
	}

	/**
	 * 截取字符串
	 * @param str 字符串
	 * @param start 开始
	 * @param end 结束
	 * @return 结果
	 */
	public static String substring(final String str, int start, int end) {
		if (str == null) {
			return StringPool.EMPTY;
		}

		if (end < 0) {
			end = str.length() + end;
		}
		if (start < 0) {
			start = str.length() + start;
		}

		if (end > str.length()) {
			end = str.length();
		}

		if (start > end) {
			return StringPool.EMPTY;
		}

		if (start < 0) {
			start = 0;
		}
		if (end < 0) {
			end = 0;
		}

		return str.substring(start, end);
	}

}