/*
 * Copyright (C) 2013 The Guava Authors
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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import org.magneton.core.base.Preconditions;
import org.magneton.core.base.Supplier;

/**
 * A builder for a multimap implementation that allows customization of the backing map
 * and value collection implementations used in a particular multimap.
 *
 * <p>
 * This can be used to easily configure multimap data structure implementations not
 * provided explicitly in {@code com.google.common.collect}, for example:
 *
 * <pre>{@code
 * ListMultimap<String, Integer> treeListMultimap =
 *     MultimapBuilder.treeKeys().arrayListValues().build();
 * SetMultimap<Integer, MyEnum> hashEnumMultimap =
 *     MultimapBuilder.hashKeys().enumSetValues(MyEnum.class).build();
 * }</pre>
 *
 * <p>
 * {@code MultimapBuilder} instances are immutable. Invoking a configuration method has no
 * effect on the receiving instance; you must store and use the new builder instance it
 * returns instead.
 *
 * <p>
 * The generated multimaps are serializable if the key and value types are serializable,
 * unless stated otherwise in one of the configuration methods.
 *
 * @author Louis Wasserman
 * @param <K0> An upper bound on the key type of the generated multimap.
 * @param <V0> An upper bound on the value type of the generated multimap.
 * @since 16.0
 */
@ElementTypesAreNonnullByDefault
public abstract class MultimapBuilder<K0 extends Object, V0 extends Object> {

	/*
	 * Leaving K and V as upper bounds rather than the actual key and value types allows
	 * type parameters to be left implicit more often. CacheBuilder uses the same
	 * technique.
	 */

	private static final int DEFAULT_EXPECTED_KEYS = 8;

	private MultimapBuilder() {
	}

	/** Uses a hash table to map keys to value collections. */
	public static MultimapBuilderWithKeys<Object> hashKeys() {
		return hashKeys(DEFAULT_EXPECTED_KEYS);
	}

	/**
	 * Uses a hash table to map keys to value collections, initialized to expect the
	 * specified number of keys.
	 * @throws IllegalArgumentException if {@code expectedKeys < 0}
	 */
	public static MultimapBuilderWithKeys<Object> hashKeys(int expectedKeys) {
		CollectPreconditions.checkNonnegative(expectedKeys, "expectedKeys");
		return new MultimapBuilderWithKeys<Object>() {
			@Override
			<K extends Object, V extends Object> Map<K, Collection<V>> createMap() {
				return Platform.newHashMapWithExpectedSize(expectedKeys);
			}
		};
	}

	/**
	 * Uses a hash table to map keys to value collections.
	 *
	 * <p>
	 * The collections returned by {@link Multimap#keySet()}, {@link Multimap#keys()}, and
	 * {@link Multimap#asMap()} will iterate through the keys in the order that they were
	 * first added to the multimap, save that if all values associated with a key are
	 * removed and then the key is added back into the multimap, that key will come last
	 * in the key iteration order.
	 */
	public static MultimapBuilderWithKeys<Object> linkedHashKeys() {
		return linkedHashKeys(DEFAULT_EXPECTED_KEYS);
	}

	/**
	 * Uses an hash table to map keys to value collections, initialized to expect the
	 * specified number of keys.
	 *
	 * <p>
	 * The collections returned by {@link Multimap#keySet()}, {@link Multimap#keys()}, and
	 * {@link Multimap#asMap()} will iterate through the keys in the order that they were
	 * first added to the multimap, save that if all values associated with a key are
	 * removed and then the key is added back into the multimap, that key will come last
	 * in the key iteration order.
	 */
	public static MultimapBuilderWithKeys<Object> linkedHashKeys(int expectedKeys) {
		CollectPreconditions.checkNonnegative(expectedKeys, "expectedKeys");
		return new MultimapBuilderWithKeys<Object>() {
			@Override
			<K, V> Map<K, Collection<V>> createMap() {
				return Platform.newLinkedHashMapWithExpectedSize(expectedKeys);
			}
		};
	}

	/**
	 * Uses a naturally-ordered {@link TreeMap} to map keys to value collections.
	 *
	 * <p>
	 * The collections returned by {@link Multimap#keySet()}, {@link Multimap#keys()}, and
	 * {@link Multimap#asMap()} will iterate through the keys in sorted order.
	 *
	 * <p>
	 * For all multimaps generated by the resulting builder, the {@link Multimap#keySet()}
	 * can be safely cast to a {@link SortedSet}, and the {@link Multimap#asMap()} can
	 * safely be cast to a {@link java.util.SortedMap}.
	 */
	public static MultimapBuilderWithKeys<Comparable> treeKeys() {
		return treeKeys(Ordering.natural());
	}

	/**
	 * Uses a {@link TreeMap} sorted by the specified comparator to map keys to value
	 * collections.
	 *
	 * <p>
	 * The collections returned by {@link Multimap#keySet()}, {@link Multimap#keys()}, and
	 * {@link Multimap#asMap()} will iterate through the keys in sorted order.
	 *
	 * <p>
	 * For all multimaps generated by the resulting builder, the {@link Multimap#keySet()}
	 * can be safely cast to a {@link SortedSet}, and the {@link Multimap#asMap()} can
	 * safely be cast to a {@link java.util.SortedMap}.
	 *
	 * <p>
	 * Multimaps generated by the resulting builder will not be serializable if
	 * {@code comparator} is not serializable.
	 */
	public static <K0 extends Object> MultimapBuilderWithKeys<K0> treeKeys(Comparator<K0> comparator) {
		Preconditions.checkNotNull(comparator);
		return new MultimapBuilderWithKeys<K0>() {
			@Override
			<K extends K0, V extends Object> Map<K, Collection<V>> createMap() {
				return new TreeMap<>(comparator);
			}
		};
	}

	/**
	 * Uses an {@link EnumMap} to map keys to value collections.
	 *
	 * @since 16.0
	 */
	public static <K0 extends Enum<K0>> MultimapBuilderWithKeys<K0> enumKeys(Class<K0> keyClass) {
		Preconditions.checkNotNull(keyClass);
		return new MultimapBuilderWithKeys<K0>() {
			@Override
			<K extends K0, V extends Object> Map<K, Collection<V>> createMap() {
				// K must actually be K0, since enums are effectively final
				// (their subclasses are inaccessible)
				return (Map<K, Collection<V>>) new EnumMap<K0, Collection<V>>(keyClass);
			}
		};
	}

	/** Returns a new, empty {@code Multimap} with the specified implementation. */
	public abstract <K extends K0, V extends V0> Multimap<K, V> build();

	/**
	 * Returns a {@code Multimap} with the specified implementation, initialized with the
	 * entries of {@code multimap}.
	 */
	public <K extends K0, V extends V0> Multimap<K, V> build(Multimap<? extends K, ? extends V> multimap) {
		Multimap<K, V> result = build();
		result.putAll(multimap);
		return result;
	}

	private enum LinkedListSupplier implements org.magneton.core.base.Supplier<List<?>> {

		INSTANCE;

		public static <V extends Object> org.magneton.core.base.Supplier<List<V>> instance() {
			// Each call generates a fresh LinkedList, which can serve as a List<V> for
			// any V.
			org.magneton.core.base.Supplier<List<V>> result = (org.magneton.core.base.Supplier) INSTANCE;
			return result;
		}

		@Override
		public List<?> get() {
			return new LinkedList<>();
		}

	}

	private static final class ArrayListSupplier<V extends Object>
			implements org.magneton.core.base.Supplier<List<V>>, Serializable {

		private final int expectedValuesPerKey;

		ArrayListSupplier(int expectedValuesPerKey) {
			this.expectedValuesPerKey = CollectPreconditions.checkNonnegative(expectedValuesPerKey,
					"expectedValuesPerKey");
		}

		@Override
		public List<V> get() {
			return new ArrayList<>(expectedValuesPerKey);
		}

	}

	private static final class HashSetSupplier<V extends Object>
			implements org.magneton.core.base.Supplier<Set<V>>, Serializable {

		private final int expectedValuesPerKey;

		HashSetSupplier(int expectedValuesPerKey) {
			this.expectedValuesPerKey = CollectPreconditions.checkNonnegative(expectedValuesPerKey,
					"expectedValuesPerKey");
		}

		@Override
		public Set<V> get() {
			return Platform.newHashSetWithExpectedSize(expectedValuesPerKey);
		}

	}

	private static final class LinkedHashSetSupplier<V extends Object>
			implements org.magneton.core.base.Supplier<Set<V>>, Serializable {

		private final int expectedValuesPerKey;

		LinkedHashSetSupplier(int expectedValuesPerKey) {
			this.expectedValuesPerKey = CollectPreconditions.checkNonnegative(expectedValuesPerKey,
					"expectedValuesPerKey");
		}

		@Override
		public Set<V> get() {
			return Platform.newLinkedHashSetWithExpectedSize(expectedValuesPerKey);
		}

	}

	private static final class TreeSetSupplier<V extends Object>
			implements org.magneton.core.base.Supplier<SortedSet<V>>, Serializable {

		private final Comparator<? super V> comparator;

		TreeSetSupplier(Comparator<? super V> comparator) {
			this.comparator = Preconditions.checkNotNull(comparator);
		}

		@Override
		public SortedSet<V> get() {
			return new TreeSet<>(comparator);
		}

	}

	private static final class EnumSetSupplier<V extends Enum<V>>
			implements org.magneton.core.base.Supplier<Set<V>>, Serializable {

		private final Class<V> clazz;

		EnumSetSupplier(Class<V> clazz) {
			this.clazz = Preconditions.checkNotNull(clazz);
		}

		@Override
		public Set<V> get() {
			return EnumSet.noneOf(clazz);
		}

	}

	/**
	 * An intermediate stage in a {@link MultimapBuilder} in which the key-value
	 * collection map implementation has been specified, but the value collection
	 * implementation has not.
	 *
	 * @param <K0> The upper bound on the key type of the generated multimap.
	 * @since 16.0
	 */
	public abstract static class MultimapBuilderWithKeys<K0 extends Object> {

		private static final int DEFAULT_EXPECTED_VALUES_PER_KEY = 2;

		MultimapBuilderWithKeys() {
		}

		abstract <K extends K0, V extends Object> Map<K, Collection<V>> createMap();

		/** Uses an {@link ArrayList} to store value collections. */
		public ListMultimapBuilder<K0, Object> arrayListValues() {
			return arrayListValues(DEFAULT_EXPECTED_VALUES_PER_KEY);
		}

		/**
		 * Uses an {@link ArrayList} to store value collections, initialized to expect the
		 * specified number of values per key.
		 * @throws IllegalArgumentException if {@code expectedValuesPerKey < 0}
		 */
		public ListMultimapBuilder<K0, Object> arrayListValues(int expectedValuesPerKey) {
			CollectPreconditions.checkNonnegative(expectedValuesPerKey, "expectedValuesPerKey");
			return new ListMultimapBuilder<K0, Object>() {
				@Override
				public <K extends K0, V extends Object> org.magneton.core.collect.ListMultimap<K, V> build() {
					return org.magneton.core.collect.Multimaps.newListMultimap(
							MultimapBuilderWithKeys.this.<K, V>createMap(),
							new ArrayListSupplier<V>(expectedValuesPerKey));
				}
			};
		}

		/** Uses a {@link LinkedList} to store value collections. */
		public ListMultimapBuilder<K0, Object> linkedListValues() {
			return new ListMultimapBuilder<K0, Object>() {
				@Override
				public <K extends K0, V extends Object> org.magneton.core.collect.ListMultimap<K, V> build() {
					return org.magneton.core.collect.Multimaps.newListMultimap(
							MultimapBuilderWithKeys.this.<K, V>createMap(), LinkedListSupplier.<V>instance());
				}
			};
		}

		/** Uses a hash-based {@code Set} to store value collections. */
		public SetMultimapBuilder<K0, Object> hashSetValues() {
			return hashSetValues(DEFAULT_EXPECTED_VALUES_PER_KEY);
		}

		/**
		 * Uses a hash-based {@code Set} to store value collections, initialized to expect
		 * the specified number of values per key.
		 * @throws IllegalArgumentException if {@code expectedValuesPerKey < 0}
		 */
		public SetMultimapBuilder<K0, Object> hashSetValues(int expectedValuesPerKey) {
			CollectPreconditions.checkNonnegative(expectedValuesPerKey, "expectedValuesPerKey");
			return new SetMultimapBuilder<K0, Object>() {
				@Override
				public <K extends K0, V extends Object> org.magneton.core.collect.SetMultimap<K, V> build() {
					return org.magneton.core.collect.Multimaps.newSetMultimap(
							MultimapBuilderWithKeys.this.<K, V>createMap(),
							new HashSetSupplier<V>(expectedValuesPerKey));
				}
			};
		}

		/**
		 * Uses an insertion-ordered hash-based {@code Set} to store value collections.
		 */
		public SetMultimapBuilder<K0, Object> linkedHashSetValues() {
			return linkedHashSetValues(DEFAULT_EXPECTED_VALUES_PER_KEY);
		}

		/**
		 * Uses an insertion-ordered hash-based {@code Set} to store value collections,
		 * initialized to expect the specified number of values per key.
		 * @throws IllegalArgumentException if {@code expectedValuesPerKey < 0}
		 */
		public SetMultimapBuilder<K0, Object> linkedHashSetValues(int expectedValuesPerKey) {
			CollectPreconditions.checkNonnegative(expectedValuesPerKey, "expectedValuesPerKey");
			return new SetMultimapBuilder<K0, Object>() {
				@Override
				public <K extends K0, V extends Object> org.magneton.core.collect.SetMultimap<K, V> build() {
					return org.magneton.core.collect.Multimaps.newSetMultimap(
							MultimapBuilderWithKeys.this.<K, V>createMap(),
							new LinkedHashSetSupplier<V>(expectedValuesPerKey));
				}
			};
		}

		/** Uses a naturally-ordered {@link TreeSet} to store value collections. */
		public SortedSetMultimapBuilder<K0, Comparable> treeSetValues() {
			return treeSetValues(Ordering.natural());
		}

		/**
		 * Uses a {@link TreeSet} ordered by the specified comparator to store value
		 * collections.
		 *
		 * <p>
		 * Multimaps generated by the resulting builder will not be serializable if {@code
		 * comparator} is not serializable.
		 */
		public <V0 extends Object> SortedSetMultimapBuilder<K0, V0> treeSetValues(Comparator<V0> comparator) {
			Preconditions.checkNotNull(comparator, "comparator");
			return new SortedSetMultimapBuilder<K0, V0>() {
				@Override
				public <K extends K0, V extends V0> SortedSetMultimap<K, V> build() {
					return org.magneton.core.collect.Multimaps.newSortedSetMultimap(
							MultimapBuilderWithKeys.this.<K, V>createMap(), new TreeSetSupplier<V>(comparator));
				}
			};
		}

		/** Uses an {@link EnumSet} to store value collections. */
		public <V0 extends Enum<V0>> SetMultimapBuilder<K0, V0> enumSetValues(Class<V0> valueClass) {
			Preconditions.checkNotNull(valueClass, "valueClass");
			return new SetMultimapBuilder<K0, V0>() {
				@Override
				public <K extends K0, V extends V0> org.magneton.core.collect.SetMultimap<K, V> build() {
					// V must actually be V0, since enums are effectively final
					// (their subclasses are inaccessible)
					org.magneton.core.base.Supplier<Set<V>> factory = (Supplier) new EnumSetSupplier<V0>(valueClass);
					return Multimaps.newSetMultimap(MultimapBuilderWithKeys.this.<K, V>createMap(), factory);
				}
			};
		}

	}

	/**
	 * A specialization of {@link MultimapBuilder} that generates
	 * {@link org.magneton.core.collect.ListMultimap} instances.
	 *
	 * @since 16.0
	 */
	public abstract static class ListMultimapBuilder<K0 extends Object, V0 extends Object>
			extends MultimapBuilder<K0, V0> {

		ListMultimapBuilder() {
		}

		@Override
		public abstract <K extends K0, V extends V0> org.magneton.core.collect.ListMultimap<K, V> build();

		@Override
		public <K extends K0, V extends V0> org.magneton.core.collect.ListMultimap<K, V> build(
				Multimap<? extends K, ? extends V> multimap) {
			return (ListMultimap<K, V>) super.build(multimap);
		}

	}

	/**
	 * A specialization of {@link MultimapBuilder} that generates
	 * {@link org.magneton.core.collect.SetMultimap} instances.
	 *
	 * @since 16.0
	 */
	public abstract static class SetMultimapBuilder<K0 extends Object, V0 extends Object>
			extends MultimapBuilder<K0, V0> {

		SetMultimapBuilder() {
		}

		@Override
		public abstract <K extends K0, V extends V0> org.magneton.core.collect.SetMultimap<K, V> build();

		@Override
		public <K extends K0, V extends V0> org.magneton.core.collect.SetMultimap<K, V> build(
				Multimap<? extends K, ? extends V> multimap) {
			return (SetMultimap<K, V>) super.build(multimap);
		}

	}

	/**
	 * A specialization of {@link MultimapBuilder} that generates
	 * {@link SortedSetMultimap} instances.
	 *
	 * @since 16.0
	 */
	public abstract static class SortedSetMultimapBuilder<K0 extends Object, V0 extends Object>
			extends SetMultimapBuilder<K0, V0> {

		SortedSetMultimapBuilder() {
		}

		@Override
		public abstract <K extends K0, V extends V0> SortedSetMultimap<K, V> build();

		@Override
		public <K extends K0, V extends V0> SortedSetMultimap<K, V> build(Multimap<? extends K, ? extends V> multimap) {
			return (SortedSetMultimap<K, V>) super.build(multimap);
		}

	}

}