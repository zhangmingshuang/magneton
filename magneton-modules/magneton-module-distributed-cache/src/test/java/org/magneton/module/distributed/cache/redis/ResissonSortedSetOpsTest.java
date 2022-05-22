package org.magneton.module.distributed.cache.redis;

import com.google.common.collect.Maps;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.magneton.module.distributed.cache.TestRedisson;
import org.magneton.module.distributed.cache.ops.SortedSetOps;

/**
 * @author zhangmsh 2022/5/21
 * @since 1.0.0
 */
class ResissonSortedSetOpsTest extends TestRedisson {

	private final SortedSetOps ops = distributedCache.opsForSortedSet();

	@Test
	void add() {
		String testKey = "sortedSet-add";
		distributedCache.del(testKey);

		boolean a = this.ops.add(testKey, "a", 1);
		Assertions.assertTrue(a);

		a = this.ops.add(testKey, "a", 2);
		Assertions.assertFalse(a);

		Double scope = this.ops.getScope(testKey, "a");
		Assertions.assertEquals(2, scope);
	}

	@Test
	void addAll() {
		String testKey = "sortedSet-addAll";
		distributedCache.del(testKey);

		Map<String, Double> values = Maps.newHashMapWithExpectedSize(10);
		for (int i = 0; i < 10; i++) {
			values.put("a" + i, (double) i);
		}
		int count = this.ops.addAll(testKey, values);
		Assertions.assertEquals(10, count);

		count = this.ops.addAll(testKey, values);
		Assertions.assertEquals(0, count);
	}

	@Test
	void valueRange_index() {
		String testKey = "sortedSet-valueRange_index";
		this.preSet(testKey);
		// 获取指定范围内的元素
		Collection<String> range = this.ops.valueRange(testKey, 0, 4);
		Assertions.assertEquals(5, range.size());
		Assertions.assertEquals("[a0, a1, a2, a3, a4]", range.toString());

		// 负数，倒数最后一个元素
		range = this.ops.valueRange(testKey, -1, -1);
		Assertions.assertEquals(1, range.size());
		Assertions.assertEquals("[a9]", range.toString());
	}

	@Test
	@SuppressWarnings("ConfusingFloatingPointLiteral")
	void valueRange_scope() {
		String testKey = "sortedSet-valueRange_scope";
		this.preSet(testKey);
		// 获取2到4分的数据
		Collection<String> range = this.ops.valueRange(testKey, 2D, 4D);
		Assertions.assertEquals("[a2, a3, a4]", range.toString());

		// 不包括的
		range = this.ops.valueRange(testKey, 2D, false, 4D, false);
		Assertions.assertEquals("[a3]", range.toString());

		// 偏移量与总量
		range = this.ops.valueRange(testKey, 2D, 4D, 2, 2);
		Assertions.assertEquals("[a4]", range.toString());

		// 不包括的， 偏移量与总量
		range = this.ops.valueRange(testKey, 2D, false, 4D, false, 2, 2);
		Assertions.assertTrue(range.isEmpty());
	}

	@Test
	void remove() {
		String testKey = "sortedSet-remove";
		distributedCache.del(testKey);

		this.ops.add(testKey, "a", 1);

		boolean remove = this.ops.remove(testKey, "a");
		Assertions.assertTrue(remove);

		remove = this.ops.remove(testKey, "a");
		System.out.println(remove);
	}

	@Test
	void remove_collection() {
		String testKey = "sortedSet-remove_collection";
		this.preSet(testKey);

		boolean remove = this.ops.remove(testKey, Arrays.asList("a0", "a1", "a2"));
		Assertions.assertTrue(remove);

		remove = this.ops.remove(testKey, Arrays.asList("a0", "a3", "a4"));
		Assertions.assertTrue(remove);

		remove = this.ops.remove(testKey, Arrays.asList("a0", "a1"));
		System.out.println(remove);
	}

	@Test
	void removeRange() {
		String testKey = "sortedSet-removeRange";
		this.preSet(testKey);

		int removeRange = this.ops.removeRange(testKey, 0, 4);
		Assertions.assertEquals(5, removeRange);
	}

	private void preSet(String testKey) {
		distributedCache.del(testKey);
		Map<String, Double> values = Maps.newHashMapWithExpectedSize(10);
		for (int i = 0; i < 10; i++) {
			values.put("a" + i, (double) i);
		}
		this.ops.addAll(testKey, values);
	}

}