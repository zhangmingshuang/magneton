package org.magneton.enhance.wechat.mp;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.magneton.enhance.wechat.mp.config.WechatMpConfig;
import org.magneton.spring.starter.modules.wechat.WechatMiniProgramAutoConfiguration;
import org.magneton.spring.starter.modules.wechat.WechatMpAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 微信公众号配置.
 *
 * @author zhangmsh 2022/4/3
 * @since 1.0.0
 * @see WechatMpAutoConfiguration
 */
@Setter
@Getter
@ToString
@ConditionalOnClass(WechatMpConfig.class)
@ConfigurationProperties(WechatMpProperties.PREFIX)
public class WechatMpProperties extends WechatMpConfig {

	public static final String PREFIX = "magneton.module.wechat.mp";

	/**
	 * 微信小程序的appId. 用来Condition时判断需要存在这个配置才生效。
	 * @see WechatMiniProgramAutoConfiguration
	 */
	public static final String CONDITION_KEY = "appid";

}