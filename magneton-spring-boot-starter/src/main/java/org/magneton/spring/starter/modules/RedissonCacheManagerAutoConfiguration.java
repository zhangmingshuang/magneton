package org.magneton.spring.starter.modules;

import io.lettuce.core.RedisClient;
import org.redisson.api.RedissonClient;
import org.redisson.spring.cache.RedissonSpringCacheManager;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * @author zhangmsh 2022/6/11
 * @since 1.0.0
 */
@Component
@Configuration(proxyBeanMethods = false)
@ConditionalOnClass({ CacheManager.class, RedisClient.class })
@ConditionalOnProperty(prefix = "magneton.spring.cache.redis", name = "enabled", havingValue = "true",
		matchIfMissing = true)
public class RedissonCacheManagerAutoConfiguration {

	@Bean
	public RedissonSpringCacheManager redissonSpringCacheManager(RedissonClient redissonClient) {
		return new RedissonSpringCacheManager(redissonClient);
	}

}
