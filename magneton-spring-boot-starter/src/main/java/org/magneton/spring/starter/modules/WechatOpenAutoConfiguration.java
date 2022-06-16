package org.magneton.spring.starter.modules;

import org.magneton.module.wechat.core.WechatAccessTokenCache;
import org.magneton.module.wechat.open.WechatOpen;
import org.magneton.module.wechat.open.WechatOpenBuilder;
import org.magneton.spring.starter.extension.wechat.RedisWechatAccessTokenCache;
import org.magneton.spring.starter.properties.WechatOpenProperties;
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
@ConditionalOnClass({ RedissonClient.class, WechatOpen.class })
@ConditionalOnProperty(prefix = WechatOpenProperties.PREFIX, name = WechatOpenProperties.CONDITION_KEY)
public class WechatOpenAutoConfiguration {

	@Bean
	@ConditionalOnMissingBean
	public WechatOpenProperties wechatOpenProperties() {
		return new WechatOpenProperties();
	}

	@Bean
	@ConditionalOnMissingBean
	public WechatAccessTokenCache accessTokenCache(RedissonClient redissonClient) {
		return new RedisWechatAccessTokenCache(redissonClient);
	}

	@Bean
	@ConditionalOnMissingBean
	public WechatOpen wechat(WechatOpenProperties wechatOpenProperties, WechatAccessTokenCache wechatAccessTokenCache) {
		return WechatOpenBuilder.newBuilder(wechatOpenProperties).accessTokenCache(wechatAccessTokenCache).build();
	}

}
