package org.magneton.module.distributed.cache.redis;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.magneton.core.collect.Maps;
import org.magneton.foundation.util.Arrays;
import org.magneton.module.distributed.cache.KV;
import org.magneton.module.distributed.cache.ops.HashOps;
import org.magneton.module.distributed.cache.util.Trans;

import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * .
 *
 * @author zhangmsh 2021/6/25
 * @since 1.0.0
 */
public class RedisTemplateHashOps extends AbstractRedisTemplateOps implements HashOps {

	public RedisTemplateHashOps(RedisTemplate<String, ?> redisTemplate, RedisValueSerializer redisValueSerializer) {
		super(redisTemplate, redisValueSerializer);
	}

	@Override
	public <V> boolean put(String hash, KV<V> kv) {
		this.redisTemplate.execute((RedisCallback) rc -> rc.hSet(this.toByte(hash), this.toByte(kv.getKey()),
				this.serialize(kv.getValue())));
		return true;
	}

	@Override
	public <V> boolean put(String hash, Map<String, V> values) {
		this.redisTemplate.execute((RedisCallback) rc -> {
			rc.hMSet(this.toByte(hash), this.serializeToMap(values));
			return null;
		});
		return true;
	}

	@Override
	public String get(String hash, String key) {
		byte[] response = (byte[]) this.redisTemplate
				.execute((RedisCallback) rc -> rc.hGet(this.toByte(hash), this.toByte(key)));
		return Arrays.isNullOrEmpty(response) ? null : Trans.toStr(response);
	}

	@Override
	public <V> V get(String hash, String key, Class<V> clazz) {
		byte[] response = (byte[]) this.redisTemplate
				.execute((RedisCallback) rc -> rc.hGet(this.toByte(hash), this.toByte(key)));
		return Arrays.isNullOrEmpty(response) ? null : this.deserialize(response, clazz);
	}

	@Override
	public Map<String, String> get(String hash, String... keys) {
		List<byte[]> response = (List<byte[]>) this.redisTemplate
				.execute((RedisCallback) rc -> rc.hMGet(this.toByte(hash), this.toByteArray(keys)));
		Map<String, String> map = Maps.newHashMapWithExpectedSize(keys.length);
		for (int i = 0; i < response.size(); i++) {
			byte[] bytes = response.get(i);
			map.put(keys[i], Arrays.isNullOrEmpty(bytes) ? null : Trans.toStr(bytes));
		}
		return map;
	}

	@Override
	public boolean containsKey(String hash, String key) {
		return (boolean) this.redisTemplate
				.execute((RedisCallback) rc -> rc.hExists(this.toByte(hash), this.toByte(key)));
	}

	@Override
	public long remove(String hash, String... keys) {
		return (long) this.redisTemplate
				.execute((RedisCallback) rc -> rc.hDel(this.toByte(hash), this.toByteArray(keys)));
	}

	@Override
	public long size(String hash) {
		return (long) this.redisTemplate.execute((RedisCallback) rc -> rc.hLen(this.toByte(hash)));
	}

	@Override
	public Set<String> keys(String hash) {
		Set<byte[]> keys = (Set<byte[]>) this.redisTemplate.execute((RedisCallback) rc -> rc.hKeys(this.toByte(hash)));
		return keys == null || keys.isEmpty() ? Collections.emptySet() : Trans.toStrSet(keys);
	}

	@Override
	public List<String> values(String hash) {
		List<byte[]> values = (List<byte[]>) this.redisTemplate
				.execute((RedisCallback) rc -> rc.hVals(this.toByte(hash)));
		return values == null || values.isEmpty() ? Collections.emptyList() : Trans.toStrList(values);
	}

	@Override
	public Map<String, String> all(String hash) {
		Map<byte[], byte[]> all = (Map<byte[], byte[]>) this.redisTemplate
				.execute((RedisCallback) rc -> rc.hGetAll(this.toByte(hash)));
		return all == null || all.isEmpty() ? Collections.emptyMap() : Trans.toStrMap(all);
	}

}
