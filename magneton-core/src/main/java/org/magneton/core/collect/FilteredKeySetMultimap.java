/*
 * Copyright (C) 2012 The Guava Authors
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

import java.util.Map.Entry;
import java.util.Set;

import javax.annotation.CheckForNull;

import org.magneton.core.base.Predicate;

/**
 * Implementation of
 * {@link Multimaps#filterKeys(org.magneton.core.collect.SetMultimap, org.magneton.core.base.Predicate)}.
 *
 * @author Louis Wasserman
 */
@ElementTypesAreNonnullByDefault
final class FilteredKeySetMultimap<K extends Object, V extends Object>
		extends org.magneton.core.collect.FilteredKeyMultimap<K, V>
		implements org.magneton.core.collect.FilteredSetMultimap<K, V> {

	FilteredKeySetMultimap(org.magneton.core.collect.SetMultimap<K, V> unfiltered, Predicate<? super K> keyPredicate) {
		super(unfiltered, keyPredicate);
	}

	@Override
	public org.magneton.core.collect.SetMultimap<K, V> unfiltered() {
		return (SetMultimap<K, V>) unfiltered;
	}

	@Override
	public Set<V> get(@ParametricNullness K key) {
		return (Set<V>) super.get(key);
	}

	@Override
	public Set<V> removeAll(@CheckForNull Object key) {
		return (Set<V>) super.removeAll(key);
	}

	@Override
	public Set<V> replaceValues(@ParametricNullness K key, Iterable<? extends V> values) {
		return (Set<V>) super.replaceValues(key, values);
	}

	@Override
	public Set<Entry<K, V>> entries() {
		return (Set<Entry<K, V>>) super.entries();
	}

	@Override
	Set<Entry<K, V>> createEntries() {
		return new EntrySet();
	}

	class EntrySet extends Entries implements Set<Entry<K, V>> {

		@Override
		public int hashCode() {
			return Sets.hashCodeImpl(this);
		}

		@Override
		public boolean equals(@CheckForNull Object o) {
			return Sets.equalsImpl(this, o);
		}

	}

}
