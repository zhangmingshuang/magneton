package org.magneton.module.pay.wechat;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * .
 *
 * @author zhangmsh 28/03/2022
 * @since 2.0.7
 */
@Setter
@Getter
@ToString
public class WechatPayConfig {

	/**
	 * app编码
	 */
	private String appId;

	/**
	 * 商户编码
	 */
	private String mchId;

	/**
	 * 异步通知地址
	 */
	private String notifyUrl;

	/**
	 * app密匙
	 */
	private String appKey;

}
