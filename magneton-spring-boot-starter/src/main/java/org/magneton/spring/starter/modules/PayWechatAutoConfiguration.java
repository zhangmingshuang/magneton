package org.magneton.spring.starter.modules;

import org.magneton.module.pay.wechat.v3.WxV3Pay;
import org.magneton.module.pay.wechat.v3.WxV3PayImpl;
import org.magneton.module.pay.wechat.v3.core.WxPayConfig;
import org.magneton.spring.starter.properties.WxPayProperties;
import org.redisson.api.RedissonClient;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * @author zhangmsh 2022/6/11
 * @since 1.0.0
 */
@Component
@Configuration(proxyBeanMethods = false)
@ConditionalOnClass({ RedissonClient.class, WxV3Pay.class, WxPayConfig.class })
public class PayWechatAutoConfiguration {

	@Bean
	@ConditionalOnMissingBean
	public WxPayProperties wechatPayProperties() {
		return new WxPayProperties();
	}

	@Bean
	@ConditionalOnMissingBean
	public WxV3Pay wechatPay(WxPayProperties wechatPayProperties) {
		return new WxV3PayImpl(wechatPayProperties);
	}

}
