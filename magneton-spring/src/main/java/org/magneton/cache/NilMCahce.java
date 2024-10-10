package org.magneton.cache;

/**
 * 关闭的缓存，不提供任何功能.
 *
 * @author zhangmsh
 * @since 2024
 */
public class NilMCahce implements MCache {

	@Override
	public boolean usable() {
		return false;
	}

	@Override
	public String clientId() {
		return null;
	}

	@Override
	public boolean del(String key) {
		return false;
	}

	@Override
	public long ttl(String key) {
		return -2;
	}

	@Override
	public String get(String key) {
		return null;
	}

	@Override
	public <T> T get(String key, Class<T> clazz) {
		return null;
	}

	@Override
	public void set(String key, String value) {
	}

	@Override
	public <V> void set(String key, V value) {
	}

	@Override
	public void set(String key, String value, int expire) {
	}

	@Override
	public <V> void set(String key, V value, int expire) {
	}

	@Override
	public boolean exist(String key) {
		return false;
	}

	@Override
	public void expire(String key, int expire) {

	}

}
