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

import java.util.function.BiConsumer;

import javax.annotation.CheckForNull;
import javax.annotations.LazyInit;

import org.magneton.core.base.Preconditions;

import static org.magneton.core.collect.CollectPreconditions.checkEntryNotNull;

/**
 * Implementation of {@link ImmutableMap} with exactly one entry.
 *
 * @author Jesse Wilson
 * @author Kevin Bourrillion
 */

// uses writeReplace(), not default serialization
@ElementTypesAreNonnullByDefault
final class SingletonImmutableBiMap<K, V> extends org.magneton.core.collect.ImmutableBiMap<K, V> {

	final transient K singleKey;

	final transient V singleValue;

	@CheckForNull
	private final transient org.magneton.core.collect.ImmutableBiMap<V, K> inverse;

	@LazyInit
	@CheckForNull
	private transient org.magneton.core.collect.ImmutableBiMap<V, K> lazyInverse;

	SingletonImmutableBiMap(K singleKey, V singleValue) {
		checkEntryNotNull(singleKey, singleValue);
		this.singleKey = singleKey;
		this.singleValue = singleValue;
		inverse = null;
	}

	private SingletonImmutableBiMap(K singleKey, V singleValue,
			org.magneton.core.collect.ImmutableBiMap<V, K> inverse) {
		this.singleKey = singleKey;
		this.singleValue = singleValue;
		this.inverse = inverse;
	}

	@Override
	@CheckForNull
	public V get(@CheckForNull Object key) {
		return singleKey.equals(key) ? singleValue : null;
	}

	@Override
	public int size() {
		return 1;
	}

	@Override
	public void forEach(BiConsumer<? super K, ? super V> action) {
		Preconditions.checkNotNull(action).accept(singleKey, singleValue);
	}

	@Override
	public boolean containsKey(@CheckForNull Object key) {
		return singleKey.equals(key);
	}

	@Override
	public boolean containsValue(@CheckForNull Object value) {
		return singleValue.equals(value);
	}

	@Override
	boolean isPartialView() {
		return false;
	}

	@Override
	org.magneton.core.collect.ImmutableSet<Entry<K, V>> createEntrySet() {
		return org.magneton.core.collect.ImmutableSet.of(Maps.immutableEntry(singleKey, singleValue));
	}

	@Override
	org.magneton.core.collect.ImmutableSet<K> createKeySet() {
		return ImmutableSet.of(singleKey);
	}

	@Override
	public org.magneton.core.collect.ImmutableBiMap<V, K> inverse() {
		if (inverse != null) {
			return inverse;
		}
		else {
			// racy single-check idiom
			ImmutableBiMap<V, K> result = lazyInverse;
			if (result == null) {
				return lazyInverse = new SingletonImmutableBiMap<>(singleValue, singleKey, this);
			}
			else {
				return result;
			}
		}
	}

}
