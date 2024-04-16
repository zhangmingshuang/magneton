package org.magneton.spring.starter.modules.wechat;

import org.magneton.module.wechat.offiaccount.EventWechatOffiaccountImpl;
import org.magneton.module.wechat.offiaccount.WechatOffiaccount;
import org.magneton.spring.starter.modules.wechat.offiaccount.WechatOffiaccountMessageHandlerPostProcessor;
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
@ConditionalOnClass({ WechatOffiaccount.class })
@ConditionalOnProperty(prefix = WechatOffiaccountProperties.PREFIX, name = WechatOffiaccountProperties.CONDITION_KEY)
public class WechatOffiaccountAutoConfiguration {

	@Bean
	public WechatOffiaccountProperties wechatOffiaccountProperties() {
		return new WechatOffiaccountProperties();
	}

	@Bean
	@ConditionalOnMissingBean
	public WechatOffiaccount wechatOffiaccount(WechatOffiaccountProperties wechatOffiaccountProperties) {
		return new EventWechatOffiaccountImpl(wechatOffiaccountProperties);
	}

	@Bean
	public WechatOffiaccountMessageHandlerPostProcessor wechatOffiaccountMessageHandlerPostProcessor() {
		return new WechatOffiaccountMessageHandlerPostProcessor();
	}

}