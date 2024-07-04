package org.magneton.enhance.wechat.mp.core.util;

import com.google.common.base.Preconditions;

import javax.annotation.Nullable;

/**
 * 菜单工具.
 *
 * @author zhangmsh.
 * @since 2024
 */
public class BizMsgMenuUtil {

	public static String revision(String text) {
		return revision(text, text);
	}

	public static String revision(String text, String content) {
		return revision(text, content, null);
	}

	public static String revision(String text, String content, @Nullable Integer id) {
		// <a href="weixin://bizmsgmenu?msgmenucontent=x2++x2&msgmenuid=123">[1] x2 x2</a>
		/// https://blog.csdn.net/gymaisyl/article/details/111151077
		// msgmenucontent：是你点击后，展示位微信对话框的内容
		// msgmenuid：微信会按照text方式将数据给开发者服务器，开发者可以拿到该参数进行后续的逻辑判断处理（MsgType为event，Event为text）

		Preconditions.checkNotNull(text, "text must not be null");
		Preconditions.checkNotNull(content, "content must not be null");

		if (id == null) {
			id = 0;
		}
		return "<a href=\"weixin://bizmsgmenu?msgmenucontent=" + content + "&msgmenuid=" + id + "\">" + text + "</a>";
	}

}