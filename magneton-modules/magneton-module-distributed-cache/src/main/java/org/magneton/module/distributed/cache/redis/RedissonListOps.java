package org.magneton.module.distributed.cache.redis;

import com.google.common.base.Preconditions;
import java.util.List;
import org.magneton.module.distributed.cache.ops.ListOps;
import org.redisson.api.RedissonClient;

/**
 * .
 *
 * @author zhangmsh 2021/6/24
 * @since 1.0.0
 */
public class RedissonListOps extends AbstractRedissonOps implements ListOps {

	public RedissonListOps(RedissonClient redissonClient) {
		super(redissonClient);
	}

	@Override
	public <V> boolean add(String list, List<V> values) {
		Preconditions.checkNotNull(list);
		Preconditions.checkNotNull(values);
		return this.redissonClient.getDeque(list).addAll(values);
	}

	@Override
	public <V> void addAtHead(String list, V... values) {
		Preconditions.checkNotNull(list);
		Preconditions.checkNotNull(values);
		this.redissonClient.getDeque(list).addFirst(values);
	}

	@Override
	public <V> List<V> range(String list, int start, int end) {
		Preconditions.checkNotNull(list);
		return (List<V>) this.redissonClient.getList(list).range(start, end);
	}

	@Override
	public long size(String list) {
		Preconditions.checkNotNull(list);
		return this.redissonClient.getList(list).size();
	}

	@Override
	public <V> V get(String list, int index) {
		Preconditions.checkNotNull(list);
		return (V) this.redissonClient.getList(list).get(index);
	}

	@Override
	public <V> boolean remove(String list, V value) {
		Preconditions.checkNotNull(list);
		Preconditions.checkNotNull(value);
		return this.redissonClient.getList(list).remove(value);
	}

}
