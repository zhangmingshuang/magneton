package org.magneton.module.distributed.cache;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.magneton.module.distributed.cache.redis.JSONRedisValueSerializer;
import org.magneton.module.distributed.cache.redis.RedisTemplateDistributedCache;
import org.magneton.module.distributed.cache.redis.RedisValueSerializer;

import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * .
 *
 * @author zhangmsh 2022/2/8
 * @since 1.2.0
 */
public class TestRedisTemplate {

	public static final RedisTemplate redisTemplate = new RedisTemplate();

	public static final DistributedCache distributedCache;

	public static final RedisValueSerializer redisValueSerializer;

	public static final int dbIndex = 13;
	static {
		RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration();
		redisStandaloneConfiguration.setDatabase(dbIndex);

		LettuceConnectionFactory lettuceConnectionFactory = new LettuceConnectionFactory(redisStandaloneConfiguration);
		lettuceConnectionFactory.afterPropertiesSet();
		redisTemplate.setConnectionFactory(lettuceConnectionFactory);
		redisTemplate.afterPropertiesSet();

		redisValueSerializer = new JSONRedisValueSerializer();
		distributedCache = new RedisTemplateDistributedCache<>(TestRedisTemplate.redisTemplate, redisValueSerializer);
	}

	@BeforeAll
	static void beforeAll() {
		System.out.println("change db to " + dbIndex);
	}

	@AfterAll
	static void afterAll() {
		System.out.println("flush " + dbIndex + " db");
		distributedCache.flushDb();
	}

}
