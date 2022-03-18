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

package org.magneton.core.cache;

import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutionException;

import org.magneton.core.collect.ImmutableMap;
import org.magneton.core.collect.Maps;

/**
 * This class provides a skeletal implementation of the {@code Cache} interface to
 * minimize the effort required to implement this interface.
 *
 * <p>
 * To implement a cache, the programmer needs only to extend this class and provide an
 * implementation for the {@link #put} and {@link #getIfPresent} methods.
 * {@link #getAllPresent} is implemented in terms of {@link #getIfPresent};
 * {@link #putAll} is implemented in terms of {@link #put},
 * {@link #invalidateAll(Iterable)} is implemented in terms of {@link #invalidate}. The
 * method {@link #cleanUp} is a no-op. All other methods throw an
 * {@link UnsupportedOperationException}.
 *
 * @author Charles Fry
 * @since 10.0
 */
@ElementTypesAreNonnullByDefault
public abstract class AbstractCache<K, V> implements org.magneton.core.cache.Cache<K, V> {

	/** Constructor for use by subclasses. */
	protected AbstractCache() {
	}

	/** @since 11.0 */
	@Override
	public V get(K key, Callable<? extends V> valueLoader) throws ExecutionException {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 *
	 * <p>
	 * This implementation of {@code getAllPresent} lacks any insight into the internal
	 * cache data structure, and is thus forced to return the query keys instead of the
	 * cached keys. This is only possible with an unsafe cast which requires {@code keys}
	 * to actually be of type {@code K}.
	 *
	 * @since 11.0
	 */
	/*
	 * <? extends Object> is mostly the same as <?> to plain Java. But to nullness
	 * checkers, they differ: <? extends Object> means "non-null types," while <?> means
	 * "all types."
	 */
	@Override
	public ImmutableMap<K, V> getAllPresent(Iterable<? extends Object> keys) {
		Map<K, V> result = Maps.newLinkedHashMap();
		for (Object key : keys) {
			if (!result.containsKey(key)) {
				K castKey = (K) key;
				V value = getIfPresent(key);
				if (value != null) {
					result.put(castKey, value);
				}
			}
		}
		return ImmutableMap.copyOf(result);
	}

	/** @since 11.0 */
	@Override
	public void put(K key, V value) {
		throw new UnsupportedOperationException();
	}

	/** @since 12.0 */
	@Override
	public void putAll(Map<? extends K, ? extends V> m) {
		for (Entry<? extends K, ? extends V> entry : m.entrySet()) {
			put(entry.getKey(), entry.getValue());
		}
	}

	@Override
	public void cleanUp() {
	}

	@Override
	public long size() {
		throw new UnsupportedOperationException();
	}

	@Override
	public void invalidate(Object key) {
		throw new UnsupportedOperationException();
	}

	/** @since 11.0 */
	@Override
	// For discussion of <? extends Object>, see getAllPresent.
	public void invalidateAll(Iterable<? extends Object> keys) {
		for (Object key : keys) {
			invalidate(key);
		}
	}

	@Override
	public void invalidateAll() {
		throw new UnsupportedOperationException();
	}

	@Override
	public org.magneton.core.cache.CacheStats stats() {
		throw new UnsupportedOperationException();
	}

	@Override
	public ConcurrentMap<K, V> asMap() {
		throw new UnsupportedOperationException();
	}

	/**
	 * Accumulates statistics during the operation of a
	 * {@link org.magneton.core.cache.Cache} for presentation by
	 * {@link org.magneton.core.cache.Cache#stats}. This is solely intended for
	 * consumption by {@code Cache} implementors.
	 *
	 * @since 10.0
	 */
	public interface StatsCounter {

		/**
		 * Records cache hits. This should be called when a cache request returns a cached
		 * value.
		 * @param count the number of hits to record
		 * @since 11.0
		 */
		void recordHits(int count);

		/**
		 * Records cache misses. This should be called when a cache request returns a
		 * value that was not found in the cache. This method should be called by the
		 * loading thread, as well as by threads blocking on the load. Multiple concurrent
		 * calls to {@link org.magneton.core.cache.Cache} lookup methods with the same key
		 * on an absent value should result in a single call to either
		 * {@code recordLoadSuccess} or {@code recordLoadException} and multiple calls to
		 * this method, despite all being served by the results of a single load
		 * operation.
		 * @param count the number of misses to record
		 * @since 11.0
		 */
		void recordMisses(int count);

		/**
		 * Records the successful load of a new entry. This should be called when a cache
		 * request causes an entry to be loaded, and the loading completes successfully.
		 * In contrast to {@link #recordMisses}, this method should only be called by the
		 * loading thread.
		 * @param loadTime the number of nanoseconds the cache spent computing or
		 * retrieving the new value
		 */
		// should accept a java.time.Duration
		void recordLoadSuccess(long loadTime);

		/**
		 * Records the failed load of a new entry. This should be called when a cache
		 * request causes an entry to be loaded, but an exception is thrown while loading
		 * the entry. In contrast to {@link #recordMisses}, this method should only be
		 * called by the loading thread.
		 * @param loadTime the number of nanoseconds the cache spent computing or
		 * retrieving the new value prior to an exception being thrown
		 */
		// should accept a java.time.Duration
		void recordLoadException(long loadTime);

		/**
		 * Records the eviction of an entry from the cache. This should only been called
		 * when an entry is evicted due to the cache's eviction strategy, and not as a
		 * result of manual {@linkplain org.magneton.core.cache.Cache#invalidate
		 * invalidations}.
		 */
		void recordEviction();

		/**
		 * Returns a snapshot of this counter's values. Note that this may be an
		 * inconsistent view, as it may be interleaved with update operations.
		 */
		org.magneton.core.cache.CacheStats snapshot();

	}

	/**
	 * A thread-safe {@link StatsCounter} implementation for use by {@link Cache}
	 * implementors.
	 *
	 * @since 10.0
	 */
	public static final class SimpleStatsCounter implements StatsCounter {

		private final org.magneton.core.cache.LongAddable hitCount = org.magneton.core.cache.LongAddables.create();

		private final org.magneton.core.cache.LongAddable missCount = org.magneton.core.cache.LongAddables.create();

		private final org.magneton.core.cache.LongAddable loadSuccessCount = org.magneton.core.cache.LongAddables
				.create();

		private final org.magneton.core.cache.LongAddable loadExceptionCount = org.magneton.core.cache.LongAddables
				.create();

		private final org.magneton.core.cache.LongAddable totalLoadTime = org.magneton.core.cache.LongAddables.create();

		private final org.magneton.core.cache.LongAddable evictionCount = org.magneton.core.cache.LongAddables.create();

		/** Constructs an instance with all counts initialized to zero. */
		public SimpleStatsCounter() {
		}

		/**
		 * Returns {@code value}, if non-negative. Otherwise, returns
		 * {@link Long#MAX_VALUE}.
		 */
		private static long negativeToMaxValue(long value) {
			return (value >= 0) ? value : Long.MAX_VALUE;
		}

		/** @since 11.0 */
		@Override
		public void recordHits(int count) {
			hitCount.add(count);
		}

		/** @since 11.0 */
		@Override
		public void recordMisses(int count) {
			missCount.add(count);
		}

		// b/122668874
		@Override
		public void recordLoadSuccess(long loadTime) {
			loadSuccessCount.increment();
			totalLoadTime.add(loadTime);
		}

		// b/122668874
		@Override
		public void recordLoadException(long loadTime) {
			loadExceptionCount.increment();
			totalLoadTime.add(loadTime);
		}

		@Override
		public void recordEviction() {
			evictionCount.increment();
		}

		@Override
		public org.magneton.core.cache.CacheStats snapshot() {
			return new org.magneton.core.cache.CacheStats(negativeToMaxValue(hitCount.sum()),
					negativeToMaxValue(missCount.sum()), negativeToMaxValue(loadSuccessCount.sum()),
					negativeToMaxValue(loadExceptionCount.sum()), negativeToMaxValue(totalLoadTime.sum()),
					negativeToMaxValue(evictionCount.sum()));
		}

		/** Increments all counters by the values in {@code other}. */
		public void incrementBy(StatsCounter other) {
			CacheStats otherStats = other.snapshot();
			hitCount.add(otherStats.hitCount());
			missCount.add(otherStats.missCount());
			loadSuccessCount.add(otherStats.loadSuccessCount());
			loadExceptionCount.add(otherStats.loadExceptionCount());
			totalLoadTime.add(otherStats.totalLoadTime());
			evictionCount.add(otherStats.evictionCount());
		}

	}

}
