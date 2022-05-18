package org.magneton.module.distributed.cache.redis;

import java.util.Collection;
import java.util.Set;
import org.magneton.core.base.Preconditions;
import org.magneton.core.collect.Lists;
import org.magneton.module.distributed.cache.ops.SetOps;
import org.redisson.api.RSet;
import org.redisson.api.RedissonClient;

/**
 * @author zhangmsh 2022/5/4
 * @since 1.0.0
 */
public class RedissonSetOps extends AbstractRedissonOps implements SetOps {

	public RedissonSetOps(RedissonClient redissonClient) {
		super(redissonClient);
	}

	@Override
	@SafeVarargs
	public final <V> boolean add(String set, V... values) {
		Preconditions.checkNotNull(set, "set must not be null");
		Preconditions.checkNotNull(values, "values must not be null");
		if (values.length == 1) {
			return this.redissonClient.getSet(set).add(values[0]);
		}
		return this.redissonClient.getSet(set).addAll(Lists.newArrayList(values));
	}

	@Override
	public <V> boolean add(String set, Collection<V> values) {
		Preconditions.checkNotNull(set, "set must not be null");
		Preconditions.checkNotNull(values, "values must not be null");
		return this.redissonClient.getSet(set).addAll(values);
	}

	@Override
	public <V> Set<V> get(String set) {
		Preconditions.checkNotNull(set, "set must not be null");
		RSet<V> result = this.redissonClient.getSet(set);
		return result.readAll();
	}

	@Override
	public int size(String set) {
		Preconditions.checkNotNull(set, "set must not be null");
		return this.redissonClient.getSet(set).size();
	}

	@Override
	public boolean isEmpty(String set) {
		Preconditions.checkNotNull(set, "set must not be null");
		return this.redissonClient.getSet(set).isEmpty();
	}

	@Override
	public <V> boolean contains(String set, V value) {
		Preconditions.checkNotNull(set, "set must not be null");
		Preconditions.checkNotNull(value, "value must not be null");
		return this.redissonClient.getSet(set).contains(value);
	}

	@Override
	public <V> boolean remove(String set, V value) {
		Preconditions.checkNotNull(set, "set must not be null");
		Preconditions.checkNotNull(value, "value must not be null");
		return this.redissonClient.getSet(set).remove(value);
	}

}
