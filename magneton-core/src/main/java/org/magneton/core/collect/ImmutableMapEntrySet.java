/*
 * Copyright (C) 2008 The Guava Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.magneton.core.collect;

import java.io.Serializable;
import java.util.Map.Entry;
import java.util.Spliterator;
import java.util.function.Consumer;

import javax.annotation.CheckForNull;
import javax.annotation.Nullable;

/**
 * {@code entrySet()} implementation for {@link org.magneton.core.collect.ImmutableMap}.
 *
 * @author Jesse Wilson
 * @author Kevin Bourrillion
 */
@ElementTypesAreNonnullByDefault
abstract class ImmutableMapEntrySet<K, V> extends ImmutableSet.CachingAsList<Entry<K, V>> {

	ImmutableMapEntrySet() {
	}

	abstract org.magneton.core.collect.ImmutableMap<K, V> map();

	@Override
	public int size() {
		return map().size();
	}

	@Override
	public boolean contains(@CheckForNull Object object) {
		if (object instanceof Entry) {
			Entry<?, ?> entry = (Entry<?, ?>) object;
			V value = map().get(entry.getKey());
			return value != null && value.equals(entry.getValue());
		}
		return false;
	}

	@Override
	boolean isPartialView() {
		return map().isPartialView();
	}

	@Override

	// not used in GWT
	boolean isHashCodeFast() {
		return map().isHashCodeFast();
	}

	@Override
	public int hashCode() {
		return map().hashCode();
	}

	@Override
	Object writeReplace() {
		return new EntrySetSerializedForm<>(map());
	}

	static final class RegularEntrySet<K, V> extends ImmutableMapEntrySet<K, V> {

		private final transient org.magneton.core.collect.ImmutableMap<K, V> map;

		private final transient org.magneton.core.collect.ImmutableList<Entry<K, V>> entries;

		RegularEntrySet(org.magneton.core.collect.ImmutableMap<K, V> map, Entry<K, V>[] entries) {
			this(map, org.magneton.core.collect.ImmutableList.<Entry<K, V>>asImmutableList(entries));
		}

		RegularEntrySet(org.magneton.core.collect.ImmutableMap<K, V> map,
				org.magneton.core.collect.ImmutableList<Entry<K, V>> entries) {
			this.map = map;
			this.entries = entries;
		}

		@Override
		org.magneton.core.collect.ImmutableMap<K, V> map() {
			return map;
		}

		@Override

		int copyIntoArray(@Nullable Object[] dst, int offset) {
			return entries.copyIntoArray(dst, offset);
		}

		@Override
		public UnmodifiableIterator<Entry<K, V>> iterator() {
			return entries.iterator();
		}

		@Override
		public Spliterator<Entry<K, V>> spliterator() {
			return entries.spliterator();
		}

		@Override
		public void forEach(Consumer<? super Entry<K, V>> action) {
			entries.forEach(action);
		}

		@Override
		ImmutableList<Entry<K, V>> createAsList() {
			return new RegularImmutableAsList<>(this, entries);
		}

	}

	private static class EntrySetSerializedForm<K, V> implements Serializable {

		private static final long serialVersionUID = 0;

		final org.magneton.core.collect.ImmutableMap<K, V> map;

		EntrySetSerializedForm(ImmutableMap<K, V> map) {
			this.map = map;
		}

		Object readResolve() {
			return map.entrySet();
		}

	}

}
