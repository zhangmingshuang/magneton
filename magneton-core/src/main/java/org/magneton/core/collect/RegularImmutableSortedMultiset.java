/*
 * Copyright (C) 2011 The Guava Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the
 * License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.magneton.core.collect;

import java.util.Comparator;
import java.util.function.ObjIntConsumer;

import javax.annotation.CheckForNull;
import javax.annotation.VisibleForTesting;

import org.magneton.core.base.Preconditions;
import org.magneton.core.primitives.Ints;

import static org.magneton.core.collect.BoundType.CLOSED;

/**
 * An immutable sorted multiset with one or more distinct elements.
 *
 * @author Louis Wasserman
 */
// uses writeReplace, not default serialization

@ElementTypesAreNonnullByDefault
final class RegularImmutableSortedMultiset<E> extends org.magneton.core.collect.ImmutableSortedMultiset<E> {

	static final org.magneton.core.collect.ImmutableSortedMultiset<Comparable> NATURAL_EMPTY_MULTISET = new RegularImmutableSortedMultiset<>(
			Ordering.natural());

	private static final long[] ZERO_CUMULATIVE_COUNTS = { 0 };

	@VisibleForTesting
	final transient org.magneton.core.collect.RegularImmutableSortedSet<E> elementSet;

	private final transient long[] cumulativeCounts;

	private final transient int offset;

	private final transient int length;

	RegularImmutableSortedMultiset(Comparator<? super E> comparator) {
		elementSet = org.magneton.core.collect.ImmutableSortedSet.emptySet(comparator);
		cumulativeCounts = ZERO_CUMULATIVE_COUNTS;
		offset = 0;
		length = 0;
	}

	RegularImmutableSortedMultiset(org.magneton.core.collect.RegularImmutableSortedSet<E> elementSet,
			long[] cumulativeCounts, int offset, int length) {
		this.elementSet = elementSet;
		this.cumulativeCounts = cumulativeCounts;
		this.offset = offset;
		this.length = length;
	}

	private int getCount(int index) {
		return (int) (cumulativeCounts[offset + index + 1] - cumulativeCounts[offset + index]);
	}

	@Override
	Multiset.Entry<E> getEntry(int index) {
		return Multisets.immutableEntry(elementSet.asList().get(index), getCount(index));
	}

	@Override
	public void forEachEntry(ObjIntConsumer<? super E> action) {
		Preconditions.checkNotNull(action);
		for (int i = 0; i < length; i++) {
			action.accept(elementSet.asList().get(i), getCount(i));
		}
	}

	@Override
	@CheckForNull
	public Multiset.Entry<E> firstEntry() {
		return isEmpty() ? null : getEntry(0);
	}

	@Override
	@CheckForNull
	public Multiset.Entry<E> lastEntry() {
		return isEmpty() ? null : getEntry(length - 1);
	}

	@Override
	public int count(@CheckForNull Object element) {
		int index = elementSet.indexOf(element);
		return (index >= 0) ? getCount(index) : 0;
	}

	@Override
	public int size() {
		long size = cumulativeCounts[offset + length] - cumulativeCounts[offset];
		return Ints.saturatedCast(size);
	}

	@Override
	public ImmutableSortedSet<E> elementSet() {
		return elementSet;
	}

	@Override
	public org.magneton.core.collect.ImmutableSortedMultiset<E> headMultiset(E upperBound,
			org.magneton.core.collect.BoundType boundType) {
		return getSubMultiset(0, elementSet.headIndex(upperBound, Preconditions.checkNotNull(boundType) == CLOSED));
	}

	@Override
	public org.magneton.core.collect.ImmutableSortedMultiset<E> tailMultiset(E lowerBound, BoundType boundType) {
		return getSubMultiset(elementSet.tailIndex(lowerBound, Preconditions.checkNotNull(boundType) == CLOSED),
				length);
	}

	ImmutableSortedMultiset<E> getSubMultiset(int from, int to) {
		Preconditions.checkPositionIndexes(from, to, length);
		if (from == to) {
			return emptyMultiset(comparator());
		}
		else if (from == 0 && to == length) {
			return this;
		}
		else {
			org.magneton.core.collect.RegularImmutableSortedSet<E> subElementSet = elementSet.getSubSet(from, to);
			return new RegularImmutableSortedMultiset<E>(subElementSet, cumulativeCounts, offset + from, to - from);
		}
	}

	@Override
	boolean isPartialView() {
		return offset > 0 || length < cumulativeCounts.length - 1;
	}

}
