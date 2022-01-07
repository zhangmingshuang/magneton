package org.magneton.core.util;

import com.google.common.annotations.Beta;
import com.google.common.base.Strings;
import javax.annotation.Nullable;

/**
 * .
 *
 * @author zhangmsh
 * @version 1.0.0
 * @since 2020/11/13
 */
@Beta
public final class MoreStrings {

	private static final char ZERO = '0';

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
		@SuppressWarnings("MagicNumber")
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

}
