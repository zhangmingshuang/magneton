package org.magneton.cache.redis;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.magneton.cache.Cache;
import org.magneton.cache.CacheTestApplication;
import org.magneton.cache.ops.HashOps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * .
 *
 * @author zhangmsh 2021/6/25
 * @since
 */
@SpringBootTest(classes = CacheTestApplication.class)
class RedisTemplateHashOpsTest {

	@Autowired
	private RedisTemplate redisTemplate;

	@Autowired
	private JsonRedisSerializer jsonRedisSerializer;

	private Cache cache;

	private HashOps hashOps;

	@BeforeEach
	void prepare() {
		this.cache = new RedisTemplateCache(this.redisTemplate, this.jsonRedisSerializer);
		this.hashOps = this.cache.opsForHash();
	}

	@Test
	void testPut() {
		String key = "test_hash_put_key";
		this.cache.del(key);
		try {
			this.hashOps.put(key, "a", "b");
			String a = this.hashOps.get(key, "a");
			Assertions.assertEquals("b", a);
			a = this.hashOps.get(key, "non");
			Assertions.assertNull(a);
		}
		finally {
			this.cache.del(key);
		}
	}

	@Test
	void testMapPut() {
		String key = "test_hash_put_map_key";
		this.cache.del(key);
		try {
			Map<String, Object> map = new HashMap<>();
			map.put("a", "a");
			map.put("b", "b");
			map.put("c", "c");
			this.hashOps.put(key, map);
			String b = this.hashOps.get(key, "b");
			Assertions.assertEquals("b", b);
		}
		finally {
			this.cache.del(key);
		}
	}

	@Test
	void testGet() {
		String key = "test_hash_get_key";
		this.cache.del(key);
		try {
			Map<String, Object> map = new HashMap<>();
			map.put("a", "a");
			map.put("b", "b");
			map.put("c", "c");
			this.hashOps.put(key, map);
			Map<String, String> ab = this.hashOps.get(key, "a", "n", "b", "d");
			Assertions.assertEquals("a", ab.get("a"));
			Assertions.assertEquals("b", ab.get("b"));
			Assertions.assertNull(ab.get("d"));
			Assertions.assertNull(ab.get("n"));
		}
		finally {
			this.cache.del(key);
		}
	}

	@Test
	void testContainsKey() {
		String key = "test_hash_contains_key";
		this.cache.del(key);
		try {
			Map<String, Object> map = new HashMap<>();
			map.put("a", "a");
			map.put("b", "b");
			map.put("c", "c");
			this.hashOps.put(key, map);
			Assertions.assertTrue(() -> {
				return this.hashOps.containsKey(key, "a") && this.hashOps.containsKey(key, "b")
						&& this.hashOps.containsKey(key, "c");
			});
		}
		finally {
			this.cache.del(key);
		}
	}

	@Test
	void testRemove() {
		String key = "test_hash_remove_key";
		this.cache.del(key);
		try {
			Map<String, Object> map = new HashMap<>();
			map.put("a", "a");
			map.put("b", "b");
			map.put("c", "c");
			this.hashOps.put(key, map);
			long a = this.hashOps.remove(key, "a");
			Assertions.assertEquals(1, a);
			long remove = this.hashOps.remove("key", "b", "c", "d");
			Assertions.assertEquals(2, remove);
		}
		finally {
			this.cache.del(key);
		}
	}

	@Test
	void testSize() {
		String key = "test_hash_remove_key";
		this.cache.del(key);
		try {
			Map<String, Object> map = new HashMap<>();
			map.put("a", "a");
			map.put("b", "b");
			map.put("c", "c");
			this.hashOps.put(key, map);
			long size = this.hashOps.size(key);
			Assertions.assertEquals(3, size);
		}
		finally {
			this.cache.del(key);
		}
	}

	@Test
	void testKeys() {
		String key = "test_hash_keys_key";
		this.cache.del(key);
		try {
			Map<String, Object> map = new HashMap<>();
			map.put("a", "a");
			map.put("b", "b");
			map.put("c", "c");
			this.hashOps.put(key, map);
			Set<String> keys = this.hashOps.keys(key);
			Assertions.assertEquals("[a, b, c]", keys.toString());
		}
		finally {
			this.cache.del(key);
		}
	}

	@Test
	void testValues() {
		String key = "test_hash_values_key";
		this.cache.del(key);
		try {
			Map<String, Object> map = new HashMap<>();
			map.put("a", "1a");
			map.put("b", "2b");
			map.put("c", "3c");
			this.hashOps.put(key, map);
			List<String> values = this.hashOps.values(key);
			Assertions.assertTrue(() -> values.containsAll(Arrays.asList("1a", "2b", "3c")));
		}
		finally {
			this.cache.del(key);
		}
	}

	@Test
	void testAll() {
		String key = "test_hash_values_key";
		this.cache.del(key);
		try {
			Map<String, Object> map = new HashMap<>();
			map.put("a", "1a");
			map.put("b", "2b");
			map.put("c", "3c");
			this.hashOps.put(key, map);
			Map<String, String> all = this.hashOps.all(key);
			Assertions.assertTrue(() -> all.containsKey("a") && all.containsKey("b") && all.containsKey("c")
					&& all.containsValue("1a") && all.containsValue("2b") && all.containsValue("3c"));
		}
		finally {
			this.cache.del(key);
		}
	}

}
