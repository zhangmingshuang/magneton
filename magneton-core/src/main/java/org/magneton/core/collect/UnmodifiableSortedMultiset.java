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
import java.util.NavigableSet;

import javax.annotation.CheckForNull;

import org.magneton.core.collect.Multisets.UnmodifiableMultiset;

/**
 * Implementation of
 * {@link org.magneton.core.collect.Multisets#unmodifiableSortedMultiset(org.magneton.core.collect.SortedMultiset)},
 * split out into its own file so it can be GWT emulated (to deal with the differing
 * elementSet() types in GWT and non-GWT).
 *
 * @author Louis Wasserman
 */

@ElementTypesAreNonnullByDefault
final class UnmodifiableSortedMultiset<E extends Object> extends UnmodifiableMultiset<E>
		implements org.magneton.core.collect.SortedMultiset<E> {

	private static final long serialVersionUID = 0;

	@CheckForNull
	private transient UnmodifiableSortedMultiset<E> descendingMultiset;

	UnmodifiableSortedMultiset(org.magneton.core.collect.SortedMultiset<E> delegate) {
		super(delegate);
	}

	@Override
	protected org.magneton.core.collect.SortedMultiset<E> delegate() {
		return (org.magneton.core.collect.SortedMultiset<E>) super.delegate();
	}

	@Override
	public Comparator<? super E> comparator() {
		return delegate().comparator();
	}

	@Override
	NavigableSet<E> createElementSet() {
		return Sets.unmodifiableNavigableSet(delegate().elementSet());
	}

	@Override
	public NavigableSet<E> elementSet() {
		return (NavigableSet<E>) super.elementSet();
	}

	@Override
	public org.magneton.core.collect.SortedMultiset<E> descendingMultiset() {
		UnmodifiableSortedMultiset<E> result = descendingMultiset;
		if (result == null) {
			result = new UnmodifiableSortedMultiset<>(delegate().descendingMultiset());
			result.descendingMultiset = this;
			return descendingMultiset = result;
		}
		return result;
	}

	@Override
	@CheckForNull
	public Multiset.Entry<E> firstEntry() {
		return delegate().firstEntry();
	}

	@Override
	@CheckForNull
	public Multiset.Entry<E> lastEntry() {
		return delegate().lastEntry();
	}

	@Override
	@CheckForNull
	public Multiset.Entry<E> pollFirstEntry() {
		throw new UnsupportedOperationException();
	}

	@Override
	@CheckForNull
	public Multiset.Entry<E> pollLastEntry() {
		throw new UnsupportedOperationException();
	}

	@Override
	public org.magneton.core.collect.SortedMultiset<E> headMultiset(@ParametricNullness E upperBound,
			org.magneton.core.collect.BoundType boundType) {
		return org.magneton.core.collect.Multisets
				.unmodifiableSortedMultiset(delegate().headMultiset(upperBound, boundType));
	}

	@Override
	public org.magneton.core.collect.SortedMultiset<E> subMultiset(@ParametricNullness E lowerBound,
			org.magneton.core.collect.BoundType lowerBoundType, @ParametricNullness E upperBound,
			org.magneton.core.collect.BoundType upperBoundType) {
		return org.magneton.core.collect.Multisets.unmodifiableSortedMultiset(delegate().subMultiset(lowerBound, lowerBoundType, upperBound, upperBoundType));
	}

	@Override
	public SortedMultiset<E> tailMultiset(@ParametricNullness E lowerBound, BoundType boundType) {
		return Multisets.unmodifiableSortedMultiset(delegate().tailMultiset(lowerBound, boundType));
	}

}
