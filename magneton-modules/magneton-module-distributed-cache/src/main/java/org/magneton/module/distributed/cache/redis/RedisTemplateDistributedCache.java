package org.magneton.module.distributed.cache.redis;

import org.magneton.core.base.Preconditions;
import org.magneton.module.distributed.cache.DistributedCache;
import org.magneton.module.distributed.cache.ops.HashOps;
import org.magneton.module.distributed.cache.ops.ListOps;
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
public class RedisTemplateDistributedCache<V> implements DistributedCache {

	private final RedisTemplate<String, V> redisTemplate;

	private final ValueOps valueOps;

	private final ListOps listOps;

	private final HashOps hashOps;

	public RedisTemplateDistributedCache(RedisTemplate<String, V> redisTemplate, RedisValueSerializer valueSerializer) {
		this.redisTemplate = redisTemplate;
		this.valueOps = new RedisTemplateValueOps(redisTemplate, valueSerializer);
		this.listOps = new RedisTemplateListOps(redisTemplate, valueSerializer);
		this.hashOps = new RedisTemplateHashOps(redisTemplate, valueSerializer);
	}

	@Override
	public HashOps opsForHash() {
		return this.hashOps;
	}

	@Override
	public ListOps opsForList() {
		return this.listOps;
	}

	@Override
	public ValueOps opsForValue() {
		return this.valueOps;
	}

	@Override
	public long ttl(String key) {
		Preconditions.checkNotNull(key, "key");
		return (long) this.redisTemplate.execute((RedisCallback) rb -> rb.ttl(Trans.toByte(key)));
	}

	@Override
	public boolean expire(String key, long expire) {
		Preconditions.checkNotNull(key, "key");
		return (boolean) this.redisTemplate.execute((RedisCallback) cb -> cb.expire(Trans.toByte(key), expire));
	}

	@Override
	public boolean exists(String key) {
		Preconditions.checkNotNull(key, "key");
		return (boolean) this.redisTemplate.execute((RedisCallback) rc -> rc.exists(Trans.toByte(key)));
	}

	@Override
	public long del(String... keys) {
		Preconditions.checkNotNull(keys, "keys");
		return this.redisTemplate.execute((RedisCallback<Long>) rc -> rc.del(Trans.toByteArray(keys)));
	}

	@Override
	public void flushDb() {
		this.redisTemplate.execute((RedisCallback<?>) rc -> {
			rc.flushDb();
			return null;
		});
	}

	@Override
	public void select(int dbIndex) {
		Preconditions.checkArgument(dbIndex > 0 && dbIndex < 17, "dbIndex must be between 0 and 16(include)");
		this.redisTemplate.execute((RedisCallback<? extends Object>) rc -> {
			rc.select(dbIndex);
			return null;
		});
	}

}
