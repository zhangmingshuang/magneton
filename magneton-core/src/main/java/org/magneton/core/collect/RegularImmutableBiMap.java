/*
 * Copyright (C) 2008 The Guava Authors
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
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import javax.annotation.CheckForNull;
import javax.annotation.Nullable;
import javax.annotation.VisibleForTesting;
import javax.annotation.concurrent.LazyInit;

import org.magneton.core.base.Preconditions;
import org.magneton.core.collect.ImmutableMapEntry.NonTerminalImmutableBiMapEntry;
import org.magneton.core.collect.RegularImmutableMap.BucketOverflowException;

import static java.util.Objects.requireNonNull;
import static org.magneton.core.collect.CollectPreconditions.checkEntryNotNull;
import static org.magneton.core.collect.ImmutableMapEntry.createEntryArray;
import static org.magneton.core.collect.RegularImmutableMap.MAX_HASH_BUCKET_LENGTH;
import static org.magneton.core.collect.RegularImmutableMap.checkNoConflictInKeyBucket;

/**
 * Bimap with zero or more mappings.
 *
 * @author Louis Wasserman
 */

// uses writeReplace(), not default serialization
@ElementTypesAreNonnullByDefault
class RegularImmutableBiMap<K, V> extends org.magneton.core.collect.ImmutableBiMap<K, V> {

	static final RegularImmutableBiMap<Object, Object> EMPTY = new RegularImmutableBiMap<>(null, null,
			(Entry<Object, Object>[]) org.magneton.core.collect.ImmutableMap.EMPTY_ENTRY_ARRAY, 0, 0);

	static final double MAX_LOAD_FACTOR = 1.2;

	@VisibleForTesting
	final transient Entry<K, V>[] entries;

	@CheckForNull
	private final transient @Nullable org.magneton.core.collect.ImmutableMapEntry<K, V>[] keyTable;

	@CheckForNull
	private final transient @Nullable org.magneton.core.collect.ImmutableMapEntry<K, V>[] valueTable;

	private final transient int mask;

	private final transient int hashCode;

	@LazyInit
	@CheckForNull
	private transient org.magneton.core.collect.ImmutableBiMap<V, K> inverse;

	private RegularImmutableBiMap(@CheckForNull @Nullable org.magneton.core.collect.ImmutableMapEntry<K, V>[] keyTable,
			@CheckForNull @Nullable org.magneton.core.collect.ImmutableMapEntry<K, V>[] valueTable,
			Entry<K, V>[] entries, int mask, int hashCode) {
		this.keyTable = keyTable;
		this.valueTable = valueTable;
		this.entries = entries;
		this.mask = mask;
		this.hashCode = hashCode;
	}

	static <K, V> org.magneton.core.collect.ImmutableBiMap<K, V> fromEntries(Entry<K, V>... entries) {
		return fromEntryArray(entries.length, entries);
	}

	// checkNoConflictInKeyBucket is static imported from RegularImmutableMap

	static <K, V> org.magneton.core.collect.ImmutableBiMap<K, V> fromEntryArray(int n,
			@Nullable Entry<K, V>[] entryArray) {
		Preconditions.checkPositionIndex(n, entryArray.length);
		int tableSize = Hashing.closedTableSize(n, MAX_LOAD_FACTOR);
		int mask = tableSize - 1;
		@Nullable
		org.magneton.core.collect.ImmutableMapEntry<K, V>[] keyTable = createEntryArray(tableSize);
		@Nullable
		org.magneton.core.collect.ImmutableMapEntry<K, V>[] valueTable = createEntryArray(tableSize);
		/*
		 * The cast is safe: n==entryArray.length means that we have filled the whole
		 * array with Entry instances, in which case it is safe to cast it from an array
		 * of nullable entries to an array of non-null entries.
		 */
		Entry<K, V>[] entries = (n == entryArray.length) ? (Entry<K, V>[]) entryArray : createEntryArray(n);
		int hashCode = 0;

		for (int i = 0; i < n; i++) {
			// requireNonNull is safe because the first `n` elements have been filled in.
			Entry<K, V> entry = requireNonNull(entryArray[i]);
			K key = entry.getKey();
			V value = entry.getValue();
			checkEntryNotNull(key, value);
			int keyHash = key.hashCode();
			int valueHash = value.hashCode();
			int keyBucket = Hashing.smear(keyHash) & mask;
			int valueBucket = Hashing.smear(valueHash) & mask;

			org.magneton.core.collect.ImmutableMapEntry<K, V> nextInKeyBucket = keyTable[keyBucket];
			org.magneton.core.collect.ImmutableMapEntry<K, V> nextInValueBucket = valueTable[valueBucket];
			try {
				checkNoConflictInKeyBucket(key, value, nextInKeyBucket,
						/* throwIfDuplicateKeys= */ true);
				checkNoConflictInValueBucket(value, entry, nextInValueBucket);
			}
			catch (BucketOverflowException e) {
				return JdkBackedImmutableBiMap.create(n, entryArray);
			}
			org.magneton.core.collect.ImmutableMapEntry<K, V> newEntry = (nextInValueBucket == null
					&& nextInKeyBucket == null)
							? org.magneton.core.collect.RegularImmutableMap.makeImmutable(entry, key, value)
							: new NonTerminalImmutableBiMapEntry<>(key, value, nextInKeyBucket, nextInValueBucket);
			keyTable[keyBucket] = newEntry;
			valueTable[valueBucket] = newEntry;
			entries[i] = newEntry;
			hashCode += keyHash ^ valueHash;
		}
		return new RegularImmutableBiMap<>(keyTable, valueTable, entries, mask, hashCode);
	}

	/**
	 * @throws IllegalArgumentException if another entry in the bucket has the same key
	 * @throws BucketOverflowException if this bucket has too many entries, which may
	 * indicate a hash flooding attack
	 */
	private static void checkNoConflictInValueBucket(Object value, Entry<?, ?> entry,
			@CheckForNull org.magneton.core.collect.ImmutableMapEntry<?, ?> valueBucketHead)
			throws BucketOverflowException {
		int bucketSize = 0;
		for (; valueBucketHead != null; valueBucketHead = valueBucketHead.getNextInValueBucket()) {
			checkNoConflict(!value.equals(valueBucketHead.getValue()), "value", entry, valueBucketHead);
			if (++bucketSize > MAX_HASH_BUCKET_LENGTH) {
				throw new BucketOverflowException();
			}
		}
	}

	@Override
	@CheckForNull
	public V get(@CheckForNull Object key) {
		return org.magneton.core.collect.RegularImmutableMap.get(key, keyTable, mask);
	}

	@Override
	org.magneton.core.collect.ImmutableSet<Entry<K, V>> createEntrySet() {
		return isEmpty() ? org.magneton.core.collect.ImmutableSet.<Entry<K, V>>of()
				: new org.magneton.core.collect.ImmutableMapEntrySet.RegularEntrySet<K, V>(this, entries);
	}

	@Override
	org.magneton.core.collect.ImmutableSet<K> createKeySet() {
		return new org.magneton.core.collect.ImmutableMapKeySet<>(this);
	}

	@Override
	public void forEach(BiConsumer<? super K, ? super V> action) {
		Preconditions.checkNotNull(action);
		for (Entry<K, V> entry : entries) {
			action.accept(entry.getKey(), entry.getValue());
		}
	}

	@Override
	boolean isHashCodeFast() {
		return true;
	}

	@Override
	public int hashCode() {
		return hashCode;
	}

	@Override
	boolean isPartialView() {
		return false;
	}

	@Override
	public int size() {
		return entries.length;
	}

	@Override
	public org.magneton.core.collect.ImmutableBiMap<V, K> inverse() {
		if (isEmpty()) {
			return org.magneton.core.collect.ImmutableBiMap.of();
		}
		org.magneton.core.collect.ImmutableBiMap<V, K> result = inverse;
		return (result == null) ? inverse = new Inverse() : result;
	}

	private static class InverseSerializedForm<K, V> implements Serializable {

		private static final long serialVersionUID = 1;

		private final org.magneton.core.collect.ImmutableBiMap<K, V> forward;

		InverseSerializedForm(ImmutableBiMap<K, V> forward) {
			this.forward = forward;
		}

		Object readResolve() {
			return forward.inverse();
		}

	}

	private final class Inverse extends org.magneton.core.collect.ImmutableBiMap<V, K> {

		@Override
		public int size() {
			return inverse().size();
		}

		@Override
		public org.magneton.core.collect.ImmutableBiMap<K, V> inverse() {
			return RegularImmutableBiMap.this;
		}

		@Override
		public void forEach(BiConsumer<? super V, ? super K> action) {
			Preconditions.checkNotNull(action);
			RegularImmutableBiMap.this.forEach((k, v) -> action.accept(v, k));
		}

		@Override
		@CheckForNull
		public K get(@CheckForNull Object value) {
			if (value == null || valueTable == null) {
				return null;
			}
			int bucket = Hashing.smear(value.hashCode()) & mask;
			for (org.magneton.core.collect.ImmutableMapEntry<K, V> entry = valueTable[bucket]; entry != null; entry = entry
					.getNextInValueBucket()) {
				if (value.equals(entry.getValue())) {
					return entry.getKey();
				}
			}
			return null;
		}

		@Override
		org.magneton.core.collect.ImmutableSet<V> createKeySet() {
			return new org.magneton.core.collect.ImmutableMapKeySet<>(this);
		}

		@Override
		ImmutableSet<Entry<V, K>> createEntrySet() {
			return new InverseEntrySet();
		}

		@Override
		boolean isPartialView() {
			return false;
		}

		@Override
		Object writeReplace() {
			return new InverseSerializedForm<>(RegularImmutableBiMap.this);
		}

		final class InverseEntrySet extends org.magneton.core.collect.ImmutableMapEntrySet<V, K> {

			@Override
			ImmutableMap<V, K> map() {
				return Inverse.this;
			}

			@Override
			boolean isHashCodeFast() {
				return true;
			}

			@Override
			public int hashCode() {
				return hashCode;
			}

			@Override
			public UnmodifiableIterator<Entry<V, K>> iterator() {
				return asList().iterator();
			}

			@Override
			public void forEach(Consumer<? super Entry<V, K>> action) {
				asList().forEach(action);
			}

			@Override
			ImmutableList<Entry<V, K>> createAsList() {
				return new org.magneton.core.collect.ImmutableAsList<Entry<V, K>>() {
					@Override
					public Entry<V, K> get(int index) {
						Entry<K, V> entry = entries[index];
						return Maps.immutableEntry(entry.getValue(), entry.getKey());
					}

					@Override
					ImmutableCollection<Entry<V, K>> delegateCollection() {
						return InverseEntrySet.this;
					}
				};
			}

		}

	}

}
