package org.magneton.spring.starter.modules.wechat;

import org.magneton.module.wechat.core.WechatAccessTokenCache;
import org.magneton.module.wechat.miniprogram.DefaultWechatMiniProgram;
import org.magneton.module.wechat.miniprogram.WechatMiniProgram;
import org.magneton.module.wechat.miniprogram.core.auth.WechatMiniProgramOAuthImpl;
import org.magneton.spring.starter.extension.wechat.RedisWechatAccessTokenCache;
import org.magneton.spring.starter.properties.WechatMiniProgramProperties;
import org.redisson.api.RedissonClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * 微信小程序自动配置.
 *
 * @author zhangmsh 2022/6/11
 * @since 1.0.0
 */
@Component
@Configuration(proxyBeanMethods = false)
@ConditionalOnClass({ RedissonClient.class, WechatMiniProgramOAuthImpl.class })
@ConditionalOnProperty(prefix = WechatMiniProgramProperties.PREFIX, name = WechatMiniProgramProperties.CONDITION_KEY)
public class WechatMiniProgramAutoConfiguration {

	@Bean
	@ConditionalOnMissingBean
	public WechatMiniProgramProperties wechatMiniProgramProperties() {
		return new WechatMiniProgramProperties();
	}

	@Bean
	@ConditionalOnMissingBean
	public WechatAccessTokenCache accessTokenCache(RedissonClient redissonClient) {
		return new RedisWechatAccessTokenCache(redissonClient);
	}

	@Bean
	@ConditionalOnMissingBean
	public WechatMiniProgram wechatMiniProgram(WechatMiniProgramProperties wechatMiniProgramProperties,
			WechatAccessTokenCache wechatAccessTokenCache) {
		return new DefaultWechatMiniProgram(wechatMiniProgramProperties, wechatAccessTokenCache);
	}

}