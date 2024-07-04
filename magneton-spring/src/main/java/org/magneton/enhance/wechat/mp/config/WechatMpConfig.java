package org.magneton.enhance.wechat.mp.config;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 微信公众号配置.
 *
 * @author zhangmsh.
 * @since 2024
 */
@Setter
@Getter
@ToString
public class WechatMpConfig {

	/**
	 * 微信公众号的appId.
	 */
	private String appid;

	/**
	 * 微信公众号的secret.
	 */
	private String secret;

	/**
	 * 微信公众号的token.
	 */
	private String token;

	/**
	 * 微信公众号的EncodingAESKey.
	 */
	private String aesKey;

	/**
	 * 是否校验签名
	 */
	private boolean signCheck = true;

}