package org.magneton.module.statistics.pvpu;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.magneton.adaptive.redis.RedissonAdapter;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.redisson.config.SingleServerConfig;

/**
 * Test Case For {@link RedissonPvUv}
 *
 * @author zhangmsh 18/03/2022
 * @since 2.0.7
 */
class RedissonPvUvTest {

	@Test
	void testMultiUv() {
		// 测试多分组
		int uvStatsCycleSec = 2;
		PvUv pvUv = this.getPvUv(uvStatsCycleSec);

		Uv uv = pvUv.uv("test-group-multi-1");
		Uv uv2 = pvUv.uv("test-group-multi-2");

		Assertions.assertTrue(uv.set(1));
		Assertions.assertTrue(uv2.set(1));

		Assertions.assertFalse(uv.is(1));
		Assertions.assertFalse(uv2.is(1));

		Assertions.assertTrue(uv.remove(1));
		Assertions.assertTrue(uv2.remove(1));

		Assertions.assertTrue(uv.is(1));
		Assertions.assertTrue(uv2.is(1));

		uv.clean();
		uv2.clean();
	}

	@Test
	void testUv() {
		int uvStatsCycleSec = 2;
		PvUv pvUv = this.getPvUv(uvStatsCycleSec);

		Uv uv = pvUv.uv("test-group");

		Assertions.assertTrue(uv.is(0));
		Assertions.assertTrue(uv.set(0));
		Assertions.assertFalse(uv.set(0));
		Assertions.assertFalse(uv.is(0));

		// ====== 测试删除 ======
		Assertions.assertTrue(uv.set(1));
		// 此时1不是UV，所以删除应该返回true
		Assertions.assertTrue(uv.remove(1));
		Assertions.assertTrue(uv.is(1));
		// 此时1已经不是UV，所以删除应该返回false
		Assertions.assertFalse(uv.remove(1));
		Assertions.assertTrue(uv.is(1));
		// 2本来就不是UV，所以删除应该返回false
		Assertions.assertTrue(uv.is(2));
		Assertions.assertFalse(uv.remove(2));

		// 周期
		uv.set(3);
		Assertions.assertFalse(uv.is(3));
		try {
			Thread.sleep(uvStatsCycleSec * 1000 + 200);
			Assertions.assertTrue(uv.is(3));

			uv.set(1);
			uv.set(2);
			uv.clean();
			Assertions.assertTrue(uv.is(1));
			Assertions.assertTrue(uv.is(2));
			Assertions.assertTrue(uv.is(3));
		}
		catch (InterruptedException e) {
			throw new RuntimeException(e);
		}

	}

	private PvUv getPvUv(int uvStatsCycleSec) {
		Config defaultSingleServerConfig = RedissonAdapter.getDefaultSingleServerConfig();
		SingleServerConfig singleServerConfig = defaultSingleServerConfig.useSingleServer();
		singleServerConfig.setPassword("123456");
		RedissonClient redissonClient = RedissonAdapter.createRedissonClient(defaultSingleServerConfig);

		PvUvConfig pvUvConfig = new PvUvConfig();
		pvUvConfig.setUvStatsCycleSec(uvStatsCycleSec);
		return new RedissonPvUv(redissonClient, pvUvConfig);
		// return PvUvImpl.register(group -> {
		// RedissonPvUvProcessor processor = new RedissonPvUvProcessor(redissonClient);
		// PvUvConfig pvUvConfig = new PvUvConfig(group);
		// pvUvConfig.setUvStatsCycleSec(uvStatsCycleSec);
		// processor.setPvUvConfig(pvUvConfig);
		// processor.afterPropertiesSet();
		// return processor;
		// });
	}

}