/*
 * Copyright (C) 2009 The Guava Authors
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

import java.util.Collections;
import java.util.Spliterator;

/**
 * Implementation of {@link org.magneton.core.collect.ImmutableList} with exactly one
 * element.
 *
 * @author Hayward Chan
 */

// uses writeReplace(), not default serialization
@ElementTypesAreNonnullByDefault
final class SingletonImmutableList<E> extends org.magneton.core.collect.ImmutableList<E> {

	final transient E element;

	SingletonImmutableList(E element) {
		this.element = org.magneton.core.base.Preconditions.checkNotNull(element);
	}

	@Override
	public E get(int index) {
		org.magneton.core.base.Preconditions.checkElementIndex(index, 1);
		return element;
	}

	@Override
	public UnmodifiableIterator<E> iterator() {
		return Iterators.singletonIterator(element);
	}

	@Override
	public Spliterator<E> spliterator() {
		return Collections.singleton(element).spliterator();
	}

	@Override
	public int size() {
		return 1;
	}

	@Override
	public org.magneton.core.collect.ImmutableList<E> subList(int fromIndex, int toIndex) {
		org.magneton.core.base.Preconditions.checkPositionIndexes(fromIndex, toIndex, 1);
		return (fromIndex == toIndex) ? ImmutableList.<E>of() : this;
	}

	@Override
	public String toString() {
		return '[' + element.toString() + ']';
	}

	@Override
	boolean isPartialView() {
		return false;
	}

}
