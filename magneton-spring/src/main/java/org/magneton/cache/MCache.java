package org.magneton.cache;

import javax.annotation.Nullable;

/**
 * 提供缓存的能力，内部动态切换缓存实现.
 *
 * @author zhangmsh
 * @since 2024
 */
public interface MCache {

	default boolean usable() {
		return true;
	}

	String clientId();

	boolean del(String key);

	/**
	 * 获取key的剩余时间.
	 * @param key key
	 * @return 剩余时间，单位毫秒。如果key不存在则返回-2，如果key存在但没有设置剩余时间则返回-1.否则返回剩余时间.
	 */
	long ttl(String key);

	@Nullable
	String get(String key);

	@Nullable
	<T> T get(String key, Class<T> clazz);

	void set(String key, @Nullable String value);

	<V> void set(String key, @Nullable V value);

	void set(String key, @Nullable String value, int expire);

	<V> void set(String key, @Nullable V value, int expire);

	boolean exist(String key);

	void expire(String key, int expire);

}
