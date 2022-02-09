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

import java.io.Serializable;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.stream.Collector;

import javax.annotations.CanIgnoreReturnValue;
import javax.annotation.CheckForNull;
import javax.annotations.DoNotCall;
import javax.annotations.LazyInit;

import org.magneton.core.base.Preconditions;
import org.magneton.core.collect.SortedLists.KeyAbsentBehavior;
import org.magneton.core.collect.SortedLists.KeyPresentBehavior;
import org.magneton.core.primitives.Ints;

import static java.util.Objects.requireNonNull;
import static org.magneton.core.collect.SortedLists.KeyAbsentBehavior.NEXT_HIGHER;
import static org.magneton.core.collect.SortedLists.KeyAbsentBehavior.NEXT_LOWER;
import static org.magneton.core.collect.SortedLists.KeyPresentBehavior.ANY_PRESENT;

/**
 * A {@link org.magneton.core.collect.RangeSet} whose contents will never change, with
 * many other important properties detailed at {@link ImmutableCollection}.
 *
 * @author Louis Wasserman
 * @since 14.0
 */

@ElementTypesAreNonnullByDefault
public final class ImmutableRangeSet<C extends Comparable> extends org.magneton.core.collect.AbstractRangeSet<C>
		implements Serializable {

	private static final ImmutableRangeSet<Comparable<?>> EMPTY = new ImmutableRangeSet<>(
			org.magneton.core.collect.ImmutableList.<Range<Comparable<?>>>of());

	private static final ImmutableRangeSet<Comparable<?>> ALL = new ImmutableRangeSet<>(
			org.magneton.core.collect.ImmutableList.of(Range.<Comparable<?>>all()));

	private final transient org.magneton.core.collect.ImmutableList<Range<C>> ranges;

	@LazyInit
	@CheckForNull
	private transient ImmutableRangeSet<C> complement;

	ImmutableRangeSet(org.magneton.core.collect.ImmutableList<Range<C>> ranges) {
		this.ranges = ranges;
	}

	private ImmutableRangeSet(org.magneton.core.collect.ImmutableList<Range<C>> ranges,
			ImmutableRangeSet<C> complement) {
		this.ranges = ranges;
		this.complement = complement;
	}

	/**
	 * Returns a {@code Collector} that accumulates the input elements into a new {@code
	 * ImmutableRangeSet}. As in {@link Builder}, overlapping ranges are not permitted and
	 * adjacent ranges will be merged.
	 *
	 * @since 23.1
	 */
	public static <E extends Comparable<? super E>> Collector<Range<E>, ?, ImmutableRangeSet<E>> toImmutableRangeSet() {
		return CollectCollectors.toImmutableRangeSet();
	}

	/**
	 * Returns an empty immutable range set.
	 *
	 * <p>
	 * <b>Performance note:</b> the instance returned is a singleton.
	 */
	public static <C extends Comparable> ImmutableRangeSet<C> of() {
		return (ImmutableRangeSet<C>) EMPTY;
	}

	/**
	 * Returns an immutable range set containing the specified single range. If
	 * {@link Range#isEmpty() range.isEmpty()}, this is equivalent to
	 * {@link ImmutableRangeSet#of()}.
	 */
	public static <C extends Comparable> ImmutableRangeSet<C> of(Range<C> range) {
		Preconditions.checkNotNull(range);
		if (range.isEmpty()) {
			return of();
		}
		else if (range.equals(Range.all())) {
			return all();
		}
		else {
			return new ImmutableRangeSet<C>(org.magneton.core.collect.ImmutableList.of(range));
		}
	}

	/** Returns an immutable range set containing the single range {@link Range#all()}. */
	static <C extends Comparable> ImmutableRangeSet<C> all() {
		return (ImmutableRangeSet<C>) ALL;
	}

	/** Returns an immutable copy of the specified {@code RangeSet}. */
	public static <C extends Comparable> ImmutableRangeSet<C> copyOf(org.magneton.core.collect.RangeSet<C> rangeSet) {
		Preconditions.checkNotNull(rangeSet);
		if (rangeSet.isEmpty()) {
			return of();
		}
		else if (rangeSet.encloses(Range.<C>all())) {
			return all();
		}

		if (rangeSet instanceof ImmutableRangeSet) {
			ImmutableRangeSet<C> immutableRangeSet = (ImmutableRangeSet<C>) rangeSet;
			if (!immutableRangeSet.isPartialView()) {
				return immutableRangeSet;
			}
		}
		return new ImmutableRangeSet<C>(org.magneton.core.collect.ImmutableList.copyOf(rangeSet.asRanges()));
	}

	/**
	 * Returns an {@code ImmutableRangeSet} containing each of the specified disjoint
	 * ranges. Overlapping ranges and empty ranges are forbidden, though adjacent ranges
	 * are permitted and will be merged.
	 * @throws IllegalArgumentException if any ranges overlap or are empty
	 * @since 21.0
	 */
	public static <C extends Comparable<?>> ImmutableRangeSet<C> copyOf(Iterable<Range<C>> ranges) {
		return new Builder<C>().addAll(ranges).build();
	}

	/**
	 * Returns an {@code ImmutableRangeSet} representing the union of the specified
	 * ranges.
	 *
	 * <p>
	 * This is the smallest {@code RangeSet} which encloses each of the specified ranges.
	 * Duplicate or connected ranges are permitted, and will be coalesced in the result.
	 *
	 * @since 21.0
	 */
	public static <C extends Comparable<?>> ImmutableRangeSet<C> unionOf(Iterable<Range<C>> ranges) {
		return copyOf(org.magneton.core.collect.TreeRangeSet.create(ranges));
	}

	/** Returns a new builder for an immutable range set. */
	public static <C extends Comparable<?>> Builder<C> builder() {
		return new Builder<C>();
	}

	@Override
	public boolean intersects(Range<C> otherRange) {
		int ceilingIndex = org.magneton.core.collect.SortedLists.binarySearch(ranges, Range.<C>lowerBoundFn(),
				otherRange.lowerBound, Ordering.natural(), ANY_PRESENT, NEXT_HIGHER);
		if (ceilingIndex < ranges.size() && ranges.get(ceilingIndex).isConnected(otherRange)
				&& !ranges.get(ceilingIndex).intersection(otherRange).isEmpty()) {
			return true;
		}
		return ceilingIndex > 0 && ranges.get(ceilingIndex - 1).isConnected(otherRange)
				&& !ranges.get(ceilingIndex - 1).intersection(otherRange).isEmpty();
	}

	@Override
	public boolean encloses(Range<C> otherRange) {
		int index = org.magneton.core.collect.SortedLists.binarySearch(ranges, Range.<C>lowerBoundFn(),
				otherRange.lowerBound, Ordering.natural(), ANY_PRESENT, NEXT_LOWER);
		return index != -1 && ranges.get(index).encloses(otherRange);
	}

	@Override
	@CheckForNull
	public Range<C> rangeContaining(C value) {
		int index = org.magneton.core.collect.SortedLists.binarySearch(ranges, Range.<C>lowerBoundFn(),
				Cut.belowValue(value), Ordering.natural(), ANY_PRESENT, NEXT_LOWER);
		if (index != -1) {
			Range<C> range = ranges.get(index);
			return range.contains(value) ? range : null;
		}
		return null;
	}

	@Override
	public Range<C> span() {
		if (ranges.isEmpty()) {
			throw new NoSuchElementException();
		}
		return Range.create(ranges.get(0).lowerBound, ranges.get(ranges.size() - 1).upperBound);
	}

	@Override
	public boolean isEmpty() {
		return ranges.isEmpty();
	}

	/**
	 * Guaranteed to throw an exception and leave the {@code RangeSet} unmodified.
	 * @throws UnsupportedOperationException always
	 * @deprecated Unsupported operation.
	 */
	@Deprecated
	@Override
	@DoNotCall("Always throws UnsupportedOperationException")
	public void add(Range<C> range) {
		throw new UnsupportedOperationException();
	}

	/**
	 * Guaranteed to throw an exception and leave the {@code RangeSet} unmodified.
	 * @throws UnsupportedOperationException always
	 * @deprecated Unsupported operation.
	 */
	@Deprecated
	@Override
	@DoNotCall("Always throws UnsupportedOperationException")
	public void addAll(org.magneton.core.collect.RangeSet<C> other) {
		throw new UnsupportedOperationException();
	}

	/**
	 * Guaranteed to throw an exception and leave the {@code RangeSet} unmodified.
	 * @throws UnsupportedOperationException always
	 * @deprecated Unsupported operation.
	 */
	@Deprecated
	@Override
	@DoNotCall("Always throws UnsupportedOperationException")
	public void addAll(Iterable<Range<C>> other) {
		throw new UnsupportedOperationException();
	}

	/**
	 * Guaranteed to throw an exception and leave the {@code RangeSet} unmodified.
	 * @throws UnsupportedOperationException always
	 * @deprecated Unsupported operation.
	 */
	@Deprecated
	@Override
	@DoNotCall("Always throws UnsupportedOperationException")
	public void remove(Range<C> range) {
		throw new UnsupportedOperationException();
	}

	/**
	 * Guaranteed to throw an exception and leave the {@code RangeSet} unmodified.
	 * @throws UnsupportedOperationException always
	 * @deprecated Unsupported operation.
	 */
	@Deprecated
	@Override
	@DoNotCall("Always throws UnsupportedOperationException")
	public void removeAll(org.magneton.core.collect.RangeSet<C> other) {
		throw new UnsupportedOperationException();
	}

	/**
	 * Guaranteed to throw an exception and leave the {@code RangeSet} unmodified.
	 * @throws UnsupportedOperationException always
	 * @deprecated Unsupported operation.
	 */
	@Deprecated
	@Override
	@DoNotCall("Always throws UnsupportedOperationException")
	public void removeAll(Iterable<Range<C>> other) {
		throw new UnsupportedOperationException();
	}

	@Override
	public org.magneton.core.collect.ImmutableSet<Range<C>> asRanges() {
		if (ranges.isEmpty()) {
			return org.magneton.core.collect.ImmutableSet.of();
		}
		return new RegularImmutableSortedSet<>(ranges, Range.<C>rangeLexOrdering());
	}

	@Override
	public org.magneton.core.collect.ImmutableSet<Range<C>> asDescendingSetOfRanges() {
		if (ranges.isEmpty()) {
			return ImmutableSet.of();
		}
		return new RegularImmutableSortedSet<>(ranges.reverse(), Range.<C>rangeLexOrdering().reverse());
	}

	@Override
	public ImmutableRangeSet<C> complement() {
		ImmutableRangeSet<C> result = complement;
		if (result != null) {
			return result;
		}
		else if (ranges.isEmpty()) {
			return complement = all();
		}
		else if (ranges.size() == 1 && ranges.get(0).equals(Range.all())) {
			return complement = of();
		}
		else {
			org.magneton.core.collect.ImmutableList<Range<C>> complementRanges = new ComplementRanges();
			result = complement = new ImmutableRangeSet<C>(complementRanges, this);
		}
		return result;
	}

	/**
	 * Returns a new range set consisting of the union of this range set and
	 * {@code other}.
	 *
	 * <p>
	 * This is essentially the same as {@code TreeRangeSet.create(this).addAll(other)}
	 * except it returns an {@code ImmutableRangeSet}.
	 *
	 * @since 21.0
	 */
	public ImmutableRangeSet<C> union(org.magneton.core.collect.RangeSet<C> other) {
		return unionOf(org.magneton.core.collect.Iterables.concat(asRanges(), other.asRanges()));
	}

	/**
	 * Returns a new range set consisting of the intersection of this range set and
	 * {@code other}.
	 *
	 * <p>
	 * This is essentially the same as {@code
	 * TreeRangeSet.create(this).removeAll(other.complement())} except it returns an
	 * {@code
	 * ImmutableRangeSet}.
	 *
	 * @since 21.0
	 */
	public ImmutableRangeSet<C> intersection(org.magneton.core.collect.RangeSet<C> other) {
		org.magneton.core.collect.RangeSet<C> copy = org.magneton.core.collect.TreeRangeSet.create(this);
		copy.removeAll(other.complement());
		return copyOf(copy);
	}

	/**
	 * Returns a new range set consisting of the difference of this range set and
	 * {@code other}.
	 *
	 * <p>
	 * This is essentially the same as {@code TreeRangeSet.create(this).removeAll(other)}
	 * except it returns an {@code ImmutableRangeSet}.
	 *
	 * @since 21.0
	 */
	public ImmutableRangeSet<C> difference(org.magneton.core.collect.RangeSet<C> other) {
		org.magneton.core.collect.RangeSet<C> copy = TreeRangeSet.create(this);
		copy.removeAll(other);
		return copyOf(copy);
	}

	/**
	 * Returns a list containing the nonempty intersections of {@code range} with the
	 * ranges in this range set.
	 */
	private org.magneton.core.collect.ImmutableList<Range<C>> intersectRanges(Range<C> range) {
		if (ranges.isEmpty() || range.isEmpty()) {
			return org.magneton.core.collect.ImmutableList.of();
		}
		else if (range.encloses(span())) {
			return ranges;
		}

		int fromIndex;
		if (range.hasLowerBound()) {
			fromIndex = org.magneton.core.collect.SortedLists.binarySearch(ranges, Range.<C>upperBoundFn(),
					range.lowerBound, KeyPresentBehavior.FIRST_AFTER, KeyAbsentBehavior.NEXT_HIGHER);
		}
		else {
			fromIndex = 0;
		}

		int toIndex;
		if (range.hasUpperBound()) {
			toIndex = org.magneton.core.collect.SortedLists.binarySearch(ranges, Range.<C>lowerBoundFn(),
					range.upperBound, KeyPresentBehavior.FIRST_PRESENT, KeyAbsentBehavior.NEXT_HIGHER);
		}
		else {
			toIndex = ranges.size();
		}
		int length = toIndex - fromIndex;
		if (length == 0) {
			return org.magneton.core.collect.ImmutableList.of();
		}
		else {
			return new org.magneton.core.collect.ImmutableList<Range<C>>() {
				@Override
				public int size() {
					return length;
				}

				@Override
				public Range<C> get(int index) {
					Preconditions.checkElementIndex(index, length);
					if (index == 0 || index == length - 1) {
						return ranges.get(index + fromIndex).intersection(range);
					}
					else {
						return ranges.get(index + fromIndex);
					}
				}

				@Override
				boolean isPartialView() {
					return true;
				}
			};
		}
	}

	/** Returns a view of the intersection of this range set with the given range. */
	@Override
	public ImmutableRangeSet<C> subRangeSet(Range<C> range) {
		if (!isEmpty()) {
			Range<C> span = span();
			if (range.encloses(span)) {
				return this;
			}
			else if (range.isConnected(span)) {
				return new ImmutableRangeSet<C>(intersectRanges(range));
			}
		}
		return of();
	}

	/**
	 * Returns an {@link ImmutableSortedSet} containing the same values in the given
	 * domain {@linkplain org.magneton.core.collect.RangeSet#contains contained} by this
	 * range set.
	 *
	 * <p>
	 * <b>Note:</b> {@code a.asSet(d).equals(b.asSet(d))} does not imply
	 * {@code a.equals(b)}! For example, {@code a} and {@code b} could be {@code [2..4]}
	 * and {@code (1..5)}, or the empty ranges {@code [3..3)} and {@code [4..4)}.
	 *
	 * <p>
	 * <b>Warning:</b> Be extremely careful what you do with the {@code asSet} view of a
	 * large range set (such as {@code ImmutableRangeSet.of(Range.greaterThan(0))}).
	 * Certain operations on such a set can be performed efficiently, but others (such as
	 * {@link Set#hashCode} or {@link Collections#frequency}) can cause major performance
	 * problems.
	 *
	 * <p>
	 * The returned set's {@link Object#toString} method returns a short-hand form of the
	 * set's contents, such as {@code "[1..100]}"}.
	 * @throws IllegalArgumentException if neither this range nor the domain has a lower
	 * bound, or if neither has an upper bound
	 */
	public ImmutableSortedSet<C> asSet(org.magneton.core.collect.DiscreteDomain<C> domain) {
		Preconditions.checkNotNull(domain);
		if (isEmpty()) {
			return ImmutableSortedSet.of();
		}
		Range<C> span = span().canonical(domain);
		if (!span.hasLowerBound()) {
			// according to the spec of canonical, neither this ImmutableRangeSet nor
			// the range have a lower bound
			throw new IllegalArgumentException("Neither the DiscreteDomain nor this range set are bounded below");
		}
		else if (!span.hasUpperBound()) {
			try {
				domain.maxValue();
			}
			catch (NoSuchElementException e) {
				throw new IllegalArgumentException("Neither the DiscreteDomain nor this range set are bounded above");
			}
		}

		return new AsSet(domain);
	}

	/**
	 * Returns {@code true} if this immutable range set's implementation contains
	 * references to user-created objects that aren't accessible via this range set's
	 * methods. This is generally used to determine whether {@code copyOf} implementations
	 * should make an explicit copy to avoid memory leaks.
	 */
	boolean isPartialView() {
		return ranges.isPartialView();
	}

	Object writeReplace() {
		return new SerializedForm<C>(ranges);
	}

	private static class AsSetSerializedForm<C extends Comparable> implements Serializable {

		private final org.magneton.core.collect.ImmutableList<Range<C>> ranges;

		private final org.magneton.core.collect.DiscreteDomain<C> domain;

		AsSetSerializedForm(org.magneton.core.collect.ImmutableList<Range<C>> ranges,
				org.magneton.core.collect.DiscreteDomain<C> domain) {
			this.ranges = ranges;
			this.domain = domain;
		}

		Object readResolve() {
			return new ImmutableRangeSet<C>(ranges).asSet(domain);
		}

	}

	/**
	 * A builder for immutable range sets.
	 *
	 * @since 14.0
	 */
	public static class Builder<C extends Comparable<?>> {

		private final List<Range<C>> ranges;

		public Builder() {
			ranges = Lists.newArrayList();
		}

		// TODO(lowasser): consider adding union, in addition to add, that does allow
		// overlap

		/**
		 * Add the specified range to this builder. Adjacent ranges are permitted and will
		 * be merged, but overlapping ranges will cause an exception when {@link #build()}
		 * is called.
		 * @throws IllegalArgumentException if {@code range} is empty
		 */
		@CanIgnoreReturnValue
		public Builder<C> add(Range<C> range) {
			Preconditions.checkArgument(!range.isEmpty(), "range must not be empty, but was %s", range);
			ranges.add(range);
			return this;
		}

		/**
		 * Add all ranges from the specified range set to this builder. Adjacent ranges
		 * are permitted and will be merged, but overlapping ranges will cause an
		 * exception when {@link #build()} is called.
		 */
		@CanIgnoreReturnValue
		public Builder<C> addAll(RangeSet<C> ranges) {
			return addAll(ranges.asRanges());
		}

		/**
		 * Add all of the specified ranges to this builder. Adjacent ranges are permitted
		 * and will be merged, but overlapping ranges will cause an exception when
		 * {@link #build()} is called.
		 * @throws IllegalArgumentException if any inserted ranges are empty
		 * @since 21.0
		 */
		@CanIgnoreReturnValue
		public Builder<C> addAll(Iterable<Range<C>> ranges) {
			for (Range<C> range : ranges) {
				add(range);
			}
			return this;
		}

		@CanIgnoreReturnValue
		Builder<C> combine(Builder<C> builder) {
			addAll(builder.ranges);
			return this;
		}

		/**
		 * Returns an {@code ImmutableRangeSet} containing the ranges added to this
		 * builder.
		 * @throws IllegalArgumentException if any input ranges have nonempty overlap
		 */
		public ImmutableRangeSet<C> build() {
			org.magneton.core.collect.ImmutableList.Builder<Range<C>> mergedRangesBuilder = new org.magneton.core.collect.ImmutableList.Builder<>(ranges.size());
			Collections.sort(ranges, Range.<C>rangeLexOrdering());
			PeekingIterator<Range<C>> peekingItr = org.magneton.core.collect.Iterators
					.peekingIterator(ranges.iterator());
			while (peekingItr.hasNext()) {
				Range<C> range = peekingItr.next();
				while (peekingItr.hasNext()) {
					Range<C> nextRange = peekingItr.peek();
					if (range.isConnected(nextRange)) {
						Preconditions.checkArgument(range.intersection(nextRange).isEmpty(),
								"Overlapping ranges not permitted but found %s overlapping %s", range, nextRange);
						range = range.span(peekingItr.next());
					}
					else {
						break;
					}
				}
				mergedRangesBuilder.add(range);
			}
			org.magneton.core.collect.ImmutableList<Range<C>> mergedRanges = mergedRangesBuilder.build();
			if (mergedRanges.isEmpty()) {
				return of();
			}
			else if (mergedRanges.size() == 1
					&& org.magneton.core.collect.Iterables.getOnlyElement(mergedRanges).equals(Range.all())) {
				return all();
			}
			else {
				return new ImmutableRangeSet<C>(mergedRanges);
			}
		}

	}

	private static final class SerializedForm<C extends Comparable> implements Serializable {

		private final org.magneton.core.collect.ImmutableList<Range<C>> ranges;

		SerializedForm(org.magneton.core.collect.ImmutableList<Range<C>> ranges) {
			this.ranges = ranges;
		}

		Object readResolve() {
			if (ranges.isEmpty()) {
				return of();
			}
			else if (ranges.equals(org.magneton.core.collect.ImmutableList.of(Range.all()))) {
				return all();
			}
			else {
				return new ImmutableRangeSet<C>(ranges);
			}
		}

	}

	private final class ComplementRanges extends ImmutableList<Range<C>> {

		// True if the "positive" range set is empty or bounded below.
		private final boolean positiveBoundedBelow;

		// True if the "positive" range set is empty or bounded above.
		private final boolean positiveBoundedAbove;

		private final int size;

		ComplementRanges() {
			positiveBoundedBelow = ranges.get(0).hasLowerBound();
			positiveBoundedAbove = Iterables.getLast(ranges).hasUpperBound();

			int size = ranges.size() - 1;
			if (positiveBoundedBelow) {
				size++;
			}
			if (positiveBoundedAbove) {
				size++;
			}
			this.size = size;
		}

		@Override
		public int size() {
			return size;
		}

		@Override
		public Range<C> get(int index) {
			Preconditions.checkElementIndex(index, size);

			Cut<C> lowerBound;
			if (positiveBoundedBelow) {
				lowerBound = (index == 0) ? Cut.<C>belowAll() : ranges.get(index - 1).upperBound;
			}
			else {
				lowerBound = ranges.get(index).upperBound;
			}

			Cut<C> upperBound;
			if (positiveBoundedAbove && index == size - 1) {
				upperBound = Cut.<C>aboveAll();
			}
			else {
				upperBound = ranges.get(index + (positiveBoundedBelow ? 0 : 1)).lowerBound;
			}

			return Range.create(lowerBound, upperBound);
		}

		@Override
		boolean isPartialView() {
			return true;
		}

	}

	private final class AsSet extends ImmutableSortedSet<C> {

		private final org.magneton.core.collect.DiscreteDomain<C> domain;

		@CheckForNull
		private transient Integer size;

		AsSet(DiscreteDomain<C> domain) {
			super(Ordering.natural());
			this.domain = domain;
		}

		@Override
		public int size() {
			// racy single-check idiom
			Integer result = size;
			if (result == null) {
				long total = 0;
				for (Range<C> range : ranges) {
					total += org.magneton.core.collect.ContiguousSet.create(range, domain).size();
					if (total >= Integer.MAX_VALUE) {
						break;
					}
				}
				result = size = org.magneton.core.primitives.Ints.saturatedCast(total);
			}
			return result.intValue();
		}

		@Override
		public UnmodifiableIterator<C> iterator() {
			return new org.magneton.core.collect.AbstractIterator<C>() {
				final Iterator<Range<C>> rangeItr = ranges.iterator();

				Iterator<C> elemItr = org.magneton.core.collect.Iterators.emptyIterator();

				@Override
				@CheckForNull
				protected C computeNext() {
					while (!elemItr.hasNext()) {
						if (rangeItr.hasNext()) {
							elemItr = org.magneton.core.collect.ContiguousSet.create(rangeItr.next(), domain)
									.iterator();
						}
						else {
							return endOfData();
						}
					}
					return elemItr.next();
				}
			};
		}

		@Override

		public UnmodifiableIterator<C> descendingIterator() {
			return new AbstractIterator<C>() {
				final Iterator<Range<C>> rangeItr = ranges.reverse().iterator();

				Iterator<C> elemItr = Iterators.emptyIterator();

				@Override
				@CheckForNull
				protected C computeNext() {
					while (!elemItr.hasNext()) {
						if (rangeItr.hasNext()) {
							elemItr = org.magneton.core.collect.ContiguousSet.create(rangeItr.next(), domain)
									.descendingIterator();
						}
						else {
							return endOfData();
						}
					}
					return elemItr.next();
				}
			};
		}

		ImmutableSortedSet<C> subSet(Range<C> range) {
			return subRangeSet(range).asSet(domain);
		}

		@Override
		ImmutableSortedSet<C> headSetImpl(C toElement, boolean inclusive) {
			return subSet(Range.upTo(toElement, org.magneton.core.collect.BoundType.forBoolean(inclusive)));
		}

		@Override
		ImmutableSortedSet<C> subSetImpl(C fromElement, boolean fromInclusive, C toElement, boolean toInclusive) {
			if (!fromInclusive && !toInclusive && Range.compareOrThrow(fromElement, toElement) == 0) {
				return ImmutableSortedSet.of();
			}
			return subSet(Range.range(fromElement, org.magneton.core.collect.BoundType.forBoolean(fromInclusive),
					toElement, org.magneton.core.collect.BoundType.forBoolean(toInclusive)));
		}

		@Override
		ImmutableSortedSet<C> tailSetImpl(C fromElement, boolean inclusive) {
			return subSet(Range.downTo(fromElement, BoundType.forBoolean(inclusive)));
		}

		@Override
		public boolean contains(@CheckForNull Object o) {
			if (o == null) {
				return false;
			}
			try {
				// we catch CCE's
				C c = (C) o;
				return ImmutableRangeSet.this.contains(c);
			}
			catch (ClassCastException e) {
				return false;
			}
		}

		@Override
		int indexOf(@CheckForNull Object target) {
			if (contains(target)) {
				// if it's contained, it's definitely a C
				C c = (C) requireNonNull(target);
				long total = 0;
				for (Range<C> range : ranges) {
					if (range.contains(c)) {
						return Ints.saturatedCast(
								total + org.magneton.core.collect.ContiguousSet.create(range, domain).indexOf(c));
					}
					else {
						total += ContiguousSet.create(range, domain).size();
					}
				}
				throw new AssertionError("impossible");
			}
			return -1;
		}

		@Override
		ImmutableSortedSet<C> createDescendingSet() {
			return new DescendingImmutableSortedSet<C>(this);
		}

		@Override
		boolean isPartialView() {
			return ranges.isPartialView();
		}

		@Override
		public String toString() {
			return ranges.toString();
		}

		@Override
		Object writeReplace() {
			return new AsSetSerializedForm<C>(ranges, domain);
		}

	}

}