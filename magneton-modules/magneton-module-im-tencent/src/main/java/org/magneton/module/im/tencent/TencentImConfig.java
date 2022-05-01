package org.magneton.module.im.tencent;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * . https://cloud.tencent.com/document/product/269/32688
 *
 * @author zhangmsh 2022/4/26
 * @since 2.0.8
 */
@Setter
@Getter
@ToString
public class TencentImConfig {

	/**
	 * 应用 SDKAppID，可在即时通信 IM 控制台 的应用卡片中获取。
	 */
	private long appId;

	/**
	 * 安全Key
	 */
	private String appSecret;

	/**
	 * 管理员账号
	 */
	private String admin;

	/**
	 * 生成的UserSign的默认生效时间，单位为秒，默认为180天
	 */
	private int userSignExpireSeconds = 180 * 86400;

}
