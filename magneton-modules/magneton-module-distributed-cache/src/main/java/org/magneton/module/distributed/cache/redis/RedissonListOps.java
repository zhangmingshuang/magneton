package org.magneton.module.distributed.cache.redis;

import com.google.common.base.Preconditions;
import org.magneton.module.distributed.cache.ops.ListOps;
import org.redisson.api.RedissonClient;

import java.util.List;

/**
 * .
 *
 * @author zhangmsh 2021/6/24
 * @since 1.0.0
 */
@SuppressWarnings("unchecked")
public class RedissonListOps extends AbstractRedissonOps implements ListOps {

	public RedissonListOps(RedissonClient redissonClient) {
		super(redissonClient);
	}

	@Override
	public <V> boolean add(String list, List<V> values) {
		Preconditions.checkNotNull(list, "list must not be null");
		Preconditions.checkNotNull(values, "values must not be null");

		return this.redissonClient.getDeque(list).addAll(values);
	}

	@Override
	public <V> void addAtHead(String list, V... values) {
		Preconditions.checkNotNull(list, "list must not be null");
		Preconditions.checkNotNull(values, "values must not be null");

		this.redissonClient.getDeque(list).addFirst(values);
	}

	@Override
	public <V> List<V> range(String list, int start, int end) {
		Preconditions.checkNotNull(list, "list must not be null");

		return (List<V>) this.redissonClient.getList(list).range(start, end);
	}

	@Override
	public long size(String list) {
		Preconditions.checkNotNull(list, "list must not be null");

		return this.redissonClient.getList(list).size();
	}

	@Override
	public <V> V get(String list, int index) {
		Preconditions.checkNotNull(list, "list must not be null");

		return (V) this.redissonClient.getList(list).get(index);
	}

	@Override
	public <V> boolean remove(String list, V value) {
		Preconditions.checkNotNull(list, "list must not be null");
		Preconditions.checkNotNull(value, "value must not be null");

		return this.redissonClient.getList(list).remove(value);
	}

}
