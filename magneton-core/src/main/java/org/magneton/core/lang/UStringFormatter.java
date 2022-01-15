package org.magneton.core.lang;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.Nullable;

import com.google.common.base.Preconditions;

/**
 * .
 *
 * @author zhangmsh 2022/1/13
 * @since 1.2.0
 */
public class UStringFormatter {

	private static final char ZERO = '0';

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
	 * @return
	 */
	public static String indexedFormat(@Nullable String template, @Nullable Object... args) {
		template = String.valueOf(template); // null -> "null"
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

	private static String lenientToString(@Nullable Object o) {
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
			Logger.getLogger(UStringFormatter.class.getName()).log(Level.WARNING,
					"Exception during lenientFormat for " + objectToString, e);
			return "<" + objectToString + " threw " + e.getClass().getName() + ">";
		}
	}

}
