/*
 * Copyright (C) 2018 The Guava Authors
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

import java.util.Spliterator;
import java.util.function.Consumer;

import javax.annotation.Nullable;

import org.magneton.core.base.Preconditions;

@ElementTypesAreNonnullByDefault
abstract class IndexedImmutableSet<E> extends ImmutableSet.CachingAsList<E> {

	abstract E get(int index);

	@Override
	public UnmodifiableIterator<E> iterator() {
		return asList().iterator();
	}

	@Override
	public Spliterator<E> spliterator() {
		return org.magneton.core.collect.CollectSpliterators.indexed(size(), SPLITERATOR_CHARACTERISTICS, this::get);
	}

	@Override
	public void forEach(Consumer<? super E> consumer) {
		Preconditions.checkNotNull(consumer);
		int n = size();
		for (int i = 0; i < n; i++) {
			consumer.accept(get(i));
		}
	}

	@Override

	int copyIntoArray(@Nullable Object[] dst, int offset) {
		return asList().copyIntoArray(dst, offset);
	}

	@Override
	ImmutableList<E> createAsList() {
		return new org.magneton.core.collect.ImmutableAsList<E>() {
			@Override
			public E get(int index) {
				return IndexedImmutableSet.this.get(index);
			}

			@Override
			boolean isPartialView() {
				return IndexedImmutableSet.this.isPartialView();
			}

			@Override
			public int size() {
				return IndexedImmutableSet.this.size();
			}

			@Override
			ImmutableCollection<E> delegateCollection() {
				return IndexedImmutableSet.this;
			}
		};
	}

}
