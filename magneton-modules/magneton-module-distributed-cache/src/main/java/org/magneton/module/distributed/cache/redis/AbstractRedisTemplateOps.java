package org.magneton.module.distributed.cache.redis;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.magneton.core.collect.Maps;
import org.magneton.module.distributed.cache.util.Trans;

import org.springframework.data.redis.core.RedisTemplate;

/**
 * .
 *
 * @author zhangmsh 2021/6/24
 * @since 1.0.0
 */
public abstract class AbstractRedisTemplateOps {

	protected final RedisTemplate<String, ?> redisTemplate;

	protected final RedisValueSerializer redisValueSerializer;

	protected AbstractRedisTemplateOps(RedisTemplate<String, ?> redisTemplate,
			RedisValueSerializer redisValueSerializer) {
		this.redisTemplate = redisTemplate;
		this.redisValueSerializer = redisValueSerializer;
	}

	protected byte[] serialize(Object value) {
		return this.redisValueSerializer.serialize(value);
	}

	protected byte[][] serializeToArray(Collection<?> collection) {
		byte[][] bytes = new byte[collection.size()][];
		int i = 0;
		for (Object o : collection) {
			bytes[i++] = this.redisValueSerializer.serialize(o);
		}
		return bytes;
	}

	protected <V> V deserialize(byte[] response, Class<V> clazz) {
		return this.redisValueSerializer.deserialize(response, clazz);
	}

	protected <V> List<V> deserialize(List<byte[]> responses, Class<V> clazz) {
		return this.redisValueSerializer.deserialize(responses, clazz);
	}

	protected <V> Map<byte[], byte[]> serializeToMap(Map<String, V> values) {
		Map<byte[], byte[]> map = Maps.newHashMapWithExpectedSize(values.size());
		values.forEach((k, v) -> map.put(Trans.toByte(k), this.redisValueSerializer.serialize(v)));
		return map;
	}

	protected byte[] toByte(String str) {
		return Trans.toByte(str);
	}

	protected byte[][] toByteArray(String... keys) {
		return Trans.toByteArray(keys);
	}

}
