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

import java.io.Serializable;
import java.util.NoSuchElementException;
import java.util.Set;

import javax.annotation.CheckForNull;

/**
 * An empty contiguous set.
 *
 * @author Gregory Kick
 */
// allow ungenerified Comparable types
@ElementTypesAreNonnullByDefault
final class EmptyContiguousSet<C extends Comparable> extends org.magneton.core.collect.ContiguousSet<C> {

	EmptyContiguousSet(org.magneton.core.collect.DiscreteDomain<C> domain) {
		super(domain);
	}

	@Override
	public C first() {
		throw new NoSuchElementException();
	}

	@Override
	public C last() {
		throw new NoSuchElementException();
	}

	@Override
	public int size() {
		return 0;
	}

	@Override
	public org.magneton.core.collect.ContiguousSet<C> intersection(org.magneton.core.collect.ContiguousSet<C> other) {
		return this;
	}

	@Override
	public Range<C> range() {
		throw new NoSuchElementException();
	}

	@Override
	public Range<C> range(org.magneton.core.collect.BoundType lowerBoundType, BoundType upperBoundType) {
		throw new NoSuchElementException();
	}

	@Override
	org.magneton.core.collect.ContiguousSet<C> headSetImpl(C toElement, boolean inclusive) {
		return this;
	}

	@Override
	org.magneton.core.collect.ContiguousSet<C> subSetImpl(C fromElement, boolean fromInclusive, C toElement,
			boolean toInclusive) {
		return this;
	}

	@Override
	ContiguousSet<C> tailSetImpl(C fromElement, boolean fromInclusive) {
		return this;
	}

	@Override
	public boolean contains(@CheckForNull Object object) {
		return false;
	}

	@Override
	int indexOf(@CheckForNull Object target) {
		return -1;
	}

	@Override
	public UnmodifiableIterator<C> iterator() {
		return org.magneton.core.collect.Iterators.emptyIterator();
	}

	@Override
	public UnmodifiableIterator<C> descendingIterator() {
		return Iterators.emptyIterator();
	}

	@Override
	boolean isPartialView() {
		return false;
	}

	@Override
	public boolean isEmpty() {
		return true;
	}

	@Override
	public org.magneton.core.collect.ImmutableList<C> asList() {
		return ImmutableList.of();
	}

	@Override
	public String toString() {
		return "[]";
	}

	@Override
	public boolean equals(@CheckForNull Object object) {
		if (object instanceof Set) {
			Set<?> that = (Set<?>) object;
			return that.isEmpty();
		}
		return false;
	}

	@Override
	boolean isHashCodeFast() {
		return true;
	}

	@Override
	public int hashCode() {
		return 0;
	}

	@Override
	Object writeReplace() {
		return new SerializedForm<>(domain);
	}

	@Override
	org.magneton.core.collect.ImmutableSortedSet<C> createDescendingSet() {
		return ImmutableSortedSet.emptySet(Ordering.natural().reverse());
	}

	private static final class SerializedForm<C extends Comparable> implements Serializable {

		private static final long serialVersionUID = 0;

		private final org.magneton.core.collect.DiscreteDomain<C> domain;

		private SerializedForm(DiscreteDomain<C> domain) {
			this.domain = domain;
		}

		private Object readResolve() {
			return new EmptyContiguousSet<>(domain);
		}

	}

}
