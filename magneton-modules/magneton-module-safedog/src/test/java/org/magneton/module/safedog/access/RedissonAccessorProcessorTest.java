package org.magneton.module.safedog.access;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.magneton.adaptive.redis.RedissonAdapter;
import org.redisson.api.RedissonClient;

/**
 * Test case of {@link RedissonAccessorProcessor}.
 *
 * @author zhangmsh.
 * @since 2023.9
 */
class RedissonAccessorProcessorTest {

	@SneakyThrows
	@Test
	void test() {
		RedissonClient redissonClient = null;
		try {
			redissonClient = RedissonAdapter.createSingleServerClient();
		}
		catch (Throwable e) {
			System.out.println("ignore RedissonAccessorProcessorTest test");
		}

		if (redissonClient == null) {
			return;
		}

		RedissonAccessorProcessor rap = new RedissonAccessorProcessor(redissonClient);

		AccessConfig accessConfig = new AccessConfig();
		accessConfig.setNumberOfWrongs(3);
		accessConfig.setWrongTimeToForget(1000);
		accessConfig.setLockTime(1000);
		accessConfig.setAccessTimeCalculator(new DefaultAccessTimeCalculator());
		rap.setAccessConfig(accessConfig);

		Accessor accessor = rap.create("RedissonAccessorProcessorTest");

		Assertions.assertEquals(2, accessor.onError());
		Assertions.assertFalse(accessor.locked());

		Thread.sleep(accessConfig.getWrongTimeToForget());
		Assertions.assertEquals(2, accessor.onError());

		for (int i = 0; i < accessConfig.getNumberOfWrongs(); i++) {
			accessor.onError();
		}
		Assertions.assertEquals(0, accessor.onError());
		Assertions.assertTrue(accessor.locked());

		Thread.sleep(accessConfig.getLockTime());
		Assertions.assertFalse(accessor.locked());

		accessor.reset();
		Assertions.assertFalse(accessor.locked());
		Assertions.assertEquals(2, accessor.onError());
	}

}