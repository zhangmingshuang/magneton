package org.magneton.cache.redis;

import com.alibaba.fastjson2.JSON;
import com.google.common.base.Strings;
import org.magneton.cache.MCache;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;

import javax.annotation.Nullable;
import java.time.Duration;

/**
 * 基于Redisson的缓存实现.
 *
 * @author zhangmsh
 * @since 2024
 */
public class RedissonMCache implements MCache {

	private final RedissonClient redissonClient;

	public RedissonMCache(RedissonClient redissonClient) {
		this.redissonClient = redissonClient;
	}

	@Override
	public String clientId() {
		return this.redissonClient.getClass().getName();
	}

	@Override
	public boolean del(String key) {
		return this.redissonClient.getBucket(key).delete();
	}

	@Override
	public long ttl(String key) {
		return this.redissonClient.getBucket(key).remainTimeToLive();
	}

	@Override
	public String get(String key) {
		RBucket<String> bucket = this.redissonClient.getBucket(key);
		return bucket.get();
	}

	@Override
	public <T> T get(String key, Class<T> clazz) {
		String json = this.get(key);
		if (Strings.isNullOrEmpty(json)) {
			return null;
		}
		return JSON.parseObject(json, clazz);
	}

	@Override
	public void set(String key, @Nullable String value) {
		if (Strings.isNullOrEmpty(value)) {
			return;
		}
		this.redissonClient.getBucket(key).set(value);
	}

	@Override
	public void set(String key, @Nullable String value, int expire) {
		if (Strings.isNullOrEmpty(value)) {
			return;
		}
		this.redissonClient.getBucket(key).set(value, Duration.ofSeconds(expire));
	}

	@Override
	public <V> void set(String key, @Nullable V value, int expire) {
		if (value == null) {
			return;
		}
		this.redissonClient.getBucket(key).set(JSON.toJSONString(value), Duration.ofSeconds(expire));
	}

	@Override
	public void set(String key, Object value) {
		if (value == null) {
			return;
		}
		this.redissonClient.getBucket(key).set(JSON.toJSONString(value));
	}

	@Override
	public boolean exist(String key) {
		return this.redissonClient.getBucket(key).isExists();
	}

	@Override
	public void expire(String key, int expire) {
		this.redissonClient.getBucket(key).expire(Duration.ofSeconds(expire));
	}

}
