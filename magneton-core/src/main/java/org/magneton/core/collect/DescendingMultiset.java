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

import java.util.Comparator;
import java.util.Iterator;
import java.util.NavigableSet;
import java.util.Set;

import javax.annotation.CheckForNull;
import javax.annotation.Nullable;

import com.google.j2objc.annotations.WeakOuter;

/**
 * A skeleton implementation of a descending multiset. Only needs
 * {@code forwardMultiset()} and {@code entryIterator()}.
 *
 * @author Louis Wasserman
 */
@ElementTypesAreNonnullByDefault
abstract class DescendingMultiset<E extends Object> extends ForwardingMultiset<E>
		implements org.magneton.core.collect.SortedMultiset<E> {

	@CheckForNull
	private transient Comparator<? super E> comparator;

	@CheckForNull
	private transient NavigableSet<E> elementSet;

	@CheckForNull
	private transient Set<Multiset.Entry<E>> entrySet;

	abstract org.magneton.core.collect.SortedMultiset<E> forwardMultiset();

	@Override
	public Comparator<? super E> comparator() {
		Comparator<? super E> result = comparator;
		if (result == null) {
			return comparator = Ordering.from(forwardMultiset().comparator()).<E>reverse();
		}
		return result;
	}

	@Override
	public NavigableSet<E> elementSet() {
		NavigableSet<E> result = elementSet;
		if (result == null) {
			return elementSet = new org.magneton.core.collect.SortedMultisets.NavigableElementSet<>(this);
		}
		return result;
	}

	@Override
	@CheckForNull
	public Multiset.Entry<E> pollFirstEntry() {
		return forwardMultiset().pollLastEntry();
	}

	@Override
	@CheckForNull
	public Multiset.Entry<E> pollLastEntry() {
		return forwardMultiset().pollFirstEntry();
	}

	@Override
	public org.magneton.core.collect.SortedMultiset<E> headMultiset(@ParametricNullness E toElement,
			org.magneton.core.collect.BoundType boundType) {
		return forwardMultiset().tailMultiset(toElement, boundType).descendingMultiset();
	}

	@Override
	public org.magneton.core.collect.SortedMultiset<E> subMultiset(@ParametricNullness E fromElement,
			org.magneton.core.collect.BoundType fromBoundType, @ParametricNullness E toElement,
			org.magneton.core.collect.BoundType toBoundType) {
		return forwardMultiset().subMultiset(toElement, toBoundType, fromElement, fromBoundType).descendingMultiset();
	}

	@Override
	public org.magneton.core.collect.SortedMultiset<E> tailMultiset(@ParametricNullness E fromElement,
			BoundType boundType) {
		return forwardMultiset().headMultiset(fromElement, boundType).descendingMultiset();
	}

	@Override
	protected Multiset<E> delegate() {
		return forwardMultiset();
	}

	@Override
	public SortedMultiset<E> descendingMultiset() {
		return forwardMultiset();
	}

	@Override
	@CheckForNull
	public Multiset.Entry<E> firstEntry() {
		return forwardMultiset().lastEntry();
	}

	@Override
	@CheckForNull
	public Multiset.Entry<E> lastEntry() {
		return forwardMultiset().firstEntry();
	}

	abstract Iterator<Multiset.Entry<E>> entryIterator();

	@Override
	public Set<Multiset.Entry<E>> entrySet() {
		Set<Multiset.Entry<E>> result = entrySet;
		return (result == null) ? entrySet = createEntrySet() : result;
	}

	Set<Multiset.Entry<E>> createEntrySet() {
		@WeakOuter
		class EntrySetImpl extends org.magneton.core.collect.Multisets.EntrySet<E> {

			@Override
			Multiset<E> multiset() {
				return DescendingMultiset.this;
			}

			@Override
			public Iterator<Multiset.Entry<E>> iterator() {
				return entryIterator();
			}

			@Override
			public int size() {
				return forwardMultiset().entrySet().size();
			}

		}
		return new EntrySetImpl();
	}

	@Override
	public Iterator<E> iterator() {
		return Multisets.iteratorImpl(this);
	}

	@Override
	public @Nullable Object[] toArray() {
		return standardToArray();
	}

	@Override
	// b/192354773 in our checker affects toArray
	// declarations
	public <T> T[] toArray(T[] array) {
		return standardToArray(array);
	}

	@Override
	public String toString() {
		return entrySet().toString();
	}

}
