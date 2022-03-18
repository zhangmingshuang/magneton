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

import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.io.Serializable;

import javax.annotation.CheckForNull;

/**
 * List returned by {@link ImmutableCollection#asList} that delegates {@code contains}
 * checks to the backing collection.
 *
 * @author Jared Levy
 * @author Louis Wasserman
 */
@ElementTypesAreNonnullByDefault
abstract class ImmutableAsList<E> extends ImmutableList<E> {

	abstract ImmutableCollection<E> delegateCollection();

	@Override
	public boolean contains(@CheckForNull Object target) {
		// The collection's contains() is at least as fast as ImmutableList's
		// and is often faster.
		return delegateCollection().contains(target);
	}

	@Override
	public int size() {
		return delegateCollection().size();
	}

	@Override
	public boolean isEmpty() {
		return delegateCollection().isEmpty();
	}

	@Override
	boolean isPartialView() {
		return delegateCollection().isPartialView();
	}

	private void readObject(ObjectInputStream stream) throws InvalidObjectException {
		throw new InvalidObjectException("Use SerializedForm");
	}

	@Override
	Object writeReplace() {
		return new SerializedForm(delegateCollection());
	}

	/** Serialized form that leads to the same performance as the original list. */
	static class SerializedForm implements Serializable {

		private static final long serialVersionUID = 0;

		final ImmutableCollection<?> collection;

		SerializedForm(ImmutableCollection<?> collection) {
			this.collection = collection;
		}

		Object readResolve() {
			return collection.asList();
		}

	}

}
