package org.magneton.module.distributed.cache.redis;

import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.magneton.core.primitives.Booleans;
import org.magneton.module.distributed.cache.EKV;
import org.magneton.module.distributed.cache.KV;
import org.magneton.module.distributed.cache.TestA;
import org.magneton.module.distributed.cache.TestRedisTemplate;
import org.magneton.test.ChaosTest;
import org.magneton.test.helper.Human;

/**
 * .
 *
 * @author zhangmsh 2022/2/8
 * @since 1.2.0
 */
class RedisTemplateValueOpsTest extends TestRedisTemplate {

	private RedisTemplateValueOps ops = new RedisTemplateValueOps(redisTemplate, redisValueSerializer);

	@Test
	void set() {
		boolean set = this.ops.set("a", "b");
		Assertions.assertTrue(set, "set a string");
	}

	@Test
	void set_kv() {
		boolean set = this.ops.set(KV.of("a-kv", ChaosTest.createExcepted(TestA.class)));
		Assertions.assertTrue(set, "set a class: TestA");
	}

	@Test
	void set_kv_list_array() {
		List<Boolean> set = this.ops.set(KV.of("a-kv-list-array-0", ChaosTest.createExcepted(TestA.class)),
				KV.of("a-kv-list-array-1", ChaosTest.createExcepted(TestA.class)),
				KV.of("a-kv-list-array-2", ChaosTest.createExcepted(TestA.class)),
				KV.of("a-kv-list-array-3", ChaosTest.createExcepted(TestA.class)),
				KV.of("a-kv-list-array-4", ChaosTest.createExcepted(TestA.class)));
		Human.sout(set);
		boolean[] booleans = Booleans.toArray(set);
		Assertions.assertEquals(5, Booleans.countTrue(booleans));
	}

	@Test
	void setNx() {
		boolean setNx = this.ops.setNx("nx", "nx");
		Assertions.assertTrue(setNx, "first set must be true");
		boolean setNx2 = this.ops.setNx(KV.of("nx", "nx"));
		Assertions.assertFalse(setNx2, "duplicate set must be false.");
	}

	@Test
	void setNx_list_array() {
		boolean setNx = this.ops.setNx("a-nx-list-array-2", "a-nx-list-array-2");
		Assertions.assertTrue(setNx, "first set must be true");

		List<Boolean> set = this.ops.setNx(KV.of("a-nx-list-array-0", ChaosTest.createExcepted(TestA.class)),
				KV.of("a-nx-list-array-1", ChaosTest.createExcepted(TestA.class)),
				KV.of("a-nx-list-array-2", ChaosTest.createExcepted(TestA.class)),
				KV.of("a-nx-list-array-3", ChaosTest.createExcepted(TestA.class)),
				KV.of("a-nx-list-array-4", ChaosTest.createExcepted(TestA.class)));
		Human.sout(set);
		boolean[] booleans = Booleans.toArray(set);
		Assertions.assertEquals(4, Booleans.countTrue(booleans));
		Assertions.assertFalse(booleans[2], "duplicate set at index 2 must be false.");
	}

	@Test
	void setEx() {
		boolean set = this.ops.setEx("ex", "ex", 10_000);
		Assertions.assertTrue(set, "setEx");
		long ttl = distributedCache.ttl("ex");
		Human.sout("ttl", ttl);
		Assertions.assertTrue(ttl > 0 && ttl <= 10_000, "ttl");
	}

	@Test
	void setEx_list_array() {
		int ttl = 300;
		List<Boolean> set = this.ops.setEx(EKV.of("a-ex-list-array-0", ChaosTest.createExcepted(TestA.class), ttl),
				EKV.of("a-ex-list-array-1", ChaosTest.createExcepted(TestA.class), ttl),
				EKV.of("a-ex-list-array-2", ChaosTest.createExcepted(TestA.class), ttl),
				EKV.of("a-ex-list-array-3", ChaosTest.createExcepted(TestA.class), ttl),
				EKV.of("a-ex-list-array-4", ChaosTest.createExcepted(TestA.class), ttl));
		Human.sout(set);
		boolean[] booleans = Booleans.toArray(set);
		Assertions.assertEquals(5, Booleans.countTrue(booleans));
	}

	@Test
	void get() {
		boolean set = this.ops.set("get", "get");
		Assertions.assertTrue(set);
		String get = this.ops.get("get");
		Assertions.assertEquals("get", get, "must be 'get'");
		String get2 = this.ops.get("nil-get");
		Assertions.assertNull(get2, "nil key must be null");
	}

	@Test
	void get_obj() {
		TestA testA = ChaosTest.createExcepted(TestA.class);
		boolean set = this.ops.set("get_obj", testA);
		Assertions.assertTrue(set);
		TestA testA1 = this.ops.get("get_obj", TestA.class);
		Assertions.assertTrue(ChaosTest.booleanSupplier().valueEquals(testA, testA1));
	}

}