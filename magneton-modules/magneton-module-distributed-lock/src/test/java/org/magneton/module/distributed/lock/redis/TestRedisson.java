package org.magneton.module.distributed.lock.redis;

import org.junit.jupiter.api.AfterAll;
import org.magneton.adaptive.redis.RedissonAdapter;
import org.redisson.api.RedissonClient;

/**
 * .
 *
 * @author zhangmsh 2022/2/8
 * @since 1.2.0
 */
public class TestRedisson {

	public static final RedissonClient redissonClient = RedissonAdapter.createSingleServerClient();

	@AfterAll
	static void afterAll() {
		redissonClient.getKeys().flushdb();
	}

}
