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

import java.util.Arrays;
import java.util.Collection;

import javax.annotation.CheckForNull;
import javax.annotation.LazyInit;
import javax.annotation.Nullable;
import javax.annotation.VisibleForTesting;

import org.magneton.core.base.Objects;
import org.magneton.core.base.Preconditions;
import org.magneton.core.primitives.Ints;

/**
 * Implementation of {@link org.magneton.core.collect.ImmutableMultiset} with zero or more
 * elements.
 *
 * @author Jared Levy
 * @author Louis Wasserman
 */

// uses writeReplace(), not default serialization
@ElementTypesAreNonnullByDefault
class RegularImmutableMultiset<E> extends org.magneton.core.collect.ImmutableMultiset<E> {

	/**
	 * Closed addressing tends to perform well even with high load factors. Being
	 * conservative here ensures that the table is still likely to be relatively sparse
	 * (hence it misses fast) while saving space.
	 */
	@javax.annotation.VisibleForTesting
	static final double MAX_LOAD_FACTOR = 1.0;

	/**
	 * Maximum allowed false positive probability of detecting a hash flooding attack
	 * given random input.
	 */
	@javax.annotation.VisibleForTesting
	static final double HASH_FLOODING_FPP = 0.001;

	/**
	 * Maximum allowed length of a hash table bucket before falling back to a j.u.HashMap
	 * based implementation. Experimentally determined.
	 */
	@VisibleForTesting
	static final int MAX_HASH_BUCKET_LENGTH = 9;

	private static final Multisets.ImmutableEntry<?>[] EMPTY_ARRAY = new Multisets.ImmutableEntry<?>[0];

	static final org.magneton.core.collect.ImmutableMultiset<Object> EMPTY = create(
			org.magneton.core.collect.ImmutableList.<Multiset.Entry<Object>>of());

	private final transient Multisets.ImmutableEntry<E>[] entries;

	private final transient @Nullable Multisets.ImmutableEntry<?>[] hashTable;

	private final transient int size;

	private final transient int hashCode;

	@LazyInit
	@CheckForNull
	private transient org.magneton.core.collect.ImmutableSet<E> elementSet;

	private RegularImmutableMultiset(Multisets.ImmutableEntry<E>[] entries,
			@Nullable Multisets.ImmutableEntry<?>[] hashTable, int size, int hashCode,
			@CheckForNull org.magneton.core.collect.ImmutableSet<E> elementSet) {
		this.entries = entries;
		this.hashTable = hashTable;
		this.size = size;
		this.hashCode = hashCode;
		this.elementSet = elementSet;
	}

	static <E> ImmutableMultiset<E> create(Collection<? extends Multiset.Entry<? extends E>> entries) {
		int distinct = entries.size();
		Multisets.ImmutableEntry<E>[] entryArray = new Multisets.ImmutableEntry[distinct];
		if (distinct == 0) {
			return new RegularImmutableMultiset<>(entryArray, EMPTY_ARRAY, 0, 0,
					org.magneton.core.collect.ImmutableSet.of());
		}
		int tableSize = Hashing.closedTableSize(distinct, MAX_LOAD_FACTOR);
		int mask = tableSize - 1;
		@Nullable
		Multisets.ImmutableEntry<E>[] hashTable = new Multisets.ImmutableEntry[tableSize];

		int index = 0;
		int hashCode = 0;
		long size = 0;
		for (Multiset.Entry<? extends E> entryWithWildcard : entries) {
			// safe because we only read from it
			Multiset.Entry<E> entry = (Multiset.Entry<E>) entryWithWildcard;
			E element = Preconditions.checkNotNull(entry.getElement());
			int count = entry.getCount();
			int hash = element.hashCode();
			int bucket = Hashing.smear(hash) & mask;
			Multisets.ImmutableEntry<E> bucketHead = hashTable[bucket];
			Multisets.ImmutableEntry<E> newEntry;
			if (bucketHead == null) {
				boolean canReuseEntry = entry instanceof Multisets.ImmutableEntry
						&& !(entry instanceof NonTerminalEntry);
				newEntry = canReuseEntry ? (Multisets.ImmutableEntry<E>) entry
						: new Multisets.ImmutableEntry<E>(element, count);
			}
			else {
				newEntry = new NonTerminalEntry<E>(element, count, bucketHead);
			}
			hashCode += hash ^ count;
			entryArray[index++] = newEntry;
			hashTable[bucket] = newEntry;
			size += count;
		}

		return hashFloodingDetected(hashTable)
				? org.magneton.core.collect.JdkBackedImmutableMultiset.create(ImmutableList.asImmutableList(entryArray))
				: new RegularImmutableMultiset<E>(entryArray, hashTable, Ints.saturatedCast(size), hashCode, null);
	}

	private static boolean hashFloodingDetected(@Nullable Multisets.ImmutableEntry<?>[] hashTable) {
		for (int i = 0; i < hashTable.length; i++) {
			int bucketLength = 0;
			for (Multisets.ImmutableEntry<?> entry = hashTable[i]; entry != null; entry = entry.nextInBucket()) {
				bucketLength++;
				if (bucketLength > MAX_HASH_BUCKET_LENGTH) {
					return true;
				}
			}
		}
		return false;
	}

	@Override
	boolean isPartialView() {
		return false;
	}

	@Override
	public int count(@CheckForNull Object element) {
		@Nullable
		Multisets.ImmutableEntry<?>[] hashTable = this.hashTable;
		if (element == null || hashTable.length == 0) {
			return 0;
		}
		int hash = Hashing.smearedHash(element);
		int mask = hashTable.length - 1;
		for (Multisets.ImmutableEntry<?> entry = hashTable[hash & mask]; entry != null; entry = entry.nextInBucket()) {
			if (Objects.equal(element, entry.getElement())) {
				return entry.getCount();
			}
		}
		return 0;
	}

	@Override
	public int size() {
		return size;
	}

	@Override
	public org.magneton.core.collect.ImmutableSet<E> elementSet() {
		ImmutableSet<E> result = elementSet;
		return (result == null) ? elementSet = new ElementSet<E>(Arrays.asList(entries), this) : result;
	}

	@Override
	Multiset.Entry<E> getEntry(int index) {
		return entries[index];
	}

	@Override
	public int hashCode() {
		return hashCode;
	}

	private static final class NonTerminalEntry<E> extends Multisets.ImmutableEntry<E> {

		private final Multisets.ImmutableEntry<E> nextInBucket;

		NonTerminalEntry(E element, int count, Multisets.ImmutableEntry<E> nextInBucket) {
			super(element, count);
			this.nextInBucket = nextInBucket;
		}

		@Override
		public Multisets.ImmutableEntry<E> nextInBucket() {
			return nextInBucket;
		}

	}

}
