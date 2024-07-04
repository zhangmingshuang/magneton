package org.magneton.enhance.wechat.mp;

import org.magneton.enhance.wechat.mp.core.EventWechatMpTemplate;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 微信公众号自动配置.
 *
 * @author zhangmsh.
 * @since 2024
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnClass({ WechatMpTemplate.class })
@ConditionalOnProperty(prefix = WechatMpProperties.PREFIX, name = WechatMpProperties.CONDITION_KEY)
public class AutoConfigurationWechatMp {

	@Bean
	public WechatMpProperties wechatMpProperties() {
		return new WechatMpProperties();
	}

	@Bean
	@ConditionalOnMissingBean
	public WechatMpTemplate wechatMpTemplate(WechatMpProperties wechatMpProperties) {
		return new EventWechatMpTemplate(wechatMpProperties);
	}

	@Bean
	public WechatMpHandlerPostProcessor wechatMpHandlerPostProcessor() {
		return new WechatMpHandlerPostProcessor();
	}

}