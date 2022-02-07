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
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.NoSuchElementException;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collector;

import javax.annotation.CanIgnoreReturnValue;
import javax.annotation.CheckForNull;
import javax.annotation.DoNotCall;
import javax.annotation.DoNotMock;

import org.magneton.core.base.Preconditions;
import org.magneton.core.collect.SortedLists.KeyAbsentBehavior;
import org.magneton.core.collect.SortedLists.KeyPresentBehavior;

/**
 * A {@link org.magneton.core.collect.RangeMap} whose contents will never change, with
 * many other important properties detailed at {@link ImmutableCollection}.
 *
 * @author Louis Wasserman
 * @since 14.0
 */

@ElementTypesAreNonnullByDefault
public class ImmutableRangeMap<K extends Comparable<?>, V>
		implements org.magneton.core.collect.RangeMap<K, V>, Serializable {

	private static final ImmutableRangeMap<Comparable<?>, Object> EMPTY = new ImmutableRangeMap<>(
			org.magneton.core.collect.ImmutableList.<Range<Comparable<?>>>of(),
			org.magneton.core.collect.ImmutableList.of());

	private static final long serialVersionUID = 0;

	private final transient org.magneton.core.collect.ImmutableList<Range<K>> ranges;

	private final transient org.magneton.core.collect.ImmutableList<V> values;

	ImmutableRangeMap(org.magneton.core.collect.ImmutableList<Range<K>> ranges,
			org.magneton.core.collect.ImmutableList<V> values) {
		this.ranges = ranges;
		this.values = values;
	}

	/**
	 * Returns a {@code Collector} that accumulates the input elements into a new {@code
	 * ImmutableRangeMap}. As in {@link Builder}, overlapping ranges are not permitted.
	 *
	 * @since 23.1
	 */
	public static <T extends Object, K extends Comparable<? super K>, V> Collector<T, ?, ImmutableRangeMap<K, V>> toImmutableRangeMap(
			Function<? super T, Range<K>> keyFunction, Function<? super T, ? extends V> valueFunction) {
		return CollectCollectors.toImmutableRangeMap(keyFunction, valueFunction);
	}

	/**
	 * Returns an empty immutable range map.
	 *
	 * <p>
	 * <b>Performance note:</b> the instance returned is a singleton.
	 */
	public static <K extends Comparable<?>, V> ImmutableRangeMap<K, V> of() {
		return (ImmutableRangeMap<K, V>) EMPTY;
	}

	/** Returns an immutable range map mapping a single range to a single value. */
	public static <K extends Comparable<?>, V> ImmutableRangeMap<K, V> of(Range<K> range, V value) {
		return new ImmutableRangeMap<>(org.magneton.core.collect.ImmutableList.of(range),
				org.magneton.core.collect.ImmutableList.of(value));
	}

	public static <K extends Comparable<?>, V> ImmutableRangeMap<K, V> copyOf(
			org.magneton.core.collect.RangeMap<K, ? extends V> rangeMap) {
		if (rangeMap instanceof ImmutableRangeMap) {
			return (ImmutableRangeMap<K, V>) rangeMap;
		}
		Map<Range<K>, ? extends V> map = rangeMap.asMapOfRanges();
		org.magneton.core.collect.ImmutableList.Builder<Range<K>> rangesBuilder = new org.magneton.core.collect.ImmutableList.Builder<>(
				map.size());
		org.magneton.core.collect.ImmutableList.Builder<V> valuesBuilder = new org.magneton.core.collect.ImmutableList.Builder<V>(
				map.size());
		for (Entry<Range<K>, ? extends V> entry : map.entrySet()) {
			rangesBuilder.add(entry.getKey());
			valuesBuilder.add(entry.getValue());
		}
		return new ImmutableRangeMap<>(rangesBuilder.build(), valuesBuilder.build());
	}

	/** Returns a new builder for an immutable range map. */
	public static <K extends Comparable<?>, V> Builder<K, V> builder() {
		return new Builder<>();
	}

	@Override
	@CheckForNull
	public V get(K key) {
		int index = org.magneton.core.collect.SortedLists.binarySearch(ranges, Range.<K>lowerBoundFn(),
				Cut.belowValue(key), KeyPresentBehavior.ANY_PRESENT, KeyAbsentBehavior.NEXT_LOWER);
		if (index == -1) {
			return null;
		}
		else {
			Range<K> range = ranges.get(index);
			return range.contains(key) ? values.get(index) : null;
		}
	}

	@Override
	@CheckForNull
	public Entry<Range<K>, V> getEntry(K key) {
		int index = org.magneton.core.collect.SortedLists.binarySearch(ranges, Range.<K>lowerBoundFn(),
				Cut.belowValue(key), KeyPresentBehavior.ANY_PRESENT, KeyAbsentBehavior.NEXT_LOWER);
		if (index == -1) {
			return null;
		}
		else {
			Range<K> range = ranges.get(index);
			return range.contains(key) ? Maps.immutableEntry(range, values.get(index)) : null;
		}
	}

	@Override
	public Range<K> span() {
		if (ranges.isEmpty()) {
			throw new NoSuchElementException();
		}
		Range<K> firstRange = ranges.get(0);
		Range<K> lastRange = ranges.get(ranges.size() - 1);
		return Range.create(firstRange.lowerBound, lastRange.upperBound);
	}

	/**
	 * Guaranteed to throw an exception and leave the {@code RangeMap} unmodified.
	 * @throws UnsupportedOperationException always
	 * @deprecated Unsupported operation.
	 */
	@Deprecated
	@Override
	@DoNotCall("Always throws UnsupportedOperationException")
	public final void put(Range<K> range, V value) {
		throw new UnsupportedOperationException();
	}

	/**
	 * Guaranteed to throw an exception and leave the {@code RangeMap} unmodified.
	 * @throws UnsupportedOperationException always
	 * @deprecated Unsupported operation.
	 */
	@Deprecated
	@Override
	@DoNotCall("Always throws UnsupportedOperationException")
	public final void putCoalescing(Range<K> range, V value) {
		throw new UnsupportedOperationException();
	}

	/**
	 * Guaranteed to throw an exception and leave the {@code RangeMap} unmodified.
	 * @throws UnsupportedOperationException always
	 * @deprecated Unsupported operation.
	 */
	@Deprecated
	@Override
	@DoNotCall("Always throws UnsupportedOperationException")
	public final void putAll(org.magneton.core.collect.RangeMap<K, V> rangeMap) {
		throw new UnsupportedOperationException();
	}

	/**
	 * Guaranteed to throw an exception and leave the {@code RangeMap} unmodified.
	 * @throws UnsupportedOperationException always
	 * @deprecated Unsupported operation.
	 */
	@Deprecated
	@Override
	@DoNotCall("Always throws UnsupportedOperationException")
	public final void clear() {
		throw new UnsupportedOperationException();
	}

	/**
	 * Guaranteed to throw an exception and leave the {@code RangeMap} unmodified.
	 * @throws UnsupportedOperationException always
	 * @deprecated Unsupported operation.
	 */
	@Deprecated
	@Override
	@DoNotCall("Always throws UnsupportedOperationException")
	public final void remove(Range<K> range) {
		throw new UnsupportedOperationException();
	}

	/**
	 * Guaranteed to throw an exception and leave the {@code RangeMap} unmodified.
	 * @throws UnsupportedOperationException always
	 * @deprecated Unsupported operation.
	 */
	@Deprecated
	@Override
	@DoNotCall("Always throws UnsupportedOperationException")
	public final void merge(Range<K> range, @CheckForNull V value,
			BiFunction<? super V, ? super @Nullable V, ? extends V> remappingFunction) {
		throw new UnsupportedOperationException();
	}

	@Override
	public org.magneton.core.collect.ImmutableMap<Range<K>, V> asMapOfRanges() {
		if (ranges.isEmpty()) {
			return org.magneton.core.collect.ImmutableMap.of();
		}
		RegularImmutableSortedSet<Range<K>> rangeSet = new RegularImmutableSortedSet<>(ranges,
				Range.<K>rangeLexOrdering());
		return new ImmutableSortedMap<>(rangeSet, values);
	}

	@Override
	public org.magneton.core.collect.ImmutableMap<Range<K>, V> asDescendingMapOfRanges() {
		if (ranges.isEmpty()) {
			return org.magneton.core.collect.ImmutableMap.of();
		}
		RegularImmutableSortedSet<Range<K>> rangeSet = new RegularImmutableSortedSet<>(ranges.reverse(),
				Range.<K>rangeLexOrdering().reverse());
		return new ImmutableSortedMap<>(rangeSet, values.reverse());
	}

	@Override
	public ImmutableRangeMap<K, V> subRangeMap(Range<K> range) {
		if (Preconditions.checkNotNull(range).isEmpty()) {
			return ImmutableRangeMap.of();
		}
		else if (ranges.isEmpty() || range.encloses(span())) {
			return this;
		}
		int lowerIndex = org.magneton.core.collect.SortedLists.binarySearch(ranges, Range.<K>upperBoundFn(),
				range.lowerBound, KeyPresentBehavior.FIRST_AFTER, KeyAbsentBehavior.NEXT_HIGHER);
		int upperIndex = org.magneton.core.collect.SortedLists.binarySearch(ranges, Range.<K>lowerBoundFn(),
				range.upperBound, KeyPresentBehavior.ANY_PRESENT, KeyAbsentBehavior.NEXT_HIGHER);
		if (lowerIndex >= upperIndex) {
			return ImmutableRangeMap.of();
		}
		int off = lowerIndex;
		int len = upperIndex - lowerIndex;
		org.magneton.core.collect.ImmutableList<Range<K>> subRanges = new ImmutableList<Range<K>>() {
			@Override
			public int size() {
				return len;
			}

			@Override
			public Range<K> get(int index) {
				Preconditions.checkElementIndex(index, len);
				if (index == 0 || index == len - 1) {
					return ranges.get(index + off).intersection(range);
				}
				else {
					return ranges.get(index + off);
				}
			}

			@Override
			boolean isPartialView() {
				return true;
			}
		};
		ImmutableRangeMap<K, V> outer = this;
		return new ImmutableRangeMap<K, V>(subRanges, values.subList(lowerIndex, upperIndex)) {
			@Override
			public ImmutableRangeMap<K, V> subRangeMap(Range<K> subRange) {
				if (range.isConnected(subRange)) {
					return outer.subRangeMap(subRange.intersection(range));
				}
				else {
					return ImmutableRangeMap.of();
				}
			}
		};
	}

	@Override
	public int hashCode() {
		return asMapOfRanges().hashCode();
	}

	@Override
	public boolean equals(@CheckForNull Object o) {
		if (o instanceof org.magneton.core.collect.RangeMap) {
			org.magneton.core.collect.RangeMap<?, ?> rangeMap = (RangeMap<?, ?>) o;
			return asMapOfRanges().equals(rangeMap.asMapOfRanges());
		}
		return false;
	}

	@Override
	public String toString() {
		return asMapOfRanges().toString();
	}

	Object writeReplace() {
		return new SerializedForm<>(asMapOfRanges());
	}

	/**
	 * A builder for immutable range maps. Overlapping ranges are prohibited.
	 *
	 * @since 14.0
	 */
	@DoNotMock
	public static final class Builder<K extends Comparable<?>, V> {

		private final List<Entry<Range<K>, V>> entries;

		public Builder() {
			entries = Lists.newArrayList();
		}

		/**
		 * Associates the specified range with the specified value.
		 * @throws IllegalArgumentException if {@code range} is empty
		 */
		@CanIgnoreReturnValue
		public Builder<K, V> put(Range<K> range, V value) {
			Preconditions.checkNotNull(range);
			Preconditions.checkNotNull(value);
			Preconditions.checkArgument(!range.isEmpty(), "Range must not be empty, but was %s", range);
			entries.add(Maps.immutableEntry(range, value));
			return this;
		}

		/** Copies all associations from the specified range map into this builder. */
		@CanIgnoreReturnValue
		public Builder<K, V> putAll(org.magneton.core.collect.RangeMap<K, ? extends V> rangeMap) {
			for (Entry<Range<K>, ? extends V> entry : rangeMap.asMapOfRanges().entrySet()) {
				put(entry.getKey(), entry.getValue());
			}
			return this;
		}

		@CanIgnoreReturnValue
		Builder<K, V> combine(Builder<K, V> builder) {
			entries.addAll(builder.entries);
			return this;
		}

		/**
		 * Returns an {@code ImmutableRangeMap} containing the associations previously
		 * added to this builder.
		 * @throws IllegalArgumentException if any two ranges inserted into this builder
		 * overlap
		 */
		public ImmutableRangeMap<K, V> build() {
			Collections.sort(entries, Range.<K>rangeLexOrdering().onKeys());
			org.magneton.core.collect.ImmutableList.Builder<Range<K>> rangesBuilder = new org.magneton.core.collect.ImmutableList.Builder<>(entries.size());
			org.magneton.core.collect.ImmutableList.Builder<V> valuesBuilder = new org.magneton.core.collect.ImmutableList.Builder<V>(entries.size());
			for (int i = 0; i < entries.size(); i++) {
				Range<K> range = entries.get(i).getKey();
				if (i > 0) {
					Range<K> prevRange = entries.get(i - 1).getKey();
					if (range.isConnected(prevRange) && !range.intersection(prevRange).isEmpty()) {
						throw new IllegalArgumentException(
								"Overlapping ranges: range " + prevRange + " overlaps with entry " + range);
					}
				}
				rangesBuilder.add(range);
				valuesBuilder.add(entries.get(i).getValue());
			}
			return new ImmutableRangeMap<>(rangesBuilder.build(), valuesBuilder.build());
		}

	}

	/**
	 * This class is used to serialize ImmutableRangeMap instances. Serializes the
	 * {@link #asMapOfRanges()} form.
	 */
	private static class SerializedForm<K extends Comparable<?>, V> implements Serializable {

		private static final long serialVersionUID = 0;

		private final org.magneton.core.collect.ImmutableMap<Range<K>, V> mapOfRanges;

		SerializedForm(ImmutableMap<Range<K>, V> mapOfRanges) {
			this.mapOfRanges = mapOfRanges;
		}

		Object readResolve() {
			if (mapOfRanges.isEmpty()) {
				return of();
			}
			else {
				return createRangeMap();
			}
		}

		Object createRangeMap() {
			Builder<K, V> builder = new Builder<>();
			for (Entry<Range<K>, V> entry : mapOfRanges.entrySet()) {
				builder.put(entry.getKey(), entry.getValue());
			}
			return builder.build();
		}

	}

}
