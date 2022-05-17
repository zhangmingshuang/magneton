package org.magneton.module.distributed.cache.redis;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.magneton.module.distributed.cache.Entry;
import org.magneton.module.distributed.cache.ExpireEntry;
import org.magneton.module.distributed.cache.TestA;
import org.magneton.module.distributed.cache.TestRedisson;
import org.magneton.module.distributed.cache.ops.ValueOps;
import org.magneton.test.ChaosTest;
import org.magneton.test.helper.Human;

/**
 * .
 *
 * @author zhangmsh 2022/2/8
 * @since 1.2.0
 */
class RedissonValueOpsTest extends TestRedisson {

	private ValueOps ops = distributedCache.opsForValue();

	@Test
	void set() {
		this.ops.set("a", "b");
		String b = this.ops.get("a");
		System.out.println(b);
		Assertions.assertEquals("b", b);
	}

	@Test
	void set_kv() {
		TestA a = ChaosTest.createExcepted(TestA.class);
		this.ops.set(Entry.of("a-kv", a));
		TestA b = this.ops.get("a-kv");
		Assertions.assertTrue(ChaosTest.booleanSupplier().valueEquals(a, b));
	}

	@Test
	void set_kv_list_array() {
		TestA a = ChaosTest.createExcepted(TestA.class);
		this.ops.set(Entry.of("a-kv-list-array-0", ChaosTest.createExcepted(TestA.class)),
				Entry.of("a-kv-list-array-1", ChaosTest.createExcepted(TestA.class)), Entry.of("a-kv-list-array-2", a),
				Entry.of("a-kv-list-array-3", ChaosTest.createExcepted(TestA.class)),
				Entry.of("a-kv-list-array-4", ChaosTest.createExcepted(TestA.class)));
		TestA b = this.ops.get("a-kv-list-array-2");
		Assertions.assertTrue(ChaosTest.booleanSupplier().valueEquals(a, b));
	}

	@Test
	void setNx() {
		boolean setNx = this.ops.setNx("nx", "nx");
		Assertions.assertTrue(setNx, "first set must be true");
		boolean setNx2 = this.ops.setNx(Entry.of("nx", "nx"));
		Assertions.assertFalse(setNx2, "duplicate set must be false.");
	}

	@Test
	void setNx_list_array() {
		boolean setNx = this.ops.setNx("a-nx-list-array-2", "a-nx-list-array-2");
		Assertions.assertTrue(setNx, "first set must be true");

		this.ops.setNx(Entry.of("a-nx-list-array-0", ChaosTest.createExcepted(TestA.class)),
				Entry.of("a-nx-list-array-1", ChaosTest.createExcepted(TestA.class)),
				Entry.of("a-nx-list-array-2", ChaosTest.createExcepted(TestA.class)),
				Entry.of("a-nx-list-array-3", ChaosTest.createExcepted(TestA.class)),
				Entry.of("a-nx-list-array-4", ChaosTest.createExcepted(TestA.class)));
		String b = this.ops.get("a-nx-list-array-2");
		Assertions.assertEquals("a-nx-list-array-2", b);
	}

	@Test
	void setEx() {
		this.ops.setEx("ex", "ex", 10_000);
		long ttl = distributedCache.ttl("ex");
		Human.sout("ttl", ttl);
		Assertions.assertTrue(ttl > 0 && ttl <= 10_000, "ttl");
	}

	@Test
	void setEx_list_array() {
		int ttl = 300;
		this.ops.setEx(ExpireEntry.of("a-ex-list-array-0", ChaosTest.createExcepted(TestA.class), ttl),
				ExpireEntry.of("a-ex-list-array-1", ChaosTest.createExcepted(TestA.class), ttl),
				ExpireEntry.of("a-ex-list-array-2", ChaosTest.createExcepted(TestA.class), ttl),
				ExpireEntry.of("a-ex-list-array-3", ChaosTest.createExcepted(TestA.class), ttl),
				ExpireEntry.of("a-ex-list-array-4", ChaosTest.createExcepted(TestA.class), ttl));
		long t = distributedCache.ttl("a-ex-list-array-3");
		Assertions.assertTrue(t > 0 && t <= 300);
	}

	@Test
	void get_obj() {
		TestA testA = ChaosTest.createExcepted(TestA.class);
		this.ops.set("get_obj", testA);

		TestA testA1 = this.ops.get("get_obj");
		Assertions.assertTrue(ChaosTest.booleanSupplier().valueEquals(testA, testA1));
	}

	@Test
	void trySet() {
		String key = "trySetKey";
		String value = "value";
		boolean trySet = this.ops.trySet(key, value, 11);
		Assertions.assertTrue(trySet);
		trySet = this.ops.trySet(key, "value2", 11);
		Assertions.assertFalse(trySet);

		Assertions.assertEquals(value, this.ops.get(key));
	}

}