package org.magneton.module.distributed.cache.redis;

import com.google.common.collect.Sets;
import java.util.Set;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.magneton.module.distributed.cache.TestRedisson;
import org.magneton.module.distributed.cache.ops.SetOps;

/**
 * @author zhangmsh 2022/5/4
 * @since 1.0.0
 */
class RedissonSetOpsTest extends TestRedisson {

	private SetOps ops = distributedCache.opsForSet();

	@Test
	void addArray() {
		boolean put = this.ops.add("addArray", "a");
		Assertions.assertTrue(put);
		Assertions.assertFalse(this.ops.add("addArray", "a"));
		Assertions.assertTrue(this.ops.add("addArray", "a", "b"));
		Assertions.assertFalse(this.ops.add("addArray", "a", "b"));
	}

	@Test
	void addCollection() {
		Set<String> set = Sets.newHashSet("a", "b");
		Assertions.assertTrue(this.ops.add("addCollection", set));
		Assertions.assertFalse(this.ops.add("addCollection", set));
		set.add("c");
		Assertions.assertTrue(this.ops.add("addCollection", set));
		Assertions.assertFalse(this.ops.add("addCollection", set));
	}

	@Test
	void get() {
		Set<String> set = Sets.newHashSet("a", "b");
		Assertions.assertTrue(this.ops.add("get", set));

		Set<String> test = this.ops.get("get");
		Assertions.assertEquals(set, test);
	}

	@Test
	void size() {
		Assertions.assertEquals(0, this.ops.size("size"));
		Set<String> set = Sets.newHashSet("a", "b");
		Assertions.assertTrue(this.ops.add("size", set));
		Assertions.assertEquals(2, this.ops.size("size"));
	}

	@Test
	void isEmpty() {
		Assertions.assertTrue(this.ops.isEmpty("isEmpty"));
		Set<String> set = Sets.newHashSet("a", "b");
		Assertions.assertTrue(this.ops.add("isEmpty", set));
		Assertions.assertFalse(this.ops.isEmpty("isEmpty"));
	}

	@Test
	void contains() {
		Assertions.assertFalse(this.ops.contains("contains", "a"));
		Set<String> set = Sets.newHashSet("a", "b");
		Assertions.assertTrue(this.ops.add("contains", set));
		Assertions.assertTrue(this.ops.contains("contains", "a"));
	}

	@Test
	void remove() {
		Assertions.assertFalse(this.ops.remove("remove", "a"));

		Set<String> set = Sets.newHashSet("a", "b");
		Assertions.assertTrue(this.ops.add("remove", set));

		Assertions.assertTrue(this.ops.remove("remove", "a"));
		Assertions.assertFalse(this.ops.remove("remove", "a"));
	}

}