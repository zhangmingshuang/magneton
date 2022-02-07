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

/**
 * This package contains generic collection interfaces and implementations, and other
 * utilities for working with collections. It is a part of the open-source
 * <a href="http://github.com/google/guava">Guava</a> library.
 *
 * <h2>Collection Types</h2>
 *
 * <dl>
 * <dt>{@link org.magneton.core.collect.BiMap}
 * <dd>An extension of {@link java.util.Map} that guarantees the uniqueness of its values
 * as well as that of its keys. This is sometimes called an "invertible map," since the
 * restriction on values enables it to support an
 * {@linkplain org.magneton.core.collect.BiMap#inverse inverse view} -- which is another
 * instance of {@code BiMap}.
 * <dt>{@link org.magneton.core.collect.Multiset}
 * <dd>An extension of {@link java.util.Collection} that may contain duplicate values like
 * a {@link java.util.List}, yet has order-independent equality like a
 * {@link java.util.Set}. One typical use for a multiset is to represent a histogram.
 * <dt>{@link org.magneton.core.collect.Multimap}
 * <dd>A new type, which is similar to {@link java.util.Map}, but may contain multiple
 * entries with the same key. Some behaviors of {@link org.magneton.core.collect.Multimap}
 * are left unspecified and are provided only by the subtypes mentioned below.
 * <dt>{@link org.magneton.core.collect.ListMultimap}
 * <dd>An extension of {@link org.magneton.core.collect.Multimap} which permits duplicate
 * entries, supports random access of values for a particular key, and has <i>partially
 * order-dependent equality</i> as defined by
 * {@link org.magneton.core.collect.ListMultimap#equals(Object)}. {@code ListMultimap}
 * takes its name from the fact that the
 * {@linkplain org.magneton.core.collect.ListMultimap#get collection of values} associated
 * with a given key fulfills the {@link java.util.List} contract.
 * <dt>{@link org.magneton.core.collect.SetMultimap}
 * <dd>An extension of {@link org.magneton.core.collect.Multimap} which has
 * order-independent equality and does not allow duplicate entries; that is, while a key
 * may appear twice in a {@code SetMultimap}, each must map to a different value.
 * {@code SetMultimap} takes its name from the fact that the
 * {@linkplain org.magneton.core.collect.SetMultimap#get collection of values} associated
 * with a given key fulfills the {@link java.util.Set} contract.
 * <dt>{@link org.magneton.core.collect.SortedSetMultimap}
 * <dd>An extension of {@link org.magneton.core.collect.SetMultimap} for which the
 * {@linkplain org.magneton.core.collect.SortedSetMultimap#get collection values}
 * associated with a given key is a {@link java.util.SortedSet}.
 * <dt>{@link org.magneton.core.collect.Table}
 * <dd>A new type, which is similar to {@link java.util.Map}, but which indexes its values
 * by an ordered pair of keys, a row key and column key.
 * <dt>{@link org.magneton.core.collect.ClassToInstanceMap}
 * <dd>An extension of {@link java.util.Map} that associates a raw type with an instance
 * of that type.
 * </dl>
 *
 * <h2>Collection Implementations</h2>
 *
 * <h3>of {@link java.util.List}</h3>
 *
 * <ul>
 * <li>{@link org.magneton.core.collect.ImmutableList}
 * </ul>
 *
 * <h3>of {@link java.util.Set}</h3>
 *
 * <ul>
 * <li>{@link org.magneton.core.collect.ImmutableSet}
 * <li>{@link org.magneton.core.collect.ImmutableSortedSet}
 * <li>{@link org.magneton.core.collect.ContiguousSet} (see {@code Range})
 * </ul>
 *
 * <h3>of {@link java.util.Map}</h3>
 *
 * <ul>
 * <li>{@link org.magneton.core.collect.ImmutableMap}
 * <li>{@link org.magneton.core.collect.ImmutableSortedMap}
 * <li>{@link org.magneton.core.collect.MapMaker}
 * </ul>
 *
 * <h3>of {@link org.magneton.core.collect.BiMap}</h3>
 *
 * <ul>
 * <li>{@link org.magneton.core.collect.ImmutableBiMap}
 * <li>{@link org.magneton.core.collect.HashBiMap}
 * <li>{@link org.magneton.core.collect.EnumBiMap}
 * <li>{@link org.magneton.core.collect.EnumHashBiMap}
 * </ul>
 *
 * <h3>of {@link org.magneton.core.collect.Multiset}</h3>
 *
 * <ul>
 * <li>{@link org.magneton.core.collect.ImmutableMultiset}
 * <li>{@link org.magneton.core.collect.ImmutableSortedMultiset}
 * <li>{@link org.magneton.core.collect.HashMultiset}
 * <li>{@link org.magneton.core.collect.LinkedHashMultiset}
 * <li>{@link org.magneton.core.collect.TreeMultiset}
 * <li>{@link org.magneton.core.collect.EnumMultiset}
 * <li>{@link org.magneton.core.collect.ConcurrentHashMultiset}
 * </ul>
 *
 * <h3>of {@link org.magneton.core.collect.Multimap}</h3>
 *
 * <ul>
 * <li>{@link org.magneton.core.collect.ImmutableMultimap}
 * <li>{@link org.magneton.core.collect.ImmutableListMultimap}
 * <li>{@link org.magneton.core.collect.ImmutableSetMultimap}
 * <li>{@link org.magneton.core.collect.ArrayListMultimap}
 * <li>{@link org.magneton.core.collect.HashMultimap}
 * <li>{@link org.magneton.core.collect.TreeMultimap}
 * <li>{@link org.magneton.core.collect.LinkedHashMultimap}
 * <li>{@link org.magneton.core.collect.LinkedListMultimap}
 * </ul>
 *
 * <h3>of {@link org.magneton.core.collect.Table}</h3>
 *
 * <ul>
 * <li>{@link org.magneton.core.collect.ImmutableTable}
 * <li>{@link org.magneton.core.collect.ArrayTable}
 * <li>{@link org.magneton.core.collect.HashBasedTable}
 * <li>{@link org.magneton.core.collect.TreeBasedTable}
 * </ul>
 *
 * <h3>of {@link org.magneton.core.collect.ClassToInstanceMap}</h3>
 *
 * <ul>
 * <li>{@link org.magneton.core.collect.ImmutableClassToInstanceMap}
 * <li>{@link org.magneton.core.collect.MutableClassToInstanceMap}
 * </ul>
 *
 * <h2>Classes of static utility methods</h2>
 *
 * <ul>
 * <li>{@link org.magneton.core.collect.Collections2}
 * <li>{@link org.magneton.core.collect.Iterators}
 * <li>{@link org.magneton.core.collect.Iterables}
 * <li>{@link org.magneton.core.collect.Lists}
 * <li>{@link org.magneton.core.collect.Maps}
 * <li>{@link org.magneton.core.collect.Queues}
 * <li>{@link org.magneton.core.collect.Sets}
 * <li>{@link org.magneton.core.collect.Multisets}
 * <li>{@link org.magneton.core.collect.Multimaps}
 * <li>{@link org.magneton.core.collect.Tables}
 * <li>{@link org.magneton.core.collect.ObjectArrays}
 * </ul>
 *
 * <h2>Comparison</h2>
 *
 * <ul>
 * <li>{@link org.magneton.core.collect.Ordering}
 * <li>{@link org.magneton.core.collect.ComparisonChain}
 * </ul>
 *
 * <h2>Abstract implementations</h2>
 *
 * <ul>
 * <li>{@link org.magneton.core.collect.AbstractIterator}
 * <li>{@link org.magneton.core.collect.AbstractSequentialIterator}
 * <li>{@link org.magneton.core.collect.ImmutableCollection}
 * <li>{@link org.magneton.core.collect.UnmodifiableIterator}
 * <li>{@link org.magneton.core.collect.UnmodifiableListIterator}
 * </ul>
 *
 * <h2>Ranges</h2>
 *
 * <ul>
 * <li>{@link org.magneton.core.collect.Range}
 * <li>{@link org.magneton.core.collect.RangeMap}
 * <li>{@link org.magneton.core.collect.DiscreteDomain}
 * <li>{@link org.magneton.core.collect.ContiguousSet}
 * </ul>
 *
 * <h2>Other</h2>
 *
 * <ul>
 * <li>{@link org.magneton.core.collect.Interner},
 * {@link org.magneton.core.collect.Interners}
 * <li>{@link org.magneton.core.collect.MapDifference},
 * {@link org.magneton.core.collect.SortedMapDifference}
 * <li>{@link org.magneton.core.collect.MinMaxPriorityQueue}
 * <li>{@link org.magneton.core.collect.PeekingIterator}
 * </ul>
 *
 * <h2>Forwarding collections</h2>
 *
 * <ul>
 * <li>{@link org.magneton.core.collect.ForwardingCollection}
 * <li>{@link org.magneton.core.collect.ForwardingConcurrentMap}
 * <li>{@link org.magneton.core.collect.ForwardingIterator}
 * <li>{@link org.magneton.core.collect.ForwardingList}
 * <li>{@link org.magneton.core.collect.ForwardingListIterator}
 * <li>{@link org.magneton.core.collect.ForwardingListMultimap}
 * <li>{@link org.magneton.core.collect.ForwardingMap}
 * <li>{@link org.magneton.core.collect.ForwardingMapEntry}
 * <li>{@link org.magneton.core.collect.ForwardingMultimap}
 * <li>{@link org.magneton.core.collect.ForwardingMultiset}
 * <li>{@link org.magneton.core.collect.ForwardingNavigableMap}
 * <li>{@link org.magneton.core.collect.ForwardingNavigableSet}
 * <li>{@link org.magneton.core.collect.ForwardingObject}
 * <li>{@link org.magneton.core.collect.ForwardingQueue}
 * <li>{@link org.magneton.core.collect.ForwardingSet}
 * <li>{@link org.magneton.core.collect.ForwardingSetMultimap}
 * <li>{@link org.magneton.core.collect.ForwardingSortedMap}
 * <li>{@link org.magneton.core.collect.ForwardingSortedMultiset}
 * <li>{@link org.magneton.core.collect.ForwardingSortedSet}
 * <li>{@link org.magneton.core.collect.ForwardingSortedSetMultimap}
 * <li>{@link org.magneton.core.collect.ForwardingTable}
 * </ul>
 */
@CheckReturnValue
@ParametersAreNonnullByDefault
package org.magneton.core.collect;

import javax.annotation.ParametersAreNonnullByDefault;

import com.google.errorprone.annotations.CheckReturnValue;
