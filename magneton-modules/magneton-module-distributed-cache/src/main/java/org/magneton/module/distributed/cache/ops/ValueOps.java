package org.magneton.module.distributed.cache.ops;

import java.util.Arrays;
import java.util.List;

import javax.annotation.Nullable;

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
	default <V> boolean set(String key, V value) {
		return this.set(KV.of(key, value));
	}

	<V> boolean set(KV<V> kv);

	default <V> List<Boolean> set(KV<V>... kvs) {
		return this.set(Arrays.asList(kvs));
	}

	<V> List<Boolean> set(List<KV<V>> kvs);

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

	default <V> List<Boolean> setNx(KV<V>... kvs) {
		return this.setNx(Arrays.asList(kvs));
	}

	<V> List<Boolean> setNx(List<KV<V>> kvs);

	/**
	 * 设置键值对
	 *
	 * <p>
	 * 如果设置的Key已经存在，则会覆盖Value
	 * @param key Key
	 * @param value Value
	 * @param expire 过期时间（秒）
	 */
	default <V> boolean setEx(String key, V value, long expire) {
		return this.setEx(EKV.of(key, value, expire));
	}

	default <V> boolean setEx(KV<V> kv, long expire) {
		return this.setEx(EKV.of(kv.getKey(), kv.getValue(), expire));
	}

	default <V> List<Boolean> setEx(EKV<V>... ekvs) {
		return this.setEx(Arrays.asList(ekvs));
	}

	<V> List<Boolean> setEx(List<EKV<V>> ekvs);

	<V> boolean setEx(EKV<V> ekv);

	@Nullable
	String get(String key);

	@Nullable
	<V> V get(String key, Class<V> clazz);

}
