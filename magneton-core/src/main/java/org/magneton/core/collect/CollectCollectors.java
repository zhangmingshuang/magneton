/*
 * Copyright (C) 2016 The Guava Authors
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

import java.util.Collection;
import java.util.Comparator;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.LinkedHashMap;
import java.util.TreeMap;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.function.ToIntFunction;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.CheckForNull;

import static java.util.stream.Collectors.collectingAndThen;

/** Collectors utilities for {@code common.collect} internals. */
@ElementTypesAreNonnullByDefault
final class CollectCollectors {

	private static final Collector<Object, ?, org.magneton.core.collect.ImmutableList<Object>> TO_IMMUTABLE_LIST = Collector
			.of(org.magneton.core.collect.ImmutableList::builder, org.magneton.core.collect.ImmutableList.Builder::add,
					org.magneton.core.collect.ImmutableList.Builder::combine,
					org.magneton.core.collect.ImmutableList.Builder::build);

	private static final Collector<Object, ?, org.magneton.core.collect.ImmutableSet<Object>> TO_IMMUTABLE_SET = Collector
			.of(org.magneton.core.collect.ImmutableSet::builder, org.magneton.core.collect.ImmutableSet.Builder::add,
					org.magneton.core.collect.ImmutableSet.Builder::combine,
					org.magneton.core.collect.ImmutableSet.Builder::build);

	private static final Collector<Range<Comparable<?>>, ?, org.magneton.core.collect.ImmutableRangeSet<Comparable<?>>> TO_IMMUTABLE_RANGE_SET = Collector
			.of(org.magneton.core.collect.ImmutableRangeSet::builder,
					org.magneton.core.collect.ImmutableRangeSet.Builder::add,
					org.magneton.core.collect.ImmutableRangeSet.Builder::combine,
					org.magneton.core.collect.ImmutableRangeSet.Builder::build);

	// Lists

	static <E> Collector<E, ?, ImmutableList<E>> toImmutableList() {
		return (Collector) TO_IMMUTABLE_LIST;
	}

	// Sets

	static <E> Collector<E, ?, org.magneton.core.collect.ImmutableSet<E>> toImmutableSet() {
		return (Collector) TO_IMMUTABLE_SET;
	}

	static <E> Collector<E, ?, ImmutableSortedSet<E>> toImmutableSortedSet(Comparator<? super E> comparator) {
		org.magneton.core.base.Preconditions.checkNotNull(comparator);
		return Collector.of(() -> new ImmutableSortedSet.Builder<E>(comparator), ImmutableSortedSet.Builder::add,
				ImmutableSortedSet.Builder::combine, ImmutableSortedSet.Builder::build);
	}

	static <E extends Enum<E>> Collector<E, ?, org.magneton.core.collect.ImmutableSet<E>> toImmutableEnumSet() {
		return (Collector) EnumSetAccumulator.TO_IMMUTABLE_ENUM_SET;
	}

	static <E extends Comparable<? super E>> Collector<Range<E>, ?, ImmutableRangeSet<E>> toImmutableRangeSet() {
		return (Collector) TO_IMMUTABLE_RANGE_SET;
	}

	static <T extends Object, E> Collector<T, ?, org.magneton.core.collect.ImmutableMultiset<E>> toImmutableMultiset(
			Function<? super T, ? extends E> elementFunction, ToIntFunction<? super T> countFunction) {
		org.magneton.core.base.Preconditions.checkNotNull(elementFunction);
		org.magneton.core.base.Preconditions.checkNotNull(countFunction);
		return Collector.of(LinkedHashMultiset::create,
				(multiset, t) -> multiset.add(
						org.magneton.core.base.Preconditions.checkNotNull(elementFunction.apply(t)),
						countFunction.applyAsInt(t)),
				(multiset1, multiset2) -> {
					multiset1.addAll(multiset2);
					return multiset1;
				}, (Multiset<E> multiset) -> ImmutableMultiset.copyFromEntries(multiset.entrySet()));
	}

	// Multisets

	static <T extends Object, E extends Object, M extends Multiset<E>> Collector<T, ?, M> toMultiset(
			Function<? super T, E> elementFunction, ToIntFunction<? super T> countFunction,
			Supplier<M> multisetSupplier) {
		org.magneton.core.base.Preconditions.checkNotNull(elementFunction);
		org.magneton.core.base.Preconditions.checkNotNull(countFunction);
		org.magneton.core.base.Preconditions.checkNotNull(multisetSupplier);
		return Collector.of(multisetSupplier, (ms, t) -> ms.add(elementFunction.apply(t), countFunction.applyAsInt(t)),
				(ms1, ms2) -> {
					ms1.addAll(ms2);
					return ms1;
				});
	}

	static <T extends Object, K, V> Collector<T, ?, org.magneton.core.collect.ImmutableMap<K, V>> toImmutableMap(
			Function<? super T, ? extends K> keyFunction, Function<? super T, ? extends V> valueFunction) {
		org.magneton.core.base.Preconditions.checkNotNull(keyFunction);
		org.magneton.core.base.Preconditions.checkNotNull(valueFunction);
		return Collector.of(org.magneton.core.collect.ImmutableMap.Builder<K, V>::new,
				(builder, input) -> builder.put(keyFunction.apply(input), valueFunction.apply(input)),
				org.magneton.core.collect.ImmutableMap.Builder::combine,
				org.magneton.core.collect.ImmutableMap.Builder::build);
	}

	// Maps

	public static <T extends Object, K, V> Collector<T, ?, org.magneton.core.collect.ImmutableMap<K, V>> toImmutableMap(
			Function<? super T, ? extends K> keyFunction, Function<? super T, ? extends V> valueFunction,
			BinaryOperator<V> mergeFunction) {
		org.magneton.core.base.Preconditions.checkNotNull(keyFunction);
		org.magneton.core.base.Preconditions.checkNotNull(valueFunction);
		org.magneton.core.base.Preconditions.checkNotNull(mergeFunction);
		return collectingAndThen(Collectors.toMap(keyFunction, valueFunction, mergeFunction, LinkedHashMap::new),
				org.magneton.core.collect.ImmutableMap::copyOf);
	}

	static <T extends Object, K, V> Collector<T, ?, ImmutableSortedMap<K, V>> toImmutableSortedMap(
			Comparator<? super K> comparator, Function<? super T, ? extends K> keyFunction,
			Function<? super T, ? extends V> valueFunction) {
		org.magneton.core.base.Preconditions.checkNotNull(comparator);
		org.magneton.core.base.Preconditions.checkNotNull(keyFunction);
		org.magneton.core.base.Preconditions.checkNotNull(valueFunction);
		/*
		 * We will always fail if there are duplicate keys, and the keys are always sorted
		 * by the Comparator, so the entries can come in an arbitrary order -- so we
		 * report UNORDERED.
		 */
		return Collector.of(() -> new ImmutableSortedMap.Builder<K, V>(comparator),
				(builder, input) -> builder.put(keyFunction.apply(input), valueFunction.apply(input)),
				ImmutableSortedMap.Builder::combine, ImmutableSortedMap.Builder::build,
				Collector.Characteristics.UNORDERED);
	}

	static <T extends Object, K, V> Collector<T, ?, ImmutableSortedMap<K, V>> toImmutableSortedMap(
			Comparator<? super K> comparator, Function<? super T, ? extends K> keyFunction,
			Function<? super T, ? extends V> valueFunction, BinaryOperator<V> mergeFunction) {
		org.magneton.core.base.Preconditions.checkNotNull(comparator);
		org.magneton.core.base.Preconditions.checkNotNull(keyFunction);
		org.magneton.core.base.Preconditions.checkNotNull(valueFunction);
		org.magneton.core.base.Preconditions.checkNotNull(mergeFunction);
		return collectingAndThen(
				Collectors.toMap(keyFunction, valueFunction, mergeFunction, () -> new TreeMap<K, V>(comparator)),
				ImmutableSortedMap::copyOfSorted);
	}

	static <T extends Object, K, V> Collector<T, ?, org.magneton.core.collect.ImmutableBiMap<K, V>> toImmutableBiMap(
			Function<? super T, ? extends K> keyFunction, Function<? super T, ? extends V> valueFunction) {
		org.magneton.core.base.Preconditions.checkNotNull(keyFunction);
		org.magneton.core.base.Preconditions.checkNotNull(valueFunction);
		return Collector.of(org.magneton.core.collect.ImmutableBiMap.Builder<K, V>::new,
				(builder, input) -> builder.put(keyFunction.apply(input), valueFunction.apply(input)),
				org.magneton.core.collect.ImmutableBiMap.Builder::combine, ImmutableBiMap.Builder::build,
				new Collector.Characteristics[0]);
	}

	static <T extends Object, K extends Enum<K>, V> Collector<T, ?, org.magneton.core.collect.ImmutableMap<K, V>> toImmutableEnumMap(
			Function<? super T, ? extends K> keyFunction, Function<? super T, ? extends V> valueFunction) {
		org.magneton.core.base.Preconditions.checkNotNull(keyFunction);
		org.magneton.core.base.Preconditions.checkNotNull(valueFunction);
		return Collector.of(() -> new EnumMapAccumulator<K, V>((v1, v2) -> {
			throw new IllegalArgumentException("Multiple values for key: " + v1 + ", " + v2);
		}), (accum, t) -> {
			/*
			 * We assign these to variables before calling checkNotNull to work around a
			 * bug in our nullness checker.
			 */
			K key = keyFunction.apply(t);
			V newValue = valueFunction.apply(t);
			accum.put(org.magneton.core.base.Preconditions.checkNotNull(key, "Null key for input %s", t),
					org.magneton.core.base.Preconditions.checkNotNull(newValue, "Null value for input %s", t));
		}, EnumMapAccumulator::combine, EnumMapAccumulator::toImmutableMap, Collector.Characteristics.UNORDERED);
	}

	static <T extends Object, K extends Enum<K>, V> Collector<T, ?, org.magneton.core.collect.ImmutableMap<K, V>> toImmutableEnumMap(
			Function<? super T, ? extends K> keyFunction, Function<? super T, ? extends V> valueFunction,
			BinaryOperator<V> mergeFunction) {
		org.magneton.core.base.Preconditions.checkNotNull(keyFunction);
		org.magneton.core.base.Preconditions.checkNotNull(valueFunction);
		org.magneton.core.base.Preconditions.checkNotNull(mergeFunction);
		// not UNORDERED because we don't know if mergeFunction is commutative
		return Collector.of(() -> new EnumMapAccumulator<K, V>(mergeFunction), (accum, t) -> {
			/*
			 * We assign these to variables before calling checkNotNull to work around a
			 * bug in our nullness checker.
			 */
			K key = keyFunction.apply(t);
			V newValue = valueFunction.apply(t);
			accum.put(org.magneton.core.base.Preconditions.checkNotNull(key, "Null key for input %s", t),
					org.magneton.core.base.Preconditions.checkNotNull(newValue, "Null value for input %s", t));
		}, EnumMapAccumulator::combine, EnumMapAccumulator::toImmutableMap);
	}

	static <T extends Object, K extends Comparable<? super K>, V> Collector<T, ?, org.magneton.core.collect.ImmutableRangeMap<K, V>> toImmutableRangeMap(
			Function<? super T, Range<K>> keyFunction, Function<? super T, ? extends V> valueFunction) {
		org.magneton.core.base.Preconditions.checkNotNull(keyFunction);
		org.magneton.core.base.Preconditions.checkNotNull(valueFunction);
		return Collector.of(org.magneton.core.collect.ImmutableRangeMap::<K, V>builder,
				(builder, input) -> builder.put(keyFunction.apply(input), valueFunction.apply(input)),
				org.magneton.core.collect.ImmutableRangeMap.Builder::combine, ImmutableRangeMap.Builder::build);
	}

	static <T extends Object, K, V> Collector<T, ?, ImmutableListMultimap<K, V>> toImmutableListMultimap(
			Function<? super T, ? extends K> keyFunction, Function<? super T, ? extends V> valueFunction) {
		org.magneton.core.base.Preconditions.checkNotNull(keyFunction, "keyFunction");
		org.magneton.core.base.Preconditions.checkNotNull(valueFunction, "valueFunction");
		return Collector.of(ImmutableListMultimap::<K, V>builder,
				(builder, t) -> builder.put(keyFunction.apply(t), valueFunction.apply(t)),
				ImmutableListMultimap.Builder::combine, ImmutableListMultimap.Builder::build);
	}

	static <T extends Object, K, V> Collector<T, ?, ImmutableListMultimap<K, V>> flatteningToImmutableListMultimap(
			Function<? super T, ? extends K> keyFunction,
			Function<? super T, ? extends Stream<? extends V>> valuesFunction) {
		org.magneton.core.base.Preconditions.checkNotNull(keyFunction);
		org.magneton.core.base.Preconditions.checkNotNull(valuesFunction);
		return collectingAndThen(
				flatteningToMultimap(
						input -> org.magneton.core.base.Preconditions.checkNotNull(keyFunction.apply(input)),
						input -> valuesFunction.apply(input).peek(org.magneton.core.base.Preconditions::checkNotNull),
						org.magneton.core.collect.MultimapBuilder.linkedHashKeys().arrayListValues()::<K, V>build),
				ImmutableListMultimap::copyOf);
	}

	// Multimaps

	static <T extends Object, K, V> Collector<T, ?, ImmutableSetMultimap<K, V>> toImmutableSetMultimap(
			Function<? super T, ? extends K> keyFunction, Function<? super T, ? extends V> valueFunction) {
		org.magneton.core.base.Preconditions.checkNotNull(keyFunction, "keyFunction");
		org.magneton.core.base.Preconditions.checkNotNull(valueFunction, "valueFunction");
		return Collector.of(ImmutableSetMultimap::<K, V>builder,
				(builder, t) -> builder.put(keyFunction.apply(t), valueFunction.apply(t)),
				ImmutableSetMultimap.Builder::combine, ImmutableSetMultimap.Builder::build);
	}

	static <T extends Object, K, V> Collector<T, ?, ImmutableSetMultimap<K, V>> flatteningToImmutableSetMultimap(
			Function<? super T, ? extends K> keyFunction,
			Function<? super T, ? extends Stream<? extends V>> valuesFunction) {
		org.magneton.core.base.Preconditions.checkNotNull(keyFunction);
		org.magneton.core.base.Preconditions.checkNotNull(valuesFunction);
		return collectingAndThen(
				flatteningToMultimap(
						input -> org.magneton.core.base.Preconditions.checkNotNull(keyFunction.apply(input)),
						input -> valuesFunction.apply(input).peek(org.magneton.core.base.Preconditions::checkNotNull),
						MultimapBuilder.linkedHashKeys().linkedHashSetValues()::<K, V>build),
				ImmutableSetMultimap::copyOf);
	}

	static <T extends Object, K extends Object, V extends Object, M extends Multimap<K, V>> Collector<T, ?, M> toMultimap(
			Function<? super T, ? extends K> keyFunction, Function<? super T, ? extends V> valueFunction,
			Supplier<M> multimapSupplier) {
		org.magneton.core.base.Preconditions.checkNotNull(keyFunction);
		org.magneton.core.base.Preconditions.checkNotNull(valueFunction);
		org.magneton.core.base.Preconditions.checkNotNull(multimapSupplier);
		return Collector.of(multimapSupplier,
				(multimap, input) -> multimap.put(keyFunction.apply(input), valueFunction.apply(input)),
				(multimap1, multimap2) -> {
					multimap1.putAll(multimap2);
					return multimap1;
				});
	}

	static <T extends Object, K extends Object, V extends Object, M extends Multimap<K, V>> Collector<T, ?, M> flatteningToMultimap(
			Function<? super T, ? extends K> keyFunction,
			Function<? super T, ? extends Stream<? extends V>> valueFunction, Supplier<M> multimapSupplier) {
		org.magneton.core.base.Preconditions.checkNotNull(keyFunction);
		org.magneton.core.base.Preconditions.checkNotNull(valueFunction);
		org.magneton.core.base.Preconditions.checkNotNull(multimapSupplier);
		return Collector.of(multimapSupplier, (multimap, input) -> {
			K key = keyFunction.apply(input);
			Collection<V> valuesForKey = multimap.get(key);
			valueFunction.apply(input).forEachOrdered(valuesForKey::add);
		}, (multimap1, multimap2) -> {
			multimap1.putAll(multimap2);
			return multimap1;
		});
	}

	private static final class EnumSetAccumulator<E extends Enum<E>> {

		static final Collector<Enum<?>, ?, org.magneton.core.collect.ImmutableSet<? extends Enum<?>>> TO_IMMUTABLE_ENUM_SET = (Collector) Collector.<Enum, EnumSetAccumulator, org.magneton.core.collect.ImmutableSet<?>>of(
				EnumSetAccumulator::new, EnumSetAccumulator::add, EnumSetAccumulator::combine,
				EnumSetAccumulator::toImmutableSet, Collector.Characteristics.UNORDERED);

		@CheckForNull
		private EnumSet<E> set;

		void add(E e) {
			if (set == null) {
				set = EnumSet.of(e);
			}
			else {
				set.add(e);
			}
		}

		EnumSetAccumulator<E> combine(EnumSetAccumulator<E> other) {
			if (set == null) {
				return other;
			}
			else if (other.set == null) {
				return this;
			}
			else {
				set.addAll(other.set);
				return this;
			}
		}

		org.magneton.core.collect.ImmutableSet<E> toImmutableSet() {
			return (set == null) ? ImmutableSet.<E>of() : org.magneton.core.collect.ImmutableEnumSet.asImmutable(set);
		}

	}

	private static class EnumMapAccumulator<K extends Enum<K>, V> {

		private final BinaryOperator<V> mergeFunction;

		@CheckForNull
		private EnumMap<K, V> map = null;

		EnumMapAccumulator(BinaryOperator<V> mergeFunction) {
			this.mergeFunction = mergeFunction;
		}

		void put(K key, V value) {
			if (map == null) {
				map = new EnumMap<>(key.getDeclaringClass());
			}
			map.merge(key, value, mergeFunction);
		}

		EnumMapAccumulator<K, V> combine(EnumMapAccumulator<K, V> other) {
			if (map == null) {
				return other;
			}
			else if (other.map == null) {
				return this;
			}
			else {
				other.map.forEach(this::put);
				return this;
			}
		}

		org.magneton.core.collect.ImmutableMap<K, V> toImmutableMap() {
			return (map == null) ? ImmutableMap.<K, V>of()
					: org.magneton.core.collect.ImmutableEnumMap.asImmutable(map);
		}

	}

}
