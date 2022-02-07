/*
 * Copyright (C) 2012 The Guava Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package org.magneton.core.collect;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.annotation.CanIgnoreReturnValue;
import javax.annotation.CheckForNull;
import javax.annotation.WeakOuter;

import org.magneton.core.base.Preconditions;
import org.magneton.core.base.Predicate;

import static java.util.Collections.emptyList;
import static java.util.Collections.emptySet;

/**
 * Implementation of
 * {@link Multimaps#filterKeys(Multimap, org.magneton.core.base.Predicate)}.
 *
 * @author Louis Wasserman
 */
@ElementTypesAreNonnullByDefault
class FilteredKeyMultimap<K extends Object, V extends Object> extends org.magneton.core.collect.AbstractMultimap<K, V>
		implements org.magneton.core.collect.FilteredMultimap<K, V> {

	final Multimap<K, V> unfiltered;

	final org.magneton.core.base.Predicate<? super K> keyPredicate;

	FilteredKeyMultimap(Multimap<K, V> unfiltered, org.magneton.core.base.Predicate<? super K> keyPredicate) {
		this.unfiltered = Preconditions.checkNotNull(unfiltered);
		this.keyPredicate = Preconditions.checkNotNull(keyPredicate);
	}

	@Override
	public Multimap<K, V> unfiltered() {
		return unfiltered;
	}

	@Override
	public Predicate<? super Entry<K, V>> entryPredicate() {
		return Maps.keyPredicateOnEntries(keyPredicate);
	}

	@Override
	public int size() {
		int size = 0;
		for (Collection<V> collection : asMap().values()) {
			size += collection.size();
		}
		return size;
	}

	@Override
	public boolean containsKey(@CheckForNull Object key) {
		if (unfiltered.containsKey(key)) {
			// k is equal to a K, if not one itself
			K k = (K) key;
			return keyPredicate.apply(k);
		}
		return false;
	}

	@Override
	public Collection<V> removeAll(@CheckForNull Object key) {
		return containsKey(key) ? unfiltered.removeAll(key) : unmodifiableEmptyCollection();
	}

	Collection<V> unmodifiableEmptyCollection() {
		if (unfiltered instanceof org.magneton.core.collect.SetMultimap) {
			return emptySet();
		}
		else {
			return emptyList();
		}
	}

	@Override
	public void clear() {
		keySet().clear();
	}

	@Override
	Set<K> createKeySet() {
		return Sets.filter(unfiltered.keySet(), keyPredicate);
	}

	@Override
	public Collection<V> get(@ParametricNullness K key) {
		if (keyPredicate.apply(key)) {
			return unfiltered.get(key);
		}
		else if (unfiltered instanceof SetMultimap) {
			return new AddRejectingSet<>(key);
		}
		else {
			return new AddRejectingList<>(key);
		}
	}

	@Override
	Iterator<Entry<K, V>> entryIterator() {
		throw new AssertionError("should never be called");
	}

	@Override
	Collection<Entry<K, V>> createEntries() {
		return new Entries();
	}

	@Override
	Collection<V> createValues() {
		return new FilteredMultimapValues<>(this);
	}

	@Override
	Map<K, Collection<V>> createAsMap() {
		return Maps.filterKeys(unfiltered.asMap(), keyPredicate);
	}

	@Override
	Multiset<K> createKeys() {
		return Multisets.filter(unfiltered.keys(), keyPredicate);
	}

	static class AddRejectingSet<K extends Object, V extends Object> extends ForwardingSet<V> {

		@ParametricNullness
		final K key;

		AddRejectingSet(@ParametricNullness K key) {
			this.key = key;
		}

		@Override
		public boolean add(@ParametricNullness V element) {
			throw new IllegalArgumentException("Key does not satisfy predicate: " + key);
		}

		@Override
		public boolean addAll(Collection<? extends V> collection) {
			Preconditions.checkNotNull(collection);
			throw new IllegalArgumentException("Key does not satisfy predicate: " + key);
		}

		@Override
		protected Set<V> delegate() {
			return Collections.emptySet();
		}

	}

	static class AddRejectingList<K extends Object, V extends Object> extends ForwardingList<V> {

		@ParametricNullness
		final K key;

		AddRejectingList(@ParametricNullness K key) {
			this.key = key;
		}

		@Override
		public boolean add(@ParametricNullness V v) {
			add(0, v);
			return true;
		}

		@Override
		public void add(int index, @ParametricNullness V element) {
			Preconditions.checkPositionIndex(index, 0);
			throw new IllegalArgumentException("Key does not satisfy predicate: " + key);
		}

		@Override
		public boolean addAll(Collection<? extends V> collection) {
			addAll(0, collection);
			return true;
		}

		@CanIgnoreReturnValue
		@Override
		public boolean addAll(int index, Collection<? extends V> elements) {
			Preconditions.checkNotNull(elements);
			Preconditions.checkPositionIndex(index, 0);
			throw new IllegalArgumentException("Key does not satisfy predicate: " + key);
		}

		@Override
		protected List<V> delegate() {
			return Collections.emptyList();
		}

	}

	@WeakOuter
	class Entries extends ForwardingCollection<Entry<K, V>> {

		@Override
		protected Collection<Entry<K, V>> delegate() {
			return Collections2.filter(unfiltered.entries(), entryPredicate());
		}

		@Override
		public boolean remove(@CheckForNull Object o) {
			if (o instanceof Entry) {
				Entry<?, ?> entry = (Entry<?, ?>) o;
				if (unfiltered.containsKey(entry.getKey())
						// if this holds, then we know entry.getKey() is a K
						&& keyPredicate.apply((K) entry.getKey())) {
					return unfiltered.remove(entry.getKey(), entry.getValue());
				}
			}
			return false;
		}

	}

}
