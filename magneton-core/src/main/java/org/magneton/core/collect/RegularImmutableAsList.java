/*
 * Copyright (C) 2012 The Guava Authors
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

import java.util.function.Consumer;

import javax.annotation.CheckForNull;
import javax.annotation.Nullable;

/**
 * An {@link org.magneton.core.collect.ImmutableAsList} implementation specialized for
 * when the delegate collection is already backed by an {@code ImmutableList} or array.
 *
 * @author Louis Wasserman
 */

// uses writeReplace, not default serialization
@ElementTypesAreNonnullByDefault
class RegularImmutableAsList<E> extends org.magneton.core.collect.ImmutableAsList<E> {

	private final org.magneton.core.collect.ImmutableCollection<E> delegate;

	private final org.magneton.core.collect.ImmutableList<? extends E> delegateList;

	RegularImmutableAsList(org.magneton.core.collect.ImmutableCollection<E> delegate,
			org.magneton.core.collect.ImmutableList<? extends E> delegateList) {
		this.delegate = delegate;
		this.delegateList = delegateList;
	}

	RegularImmutableAsList(org.magneton.core.collect.ImmutableCollection<E> delegate, Object[] array) {
		this(delegate, org.magneton.core.collect.ImmutableList.<E>asImmutableList(array));
	}

	@Override
	ImmutableCollection<E> delegateCollection() {
		return delegate;
	}

	ImmutableList<? extends E> delegateList() {
		return delegateList;
	}

	// safe covariant cast!
	@Override
	public UnmodifiableListIterator<E> listIterator(int index) {
		return (UnmodifiableListIterator<E>) delegateList.listIterator(index);
	}

	@Override
	public void forEach(Consumer<? super E> action) {
		delegateList.forEach(action);
	}

	@Override
	int copyIntoArray(@Nullable Object[] dst, int offset) {
		return delegateList.copyIntoArray(dst, offset);
	}

	@Override
	@CheckForNull
	Object[] internalArray() {
		return delegateList.internalArray();
	}

	@Override
	int internalArrayStart() {
		return delegateList.internalArrayStart();
	}

	@Override
	int internalArrayEnd() {
		return delegateList.internalArrayEnd();
	}

	@Override
	public E get(int index) {
		return delegateList.get(index);
	}

}
