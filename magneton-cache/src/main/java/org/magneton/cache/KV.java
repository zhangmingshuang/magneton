package org.magneton.cache;

/**
 * .
 *
 * @author zhangmsh 2021/6/24
 * @since 1.0.0
 */
public class KV<T> {

	private final String key;

	private final T value;

	public KV(String key, T value) {
		this.key = key;
		this.value = value;
	}

	public static <T> KV of(String key, T value) {
		return new KV(key, value);
	}

	public String getKey() {
		return this.key;
	}

	public T getValue() {
		return this.value;
	}

}
