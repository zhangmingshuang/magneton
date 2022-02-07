package org.magneton.cache.redis;

import java.util.Collection;
import java.util.Map;

import org.magneton.cache.util.Trans;
import org.magneton.core.collect.Maps;

import org.springframework.data.redis.core.RedisTemplate;

/**
 * .
 *
 * @author zhangmsh 2021/6/24
 * @since 1.0.0
 */
public abstract class AbstractRedisTemplateOps {

	protected final RedisTemplate redisTemplate;

	protected final RedisValueSerializer redisValueSerializer;

	protected AbstractRedisTemplateOps(RedisTemplate redisTemplate, RedisValueSerializer redisValueSerializer) {
		this.redisTemplate = redisTemplate;
		this.redisValueSerializer = redisValueSerializer;
	}

	protected byte[] serialize(Object value) {
		return redisValueSerializer.serialize(value);
	}

	protected byte[][] serializeToArray(Collection collection) {
		byte[][] bytes = new byte[collection.size()][];
		int i = 0;
		for (Object o : collection) {
			bytes[i++] = redisValueSerializer.serialize(o);
		}
		return bytes;
	}

	protected <T> T deserialize(byte[] response, Class<T> clazz) {
		return redisValueSerializer.deserialize(response, clazz);
	}

	protected Map<byte[], byte[]> serializeToMap(Map<String, Object> values) {
		Map<byte[], byte[]> map = Maps.newHashMapWithExpectedSize(values.size());
		values.forEach((k, v) -> map.put(Trans.toByte(k), redisValueSerializer.serialize(v)));
		return map;
	}

	protected byte[] toByte(String str) {
		return Trans.toByte(str);
	}

	protected byte[][] toByteArray(String... keys) {
		return Trans.toByteArray(keys);
	}

}
