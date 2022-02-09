package org.magneton.module.distributed.cache.ops;

import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Nullable;

import org.magneton.core.collect.Maps;
import org.magneton.module.distributed.cache.KV;

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
	default <V> boolean put(String hash, String key, V value) {
		return this.put(hash, KV.of(key, value));
	}

	<V> boolean put(String hash, KV<V> kv);

	<V> boolean put(String hash, Map<String, V> values);

	default <V> boolean put(String hash, List<KV<V>> values) {
		Map<String, V> mapValues = Maps.newHashMapWithExpectedSize(values.size());
		values.forEach(kv -> mapValues.put(kv.getKey(), kv.getValue()));
		return this.put(hash, mapValues);
	}

	@Nullable
	String get(String hash, String key);

	@Nullable
	<V> V get(String hash, String key, Class<V> clazz);

	Map<String, String> get(String hash, String... keys);

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

	List<String> values(String hash);

	Map<String, String> all(String hash);

}
