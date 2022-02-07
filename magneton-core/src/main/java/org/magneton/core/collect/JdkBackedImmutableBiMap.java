/*
 * Copyright (C) 2018 The Guava Authors
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

import java.util.Map;

import javax.annotation.CheckForNull;
import javax.annotation.Nullable;

import com.google.errorprone.annotations.concurrent.LazyInit;
import com.google.j2objc.annotations.RetainedWith;
import com.google.j2objc.annotations.WeakOuter;
import javax.annotation.VisibleForTesting;

import static java.util.Objects.requireNonNull;

/**
 * Implementation of ImmutableBiMap backed by a pair of JDK HashMaps, which have smartness
 * protecting against hash flooding.
 */
@ElementTypesAreNonnullByDefault
final class JdkBackedImmutableBiMap<K, V> extends org.magneton.core.collect.ImmutableBiMap<K, V> {

	private final transient org.magneton.core.collect.ImmutableList<Entry<K, V>> entries;

	private final Map<K, V> forwardDelegate;

	private final Map<V, K> backwardDelegate;

	@LazyInit
	@RetainedWith
	@CheckForNull
	private transient JdkBackedImmutableBiMap<V, K> inverse;

	private JdkBackedImmutableBiMap(org.magneton.core.collect.ImmutableList<Entry<K, V>> entries,
			Map<K, V> forwardDelegate, Map<V, K> backwardDelegate) {
		this.entries = entries;
		this.forwardDelegate = forwardDelegate;
		this.backwardDelegate = backwardDelegate;
	}

	@VisibleForTesting
	static <K, V> org.magneton.core.collect.ImmutableBiMap<K, V> create(int n, @Nullable Entry<K, V>[] entryArray) {
		Map<K, V> forwardDelegate = Maps.newHashMapWithExpectedSize(n);
		Map<V, K> backwardDelegate = Maps.newHashMapWithExpectedSize(n);
		for (int i = 0; i < n; i++) {
			// requireNonNull is safe because the first `n` elements have been filled in.
			Entry<K, V> e = org.magneton.core.collect.RegularImmutableMap.makeImmutable(requireNonNull(entryArray[i]));
			entryArray[i] = e;
			V oldValue = forwardDelegate.putIfAbsent(e.getKey(), e.getValue());
			if (oldValue != null) {
				throw conflictException("key", e.getKey() + "=" + oldValue, entryArray[i]);
			}
			K oldKey = backwardDelegate.putIfAbsent(e.getValue(), e.getKey());
			if (oldKey != null) {
				throw conflictException("value", oldKey + "=" + e.getValue(), entryArray[i]);
			}
		}
		org.magneton.core.collect.ImmutableList<Entry<K, V>> entryList = org.magneton.core.collect.ImmutableList
				.asImmutableList(entryArray, n);
		return new JdkBackedImmutableBiMap<>(entryList, forwardDelegate, backwardDelegate);
	}

	@Override
	public int size() {
		return entries.size();
	}

	@Override
	public ImmutableBiMap<V, K> inverse() {
		JdkBackedImmutableBiMap<V, K> result = inverse;
		if (result == null) {
			inverse = result = new JdkBackedImmutableBiMap<>(new InverseEntries(), backwardDelegate, forwardDelegate);
			result.inverse = this;
		}
		return result;
	}

	@Override
	@CheckForNull
	public V get(@CheckForNull Object key) {
		return forwardDelegate.get(key);
	}

	@Override
	org.magneton.core.collect.ImmutableSet<Entry<K, V>> createEntrySet() {
		return new org.magneton.core.collect.ImmutableMapEntrySet.RegularEntrySet<>(this, entries);
	}

	@Override
	ImmutableSet<K> createKeySet() {
		return new org.magneton.core.collect.ImmutableMapKeySet<>(this);
	}

	@Override
	boolean isPartialView() {
		return false;
	}

	@WeakOuter
	private final class InverseEntries extends ImmutableList<Entry<V, K>> {

		@Override
		public Entry<V, K> get(int index) {
			Entry<K, V> entry = entries.get(index);
			return Maps.immutableEntry(entry.getValue(), entry.getKey());
		}

		@Override
		boolean isPartialView() {
			return false;
		}

		@Override
		public int size() {
			return entries.size();
		}

	}

}
