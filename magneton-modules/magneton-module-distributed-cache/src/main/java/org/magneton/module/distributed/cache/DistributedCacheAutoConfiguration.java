package org.magneton.module.distributed.cache;

import org.magneton.module.distributed.cache.redis.JSONRedisValueSerializer;
import org.magneton.module.distributed.cache.redis.RedisTemplateDistributedCache;
import org.magneton.module.distributed.cache.redis.RedisValueSerializer;

import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * .
 *
 * @author zhangmsh
 * @since 2021/6/24
 */
@Configuration
class DistributedCacheAutoConfiguration {

	@Bean
	@ConditionalOnMissingBean
	@ConditionalOnBean(RedisTemplate.class)
	@ConditionalOnProperty(prefix = "magneton.module.distributed.cache", name = "redis", havingValue = "true",
			matchIfMissing = true)
	public RedisValueSerializer redisValueSerializer() {
		return new JSONRedisValueSerializer();
	}

	@Bean
	@ConditionalOnBean(RedisTemplate.class)
	@ConditionalOnProperty(prefix = "magneton.module.distributed.cache", name = "redis", havingValue = "true",
			matchIfMissing = true)
	public DistributedCache redisCache(RedisTemplate redisTemplate, RedisValueSerializer redisValueSerializer) {
		return new RedisTemplateDistributedCache(redisTemplate, redisValueSerializer);
	}

}
