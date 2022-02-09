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

import java.util.AbstractMap;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.NavigableMap;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.function.BiFunction;

import javax.annotation.CheckForNull;

import org.magneton.core.base.MoreObjects;
import org.magneton.core.base.Preconditions;
import org.magneton.core.base.Predicate;
import org.magneton.core.base.Predicates;

import static java.util.Objects.requireNonNull;

/**
 * An implementation of {@code RangeMap} based on a {@code TreeMap}, supporting all
 * optional operations.
 *
 * <p>
 * Like all {@code RangeMap} implementations, this supports neither null keys nor null
 * values.
 *
 * @author Louis Wasserman
 * @since 14.0
 */

@ElementTypesAreNonnullByDefault
public final class TreeRangeMap<K extends Comparable, V> implements org.magneton.core.collect.RangeMap<K, V> {

	// This RangeMap is immutable.
	private static final org.magneton.core.collect.RangeMap<Comparable<?>, Object> EMPTY_SUB_RANGE_MAP = new org.magneton.core.collect.RangeMap<Comparable<?>, Object>() {
		@Override
		@CheckForNull
		public Object get(Comparable<?> key) {
			return null;
		}

		@Override
		@CheckForNull
		public Entry<Range<Comparable<?>>, Object> getEntry(Comparable<?> key) {
			return null;
		}

		@Override
		public Range<Comparable<?>> span() {
			throw new NoSuchElementException();
		}

		@Override
		public void put(Range<Comparable<?>> range, Object value) {
			Preconditions.checkNotNull(range);
			throw new IllegalArgumentException("Cannot insert range " + range + " into an empty subRangeMap");
		}

		@Override
		public void putCoalescing(Range<Comparable<?>> range, Object value) {
			Preconditions.checkNotNull(range);
			throw new IllegalArgumentException("Cannot insert range " + range + " into an empty subRangeMap");
		}

		@Override
		public void putAll(org.magneton.core.collect.RangeMap<Comparable<?>, Object> rangeMap) {
			if (!rangeMap.asMapOfRanges().isEmpty()) {
				throw new IllegalArgumentException("Cannot putAll(nonEmptyRangeMap) into an empty subRangeMap");
			}
		}

		@Override
		public void clear() {
		}

		@Override
		public void remove(Range<Comparable<?>> range) {
			Preconditions.checkNotNull(range);
		}

		@Override
		public void merge(Range<Comparable<?>> range, @CheckForNull Object value,
				BiFunction<? super Object, ? super Object, ? extends Object> remappingFunction) {
			Preconditions.checkNotNull(range);
			throw new IllegalArgumentException("Cannot merge range " + range + " into an empty subRangeMap");
		}

		@Override
		public Map<Range<Comparable<?>>, Object> asMapOfRanges() {
			return Collections.emptyMap();
		}

		@Override
		public Map<Range<Comparable<?>>, Object> asDescendingMapOfRanges() {
			return Collections.emptyMap();
		}

		@Override
		public org.magneton.core.collect.RangeMap<Comparable<?>, Object> subRangeMap(Range<Comparable<?>> range) {
			Preconditions.checkNotNull(range);
			return this;
		}
	};

	private final NavigableMap<Cut<K>, RangeMapEntry<K, V>> entriesByLowerBound;

	private TreeRangeMap() {
		entriesByLowerBound = Maps.newTreeMap();
	}

	public static <K extends Comparable, V> TreeRangeMap<K, V> create() {
		return new TreeRangeMap<>();
	}

	/**
	 * Returns the range that spans the given range and entry, if the entry can be
	 * coalesced.
	 */
	private static <K extends Comparable, V> Range<K> coalesce(Range<K> range, V value,
			@CheckForNull Entry<Cut<K>, RangeMapEntry<K, V>> entry) {
		if (entry != null && entry.getValue().getKey().isConnected(range)
				&& entry.getValue().getValue().equals(value)) {
			return range.span(entry.getValue().getKey());
		}
		return range;
	}

	@Override
	@CheckForNull
	public V get(K key) {
		Entry<Range<K>, V> entry = getEntry(key);
		return (entry == null) ? null : entry.getValue();
	}

	@Override
	@CheckForNull
	public Entry<Range<K>, V> getEntry(K key) {
		Entry<Cut<K>, RangeMapEntry<K, V>> mapEntry = entriesByLowerBound.floorEntry(Cut.belowValue(key));
		if (mapEntry != null && mapEntry.getValue().contains(key)) {
			return mapEntry.getValue();
		}
		else {
			return null;
		}
	}

	@Override
	public void put(Range<K> range, V value) {
		if (!range.isEmpty()) {
			Preconditions.checkNotNull(value);
			remove(range);
			entriesByLowerBound.put(range.lowerBound, new RangeMapEntry<K, V>(range, value));
		}
	}

	@Override
	public void putCoalescing(Range<K> range, V value) {
		// don't short-circuit if the range is empty - it may be between two ranges we can
		// coalesce.
		if (entriesByLowerBound.isEmpty()) {
			put(range, value);
			return;
		}

		Range<K> coalescedRange = coalescedRange(range, Preconditions.checkNotNull(value));
		put(coalescedRange, value);
	}

	/**
	 * Computes the coalesced range for the given range+value - does not mutate the map.
	 */
	private Range<K> coalescedRange(Range<K> range, V value) {
		Range<K> coalescedRange = range;
		Entry<Cut<K>, RangeMapEntry<K, V>> lowerEntry = entriesByLowerBound.lowerEntry(range.lowerBound);
		coalescedRange = coalesce(coalescedRange, value, lowerEntry);

		Entry<Cut<K>, RangeMapEntry<K, V>> higherEntry = entriesByLowerBound.floorEntry(range.upperBound);
		coalescedRange = coalesce(coalescedRange, value, higherEntry);

		return coalescedRange;
	}

	@Override
	public void putAll(org.magneton.core.collect.RangeMap<K, V> rangeMap) {
		for (Entry<Range<K>, V> entry : rangeMap.asMapOfRanges().entrySet()) {
			put(entry.getKey(), entry.getValue());
		}
	}

	@Override
	public void clear() {
		entriesByLowerBound.clear();
	}

	@Override
	public Range<K> span() {
		Entry<Cut<K>, RangeMapEntry<K, V>> firstEntry = entriesByLowerBound.firstEntry();
		Entry<Cut<K>, RangeMapEntry<K, V>> lastEntry = entriesByLowerBound.lastEntry();
		// Either both are null or neither is, but we check both to satisfy the nullness
		// checker.
		if (firstEntry == null || lastEntry == null) {
			throw new NoSuchElementException();
		}
		return Range.create(firstEntry.getValue().getKey().lowerBound, lastEntry.getValue().getKey().upperBound);
	}

	private void putRangeMapEntry(Cut<K> lowerBound, Cut<K> upperBound, V value) {
		entriesByLowerBound.put(lowerBound, new RangeMapEntry<K, V>(lowerBound, upperBound, value));
	}

	@Override
	public void remove(Range<K> rangeToRemove) {
		if (rangeToRemove.isEmpty()) {
			return;
		}

		/*
		 * The comments for this method will use [ ] to indicate the bounds of
		 * rangeToRemove and ( ) to indicate the bounds of ranges in the range map.
		 */
		Entry<Cut<K>, RangeMapEntry<K, V>> mapEntryBelowToTruncate = entriesByLowerBound
				.lowerEntry(rangeToRemove.lowerBound);
		if (mapEntryBelowToTruncate != null) {
			// we know ( [
			RangeMapEntry<K, V> rangeMapEntry = mapEntryBelowToTruncate.getValue();
			if (rangeMapEntry.getUpperBound().compareTo(rangeToRemove.lowerBound) > 0) {
				// we know ( [ )
				if (rangeMapEntry.getUpperBound().compareTo(rangeToRemove.upperBound) > 0) {
					// we know ( [ ] ), so insert the range ] ) back into the map --
					// it's being split apart
					putRangeMapEntry(rangeToRemove.upperBound, rangeMapEntry.getUpperBound(),
							mapEntryBelowToTruncate.getValue().getValue());
				}
				// overwrite mapEntryToTruncateBelow with a truncated range
				putRangeMapEntry(rangeMapEntry.getLowerBound(), rangeToRemove.lowerBound,
						mapEntryBelowToTruncate.getValue().getValue());
			}
		}

		Entry<Cut<K>, RangeMapEntry<K, V>> mapEntryAboveToTruncate = entriesByLowerBound
				.lowerEntry(rangeToRemove.upperBound);
		if (mapEntryAboveToTruncate != null) {
			// we know ( ]
			RangeMapEntry<K, V> rangeMapEntry = mapEntryAboveToTruncate.getValue();
			if (rangeMapEntry.getUpperBound().compareTo(rangeToRemove.upperBound) > 0) {
				// we know ( ] ), and since we dealt with truncating below already,
				// we know [ ( ] )
				putRangeMapEntry(rangeToRemove.upperBound, rangeMapEntry.getUpperBound(),
						mapEntryAboveToTruncate.getValue().getValue());
			}
		}
		entriesByLowerBound.subMap(rangeToRemove.lowerBound, rangeToRemove.upperBound).clear();
	}

	private void split(Cut<K> cut) {
		/*
		 * The comments for this method will use | to indicate the cut point and ( ) to
		 * indicate the bounds of ranges in the range map.
		 */
		Entry<Cut<K>, RangeMapEntry<K, V>> mapEntryToSplit = entriesByLowerBound.lowerEntry(cut);
		if (mapEntryToSplit == null) {
			return;
		}
		// we know ( |
		RangeMapEntry<K, V> rangeMapEntry = mapEntryToSplit.getValue();
		if (rangeMapEntry.getUpperBound().compareTo(cut) <= 0) {
			return;
		}
		// we know ( | )
		putRangeMapEntry(rangeMapEntry.getLowerBound(), cut, rangeMapEntry.getValue());
		putRangeMapEntry(cut, rangeMapEntry.getUpperBound(), rangeMapEntry.getValue());
	}

	@Override
	public void merge(Range<K> range, @CheckForNull V value,
			BiFunction<? super V, ? super V, ? extends V> remappingFunction) {
		Preconditions.checkNotNull(range);
		Preconditions.checkNotNull(remappingFunction);

		if (range.isEmpty()) {
			return;
		}
		split(range.lowerBound);
		split(range.upperBound);

		// Due to the splitting of any entries spanning the range bounds, we know that any
		// entry with a
		// lower bound in the merge range is entirely contained by the merge range.
		Set<Entry<Cut<K>, RangeMapEntry<K, V>>> entriesInMergeRange = entriesByLowerBound
				.subMap(range.lowerBound, range.upperBound).entrySet();

		// Create entries mapping any unmapped ranges in the merge range to the specified
		// value.
		ImmutableMap.Builder<Cut<K>, RangeMapEntry<K, V>> gaps = ImmutableMap.builder();
		if (value != null) {
			Iterator<Entry<Cut<K>, RangeMapEntry<K, V>>> backingItr = entriesInMergeRange.iterator();
			Cut<K> lowerBound = range.lowerBound;
			while (backingItr.hasNext()) {
				RangeMapEntry<K, V> entry = backingItr.next().getValue();
				Cut<K> upperBound = entry.getLowerBound();
				if (!lowerBound.equals(upperBound)) {
					gaps.put(lowerBound, new RangeMapEntry<K, V>(lowerBound, upperBound, value));
				}
				lowerBound = entry.getUpperBound();
			}
			if (!lowerBound.equals(range.upperBound)) {
				gaps.put(lowerBound, new RangeMapEntry<K, V>(lowerBound, range.upperBound, value));
			}
		}

		// Remap all existing entries in the merge range.
		Iterator<Entry<Cut<K>, RangeMapEntry<K, V>>> backingItr = entriesInMergeRange.iterator();
		while (backingItr.hasNext()) {
			Entry<Cut<K>, RangeMapEntry<K, V>> entry = backingItr.next();
			V newValue = remappingFunction.apply(entry.getValue().getValue(), value);
			if (newValue == null) {
				backingItr.remove();
			}
			else {
				entry.setValue(new RangeMapEntry<K, V>(entry.getValue().getLowerBound(),
						entry.getValue().getUpperBound(), newValue));
			}
		}

		entriesByLowerBound.putAll(gaps.build());
	}

	@Override
	public Map<Range<K>, V> asMapOfRanges() {
		return new AsMapOfRanges(entriesByLowerBound.values());
	}

	@Override
	public Map<Range<K>, V> asDescendingMapOfRanges() {
		return new AsMapOfRanges(entriesByLowerBound.descendingMap().values());
	}

	@Override
	public org.magneton.core.collect.RangeMap<K, V> subRangeMap(Range<K> subRange) {
		if (subRange.equals(Range.all())) {
			return this;
		}
		else {
			return new SubRangeMap(subRange);
		}
	}

	private org.magneton.core.collect.RangeMap<K, V> emptySubRangeMap() {
		return (org.magneton.core.collect.RangeMap<K, V>) (org.magneton.core.collect.RangeMap<?, ?>) EMPTY_SUB_RANGE_MAP;
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
	public int hashCode() {
		return asMapOfRanges().hashCode();
	}

	@Override
	public String toString() {
		return entriesByLowerBound.values().toString();
	}

	private static final class RangeMapEntry<K extends Comparable, V> extends AbstractMapEntry<Range<K>, V> {

		private final Range<K> range;

		private final V value;

		RangeMapEntry(Cut<K> lowerBound, Cut<K> upperBound, V value) {
			this(Range.create(lowerBound, upperBound), value);
		}

		RangeMapEntry(Range<K> range, V value) {
			this.range = range;
			this.value = value;
		}

		@Override
		public Range<K> getKey() {
			return range;
		}

		@Override
		public V getValue() {
			return value;
		}

		public boolean contains(K value) {
			return range.contains(value);
		}

		Cut<K> getLowerBound() {
			return range.lowerBound;
		}

		Cut<K> getUpperBound() {
			return range.upperBound;
		}

	}

	private final class AsMapOfRanges extends Maps.IteratorBasedAbstractMap<Range<K>, V> {

		final Iterable<Entry<Range<K>, V>> entryIterable;

		// it's safe to upcast iterables
		AsMapOfRanges(Iterable<RangeMapEntry<K, V>> entryIterable) {
			this.entryIterable = (Iterable) entryIterable;
		}

		@Override
		public boolean containsKey(@CheckForNull Object key) {
			return get(key) != null;
		}

		@Override
		@CheckForNull
		public V get(@CheckForNull Object key) {
			if (key instanceof Range) {
				Range<?> range = (Range<?>) key;
				RangeMapEntry<K, V> rangeMapEntry = entriesByLowerBound.get(range.lowerBound);
				if (rangeMapEntry != null && rangeMapEntry.getKey().equals(range)) {
					return rangeMapEntry.getValue();
				}
			}
			return null;
		}

		@Override
		public int size() {
			return entriesByLowerBound.size();
		}

		@Override
		Iterator<Entry<Range<K>, V>> entryIterator() {
			return entryIterable.iterator();
		}

	}

	private class SubRangeMap implements org.magneton.core.collect.RangeMap<K, V> {

		private final Range<K> subRange;

		SubRangeMap(Range<K> subRange) {
			this.subRange = subRange;
		}

		@Override
		@CheckForNull
		public V get(K key) {
			return subRange.contains(key) ? TreeRangeMap.this.get(key) : null;
		}

		@Override
		@CheckForNull
		public Entry<Range<K>, V> getEntry(K key) {
			if (subRange.contains(key)) {
				Entry<Range<K>, V> entry = TreeRangeMap.this.getEntry(key);
				if (entry != null) {
					return Maps.immutableEntry(entry.getKey().intersection(subRange), entry.getValue());
				}
			}
			return null;
		}

		@Override
		public Range<K> span() {
			Cut<K> lowerBound;
			Entry<Cut<K>, RangeMapEntry<K, V>> lowerEntry = entriesByLowerBound.floorEntry(subRange.lowerBound);
			if (lowerEntry != null && lowerEntry.getValue().getUpperBound().compareTo(subRange.lowerBound) > 0) {
				lowerBound = subRange.lowerBound;
			}
			else {
				lowerBound = entriesByLowerBound.ceilingKey(subRange.lowerBound);
				if (lowerBound == null || lowerBound.compareTo(subRange.upperBound) >= 0) {
					throw new NoSuchElementException();
				}
			}

			Cut<K> upperBound;
			Entry<Cut<K>, RangeMapEntry<K, V>> upperEntry = entriesByLowerBound.lowerEntry(subRange.upperBound);
			if (upperEntry == null) {
				throw new NoSuchElementException();
			}
			else if (upperEntry.getValue().getUpperBound().compareTo(subRange.upperBound) >= 0) {
				upperBound = subRange.upperBound;
			}
			else {
				upperBound = upperEntry.getValue().getUpperBound();
			}
			return Range.create(lowerBound, upperBound);
		}

		@Override
		public void put(Range<K> range, V value) {
			Preconditions.checkArgument(subRange.encloses(range), "Cannot put range %s into a subRangeMap(%s)", range, subRange);
			TreeRangeMap.this.put(range, value);
		}

		@Override
		public void putCoalescing(Range<K> range, V value) {
			if (entriesByLowerBound.isEmpty() || !subRange.encloses(range)) {
				put(range, value);
				return;
			}

			Range<K> coalescedRange = coalescedRange(range, Preconditions.checkNotNull(value));
			// only coalesce ranges within the subRange
			put(coalescedRange.intersection(subRange), value);
		}

		@Override
		public void putAll(org.magneton.core.collect.RangeMap<K, V> rangeMap) {
			if (rangeMap.asMapOfRanges().isEmpty()) {
				return;
			}
			Range<K> span = rangeMap.span();
			Preconditions.checkArgument(subRange.encloses(span),
					"Cannot putAll rangeMap with span %s into a subRangeMap(%s)", span, subRange);
			TreeRangeMap.this.putAll(rangeMap);
		}

		@Override
		public void clear() {
			TreeRangeMap.this.remove(subRange);
		}

		@Override
		public void remove(Range<K> range) {
			if (range.isConnected(subRange)) {
				TreeRangeMap.this.remove(range.intersection(subRange));
			}
		}

		@Override
		public void merge(Range<K> range, @CheckForNull V value,
				BiFunction<? super V, ? super V, ? extends V> remappingFunction) {
			Preconditions.checkArgument(subRange.encloses(range), "Cannot merge range %s into a subRangeMap(%s)", range, subRange);
			TreeRangeMap.this.merge(range, value, remappingFunction);
		}

		@Override
		public org.magneton.core.collect.RangeMap<K, V> subRangeMap(Range<K> range) {
			if (!range.isConnected(subRange)) {
				return emptySubRangeMap();
			}
			else {
				return TreeRangeMap.this.subRangeMap(range.intersection(subRange));
			}
		}

		@Override
		public Map<Range<K>, V> asMapOfRanges() {
			return new SubRangeMapAsMap();
		}

		@Override
		public Map<Range<K>, V> asDescendingMapOfRanges() {
			return new SubRangeMapAsMap() {

				@Override
				Iterator<Entry<Range<K>, V>> entryIterator() {
					if (subRange.isEmpty()) {
						return org.magneton.core.collect.Iterators.emptyIterator();
					}
					Iterator<RangeMapEntry<K, V>> backingItr = entriesByLowerBound.headMap(subRange.upperBound, false)
							.descendingMap().values().iterator();
					return new AbstractIterator<Entry<Range<K>, V>>() {

						@Override
						@CheckForNull
						protected Entry<Range<K>, V> computeNext() {
							if (backingItr.hasNext()) {
								RangeMapEntry<K, V> entry = backingItr.next();
								if (entry.getUpperBound().compareTo(subRange.lowerBound) <= 0) {
									return endOfData();
								}
								return Maps.immutableEntry(entry.getKey().intersection(subRange), entry.getValue());
							}
							return endOfData();
						}
					};
				}
			};
		}

		@Override
		public boolean equals(@CheckForNull Object o) {
			if (o instanceof org.magneton.core.collect.RangeMap) {
				org.magneton.core.collect.RangeMap<?, ?> rangeMap = (org.magneton.core.collect.RangeMap<?, ?>) o;
				return asMapOfRanges().equals(rangeMap.asMapOfRanges());
			}
			return false;
		}

		@Override
		public int hashCode() {
			return asMapOfRanges().hashCode();
		}

		@Override
		public String toString() {
			return asMapOfRanges().toString();
		}

		class SubRangeMapAsMap extends AbstractMap<Range<K>, V> {

			@Override
			public boolean containsKey(@CheckForNull Object key) {
				return get(key) != null;
			}

			@Override
			@CheckForNull
			public V get(@CheckForNull Object key) {
				try {
					if (key instanceof Range) {
						// we catch ClassCastExceptions
						Range<K> r = (Range<K>) key;
						if (!subRange.encloses(r) || r.isEmpty()) {
							return null;
						}
						RangeMapEntry<K, V> candidate = null;
						if (r.lowerBound.compareTo(subRange.lowerBound) == 0) {
							// r could be truncated on the left
							Entry<Cut<K>, RangeMapEntry<K, V>> entry = entriesByLowerBound.floorEntry(r.lowerBound);
							if (entry != null) {
								candidate = entry.getValue();
							}
						}
						else {
							candidate = entriesByLowerBound.get(r.lowerBound);
						}

						if (candidate != null && candidate.getKey().isConnected(subRange)
								&& candidate.getKey().intersection(subRange).equals(r)) {
							return candidate.getValue();
						}
					}
				}
				catch (ClassCastException e) {
					return null;
				}
				return null;
			}

			@Override
			@CheckForNull
			public V remove(@CheckForNull Object key) {
				V value = get(key);
				if (value != null) {
					// it's definitely in the map, so the cast and requireNonNull are safe
					Range<K> range = (Range<K>) requireNonNull(key);
					TreeRangeMap.this.remove(range);
					return value;
				}
				return null;
			}

			@Override
			public void clear() {
				SubRangeMap.this.clear();
			}

			private boolean removeEntryIf(Predicate<? super Entry<Range<K>, V>> predicate) {
				List<Range<K>> toRemove = Lists.newArrayList();
				for (Entry<Range<K>, V> entry : entrySet()) {
					if (predicate.apply(entry)) {
						toRemove.add(entry.getKey());
					}
				}
				for (Range<K> range : toRemove) {
					TreeRangeMap.this.remove(range);
				}
				return !toRemove.isEmpty();
			}

			@Override
			public Set<Range<K>> keySet() {
				return new Maps.KeySet<Range<K>, V>(SubRangeMapAsMap.this) {
					@Override
					public boolean remove(@CheckForNull Object o) {
						return SubRangeMapAsMap.this.remove(o) != null;
					}

					@Override
					public boolean retainAll(Collection<?> c) {
						return removeEntryIf(
								Predicates.compose(Predicates.not(Predicates.in(c)), Maps.<Range<K>>keyFunction()));
					}
				};
			}

			@Override
			public Set<Entry<Range<K>, V>> entrySet() {
				return new Maps.EntrySet<Range<K>, V>() {
					@Override
					Map<Range<K>, V> map() {
						return SubRangeMapAsMap.this;
					}

					@Override
					public Iterator<Entry<Range<K>, V>> iterator() {
						return entryIterator();
					}

					@Override
					public boolean retainAll(Collection<?> c) {
						return removeEntryIf(Predicates.not(Predicates.in(c)));
					}

					@Override
					public int size() {
						return org.magneton.core.collect.Iterators.size(iterator());
					}

					@Override
					public boolean isEmpty() {
						return !iterator().hasNext();
					}
				};
			}

			Iterator<Entry<Range<K>, V>> entryIterator() {
				if (subRange.isEmpty()) {
					return Iterators.emptyIterator();
				}
				Cut<K> cutToStart = MoreObjects.firstNonNull(entriesByLowerBound.floorKey(subRange.lowerBound), subRange.lowerBound);
				Iterator<RangeMapEntry<K, V>> backingItr = entriesByLowerBound.tailMap(cutToStart, true).values()
						.iterator();
				return new AbstractIterator<Entry<Range<K>, V>>() {

					@Override
					@CheckForNull
					protected Entry<Range<K>, V> computeNext() {
						while (backingItr.hasNext()) {
							RangeMapEntry<K, V> entry = backingItr.next();
							if (entry.getLowerBound().compareTo(subRange.upperBound) >= 0) {
								return endOfData();
							}
							else if (entry.getUpperBound().compareTo(subRange.lowerBound) > 0) {
								// this might not be true e.g. at the start of the
								// iteration
								return Maps.immutableEntry(entry.getKey().intersection(subRange), entry.getValue());
							}
						}
						return endOfData();
					}
				};
			}

			@Override
			public Collection<V> values() {
				return new Maps.Values<Range<K>, V>(this) {
					@Override
					public boolean removeAll(Collection<?> c) {
						return removeEntryIf(Predicates.compose(Predicates.in(c), Maps.<V>valueFunction()));
					}

					@Override
					public boolean retainAll(Collection<?> c) {
						return removeEntryIf(
								Predicates.compose(Predicates.not(Predicates.in(c)), Maps.<V>valueFunction()));
					}
				};
			}

		}

	}

}