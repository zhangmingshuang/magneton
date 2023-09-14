package org.magneton.module.distributed.cache;

import org.magneton.module.distributed.cache.ops.*;

import java.util.List;

/**
 * 分布式缓存实现类
 *
 * @author zhangmsh.
 * @since 2023.9
 */
public class DistributedCacheImpl implements DistributedCache {

	private DistributedCacheBuilder builder;

	public DistributedCacheImpl(DistributedCacheBuilder builder) {
		this.builder = builder;
	}

	@Override
	public ValueOps opsForValue() {
		return this.builder.valueOps;
	}

	@Override
	public ListOps opsForList() {
		return this.builder.listOps;
	}

	@Override
	public HashOps opsForHash() {
		return this.builder.hashOps;
	}

	@Override
	public SetOps opsForSet() {
		return this.builder.setOps;
	}

	@Override
	public SortedSetOps opsForSortedSet() {
		return this.builder.sortedSetOps;
	}

	@Override
	public long ttl(String key) {
		return this.builder.commonOps.ttl(key);
	}

	@Override
	public boolean expire(String key, long expire) {
		return this.builder.commonOps.expire(key, expire);
	}

	@Override
	public boolean expireByOther(String key, String otherKey) {
		return this.builder.commonOps.expireByOther(key, otherKey);
	}

	@Override
	public boolean exists(String key) {
		return this.builder.commonOps.exists(key);
	}

	@Override
	public long del(String... keys) {
		return this.builder.commonOps.del(keys);
	}

	@Override
	public void flushDb() {
		this.builder.commonOps.flushDb();
	}

	@Override
	public List<String> keys(String pattern) {
		return this.builder.commonOps.keys(pattern);
	}

}
