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

import javax.annotation.CheckForNull;

import com.google.common.annotations.GwtIncompatible;

/**
 * Skeletal implementation of
 * {@link org.magneton.core.collect.ImmutableSortedSet#descendingSet()}.
 *
 * @author Louis Wasserman
 */
@ElementTypesAreNonnullByDefault
final class DescendingImmutableSortedSet<E> extends org.magneton.core.collect.ImmutableSortedSet<E> {

	private final org.magneton.core.collect.ImmutableSortedSet<E> forward;

	DescendingImmutableSortedSet(org.magneton.core.collect.ImmutableSortedSet<E> forward) {
		super(Ordering.from(forward.comparator()).reverse());
		this.forward = forward;
	}

	@Override
	public boolean contains(@CheckForNull Object object) {
		return forward.contains(object);
	}

	@Override
	public int size() {
		return forward.size();
	}

	@Override
	public org.magneton.core.collect.UnmodifiableIterator<E> iterator() {
		return forward.descendingIterator();
	}

	@Override
	org.magneton.core.collect.ImmutableSortedSet<E> headSetImpl(E toElement, boolean inclusive) {
		return forward.tailSet(toElement, inclusive).descendingSet();
	}

	@Override
	org.magneton.core.collect.ImmutableSortedSet<E> subSetImpl(E fromElement, boolean fromInclusive, E toElement,
			boolean toInclusive) {
		return forward.subSet(toElement, toInclusive, fromElement, fromInclusive).descendingSet();
	}

	@Override
	org.magneton.core.collect.ImmutableSortedSet<E> tailSetImpl(E fromElement, boolean inclusive) {
		return forward.headSet(fromElement, inclusive).descendingSet();
	}

	@Override
	public org.magneton.core.collect.ImmutableSortedSet<E> descendingSet() {
		return forward;
	}

	@Override
	public UnmodifiableIterator<E> descendingIterator() {
		return forward.iterator();
	}

	@Override

	ImmutableSortedSet<E> createDescendingSet() {
		throw new AssertionError("should never be called");
	}

	@Override
	@CheckForNull
	public E lower(E element) {
		return forward.higher(element);
	}

	@Override
	@CheckForNull
	public E floor(E element) {
		return forward.ceiling(element);
	}

	@Override
	@CheckForNull
	public E ceiling(E element) {
		return forward.floor(element);
	}

	@Override
	@CheckForNull
	public E higher(E element) {
		return forward.lower(element);
	}

	@Override
	int indexOf(@CheckForNull Object target) {
		int index = forward.indexOf(target);
		if (index == -1) {
			return index;
		}
		else {
			return size() - 1 - index;
		}
	}

	@Override
	boolean isPartialView() {
		return forward.isPartialView();
	}

}
