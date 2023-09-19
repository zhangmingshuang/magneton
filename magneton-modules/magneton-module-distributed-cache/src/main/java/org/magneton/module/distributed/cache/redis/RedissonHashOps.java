package org.magneton.module.distributed.cache.redis;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import org.magneton.foundation.MoreArrays;
import org.magneton.foundation.MoreCollections;
import org.magneton.module.distributed.cache.Entry;
import org.magneton.module.distributed.cache.ops.HashOps;
import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * .
 *
 * @author zhangmsh 2021/6/25
 * @since 1.0.0
 */
@SuppressWarnings("unchecked")
public class RedissonHashOps extends AbstractRedissonOps implements HashOps {

	public RedissonHashOps(RedissonClient redissonClient) {
		super(redissonClient);
	}

	@Override
	public <V> V put(String hash, Entry<V> entry) {
		Preconditions.checkNotNull(hash, "hash must not be null");
		Preconditions.checkNotNull(entry, "hash must not be null");

		return (V) this.redissonClient.getMap(hash).put(entry.getKey(), entry.getValue());
	}

	@Override
	public <V> void put(String hash, Map<String, V> values) {
		Preconditions.checkNotNull(hash, "hash must not be null");
		Preconditions.checkNotNull(values, "values must not be null");

		this.redissonClient.getMap(hash).putAll(values);
	}

	@Override
	public <V> V get(String hash, String key) {
		Preconditions.checkNotNull(hash, "hash must not be null");
		Preconditions.checkNotNull(key, "key must not be null");

		return (V) this.redissonClient.getMap(hash).get(key);
	}

	@Override
	public <V> Map<String, V> get(String hash, Set<String> keys) {
		Preconditions.checkNotNull(hash, "hash must not be null");
		Preconditions.checkNotNull(keys, "key must not be null");

		if (MoreCollections.isNullOrEmpty(keys)) {
			return Collections.emptyMap();
		}
		RMap<String, V> map = this.redissonClient.getMap(hash);
		return map.getAll(keys);
	}

	@Override
	public boolean containsKey(String hash, String key) {
		Preconditions.checkNotNull(hash, "hash must not be null");
		Preconditions.checkNotNull(key, "key must not be null");

		return this.redissonClient.getMap(hash).containsKey(key);
	}

	@Override
	public long remove(String hash, String... keys) {
		Preconditions.checkNotNull(hash, "hash must not be null");
		Preconditions.checkNotNull(keys, "keys must not be null");

		if (MoreArrays.isNullOrEmpty(keys)) {
			return 0;
		}
		return this.redissonClient.getMap(hash).fastRemove(keys);
	}

	@Override
	public long size(String hash) {
		Preconditions.checkNotNull(hash, "hash must not be null");

		return this.redissonClient.getMap(hash).size();
	}

	@Override
	public Set<String> keys(String hash) {
		Preconditions.checkNotNull(hash, "hash must not be null");

		RMap<String, Object> map = this.redissonClient.getMap(hash);
		return map.keySet();
	}

	@Override
	public <V> List<V> values(String hash) {
		Preconditions.checkNotNull(hash, "hash must not be null");

		RMap<String, V> map = this.redissonClient.getMap(hash);
		return Lists.newArrayList(map.values());
	}

	@Override
	public <V> Set<Map.Entry<String, V>> entrySet(String hash) {
		Preconditions.checkNotNull(hash, "hash must not be null");

		RMap<String, V> map = this.redissonClient.getMap(hash);
		return map.readAllEntrySet();
	}

	@Override
	public long incr(String hash, String key, long incr) {
		Preconditions.checkNotNull(hash, "hash must not be null");
		Preconditions.checkNotNull(key, "key must not be null");

		RMap<String, Object> map = this.redissonClient.getMap(hash);
		return (long) map.addAndGet(key, incr);
	}

}
