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
import java.util.Spliterator;
import java.util.function.Consumer;

import javax.annotation.CheckForNull;

import org.magneton.core.base.Preconditions;

/**
 * {@code keySet()} implementation for {@link org.magneton.core.collect.ImmutableMap}.
 *
 * @author Jesse Wilson
 * @author Kevin Bourrillion
 */
@ElementTypesAreNonnullByDefault
final class ImmutableMapKeySet<K, V> extends IndexedImmutableSet<K> {

	private final org.magneton.core.collect.ImmutableMap<K, V> map;

	ImmutableMapKeySet(org.magneton.core.collect.ImmutableMap<K, V> map) {
		this.map = map;
	}

	@Override
	public int size() {
		return map.size();
	}

	@Override
	public UnmodifiableIterator<K> iterator() {
		return map.keyIterator();
	}

	@Override
	public Spliterator<K> spliterator() {
		return map.keySpliterator();
	}

	@Override
	public boolean contains(@CheckForNull Object object) {
		return map.containsKey(object);
	}

	@Override
	K get(int index) {
		return map.entrySet().asList().get(index).getKey();
	}

	@Override
	public void forEach(Consumer<? super K> action) {
		Preconditions.checkNotNull(action);
		map.forEach((k, v) -> action.accept(k));
	}

	@Override
	boolean isPartialView() {
		return true;
	}

	// No longer used for new writes, but kept so that old data can still be read.

	private static class KeySetSerializedForm<K> implements Serializable {

		private static final long serialVersionUID = 0;

		final org.magneton.core.collect.ImmutableMap<K, ?> map;

		KeySetSerializedForm(ImmutableMap<K, ?> map) {
			this.map = map;
		}

		Object readResolve() {
			return map.keySet();
		}

	}

}
