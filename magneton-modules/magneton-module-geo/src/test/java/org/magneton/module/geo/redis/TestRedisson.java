package org.magneton.module.geo.redis;

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

	public static RedissonClient redissonClient;

	static {
		try {
			redissonClient = RedissonAdapter.createSingleServerClient();
		}
		catch (Exception e) {
			// ignore
			System.out.println("Error: RedissonClient not found.");
		}
	}

	public boolean isNeed() {
		return redissonClient != null;
	}

	@AfterAll
	static void afterAll() {
		if (redissonClient != null) {
			redissonClient.getKeys().flushdb();
		}
	}

}