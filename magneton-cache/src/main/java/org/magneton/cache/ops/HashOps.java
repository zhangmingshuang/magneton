package org.magneton.cache.ops;

import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Nullable;

import org.magneton.cache.KV;
import org.magneton.core.collect.Maps;

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
	default boolean put(String hash, String key, Object value) {
		return put(hash, KV.of(key, value));
	}

	boolean put(String hash, KV kv);

	boolean put(String hash, Map<String, Object> values);

	default boolean put(String hash, List<KV> values) {
		Map<String, Object> mapValues = Maps.newHashMapWithExpectedSize(values.size());
		values.forEach(kv -> mapValues.put(kv.getKey(), kv.getValue()));
		return put(hash, mapValues);
	}

	@Nullable
	String get(String hash, String key);

	@Nullable
	<T> T get(String hash, String key, Class<T> clazz);

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
