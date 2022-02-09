/*
 * Copyright (C) 2009 The Guava Authors
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

import java.io.IOException;
import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.Map.Entry;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Stream;

import javax.annotations.CanIgnoreReturnValue;
import javax.annotation.CheckForNull;
import javax.annotations.DoNotCall;
import javax.annotations.LazyInit;
import javax.annotations.Weak;

import org.magneton.core.base.MoreObjects;
import org.magneton.core.base.Preconditions;

/**
 * A {@link org.magneton.core.collect.SetMultimap} whose contents will never change, with
 * many other important properties detailed at {@link ImmutableCollection}.
 *
 * <p>
 * <b>Warning:</b> As in all {@link org.magneton.core.collect.SetMultimap}s, do not modify
 * either a key <i>or a value</i> of a {@code ImmutableSetMultimap} in a way that affects
 * its {@link Object#equals} behavior. Undefined behavior and bugs will result.
 *
 * <p>
 * See the Guava User Guide article on
 * <a href= "https://github.com/google/guava/wiki/ImmutableCollectionsExplained">
 * immutable collections</a>.
 *
 * @author Mike Ward
 * @since 2.0
 */
@ElementTypesAreNonnullByDefault
public class ImmutableSetMultimap<K, V> extends org.magneton.core.collect.ImmutableMultimap<K, V>
		implements SetMultimap<K, V> {

	private static final long serialVersionUID = 0;

	/**
	 * Returned by get() when a missing key is provided. Also holds the comparator, if
	 * any, used for values.
	 */
	private final transient org.magneton.core.collect.ImmutableSet<V> emptySet;

	@LazyInit
	@CheckForNull
	private transient ImmutableSetMultimap<V, K> inverse;

	@LazyInit
	@CheckForNull
	private transient org.magneton.core.collect.ImmutableSet<Entry<K, V>> entries;

	ImmutableSetMultimap(org.magneton.core.collect.ImmutableMap<K, org.magneton.core.collect.ImmutableSet<V>> map,
			int size, @CheckForNull Comparator<? super V> valueComparator) {
		super(map, size);
		emptySet = emptySet(valueComparator);
	}

	/**
	 * Returns a {@link Collector} that accumulates elements into an
	 * {@code ImmutableSetMultimap} whose keys and values are the result of applying the
	 * provided mapping functions to the input elements.
	 *
	 * <p>
	 * For streams with defined encounter order (as defined in the Ordering section of the
	 * {@link java.util.stream} Javadoc), that order is preserved, but entries are
	 * <a href="ImmutableMultimap.html#iteration">grouped by key</a>.
	 *
	 * <p>
	 * Example:
	 *
	 * <pre>{@code
	 * static final Multimap<Character, String> FIRST_LETTER_MULTIMAP =
	 *     Stream.of("banana", "apple", "carrot", "asparagus", "cherry")
	 *         .collect(toImmutableSetMultimap(str -> str.charAt(0), str -> str.substring(1)));
	 *
	 * // is equivalent to
	 *
	 * static final Multimap<Character, String> FIRST_LETTER_MULTIMAP =
	 *     new ImmutableSetMultimap.Builder<Character, String>()
	 *         .put('b', "anana")
	 *         .putAll('a', "pple", "sparagus")
	 *         .putAll('c', "arrot", "herry")
	 *         .build();
	 * }</pre>
	 *
	 * @since 21.0
	 */
	public static <T extends Object, K, V> Collector<T, ?, ImmutableSetMultimap<K, V>> toImmutableSetMultimap(
			Function<? super T, ? extends K> keyFunction, Function<? super T, ? extends V> valueFunction) {
		return org.magneton.core.collect.CollectCollectors.toImmutableSetMultimap(keyFunction, valueFunction);
	}

	/**
	 * Returns a {@code Collector} accumulating entries into an
	 * {@code ImmutableSetMultimap}. Each input element is mapped to a key and a stream of
	 * values, each of which are put into the resulting {@code Multimap}, in the encounter
	 * order of the stream and the encounter order of the streams of values.
	 *
	 * <p>
	 * Example:
	 *
	 * <pre>{@code
	 * static final ImmutableSetMultimap<Character, Character> FIRST_LETTER_MULTIMAP =
	 *     Stream.of("banana", "apple", "carrot", "asparagus", "cherry")
	 *         .collect(
	 *             flatteningToImmutableSetMultimap(
	 *                  str -> str.charAt(0),
	 *                  str -> str.substring(1).chars().mapToObj(c -> (char) c));
	 *
	 * // is equivalent to
	 *
	 * static final ImmutableSetMultimap<Character, Character> FIRST_LETTER_MULTIMAP =
	 *     ImmutableSetMultimap.<Character, Character>builder()
	 *         .putAll('b', Arrays.asList('a', 'n', 'a', 'n', 'a'))
	 *         .putAll('a', Arrays.asList('p', 'p', 'l', 'e'))
	 *         .putAll('c', Arrays.asList('a', 'r', 'r', 'o', 't'))
	 *         .putAll('a', Arrays.asList('s', 'p', 'a', 'r', 'a', 'g', 'u', 's'))
	 *         .putAll('c', Arrays.asList('h', 'e', 'r', 'r', 'y'))
	 *         .build();
	 *
	 * // after deduplication, the resulting multimap is equivalent to
	 *
	 * static final ImmutableSetMultimap<Character, Character> FIRST_LETTER_MULTIMAP =
	 *     ImmutableSetMultimap.<Character, Character>builder()
	 *         .putAll('b', Arrays.asList('a', 'n'))
	 *         .putAll('a', Arrays.asList('p', 'l', 'e', 's', 'a', 'r', 'g', 'u'))
	 *         .putAll('c', Arrays.asList('a', 'r', 'o', 't', 'h', 'e', 'y'))
	 *         .build();
	 * }
	 * }</pre>
	 *
	 * @since 21.0
	 */
	public static <T extends Object, K, V> Collector<T, ?, ImmutableSetMultimap<K, V>> flatteningToImmutableSetMultimap(
			Function<? super T, ? extends K> keyFunction,
			Function<? super T, ? extends Stream<? extends V>> valuesFunction) {
		return org.magneton.core.collect.CollectCollectors.flatteningToImmutableSetMultimap(keyFunction,
				valuesFunction);
	}

	/**
	 * Returns the empty multimap.
	 *
	 * <p>
	 * <b>Performance note:</b> the instance returned is a singleton.
	 */
	// Casting is safe because the multimap will never hold any elements.
	public static <K, V> ImmutableSetMultimap<K, V> of() {
		return (ImmutableSetMultimap<K, V>) EmptyImmutableSetMultimap.INSTANCE;
	}

	// looking for of() with > 5 entries? Use the builder instead.

	/** Returns an immutable multimap containing a single entry. */
	public static <K, V> ImmutableSetMultimap<K, V> of(K k1, V v1) {
		Builder<K, V> builder = ImmutableSetMultimap.builder();
		builder.put(k1, v1);
		return builder.build();
	}

	/**
	 * Returns an immutable multimap containing the given entries, in order. Repeated
	 * occurrences of an entry (according to {@link Object#equals}) after the first are
	 * ignored.
	 */
	public static <K, V> ImmutableSetMultimap<K, V> of(K k1, V v1, K k2, V v2) {
		Builder<K, V> builder = ImmutableSetMultimap.builder();
		builder.put(k1, v1);
		builder.put(k2, v2);
		return builder.build();
	}

	/**
	 * Returns an immutable multimap containing the given entries, in order. Repeated
	 * occurrences of an entry (according to {@link Object#equals}) after the first are
	 * ignored.
	 */
	public static <K, V> ImmutableSetMultimap<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3) {
		Builder<K, V> builder = ImmutableSetMultimap.builder();
		builder.put(k1, v1);
		builder.put(k2, v2);
		builder.put(k3, v3);
		return builder.build();
	}

	/**
	 * Returns an immutable multimap containing the given entries, in order. Repeated
	 * occurrences of an entry (according to {@link Object#equals}) after the first are
	 * ignored.
	 */
	public static <K, V> ImmutableSetMultimap<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4) {
		Builder<K, V> builder = ImmutableSetMultimap.builder();
		builder.put(k1, v1);
		builder.put(k2, v2);
		builder.put(k3, v3);
		builder.put(k4, v4);
		return builder.build();
	}

	/**
	 * Returns an immutable multimap containing the given entries, in order. Repeated
	 * occurrences of an entry (according to {@link Object#equals}) after the first are
	 * ignored.
	 */
	public static <K, V> ImmutableSetMultimap<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5) {
		Builder<K, V> builder = ImmutableSetMultimap.builder();
		builder.put(k1, v1);
		builder.put(k2, v2);
		builder.put(k3, v3);
		builder.put(k4, v4);
		builder.put(k5, v5);
		return builder.build();
	}

	/** Returns a new {@link Builder}. */
	public static <K, V> Builder<K, V> builder() {
		return new Builder<>();
	}

	/**
	 * Returns an immutable set multimap containing the same mappings as {@code multimap}.
	 * The generated multimap's key and value orderings correspond to the iteration
	 * ordering of the {@code
	 * multimap.asMap()} view. Repeated occurrences of an entry in the multimap after the
	 * first are ignored.
	 *
	 * <p>
	 * Despite the method name, this method attempts to avoid actually copying the data
	 * when it is safe to do so. The exact circumstances under which a copy will or will
	 * not be performed are undocumented and subject to change.
	 * @throws NullPointerException if any key or value in {@code multimap} is null
	 */
	public static <K, V> ImmutableSetMultimap<K, V> copyOf(Multimap<? extends K, ? extends V> multimap) {
		return copyOf(multimap, null);
	}

	private static <K, V> ImmutableSetMultimap<K, V> copyOf(Multimap<? extends K, ? extends V> multimap,
			@CheckForNull Comparator<? super V> valueComparator) {
		Preconditions.checkNotNull(multimap); // eager for GWT
		if (multimap.isEmpty() && valueComparator == null) {
			return of();
		}

		if (multimap instanceof ImmutableSetMultimap) {
			// safe since multimap is not writable
			ImmutableSetMultimap<K, V> kvMultimap = (ImmutableSetMultimap<K, V>) multimap;
			if (!kvMultimap.isPartialView()) {
				return kvMultimap;
			}
		}

		return fromMapEntries(multimap.asMap().entrySet(), valueComparator);
	}

	// views

	/**
	 * Returns an immutable multimap containing the specified entries. The returned
	 * multimap iterates over keys in the order they were first encountered in the input,
	 * and the values for each key are iterated in the order they were encountered. If two
	 * values for the same key are {@linkplain Object#equals equal}, the first value
	 * encountered is used.
	 * @throws NullPointerException if any key, value, or entry is null
	 * @since 19.0
	 */

	public static <K, V> ImmutableSetMultimap<K, V> copyOf(
			Iterable<? extends Entry<? extends K, ? extends V>> entries) {
		return new Builder<K, V>().putAll(entries).build();
	}

	/** Creates an ImmutableSetMultimap from an asMap.entrySet. */
	static <K, V> ImmutableSetMultimap<K, V> fromMapEntries(
			Collection<? extends Entry<? extends K, ? extends Collection<? extends V>>> mapEntries,
			@CheckForNull Comparator<? super V> valueComparator) {
		if (mapEntries.isEmpty()) {
			return of();
		}
		org.magneton.core.collect.ImmutableMap.Builder<K, org.magneton.core.collect.ImmutableSet<V>> builder = new org.magneton.core.collect.ImmutableMap.Builder<>(
				mapEntries.size());
		int size = 0;

		for (Entry<? extends K, ? extends Collection<? extends V>> entry : mapEntries) {
			K key = entry.getKey();
			Collection<? extends V> values = entry.getValue();
			org.magneton.core.collect.ImmutableSet<V> set = valueSet(valueComparator, values);
			if (!set.isEmpty()) {
				builder.put(key, set);
				size += set.size();
			}
		}

		return new ImmutableSetMultimap<>(builder.build(), size, valueComparator);
	}

	private static <V> org.magneton.core.collect.ImmutableSet<V> valueSet(
			@CheckForNull Comparator<? super V> valueComparator, Collection<? extends V> values) {
		return (valueComparator == null) ? org.magneton.core.collect.ImmutableSet.copyOf(values)
				: org.magneton.core.collect.ImmutableSortedSet.copyOf(valueComparator, values);
	}

	private static <V> org.magneton.core.collect.ImmutableSet<V> emptySet(
			@CheckForNull Comparator<? super V> valueComparator) {
		return (valueComparator == null) ? org.magneton.core.collect.ImmutableSet.<V>of()
				: org.magneton.core.collect.ImmutableSortedSet.<V>emptySet(valueComparator);
	}

	private static <V> org.magneton.core.collect.ImmutableSet.Builder<V> valuesBuilder(
			@CheckForNull Comparator<? super V> valueComparator) {
		return (valueComparator == null) ? new org.magneton.core.collect.ImmutableSet.Builder<V>()
				: new org.magneton.core.collect.ImmutableSortedSet.Builder<V>(valueComparator);
	}

	/**
	 * Returns an immutable set of the values for the given key. If no mappings in the
	 * multimap have the provided key, an empty immutable set is returned. The values are
	 * in the same order as the parameters used to build this multimap.
	 */
	@Override
	public org.magneton.core.collect.ImmutableSet<V> get(K key) {
		// This cast is safe as its type is known in constructor.
		org.magneton.core.collect.ImmutableSet<V> set = (org.magneton.core.collect.ImmutableSet<V>) map.get(key);
		return MoreObjects.firstNonNull(set, emptySet);
	}

	/**
	 * {@inheritDoc}
	 *
	 * <p>
	 * Because an inverse of a set multimap cannot contain multiple pairs with the same
	 * key and value, this method returns an {@code ImmutableSetMultimap} rather than the
	 * {@code
	 * ImmutableMultimap} specified in the {@code ImmutableMultimap} class.
	 */
	@Override
	public ImmutableSetMultimap<V, K> inverse() {
		ImmutableSetMultimap<V, K> result = inverse;
		return (result == null) ? (inverse = invert()) : result;
	}

	private ImmutableSetMultimap<V, K> invert() {
		Builder<V, K> builder = builder();
		for (Entry<K, V> entry : entries()) {
			builder.put(entry.getValue(), entry.getKey());
		}
		ImmutableSetMultimap<V, K> invertedMultimap = builder.build();
		invertedMultimap.inverse = this;
		return invertedMultimap;
	}

	/**
	 * Guaranteed to throw an exception and leave the multimap unmodified.
	 * @throws UnsupportedOperationException always
	 * @deprecated Unsupported operation.
	 */
	@CanIgnoreReturnValue
	@Deprecated
	@Override
	@DoNotCall("Always throws UnsupportedOperationException")
	public final org.magneton.core.collect.ImmutableSet<V> removeAll(@CheckForNull Object key) {
		throw new UnsupportedOperationException();
	}

	/**
	 * Guaranteed to throw an exception and leave the multimap unmodified.
	 * @throws UnsupportedOperationException always
	 * @deprecated Unsupported operation.
	 */
	@CanIgnoreReturnValue
	@Deprecated
	@Override
	@DoNotCall("Always throws UnsupportedOperationException")
	public final org.magneton.core.collect.ImmutableSet<V> replaceValues(K key, Iterable<? extends V> values) {
		throw new UnsupportedOperationException();
	}

	/**
	 * Returns an immutable collection of all key-value pairs in the multimap. Its
	 * iterator traverses the values for the first key, the values for the second key, and
	 * so on.
	 */
	@Override
	public org.magneton.core.collect.ImmutableSet<Entry<K, V>> entries() {
		org.magneton.core.collect.ImmutableSet<Entry<K, V>> result = entries;
		return result == null ? (entries = new EntrySet<>(this)) : result;
	}

	/**
	 * @serialData number of distinct keys, and then for each distinct key: the key, the
	 * number of values for that key, and the key's values
	 */

	private void writeObject(ObjectOutputStream stream) throws IOException {
		stream.defaultWriteObject();
		stream.writeObject(valueComparator());
		org.magneton.core.collect.Serialization.writeMultimap(this, stream);
	}

	@CheckForNull
	Comparator<? super V> valueComparator() {
		return emptySet instanceof org.magneton.core.collect.ImmutableSortedSet
				? ((org.magneton.core.collect.ImmutableSortedSet<V>) emptySet).comparator() : null;
	}

	// Serialization type safety is at the caller's mercy.
	private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
		stream.defaultReadObject();
		Comparator<Object> valueComparator = (Comparator<Object>) stream.readObject();
		int keyCount = stream.readInt();
		if (keyCount < 0) {
			throw new InvalidObjectException("Invalid key count " + keyCount);
		}
		org.magneton.core.collect.ImmutableMap.Builder<Object, org.magneton.core.collect.ImmutableSet<Object>> builder = org.magneton.core.collect.ImmutableMap
				.builder();
		int tmpSize = 0;

		for (int i = 0; i < keyCount; i++) {
			Object key = stream.readObject();
			int valueCount = stream.readInt();
			if (valueCount <= 0) {
				throw new InvalidObjectException("Invalid value count " + valueCount);
			}

			org.magneton.core.collect.ImmutableSet.Builder<Object> valuesBuilder = valuesBuilder(valueComparator);
			for (int j = 0; j < valueCount; j++) {
				valuesBuilder.add(stream.readObject());
			}
			org.magneton.core.collect.ImmutableSet<Object> valueSet = valuesBuilder.build();
			if (valueSet.size() != valueCount) {
				throw new InvalidObjectException("Duplicate key-value pairs exist for key " + key);
			}
			builder.put(key, valueSet);
			tmpSize += valueCount;
		}

		ImmutableMap<Object, ImmutableSet<Object>> tmpMap;
		try {
			tmpMap = builder.build();
		}
		catch (IllegalArgumentException e) {
			throw (InvalidObjectException) new InvalidObjectException(e.getMessage()).initCause(e);
		}

		FieldSettersHolder.MAP_FIELD_SETTER.set(this, tmpMap);
		FieldSettersHolder.SIZE_FIELD_SETTER.set(this, tmpSize);
		SetFieldSettersHolder.EMPTY_SET_FIELD_SETTER.set(this, emptySet(valueComparator));
	}

	/**
	 * A builder for creating immutable {@code SetMultimap} instances, especially
	 * {@code public static
	 * final} multimaps ("constant multimaps"). Example:
	 *
	 * <pre>{@code
	 * static final Multimap<String, Integer> STRING_TO_INTEGER_MULTIMAP =
	 *     new ImmutableSetMultimap.Builder<String, Integer>()
	 *         .put("one", 1)
	 *         .putAll("several", 1, 2, 3)
	 *         .putAll("many", 1, 2, 3, 4, 5)
	 *         .build();
	 * }</pre>
	 *
	 * <p>
	 * Builder instances can be reused; it is safe to call {@link #build} multiple times
	 * to build multiple multimaps in series. Each multimap contains the key-value
	 * mappings in the previously created multimaps.
	 *
	 * @since 2.0
	 */
	public static final class Builder<K, V> extends org.magneton.core.collect.ImmutableMultimap.Builder<K, V> {

		/**
		 * Creates a new builder. The returned builder is equivalent to the builder
		 * generated by {@link ImmutableSetMultimap#builder}.
		 */
		public Builder() {
			super();
		}

		@Override
		Collection<V> newMutableValueCollection() {
			return Platform.preservesInsertionOrderOnAddsSet();
		}

		/**
		 * Adds a key-value mapping to the built multimap if it is not already present.
		 */
		@CanIgnoreReturnValue
		@Override
		public Builder<K, V> put(K key, V value) {
			super.put(key, value);
			return this;
		}

		/**
		 * Adds an entry to the built multimap if it is not already present.
		 *
		 * @since 11.0
		 */
		@CanIgnoreReturnValue
		@Override
		public Builder<K, V> put(Entry<? extends K, ? extends V> entry) {
			super.put(entry);
			return this;
		}

		/**
		 * {@inheritDoc}
		 *
		 * @since 19.0
		 */
		@CanIgnoreReturnValue

		@Override
		public Builder<K, V> putAll(Iterable<? extends Entry<? extends K, ? extends V>> entries) {
			super.putAll(entries);
			return this;
		}

		@CanIgnoreReturnValue
		@Override
		public Builder<K, V> putAll(K key, Iterable<? extends V> values) {
			super.putAll(key, values);
			return this;
		}

		@CanIgnoreReturnValue
		@Override
		public Builder<K, V> putAll(K key, V... values) {
			return putAll(key, Arrays.asList(values));
		}

		@CanIgnoreReturnValue
		@Override
		public Builder<K, V> putAll(Multimap<? extends K, ? extends V> multimap) {
			for (Entry<? extends K, ? extends Collection<? extends V>> entry : multimap.asMap().entrySet()) {
				putAll(entry.getKey(), entry.getValue());
			}
			return this;
		}

		@CanIgnoreReturnValue
		@Override
		Builder<K, V> combine(ImmutableMultimap.Builder<K, V> other) {
			super.combine(other);
			return this;
		}

		/**
		 * {@inheritDoc}
		 *
		 * @since 8.0
		 */
		@CanIgnoreReturnValue
		@Override
		public Builder<K, V> orderKeysBy(Comparator<? super K> keyComparator) {
			super.orderKeysBy(keyComparator);
			return this;
		}

		/**
		 * Specifies the ordering of the generated multimap's values for each key.
		 *
		 * <p>
		 * If this method is called, the sets returned by the {@code get()} method of the
		 * generated multimap and its {@link Multimap#asMap()} view are
		 * {@link ImmutableSortedSet} instances. However, serialization does not preserve
		 * that property, though it does maintain the key and value ordering.
		 *
		 * @since 8.0
		 */
		// TODO: Make serialization behavior consistent.
		@CanIgnoreReturnValue
		@Override
		public Builder<K, V> orderValuesBy(Comparator<? super V> valueComparator) {
			super.orderValuesBy(valueComparator);
			return this;
		}

		/** Returns a newly-created immutable set multimap. */
		@Override
		public ImmutableSetMultimap<K, V> build() {
			Collection<Entry<K, Collection<V>>> mapEntries = builderMap.entrySet();
			if (keyComparator != null) {
				mapEntries = Ordering.from(keyComparator).<K>onKeys().immutableSortedCopy(mapEntries);
			}
			return fromMapEntries(mapEntries, valueComparator);
		}

	}

	private static final class EntrySet<K, V> extends ImmutableSet<Entry<K, V>> {

		@Weak
		private final transient ImmutableSetMultimap<K, V> multimap;

		EntrySet(ImmutableSetMultimap<K, V> multimap) {
			this.multimap = multimap;
		}

		@Override
		public boolean contains(@CheckForNull Object object) {
			if (object instanceof Entry) {
				Entry<?, ?> entry = (Entry<?, ?>) object;
				return multimap.containsEntry(entry.getKey(), entry.getValue());
			}
			return false;
		}

		@Override
		public int size() {
			return multimap.size();
		}

		@Override
		public UnmodifiableIterator<Entry<K, V>> iterator() {
			return multimap.entryIterator();
		}

		@Override
		boolean isPartialView() {
			return false;
		}

	}

	private static final class SetFieldSettersHolder {

		static final org.magneton.core.collect.Serialization.FieldSetter<ImmutableSetMultimap> EMPTY_SET_FIELD_SETTER = org.magneton.core.collect.Serialization
				.getFieldSetter(ImmutableSetMultimap.class, "emptySet");

	}

}