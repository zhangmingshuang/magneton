/*
 * Copyright (C) 2007 The Guava Authors
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

import java.io.InvalidObjectException;
import java.io.ObjectStreamException;
import java.io.Serializable;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.function.ObjIntConsumer;

import javax.annotations.CanIgnoreReturnValue;
import javax.annotation.CheckForNull;

import org.magneton.core.base.Preconditions;
import org.magneton.core.primitives.Ints;

import static java.util.Objects.requireNonNull;
import static org.magneton.core.collect.CollectPreconditions.checkRemove;

/**
 * Basic implementation of {@code Multiset<E>} backed by an instance of
 * {@code Map<E, Count>}.
 *
 * <p>
 * For serialization to work, the subclass must specify explicit {@code readObject} and
 * {@code
 * writeObject} methods.
 *
 * @author Kevin Bourrillion
 */
@ElementTypesAreNonnullByDefault
abstract class AbstractMapBasedMultiset<E extends Object> extends org.magneton.core.collect.AbstractMultiset<E>
		implements Serializable {

	private static final long serialVersionUID = -2250766705698539974L;

	// TODO(lowasser): consider overhauling this back to Map<E, Integer>
	private transient Map<E, Count> backingMap;

	/*
	 * Cache the size for efficiency. Using a long lets us avoid the need for overflow
	 * checking and ensures that size() will function correctly even if the multiset had
	 * once been larger than Integer.MAX_VALUE.
	 */
	private transient long size;

	/** Standard constructor. */
	protected AbstractMapBasedMultiset(Map<E, Count> backingMap) {
		Preconditions.checkArgument(backingMap.isEmpty());
		this.backingMap = backingMap;
	}

	// Required Implementations

	private static int getAndSet(@CheckForNull Count i, int count) {
		if (i == null) {
			return 0;
		}

		return i.getAndSet(count);
	}

	/** Used during deserialization only. The backing map must be empty. */
	void setBackingMap(Map<E, Count> backingMap) {
		this.backingMap = backingMap;
	}

	/**
	 * {@inheritDoc}
	 *
	 * <p>
	 * Invoking {@link Multiset.Entry#getCount} on an entry in the returned set always
	 * returns the current count of that element in the multiset, as opposed to the count
	 * at the time the entry was retrieved.
	 */
	@Override
	public Set<Multiset.Entry<E>> entrySet() {
		return super.entrySet();
	}

	@Override
	Iterator<E> elementIterator() {
		Iterator<Map.Entry<E, Count>> backingEntries = backingMap.entrySet().iterator();
		return new Iterator<E>() {
			@CheckForNull
			Map.Entry<E, Count> toRemove;

			@Override
			public boolean hasNext() {
				return backingEntries.hasNext();
			}

			@Override
			@ParametricNullness
			public E next() {
				Map.Entry<E, Count> mapEntry = backingEntries.next();
				toRemove = mapEntry;
				return mapEntry.getKey();
			}

			@Override
			public void remove() {
				Preconditions.checkState(toRemove != null, "no calls to next() since the last call to remove()");
				size -= toRemove.getValue().getAndSet(0);
				backingEntries.remove();
				toRemove = null;
			}
		};
	}

	@Override
	Iterator<Multiset.Entry<E>> entryIterator() {
		Iterator<Map.Entry<E, Count>> backingEntries = backingMap.entrySet().iterator();
		return new Iterator<Multiset.Entry<E>>() {
			@CheckForNull
			Map.Entry<E, Count> toRemove;

			@Override
			public boolean hasNext() {
				return backingEntries.hasNext();
			}

			@Override
			public Multiset.Entry<E> next() {
				Map.Entry<E, Count> mapEntry = backingEntries.next();
				toRemove = mapEntry;
				return new Multisets.AbstractEntry<E>() {
					@Override
					@ParametricNullness
					public E getElement() {
						return mapEntry.getKey();
					}

					@Override
					public int getCount() {
						Count count = mapEntry.getValue();
						if (count == null || count.get() == 0) {
							Count frequency = backingMap.get(getElement());
							if (frequency != null) {
								return frequency.get();
							}
						}
						return (count == null) ? 0 : count.get();
					}
				};
			}

			@Override
			public void remove() {
				Preconditions.checkState(toRemove != null, "no calls to next() since the last call to remove()");
				size -= toRemove.getValue().getAndSet(0);
				backingEntries.remove();
				toRemove = null;
			}
		};
	}

	@Override
	public void forEachEntry(ObjIntConsumer<? super E> action) {
		Preconditions.checkNotNull(action);
		backingMap.forEach((element, count) -> action.accept(element, count.get()));
	}

	// Optimizations - Query Operations

	@Override
	public void clear() {
		for (Count frequency : backingMap.values()) {
			frequency.set(0);
		}
		backingMap.clear();
		size = 0L;
	}

	@Override
	int distinctElements() {
		return backingMap.size();
	}

	@Override
	public int size() {
		return Ints.saturatedCast(size);
	}

	@Override
	public Iterator<E> iterator() {
		return new MapBasedMultisetIterator();
	}

	// Optional Operations - Modification Operations

	@Override
	public int count(@CheckForNull Object element) {
		Count frequency = Maps.safeGet(backingMap, element);
		return (frequency == null) ? 0 : frequency.get();
	}

	/**
	 * {@inheritDoc}
	 * @throws IllegalArgumentException if the call would result in more than
	 * {@link Integer#MAX_VALUE} occurrences of {@code element} in this multiset.
	 */
	@CanIgnoreReturnValue
	@Override
	public int add(@ParametricNullness E element, int occurrences) {
		if (occurrences == 0) {
			return count(element);
		}
		Preconditions.checkArgument(occurrences > 0, "occurrences cannot be negative: %s", occurrences);
		Count frequency = backingMap.get(element);
		int oldCount;
		if (frequency == null) {
			oldCount = 0;
			backingMap.put(element, new Count(occurrences));
		}
		else {
			oldCount = frequency.get();
			long newCount = (long) oldCount + (long) occurrences;
			Preconditions.checkArgument(newCount <= Integer.MAX_VALUE, "too many occurrences: %s", newCount);
			frequency.add(occurrences);
		}
		size += occurrences;
		return oldCount;
	}

	@CanIgnoreReturnValue
	@Override
	public int remove(@CheckForNull Object element, int occurrences) {
		if (occurrences == 0) {
			return count(element);
		}
		Preconditions.checkArgument(occurrences > 0, "occurrences cannot be negative: %s", occurrences);
		Count frequency = backingMap.get(element);
		if (frequency == null) {
			return 0;
		}

		int oldCount = frequency.get();

		int numberRemoved;
		if (oldCount > occurrences) {
			numberRemoved = occurrences;
		}
		else {
			numberRemoved = oldCount;
			backingMap.remove(element);
		}

		frequency.add(-numberRemoved);
		size -= numberRemoved;
		return oldCount;
	}

	// Roughly a 33% performance improvement over AbstractMultiset.setCount().
	@CanIgnoreReturnValue
	@Override
	public int setCount(@ParametricNullness E element, int count) {
		CollectPreconditions.checkNonnegative(count, "count");

		Count existingCounter;
		int oldCount;
		if (count == 0) {
			existingCounter = backingMap.remove(element);
			oldCount = getAndSet(existingCounter, count);
		}
		else {
			existingCounter = backingMap.get(element);
			oldCount = getAndSet(existingCounter, count);

			if (existingCounter == null) {
				backingMap.put(element, new Count(count));
			}
		}

		size += (count - oldCount);
		return oldCount;
	}

	// Don't allow default serialization.
	private void readObjectNoData() throws ObjectStreamException {
		throw new InvalidObjectException("Stream data required");
	}

	/*
	 * Not subclassing AbstractMultiset$MultisetIterator because next() needs to retrieve
	 * the Map.Entry<E, Count> entry, which can then be used for a more efficient remove()
	 * call.
	 */
	private class MapBasedMultisetIterator implements Iterator<E> {

		final Iterator<Map.Entry<E, Count>> entryIterator;

		@CheckForNull
		Map.Entry<E, Count> currentEntry;

		int occurrencesLeft;

		boolean canRemove;

		MapBasedMultisetIterator() {
			entryIterator = backingMap.entrySet().iterator();
		}

		@Override
		public boolean hasNext() {
			return occurrencesLeft > 0 || entryIterator.hasNext();
		}

		@Override
		@ParametricNullness
		public E next() {
			if (occurrencesLeft == 0) {
				currentEntry = entryIterator.next();
				occurrencesLeft = currentEntry.getValue().get();
			}
			occurrencesLeft--;
			canRemove = true;
			/*
			 * requireNonNull is safe because occurrencesLeft starts at 0, forcing us to
			 * initialize currentEntry above. After that, we never clear it.
			 */
			return requireNonNull(currentEntry).getKey();
		}

		@Override
		public void remove() {
			checkRemove(canRemove);
			/*
			 * requireNonNull is safe because canRemove is set to true only after we
			 * initialize currentEntry (which we never subsequently clear).
			 */
			int frequency = requireNonNull(currentEntry).getValue().get();
			if (frequency <= 0) {
				throw new ConcurrentModificationException();
			}
			if (currentEntry.getValue().addAndGet(-1) == 0) {
				entryIterator.remove();
			}
			size--;
			canRemove = false;
		}

	}

}
