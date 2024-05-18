package org.magneton.module.wechat.mp.core;

/**
 * 微信公众号模块上下文.
 *
 * @author zhangmsh.
 * @since 2024
 */
public class MpContext {

	private static final ThreadLocal<String> APPID = new InheritableThreadLocal<>();

	public static String currentAppid() {
		String appid = APPID.get();
		if (appid == null) {
			throw new IllegalStateException("appid is null.");
		}
		return appid;
	}

	public static void setCurrentAppid(String appid) {
		APPID.set(appid);
	}

}