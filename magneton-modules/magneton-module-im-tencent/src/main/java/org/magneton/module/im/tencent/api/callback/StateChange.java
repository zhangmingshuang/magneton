package org.magneton.module.im.tencent.api.callback;

/**
 * @author zhangmsh 2022/5/12
 * @since 1.0.0
 */
public class StateChange {

	/**
	 * 创建应用时在即时通信 IM 控制台分配的 SDKAppID
	 */
	private String sdkAppid;

	/**
	 * 固定值为 JSON
	 */
	private String contentType;

	/**
	 * 客户端 IP 地址
	 */
	private String clientIP;

	/**
	 * 客户端平台
	 * @see org.magneton.module.im.tencent.api.OptPlatform
	 */
	private String optPlatform;

}
