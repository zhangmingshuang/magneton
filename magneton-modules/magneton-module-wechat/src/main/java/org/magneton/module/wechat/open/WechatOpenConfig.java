package org.magneton.module.wechat.open;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 微信开放平台配置
 *
 * @author zhangmsh 2022/4/1
 * @since 1.0.0
 */
@Setter
@Getter
@ToString
public class WechatOpenConfig {

	/**
	 * 应用唯一标识，在微信开放平台提交应用审核通过后获得
	 */
	private String appid;

	/**
	 * 应用密钥 AppSecret，在微信开放平台提交应用审核通过后获得
	 */
	private String secret;

}