package org.magneton.support.doc;

/**
 * .
 *
 * @author zhangmsh 15/03/2022
 * @since 2.0.7
 */
@SuppressWarnings("DoubleCheckedLocking")
public class HtmlDoc {

	private static StringBuilder api = new StringBuilder(1024);

	private static String html;

	public static void addApi(String title) {
		api.append(title).append("Api").append("<br />");
	}

	public static String getHtml() {
		if (html == null) {
			synchronized (HtmlDoc.class) {
				if (html == null) {
					html = api.toString();
					api.setLength(0);
					api = null;
				}
			}
		}
		return html;
	}

}
