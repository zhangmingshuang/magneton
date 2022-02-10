package org.magneton.module.distributed.cache.ops;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

import org.magneton.core.base.Preconditions;
import org.magneton.core.collect.Maps;
import org.magneton.module.distributed.cache.EKV;
import org.magneton.module.distributed.cache.KV;

/**
 * .
 *
 * @author zhangmsh 2021/6/24
 * @since 1.0.0
 */
public interface ValueOps {

	/**
	 * 设置键值对
	 *
	 * <p>
	 * 如果设置的Key已经存在，则会覆盖Value
	 * @param key 键
	 * @param value 值
	 */
	default <V> void set(String key, V value) {
		this.set(KV.of(key, value));
	}

	<V> void set(KV<V> kv);

	<V> void set(Map<String, V> map);

	default <V> void set(KV<V>... kvs) {
		this.set(Arrays.asList(kvs));
	}

	default <V> void set(List<KV<V>> kvs) {
		Preconditions.checkNotNull(kvs);
		Map<String, V> map = Maps.newHashMapWithExpectedSize(kvs.size());
		kvs.forEach(kv -> {
			map.put(kv.getKey(), kv.getValue());
		});
		this.set(map);
	}

	/**
	 * 如果Key不存在则设置Value，如果存在则忽略
	 * @param key Key
	 * @param value Value
	 * @return 如果设置成功返回true，否则返回false
	 */
	default <V> boolean setNx(String key, V value) {
		return this.setNx(KV.of(key, value));
	}

	<V> boolean setNx(KV<V> kv);

	<V> boolean setNx(Map<String, V> map);

	default <V> boolean setNx(KV<V>... kvs) {
		return this.setNx(Arrays.asList(kvs));
	}

	default <V> boolean setNx(List<KV<V>> kvs) {
		Preconditions.checkNotNull(kvs);
		Map<String, V> map = Maps.newHashMapWithExpectedSize(kvs.size());
		kvs.forEach(kv -> {
			map.put(kv.getKey(), kv.getValue());
		});
		return this.setNx(map);
	}

	/**
	 * 设置键值对
	 *
	 * <p>
	 * 如果设置的Key已经存在，则会覆盖Value
	 * @param key Key
	 * @param value Value
	 * @param expire 过期时间（秒）
	 */
	default <V> void setEx(String key, V value, long expire) {
		this.setEx(EKV.of(key, value, expire));
	}

	default <V> void setEx(KV<V> kv, long expire) {
		this.setEx(EKV.of(kv.getKey(), kv.getValue(), expire));
	}

	default <V> void setEx(EKV<V>... ekvs) {
		this.setEx(Arrays.asList(ekvs));
	}

	<V> void setEx(List<EKV<V>> ekvs);

	<V> void setEx(EKV<V> ekv);

	@Nullable
	<V> V get(String key);

}
