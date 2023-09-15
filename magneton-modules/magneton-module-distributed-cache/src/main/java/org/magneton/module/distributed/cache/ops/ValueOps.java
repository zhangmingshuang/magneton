package org.magneton.module.distributed.cache.ops;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import org.magneton.module.distributed.cache.Entry;
import org.magneton.module.distributed.cache.ExpireEntry;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Value Operations.
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

	/**
	 * 设置键值对
	 * @param entry 键值对
	 * @param <V> V
	 */
	<V> boolean set(Entry<V> entry);

	/**
	 * 设置键值对
	 * @param map 键值对
	 * @param <V> V
	 * @return 如果设置成功返回true，否则返回false
	 */
	<V> boolean set(Map<String, V> map);

	/**
	 * 设置键值对
	 * @param entries 键值对
	 * @param <V> V
	 * @return 如果设置成功返回true，否则返回false
	 */
	default <V> boolean set(Entry<V>... entries) {
		return this.set(Arrays.asList(entries));
	}

	/**
	 * 设置键值对
	 * @param entries 键值对
	 * @param <V> V
	 * @return 如果设置成功返回true，否则返回false
	 */
	default <V> boolean set(List<Entry<V>> entries) {
		Preconditions.checkNotNull(entries, "entries must be not null");

		Map<String, V> map = Maps.newHashMapWithExpectedSize(entries.size());
		entries.forEach(kv -> {
			map.put(kv.getKey(), kv.getValue());
		});
		return this.set(map);
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

	/**
	 * 如果Key不存在则设置Value，如果存在则忽略
	 * @param entry 键值对
	 * @param <V> V
	 * @return 如果设置成功返回true，否则返回false
	 */
	<V> boolean setNx(Entry<V> entry);

	/**
	 * 如果Key不存在则设置Value，如果存在则忽略
	 * @param map 键值对
	 * @param <V> V
	 * @return 如果设置成功返回true，否则返回false
	 */
	<V> boolean setNx(Map<String, V> map);

	/**
	 * 如果Key不存在则设置Value，如果存在则忽略
	 * @param entries 键值对
	 * @param <V> V
	 * @return 如果设置成功返回true，否则返回false
	 */
	default <V> boolean setNx(Entry<V>... entries) {
		return this.setNx(Arrays.asList(entries));
	}

	/**
	 * 如果Key不存在则设置Value，如果存在则忽略
	 * @param entries 键值对
	 * @param <V> V
	 * @return 如果设置成功返回true，否则返回false
	 */
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

	/**
	 * 设置键值对
	 * @param entry 键值对
	 * @param expire 过期时间（秒）
	 * @param <V> V
	 */
	default <V> void setEx(Entry<V> entry, long expire) {
		this.setEx(ExpireEntry.of(entry.getKey(), entry.getValue(), expire));
	}

	/**
	 * 设置键值对
	 * @param expireEntries 键值对
	 * @param <V> V
	 */
	default <V> void setEx(ExpireEntry<V>... expireEntries) {
		this.setEx(Arrays.asList(expireEntries));
	}

	/**
	 * 设置键值对
	 * @param expireEntries 键值对
	 * @param <V> V
	 */
	<V> void setEx(List<ExpireEntry<V>> expireEntries);

	/**
	 * 设置键值对
	 * @param expireEntry 键值对
	 * @param <V> V
	 */
	<V> void setEx(ExpireEntry<V> expireEntry);

	/**
	 * 设置键值对
	 * @param key Key
	 * @param value Value
	 * @param expire 过期时间（秒）
	 * @param <V> V
	 * @return 如果设置成功返回true，否则返回false
	 */
	<V> boolean trySet(String key, V value, long expire);

	/**
	 * 获取值
	 * @param key Key
	 * @param <V> V
	 * @return Value 如果对应的Key不存在，则返回null
	 */
	@Nullable
	<V> V get(String key);

	/**
	 * 自增
	 * @apiNote 如果Key不存在，则会创建一个Key，并将其值设置为0，然后再执行自增操作
	 * @param key Key
	 * @return 自增之后的值
	 */
	default long incr(String key) {
		return this.incr(key, 1L);
	}

	/**
	 * 自增
	 * @apiNote 如果Key不存在，则会创建一个Key，并将其值设置为0，然后再执行自增操作
	 * @param key Key
	 * @param incr 要自增的值，比如2，表示自增2
	 * @return 自增之后的值
	 */
	long incr(String key, long incr);

}
