package org.magneton.module.distributed.cache.redis;

import java.util.List;

import org.magneton.core.base.Preconditions;
import org.magneton.core.collect.Collections;
import org.magneton.foundation.util.Arrays;
import org.magneton.module.distributed.cache.ops.ListOps;
import org.magneton.module.distributed.cache.util.Trans;

import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * .
 *
 * @author zhangmsh 2021/6/24
 * @since 1.0.0
 */
public class RedisTemplateListOps extends AbstractRedisTemplateOps implements ListOps {

	public RedisTemplateListOps(RedisTemplate<String, ?> redisTemplate, RedisValueSerializer redisValueSerializer) {
		super(redisTemplate, redisValueSerializer);
	}

	@Override
	public <V> long add(String list, List<V> values) {
		Preconditions.checkNotNull(list);
		Preconditions.checkNotNull(values);
		return (long) this.redisTemplate
				.execute((RedisCallback) rc -> rc.rPush(Trans.toByte(list), this.serializeToArray(values)));
	}

	@Override
	public <V> long addAtHead(String list, List<V> values) {
		Preconditions.checkNotNull(list);
		Preconditions.checkNotNull(values);
		return (long) this.redisTemplate
				.execute((RedisCallback) rc -> rc.lPush(Trans.toByte(list), this.serializeToArray(values)));
	}

	@Override
	public List<String> range(String list, long start, long end) {
		Preconditions.checkNotNull(list);
		List<byte[]> response = (List<byte[]>) this.redisTemplate
				.execute((RedisCallback) rc -> rc.lRange(Trans.toByte(list), start, end));
		return Collections.isNullOrEmpty(response) ? Collections.emptyList() : Trans.toStr(response);
	}

	@Override
	public <V> List<V> range(String list, long start, long end, Class<V> clazz) {
		Preconditions.checkNotNull(list);
		List<byte[]> response = (List<byte[]>) this.redisTemplate
				.execute((RedisCallback) rc -> rc.lRange(Trans.toByte(list), start, end));
		return Collections.isNullOrEmpty(response) ? Collections.emptyList() : this.deserialize(response, clazz);
	}

	@Override
	public long size(String list) {
		Preconditions.checkNotNull(list);
		return (long) this.redisTemplate.execute((RedisCallback) rc -> rc.lLen(Trans.toByte(list)));
	}

	@Override
	public String get(String list, long index) {
		Preconditions.checkNotNull(list);
		byte[] response = (byte[]) this.redisTemplate
				.execute((RedisCallback) rc -> rc.lIndex(Trans.toByte(list), index));
		return Arrays.isNullOrEmpty(response) ? null : Trans.toStr(response);
	}

	@Override
	public <V> V get(String list, long index, Class<V> clazz) {
		Preconditions.checkNotNull(list);
		Preconditions.checkNotNull(clazz);
		byte[] response = (byte[]) this.redisTemplate
				.execute((RedisCallback) rc -> rc.lIndex(Trans.toByte(list), index));
		return Arrays.isNullOrEmpty(response) ? null : this.deserialize(response, clazz);
	}

	@Override
	public <V> long remove(String list, V value) {
		Preconditions.checkNotNull(list);
		Preconditions.checkNotNull(value);
		return (long) this.redisTemplate
				.execute((RedisCallback) rc -> rc.lRem(Trans.toByte(list), 0, this.serialize(value)));
	}

}
