package org.magneton.foundation;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.CheckForNull;
import javax.annotation.Nullable;

/**
 * .
 *
 * @author zhangmsh
 * @version 1.0.0
 * @since 2020/11/13
 */

public final class MoreStrings {

	private static final char ZERO = '0';

	public static final String NULL = "null";

	private MoreStrings() {
		// private
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

}
