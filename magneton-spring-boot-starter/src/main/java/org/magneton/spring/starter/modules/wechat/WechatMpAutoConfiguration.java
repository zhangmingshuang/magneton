package org.magneton.spring.starter.modules.wechat;

import org.magneton.module.wechat.mp.WechatMpTemplate;
import org.magneton.module.wechat.mp.core.EventWechatMpTemplate;
import org.magneton.spring.starter.modules.wechat.offiaccount.WechatMpHandlerPostProcessor;
import org.magneton.spring.starter.properties.WechatOffiaccountProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * 微信公众号自动配置.
 *
 * @author zhangmsh.
 * @since 2024
 */
@Component
@Configuration(proxyBeanMethods = false)
@ConditionalOnClass({ WechatMpTemplate.class })
@ConditionalOnProperty(prefix = WechatOffiaccountProperties.PREFIX, name = WechatOffiaccountProperties.CONDITION_KEY)
public class WechatMpAutoConfiguration {

	@Bean
	public WechatOffiaccountProperties wechatOffiaccountProperties() {
		return new WechatOffiaccountProperties();
	}

	@Bean
	@ConditionalOnMissingBean
	public WechatMpTemplate wechatOffiaccount(WechatOffiaccountProperties wechatOffiaccountProperties) {
		return new EventWechatMpTemplate(wechatOffiaccountProperties);
	}

	@Bean
	public WechatMpHandlerPostProcessor wechatOffiaccountMessageHandlerPostProcessor() {
		return new WechatMpHandlerPostProcessor();
	}

}