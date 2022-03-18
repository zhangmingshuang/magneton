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
import java.util.Collection;

import javax.annotation.CheckForNull;

import org.magneton.core.base.Preconditions;

import static java.util.Objects.requireNonNull;
import static org.magneton.core.collect.BoundType.CLOSED;

/**
 * An implementation of {@link org.magneton.core.collect.ContiguousSet} that contains one
 * or more elements.
 *
 * @author Gregory Kick
 */

// allow ungenerified Comparable types
@ElementTypesAreNonnullByDefault
final class RegularContiguousSet<C extends Comparable> extends org.magneton.core.collect.ContiguousSet<C> {

	private static final long serialVersionUID = 0;

	private final Range<C> range;

	RegularContiguousSet(Range<C> range, org.magneton.core.collect.DiscreteDomain<C> domain) {
		super(domain);
		this.range = range;
	}

	private static boolean equalsOrThrow(Comparable<?> left, @CheckForNull Comparable<?> right) {
		return right != null && Range.compareOrThrow(left, right) == 0;
	}

	private org.magneton.core.collect.ContiguousSet<C> intersectionInCurrentDomain(Range<C> other) {
		return range.isConnected(other)
				? org.magneton.core.collect.ContiguousSet.create(range.intersection(other), domain)
				: new org.magneton.core.collect.EmptyContiguousSet<C>(domain);
	}

	@Override
	org.magneton.core.collect.ContiguousSet<C> headSetImpl(C toElement, boolean inclusive) {
		return intersectionInCurrentDomain(
				Range.upTo(toElement, org.magneton.core.collect.BoundType.forBoolean(inclusive)));
	}

	@Override
	org.magneton.core.collect.ContiguousSet<C> subSetImpl(C fromElement, boolean fromInclusive, C toElement,
			boolean toInclusive) {
		if (fromElement.compareTo(toElement) == 0 && !fromInclusive && !toInclusive) {
			// Range would reject our attempt to create (x, x).
			return new org.magneton.core.collect.EmptyContiguousSet<>(domain);
		}
		return intersectionInCurrentDomain(
				Range.range(fromElement, org.magneton.core.collect.BoundType.forBoolean(fromInclusive), toElement,
						org.magneton.core.collect.BoundType.forBoolean(toInclusive)));
	}

	@Override
	org.magneton.core.collect.ContiguousSet<C> tailSetImpl(C fromElement, boolean inclusive) {
		return intersectionInCurrentDomain(
				Range.downTo(fromElement, org.magneton.core.collect.BoundType.forBoolean(inclusive)));
	}

	@Override
	int indexOf(@CheckForNull Object target) {
		// requireNonNull is safe because of the contains check.
		return contains(target) ? (int) domain.distance(first(), (C) requireNonNull(target)) : -1;
	}

	@Override
	public org.magneton.core.collect.UnmodifiableIterator<C> iterator() {
		return new AbstractSequentialIterator<C>(first()) {
			final C last = last();

			@Override
			@CheckForNull
			protected C computeNext(C previous) {
				return equalsOrThrow(previous, last) ? null : domain.next(previous);
			}
		};
	}

	@Override
	public UnmodifiableIterator<C> descendingIterator() {
		return new AbstractSequentialIterator<C>(last()) {
			final C first = first();

			@Override
			@CheckForNull
			protected C computeNext(C previous) {
				return equalsOrThrow(previous, first) ? null : domain.previous(previous);
			}
		};
	}

	@Override
	boolean isPartialView() {
		return false;
	}

	@Override
	public C first() {
		// requireNonNull is safe because we checked the range is not empty in
		// ContiguousSet.create.
		return requireNonNull(range.lowerBound.leastValueAbove(domain));
	}

	@Override
	public C last() {
		// requireNonNull is safe because we checked the range is not empty in
		// ContiguousSet.create.
		return requireNonNull(range.upperBound.greatestValueBelow(domain));
	}

	@Override
	ImmutableList<C> createAsList() {
		if (domain.supportsFastOffset) {
			return new org.magneton.core.collect.ImmutableAsList<C>() {
				@Override
				ImmutableSortedSet<C> delegateCollection() {
					return RegularContiguousSet.this;
				}

				@Override
				public C get(int i) {
					Preconditions.checkElementIndex(i, size());
					return domain.offset(first(), i);
				}
			};
		}
		else {
			return super.createAsList();
		}
	}

	@Override
	public int size() {
		long distance = domain.distance(first(), last());
		return (distance >= Integer.MAX_VALUE) ? Integer.MAX_VALUE : (int) distance + 1;
	}

	@Override
	public boolean contains(@CheckForNull Object object) {
		if (object == null) {
			return false;
		}
		try {
			return range.contains((C) object);
		}
		catch (ClassCastException e) {
			return false;
		}
	}

	@Override
	public boolean containsAll(Collection<?> targets) {
		return Collections2.containsAllImpl(this, targets);
	}

	@Override
	public boolean isEmpty() {
		return false;
	}

	@Override
	public org.magneton.core.collect.ContiguousSet<C> intersection(org.magneton.core.collect.ContiguousSet<C> other) {
		Preconditions.checkNotNull(other);
		Preconditions.checkArgument(domain.equals(other.domain));
		if (other.isEmpty()) {
			return other;
		}
		else {
			C lowerEndpoint = Ordering.natural().max(first(), other.first());
			C upperEndpoint = Ordering.natural().min(last(), other.last());
			return (lowerEndpoint.compareTo(upperEndpoint) <= 0)
					? ContiguousSet.create(Range.closed(lowerEndpoint, upperEndpoint), domain)
					: new org.magneton.core.collect.EmptyContiguousSet<C>(domain);
		}
	}

	@Override
	public Range<C> range() {
		return range(CLOSED, CLOSED);
	}

	@Override
	public Range<C> range(org.magneton.core.collect.BoundType lowerBoundType, BoundType upperBoundType) {
		return Range.create(range.lowerBound.withLowerBoundType(lowerBoundType, domain),
				range.upperBound.withUpperBoundType(upperBoundType, domain));
	}

	@Override
	public boolean equals(@CheckForNull Object object) {
		if (object == this) {
			return true;
		}
		else if (object instanceof RegularContiguousSet) {
			RegularContiguousSet<?> that = (RegularContiguousSet<?>) object;
			if (domain.equals(that.domain)) {
				return first().equals(that.first()) && last().equals(that.last());
			}
		}
		return super.equals(object);
	}

	// copied to make sure not to use the GWT-emulated version
	@Override
	public int hashCode() {
		return Sets.hashCodeImpl(this);
	}

	@Override
	Object writeReplace() {
		return new SerializedForm<>(range, domain);
	}

	private static final class SerializedForm<C extends Comparable> implements Serializable {

		final Range<C> range;

		final org.magneton.core.collect.DiscreteDomain<C> domain;

		private SerializedForm(Range<C> range, DiscreteDomain<C> domain) {
			this.range = range;
			this.domain = domain;
		}

		private Object readResolve() {
			return new RegularContiguousSet<>(range, domain);
		}

	}

}
