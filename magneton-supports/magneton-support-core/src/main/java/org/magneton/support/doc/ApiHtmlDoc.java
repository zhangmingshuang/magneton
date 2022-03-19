package org.magneton.support.doc;

import javax.annotation.Nullable;
import org.magneton.core.base.Preconditions;
import org.magneton.core.base.Strings;

/**
 * .
 *
 * @author zhangmsh 15/03/2022
 * @since 2.0.7
 */
@SuppressWarnings("DoubleCheckedLocking")
public class ApiHtmlDoc {

	@Nullable
	private static final StringBuilder API = new StringBuilder(1024);

	private static String body = null;

	private ApiHtmlDoc() {
	}

	public static void addDescription(String title, String context) {
		Preconditions.checkNotNull(title);
		Preconditions.checkNotNull(context);
		StringBuilder builder = new StringBuilder(title.length() + context.length() + 128);
		builder.append("<h3>").append(title).append("</h3>");
		builder.append(context);

		API.insert(0, builder);
		body = null;
	}

	public static void addApi(String title, String path) {
		addApi(title, path, "");
	}

	public static void addApi(String title, String path, @Nullable String description) {
		Preconditions.checkNotNull(title);
		Preconditions.checkNotNull(path);

		API.append("<h2><a href=\"./").append(path).append("/index.html\">").append(title).append("</a></h2>");
		if (!Strings.isNullOrEmpty(description)) {
			API.append(description);
		}
		API.append("<br />");
		body = null;
	}

	public static String getBody() {
		if (body == null) {
			// noinspection SynchronizeOnThis
			synchronized (ApiHtmlDoc.class) {
				if (body == null) {
					body = API.toString();
				}
			}
		}
		return body;
	}

}
