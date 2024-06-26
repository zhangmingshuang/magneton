package org.magneton.spring.starter.properties;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.magneton.module.wechat.miniprogram.WechatMiniProgramConfig;
import org.magneton.spring.starter.modules.wechat.WechatMiniProgramAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 微信小程序配置.
 *
 * @author zhangmsh 2022/4/3
 * @since 1.0.0
 * @see WechatMiniProgramAutoConfiguration
 */
@Setter
@Getter
@ToString
@ConditionalOnClass(WechatMiniProgramConfig.class)
@ConfigurationProperties(WechatMiniProgramProperties.PREFIX)
public class WechatMiniProgramProperties extends WechatMiniProgramConfig {

	public static final String PREFIX = "magneton.module.wechat.mini-program";

	/**
	 * 微信小程序的appId. 用来Condition时判断需要存在这个配置才生效。
	 * @see WechatMiniProgramAutoConfiguration
	 */
	public static final String CONDITION_KEY = "appid";

}