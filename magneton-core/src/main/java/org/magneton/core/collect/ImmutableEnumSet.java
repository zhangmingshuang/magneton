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

import java.io.Serializable;
import java.util.Collection;
import java.util.EnumSet;
import java.util.Spliterator;
import java.util.function.Consumer;

import javax.annotation.CheckForNull;

import javax.annotation.concurrent.LazyInit;

/**
 * Implementation of {@link org.magneton.core.collect.ImmutableSet} backed by a non-empty
 * {@link EnumSet}.
 *
 * @author Jared Levy
 */
// we're overriding default serialization
@ElementTypesAreNonnullByDefault
final class ImmutableEnumSet<E extends Enum<E>> extends org.magneton.core.collect.ImmutableSet<E> {

	/*
	 * Notes on EnumSet and <E extends Enum<E>>:
	 *
	 * This class isn't an arbitrary ForwardingImmutableSet because we need to know that
	 * calling {@code clone()} during deserialization will return an object that no one
	 * else has a reference to, allowing us to guarantee immutability. Hence, we support
	 * only {@link EnumSet}.
	 */
	private final transient EnumSet<E> delegate;

	@LazyInit
	private transient int hashCode;

	private ImmutableEnumSet(EnumSet<E> delegate) {
		this.delegate = delegate;
	}

	// necessary to compile against Java 8
	static org.magneton.core.collect.ImmutableSet asImmutable(EnumSet set) {
		switch (set.size()) {
		case 0:
			return org.magneton.core.collect.ImmutableSet.of();
		case 1:
			return ImmutableSet.of(Iterables.getOnlyElement(set));
		default:
			return new ImmutableEnumSet(set);
		}
	}

	@Override
	boolean isPartialView() {
		return false;
	}

	@Override
	public UnmodifiableIterator<E> iterator() {
		return Iterators.unmodifiableIterator(delegate.iterator());
	}

	@Override
	public Spliterator<E> spliterator() {
		return delegate.spliterator();
	}

	@Override
	public void forEach(Consumer<? super E> action) {
		delegate.forEach(action);
	}

	@Override
	public int size() {
		return delegate.size();
	}

	@Override
	public boolean contains(@CheckForNull Object object) {
		return delegate.contains(object);
	}

	@Override
	public boolean containsAll(Collection<?> collection) {
		if (collection instanceof ImmutableEnumSet<?>) {
			collection = ((ImmutableEnumSet<?>) collection).delegate;
		}
		return delegate.containsAll(collection);
	}

	@Override
	public boolean isEmpty() {
		return delegate.isEmpty();
	}

	@Override
	public boolean equals(@CheckForNull Object object) {
		if (object == this) {
			return true;
		}
		if (object instanceof ImmutableEnumSet) {
			object = ((ImmutableEnumSet<?>) object).delegate;
		}
		return delegate.equals(object);
	}

	@Override
	boolean isHashCodeFast() {
		return true;
	}

	@Override
	public int hashCode() {
		int result = hashCode;
		return (result == 0) ? hashCode = delegate.hashCode() : result;
	}

	@Override
	public String toString() {
		return delegate.toString();
	}

	// All callers of the constructor are restricted to <E extends Enum<E>>.
	@Override
	Object writeReplace() {
		return new EnumSerializedForm<E>(delegate);
	}

	/*
	 * This class is used to serialize ImmutableEnumSet instances.
	 */
	private static class EnumSerializedForm<E extends Enum<E>> implements Serializable {

		private static final long serialVersionUID = 0;

		final EnumSet<E> delegate;

		EnumSerializedForm(EnumSet<E> delegate) {
			this.delegate = delegate;
		}

		Object readResolve() {
			// EJ2 #76: Write readObject() methods defensively.
			return new ImmutableEnumSet<E>(delegate.clone());
		}

	}

}
