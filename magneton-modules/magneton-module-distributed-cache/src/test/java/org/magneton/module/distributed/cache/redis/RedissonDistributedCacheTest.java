package org.magneton.module.distributed.cache.redis;

import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.magneton.core.base.Stopwatch;
import org.magneton.module.distributed.cache.TestRedisson;
import org.magneton.module.distributed.cache.ops.HashOps;
import org.magneton.module.distributed.cache.ops.ListOps;
import org.magneton.module.distributed.cache.ops.ValueOps;
import org.magneton.test.helper.Human;

/**
 * .
 *
 * @author zhangmsh 2022/2/9
 * @since 1.2.0
 */
class RedissonDistributedCacheTest extends TestRedisson {

	@Test
	void opsForHash() {
		HashOps hashOps = distributedCache.opsForHash();
		Assertions.assertNotNull(hashOps);
	}

	@Test
	void opsForList() {
		ListOps listOps = distributedCache.opsForList();
		Assertions.assertNotNull(listOps);
	}

	@Test
	void opsForValue() {
		ValueOps valueOps = distributedCache.opsForValue();
		Assertions.assertNotNull(valueOps);
	}

	@Test
	void ttl() {
		ValueOps valueOps = distributedCache.opsForValue();
		valueOps.setEx("ttl", "ttl", 10);

		long ttl = distributedCache.ttl("ttl");
		Assertions.assertTrue(ttl <= 10, "过期时间不在范围内,ttl=" + ttl);

		// test miss.
		long ttl1 = distributedCache.ttl("ttl-nil");
		Assertions.assertEquals(-2, ttl1, "过期时间不在范围内,ttl=" + ttl1);
	}

	@Test
	void expire() {
		ValueOps valueOps = distributedCache.opsForValue();
		valueOps.set("expire", "expire");

		// permanent
		long ttl = distributedCache.ttl("expire");
		Assertions.assertEquals(-1, ttl);

		boolean expire = distributedCache.expire("expire", 10);
		Assertions.assertTrue(expire);
		expire = distributedCache.expire("expire", 10);
		Assertions.assertTrue(expire);
		expire = distributedCache.expire("expire-nil", 10);
		Assertions.assertFalse(expire);
	}

	@Test
	void expireByOther() {
		ValueOps valueOps = distributedCache.opsForValue();
		valueOps.set("expireByOther", "expire");
		valueOps.set("expireByOtherKey", "expire");

		boolean expire = distributedCache.expire("expireByOther", 10);
		Assertions.assertTrue(expire);

		expire = distributedCache.expireByOther("expireByOtherKey", "expireByOther");
		Assertions.assertTrue(expire);
		distributedCache.del("expireByOther");

		expire = distributedCache.expireByOther("expireByOtherKey", "expireByOther");
		Assertions.assertFalse(expire);

		long ttl = distributedCache.ttl("expireByOtherKey");
		Assertions.assertTrue(ttl > 0 && ttl <= 10, "过期时间不在范围内");
	}

	@Test
	void exists() {
		boolean exists = distributedCache.exists("exist-nil");
		Assertions.assertFalse(exists);

		ValueOps valueOps = distributedCache.opsForValue();
		valueOps.set("exists", "exists");

		exists = distributedCache.exists("exists");
		Assertions.assertTrue(exists);
	}

	@Test
	void del() {
		ValueOps valueOps = distributedCache.opsForValue();
		valueOps.set("del", "del");

		long del = distributedCache.del("del", "del-nil");
		Assertions.assertEquals(1, del);

		boolean exist = distributedCache.exists("del");
		Assertions.assertFalse(exist);

		del = distributedCache.del("del-nil");
		Assertions.assertEquals(0, del);
	}

	@Test
	void flushDb() {
		ValueOps valueOps = distributedCache.opsForValue();
		valueOps.set("flushDb", "flushDb");

		distributedCache.flushDb();

		boolean exist = distributedCache.exists("flushDb");
		Assertions.assertFalse(exist);
	}

	@Test
	void keys() {
		ValueOps valueOps = distributedCache.opsForValue();
		Stopwatch sw = Stopwatch.createStarted();
		for (int i = 0; i < 10_000; i++) {
			valueOps.set(String.valueOf(i), "keys");
		}
		System.out.println("set:" + sw.stop());

		sw = Stopwatch.createStarted();
		List<String> keys = distributedCache.keys("1*1");
		System.out.println("keys:" + sw.stop());
		Human.sout(keys);
		Assertions.assertEquals(111, keys.size());
	}

}