package org.magneton.module.statistics.redis;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.magneton.adaptive.redis.RedissonAdapter;
import org.magneton.module.statistics.pvpu.RedissonPvUvProcessor;

/**
 * .
 *
 * @author zhangmsh 18/03/2022
 * @since 2.0.7
 */
class RedissonPvUvProcessorTest {

	private static RedissonPvUvProcessor statistics;

	@BeforeAll
	public static void init() {
		statistics = new RedissonPvUvProcessor(RedissonAdapter.createSingleServerClient());
	}

	@Test
	void test() {
		statistics.clean("test");

		boolean test = statistics.pvUv().isUv("test", 1);
		Assertions.assertTrue(test);
		test = statistics.pvUv().isUv("test", 1);
		Assertions.assertFalse(test);

		statistics.clean("test");
	}

}