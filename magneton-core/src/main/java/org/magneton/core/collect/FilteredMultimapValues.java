/*
 * Copyright (C) 2013 The Guava Authors
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

import java.util.AbstractCollection;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map.Entry;

import javax.annotation.CheckForNull;

import com.google.j2objc.annotations.Weak;
import org.magneton.core.base.Objects;
import org.magneton.core.base.Preconditions;
import org.magneton.core.base.Predicate;
import org.magneton.core.base.Predicates;

/**
 * Implementation for {@link org.magneton.core.collect.FilteredMultimap#values()}.
 *
 * @author Louis Wasserman
 */
@ElementTypesAreNonnullByDefault
final class FilteredMultimapValues<K extends Object, V extends Object> extends AbstractCollection<V> {

	@Weak
	private final org.magneton.core.collect.FilteredMultimap<K, V> multimap;

	FilteredMultimapValues(org.magneton.core.collect.FilteredMultimap<K, V> multimap) {
		this.multimap = Preconditions.checkNotNull(multimap);
	}

	@Override
	public Iterator<V> iterator() {
		return Maps.valueIterator(multimap.entries().iterator());
	}

	@Override
	public boolean contains(@CheckForNull Object o) {
		return multimap.containsValue(o);
	}

	@Override
	public int size() {
		return multimap.size();
	}

	@Override
	public boolean remove(@CheckForNull Object o) {
		Predicate<? super Entry<K, V>> entryPredicate = multimap.entryPredicate();
		for (Iterator<Entry<K, V>> unfilteredItr = multimap.unfiltered().entries().iterator(); unfilteredItr
				.hasNext();) {
			Entry<K, V> entry = unfilteredItr.next();
			if (entryPredicate.apply(entry) && Objects.equal(entry.getValue(), o)) {
				unfilteredItr.remove();
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		return org.magneton.core.collect.Iterables.removeIf(multimap.unfiltered().entries(),
				// explicit <Entry<K, V>> is required to build with JDK6
				org.magneton.core.base.Predicates.<Entry<K, V>>and(multimap.entryPredicate(),
						Maps.<V>valuePredicateOnEntries(org.magneton.core.base.Predicates.in(c))));
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		return Iterables.removeIf(multimap.unfiltered().entries(),
				// explicit <Entry<K, V>> is required to build with JDK6
				org.magneton.core.base.Predicates.<Entry<K, V>>and(multimap.entryPredicate(),
						Maps.<V>valuePredicateOnEntries(org.magneton.core.base.Predicates.not(Predicates.in(c)))));
	}

	@Override
	public void clear() {
		multimap.clear();
	}

}
