package org.magneton.module.distributed.cache.redis;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.magneton.core.base.Preconditions;
import org.magneton.core.base.Strings;
import org.magneton.core.collect.Lists;
import org.magneton.module.distributed.cache.DistributedCache;
import org.magneton.module.distributed.cache.ops.HashOps;
import org.magneton.module.distributed.cache.ops.ListOps;
import org.magneton.module.distributed.cache.ops.ValueOps;
import org.redisson.api.RedissonClient;

/**
 * .
 *
 * @author zhangmsh 2021/6/24
 * @since 1.0.0
 */
public class RedissonDistributedCache implements DistributedCache {

	private final RedissonClient redissonClient;

	private final ValueOps valueOps;

	private final ListOps listOps;

	private final HashOps hashOps;

	public RedissonDistributedCache(RedissonClient redissonClient) {
		this.redissonClient = Preconditions.checkNotNull(redissonClient);
		this.valueOps = new RedissonValueOps(this.redissonClient);
		this.listOps = new RedissonListOps(this.redissonClient);
		this.hashOps = new RedissonHashOps(this.redissonClient);
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
		return this.redissonClient.getBucket(key).remainTimeToLive();
	}

	@Override
	public boolean expire(String key, long expire) {
		Preconditions.checkNotNull(key, "key");
		return this.redissonClient.getBucket(key).expire(expire, TimeUnit.SECONDS);
	}

	@Override
	public boolean exists(String key) {
		Preconditions.checkNotNull(key, "key");
		return this.redissonClient.getBucket(key).isExists();
	}

	@Override
	public long del(String... keys) {
		Preconditions.checkNotNull(keys, "keys");
		return this.redissonClient.getKeys().unlink(keys);
	}

	@Override
	public void flushDb() {
		this.redissonClient.getKeys().flushdb();
	}

	@Override
	public List<String> keys(String pattern) {
		pattern = Strings.defaultIfNullOrEmpty(pattern, "*");

		Iterable<String> iterable = this.redissonClient.getKeys().getKeysByPattern(pattern);
		List<String> keys = Lists.newArrayListWithCapacity(64);
		iterable.forEach(keys::add);
		return keys;
	}

}
