package org.magneton.module.distributed.cache;

import org.junit.jupiter.api.AfterAll;
import org.magneton.adaptive.redis.RedissonAdapter;
import org.magneton.module.distributed.cache.redis.RedissonDistributedCache;
import org.redisson.api.RedissonClient;

/**
 * .
 *
 * @author zhangmsh 2022/2/8
 * @since 1.2.0
 */
public class TestRedisson {

	public static final RedissonClient redissonClient = RedissonAdapter.createSingleServerClient();

	public static final DistributedCache distributedCache;

	static {
		distributedCache = new RedissonDistributedCache(TestRedisson.redissonClient);
	}

	@AfterAll
	static void afterAll() {
		distributedCache.flushDb();
	}

}
