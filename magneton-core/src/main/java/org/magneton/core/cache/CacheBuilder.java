/*
 * Copyright (C) 2009 The Guava Authors
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

import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;
import java.time.Duration;
import java.util.ConcurrentModificationException;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotations.CheckReturnValue;
import javax.annotation.Nullable;

import org.magneton.core.base.Ascii;
import org.magneton.core.base.Equivalence;
import org.magneton.core.base.MoreObjects;
import org.magneton.core.base.Supplier;
import org.magneton.core.base.Suppliers;
import org.magneton.core.base.Ticker;
import org.magneton.foundation.util.concurrent.ListenableFuture;

import static org.magneton.core.base.Preconditions.checkArgument;
import static org.magneton.core.base.Preconditions.checkNotNull;
import static org.magneton.core.base.Preconditions.checkState;

/**
 * A builder of {@link LoadingCache} and {@link Cache} instances.
 *
 * <h2>Prefer <a href="https://github.com/ben-manes/caffeine/wiki">Caffeine</a> over
 * Guava's caching API</h2>
 *
 * <p>
 * The successor to Guava's caching API is
 * <a href="https://github.com/ben-manes/caffeine/wiki">Caffeine</a>. Its API is designed
 * to make it a nearly drop-in replacement -- though it requires Java 8 APIs and is not
 * available for Android or GWT/j2cl. Its equivalent to {@code CacheBuilder} is its
 * <a href=
 * "https://www.javadoc.io/doc/com.github.ben-manes.caffeine/caffeine/latest/com.github.benmanes.caffeine/com/github/benmanes/caffeine/cache/Caffeine.html">{@code
 * Caffeine}</a> class. Caffeine offers better performance, more features (including
 * asynchronous loading), and fewer <a href=
 * "https://github.com/google/guava/issues?q=is%3Aopen+is%3Aissue+label%3Apackage%3Dcache+label%3Atype%3Ddefect">bugs</a>.
 *
 * <p>
 * Caffeine defines its own interfaces (<a href=
 * "https://www.javadoc.io/doc/com.github.ben-manes.caffeine/caffeine/latest/com.github.benmanes.caffeine/com/github/benmanes/caffeine/cache/Cache.html">{@code
 * Cache}</a>, <a href=
 * "https://www.javadoc.io/doc/com.github.ben-manes.caffeine/caffeine/latest/com.github.benmanes.caffeine/com/github/benmanes/caffeine/cache/LoadingCache.html">{@code
 * LoadingCache}</a>, <a href=
 * "https://www.javadoc.io/doc/com.github.ben-manes.caffeine/caffeine/latest/com.github.benmanes.caffeine/com/github/benmanes/caffeine/cache/CacheLoader.html">{@code
 * CacheLoader}</a>, etc.), so you can use Caffeine without needing to use any Guava
 * types. Caffeine's types are better than Guava's, especially for <a href=
 * "https://www.javadoc.io/doc/com.github.ben-manes.caffeine/caffeine/latest/com.github.benmanes.caffeine/com/github/benmanes/caffeine/cache/AsyncLoadingCache.html">their
 * deep support for asynchronous operations</a>. But if you want to migrate to Caffeine
 * with minimal code changes, you can use <a href=
 * "https://www.javadoc.io/doc/com.github.ben-manes.caffeine/guava/latest/com.github.benmanes.caffeine.guava/com/github/benmanes/caffeine/guava/CaffeinatedGuava.html">its
 * {@code CaffeinatedGuava} adapter class</a>, which lets you build a Guava {@code Cache}
 * or a Guava {@code LoadingCache} backed by a Guava {@code CacheLoader}.
 *
 * <p>
 * Caffeine's API for asynchronous operations uses {@code CompletableFuture}: <a href=
 * "https://www.javadoc.io/doc/com.github.ben-manes.caffeine/caffeine/latest/com.github.benmanes.caffeine/com/github/benmanes/caffeine/cache/AsyncLoadingCache.html#get(K)">{@code
 * AsyncLoadingCache.get}</a> returns a {@code CompletableFuture}, and implementations of
 * <a href=
 * "https://www.javadoc.io/doc/com.github.ben-manes.caffeine/caffeine/latest/com.github.benmanes.caffeine/com/github/benmanes/caffeine/cache/AsyncCacheLoader.html#asyncLoad(K,java.util.concurrent.Executor)">{@code
 * AsyncCacheLoader.asyncLoad}</a> must return a {@code CompletableFuture}. Users of
 * Guava's {@link ListenableFuture} can adapt between the two {@code Future} types by
 * using <a href="https://github.com/lukas-krecan/future-converter#java8-guava">{@code
 * net.javacrumbs.futureconverter.java8guava.FutureConverter}</a>.
 *
 * <h2>More on {@code CacheBuilder}</h2>
 *
 * {@code CacheBuilder} builds caches with any combination of the following features:
 *
 * <ul>
 * <li>automatic loading of entries into the cache
 * <li>least-recently-used eviction when a maximum size is exceeded (note that the cache
 * is divided into segments, each of which does LRU internally)
 * <li>time-based expiration of entries, measured since last access or last write
 * <li>keys automatically wrapped in {@code WeakReference}
 * <li>values automatically wrapped in {@code WeakReference} or {@code SoftReference}
 * <li>notification of evicted (or otherwise removed) entries
 * <li>accumulation of cache access statistics
 * </ul>
 *
 * <p>
 * These features are all optional; caches can be created using all or none of them. By
 * default cache instances created by {@code CacheBuilder} will not perform any type of
 * eviction.
 *
 * <p>
 * Usage example:
 *
 * <pre>{@code
 * LoadingCache<Key, Graph> graphs = CacheBuilder.newBuilder()
 *     .maximumSize(10000)
 *     .expireAfterWrite(Duration.ofMinutes(10))
 *     .removalListener(MY_LISTENER)
 *     .build(
 *         new CacheLoader<Key, Graph>() {
 *           public Graph load(Key key) throws AnyException {
 *             return createExpensiveGraph(key);
 *           }
 *         });
 * }</pre>
 *
 * <p>
 * Or equivalently,
 *
 * <pre>{@code
 * // In real life this would come from a command-line flag or config file
 * String spec = "maximumSize=10000,expireAfterWrite=10m";
 *
 * LoadingCache<Key, Graph> graphs = CacheBuilder.from(spec)
 *     .removalListener(MY_LISTENER)
 *     .build(
 *         new CacheLoader<Key, Graph>() {
 *           public Graph load(Key key) throws AnyException {
 *             return createExpensiveGraph(key);
 *           }
 *         });
 * }</pre>
 *
 * <p>
 * The returned cache is implemented as a hash table with similar performance
 * characteristics to {@link ConcurrentHashMap}. It implements all optional operations of
 * the {@link LoadingCache} and {@link Cache} interfaces. The {@code asMap} view (and its
 * collection views) have <i>weakly consistent iterators</i>. This means that they are
 * safe for concurrent use, but if other threads modify the cache after the iterator is
 * created, it is undefined which of these changes, if any, are reflected in that
 * iterator. These iterators never throw {@link ConcurrentModificationException}.
 *
 * <p>
 * <b>Note:</b> by default, the returned cache uses equality comparisons (the
 * {@link Object#equals equals} method) to determine equality for keys or values. However,
 * if {@link #weakKeys} was specified, the cache uses identity ({@code ==}) comparisons
 * instead for keys. Likewise, if {@link #weakValues} or {@link #softValues} was
 * specified, the cache uses identity comparisons for values.
 *
 * <p>
 * Entries are automatically evicted from the cache when any of
 * {@linkplain #maximumSize(long) maximumSize}, {@linkplain #maximumWeight(long)
 * maximumWeight}, {@linkplain #expireAfterWrite expireAfterWrite},
 * {@linkplain #expireAfterAccess expireAfterAccess}, {@linkplain #weakKeys weakKeys},
 * {@linkplain #weakValues weakValues}, or {@linkplain #softValues softValues} are
 * requested.
 *
 * <p>
 * If {@linkplain #maximumSize(long) maximumSize} or {@linkplain #maximumWeight(long)
 * maximumWeight} is requested entries may be evicted on each cache modification.
 *
 * <p>
 * If {@linkplain #expireAfterWrite expireAfterWrite} or {@linkplain #expireAfterAccess
 * expireAfterAccess} is requested entries may be evicted on each cache modification, on
 * occasional cache accesses, or on calls to {@link Cache#cleanUp}. Expired entries may be
 * counted by {@link Cache#size}, but will never be visible to read or write operations.
 *
 * <p>
 * If {@linkplain #weakKeys weakKeys}, {@linkplain #weakValues weakValues}, or
 * {@linkplain #softValues softValues} are requested, it is possible for a key or value
 * present in the cache to be reclaimed by the garbage collector. Entries with reclaimed
 * keys or values may be removed from the cache on each cache modification, on occasional
 * cache accesses, or on calls to {@link Cache#cleanUp}; such entries may be counted in
 * {@link Cache#size}, but will never be visible to read or write operations.
 *
 * <p>
 * Certain cache configurations will result in the accrual of periodic maintenance tasks
 * which will be performed during write operations, or during occasional read operations
 * in the absence of writes. The {@link Cache#cleanUp} method of the returned cache will
 * also perform maintenance, but calling it should not be necessary with a high throughput
 * cache. Only caches built with {@linkplain #removalListener removalListener},
 * {@linkplain #expireAfterWrite expireAfterWrite}, {@linkplain #expireAfterAccess
 * expireAfterAccess}, {@linkplain #weakKeys weakKeys}, {@linkplain #weakValues
 * weakValues}, or {@linkplain #softValues softValues} perform periodic maintenance.
 *
 * <p>
 * The caches produced by {@code CacheBuilder} are serializable, and the deserialized
 * caches retain all the configuration properties of the original cache. Note that the
 * serialized form does <i>not</i> include cache contents, but only configuration.
 *
 * <p>
 * See the Guava User Guide article on
 * <a href="https://github.com/google/guava/wiki/CachesExplained">caching</a> for a
 * higher-level explanation.
 *
 * @param <K> the most general key type this builder will be able to create caches for.
 * This is normally {@code Object} unless it is constrained by using a method like {@code
 *     #removalListener}. Cache keys may not be null.
 * @param <V> the most general value type this builder will be able to create caches for.
 * This is normally {@code Object} unless it is constrained by using a method like {@code
 *     #removalListener}. Cache values may not be null.
 * @author Charles Fry
 * @author Kevin Bourrillion
 * @since 10.0
 */
@ElementTypesAreNonnullByDefault
public final class CacheBuilder<K, V> {

	static final CacheStats EMPTY_STATS = new CacheStats(0, 0, 0, 0, 0, 0);

	static final Supplier<? extends AbstractCache.StatsCounter> NULL_STATS_COUNTER = Suppliers
			.ofInstance(new AbstractCache.StatsCounter() {
				@Override
				public void recordHits(int count) {
				}

				@Override
				public void recordMisses(int count) {
				}

				// b/122668874
				@Override
				public void recordLoadSuccess(long loadTime) {
				}

				// b/122668874
				@Override
				public void recordLoadException(long loadTime) {
				}

				@Override
				public void recordEviction() {
				}

				@Override
				public CacheStats snapshot() {
					return EMPTY_STATS;
				}
			});

	static final Supplier<AbstractCache.StatsCounter> CACHE_STATS_COUNTER = new Supplier<AbstractCache.StatsCounter>() {
		@Override
		public AbstractCache.StatsCounter get() {
			return new AbstractCache.SimpleStatsCounter();
		}
	};

	static final Ticker NULL_TICKER = new Ticker() {
		@Override
		public long read() {
			return 0;
		}
	};

	static final int UNSET_INT = -1;

	private static final int DEFAULT_INITIAL_CAPACITY = 16;

	private static final int DEFAULT_CONCURRENCY_LEVEL = 4;

	// should be a Duration
	private static final int DEFAULT_EXPIRATION_NANOS = 0;

	// should be a Duration
	private static final int DEFAULT_REFRESH_NANOS = 0;

	private static final Logger logger = Logger.getLogger(CacheBuilder.class.getName());

	boolean strictParsing = true;

	int initialCapacity = UNSET_INT;

	int concurrencyLevel = UNSET_INT;

	long maximumSize = UNSET_INT;

	long maximumWeight = UNSET_INT;

	@Nullable
	Weigher<? super K, ? super V> weigher;

	@Nullable
	LocalCache.Strength keyStrength;

	@Nullable
	LocalCache.Strength valueStrength;

	// should be a Duration
	long expireAfterWriteNanos = UNSET_INT;

	// should be a Duration
	long expireAfterAccessNanos = UNSET_INT;

	// should be a Duration
	long refreshNanos = UNSET_INT;

	@Nullable
	Equivalence<Object> keyEquivalence;

	@Nullable
	Equivalence<Object> valueEquivalence;

	@Nullable
	RemovalListener<? super K, ? super V> removalListener;

	@Nullable
	Ticker ticker;

	Supplier<? extends AbstractCache.StatsCounter> statsCounterSupplier = NULL_STATS_COUNTER;

	private CacheBuilder() {
	}

	/**
	 * Constructs a new {@code CacheBuilder} instance with default settings, including
	 * strong keys, strong values, and no automatic eviction of any kind.
	 *
	 * <p>
	 * Note that while this return type is {@code CacheBuilder<Object, Object>}, type
	 * parameters on the {@link #build} methods allow you to create a cache of any key and
	 * value type desired.
	 */
	@CheckReturnValue
	public static CacheBuilder<Object, Object> newBuilder() {
		return new CacheBuilder<>();
	}

	/**
	 * Constructs a new {@code CacheBuilder} instance with the settings specified in
	 * {@code spec}.
	 *
	 * @since 12.0
	 */
	@CheckReturnValue
	public static CacheBuilder<Object, Object> from(CacheBuilderSpec spec) {
		return spec.toCacheBuilder().lenientParsing();
	}

	/**
	 * Constructs a new {@code CacheBuilder} instance with the settings specified in
	 * {@code spec}. This is especially useful for command-line configuration of a
	 * {@code CacheBuilder}.
	 * @param spec a String in the format specified by {@link CacheBuilderSpec}
	 * @since 12.0
	 */
	@CheckReturnValue
	public static CacheBuilder<Object, Object> from(String spec) {
		return from(CacheBuilderSpec.parse(spec));
	}

	/**
	 * Returns the number of nanoseconds of the given duration without throwing or
	 * overflowing.
	 *
	 * <p>
	 * Instead of throwing {@link ArithmeticException}, this method silently saturates to
	 * either {@link Long#MAX_VALUE} or {@link Long#MIN_VALUE}. This behavior can be
	 * useful when decomposing a duration in order to call a legacy API which requires a
	 * {@code long, TimeUnit} pair.
	 */
	// duration decomposition
	private static long toNanosSaturated(Duration duration) {
		// Using a try/catch seems lazy, but the catch block will rarely get invoked
		// (except for
		// durations longer than approximately +/- 292 years).
		try {
			return duration.toNanos();
		}
		catch (ArithmeticException tooBig) {
			return duration.isNegative() ? Long.MIN_VALUE : Long.MAX_VALUE;
		}
	}

	/**
	 * Enables lenient parsing. Useful for tests and spec parsing.
	 * @return this {@code CacheBuilder} instance (for chaining)
	 */
	CacheBuilder<K, V> lenientParsing() {
		strictParsing = false;
		return this;
	}

	/**
	 * Sets a custom {@code Equivalence} strategy for comparing keys.
	 *
	 * <p>
	 * By default, the cache uses {@link Equivalence#identity} to determine key equality
	 * when {@link #weakKeys} is specified, and {@link Equivalence#equals()} otherwise.
	 * @return this {@code CacheBuilder} instance (for chaining)
	 */
	CacheBuilder<K, V> keyEquivalence(Equivalence<Object> equivalence) {
		checkState(keyEquivalence == null, "key equivalence was already set to %s", keyEquivalence);
		keyEquivalence = checkNotNull(equivalence);
		return this;
	}

	Equivalence<Object> getKeyEquivalence() {
		return MoreObjects.firstNonNull(keyEquivalence, getKeyStrength().defaultEquivalence());
	}

	/**
	 * Sets a custom {@code Equivalence} strategy for comparing values.
	 *
	 * <p>
	 * By default, the cache uses {@link Equivalence#identity} to determine value equality
	 * when {@link #weakValues} or {@link #softValues} is specified, and
	 * {@link Equivalence#equals()} otherwise.
	 * @return this {@code CacheBuilder} instance (for chaining)
	 */
	CacheBuilder<K, V> valueEquivalence(Equivalence<Object> equivalence) {
		checkState(valueEquivalence == null, "value equivalence was already set to %s", valueEquivalence);
		valueEquivalence = checkNotNull(equivalence);
		return this;
	}

	Equivalence<Object> getValueEquivalence() {
		return MoreObjects.firstNonNull(valueEquivalence, getValueStrength().defaultEquivalence());
	}

	/**
	 * Sets the minimum total size for the internal hash tables. For example, if the
	 * initial capacity is {@code 60}, and the concurrency level is {@code 8}, then eight
	 * segments are created, each having a hash table of size eight. Providing a large
	 * enough estimate at construction time avoids the need for expensive resizing
	 * operations later, but setting this value unnecessarily high wastes memory.
	 * @return this {@code CacheBuilder} instance (for chaining)
	 * @throws IllegalArgumentException if {@code initialCapacity} is negative
	 * @throws IllegalStateException if an initial capacity was already set
	 */
	public CacheBuilder<K, V> initialCapacity(int initialCapacity) {
		checkState(this.initialCapacity == UNSET_INT, "initial capacity was already set to %s", this.initialCapacity);
		checkArgument(initialCapacity >= 0);
		this.initialCapacity = initialCapacity;
		return this;
	}

	int getInitialCapacity() {
		return (initialCapacity == UNSET_INT) ? DEFAULT_INITIAL_CAPACITY : initialCapacity;
	}

	/**
	 * Guides the allowed concurrency among update operations. Used as a hint for internal
	 * sizing. The table is internally partitioned to try to permit the indicated number
	 * of concurrent updates without contention. Because assignment of entries to these
	 * partitions is not necessarily uniform, the actual concurrency observed may vary.
	 * Ideally, you should choose a value to accommodate as many threads as will ever
	 * concurrently modify the table. Using a significantly higher value than you need can
	 * waste space and time, and a significantly lower value can lead to thread
	 * contention. But overestimates and underestimates within an order of magnitude do
	 * not usually have much noticeable impact. A value of one permits only one thread to
	 * modify the cache at a time, but since read operations and cache loading
	 * computations can proceed concurrently, this still yields higher concurrency than
	 * full synchronization.
	 *
	 * <p>
	 * Defaults to 4. <b>Note:</b>The default may change in the future. If you care about
	 * this value, you should always choose it explicitly.
	 *
	 * <p>
	 * The current implementation uses the concurrency level to create a fixed number of
	 * hashtable segments, each governed by its own write lock. The segment lock is taken
	 * once for each explicit write, and twice for each cache loading computation (once
	 * prior to loading the new value, and once after loading completes). Much internal
	 * cache management is performed at the segment granularity. For example, access
	 * queues and write queues are kept per segment when they are required by the selected
	 * eviction algorithm. As such, when writing unit tests it is not uncommon to specify
	 * {@code concurrencyLevel(1)} in order to achieve more deterministic eviction
	 * behavior.
	 *
	 * <p>
	 * Note that future implementations may abandon segment locking in favor of more
	 * advanced concurrency controls.
	 * @return this {@code CacheBuilder} instance (for chaining)
	 * @throws IllegalArgumentException if {@code concurrencyLevel} is nonpositive
	 * @throws IllegalStateException if a concurrency level was already set
	 */
	public CacheBuilder<K, V> concurrencyLevel(int concurrencyLevel) {
		checkState(this.concurrencyLevel == UNSET_INT, "concurrency level was already set to %s",
				this.concurrencyLevel);
		checkArgument(concurrencyLevel > 0);
		this.concurrencyLevel = concurrencyLevel;
		return this;
	}

	int getConcurrencyLevel() {
		return (concurrencyLevel == UNSET_INT) ? DEFAULT_CONCURRENCY_LEVEL : concurrencyLevel;
	}

	/**
	 * Specifies the maximum number of entries the cache may contain.
	 *
	 * <p>
	 * Note that the cache <b>may evict an entry before this limit is exceeded</b>. For
	 * example, in the current implementation, when {@code concurrencyLevel} is greater
	 * than {@code 1}, each resulting segment inside the cache <i>independently</i> limits
	 * its own size to approximately {@code maximumSize / concurrencyLevel}.
	 *
	 * <p>
	 * When eviction is necessary, the cache evicts entries that are less likely to be
	 * used again. For example, the cache may evict an entry because it hasn't been used
	 * recently or very often.
	 *
	 * <p>
	 * If {@code maximumSize} is zero, elements will be evicted immediately after being
	 * loaded into cache. This can be useful in testing, or to disable caching
	 * temporarily.
	 *
	 * <p>
	 * This feature cannot be used in conjunction with {@link #maximumWeight}.
	 * @param maximumSize the maximum size of the cache
	 * @return this {@code CacheBuilder} instance (for chaining)
	 * @throws IllegalArgumentException if {@code maximumSize} is negative
	 * @throws IllegalStateException if a maximum size or weight was already set
	 */
	public CacheBuilder<K, V> maximumSize(long maximumSize) {
		checkState(this.maximumSize == UNSET_INT, "maximum size was already set to %s", this.maximumSize);
		checkState(maximumWeight == UNSET_INT, "maximum weight was already set to %s", maximumWeight);
		checkState(weigher == null, "maximum size can not be combined with weigher");
		checkArgument(maximumSize >= 0, "maximum size must not be negative");
		this.maximumSize = maximumSize;
		return this;
	}

	/**
	 * Specifies the maximum weight of entries the cache may contain. Weight is determined
	 * using the {@link Weigher} specified with {@link #weigher}, and use of this method
	 * requires a corresponding call to {@link #weigher} prior to calling {@link #build}.
	 *
	 * <p>
	 * Note that the cache <b>may evict an entry before this limit is exceeded</b>. For
	 * example, in the current implementation, when {@code concurrencyLevel} is greater
	 * than {@code 1}, each resulting segment inside the cache <i>independently</i> limits
	 * its own weight to approximately {@code maximumWeight / concurrencyLevel}.
	 *
	 * <p>
	 * When eviction is necessary, the cache evicts entries that are less likely to be
	 * used again. For example, the cache may evict an entry because it hasn't been used
	 * recently or very often.
	 *
	 * <p>
	 * If {@code maximumWeight} is zero, elements will be evicted immediately after being
	 * loaded into cache. This can be useful in testing, or to disable caching
	 * temporarily.
	 *
	 * <p>
	 * Note that weight is only used to determine whether the cache is over capacity; it
	 * has no effect on selecting which entry should be evicted next.
	 *
	 * <p>
	 * This feature cannot be used in conjunction with {@link #maximumSize}.
	 * @param maximumWeight the maximum total weight of entries the cache may contain
	 * @return this {@code CacheBuilder} instance (for chaining)
	 * @throws IllegalArgumentException if {@code maximumWeight} is negative
	 * @throws IllegalStateException if a maximum weight or size was already set
	 * @since 11.0
	 */
	public CacheBuilder<K, V> maximumWeight(long maximumWeight) {
		checkState(this.maximumWeight == UNSET_INT, "maximum weight was already set to %s", this.maximumWeight);
		checkState(maximumSize == UNSET_INT, "maximum size was already set to %s", maximumSize);
		checkArgument(maximumWeight >= 0, "maximum weight must not be negative");
		this.maximumWeight = maximumWeight;
		return this;
	}

	/**
	 * Specifies the weigher to use in determining the weight of entries. Entry weight is
	 * taken into consideration by {@link #maximumWeight(long)} when determining which
	 * entries to evict, and use of this method requires a corresponding call to
	 * {@link #maximumWeight(long)} prior to calling {@link #build}. Weights are measured
	 * and recorded when entries are inserted into the cache, and are thus effectively
	 * static during the lifetime of a cache entry.
	 *
	 * <p>
	 * When the weight of an entry is zero it will not be considered for size-based
	 * eviction (though it still may be evicted by other means).
	 *
	 * <p>
	 * <b>Important note:</b> Instead of returning <em>this</em> as a {@code CacheBuilder}
	 * instance, this method returns {@code CacheBuilder<K1, V1>}. From this point on,
	 * either the original reference or the returned reference may be used to complete
	 * configuration and build the cache, but only the "generic" one is type-safe. That
	 * is, it will properly prevent you from building caches whose key or value types are
	 * incompatible with the types accepted by the weigher already provided; the
	 * {@code CacheBuilder} type cannot do this. For best results, simply use the standard
	 * method-chaining idiom, as illustrated in the documentation at top, configuring a
	 * {@code CacheBuilder} and building your {@link Cache} all in a single statement.
	 *
	 * <p>
	 * <b>Warning:</b> if you ignore the above advice, and use this {@code CacheBuilder}
	 * to build a cache whose key or value type is incompatible with the weigher, you will
	 * likely experience a {@link ClassCastException} at some <i>undefined</i> point in
	 * the future.
	 * @param weigher the weigher to use in calculating the weight of cache entries
	 * @return this {@code CacheBuilder} instance (for chaining)
	 * @throws IllegalArgumentException if {@code size} is negative
	 * @throws IllegalStateException if a maximum size was already set
	 * @since 11.0
	 */
	public <K1 extends K, V1 extends V> CacheBuilder<K1, V1> weigher(Weigher<? super K1, ? super V1> weigher) {
		checkState(this.weigher == null);
		if (strictParsing) {
			checkState(maximumSize == UNSET_INT, "weigher can not be combined with maximum size", maximumSize);
		}

		// safely limiting the kinds of caches this can produce
		CacheBuilder<K1, V1> me = (CacheBuilder<K1, V1>) this;
		me.weigher = checkNotNull(weigher);
		return me;
	}

	long getMaximumWeight() {
		if (expireAfterWriteNanos == 0 || expireAfterAccessNanos == 0) {
			return 0;
		}
		return (weigher == null) ? maximumSize : maximumWeight;
	}

	// Make a safe contravariant cast now so we don't have to do it over and over.
	<K1 extends K, V1 extends V> Weigher<K1, V1> getWeigher() {
		return (Weigher<K1, V1>) MoreObjects.firstNonNull(weigher, OneWeigher.INSTANCE);
	}

	/**
	 * Specifies that each key (not value) stored in the cache should be wrapped in a
	 * {@link WeakReference} (by default, strong references are used).
	 *
	 * <p>
	 * <b>Warning:</b> when this method is used, the resulting cache will use identity
	 * ({@code ==}) comparison to determine equality of keys. Its {@link Cache#asMap} view
	 * will therefore technically violate the {@link Map} specification (in the same way
	 * that {@link IdentityHashMap} does).
	 *
	 * <p>
	 * Entries with keys that have been garbage collected may be counted in
	 * {@link Cache#size}, but will never be visible to read or write operations; such
	 * entries are cleaned up as part of the routine maintenance described in the class
	 * javadoc.
	 * @return this {@code CacheBuilder} instance (for chaining)
	 * @throws IllegalStateException if the key strength was already set
	 */
	public CacheBuilder<K, V> weakKeys() {
		return setKeyStrength(LocalCache.Strength.WEAK);
	}

	LocalCache.Strength getKeyStrength() {
		return MoreObjects.firstNonNull(keyStrength, LocalCache.Strength.STRONG);
	}

	CacheBuilder<K, V> setKeyStrength(LocalCache.Strength strength) {
		checkState(keyStrength == null, "Key strength was already set to %s", keyStrength);
		keyStrength = checkNotNull(strength);
		return this;
	}

	/**
	 * Specifies that each value (not key) stored in the cache should be wrapped in a
	 * {@link WeakReference} (by default, strong references are used).
	 *
	 * <p>
	 * Weak values will be garbage collected once they are weakly reachable. This makes
	 * them a poor candidate for caching; consider {@link #softValues} instead.
	 *
	 * <p>
	 * <b>Note:</b> when this method is used, the resulting cache will use identity
	 * ({@code ==}) comparison to determine equality of values.
	 *
	 * <p>
	 * Entries with values that have been garbage collected may be counted in
	 * {@link Cache#size}, but will never be visible to read or write operations; such
	 * entries are cleaned up as part of the routine maintenance described in the class
	 * javadoc.
	 * @return this {@code CacheBuilder} instance (for chaining)
	 * @throws IllegalStateException if the value strength was already set
	 */
	public CacheBuilder<K, V> weakValues() {
		return setValueStrength(LocalCache.Strength.WEAK);
	}

	/**
	 * Specifies that each value (not key) stored in the cache should be wrapped in a
	 * {@link SoftReference} (by default, strong references are used). Softly-referenced
	 * objects will be garbage-collected in a <i>globally</i> least-recently-used manner,
	 * in response to memory demand.
	 *
	 * <p>
	 * <b>Warning:</b> in most circumstances it is better to set a per-cache
	 * {@linkplain #maximumSize(long) maximum size} instead of using soft references. You
	 * should only use this method if you are well familiar with the practical
	 * consequences of soft references.
	 *
	 * <p>
	 * <b>Note:</b> when this method is used, the resulting cache will use identity
	 * ({@code ==}) comparison to determine equality of values.
	 *
	 * <p>
	 * Entries with values that have been garbage collected may be counted in
	 * {@link Cache#size}, but will never be visible to read or write operations; such
	 * entries are cleaned up as part of the routine maintenance described in the class
	 * javadoc.
	 * @return this {@code CacheBuilder} instance (for chaining)
	 * @throws IllegalStateException if the value strength was already set
	 */
	public CacheBuilder<K, V> softValues() {
		return setValueStrength(LocalCache.Strength.SOFT);
	}

	LocalCache.Strength getValueStrength() {
		return MoreObjects.firstNonNull(valueStrength, LocalCache.Strength.STRONG);
	}

	CacheBuilder<K, V> setValueStrength(LocalCache.Strength strength) {
		checkState(valueStrength == null, "Value strength was already set to %s", valueStrength);
		valueStrength = checkNotNull(strength);
		return this;
	}

	/**
	 * Specifies that each entry should be automatically removed from the cache once a
	 * fixed duration has elapsed after the entry's creation, or the most recent
	 * replacement of its value.
	 *
	 * <p>
	 * When {@code duration} is zero, this method hands off to {@link #maximumSize(long)
	 * maximumSize}{@code (0)}, ignoring any otherwise-specified maximum size or weight.
	 * This can be useful in testing, or to disable caching temporarily without a code
	 * change.
	 *
	 * <p>
	 * Expired entries may be counted in {@link Cache#size}, but will never be visible to
	 * read or write operations. Expired entries are cleaned up as part of the routine
	 * maintenance described in the class javadoc.
	 * @param duration the length of time after an entry is created that it should be
	 * automatically removed
	 * @return this {@code CacheBuilder} instance (for chaining)
	 * @throws IllegalArgumentException if {@code duration} is negative
	 * @throws IllegalStateException if {@link #expireAfterWrite} was already set
	 * @throws ArithmeticException for durations greater than +/- approximately 292 years
	 * @since 25.0
	 */
	// Duration decomposition
	public CacheBuilder<K, V> expireAfterWrite(Duration duration) {
		return expireAfterWrite(toNanosSaturated(duration), TimeUnit.NANOSECONDS);
	}

	/**
	 * Specifies that each entry should be automatically removed from the cache once a
	 * fixed duration has elapsed after the entry's creation, or the most recent
	 * replacement of its value.
	 *
	 * <p>
	 * When {@code duration} is zero, this method hands off to {@link #maximumSize(long)
	 * maximumSize}{@code (0)}, ignoring any otherwise-specified maximum size or weight.
	 * This can be useful in testing, or to disable caching temporarily without a code
	 * change.
	 *
	 * <p>
	 * Expired entries may be counted in {@link Cache#size}, but will never be visible to
	 * read or write operations. Expired entries are cleaned up as part of the routine
	 * maintenance described in the class javadoc.
	 *
	 * <p>
	 * If you can represent the duration as a {@link Duration} (which should be preferred
	 * when feasible), use {@link #expireAfterWrite(Duration)} instead.
	 * @param duration the length of time after an entry is created that it should be
	 * automatically removed
	 * @param unit the unit that {@code duration} is expressed in
	 * @return this {@code CacheBuilder} instance (for chaining)
	 * @throws IllegalArgumentException if {@code duration} is negative
	 * @throws IllegalStateException if {@link #expireAfterWrite} was already set
	 */
	// should accept a Duration
	public CacheBuilder<K, V> expireAfterWrite(long duration, TimeUnit unit) {
		checkState(expireAfterWriteNanos == UNSET_INT, "expireAfterWrite was already set to %s ns",
				expireAfterWriteNanos);
		checkArgument(duration >= 0, "duration cannot be negative: %s %s", duration, unit);
		expireAfterWriteNanos = unit.toNanos(duration);
		return this;
	}

	// nanos internally, should be Duration
	long getExpireAfterWriteNanos() {
		return (expireAfterWriteNanos == UNSET_INT) ? DEFAULT_EXPIRATION_NANOS : expireAfterWriteNanos;
	}

	/**
	 * Specifies that each entry should be automatically removed from the cache once a
	 * fixed duration has elapsed after the entry's creation, the most recent replacement
	 * of its value, or its last access. Access time is reset by all cache read and write
	 * operations (including {@code
	 * Cache.asMap().get(Object)} and {@code Cache.asMap().put(K, V)}), but not by {@code
	 * containsKey(Object)}, nor by operations on the collection-views of
	 * {@link Cache#asMap}}. So, for example, iterating through
	 * {@code Cache.asMap().entrySet()} does not reset access time for the entries you
	 * retrieve.
	 *
	 * <p>
	 * When {@code duration} is zero, this method hands off to {@link #maximumSize(long)
	 * maximumSize}{@code (0)}, ignoring any otherwise-specified maximum size or weight.
	 * This can be useful in testing, or to disable caching temporarily without a code
	 * change.
	 *
	 * <p>
	 * Expired entries may be counted in {@link Cache#size}, but will never be visible to
	 * read or write operations. Expired entries are cleaned up as part of the routine
	 * maintenance described in the class javadoc.
	 * @param duration the length of time after an entry is last accessed that it should
	 * be automatically removed
	 * @return this {@code CacheBuilder} instance (for chaining)
	 * @throws IllegalArgumentException if {@code duration} is negative
	 * @throws IllegalStateException if {@link #expireAfterAccess} was already set
	 * @throws ArithmeticException for durations greater than +/- approximately 292 years
	 * @since 25.0
	 */
	// Duration decomposition
	public CacheBuilder<K, V> expireAfterAccess(Duration duration) {
		return expireAfterAccess(toNanosSaturated(duration), TimeUnit.NANOSECONDS);
	}

	/**
	 * Specifies that each entry should be automatically removed from the cache once a
	 * fixed duration has elapsed after the entry's creation, the most recent replacement
	 * of its value, or its last access. Access time is reset by all cache read and write
	 * operations (including {@code
	 * Cache.asMap().get(Object)} and {@code Cache.asMap().put(K, V)}), but not by {@code
	 * containsKey(Object)}, nor by operations on the collection-views of
	 * {@link Cache#asMap}. So, for example, iterating through
	 * {@code Cache.asMap().entrySet()} does not reset access time for the entries you
	 * retrieve.
	 *
	 * <p>
	 * When {@code duration} is zero, this method hands off to {@link #maximumSize(long)
	 * maximumSize}{@code (0)}, ignoring any otherwise-specified maximum size or weight.
	 * This can be useful in testing, or to disable caching temporarily without a code
	 * change.
	 *
	 * <p>
	 * Expired entries may be counted in {@link Cache#size}, but will never be visible to
	 * read or write operations. Expired entries are cleaned up as part of the routine
	 * maintenance described in the class javadoc.
	 *
	 * <p>
	 * If you can represent the duration as a {@link Duration} (which should be preferred
	 * when feasible), use {@link #expireAfterAccess(Duration)} instead.
	 * @param duration the length of time after an entry is last accessed that it should
	 * be automatically removed
	 * @param unit the unit that {@code duration} is expressed in
	 * @return this {@code CacheBuilder} instance (for chaining)
	 * @throws IllegalArgumentException if {@code duration} is negative
	 * @throws IllegalStateException if {@link #expireAfterAccess} was already set
	 */
	// should accept a Duration
	public CacheBuilder<K, V> expireAfterAccess(long duration, TimeUnit unit) {
		checkState(expireAfterAccessNanos == UNSET_INT, "expireAfterAccess was already set to %s ns",
				expireAfterAccessNanos);
		checkArgument(duration >= 0, "duration cannot be negative: %s %s", duration, unit);
		expireAfterAccessNanos = unit.toNanos(duration);
		return this;
	}

	// nanos internally, should be Duration
	long getExpireAfterAccessNanos() {
		return (expireAfterAccessNanos == UNSET_INT) ? DEFAULT_EXPIRATION_NANOS : expireAfterAccessNanos;
	}

	/**
	 * Specifies that active entries are eligible for automatic refresh once a fixed
	 * duration has elapsed after the entry's creation, or the most recent replacement of
	 * its value. The semantics of refreshes are specified in
	 * {@link LoadingCache#refresh}, and are performed by calling
	 * {@link CacheLoader#reload}.
	 *
	 * <p>
	 * As the default implementation of {@link CacheLoader#reload} is synchronous, it is
	 * recommended that users of this method override {@link CacheLoader#reload} with an
	 * asynchronous implementation; otherwise refreshes will be performed during unrelated
	 * cache read and write operations.
	 *
	 * <p>
	 * Currently automatic refreshes are performed when the first stale request for an
	 * entry occurs. The request triggering refresh will make a synchronous call to
	 * {@link CacheLoader#reload} to obtain a future of the new value. If the returned
	 * future is already complete, it is returned immediately. Otherwise, the old value is
	 * returned.
	 *
	 * <p>
	 * <b>Note:</b> <i>all exceptions thrown during refresh will be logged and then
	 * swallowed</i>.
	 * @param duration the length of time after an entry is created that it should be
	 * considered stale, and thus eligible for refresh
	 * @return this {@code CacheBuilder} instance (for chaining)
	 * @throws IllegalArgumentException if {@code duration} is negative
	 * @throws IllegalStateException if {@link #refreshAfterWrite} was already set
	 * @throws ArithmeticException for durations greater than +/- approximately 292 years
	 * @since 25.0
	 */
	// Duration decomposition
	public CacheBuilder<K, V> refreshAfterWrite(Duration duration) {
		return refreshAfterWrite(toNanosSaturated(duration), TimeUnit.NANOSECONDS);
	}

	/**
	 * Specifies that active entries are eligible for automatic refresh once a fixed
	 * duration has elapsed after the entry's creation, or the most recent replacement of
	 * its value. The semantics of refreshes are specified in
	 * {@link LoadingCache#refresh}, and are performed by calling
	 * {@link CacheLoader#reload}.
	 *
	 * <p>
	 * As the default implementation of {@link CacheLoader#reload} is synchronous, it is
	 * recommended that users of this method override {@link CacheLoader#reload} with an
	 * asynchronous implementation; otherwise refreshes will be performed during unrelated
	 * cache read and write operations.
	 *
	 * <p>
	 * Currently automatic refreshes are performed when the first stale request for an
	 * entry occurs. The request triggering refresh will make a synchronous call to
	 * {@link CacheLoader#reload} and immediately return the new value if the returned
	 * future is complete, and the old value otherwise.
	 *
	 * <p>
	 * <b>Note:</b> <i>all exceptions thrown during refresh will be logged and then
	 * swallowed</i>.
	 *
	 * <p>
	 * If you can represent the duration as a {@link Duration} (which should be preferred
	 * when feasible), use {@link #refreshAfterWrite(Duration)} instead.
	 * @param duration the length of time after an entry is created that it should be
	 * considered stale, and thus eligible for refresh
	 * @param unit the unit that {@code duration} is expressed in
	 * @return this {@code CacheBuilder} instance (for chaining)
	 * @throws IllegalArgumentException if {@code duration} is negative
	 * @throws IllegalStateException if {@link #refreshAfterWrite} was already set
	 * @since 11.0
	 */
	// should accept a Duration
	public CacheBuilder<K, V> refreshAfterWrite(long duration, TimeUnit unit) {
		checkNotNull(unit);
		checkState(refreshNanos == UNSET_INT, "refresh was already set to %s ns", refreshNanos);
		checkArgument(duration > 0, "duration must be positive: %s %s", duration, unit);
		refreshNanos = unit.toNanos(duration);
		return this;
	}

	// nanos internally, should be Duration
	long getRefreshNanos() {
		return (refreshNanos == UNSET_INT) ? DEFAULT_REFRESH_NANOS : refreshNanos;
	}

	/**
	 * Specifies a nanosecond-precision time source for this cache. By default,
	 * {@link System#nanoTime} is used.
	 *
	 * <p>
	 * The primary intent of this method is to facilitate testing of caches with a fake or
	 * mock time source.
	 * @return this {@code CacheBuilder} instance (for chaining)
	 * @throws IllegalStateException if a ticker was already set
	 */
	public CacheBuilder<K, V> ticker(Ticker ticker) {
		checkState(this.ticker == null);
		this.ticker = checkNotNull(ticker);
		return this;
	}

	Ticker getTicker(boolean recordsTime) {
		if (ticker != null) {
			return ticker;
		}
		return recordsTime ? Ticker.systemTicker() : NULL_TICKER;
	}

	/**
	 * Specifies a listener instance that caches should notify each time an entry is
	 * removed for any {@linkplain RemovalCause reason}. Each cache created by this
	 * builder will invoke this listener as part of the routine maintenance described in
	 * the class documentation above.
	 *
	 * <p>
	 * <b>Warning:</b> after invoking this method, do not continue to use <i>this</i>
	 * cache builder reference; instead use the reference this method <i>returns</i>. At
	 * runtime, these point to the same instance, but only the returned reference has the
	 * correct generic type information so as to ensure type safety. For best results, use
	 * the standard method-chaining idiom illustrated in the class documentation above,
	 * configuring a builder and building your cache in a single statement. Failure to
	 * heed this advice can result in a {@link ClassCastException} being thrown by a cache
	 * operation at some <i>undefined</i> point in the future.
	 *
	 * <p>
	 * <b>Warning:</b> any exception thrown by {@code listener} will <i>not</i> be
	 * propagated to the {@code Cache} user, only logged via a {@link Logger}.
	 * @return the cache builder reference that should be used instead of {@code this} for
	 * any remaining configuration and cache building
	 * @return this {@code CacheBuilder} instance (for chaining)
	 * @throws IllegalStateException if a removal listener was already set
	 */
	@CheckReturnValue
	public <K1 extends K, V1 extends V> CacheBuilder<K1, V1> removalListener(
			RemovalListener<? super K1, ? super V1> listener) {
		checkState(removalListener == null);

		// safely limiting the kinds of caches this can produce
		CacheBuilder<K1, V1> me = (CacheBuilder<K1, V1>) this;
		me.removalListener = checkNotNull(listener);
		return me;
	}

	// Make a safe contravariant cast now so we don't have to do it over and over.
	<K1 extends K, V1 extends V> RemovalListener<K1, V1> getRemovalListener() {
		return (RemovalListener<K1, V1>) MoreObjects.firstNonNull(removalListener, NullListener.INSTANCE);
	}

	/**
	 * Enable the accumulation of {@link CacheStats} during the operation of the cache.
	 * Without this {@link Cache#stats} will return zero for all statistics. Note that
	 * recording stats requires bookkeeping to be performed with each operation, and thus
	 * imposes a performance penalty on cache operation.
	 * @return this {@code CacheBuilder} instance (for chaining)
	 * @since 12.0 (previously, stats collection was automatic)
	 */
	public CacheBuilder<K, V> recordStats() {
		statsCounterSupplier = CACHE_STATS_COUNTER;
		return this;
	}

	boolean isRecordingStats() {
		return statsCounterSupplier == CACHE_STATS_COUNTER;
	}

	Supplier<? extends AbstractCache.StatsCounter> getStatsCounterSupplier() {
		return statsCounterSupplier;
	}

	/**
	 * Builds a cache, which either returns an already-loaded value for a given key or
	 * atomically computes or retrieves it using the supplied {@code CacheLoader}. If
	 * another thread is currently loading the value for this key, simply waits for that
	 * thread to finish and returns its loaded value. Note that multiple threads can
	 * concurrently load values for distinct keys.
	 *
	 * <p>
	 * This method does not alter the state of this {@code CacheBuilder} instance, so it
	 * can be invoked again to create multiple independent caches.
	 * @param loader the cache loader used to obtain new values
	 * @return a cache having the requested features
	 */
	@CheckReturnValue
	public <K1 extends K, V1 extends V> LoadingCache<K1, V1> build(CacheLoader<? super K1, V1> loader) {
		checkWeightWithWeigher();
		return new LocalCache.LocalLoadingCache<>(this, loader);
	}

	/**
	 * Builds a cache which does not automatically load values when keys are requested.
	 *
	 * <p>
	 * Consider {@link #build(CacheLoader)} instead, if it is feasible to implement a
	 * {@code
	 * CacheLoader}.
	 *
	 * <p>
	 * This method does not alter the state of this {@code CacheBuilder} instance, so it
	 * can be invoked again to create multiple independent caches.
	 * @return a cache having the requested features
	 * @since 11.0
	 */
	@CheckReturnValue
	public <K1 extends K, V1 extends V> Cache<K1, V1> build() {
		checkWeightWithWeigher();
		checkNonLoadingCache();
		return new LocalCache.LocalManualCache<>(this);
	}

	private void checkNonLoadingCache() {
		checkState(refreshNanos == UNSET_INT, "refreshAfterWrite requires a LoadingCache");
	}

	private void checkWeightWithWeigher() {
		if (weigher == null) {
			checkState(maximumWeight == UNSET_INT, "maximumWeight requires weigher");
		}
		else {
			if (strictParsing) {
				checkState(maximumWeight != UNSET_INT, "weigher requires maximumWeight");
			}
			else {
				if (maximumWeight == UNSET_INT) {
					logger.log(Level.WARNING, "ignoring weigher specified without maximumWeight");
				}
			}
		}
	}

	/**
	 * Returns a string representation for this CacheBuilder instance. The exact form of
	 * the returned string is not specified.
	 */
	@Override
	public String toString() {
		MoreObjects.ToStringHelper s = MoreObjects.toStringHelper(this);
		if (initialCapacity != UNSET_INT) {
			s.add("initialCapacity", initialCapacity);
		}
		if (concurrencyLevel != UNSET_INT) {
			s.add("concurrencyLevel", concurrencyLevel);
		}
		if (maximumSize != UNSET_INT) {
			s.add("maximumSize", maximumSize);
		}
		if (maximumWeight != UNSET_INT) {
			s.add("maximumWeight", maximumWeight);
		}
		if (expireAfterWriteNanos != UNSET_INT) {
			s.add("expireAfterWrite", expireAfterWriteNanos + "ns");
		}
		if (expireAfterAccessNanos != UNSET_INT) {
			s.add("expireAfterAccess", expireAfterAccessNanos + "ns");
		}
		if (keyStrength != null) {
			s.add("keyStrength", Ascii.toLowerCase(keyStrength.toString()));
		}
		if (valueStrength != null) {
			s.add("valueStrength", Ascii.toLowerCase(valueStrength.toString()));
		}
		if (keyEquivalence != null) {
			s.addValue("keyEquivalence");
		}
		if (valueEquivalence != null) {
			s.addValue("valueEquivalence");
		}
		if (removalListener != null) {
			s.addValue("removalListener");
		}
		return s.toString();
	}

	enum NullListener implements RemovalListener<Object, Object> {

		INSTANCE;

		@Override
		public void onRemoval(RemovalNotification<Object, Object> notification) {
		}

	}

	enum OneWeigher implements Weigher<Object, Object> {

		INSTANCE;

		@Override
		public int weigh(Object key, Object value) {
			return 1;
		}

	}

}
