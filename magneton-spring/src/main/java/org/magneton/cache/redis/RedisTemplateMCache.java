package org.magneton.cache.redis;

import com.alibaba.fastjson2.JSON;
import com.google.common.base.Strings;
import org.magneton.spring.cache.MCache;
import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.Nullable;
import java.util.concurrent.TimeUnit;

/**
 * 基于RedisTemplate的缓存实现.
 *
 * @author zhangmsh
 * @since 2024
 */
@SuppressWarnings("unchecked")
public class RedisTemplateMCache implements MCache {

	private final RedisTemplate redisTemplate;

	public RedisTemplateMCache(RedisTemplate redisTemplate) {
		this.redisTemplate = redisTemplate;
	}

	@Override
	public String clientId() {
		return this.redisTemplate.getClass().getName();
	}

	@Override
	public boolean del(String key) {
		return Boolean.TRUE.equals(this.redisTemplate.delete(key));
	}

	@Override
	public long ttl(String key) {
		return this.redisTemplate.getExpire(key, TimeUnit.MILLISECONDS);
	}

	@Override
	public String get(String key) {
		return (String) this.redisTemplate.opsForValue().get(key);
	}

	@Override
	public <T> T get(String key, Class<T> clazz) {
		String json = this.get(key);
		if (!Strings.isNullOrEmpty(json)) {
			return JSON.parseObject(json, clazz);
		}
		return null;
	}

	@Override
	public void set(String key, @Nullable String value) {
		if (Strings.isNullOrEmpty(value)) {
			return;
		}
		this.redisTemplate.opsForValue().set(key, value);
	}

	@Override
	public void set(String key, Object value) {
		if (value == null) {
			return;
		}
		this.redisTemplate.opsForValue().set(key, JSON.toJSONString(value));
	}

	@Override
	public void set(String key, @Nullable String value, int expire) {
		if (Strings.isNullOrEmpty(value)) {
			return;
		}
		this.redisTemplate.opsForValue().set(key, value, expire, TimeUnit.SECONDS);
	}

	@Override
	public <V> void set(String key, @Nullable V value, int expire) {
		if (value == null) {
			return;
		}
		this.redisTemplate.opsForValue().set(key, JSON.toJSONString(value), expire, TimeUnit.SECONDS);
	}

}
