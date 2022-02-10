package org.magneton.module.distributed.cache.redis;

import org.redisson.api.RedissonClient;

/**
 * .
 *
 * @author zhangmsh 2021/6/24
 * @since 1.0.0
 */
public abstract class AbstractRedissonOps {

	protected final RedissonClient redissonClient;

	protected AbstractRedissonOps(RedissonClient redissonClient) {
		this.redissonClient = redissonClient;
	}

}
