package org.magneton.module.distributed.cache.ops;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

import org.magneton.core.base.Preconditions;
import org.magneton.core.collect.Maps;
import org.magneton.module.distributed.cache.Entry;
import org.magneton.module.distributed.cache.ExpireEntry;

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
		this.set(Entry.of(key, value));
	}

	<V> void set(Entry<V> entry);

	<V> void set(Map<String, V> map);

	default <V> void set(Entry<V>... entries) {
		this.set(Arrays.asList(entries));
	}

	default <V> void set(List<Entry<V>> entries) {
		Preconditions.checkNotNull(entries);
		Map<String, V> map = Maps.newHashMapWithExpectedSize(entries.size());
		entries.forEach(kv -> {
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
		return this.setNx(Entry.of(key, value));
	}

	<V> boolean setNx(Entry<V> entry);

	<V> boolean setNx(Map<String, V> map);

	default <V> boolean setNx(Entry<V>... entries) {
		return this.setNx(Arrays.asList(entries));
	}

	default <V> boolean setNx(List<Entry<V>> entries) {
		Preconditions.checkNotNull(entries);
		Map<String, V> map = Maps.newHashMapWithExpectedSize(entries.size());
		entries.forEach(kv -> {
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
		this.setEx(ExpireEntry.of(key, value, expire));
	}

	default <V> void setEx(Entry<V> entry, long expire) {
		this.setEx(ExpireEntry.of(entry.getKey(), entry.getValue(), expire));
	}

	default <V> void setEx(ExpireEntry<V>... expireEntries) {
		this.setEx(Arrays.asList(expireEntries));
	}

	<V> void setEx(List<ExpireEntry<V>> expireEntries);

	<V> void setEx(ExpireEntry<V> expireEntry);

	@Nullable
	<V> V get(String key);

	default long incr(String key) {
		return this.incr(key, 1L);
	}

	long incr(String key, long incr);

}
