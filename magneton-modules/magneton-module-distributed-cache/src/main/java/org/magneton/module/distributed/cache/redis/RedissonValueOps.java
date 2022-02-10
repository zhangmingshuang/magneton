package org.magneton.module.distributed.cache.redis;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.annotation.Nullable;

import org.magneton.core.base.Preconditions;
import org.magneton.core.collect.Collections;
import org.magneton.module.distributed.cache.EKV;
import org.magneton.module.distributed.cache.KV;
import org.magneton.module.distributed.cache.ops.ValueOps;
import org.redisson.api.RBatch;
import org.redisson.api.RedissonClient;

/**
 * .
 *
 * @author zhangmsh 2021/6/24
 * @since 1.0.0
 */
public class RedissonValueOps extends AbstractRedissonOps implements ValueOps {

	public RedissonValueOps(RedissonClient redissonClient) {
		super(redissonClient);
	}

	@Override
	public <V> void set(KV<V> kv) {
		Preconditions.checkNotNull(kv, "set by kv");
		this.redissonClient.getBucket(kv.getKey()).set(kv.getValue());
	}

	@Override
	public <V> void set(Map<String, V> map) {
		if (Collections.isNullOrEmpty(map)) {
			return;
		}
		this.redissonClient.getBuckets().set(map);
	}

	@Override
	public <V> boolean setNx(KV<V> kv) {
		Preconditions.checkNotNull(kv, "setNx by kv");
		return this.redissonClient.getBucket(kv.getKey()).trySet(kv.getValue());
	}

	@Override
	public <V> boolean setNx(Map<String, V> map) {
		if (Collections.isNullOrEmpty(map)) {
			return false;
		}
		return this.redissonClient.getBuckets().trySet(map);
	}

	@Override
	public <V> void setEx(EKV<V> ekv) {
		Preconditions.checkNotNull(ekv, "setEx by ekv");
		this.redissonClient.getBucket(ekv.getKey()).set(ekv.getValue(), ekv.getExpire(), TimeUnit.SECONDS);
	}

	@Override
	public <V> void setEx(List<EKV<V>> ekvs) {
		Preconditions.checkNotNull(ekvs, "setEx list by ekvs");
		RBatch batch = this.redissonClient.createBatch();
		ekvs.forEach(ekv -> {
			batch.getBucket(ekv.getKey()).trySetAsync(ekv.getValue(), ekv.getExpire(), TimeUnit.SECONDS);
		});
		batch.execute();
	}

	@Nullable
	@Override
	public <V> V get(String key) {
		return (V) this.redissonClient.getBucket(key).get();
	}

}
