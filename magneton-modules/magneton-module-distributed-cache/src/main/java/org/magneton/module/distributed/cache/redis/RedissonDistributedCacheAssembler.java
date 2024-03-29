package org.magneton.module.distributed.cache.redis;

import cn.hutool.core.text.CharSequenceUtil;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import org.magneton.module.distributed.cache.DistributedCacheBuilder;
import org.magneton.module.distributed.cache.ops.*;
import org.redisson.api.RedissonClient;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 基于Redisson的分布式缓存组装器.
 *
 * @author zhangmsh 2021/6/24
 * @since 1.0.0
 */
public class RedissonDistributedCacheAssembler implements CommonOps, DistributedCacheBuilder.Assembler {

	private final RedissonClient redissonClient;

	private final ValueOps valueOps;

	private final ListOps listOps;

	private final HashOps hashOps;

	private final SetOps setOps;

	private final SortedSetOps sortedSetOps;

	public RedissonDistributedCacheAssembler(RedissonClient redissonClient) {
		this.redissonClient = Preconditions.checkNotNull(redissonClient);
		this.valueOps = new RedissonValueOps(this.redissonClient);
		this.listOps = new RedissonListOps(this.redissonClient);
		this.hashOps = new RedissonHashOps(this.redissonClient);
		this.setOps = new RedissonSetOps(this.redissonClient);
		this.sortedSetOps = new ResissonSortedSetOps(this.redissonClient);
	}

	@Override
	public HashOps hashOps() {
		return this.hashOps;
	}

	@Override
	public SetOps setOps() {
		return this.setOps;
	}

	@Override
	public SortedSetOps sortedSetOps() {
		return this.sortedSetOps;
	}

	@Override
	public ListOps listOps() {
		return this.listOps;
	}

	@Override
	public ValueOps valueOps() {
		return this.valueOps;
	}

	@Override
	public CommonOps commonOps() {
		return this;
	}

	@Override
	public long ttl(String key) {
		Preconditions.checkNotNull(key, "key must not be null");

		long ttl = this.redissonClient.getBucket(key).remainTimeToLive();
		return ttl <= 0 ? ttl : ttl / 1000;
	}

	@Override
	public boolean expire(String key, long expire) {
		Preconditions.checkNotNull(key, "key must not be null");

		return this.redissonClient.getBucket(key).expire(expire, TimeUnit.SECONDS);
	}

	@Override
	public boolean expireByOther(String key, String otherKey) {
		Preconditions.checkNotNull(key, "key must not be null");
		Preconditions.checkNotNull(otherKey, "otherKey must not be null");

		long ttl = this.ttl(otherKey);
		if (ttl <= 0) {
			return false;
		}
		return this.expire(key, ttl);
	}

	@Override
	public boolean exists(String key) {
		Preconditions.checkNotNull(key, "key must not be null");

		return this.redissonClient.getBucket(key).isExists();
	}

	@Override
	public long del(String... keys) {
		Preconditions.checkNotNull(keys, "keys must not be null");

		return this.redissonClient.getKeys().unlink(keys);
	}

	@Override
	public void flushDb() {
		this.redissonClient.getKeys().flushdb();
	}

	@Override
	public List<String> keys(String pattern) {
		pattern = CharSequenceUtil.blankToDefault(pattern, "*");

		Iterable<String> iterable = this.redissonClient.getKeys().getKeysByPattern(pattern);
		List<String> keys = Lists.newArrayListWithCapacity(64);
		iterable.forEach(keys::add);
		return keys;
	}

}
