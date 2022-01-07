package org.magneton.cache.ops;

import java.util.Arrays;
import java.util.List;
import javax.annotation.Nullable;
import org.magneton.cache.EKV;
import org.magneton.cache.KV;

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
	default boolean set(String key, Object value) {
		return this.set(KV.of(key, value));
	}

	boolean set(KV kv);

	default List<Boolean> set(KV... kvs) {
		return this.set(Arrays.asList(kvs));
	}

	List<Boolean> set(List<KV> kvs);

	/**
	 * 如果Key不存在则设置Value，如果存在则忽略
	 * @param key Key
	 * @param value Value
	 * @return 如果设置成功返回true，否则返回false
	 */
	default boolean setNx(String key, Object value) {
		return this.setNx(KV.of(key, value));
	}

	boolean setNx(KV kv);

	default List<Boolean> setNx(KV... kvs) {
		return this.setNx(Arrays.asList(kvs));
	}

	List<Boolean> setNx(List<KV> kvs);

	/**
	 * 设置键值对
	 *
	 * <p>
	 * 如果设置的Key已经存在，则会覆盖Value
	 * @param key Key
	 * @param value Value
	 * @param expire 过期时间（秒）
	 */
	default boolean setEx(String key, Object value, long expire) {
		return this.setEx(EKV.of(key, value, expire));
	}

	default boolean setEx(KV kv, long expire) {
		return this.setEx(EKV.of(kv.getKey(), kv.getValue(), expire));
	}

	default List<Boolean> setEx(EKV... ekvs) {
		return this.setEx(Arrays.asList(ekvs));
	}

	List<Boolean> setEx(List<EKV> ekvs);

	boolean setEx(EKV ekv);

	@Nullable
	String get(String key);

	@Nullable
	<T> T get(String key, Class<T> clazz);

}
