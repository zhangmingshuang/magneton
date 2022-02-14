package org.magneton.spring.starter.configuration;

import org.magneton.module.distributed.cache.DistributedCache;
import org.magneton.module.distributed.cache.redis.RedissonDistributedCache;
import org.magneton.module.distributed.lock.DistributedLock;
import org.magneton.module.distributed.lock.redis.RedissonDistributedLock;
import org.magneton.module.geo.Geo;
import org.magneton.module.geo.redis.RedissonGeo;
import org.redisson.api.RedissonClient;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * .
 *
 * @author zhangmsh 2022/2/11
 * @since 1.2.0
 */
@Configuration
public class ModulesAutoConfiguration {

	@Bean
	@ConditionalOnClass(RedissonClient.class)
	@ConditionalOnMissingBean
	public DistributedCache distributedCache(RedissonClient redissonClient) {
		return new RedissonDistributedCache(redissonClient);
	}

	@Bean
	@ConditionalOnClass(RedissonClient.class)
	@ConditionalOnMissingBean
	public DistributedLock distributedLock(RedissonClient redissonClient) {
		return new RedissonDistributedLock(redissonClient);
	}

	@Bean
	@ConditionalOnClass(RedissonClient.class)
	@ConditionalOnMissingBean
	public Geo geo(RedissonClient redissonClient) {
		return new RedissonGeo(redissonClient);
	}

}
