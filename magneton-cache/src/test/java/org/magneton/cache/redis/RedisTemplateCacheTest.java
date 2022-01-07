package org.magneton.cache.redis;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.magneton.cache.Cache;
import org.magneton.cache.CacheTestApplication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * .
 *
 * @author zhangmsh 2021/6/24
 * @since 1.0.0
 */
@SpringBootTest(classes = CacheTestApplication.class)
public class RedisTemplateCacheTest {

	@Autowired
	private RedisTemplate redisTemplate;

	@Autowired
	private JsonRedisSerializer jsonRedisSerializer;

	private Cache cache;

	@BeforeEach
	void prepare() {
		this.cache = new RedisTemplateCache(this.redisTemplate, this.jsonRedisSerializer);
	}

	@Test
	void testExpire() {
		String expireKey = "expire-key";
		this.cache.opsForValue().set(expireKey, "expire-value");
		boolean expire = this.cache.expire(expireKey, 3);
		Assertions.assertTrue(expire);
		long ttl = this.cache.ttl(expireKey);
		Assertions.assertTrue(ttl > 0 && ttl <= 3);
	}

	@Test
	void testTtl() {
		String key = "ttl-key";
		boolean val = this.cache.opsForValue().set(key, "val");
		Assertions.assertTrue(val);
		long ttl = this.cache.ttl(key);
		Assertions.assertEquals(-1, ttl, "stable key must be -1");
		boolean expire = this.cache.expire(key, 3);
		Assertions.assertTrue(expire);
		ttl = this.cache.ttl(key);
		Assertions.assertTrue(ttl > 0 && ttl <= 3);
		ttl = this.cache.ttl("non-expire");
		Assertions.assertEquals(ttl, -2);
	}

	@Test
	void testDel() {
		this.cache.opsForValue().set("a", "b");
		long a = this.cache.del("a");
		Assertions.assertEquals(a, 1);
		this.cache.opsForValue().set("a1", "b");
		this.cache.opsForValue().set("a2", "b");
		this.cache.opsForValue().set("a3", "b");
		a = this.cache.del("a1", "a2", "a3");
		Assertions.assertEquals(a, 3);
	}

}
