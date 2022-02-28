/*
 * Copyright (C) 2007 The Guava Authors
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

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Arrays;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;

import javax.annotation.CheckForNull;
import javax.annotation.Nullable;
import javax.annotations.CanIgnoreReturnValue;
import javax.annotations.LazyInit;
import javax.annotations.Weak;

import org.magneton.core.base.Objects;
import org.magneton.core.base.Preconditions;

import static java.util.Objects.requireNonNull;

/**
 * A {@link BiMap} backed by two hash tables. This implementation allows null keys and
 * values. A {@code HashBiMap} and its inverse are both serializable.
 *
 * <p>
 * This implementation guarantees insertion-based iteration order of its keys.
 *
 * <p>
 * See the Guava User Guide article on
 * <a href= "https://github.com/google/guava/wiki/NewCollectionTypesExplained#bimap">
 * {@code BiMap} </a>.
 *
 * @author Louis Wasserman
 * @author Mike Bostock
 * @since 2.0
 */
@ElementTypesAreNonnullByDefault
public final class HashBiMap<K extends Object, V extends Object> extends Maps.IteratorBasedAbstractMap<K, V>
		implements BiMap<K, V>, Serializable {

	private static final double LOAD_FACTOR = 1.0;

	private static final long serialVersionUID = 0;

	/*
	 * The following two arrays may *contain* nulls, but they are never *themselves* null:
	 * Even though they are not initialized inline in the constructor, they are
	 * initialized from init(), which the constructor calls (as does readObject()).
	 */
	private transient @Nullable BiEntry<K, V>[] hashTableKToV;

	private transient @Nullable BiEntry<K, V>[] hashTableVToK;

	@Weak
	@CheckForNull
	private transient BiEntry<K, V> firstInKeyInsertionOrder;

	@Weak
	@CheckForNull
	private transient BiEntry<K, V> lastInKeyInsertionOrder;

	private transient int size;

	private transient int mask;

	private transient int modCount;

	@LazyInit
	@CheckForNull
	private transient BiMap<V, K> inverse;

	private HashBiMap(int expectedSize) {
		this.init(expectedSize);
	}

	/** Returns a new, empty {@code HashBiMap} with the default initial capacity (16). */
	public static <K extends Object, V extends Object> HashBiMap<K, V> create() {
		return create(16);
	}

	/**
	 * Constructs a new, empty bimap with the specified expected size.
	 * @param expectedSize the expected number of entries
	 * @throws IllegalArgumentException if the specified expected size is negative
	 */
	public static <K extends Object, V extends Object> HashBiMap<K, V> create(int expectedSize) {
		return new HashBiMap<>(expectedSize);
	}

	/**
	 * Constructs a new bimap containing initial values from {@code map}. The bimap is
	 * created with an initial capacity sufficient to hold the mappings in the specified
	 * map.
	 */
	public static <K extends Object, V extends Object> HashBiMap<K, V> create(Map<? extends K, ? extends V> map) {
		HashBiMap<K, V> bimap = create(map.size());
		bimap.putAll(map);
		return bimap;
	}

	private void init(int expectedSize) {
		CollectPreconditions.checkNonnegative(expectedSize, "expectedSize");
		int tableSize = Hashing.closedTableSize(expectedSize, LOAD_FACTOR);
		this.hashTableKToV = this.createTable(tableSize);
		this.hashTableVToK = this.createTable(tableSize);
		this.firstInKeyInsertionOrder = null;
		this.lastInKeyInsertionOrder = null;
		this.size = 0;
		this.mask = tableSize - 1;
		this.modCount = 0;
	}

	/**
	 * Finds and removes {@code entry} from the bucket linked lists in both the
	 * key-to-value direction and the value-to-key direction.
	 */
	private void delete(BiEntry<K, V> entry) {
		int keyBucket = entry.keyHash & this.mask;
		BiEntry<K, V> prevBucketEntry = null;
		for (BiEntry<K, V> bucketEntry = this.hashTableKToV[keyBucket]; true; bucketEntry = bucketEntry.nextInKToVBucket) {
			if (bucketEntry == entry) {
				if (prevBucketEntry == null) {
					this.hashTableKToV[keyBucket] = entry.nextInKToVBucket;
				}
				else {
					prevBucketEntry.nextInKToVBucket = entry.nextInKToVBucket;
				}
				break;
			}
			prevBucketEntry = bucketEntry;
		}

		int valueBucket = entry.valueHash & this.mask;
		prevBucketEntry = null;
		for (BiEntry<K, V> bucketEntry = this.hashTableVToK[valueBucket]; true; bucketEntry = bucketEntry.nextInVToKBucket) {
			if (bucketEntry == entry) {
				if (prevBucketEntry == null) {
					this.hashTableVToK[valueBucket] = entry.nextInVToKBucket;
				}
				else {
					prevBucketEntry.nextInVToKBucket = entry.nextInVToKBucket;
				}
				break;
			}
			prevBucketEntry = bucketEntry;
		}

		if (entry.prevInKeyInsertionOrder == null) {
			this.firstInKeyInsertionOrder = entry.nextInKeyInsertionOrder;
		}
		else {
			entry.prevInKeyInsertionOrder.nextInKeyInsertionOrder = entry.nextInKeyInsertionOrder;
		}

		if (entry.nextInKeyInsertionOrder == null) {
			this.lastInKeyInsertionOrder = entry.prevInKeyInsertionOrder;
		}
		else {
			entry.nextInKeyInsertionOrder.prevInKeyInsertionOrder = entry.prevInKeyInsertionOrder;
		}

		this.size--;
		this.modCount++;
	}

	private void insert(BiEntry<K, V> entry, @CheckForNull BiEntry<K, V> oldEntryForKey) {
		int keyBucket = entry.keyHash & this.mask;
		entry.nextInKToVBucket = this.hashTableKToV[keyBucket];
		this.hashTableKToV[keyBucket] = entry;

		int valueBucket = entry.valueHash & this.mask;
		entry.nextInVToKBucket = this.hashTableVToK[valueBucket];
		this.hashTableVToK[valueBucket] = entry;

		if (oldEntryForKey == null) {
			entry.prevInKeyInsertionOrder = this.lastInKeyInsertionOrder;
			entry.nextInKeyInsertionOrder = null;
			if (this.lastInKeyInsertionOrder == null) {
				this.firstInKeyInsertionOrder = entry;
			}
			else {
				this.lastInKeyInsertionOrder.nextInKeyInsertionOrder = entry;
			}
			this.lastInKeyInsertionOrder = entry;
		}
		else {
			entry.prevInKeyInsertionOrder = oldEntryForKey.prevInKeyInsertionOrder;
			if (entry.prevInKeyInsertionOrder == null) {
				this.firstInKeyInsertionOrder = entry;
			}
			else {
				entry.prevInKeyInsertionOrder.nextInKeyInsertionOrder = entry;
			}
			entry.nextInKeyInsertionOrder = oldEntryForKey.nextInKeyInsertionOrder;
			if (entry.nextInKeyInsertionOrder == null) {
				this.lastInKeyInsertionOrder = entry;
			}
			else {
				entry.nextInKeyInsertionOrder.prevInKeyInsertionOrder = entry;
			}
		}

		this.size++;
		this.modCount++;
	}

	@CheckForNull
	private BiEntry<K, V> seekByKey(@CheckForNull Object key, int keyHash) {
		for (BiEntry<K, V> entry = this.hashTableKToV[keyHash
				& this.mask]; entry != null; entry = entry.nextInKToVBucket) {
			if (keyHash == entry.keyHash && org.magneton.core.base.Objects.equal(key, entry.key)) {
				return entry;
			}
		}
		return null;
	}

	@CheckForNull
	private BiEntry<K, V> seekByValue(@CheckForNull Object value, int valueHash) {
		for (BiEntry<K, V> entry = this.hashTableVToK[valueHash
				& this.mask]; entry != null; entry = entry.nextInVToKBucket) {
			if (valueHash == entry.valueHash && org.magneton.core.base.Objects.equal(value, entry.value)) {
				return entry;
			}
		}
		return null;
	}

	@Override
	public boolean containsKey(@CheckForNull Object key) {
		return this.seekByKey(key, Hashing.smearedHash(key)) != null;
	}

	/**
	 * Returns {@code true} if this BiMap contains an entry whose value is equal to
	 * {@code value} (or, equivalently, if this inverse view contains a key that is equal
	 * to {@code value}).
	 *
	 * <p>
	 * Due to the property that values in a BiMap are unique, this will tend to execute in
	 * faster-than-linear time.
	 * @param value the object to search for in the values of this BiMap
	 * @return true if a mapping exists from a key to the specified value
	 */
	@Override
	public boolean containsValue(@CheckForNull Object value) {
		return this.seekByValue(value, Hashing.smearedHash(value)) != null;
	}

	@Override
	@CheckForNull
	public V get(@CheckForNull Object key) {
		return Maps.valueOrNull(this.seekByKey(key, Hashing.smearedHash(key)));
	}

	@CanIgnoreReturnValue
	@Override
	@CheckForNull
	public V put(@ParametricNullness K key, @ParametricNullness V value) {
		return this.put(key, value, false);
	}

	@CheckForNull
	private V put(@ParametricNullness K key, @ParametricNullness V value, boolean force) {
		int keyHash = Hashing.smearedHash(key);
		int valueHash = Hashing.smearedHash(value);

		BiEntry<K, V> oldEntryForKey = this.seekByKey(key, keyHash);
		if (oldEntryForKey != null && valueHash == oldEntryForKey.valueHash
				&& org.magneton.core.base.Objects.equal(value, oldEntryForKey.value)) {
			return value;
		}

		BiEntry<K, V> oldEntryForValue = this.seekByValue(value, valueHash);
		if (oldEntryForValue != null) {
			if (force) {
				this.delete(oldEntryForValue);
			}
			else {
				throw new IllegalArgumentException("value already present: " + value);
			}
		}

		BiEntry<K, V> newEntry = new BiEntry<>(key, keyHash, value, valueHash);
		if (oldEntryForKey != null) {
			this.delete(oldEntryForKey);
			this.insert(newEntry, oldEntryForKey);
			oldEntryForKey.prevInKeyInsertionOrder = null;
			oldEntryForKey.nextInKeyInsertionOrder = null;
			return oldEntryForKey.value;
		}
		else {
			this.insert(newEntry, null);
			this.rehashIfNecessary();
			return null;
		}
	}

	@CanIgnoreReturnValue
	@Override
	@CheckForNull
	public V forcePut(@ParametricNullness K key, @ParametricNullness V value) {
		return this.put(key, value, true);
	}

	@CheckForNull
	private K putInverse(@ParametricNullness V value, @ParametricNullness K key, boolean force) {
		int valueHash = Hashing.smearedHash(value);
		int keyHash = Hashing.smearedHash(key);

		BiEntry<K, V> oldEntryForValue = this.seekByValue(value, valueHash);
		BiEntry<K, V> oldEntryForKey = this.seekByKey(key, keyHash);
		if (oldEntryForValue != null && keyHash == oldEntryForValue.keyHash
				&& org.magneton.core.base.Objects.equal(key, oldEntryForValue.key)) {
			return key;
		}
		else if (oldEntryForKey != null && !force) {
			throw new IllegalArgumentException("key already present: " + key);
		}

		/*
		 * The ordering here is important: if we deleted the key entry and then the value
		 * entry, the key entry's prev or next pointer might point to the dead value
		 * entry, and when we put the new entry in the key entry's position in iteration
		 * order, it might invalidate the linked list.
		 */

		if (oldEntryForValue != null) {
			this.delete(oldEntryForValue);
		}

		if (oldEntryForKey != null) {
			this.delete(oldEntryForKey);
		}

		BiEntry<K, V> newEntry = new BiEntry<>(key, keyHash, value, valueHash);
		this.insert(newEntry, oldEntryForKey);

		if (oldEntryForKey != null) {
			oldEntryForKey.prevInKeyInsertionOrder = null;
			oldEntryForKey.nextInKeyInsertionOrder = null;
		}
		if (oldEntryForValue != null) {
			oldEntryForValue.prevInKeyInsertionOrder = null;
			oldEntryForValue.nextInKeyInsertionOrder = null;
		}
		this.rehashIfNecessary();
		return Maps.keyOrNull(oldEntryForValue);
	}

	private void rehashIfNecessary() {
		@Nullable
		BiEntry<K, V>[] oldKToV = this.hashTableKToV;
		if (Hashing.needsResizing(this.size, oldKToV.length, LOAD_FACTOR)) {
			int newTableSize = oldKToV.length * 2;

			this.hashTableKToV = this.createTable(newTableSize);
			this.hashTableVToK = this.createTable(newTableSize);
			this.mask = newTableSize - 1;
			this.size = 0;

			for (BiEntry<K, V> entry = this.firstInKeyInsertionOrder; entry != null; entry = entry.nextInKeyInsertionOrder) {
				this.insert(entry, entry);
			}
			this.modCount++;
		}
	}

	private BiEntry<K, V>[] createTable(int length) {
		return new BiEntry[length];
	}

	@CanIgnoreReturnValue
	@Override
	@CheckForNull
	public V remove(@CheckForNull Object key) {
		BiEntry<K, V> entry = this.seekByKey(key, Hashing.smearedHash(key));
		if (entry == null) {
			return null;
		}
		else {
			this.delete(entry);
			entry.prevInKeyInsertionOrder = null;
			entry.nextInKeyInsertionOrder = null;
			return entry.value;
		}
	}

	@Override
	public void clear() {
		this.size = 0;
		Arrays.fill(this.hashTableKToV, null);
		Arrays.fill(this.hashTableVToK, null);
		this.firstInKeyInsertionOrder = null;
		this.lastInKeyInsertionOrder = null;
		this.modCount++;
	}

	@Override
	public int size() {
		return this.size;
	}

	@Override
	public Set<K> keySet() {
		return new KeySet();
	}

	@Override
	public Set<V> values() {
		return this.inverse().keySet();
	}

	@Override
	Iterator<Entry<K, V>> entryIterator() {
		return new Itr<Entry<K, V>>() {
			@Override
			Entry<K, V> output(BiEntry<K, V> entry) {
				return new MapEntry(entry);
			}

			class MapEntry extends AbstractMapEntry<K, V> {

				BiEntry<K, V> delegate;

				MapEntry(BiEntry<K, V> entry) {
					this.delegate = entry;
				}

				@Override
				public K getKey() {
					return this.delegate.key;
				}

				@Override
				public V getValue() {
					return this.delegate.value;
				}

				@Override
				public V setValue(V value) {
					V oldValue = this.delegate.value;
					int valueHash = Hashing.smearedHash(value);
					if (valueHash == this.delegate.valueHash && org.magneton.core.base.Objects.equal(value, oldValue)) {
						return value;
					}
					Preconditions.checkArgument(HashBiMap.this.seekByValue(value, valueHash) == null,
							"value already present: %s", value);
					HashBiMap.this.delete(this.delegate);
					BiEntry<K, V> newEntry = new BiEntry<>(this.delegate.key, this.delegate.keyHash, value, valueHash);
					HashBiMap.this.insert(newEntry, this.delegate);
					this.delegate.prevInKeyInsertionOrder = null;
					this.delegate.nextInKeyInsertionOrder = null;
					expectedModCount = HashBiMap.this.modCount;
					if (toRemove == this.delegate) {
						toRemove = newEntry;
					}
					this.delegate = newEntry;
					return oldValue;
				}

			}
		};
	}

	@Override
	public void forEach(BiConsumer<? super K, ? super V> action) {
		Preconditions.checkNotNull(action);
		for (BiEntry<K, V> entry = this.firstInKeyInsertionOrder; entry != null; entry = entry.nextInKeyInsertionOrder) {
			action.accept(entry.key, entry.value);
		}
	}

	@Override
	public void replaceAll(BiFunction<? super K, ? super V, ? extends V> function) {
		Preconditions.checkNotNull(function);
		BiEntry<K, V> oldFirst = this.firstInKeyInsertionOrder;
		this.clear();
		for (BiEntry<K, V> entry = oldFirst; entry != null; entry = entry.nextInKeyInsertionOrder) {
			this.put(entry.key, function.apply(entry.key, entry.value));
		}
	}

	@Override
	public BiMap<V, K> inverse() {
		BiMap<V, K> result = this.inverse;
		return (result == null) ? this.inverse = new Inverse() : result;
	}

	/**
	 * @serialData the number of entries, first key, first value, second key, second
	 * value, and so on.
	 */

	private void writeObject(ObjectOutputStream stream) throws IOException {
		stream.defaultWriteObject();
		Serialization.writeMap(this, stream);
	}

	private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
		stream.defaultReadObject();
		int size = Serialization.readCount(stream);
		this.init(16); // resist hostile attempts to allocate gratuitous heap
		Serialization.populateMap(this, stream, size);
	}

	private static final class BiEntry<K extends Object, V extends Object> extends ImmutableEntry<K, V> {

		final int keyHash;

		final int valueHash;

		// All BiEntry instances are strongly reachable from owning HashBiMap through
		// "HashBiMap.hashTableKToV" and "BiEntry.nextInKToVBucket" references.
		// Under that assumption, the remaining references can be safely marked as @Weak.
		// Using @Weak is necessary to avoid retain-cycles between BiEntry instances on
		// iOS,
		// which would cause memory leaks when non-empty HashBiMap with cyclic BiEntry
		// instances is deallocated.
		@CheckForNull
		BiEntry<K, V> nextInKToVBucket;

		@Weak
		@CheckForNull
		BiEntry<K, V> nextInVToKBucket;

		@Weak
		@CheckForNull
		BiEntry<K, V> nextInKeyInsertionOrder;

		@Weak
		@CheckForNull
		BiEntry<K, V> prevInKeyInsertionOrder;

		BiEntry(@ParametricNullness K key, int keyHash, @ParametricNullness V value, int valueHash) {
			super(key, value);
			this.keyHash = keyHash;
			this.valueHash = valueHash;
		}

	}

	private static final class InverseSerializedForm<K extends Object, V extends Object> implements Serializable {

		private final HashBiMap<K, V> bimap;

		InverseSerializedForm(HashBiMap<K, V> bimap) {
			this.bimap = bimap;
		}

		Object readResolve() {
			return this.bimap.inverse();
		}

	}

	abstract class Itr<T> implements Iterator<T> {

		@CheckForNull
		BiEntry<K, V> next = HashBiMap.this.firstInKeyInsertionOrder;

		@CheckForNull
		BiEntry<K, V> toRemove = null;

		int expectedModCount = HashBiMap.this.modCount;

		int remaining = HashBiMap.this.size();

		@Override
		public boolean hasNext() {
			if (HashBiMap.this.modCount != this.expectedModCount) {
				throw new ConcurrentModificationException();
			}
			return this.next != null && this.remaining > 0;
		}

		@Override
		public T next() {
			if (!this.hasNext()) {
				throw new NoSuchElementException();
			}

			// requireNonNull is safe because of the hasNext check.
			BiEntry<K, V> entry = requireNonNull(this.next);
			this.next = entry.nextInKeyInsertionOrder;
			this.toRemove = entry;
			this.remaining--;
			return this.output(entry);
		}

		@Override
		public void remove() {
			if (HashBiMap.this.modCount != this.expectedModCount) {
				throw new ConcurrentModificationException();
			}
			if (this.toRemove == null) {
				throw new IllegalStateException("no calls to next() since the last call to remove()");
			}
			HashBiMap.this.delete(this.toRemove);
			this.expectedModCount = HashBiMap.this.modCount;
			this.toRemove = null;
		}

		abstract T output(BiEntry<K, V> entry);

	}

	private final class KeySet extends Maps.KeySet<K, V> {

		KeySet() {
			super(HashBiMap.this);
		}

		@Override
		public Iterator<K> iterator() {
			return new Itr<K>() {
				@Override
				K output(BiEntry<K, V> entry) {
					return entry.key;
				}
			};
		}

		@Override
		public boolean remove(@CheckForNull Object o) {
			BiEntry<K, V> entry = HashBiMap.this.seekByKey(o, Hashing.smearedHash(o));
			if (entry == null) {
				return false;
			}
			else {
				HashBiMap.this.delete(entry);
				entry.prevInKeyInsertionOrder = null;
				entry.nextInKeyInsertionOrder = null;
				return true;
			}
		}

	}

	private final class Inverse extends Maps.IteratorBasedAbstractMap<V, K> implements BiMap<V, K>, Serializable {

		BiMap<K, V> forward() {
			return HashBiMap.this;
		}

		@Override
		public int size() {
			return HashBiMap.this.size;
		}

		@Override
		public void clear() {
			this.forward().clear();
		}

		@Override
		public boolean containsKey(@CheckForNull Object value) {
			return this.forward().containsValue(value);
		}

		@Override
		@CheckForNull
		public K get(@CheckForNull Object value) {
			return Maps.keyOrNull(HashBiMap.this.seekByValue(value, Hashing.smearedHash(value)));
		}

		@CanIgnoreReturnValue
		@Override
		@CheckForNull
		public K put(@ParametricNullness V value, @ParametricNullness K key) {
			return HashBiMap.this.putInverse(value, key, false);
		}

		@Override
		@CheckForNull
		public K forcePut(@ParametricNullness V value, @ParametricNullness K key) {
			return HashBiMap.this.putInverse(value, key, true);
		}

		@Override
		@CheckForNull
		public K remove(@CheckForNull Object value) {
			BiEntry<K, V> entry = HashBiMap.this.seekByValue(value, Hashing.smearedHash(value));
			if (entry == null) {
				return null;
			}
			else {
				HashBiMap.this.delete(entry);
				entry.prevInKeyInsertionOrder = null;
				entry.nextInKeyInsertionOrder = null;
				return entry.key;
			}
		}

		@Override
		public BiMap<K, V> inverse() {
			return this.forward();
		}

		@Override
		public Set<V> keySet() {
			return new InverseKeySet();
		}

		@Override
		public Set<K> values() {
			return this.forward().keySet();
		}

		@Override
		Iterator<Entry<V, K>> entryIterator() {
			return new Itr<Entry<V, K>>() {
				@Override
				Entry<V, K> output(BiEntry<K, V> entry) {
					return new InverseEntry(entry);
				}

				class InverseEntry extends AbstractMapEntry<V, K> {

					BiEntry<K, V> delegate;

					InverseEntry(BiEntry<K, V> entry) {
						this.delegate = entry;
					}

					@Override
					public V getKey() {
						return this.delegate.value;
					}

					@Override
					public K getValue() {
						return this.delegate.key;
					}

					@Override
					public K setValue(K key) {
						K oldKey = this.delegate.key;
						int keyHash = Hashing.smearedHash(key);
						if (keyHash == this.delegate.keyHash && Objects.equal(key, oldKey)) {
							return key;
						}
						Preconditions.checkArgument(HashBiMap.this.seekByKey(key, keyHash) == null,
								"value already present: %s", key);
						HashBiMap.this.delete(this.delegate);
						BiEntry<K, V> newEntry = new BiEntry<>(key, keyHash, this.delegate.value,
								this.delegate.valueHash);
						this.delegate = newEntry;
						HashBiMap.this.insert(newEntry, null);
						expectedModCount = HashBiMap.this.modCount;
						return oldKey;
					}

				}
			};
		}

		@Override
		public void forEach(BiConsumer<? super V, ? super K> action) {
			Preconditions.checkNotNull(action);
			HashBiMap.this.forEach((k, v) -> action.accept(v, k));
		}

		@Override
		public void replaceAll(BiFunction<? super V, ? super K, ? extends K> function) {
			Preconditions.checkNotNull(function);
			BiEntry<K, V> oldFirst = HashBiMap.this.firstInKeyInsertionOrder;
			this.clear();
			for (BiEntry<K, V> entry = oldFirst; entry != null; entry = entry.nextInKeyInsertionOrder) {
				this.put(entry.value, function.apply(entry.value, entry.key));
			}
		}

		Object writeReplace() {
			return new InverseSerializedForm<>(HashBiMap.this);
		}

		private final class InverseKeySet extends Maps.KeySet<V, K> {

			InverseKeySet() {
				super(Inverse.this);
			}

			@Override
			public boolean remove(@CheckForNull Object o) {
				BiEntry<K, V> entry = HashBiMap.this.seekByValue(o, Hashing.smearedHash(o));
				if (entry == null) {
					return false;
				}
				else {
					HashBiMap.this.delete(entry);
					return true;
				}
			}

			@Override
			public Iterator<V> iterator() {
				return new Itr<V>() {
					@Override
					V output(BiEntry<K, V> entry) {
						return entry.value;
					}
				};
			}

		}

	}

}
