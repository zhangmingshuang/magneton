package org.magneton.cache.redis;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.magneton.cache.KV;
import org.magneton.cache.ops.HashOps;
import org.magneton.cache.util.Datas;
import org.magneton.cache.util.Trans;
import org.magneton.core.collect.Maps;

import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * .
 *
 * @author zhangmsh 2021/6/25
 * @since 1.0.0
 */
public class RedisTemplateHashOps extends AbstractRedisTemplateOps implements HashOps {

	public RedisTemplateHashOps(RedisTemplate redisTemplate, RedisValueSerializer redisValueSerializer) {
		super(redisTemplate, redisValueSerializer);
	}

	@Override
	public boolean put(String hash, KV kv) {
		redisTemplate
				.execute((RedisCallback) rc -> rc.hSet(toByte(hash), toByte(kv.getKey()), serialize(kv.getValue())));
		return true;
	}

	@Override
	public boolean put(String hash, Map<String, Object> values) {
		redisTemplate.execute((RedisCallback) rc -> {
			rc.hMSet(toByte(hash), serializeToMap(values));
			return null;
		});
		return true;
	}

	@Override
	public String get(String hash, String key) {
		byte[] response = (byte[]) redisTemplate.execute((RedisCallback) rc -> rc.hGet(toByte(hash), toByte(key)));
		return Datas.isEmpty(response) ? null : Trans.toStr(response);
	}

	@Override
	public <T> T get(String hash, String key, Class<T> clazz) {
		byte[] response = (byte[]) redisTemplate.execute((RedisCallback) rc -> rc.hGet(toByte(hash), toByte(key)));
		return Datas.isEmpty(response) ? null : deserialize(response, clazz);
	}

	@Override
	public Map<String, String> get(String hash, String... keys) {
		List<byte[]> response = (List<byte[]>) redisTemplate
				.execute((RedisCallback) rc -> rc.hMGet(toByte(hash), toByteArray(keys)));
		Map<String, String> map = Maps.newHashMapWithExpectedSize(keys.length);
		for (int i = 0; i < response.size(); i++) {
			byte[] bytes = response.get(i);
			map.put(keys[i], Datas.isEmpty(bytes) ? null : Trans.toStr(bytes));
		}
		return map;
	}

	@Override
	public boolean containsKey(String hash, String key) {
		return (boolean) redisTemplate.execute((RedisCallback) rc -> rc.hExists(toByte(hash), toByte(key)));
	}

	@Override
	public long remove(String hash, String... keys) {
		return (long) redisTemplate.execute((RedisCallback) rc -> rc.hDel(toByte(hash), toByteArray(keys)));
	}

	@Override
	public long size(String hash) {
		return (long) redisTemplate.execute((RedisCallback) rc -> rc.hLen(toByte(hash)));
	}

	@Override
	public Set<String> keys(String hash) {
		Set<byte[]> keys = (Set<byte[]>) redisTemplate.execute((RedisCallback) rc -> rc.hKeys(toByte(hash)));
		return keys == null || keys.isEmpty() ? Collections.emptySet() : Trans.toStrSet(keys);
	}

	@Override
	public List<String> values(String hash) {
		List<byte[]> values = (List<byte[]>) redisTemplate.execute((RedisCallback) rc -> rc.hVals(toByte(hash)));
		return values == null || values.isEmpty() ? Collections.emptyList() : Trans.toStrList(values);
	}

	@Override
	public Map<String, String> all(String hash) {
		Map<byte[], byte[]> all = (Map<byte[], byte[]>) redisTemplate
				.execute((RedisCallback) rc -> rc.hGetAll(toByte(hash)));
		return all == null || all.isEmpty() ? Collections.emptyMap() : Trans.toStrMap(all);
	}

}
