package org.magneton.module.distributed.cache.redis;

import com.google.common.collect.Lists;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.magneton.module.distributed.cache.TestA;
import org.magneton.module.distributed.cache.TestRedisson;
import org.magneton.module.distributed.cache.ops.ListOps;
import org.magneton.test.ChaosTest;
import org.magneton.test.helper.Human;

/**
 * .
 *
 * @author zhangmsh 2022/2/8
 * @since 1.2.0
 */
class RedissonListOpsTest extends TestRedisson {

	private ListOps ops = distributedCache.opsForList();

	@Test
	void add() {
		this.ops.add("list", "a", "b", "c");
		this.ops.add("list", "d");

		String d = this.ops.get("list", 3);
		Assertions.assertEquals("d", d);

		String c = this.ops.get("list", -2);
		Assertions.assertEquals("c", c);
	}

	@Test
	void add_obj() {
		List<TestA> testAs = this.getTestAS(10);
		this.ops.add("list-obj", testAs);

		TestA b = this.ops.get("list-obj", 1);
		Human.sout(testAs.get(1), b);
		Assertions.assertTrue(ChaosTest.booleanSupplier().valueEquals(testAs.get(1), b));
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
		this.ops.add("list-head", "a", "b", "c");

		this.ops.addAtHead("list-head", "d");

		String s = this.ops.get("list-head", 0);
		Assertions.assertEquals("d", s);
	}

	@Test
	void range() {
		this.ops.add("list-range", "a", "b", "c");

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
		this.ops.add("list-range-clazz", testAs);

		List<TestA> range = this.ops.range("list-range-clazz", 0, 0);
		Human.sout(range);
		Assertions.assertEquals(1, range.size());

		Assertions.assertTrue(ChaosTest.booleanSupplier().valueEquals(testAs.get(0), range.get(0)));
	}

	@Test
	void size() {
		int size = ThreadLocalRandom.current().nextInt(1, 50);
		List<TestA> testAs = this.getTestAS(size);
		this.ops.add("list-size", testAs);

		long size1 = this.ops.size("list-size");
		Assertions.assertEquals(size, size1);
	}

	@Test
	void get() {
		int size = ThreadLocalRandom.current().nextInt(1, 50);
		List<TestA> testAs = this.getTestAS(size);
		this.ops.add("list-get", testAs);

		TestA s0 = this.ops.get("list-get", 0);
		Assertions.assertNotNull(s0);

		TestA testA = this.ops.get("list-get", size - 1);
		Assertions.assertNotNull(testA);

		Assertions.assertTrue(ChaosTest.booleanSupplier().valueEquals(testAs.get(size - 1), testA));
	}

	@Test
	void remove() {
		this.ops.add("list-remove", "a", "b", "c");

		boolean a = this.ops.remove("list-remove", "a");
		Assertions.assertTrue(a);

		boolean b = this.ops.remove("list-remove", "a");
		Assertions.assertFalse(b);
	}

	@Test
	void remove_obj() {
		List<TestA> testAs = this.getTestAS(10);
		this.ops.add("list-remove-obj", testAs);

		boolean remove = this.ops.remove("list-remove-obj", testAs.get(0));
		Assertions.assertTrue(remove);

		long size = this.ops.size("list-remove-obj");
		Assertions.assertEquals(9, size);

		for (TestA testA : testAs) {
			this.ops.remove("list-remove-obj", testA);
		}
		long size1 = this.ops.size("list-remove-obj");
		Assertions.assertEquals(0, size1);
	}

}