package org.magneton.enhance.im.tencent;

import org.magneton.redis.enhance.im.tencent.TencentImUserSignCache;
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
public class AutoConfigurationImTencent {

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
