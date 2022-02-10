package org.magneton.module.distributed.cache;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
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

	public static final int dbIndex = 13;
	static {
		distributedCache = new RedissonDistributedCache<>(TestRedisson.redissonClient);
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
