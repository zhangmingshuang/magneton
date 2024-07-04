package org.magneton.enhance.pay.wxv3;

import org.redisson.api.RedissonClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * @author zhangmsh 2022/6/11
 * @since 1.0.0
 */
@Component
@Configuration(proxyBeanMethods = false)
@ConditionalOnClass({ RedissonClient.class, WxV3Pay.class })
@ConditionalOnProperty(prefix = WxPayProperties.PREFIX, name = WxPayProperties.CONDITION_KEY)
public class AutoConfigurationWechatPay {

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
