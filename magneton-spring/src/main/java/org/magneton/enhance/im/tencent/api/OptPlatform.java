package org.magneton.enhance.im.tencent.api;

import com.google.common.base.Preconditions;

/**
 * 客户端平台
 *
 * @author zhangmsh 2022/5/12
 * @since 1.0.0
 */
public enum OptPlatform {

	/**
	 * 使用 REST API 发送请求
	 */
	RESTAPI("RESTAPI"),
	/**
	 * 使用 Web SDK 发送请求
	 */
	WEB("Web"),

	/**
	 * 安卓
	 */
	ANDROID("Android"),
	/**
	 * IOS State.StateChange 回调是大写的 IOS，其余回调是小写的 iOS，使用时请自行兼容。
	 */
	IOS("iOS"),
	/**
	 * Windows
	 */
	WINDOWS("Windows"),
	/**
	 * Mac
	 */
	MAC("Mac"),
	/**
	 * iPad
	 */
	IPAD("iPad"),
	/**
	 * 使用未知类型的设备发送请求
	 */
	UNKNOWN("Unknown");

	private final String name;

	OptPlatform(String name) {
		this.name = name;
	}

	public String getName() {
		return this.name;
	}

	public static OptPlatform nameOf(String name) {
		Preconditions.checkNotNull(name, "name must not be null");
		for (OptPlatform platform : OptPlatform.values()) {
			if (platform.getName().equalsIgnoreCase(name)) {
				return platform;
			}
		}
		throw new IllegalArgumentException("unknown platform: " + name);
	}

}
