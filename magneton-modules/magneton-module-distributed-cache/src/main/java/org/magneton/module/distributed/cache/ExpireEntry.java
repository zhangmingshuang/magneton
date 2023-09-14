package org.magneton.module.distributed.cache;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.List;

/**
 * 有效期的键值.
 *
 * @author zhangmsh
 * @since 1.0.0
 */
public class ExpireEntry<T> {

	private final long expire;

	private final String key;

	private final T value;

	public ExpireEntry(String key, T value, long expire) {
		this.key = key;
		this.value = value;
		this.expire = expire;
	}

	public static <T> ExpireEntry<T> of(String key, T value, long expire) {
		return new ExpireEntry(key, value, expire);
	}

	public static <T> List<ExpireEntry<T>> of(String key1, T value1, long expire1, String key2, T value2,
			long expire2) {
		Preconditions.checkNotNull(key1);
		Preconditions.checkNotNull(value1);
		Preconditions.checkNotNull(key2);
		Preconditions.checkNotNull(value2);

		List<ExpireEntry<T>> list = Lists.newArrayListWithCapacity(2);
		list.add(ExpireEntry.of(key1, value1, expire1));
		list.add(ExpireEntry.of(key2, value2, expire2));
		return list;
	}

	public static <T> List<ExpireEntry<T>> of(String key1, T value1, long expire1, String key2, T value2, long expire2,
			String key3, T value3, long expire3) {
		Preconditions.checkNotNull(key1);
		Preconditions.checkNotNull(value1);
		Preconditions.checkNotNull(key2);
		Preconditions.checkNotNull(value2);
		Preconditions.checkNotNull(key3);
		Preconditions.checkNotNull(value3);

		List<ExpireEntry<T>> list = Lists.newArrayListWithCapacity(2);
		list.add(ExpireEntry.of(key1, value1, expire1));
		list.add(ExpireEntry.of(key2, value2, expire2));
		list.add(ExpireEntry.of(key3, value3, expire3));
		return list;
	}

	public String getKey() {
		return this.key;
	}

	public T getValue() {
		return this.value;
	}

	public long getExpire() {
		return this.expire;
	}

	public static class Builder<T> {

		private final List<ExpireEntry<T>> entries = new ArrayList<>();

		public Builder<T> of(String key, T value, long expire) {
			this.entries.add(ExpireEntry.of(key, value, expire));
			return this;
		}

		public List<ExpireEntry<T>> build() {
			return this.entries;
		}

	}

}
