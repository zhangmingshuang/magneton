package org.magneton.module.distributed.cache.redis;

import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.magneton.core.base.Randoms;
import org.magneton.core.collect.Lists;
import org.magneton.module.distributed.cache.TestA;
import org.magneton.module.distributed.cache.TestRedisTemplate;
import org.magneton.module.distributed.cache.ops.ListOps;
import org.magneton.test.ChaosTest;
import org.magneton.test.helper.Human;

/**
 * .
 *
 * @author zhangmsh 2022/2/8
 * @since 1.2.0
 */
class RedisTemplateListOpsTest extends TestRedisTemplate {

	private ListOps ops = new RedisTemplateListOps(redisTemplate, redisValueSerializer);

	@Test
	void add() {
		long add = this.ops.add("list", "a", "b", "c");
		Assertions.assertEquals(3, add);
		long add1 = this.ops.add("list", "d");
		Assertions.assertEquals(4, add1);
	}

	@Test
	void add_obj() {
		List<TestA> testAs = this.getTestAS(10);
		long add = this.ops.add("list-obj", testAs);
		Assertions.assertEquals(10, add);
	}

	private List<TestA> getTestAS(int size) {
		List<TestA> testAs = Lists.newArrayList();
		for (int i = 0; i < size; i++) {
			TestA excepted = ChaosTest.createExcepted(TestA.class);
			testAs.add(excepted);
		}
		return testAs;
	}

	@Test
	void addAtHead() {
		long add = this.ops.add("list-head", "a", "b", "c");
		Assertions.assertEquals(3, add);

		long d = this.ops.addAtHead("list-head", "d");
		Assertions.assertEquals(4, d);

		String s = this.ops.get("list-head", 0);
		Assertions.assertEquals("d", s);
	}

	@Test
	void range() {
		long add = this.ops.add("list-range", "a", "b", "c");
		Assertions.assertEquals(3, add);

		List<String> range = this.ops.range("list-range", 0, 11);
		Human.sout(range);
		Assertions.assertEquals(3, range.size());

		List<String> range1 = this.ops.range("list-range", -2, -1);
		Human.sout(range1);
		Assertions.assertEquals(range1.get(0), "b");
		Assertions.assertEquals(range1.get(1), "c");
	}

	@Test
	void range_class() {
		List<TestA> testAs = this.getTestAS(10);
		long add = this.ops.add("list-range-clazz", testAs);
		Assertions.assertEquals(10, add);
		List<TestA> range = this.ops.range("list-range-clazz", 0, 0, TestA.class);
		Human.sout(range);
		Assertions.assertEquals(1, range.size());

		Assertions.assertTrue(ChaosTest.booleanSupplier().valueEquals(testAs.get(0), range.get(0)));
	}

	@Test
	void size() {
		int size = Randoms.nextInt(1, 50);
		List<TestA> testAs = this.getTestAS(size);
		long add = this.ops.add("list-size", testAs);
		Assertions.assertEquals(size, add);

		long size1 = this.ops.size("list-size");
		Assertions.assertEquals(size, size1);
	}

	@Test
	void get() {
		int size = Randoms.nextInt(1, 50);
		List<TestA> testAs = this.getTestAS(size);
		long add = this.ops.add("list-get", testAs);
		Assertions.assertEquals(size, add);

		String s0 = this.ops.get("list-get", 0);
		Assertions.assertNotNull(s0);

		TestA testA = this.ops.get("list-get", size - 1, TestA.class);
		Assertions.assertNotNull(testA);

		Assertions.assertTrue(ChaosTest.booleanSupplier().valueEquals(testAs.get(size - 1), testA));
	}

	@Test
	void remove() {
		long add = this.ops.add("list-remove", "a", "b", "c");
		Assertions.assertEquals(3, add);

		long a = this.ops.remove("list-remove", "a");
		Assertions.assertEquals(1, a);

		long remove = this.ops.remove("list-remove", "a");
		Assertions.assertEquals(0, remove);
	}

	@Test
	void remove_obj() {
		List<TestA> testAs = this.getTestAS(10);
		long add = this.ops.add("list-remove-obj", testAs);
		Assertions.assertEquals(10, add);

		long remove = this.ops.remove("list-remove-obj", testAs.get(0));
		Assertions.assertEquals(1, remove);

		long size = this.ops.size("list-remove-obj");
		Assertions.assertEquals(9, size);

		for (TestA testA : testAs) {
			this.ops.remove("list-remove-obj", testA);
		}
		long size1 = this.ops.size("list-remove-obj");
		Assertions.assertEquals(0, size1);
	}

}