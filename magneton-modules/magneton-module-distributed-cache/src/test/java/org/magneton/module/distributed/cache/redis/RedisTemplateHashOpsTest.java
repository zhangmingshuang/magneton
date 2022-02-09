package org.magneton.module.distributed.cache.redis;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.magneton.core.collect.Lists;
import org.magneton.core.collect.Maps;
import org.magneton.module.distributed.cache.KV;
import org.magneton.module.distributed.cache.TestA;
import org.magneton.module.distributed.cache.TestRedisTemplate;
import org.magneton.module.distributed.cache.ops.HashOps;
import org.magneton.test.ChaosTest;
import org.magneton.test.helper.Human;

/**
 * .
 *
 * @author zhangmsh 2022/2/9
 * @since 1.2.0
 */
class RedisTemplateHashOpsTest extends TestRedisTemplate {

	private HashOps ops = distributedCache.opsForHash();

	@Test
	void put() {
		boolean put = this.ops.put("hash", "a", "b");
		Assertions.assertTrue(put);

		boolean putObj = this.ops.put("hash", "a-obj", ChaosTest.createExcepted(TestA.class));
		Assertions.assertTrue(putObj);
	}

	@Test
	void put_map() {
		Map<String, TestA> map = Maps.newHashMapWithExpectedSize(10);
		for (int i = 0; i < 10; i++) {
			map.put(String.valueOf(i), ChaosTest.createExcepted(TestA.class));
		}
		boolean put = this.ops.put("hash-map", map);
		Assertions.assertTrue(put);
	}

	@Test
	void put_list() {
		List<KV<TestA>> list = Lists.newArrayListWithCapacity(10);
		for (int i = 0; i < 10; i++) {
			list.add(KV.of(String.valueOf(i), ChaosTest.createExcepted(TestA.class)));
		}
		boolean put = this.ops.put("hash-map", list);
		Assertions.assertTrue(put);
	}

	@Test
	void get() {
		TestA excepted = ChaosTest.createExcepted(TestA.class);
		boolean put = this.ops.put("hash-get", "a", "b");
		Assertions.assertTrue(put);
		boolean putObj = this.ops.put("hash-get", "a-obj", excepted);
		Assertions.assertTrue(putObj);

		String a = this.ops.get("hash-get", "a");
		Assertions.assertEquals("b", a);
		TestA testA = this.ops.get("hash-get", "a-obj", TestA.class);
		Assertions.assertTrue(ChaosTest.booleanSupplier().valueEquals(excepted, testA));
	}

	@Test
	void get_map() {
		Map<String, TestA> map = Maps.newHashMapWithExpectedSize(10);
		for (int i = 0; i < 10; i++) {
			map.put(String.valueOf(i), ChaosTest.createExcepted(TestA.class));
		}
		boolean put = this.ops.put("hash-get-map", map);
		Assertions.assertTrue(put);

		Map<String, String> result = this.ops.get("hash-get-map", "0", "1", "2");
		Human.sout(result);
		Assertions.assertEquals(3, result.size());

		Map<String, TestA> resultObj = this.ops.get("hash-get-map", new String[] { "0", "1", "2", "-1" }, TestA.class);
		Human.sout(result);
		Assertions.assertEquals(3, result.size());
		Assertions.assertTrue(ChaosTest.booleanSupplier().valueEquals(map.get("0"), resultObj.get("0")));
		Assertions.assertTrue(ChaosTest.booleanSupplier().valueEquals(map.get("1"), resultObj.get("1")));
		Assertions.assertTrue(ChaosTest.booleanSupplier().valueEquals(map.get("2"), resultObj.get("2")));
		Assertions.assertNull(result.get("-1"));
	}

	@Test
	void containsKey() {
		Map<String, TestA> map = Maps.newHashMapWithExpectedSize(10);
		for (int i = 0; i < 10; i++) {
			map.put(String.valueOf(i), ChaosTest.createExcepted(TestA.class));
		}
		boolean put = this.ops.put("hash-containsKey", map);
		Assertions.assertTrue(put);

		boolean b = this.ops.containsKey("hash-containsKey", "0");
		Assertions.assertTrue(b);

		boolean b1 = this.ops.containsKey("hash-containsKey", "-1");
		Assertions.assertFalse(b1);
	}

	@Test
	void remove() {
		Map<String, TestA> map = Maps.newHashMapWithExpectedSize(10);
		for (int i = 0; i < 10; i++) {
			map.put(String.valueOf(i), ChaosTest.createExcepted(TestA.class));
		}
		boolean put = this.ops.put("hash-remove", map);
		Assertions.assertTrue(put);

		long remove = this.ops.remove("hash-remove", "0", "1", "-1");
		Assertions.assertEquals(2, remove);
	}

	@Test
	void size() {
		Map<String, TestA> map = Maps.newHashMapWithExpectedSize(10);
		for (int i = 0; i < 10; i++) {
			map.put(String.valueOf(i), ChaosTest.createExcepted(TestA.class));
		}
		boolean put = this.ops.put("hash-size", map);
		Assertions.assertTrue(put);

		long size = this.ops.size("hash-size");
		Assertions.assertEquals(10, size);

		long remove = this.ops.remove("hash-size", "0", "1", "-1");
		Assertions.assertEquals(2, remove);

		long size1 = this.ops.size("hash-size");
		Assertions.assertEquals(8, size1);
	}

	@Test
	void keys() {
		Map<String, TestA> map = Maps.newHashMapWithExpectedSize(10);
		for (int i = 0; i < 10; i++) {
			map.put(String.valueOf(i), ChaosTest.createExcepted(TestA.class));
		}
		boolean put = this.ops.put("hash-keys", map);
		Assertions.assertTrue(put);

		Set<String> keys = this.ops.keys("hash-keys");
		Assertions.assertEquals(10, keys.size());
	}

	@Test
	void values() {
		Map<String, TestA> map = Maps.newHashMapWithExpectedSize(10);
		for (int i = 0; i < 10; i++) {
			map.put(String.valueOf(i), ChaosTest.createExcepted(TestA.class));
		}
		boolean put = this.ops.put("hash-values", map);
		Assertions.assertTrue(put);

		List<String> values = this.ops.values("hash-values");
		Assertions.assertEquals(10, values.size());

		List<TestA> valuesObj = this.ops.values("hash-values", TestA.class);
		Assertions.assertEquals(10, valuesObj.size());
	}

	@Test
	void all() {
		Map<String, TestA> map = Maps.newHashMapWithExpectedSize(10);
		for (int i = 0; i < 10; i++) {
			map.put(String.valueOf(i), ChaosTest.createExcepted(TestA.class));
		}
		boolean put = this.ops.put("hash-all", map);
		Assertions.assertTrue(put);

		Map<String, String> all = this.ops.all("hash-all");
		Assertions.assertEquals(10, all.size());

		Map<String, TestA> allObj = this.ops.all("hash-all", TestA.class);
		Assertions.assertEquals(10, allObj.size());
	}

}