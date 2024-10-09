package org.magneton.spring.cache;

/**
 * 提供缓存的能力，内部动态切换缓存实现.
 *
 * @author zhangmsh
 * @since 2024
 */
public interface MCache {

	String get(String key);

	<T> T get(String key, Class<T> clazz);

	void set(String key, Object value);

}
