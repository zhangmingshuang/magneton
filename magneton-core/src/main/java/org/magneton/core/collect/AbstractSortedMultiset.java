/*
 * Copyright (C) 2011 The Guava Authors
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

import java.util.Comparator;
import java.util.Iterator;
import java.util.NavigableSet;

import javax.annotation.CheckForNull;

import com.google.j2objc.annotations.WeakOuter;
import org.magneton.core.base.Preconditions;

/**
 * This class provides a skeletal implementation of the
 * {@link org.magneton.core.collect.SortedMultiset} interface.
 *
 * <p>
 * The {@link #count} and {@link #size} implementations all iterate across the set
 * returned by {@link Multiset#entrySet()}, as do many methods acting on the set returned
 * by {@link #elementSet()}. Override those methods for better performance.
 *
 * @author Louis Wasserman
 */
@ElementTypesAreNonnullByDefault
abstract class AbstractSortedMultiset<E extends Object> extends org.magneton.core.collect.AbstractMultiset<E>
		implements org.magneton.core.collect.SortedMultiset<E> {

	@org.magneton.core.collect.GwtTransient
	final Comparator<? super E> comparator;

	@CheckForNull
	private transient org.magneton.core.collect.SortedMultiset<E> descendingMultiset;

	// needed for serialization
	AbstractSortedMultiset() {
		this((Comparator) Ordering.natural());
	}

	AbstractSortedMultiset(Comparator<? super E> comparator) {
		this.comparator = Preconditions.checkNotNull(comparator);
	}

	@Override
	public NavigableSet<E> elementSet() {
		return (NavigableSet<E>) super.elementSet();
	}

	@Override
	NavigableSet<E> createElementSet() {
		return new org.magneton.core.collect.SortedMultisets.NavigableElementSet<>(this);
	}

	@Override
	public Comparator<? super E> comparator() {
		return comparator;
	}

	@Override
	@CheckForNull
	public Multiset.Entry<E> firstEntry() {
		Iterator<Multiset.Entry<E>> entryIterator = entryIterator();
		return entryIterator.hasNext() ? entryIterator.next() : null;
	}

	@Override
	@CheckForNull
	public Multiset.Entry<E> lastEntry() {
		Iterator<Multiset.Entry<E>> entryIterator = descendingEntryIterator();
		return entryIterator.hasNext() ? entryIterator.next() : null;
	}

	@Override
	@CheckForNull
	public Multiset.Entry<E> pollFirstEntry() {
		Iterator<Multiset.Entry<E>> entryIterator = entryIterator();
		if (entryIterator.hasNext()) {
			Multiset.Entry<E> result = entryIterator.next();
			result = org.magneton.core.collect.Multisets.immutableEntry(result.getElement(), result.getCount());
			entryIterator.remove();
			return result;
		}
		return null;
	}

	@Override
	@CheckForNull
	public Multiset.Entry<E> pollLastEntry() {
		Iterator<Multiset.Entry<E>> entryIterator = descendingEntryIterator();
		if (entryIterator.hasNext()) {
			Multiset.Entry<E> result = entryIterator.next();
			result = org.magneton.core.collect.Multisets.immutableEntry(result.getElement(), result.getCount());
			entryIterator.remove();
			return result;
		}
		return null;
	}

	@Override
	public org.magneton.core.collect.SortedMultiset<E> subMultiset(@ParametricNullness E fromElement,
			org.magneton.core.collect.BoundType fromBoundType, @ParametricNullness E toElement, BoundType toBoundType) {
		// These are checked elsewhere, but NullPointerTester wants them checked eagerly.
		Preconditions.checkNotNull(fromBoundType);
		Preconditions.checkNotNull(toBoundType);
		return tailMultiset(fromElement, fromBoundType).headMultiset(toElement, toBoundType);
	}

	abstract Iterator<Multiset.Entry<E>> descendingEntryIterator();

	Iterator<E> descendingIterator() {
		return Multisets.iteratorImpl(descendingMultiset());
	}

	@Override
	public org.magneton.core.collect.SortedMultiset<E> descendingMultiset() {
		org.magneton.core.collect.SortedMultiset<E> result = descendingMultiset;
		return (result == null) ? descendingMultiset = createDescendingMultiset() : result;
	}

	org.magneton.core.collect.SortedMultiset<E> createDescendingMultiset() {
		@WeakOuter
		class DescendingMultisetImpl extends org.magneton.core.collect.DescendingMultiset<E> {

			@Override
			SortedMultiset<E> forwardMultiset() {
				return AbstractSortedMultiset.this;
			}

			@Override
			Iterator<Multiset.Entry<E>> entryIterator() {
				return descendingEntryIterator();
			}

			@Override
			public Iterator<E> iterator() {
				return descendingIterator();
			}

		}
		return new DescendingMultisetImpl();
	}

}
