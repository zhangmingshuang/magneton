package org.magneton.module.wechat.miniprogram;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 微信小程序配置
 *
 * @author zhangmsh 2022/5/1
 * @since 1.0.0
 */
@Setter
@Getter
@ToString
public class WechatMiniProgramConfig {

	/**
	 * appid string 是 小程序 appId
	 */
	private String appid;

	/**
	 * secret string 是 小程序 appSecret
	 */
	private String secret;

}
