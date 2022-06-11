package org.magneton.spring.starter.modules;

import org.magneton.module.im.tencent.TencentIm;
import org.magneton.module.im.tencent.UserSignCache;
import org.magneton.spring.starter.extension.tencent.im.TencentImUserSignCache;
import org.magneton.spring.starter.properties.TencentImProperties;
import org.redisson.api.RedissonClient;

import org.springframework.beans.factory.annotation.Autowired;
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
@ConditionalOnClass({ RedissonClient.class, TencentIm.class })
@ConditionalOnProperty(prefix = TencentImProperties.PREFIX, name = TencentImProperties.CONDITION_KEY)
public class TencentImAutoConfiguration {

	@Bean
	@ConditionalOnMissingBean
	public TencentImProperties tencentImProperties() {
		return new TencentImProperties();
	}

	@Bean
	@ConditionalOnMissingBean
	public UserSignCache userSignCache(RedissonClient redissonClient) {
		return new TencentImUserSignCache(redissonClient);
	}

	@Bean
	@ConditionalOnMissingBean
	public TencentIm tencentIm(TencentImProperties tencentImProperties,
			@Autowired(required = false) UserSignCache userSignCache) {
		TencentIm tencentIm = new TencentIm(tencentImProperties);
		tencentIm.setUserSignCache(userSignCache);
		return tencentIm;
	}

}
