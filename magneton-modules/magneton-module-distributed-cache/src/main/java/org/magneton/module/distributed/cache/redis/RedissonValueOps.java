package org.magneton.module.distributed.cache.redis;

import com.google.common.base.Preconditions;
import org.magneton.foundation.MoreCollections;
import org.magneton.module.distributed.cache.Entry;
import org.magneton.module.distributed.cache.ExpireEntry;
import org.magneton.module.distributed.cache.ops.ValueOps;
import org.redisson.api.RBatch;
import org.redisson.api.RedissonClient;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * .
 *
 * @author zhangmsh 2021/6/24
 * @since 1.0.0
 */
@SuppressWarnings("unchecked")
public class RedissonValueOps extends AbstractRedissonOps implements ValueOps {

	public RedissonValueOps(RedissonClient redissonClient) {
		super(redissonClient);
	}

	@Override
	public <V> boolean set(Entry<V> entry) {
		Preconditions.checkNotNull(entry, "entry must not be null");

		this.redissonClient.getBucket(entry.getKey()).set(entry.getValue());
		return true;
	}

	@Override
	public <V> boolean set(Map<String, V> map) {
		if (MoreCollections.isNullOrEmpty(map)) {
			return false;
		}
		this.redissonClient.getBuckets().set(map);
		return true;
	}

	@Override
	public <V> boolean setNx(Entry<V> entry) {
		Preconditions.checkNotNull(entry, "entry must not be null");

		return this.redissonClient.getBucket(entry.getKey()).trySet(entry.getValue());
	}

	@Override
	public <V> boolean setNx(Map<String, V> map) {
		if (MoreCollections.isNullOrEmpty(map)) {
			return false;
		}
		return this.redissonClient.getBuckets().trySet(map);
	}

	@Override
	public <V> void setEx(ExpireEntry<V> expireEntry) {
		Preconditions.checkNotNull(expireEntry, "expireEntry must not be null");

		this.redissonClient.getBucket(expireEntry.getKey()).set(expireEntry.getValue(), expireEntry.getExpire(),
				TimeUnit.SECONDS);
	}

	@Override
	public <V> void setEx(List<ExpireEntry<V>> expireEntries) {
		Preconditions.checkNotNull(expireEntries, "expireEntries must not be null");

		RBatch batch = this.redissonClient.createBatch();
		expireEntries.forEach(expireEntry -> {
			batch.getBucket(expireEntry.getKey()).trySetAsync(expireEntry.getValue(), expireEntry.getExpire(),
					TimeUnit.SECONDS);
		});
		batch.execute();
	}

	@Override
	public <V> boolean trySet(String key, V value, long expire) {
		Preconditions.checkNotNull(key, "key must not be null");
		Preconditions.checkNotNull(value, "value must not be null");

		return this.redissonClient.getBucket(key).trySet(value, expire, TimeUnit.SECONDS);
	}

	@Nullable
	@Override
	public <V> V get(String key) {
		Preconditions.checkNotNull(key, "key must not be null");

		return (V) this.redissonClient.getBucket(key).get();
	}

	@Override
	public long incr(String key, long incr) {
		Preconditions.checkNotNull(key, "key must not be null");

		return this.redissonClient.getAtomicLong(key).addAndGet(incr);
	}

}
