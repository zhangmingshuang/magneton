package org.magneton.module.distributed.cache.ops;

import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Nullable;

import org.magneton.core.collect.Maps;
import org.magneton.core.collect.Sets;
import org.magneton.module.distributed.cache.Entry;

/**
 * .
 *
 * @author zhangmsh 2021/6/25
 * @since 1.0.0
 */
public interface HashOps {

	/**
	 * 向Hash表中添加一个数据
	 * @param hash Hash表名称
	 * @param key Key
	 * @param value Value
	 * @return 是否设置成功
	 */
	@Nullable
	default <V> V put(String hash, String key, V value) {
		return (V) this.put(hash, Entry.of(key, value));
	}

	@Nullable
	<V> V put(String hash, Entry<V> entry);

	<V> void put(String hash, Map<String, V> values);

	default <V> void put(String hash, List<Entry<V>> values) {
		Map<String, V> mapValues = Maps.newHashMapWithExpectedSize(values.size());
		values.forEach(kv -> mapValues.put(kv.getKey(), kv.getValue()));
		this.put(hash, mapValues);
	}

	@Nullable
	<V> V get(String hash, String key);

	<V> Map<String, V> get(String hash, Set<String> keys);

	default <V> Map<String, V> get(String hash, String... keys) {
		return this.get(hash, Sets.newHashSet(keys));
	}

	default <V> Map<String, V> get(String hash, List<String> keys) {
		return this.get(hash, Sets.newHashSet(keys));
	}

	/**
	 * 判断Hash表是否存在Key
	 * @param hash Hash表名称
	 * @param key Key
	 * @return 如果 {@code key} 存在则返回 {@code true}， 否则返回 {@code false}
	 */
	boolean containsKey(String hash, String key);

	/**
	 * 从Hash表中删除
	 * @param hash Hash表名称
	 * @param keys 要删除的Key，如果不存在则会被忽略
	 * @return 删除的数量，不包括被忽略的Key
	 */
	long remove(String hash, String... keys);

	long size(String hash);

	Set<String> keys(String hash);

	<V> List<V> values(String hash);

	<V> Set<Map.Entry<String, V>> entrySet(String hash);

}
