package org.magneton.module.distributed.cache.redis;

import java.util.List;

import javax.annotation.Nullable;

import org.magneton.core.base.Preconditions;
import org.magneton.foundation.util.Arrays;
import org.magneton.module.distributed.cache.EKV;
import org.magneton.module.distributed.cache.KV;
import org.magneton.module.distributed.cache.ops.ValueOps;
import org.magneton.module.distributed.cache.util.Trans;

import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * .
 *
 * @author zhangmsh 2021/6/24
 * @since 1.0.0
 */
public class RedisTemplateValueOps extends AbstractRedisTemplateOps implements ValueOps {

	public RedisTemplateValueOps(RedisTemplate<String, ?> redisTemplate, RedisValueSerializer redisValueSerializer) {
		super(redisTemplate, redisValueSerializer);
	}

	@Override
	public <V> boolean set(KV<V> kv) {
		Preconditions.checkNotNull(kv, "set by kv");
		return (boolean) this.redisTemplate
				.execute((RedisCallback) cb -> cb.set(this.toByte(kv.getKey()), this.serialize(kv.getValue())));
	}

	@Override
	public <V> List<Boolean> set(List<KV<V>> kvs) {
		Preconditions.checkNotNull(kvs, "set list by kvs");
		List<Object> objects = this.redisTemplate.executePipelined((RedisCallback<Boolean>) rc -> {
			kvs.forEach(kv -> rc.set(this.toByte(kv.getKey()), this.serialize(kv.getValue())));
			return null;
		});
		return Trans.toBoolean(objects);
	}

	@Override
	public <V> boolean setNx(KV<V> kv) {
		Preconditions.checkNotNull(kv, "setNx by kv");
		return (boolean) this.redisTemplate
				.execute((RedisCallback) rc -> rc.setNX(this.toByte(kv.getKey()), this.serialize(kv.getValue())));
	}

	@Override
	public <V> List<Boolean> setNx(List<KV<V>> kvs) {
		Preconditions.checkNotNull(kvs, "setNx list by kvs");
		List<Object> objects = this.redisTemplate.executePipelined((RedisCallback<Boolean>) rc -> {
			kvs.forEach(kv -> rc.setNX(this.toByte(kv.getKey()), this.serialize(kv.getValue())));
			return null;
		});
		return Trans.toBoolean(objects);
	}

	@Override
	public <V> boolean setEx(EKV<V> ekv) {
		Preconditions.checkNotNull(ekv, "setEx by ekv");
		return (boolean) this.redisTemplate.execute((RedisCallback) cb -> cb.setEx(this.toByte(ekv.getKey()),
				ekv.getExpire(), this.serialize(ekv.getValue())));
	}

	@Override
	public <V> List<Boolean> setEx(List<EKV<V>> ekvs) {
		Preconditions.checkNotNull(ekvs, "setEx list by ekvs");
		List<Object> objects = this.redisTemplate.executePipelined((RedisCallback<Boolean>) rc -> {
			ekvs.forEach(ekv -> rc.setEx(this.toByte(ekv.getKey()), ekv.getExpire(), this.serialize(ekv.getValue())));
			return null;
		});
		return Trans.toBoolean(objects);
	}

	@Override
	public String get(String key) {
		Preconditions.checkNotNull(key, "get by key");
		byte[] response = (byte[]) this.redisTemplate.execute((RedisCallback) cb -> cb.get(this.toByte(key)));
		return Arrays.isNullOrEmpty(response) ? null : Trans.toStr(response);
	}

	@Nullable
	@Override
	public <V> V get(String key, Class<V> clazz) {
		Preconditions.checkNotNull(key, "get by key&class but key");
		Preconditions.checkNotNull(clazz, "get by key&class but clazz");
		byte[] response = (byte[]) this.redisTemplate.execute((RedisCallback) cb -> cb.get(this.toByte(key)));
		return Arrays.isNullOrEmpty(response) ? null : this.deserialize(response, clazz);
	}

}
