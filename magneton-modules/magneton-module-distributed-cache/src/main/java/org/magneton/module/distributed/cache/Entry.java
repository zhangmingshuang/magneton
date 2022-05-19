package org.magneton.module.distributed.cache;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import java.util.List;

/**
 * .
 *
 * @author zhangmsh 2021/6/24
 * @since 1.0.0
 */
public class Entry<T> {

	private final String key;

	private final T value;

	public Entry(String key, T value) {
		this.key = key;
		this.value = value;
	}

	public static <T> Entry of(String key, T value) {
		Preconditions.checkNotNull(key);
		Preconditions.checkNotNull(value);
		return new Entry(key, value);
	}

	public static <T> List<Entry<T>> of(String key1, T value1, String key2, T value2) {
		Preconditions.checkNotNull(key1);
		Preconditions.checkNotNull(value1);
		Preconditions.checkNotNull(key2);
		Preconditions.checkNotNull(value2);
		List<Entry<T>> list = Lists.newArrayListWithCapacity(2);
		list.add(Entry.of(key1, value1));
		list.add(Entry.of(key2, value2));
		return list;
	}

	public static <T> List<Entry<T>> of(String key1, T value1, String key2, T value2, String key3, T value3) {
		Preconditions.checkNotNull(key1);
		Preconditions.checkNotNull(value1);
		Preconditions.checkNotNull(key2);
		Preconditions.checkNotNull(value2);
		Preconditions.checkNotNull(key3);
		Preconditions.checkNotNull(value3);
		List<Entry<T>> list = Lists.newArrayListWithCapacity(2);
		list.add(Entry.of(key1, value1));
		list.add(Entry.of(key2, value2));
		list.add(Entry.of(key3, value3));
		return list;
	}

	public String getKey() {
		return this.key;
	}

	public T getValue() {
		return this.value;
	}

}
